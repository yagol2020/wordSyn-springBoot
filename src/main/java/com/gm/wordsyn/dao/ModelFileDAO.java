package com.gm.wordsyn.dao;

import com.gm.wordsyn.entity.ModelFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ModelFileDAO extends JpaRepository<ModelFile, Integer> {

    @Query(nativeQuery = true, value = "SELECT xml_file_location FROM wordsyn.modelfile where id=?1")
    String findXmlFileLocationFromId(int id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "UPDATE `wordsyn`.`modelfile` SET `xml_file_location` = ?1, `xml_file_name` = " +
            "?2 WHERE (`id` = ?3)")
    void updateXmlFileLocationAndXmlFileName(String xmlFileLocation,String xmlFileName,int id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "DELETE FROM `wordsyn`.`modelfile` WHERE (`id` = ?1)")
    void deleteModelDataByModelId(int modelFileId);


}
