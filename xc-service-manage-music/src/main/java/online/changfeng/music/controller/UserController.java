package online.changfeng.music.controller;

import com.xuecheng.api.user.UserControllerApi;
import com.xuecheng.framework.domain.user.pojo.Author;
import com.xuecheng.framework.domain.user.pojo.Book;
import com.xuecheng.framework.domain.user.pojo.Users;
import com.xuecheng.framework.domain.user.enums.SearchFriendsStatusEnum;
import com.xuecheng.framework.domain.user.pojo.UsersBO;
import online.changfeng.music.pojo.vo.UsersVO;
import online.changfeng.music.service.UserService;
import online.changfeng.music.utils.FastDFSTool;
import online.changfeng.music.utils.FileUtils;
import online.changfeng.music.utils.IMoocJSONResult;
import online.changfeng.music.utils.MD5Utils;
import online.changfeng.music.utils.dictionary.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import java.util.Date;

/**
 * Created by sang on 2018/7/4.
 */
@RestController
@RequestMapping("u")
@MultipartConfig(location="/upload",
        fileSizeThreshold=0,
        maxFileSize=5242880,       // 5 MB
        maxRequestSize=20971520)
public class UserController implements UserControllerApi {

//    @Autowired
//    private FastDFSClient fastDFSClient;
    @Autowired
    private UserService userService;

    @RequestMapping("/book")
    public Book book() {
        Book book = new Book();
        book.setAuthor("�޹���");
        book.setName("��������");
        book.setPrice(30f);
        book.setPublicationDate(new Date());
        return book;
    }

    @GetMapping("/init")
    @ResponseBody
    public String book(@ModelAttribute("b") Book book, @ModelAttribute("a") Author author) {
        return book.toString() + ">>>" + author.toString();
    }
    /**
     * @Description: �û�ע��/��¼
     */
    @RequestMapping("/registOrLogin")
    public IMoocJSONResult registOrLogin(Users user) throws Exception {
        System.out.println("�����11"+user.getUsername()+"::"+user.getPassword());
        // 0. �ж��û��������벻��Ϊ��
        if (StringUtils.isBlank(user.getUsername())
                || StringUtils.isBlank(user.getPassword())) {
            return IMoocJSONResult.errorMsg("user or password is empty...");
        }

        // 1. �ж��û����Ƿ���ڣ�������ھ͵�¼�������������ע��
        boolean usernameIsExist = userService.queryUsernameIsExist(user.getUsername());
        Users userResult = null;
        if (usernameIsExist) {
            // 1.1 ��¼
            userResult = userService.queryUserForLogin(user.getUsername(),
                    MD5Utils.getMD5Str(user.getPassword()));
            if (userResult == null) {
                return IMoocJSONResult.errorMsg("user or password is error...");
            }
        } else {
            // 1.2 ע��
            user.setNickname(user.getUsername());
            user.setFaceImage("");
            user.setFaceImageBig("");
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));

            userResult = userService.saveUser(user);
        }

        UsersVO userVO = new UsersVO();
        BeanUtils.copyProperties(userResult, userVO);

        return IMoocJSONResult.ok(userVO);
    }


    /**
     * @Description: �ϴ��û�ͷ��
     */
//    @RequestMapping("/uploadFaceBase64", consumes = "application/json")
    @RequestMapping(value = "/uploadFaceBase64", method = RequestMethod.POST, consumes = "application/json")
    public IMoocJSONResult uploadFaceBase64(@RequestBody(required=false) UsersBO userBO,@RequestBody(required=false)MultipartFile file) throws Exception {
        System.out.println("�����mmm"+userBO.getUserId()+"::"+userBO.getFaceData()+file);
        // ��ȡǰ�˴�������base64�ַ���, Ȼ��ת��Ϊ�ļ��������ϴ�
        String base64Data = userBO.getFaceData();
        String userFacePath = "C:\\" + userBO.getUserId() + "userface64.png";
        FileUtils.base64ToFile(userFacePath, base64Data);
        // �ϴ��ļ���fastdfs
        MultipartFile faceFile = FileUtils.fileToMultipart(userFacePath);
        String uploadFile = FastDFSTool.uploadFile(faceFile.getBytes(),
                userFacePath);
        String imagePath= Constants.FDFS_SERVER + uploadFile;
//        String url = fastDFSClient.uploadBase64(faceFile);
//		"dhawuidhwaiuh3u89u98432.png"
//		"dhawuidhwaiuh3u89u98432_80x80.png"
        // ���ļ��ϴ����ֲ�ʽ�ļ�ϵͳ���������ļ��Ĵ洢·��������
        // ����json��ʽ���ַ�����ͼƬ�ľ��������ŵ�ַ��
//        HashMap<String, String> hashMap = new HashMap<String, String>();
//        hashMap.put("path", Constants.FDFS_SERVER + uploadFile);
//        // ��ȡ����ͼ��url
//        String thump = "_80x80.";
//        String arr[] = uploadFile.split("\\.");
//        String thumpImgUrl = arr[0] + thump + arr[1];
//        System.err.println(thumpImgUrl);
        // ��ϸ�û�ͷ��
        Users user = new Users();
        user.setId(userBO.getUserId());
        user.setFaceImage(imagePath);
        user.setFaceImageBig(imagePath);

        Users result = userService.updateUserInfo(user);

        return IMoocJSONResult.ok(result);
    }

    /**
     * @Description: �����û��ǳ�
     */
    @RequestMapping("/setNickname")
    public IMoocJSONResult setNickname(@RequestBody UsersBO userBO) throws Exception {

        Users user = new Users();
        user.setId(userBO.getUserId());
        user.setNickname(userBO.getNickname());

        Users result = userService.updateUserInfo(user);

        return IMoocJSONResult.ok(result);
    }
    /**
     * @Description: �������ѽӿ�, �����˺���ƥ���ѯ������ģ����ѯ
     */
    @RequestMapping("/search")
    public IMoocJSONResult searchUser(String myUserId, String friendUsername)
            throws Exception {
        System.out.println("[search]---start---");
        // 0. �ж� myUserId friendUsername ����Ϊ��
        if (StringUtils.isBlank(myUserId)
                || StringUtils.isBlank(friendUsername)) {
            return IMoocJSONResult.errorMsg("");
        }

        // ǰ������ - 1. �������û���������ڣ�����[�޴��û�]
        // ǰ������ - 2. �����˺������Լ�������[��������Լ�]
        // ǰ������ - 3. �����������Ѿ�����ĺ��ѣ�����[���û��Ѿ�����ĺ���]
        Integer status = userService.preconditionSearchFriends(myUserId, friendUsername);
        if (status == SearchFriendsStatusEnum.SUCCESS.status) {
            Users user = userService.queryUserInfoByUsername(friendUsername);
            UsersVO userVO = new UsersVO();
            BeanUtils.copyProperties(user, userVO);
            return IMoocJSONResult.ok(userVO);
        } else {
            String errorMsg = SearchFriendsStatusEnum.getMsgByKey(status);
            return IMoocJSONResult.errorMsg(errorMsg);
        }
    }


}
