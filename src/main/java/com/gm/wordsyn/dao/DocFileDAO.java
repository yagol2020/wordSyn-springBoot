package com.gm.wordsyn.dao;

import com.gm.wordsyn.entity.DocFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface DocFileDAO extends JpaRepository<DocFile, Integer> {
    @Query(nativeQuery = true, value = "SELECT xml_file_location FROM wordsyn.docfile where id=?1")
    String findXmlFileLocationFromId(int id);

    @Query(nativeQuery = true, value = "SELECT compare_file_location FROM wordsyn.comparefile where doc_file_id=?1")
    String findXmlCompareFileLocationFromId(int id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "UPDATE `wordsyn`.`docfile` SET `xml_file_location` = ?1, `xml_file_name` = " +
            "?2 WHERE (`id` = ?3)")
    void updateXmlFileLocationAndXmlFileName(String xmlFileLocation,String xmlFileName,int id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "DELETE FROM `wordsyn`.`docfile` WHERE (`id` = ?1)")
    void deleteDocDataByDocId(int docFileId);

}