package com.gm.wordsyn.dao;

import com.gm.wordsyn.entity.CompareFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CompareDAO extends JpaRepository<CompareFile, Integer> {

    @Query(nativeQuery = true, value = "SELECT doc_file_id as \"docFileId\",docfile.file_name as \"docFileName\"," +
            "docfile.uploader as \"docFileUploader\",docfile.update_time as \"docFileUpdateTime\",model_file_id as " +
            "\"modelFileId\",modelfile.file_name as \"modelFileName\",modelfile.uploader as \"modelFileUploader\"," +
            "compare_file_location as \"compareXmlPath\",wordsyn.docfile.xml_file_location as \"docXmlFileLocation\"," +
            "comparefile.compare_doc_word_file_location as\"compareDocWordFileLocation\",comparefile" +
            ".compare_file_location as\"compareFileLocation\", docfile.file_location as \"fileLocation\"" +
            "  FROM" +
            " wordsyn.comparefile join wordsyn.docfile join wordsyn.modelfile where wordsyn.docfile.id=wordsyn.comparefile.doc_file_id and wordsyn.modelfile.id=wordsyn.comparefile.model_file_id")
    public List<Object[]> findAllDocList();

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `wordsyn`.`comparefile` SET `model_file_id` = ?1 WHERE (`doc_file_id` =" +
            " " +
            "?2)")
    void updateDocFilesModelFile(int modelFileId, int docFileId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `wordsyn`.`comparefile` SET `compare_file_location` = ?1, " +
            "`compare_file_name` = ?2 WHERE (`doc_file_id` = ?3)")
    void updateComapreXmlLocationAndXmlName(String comapreLocation, String compareFileName, int docFileId);


    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM `wordsyn`.`comparefile` WHERE (`model_file_id` = ?1)")
    void deleteAllModelDataByModelId(int modelFileId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM `wordsyn`.`comparefile` WHERE (`doc_file_id` = ?1)")
    void deleteAllDocDataByDocId(int docFileId);


    @Query(nativeQuery = true, value = "select comparefile.compare_file_location from comparefile where doc_file_id=?1")
    String findXmlCompareFileLocationByDocId(int docFileId);

    @Query(nativeQuery = true, value = "select comparefile.doc_file_id from comparefile where model_file_id=?1")
    List<Object> findAllDocIdByModelId(int modelFileId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `wordsyn`.`comparefile` SET `compare_doc_word_file_location` = " +
            "?1 WHERE (`doc_file_id` = ?2)")
    public void updateCompareDocWordFilePath(String docWordComparePath, int docId);

    @Query(nativeQuery = true, value = "SELECT wordsyn.comparefile.compare_doc_word_file_location FROM wordsyn" +
            ".comparefile where doc_file_id=?1")
    public String findCompareDocFilePathFromDocId(int docId);
}
