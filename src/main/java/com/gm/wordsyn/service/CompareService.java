package com.gm.wordsyn.service;

import com.gm.wordsyn.dao.CompareDAO;
import com.gm.wordsyn.dao.DocFileDAO;
import com.gm.wordsyn.dao.ModelFileDAO;
import com.gm.wordsyn.vueResult.DocList;
import com.gm.wordsyn.webService.WebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Slf4j
@Service
public class CompareService {
    @Autowired
    CompareDAO compareDAO;
    @Autowired
    DocFileDAO docFileDAO;
    @Autowired
    ModelFileDAO modelFileDAO;

    public List<DocList> getDocList() {
        List<DocList> docLists = new ArrayList<>();
        List<Object[]> list = compareDAO.findAllDocList();
        for (Object[] objects : list) {
            int docFileId = (int) objects[0];
            String docFileName = (String) objects[1];
            String docFileUploader = (String) objects[2];
            String docFileUpdateTime = (String) objects[3];
            int modelFileId = (int) objects[4];
            String modelFileName = (String) objects[5];
            String modelFileUploader = (String) objects[6];
            String compareXmlPath = (String) objects[7];
            String docXmlFileLocation = (String) objects[8];
            String compareDocWordFileLocation = (String) objects[9];
            String compareFilePath = (String) objects[10];
            String fileLocation = (String) objects[11];
            boolean comapreAble = false;
            if (compareXmlPath != null) {
                comapreAble = true;
            }
            docLists.add(new DocList(docFileId, docFileName, docFileUploader, docFileUpdateTime, modelFileId,
                    modelFileName, modelFileUploader, comapreAble, docXmlFileLocation, compareDocWordFileLocation,
                    compareFilePath, fileLocation));
        }
        return docLists;
    }

    public boolean updateDocFilesModelFile(int modelFileId, int docFileId) {
        compareDAO.updateDocFilesModelFile(modelFileId, docFileId);
        return true;
    }

    public Vector compare2Xml(int docFileId, int modelFileId) {
        Vector result = new Vector();
        String docXmlPath = docFileDAO.findById(docFileId).get().getXmlFileLocation();
        String modelXmlPath = modelFileDAO.findById(modelFileId).get().getXmlFileLocation();
        if (docXmlPath == null) {
            result.set(0, "not find docXml");
            return result;
        }
        if (modelXmlPath == null) {
            result.set(0, "not find modelXml");
            return result;
        }
        String docFileName = docFileDAO.findById(docFileId).get().getFileName();
        WebService webService = new WebService();
        result = webService.compare2Xml(modelXmlPath, docXmlPath);
        String compareFileLocation = docXmlPath + ".compare.xml";
        String compareFileName = docFileName + ".compare.xml";
        compareDAO.updateComapreXmlLocationAndXmlName(compareFileLocation, compareFileName, docFileId);
        return result;
    }

    public byte[] downloadFile(String filePath) {
        String fileName = filePath.substring(12);
        WebService webService = new WebService();
        byte[] fileByte = webService.downloadFile(fileName);
        return fileByte;
    }

    public String addComment(int docId) {
        String docFilePath = docFileDAO.findById(docId).get().getFileLocation();
        WebService webService = new WebService();
        webService.addComment(docFilePath);
        String wordfileName = docFilePath + ".comment." + docFilePath.substring(12);
        compareDAO.updateCompareDocWordFilePath(wordfileName, docId);
        return "add commnet ok";
    }
}
