package com.gm.wordsyn.service;

import com.gm.wordsyn.dao.CompareDAO;
import com.gm.wordsyn.dao.DocFileDAO;
import com.gm.wordsyn.dao.UserDAO;
import com.gm.wordsyn.entity.CompareFile;
import com.gm.wordsyn.entity.DocFile;
import com.gm.wordsyn.util.XmlReader;
import com.gm.wordsyn.webService.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocFileService {
    @Autowired
    DocFileDAO docFileDAO;
    @Autowired
    CompareDAO compareDAO;

    @Autowired
    UserDAO userDAO;

    public List<DocFile> findAll() {
        return docFileDAO.findAll();
    }


    /**
     * 从数据库中得到doc文件id对应的xml文件，读取xml文件为String
     *
     * @param docFileId
     * @return
     */
    public String docFileOnXml(int docFileId) {
        String docFileXmlLocation = docFileDAO.findXmlFileLocationFromId(docFileId);
        String randomFileName = docFileXmlLocation.substring(12);
        WebService webService = new WebService();
        byte[] buffer = webService.downLoadFile(randomFileName);
        XmlReader xmlReader = new XmlReader();
        return xmlReader.getXmlString(buffer);
    }

    /**
     * 从数据库中得到doc文件对应的，经过比较的，id对应的xml文件，读取xml文件为String
     *
     * @param docFileId
     * @return
     */
    public String docCompareFileOnXml(int docFileId) {
        String docCompareFileXmlLocation = docFileDAO.findXmlCompareFileLocationFromId(docFileId);
        String randomFileName = docCompareFileXmlLocation.substring(12);
        WebService webService = new WebService();
        byte[] buffer = webService.downLoadFile(randomFileName);
        XmlReader xmlReader = new XmlReader();
        return xmlReader.getXmlString(buffer);
    }

    /**
     * 添加被测文件信息至数据库
     *
     * @param fileName
     * @param fileLocation
     * @param updateTime
     * @param uploader
     * @param modelFileId
     * @return
     */
    public boolean addDocFile(String fileName, String fileLocation, String updateTime, String uploader,
                              int modelFileId) {
        int uploaderId = userDAO.findUserIdByUsername(uploader);
        DocFile docFile = new DocFile();
        docFile = docFileDAO.save(new DocFile(fileName, fileLocation, updateTime, uploader, uploaderId));
        if (docFile != null) {
            compareDAO.save(new CompareFile(docFile.getId(), modelFileId));
            return true;
        } else {
            return false;
        }
    }

    /**
     * 分析被测文件
     *
     * @param id
     * @return
     */
    public boolean anyDocFileById(int id) {
        WebService webService = new WebService();
        DocFile docFile = docFileDAO.findById(id).get();
        String fileLocation = docFile.getFileLocation();
        String fileName = docFile.getFileName();
        webService.anyDocFile(fileLocation);
        docFileDAO.updateXmlFileLocationAndXmlFileName(fileLocation + ".xml", fileName + ".xml", id);
        return true;
    }

    /**
     * 删除被测文档
     *
     * @param id
     * @return
     */
    public boolean deleteDocFile(int id) {
        WebService webService = new WebService();
        DocFile docFile = docFileDAO.findById(id).get();
        String fileName = docFile.getFileLocation().substring(12);
        webService.deleteFile(fileName);
        String xmlFileLocation = docFile.getXmlFileLocation();

        if (xmlFileLocation != null) {
            String deleteFileName = xmlFileLocation.substring(12);
            webService.deleteFile(deleteFileName);
        }
        String xmlCompareFileLocation = compareDAO.findXmlCompareFileLocationByDocId(id);
        if (xmlCompareFileLocation != null) {
            String deleteFileName = xmlCompareFileLocation.substring(12);
            webService.deleteFile(deleteFileName);
        }
        String docWordCompareFilePath = compareDAO.findCompareDocFilePathFromDocId(id);
        if (docWordCompareFilePath != null) {
            String deleteFileName = docWordCompareFilePath.substring(12);
            webService.deleteFile(deleteFileName);
        }
        compareDAO.deleteAllDocDataByDocId(id);
        docFileDAO.deleteDocDataByDocId(id);
        return true;
    }

    /**
     * 下载被测文档
     *
     * @param filePath
     * @return
     */
    public byte[] downloadDocFile(String filePath) {
        String fileName = filePath.substring(12);
        WebService webService = new WebService();
        byte[] fileByte = webService.downloadFile(fileName);
        return fileByte;
    }

}
