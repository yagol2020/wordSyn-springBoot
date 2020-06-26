package com.gm.wordsyn.service;

import com.gm.wordsyn.dao.CompareDAO;
import com.gm.wordsyn.dao.DocFileDAO;
import com.gm.wordsyn.dao.ModelFileDAO;
import com.gm.wordsyn.dao.UserDAO;
import com.gm.wordsyn.entity.DocFile;
import com.gm.wordsyn.entity.ModelFile;
import com.gm.wordsyn.util.XmlReader;
import com.gm.wordsyn.webService.WebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ModelFileService {
    @Autowired
    ModelFileDAO modelFileDAO;
    @Autowired
    DocFileDAO docFileDAO;
    @Autowired
    CompareDAO compareDAO;
    @Autowired
    UserDAO userDAO;

    public List<ModelFile> findAll() {
        return modelFileDAO.findAll();
    }

    public ModelFile findOneById(int id) {
        return modelFileDAO.findById(id).get();
    }

    /**
     * 从数据库中得到model文件id对应的xml文件，解析为json的string
     *
     * @param modelFileId
     * @return
     */
    public String modelFileOnXmlJson(int modelFileId) {
        String modelFileXmlLocation = modelFileDAO.findXmlFileLocationFromId(modelFileId);
        String randomFileName = modelFileXmlLocation.substring(12);
        //log.info(randomFileName);
        WebService webService = new WebService();
        byte[] buffer = webService.downLoadFile(randomFileName);
        XmlReader xmlReader = new XmlReader();
        return xmlReader.getXmlString(buffer);
    }

    public boolean addModelFile(String fileName, String fileLocation, String updateTime, String uploaderName) {
        int uploaderId = userDAO.findUserIdByUsername(uploaderName);
        if (modelFileDAO.save(new ModelFile(fileName, fileLocation, updateTime, uploaderName, uploaderId)) != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean anyModelFileById(int id) {
        WebService webService = new WebService();
        ModelFile modelFile = modelFileDAO.findById(id).get();
        String fileLocation = modelFile.getFileLocation();
        String fileName = modelFile.getFileName();
        webService.anyModelFile(fileLocation);
        modelFileDAO.updateXmlFileLocationAndXmlFileName(fileLocation + ".xml", fileName + ".xml", id);
        return true;
    }

    public boolean deleteModelFile(int id) {
        WebService webService = new WebService();
        ModelFile modelFile = modelFileDAO.findById(id).get();
        String fileName = modelFile.getFileLocation().substring(12);
        webService.deleteFile(fileName);

        String xmlFileLocation = modelFile.getXmlFileLocation();
        if (xmlFileLocation != null) {
            String xmlFileName = xmlFileLocation.substring(12);
            webService.deleteFile(xmlFileName);
        }
        List<Object> docFileIds = compareDAO.findAllDocIdByModelId(id);
        for (Object obj : docFileIds) {
            int docFileId = (int) obj;
            DocFile docFile = docFileDAO.findById(docFileId).get();
            String docFileName = docFile.getFileLocation().substring(12);
            webService.deleteFile(docFileName);
            String docXmlFileLocation = docFile.getXmlFileLocation();
            if (docXmlFileLocation != null) {
                String xmlFileName = docXmlFileLocation.substring(12);
                webService.deleteFile(xmlFileName);
            }
            String xmlCompareFileLocation = compareDAO.findXmlCompareFileLocationByDocId(docFileId);
            if (xmlCompareFileLocation != null) {
                String xmlCompareFileName = xmlCompareFileLocation.substring(12);
                webService.deleteFile(xmlCompareFileName);
            }
            String docWordCompareFilePath = compareDAO.findCompareDocFilePathFromDocId(docFileId);
            if (docWordCompareFilePath != null) {
                String deleteFileName = docWordCompareFilePath.substring(12);
                webService.deleteFile(deleteFileName);
            }
            compareDAO.deleteAllDocDataByDocId(docFileId);
            docFileDAO.deleteDocDataByDocId(docFileId);
        }
        compareDAO.deleteAllModelDataByModelId(id);
        modelFileDAO.deleteModelDataByModelId(id);
        return true;
    }


    public byte[] downloadModelFile(String filePath) {
        String fileName = filePath.substring(12);
        WebService webService = new WebService();
        byte[] fileByte = webService.downloadFile(fileName);
        return fileByte;
    }


}
