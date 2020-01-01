package cn.itsource.hrm.controller;

import cn.itsource.hrm.util.FastDfsApiOpr;
import cn.itsource.util.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Description: TODO
 * @Author: LY
 * @Date: 2019-12-30 18:35
 * @Version: v1.0
 **/
@RestController
@RequestMapping("/fastdfs")
public class FileController {

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public AjaxResult upload(MultipartFile file){

        String fileName = file.getName();
        String originalFilename = file.getOriginalFilename();
        //文件后缀名
        int index = originalFilename.lastIndexOf(".");
        String extName = originalFilename.substring(index + 1);
        try {
            String file_id = FastDfsApiOpr.upload(file.getBytes(), extName);
            return AjaxResult.me().setResultObj(file_id);
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("上传失败!"+e.getMessage());
        }

    }

    /**
     * 文件删除
     */
    @GetMapping("/del       ete")
    public AjaxResult delete(String file_id){
        try {
            file_id = file_id.substring(1);
            int index = file_id.indexOf("/");
            String groupName = file_id.substring(0,index);
            String fileName = file_id.substring(index+1);
            FastDfsApiOpr.delete(groupName,fileName);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("删除失败!"+e.getMessage());
        }
    }

}
