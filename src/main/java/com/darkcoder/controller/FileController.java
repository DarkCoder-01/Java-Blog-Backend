package com.darkcoder.controller;

import com.darkcoder.common.lang.Result;
import com.darkcoder.util.UploadFileUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 徐泽爽
 * @version 1.0
 * @date 2021/5/2 12:50
 */
@RestController
public class FileController {
    //图片存放路径虚拟地址
    @Value("${file.file-host}")
    private String fileHost;

    //图片上传的真实路径
    @Value("${file.file-save-path}")
    private String fileSavePath;

    @RequiresAuthentication
    @PostMapping("/upload")
    public Result uploadImages(@RequestParam("image") MultipartFile file) {
        System.out.println("图片上传，保存位置：" + fileSavePath);
        List<String> paths = null;
        try {
            List<MultipartFile> files = new ArrayList<>();
            files.add(file);
            paths = UploadFileUtil.uploadFiles(files, fileSavePath);
        } catch (IOException e) {
            return Result.fail("上传图片时IO异常！");
        }
        if (paths.get(0).equals("file_empty") || paths.get(0).equals("wrong_file_extension")) {
            return Result.fail("上传图片时请求参数异常！");
        }
        //返回形如的可访问连接"http://139.196.175.10/img/1ae9314834a346c68e38686216bb55ef.jpg"
        String corverUrl = fileHost + paths.get(0);
        System.out.println(corverUrl);
        return Result.succ(corverUrl);
    }
}
