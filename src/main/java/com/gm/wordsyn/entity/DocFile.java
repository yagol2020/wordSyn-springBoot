package com.gm.wordsyn.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "docfile")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class DocFile {
    String fileName;
    String fileLocation;
    String updateTime;
    String uploader;
    int uploadId;
    String xmlFileLocation;
    String xmlFileName;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    public DocFile(){

    }

    /**
     * 没有xmlFile相关，因为没有比较过
     * 用于：上传文件
     * @param fileName
     * @param fileLocation
     * @param updateTime
     * @param uploader
     */
    public DocFile(String fileName,String fileLocation,String updateTime,String uploader,int uploadId){
        this.fileName=fileName;
        this.fileLocation=fileLocation;
        this.updateTime=updateTime;
        this.uploader=uploader;
        this.uploadId=uploadId;
    }
}