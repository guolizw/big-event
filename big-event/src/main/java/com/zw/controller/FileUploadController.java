package com.zw.controller;


import com.zw.pojo.Result;
import com.zw.utils.AliOssUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FileUploadController {

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws Exception {

        String originalFilename = file.getOriginalFilename();
        //确保文件名字是唯一的 防止文件被覆盖
        String filename = UUID.randomUUID().toString()+originalFilename.substring(originalFilename.lastIndexOf("."));
        //将文件存储在本地的磁盘上
//        file.transferTo(new File("D:\\SpringBoot3-Project\\files\\"+filename));
        String url = AliOssUtil.Upload(filename, file.getInputStream());
        return Result.success(url);
    }
}
