package com.darkcoder.util;

/**
 * @author 徐泽爽
 * @version 1.0
 * @date 2021/5/2 13:14
 */

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class UploadFileUtil {
    /**
     * 多张/单张都可以用这个 保存图片
     * @param files 上传的文件
     * @param path 图片保存的路径
     * @return "wrong_file_extension"-错误的后缀, "file_empty"-空文件 或者 保存后的绝对路径
     */
    public static List<String> uploadFiles(List<MultipartFile> files, String path) throws IOException {
        List<String> msgs = new ArrayList<>();
        if (files.size() < 1) {
            msgs.add("file_empty");
            return msgs;
        }
        for (int i = 0; i < files.size(); ++i) {
            MultipartFile file = files.get(i);
            if (!file.isEmpty()) {
                String filename = file.getOriginalFilename();
                String type = filename.indexOf(".") != -1
                        ? filename.substring(filename.lastIndexOf("."), filename.length())
                        : null;
                if (type == null) {
                    msgs.add("file_empty");
                    return msgs;
                }

                if (!(".PNG".equals(type.toUpperCase()) || ".JPG".equals(type.toUpperCase()))) {
                    msgs.add("wrong_file_extension");
                    return msgs;
                }
            }
        }
        for (int i = 0; i < files.size(); ++i) {
            MultipartFile file = files.get(i);
            String filename = file.getOriginalFilename();
            String type = filename.indexOf(".") != -1 ? filename.substring(filename.lastIndexOf("."), filename.length())
                    : null;
            filename = UUID.randomUUID().toString().replaceAll("-", "") + type;
            String filepath = path + filename;
            File filesPath = new File(path);
            if (!filesPath.exists()) {
                filesPath.mkdir();
            }
            BufferedOutputStream out = null;
            type = filepath.indexOf(".") != -1 ? filepath.substring(filepath.lastIndexOf(".") + 1, filepath.length())
                    : null;
            try {
                out = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
                out.write(file.getBytes());
                msgs.add(filename);
            } catch (Exception e) {
                // 没有上传成功
                e.printStackTrace();
            } finally {
                out.flush();
                out.close();
            }
        }
        return msgs;
    }

}
