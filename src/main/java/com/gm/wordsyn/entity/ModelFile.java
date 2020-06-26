package com.gm.wordsyn.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "modelfile")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class ModelFile {
    String fileName;
    String fileLocation;
    String updateTime;
    int uploaderId;
    String uploader;
    String xmlFileLocation;
    String xmlFileName;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    public ModelFile(){

    }

    /**
     * 没有xmlFile相关，因为没有比较过
     * 用于：上传文件
     * @param fileName
     * @param fileLocation
     * @param updateTime
     * @param uploader
     */
    public ModelFile(String fileName,String fileLocation,String updateTime,String uploader,int uploaderId){
        this.fileName=fileName;
        this.fileLocation=fileLocation;
        this.updateTime=updateTime;
        this.uploader=uploader;
        this.uploaderId=uploaderId;
    }

    /**
     * 全参构造方法
     * @param fileName
     * @param fileLocation
     * @param updateTime
     * @param uploader
     * @param xmlFileLocation
     * @param xmlFileName
     */
    public ModelFile(String fileName,String fileLocation,String updateTime,String uploader,String xmlFileLocation,
                     String xmlFileName){
        this.fileName=fileName;
        this.fileLocation=fileLocation;
        this.updateTime=updateTime;
        this.uploader=uploader;
        this.xmlFileLocation=xmlFileLocation;
        this.xmlFileName=xmlFileName;
    }
}
