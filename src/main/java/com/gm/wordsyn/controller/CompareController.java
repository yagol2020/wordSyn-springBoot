package com.gm.wordsyn.controller;

import com.gm.wordsyn.result.Result;
import com.gm.wordsyn.result.ResultFactory;
import com.gm.wordsyn.service.CompareService;
import com.gm.wordsyn.service.DocFileService;
import com.gm.wordsyn.service.ModelFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Vector;


/**
 * 比较相关，读取数据库的文件地址，使用webSerivce进行比较
 */
@Slf4j
@RequestMapping(value = "/api/wordsyn/compare")
@RestController
public class CompareController {
    @Autowired
    ModelFileService modelFileService;
    @Autowired
    DocFileService docFileService;
    @Autowired
    CompareService compareService;

    @PostMapping(value = "compare2Xml")
    public Result setDocFilesModelFile(@RequestParam(value = "docFileId") int docFileId,
                                       @RequestParam(value = "modelFileId") int modelFileId) {
        Vector result=compareService.compare2Xml(docFileId,modelFileId);
        if(!result.get(0).equals("not find docXml")||!result.get(0).equals("not find modelXml")){
            return ResultFactory.buildSuccessResult(result);
        }
        else
        {
            return ResultFactory.buildFailResult((String)result.get(0));
        }

    }
}
