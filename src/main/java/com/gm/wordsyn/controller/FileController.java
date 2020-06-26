package com.gm.wordsyn.controller;

import com.gm.wordsyn.result.Result;
import com.gm.wordsyn.result.ResultFactory;
import com.gm.wordsyn.service.CompareService;
import com.gm.wordsyn.service.DocFileService;
import com.gm.wordsyn.service.ModelFileService;
import com.gm.wordsyn.util.MyDate;
import com.gm.wordsyn.util.StringUtils;
import com.gm.wordsyn.webService.WebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 与数据库文件有关的Controller，主要负责数据库file的信息处理
 */
@Slf4j
@RequestMapping(value = "/api/wordsyn/file")
@RestController
public class FileController {
    @Autowired
    ModelFileService modelFileService;
    @Autowired
    DocFileService docFileService;
    @Autowired
    CompareService compareService;
    String baseFileLocationOnline = "C:\\ftp\\data";//数据库基础地址

    /**
     * 返回所有模板文件列表
     *
     * @return
     */
    @GetMapping(value = "modelFile/all")
    public Result getAllModelFileList() {
        return ResultFactory.buildSuccessResult(modelFileService.findAll());
    }

    /**
     * 返回所有待检测文件列表
     *
     * @return
     */
    @GetMapping(value = "docFile/all")
    public Result getAllDocFileList() {
        return ResultFactory.buildSuccessResult(compareService.getDocList());
    }

    @PostMapping(value = "update/docFilesModelFile")
    public Result setDocFilesModelFile(@RequestParam(value = "docFileId") int docFileId,
                                       @RequestParam(value = "modelFileId") int modelFileId) {
        compareService.updateDocFilesModelFile(modelFileId, docFileId);
        return ResultFactory.buildSuccessResult("updateOK");
    }


    /**
     * 上传word文件，调用webSerivce对服务器上传文件，同时将文件地址添加进数据库
     *
     * @return
     */
    @PostMapping(value = "uploadFile/modelFile")
    public Result postUploadModelFile(MultipartFile file, @RequestParam(value = "uploaderName") String uploadName) {
        WebService webService = new WebService();
        String fileOriginalName = file.getOriginalFilename();
        String fileRandomName = StringUtils.getRandomString(6) + file.getOriginalFilename();//为了防止数据存储时，命名冲突
        try {
            webService.uploadFile(file, fileRandomName);
            MyDate myDate = new MyDate();
            modelFileService.addModelFile(fileOriginalName,
                    baseFileLocationOnline + "\\" + fileRandomName, myDate.getCurrentDate(), uploadName);
            return ResultFactory.buildSuccessResult("uploadOK");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultFactory.buildFailResult("uploadFail");
        }
    }

    /**
     * 上传word文件，调用webSerivce对服务器上传文件，同时将文件地址添加进数据库
     *
     * @return
     */
    @PostMapping(value = "uploadFile/docFile")
    public Result postUploadDocFile(MultipartFile file, @RequestParam(value = "modelFileId") int modelFileId,
                                    @RequestParam(value = "uploaderName") String uploadName) {
        WebService webService = new WebService();
        String fileOriginalName = file.getOriginalFilename();
        String fileRandomName = StringUtils.getRandomString(6) + file.getOriginalFilename();//为了防止数据存储时，命名冲突
        try {
            webService.uploadFile(file, fileRandomName);
            MyDate myDate = new MyDate();
            docFileService.addDocFile(fileOriginalName,
                    baseFileLocationOnline + "\\" + fileRandomName, myDate.getCurrentDate(), uploadName, modelFileId);
            return ResultFactory.buildSuccessResult("uploadOK");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultFactory.buildFailResult("uploadFail");
        }
    }

    @PostMapping(value = "deleteFile")
    public Result deleteFile(@RequestParam(value = "classification") String classification,
                             @RequestParam(value = "id") int id) {
        if (classification.equals("ModelFile")) {
            modelFileService.deleteModelFile(id);
            return ResultFactory.buildSuccessResult("delete OK");
        }
        if (classification.equals("DocFile")) {
            docFileService.deleteDocFile(id);
            return ResultFactory.buildSuccessResult("delete OK");
        }
        return ResultFactory.buildSuccessResult("delete fail");
    }

    @GetMapping(value = "downloadFile")
    public void downloadFile(@RequestParam(value = "path") String path, final HttpServletResponse response,
                             final HttpServletRequest request) {
        ServletOutputStream os = null;
        InputStream is = null;
        try {
            // 取得输出流
            os = response.getOutputStream();
            // 清空输出流
            response.reset();
            response.setHeader("content-type", "application/octet-stream");
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(path.substring(18).getBytes(
                    "utf-8"),
                    "iso-8859-1"));
            //读取流
            log.info("请求下载文件:" + path);
            is = new ByteArrayInputStream(compareService.downloadFile(path));
            if (is == null) {

            }
            os = response.getOutputStream();
//            byte[] bytes=new byte[1024];
//            while((is.read(bytes))>0){
//                os.write(bytes);
//            }
            os.write(compareService.downloadFile(path));
            is.close();
            ;
            os.close();
        } catch (IOException e) {

        }
    }


    @GetMapping(value = "addComment")
    public Result addCommnet(@RequestParam("docFileId") int docId) {
        compareService.addComment(docId);
        return ResultFactory.buildSuccessResult("add Comment OK");
    }

    @GetMapping(value = "downloadModelFile")
    public void downloadModelFile(@RequestParam(value = "path") String path, final HttpServletResponse response,
                                  final HttpServletRequest request) {
        ServletOutputStream os = null;
        InputStream is = null;
        try {
            // 取得输出流
            os = response.getOutputStream();
            // 清空输出流
            response.reset();
            response.setHeader("content-type", "application/octet-stream");
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(path.substring(18).getBytes(
                    "utf-8"),
                    "iso-8859-1"));
            //读取流
            log.info("请求下载文件:" + path);
            is = new ByteArrayInputStream(modelFileService.downloadModelFile(path));
            if (is == null) {

            }
            os = response.getOutputStream();
            os.write(compareService.downloadFile(path));
            is.close();
            ;
            os.close();
        } catch (IOException e) {

        }

    }

    @GetMapping(value = "downloadDocFile")
    public void downloadDocFile(@RequestParam(value = "path") String path, final HttpServletResponse response,
                                final HttpServletRequest request) {
        ServletOutputStream os = null;
        InputStream is = null;
        try {
            // 取得输出流
            os = response.getOutputStream();
            // 清空输出流
            response.reset();
            response.setHeader("content-type", "application/octet-stream");
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(path.substring(12).getBytes(
                    "utf-8"),
                    "iso-8859-1"));
            //读取流
            log.info("请求下载文件:" + path);
            is = new ByteArrayInputStream(docFileService.downloadDocFile(path));
            if (is == null) {

            }
            os = response.getOutputStream();
            os.write(compareService.downloadFile(path));
            is.close();
            ;
            os.close();
        } catch (IOException e) {

        }
    }
}
