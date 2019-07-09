package online.changfeng.music.controller;

import online.changfeng.music.utils.FastDFSTool;
import online.changfeng.music.utils.FileUtils;
import online.changfeng.music.utils.dictionary.Constants;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;

/**
 * Created by sang on 2018/7/14.
 */
@WebServlet("/my")
@MultipartConfig(
        fileSizeThreshold=0,
        maxFileSize=5242880,       // 5 MB
        maxRequestSize=20971520)
public class MyServlet extends HttpServlet {
    private String base64Data;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doPost(req,resp);
    }
    @Override

    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws IOException, ServletException {
        Part part = request.getPart("file");
        String fileNames = getFileName(part);
        System.out.println("fileNames>>>"+fileNames);
        InputStream in = part.getInputStream();
        MultipartFile faceFile = new MockMultipartFile(fileNames, "png", "image/png", in);
        String uploadFile="";
        try {
             uploadFile = FastDFSTool.uploadFile(faceFile.getBytes(),
                     fileNames);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 上传文件到fastdfs
        String imagePath= Constants.FDFS_SERVER + uploadFile;
        System.out.println(imagePath);
    }
    private String getFileName(Part part) {
         String head = part.getHeader("Content-Disposition");
            String fileName = head.substring(head.indexOf("filename=\"")+10, head.lastIndexOf("\""));
            return fileName;
    }
private String writeTo(String fileName, Part part,String nowDate)throws IOException {
    String filesPath="images";
     String addAndFilename = "/"+"upload" + "/" + nowDate + "/";
     filesPath.replace("file:","");
    String finalPath = filesPath + addAndFilename;
    if (!new File(finalPath).exists() || !new File(finalPath).isDirectory()) {
         new File(finalPath).mkdirs();
       }
            InputStream in = part.getInputStream();
            OutputStream out = new FileOutputStream(finalPath+fileName);
            byte[] b = new byte[1024];
            int length = -1;
            while((length = in.read(b))!=-1)
            {
            out.write(b, 0, length);
            }
            in.close();
            out.close();
            return addAndFilename+fileName;
        }

}
