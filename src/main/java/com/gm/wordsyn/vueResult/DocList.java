package com.gm.wordsyn.vueResult;

import lombok.Data;

@Data
public class DocList {
    public DocList(int docFileId, String docFileName, String docFileUploader, String docFileUpdateTime, int modelFileId,
                   String modelFileName, String modelFileUploader, boolean compareAble, String docXmlFileLocation,
                   String compareDocWordFileLocation, String compareFilePath, String fileLocation) {
        this.docFileId = docFileId;
        this.docFileName = docFileName;
        this.docFileUploader = docFileUploader;
        this.docFileUpdateTime = docFileUpdateTime;
        this.modelFileId = modelFileId;
        this.modelFileName = modelFileName;
        this.modelFileUploader = modelFileUploader;
        this.compareAble = compareAble;
        this.docXmlFileLocation = docXmlFileLocation;
        this.compareDocWordFileLocation = compareDocWordFileLocation;
        this.compareFilePath = compareFilePath;
        this.fileLocation = fileLocation;
    }


    int docFileId;
    String docFileUpdateTime;
    String docFileName;
    String docFileUploader;
    String fileLocation;
    String docXmlFileLocation;
    String compareDocWordFileLocation;
    String compareFilePath;

    int modelFileId;
    String modelFileName;
    String modelFileUploader;

    boolean compareAble = false;

}
