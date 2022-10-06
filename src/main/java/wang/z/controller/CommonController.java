package wang.z.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import wang.z.common.R;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

/**
 * @author like
 * @date 2022/10/1 10:58
 * @Description TODO
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file)
    {   //file是一个临时文件，需要转崔到指定位置，佛则本次请求完成过后临时文件会删除
        log.info(file.toString());
        //原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID重新生车工文件名，防止文件名重复造成文件覆盖
        String filename = UUID.randomUUID().toString() + suffix;

        //创建一个目录对象
        File dir = new File(basePath);
        //判断当前目录是否存在
        if(!dir.exists()){
            //目录不存在，需要创建
            dir.mkdirs();
        }
        try {
            //见临时文件转转存指定位置
            file.transferTo(new File(basePath+filename));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return R.success(filename);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

       try {
           //输入流，通过输入流读取文件内容
           FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
           //输入流，通过输出流将文件协会浏览器，在浏览器展示图片
           ServletOutputStream outputStream = response.getOutputStream();

           response.setContentType("image/jpeg");
           int len = 0;
           byte [] bytes = new byte[1024];
           while((len = fileInputStream.read(bytes)) != -1){
               outputStream.write(bytes, 0, len);
               outputStream.flush();
           }

            outputStream.close();
           fileInputStream.close();
       }catch (Exception e) {
           e.printStackTrace();
       }
    }
}
