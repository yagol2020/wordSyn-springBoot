package com.gm.wordsyn.controller;


import com.gm.wordsyn.result.Result;
import com.gm.wordsyn.result.ResultFactory;
import com.gm.wordsyn.service.CompareService;
import com.gm.wordsyn.service.DocFileService;
import com.gm.wordsyn.service.ModelFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 树状图相关，读取数据库中xml文件，返回xml文件信息
 */
@Slf4j
@RequestMapping(value = "/api/wordsyn/xml")
@RestController
public class XmlController {
    @Autowired
    ModelFileService modelFileService;
    @Autowired
    DocFileService docFileService;
    @Autowired
    CompareService compareService;

    /**
     * 得到指定id的模板文件配套的xml文件，打开xml文件，返回xml文件内容
     *
     * @param id
     * @return
     */
    @GetMapping(value = "modelFile")
    public Result getXmlOnModelFileJson(@RequestParam(value = "id") int id) {
        String xmlString = modelFileService.modelFileOnXmlJson(id);
        return ResultFactory.buildSuccessResult(xmlString);
    }

    /**
     * 得到指定id的待检测文件配套的xml文件，打开xml文件，返回xml文件内容
     *
     * @param id
     * @return
     */
    @GetMapping(value = "docFile")
    public Result getXmlOnDocFileJson(@RequestParam(value = "id") int id) {
        String xmlString = docFileService.docFileOnXml(id);
        return ResultFactory.buildSuccessResult(xmlString);
    }

    /**
     * 得到指定id的待检测文件经过比较后的，配套的xml文件，打开xml文件，返回xml文件内容
     *
     * @param id
     * @return
     */
    @GetMapping(value = "compareFile")
    public Result getXmlOnDocCompareFileJson(@RequestParam(value = "id") int id) {
        String xmlString = docFileService.docCompareFileOnXml(id);
        return ResultFactory.buildSuccessResult(xmlString);
    }

    @GetMapping(value = "any")
    public Result anyFileByIdAndClassification(@RequestParam(value = "classification") String classification,
                                               @RequestParam(value = "id") int id) {
        if (classification.equals("ModelFile")) {
            modelFileService.anyModelFileById(id);
        }
        if (classification.equals("DocFile")) {
            docFileService.anyDocFileById(id);
        }
        return ResultFactory.buildFailResult("wrong classification");
    }
}
