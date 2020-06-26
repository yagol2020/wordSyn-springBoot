package com.gm.wordsyn.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "comparefile")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class CompareFile {
    int modelFileId;
    int docFileId;
    String compareFileLocation;
    String compareFileName;
    String compareDocWordFileLocation;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compareId")
    private int compareId;

    public CompareFile(int docFileId,int modelFileId){
        this.docFileId=docFileId;
        this.modelFileId=modelFileId;
    }
}