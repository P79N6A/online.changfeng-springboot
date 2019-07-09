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
        book.setAuthor("罗贯中");
        book.setName("三国演义");
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
     * @Description: 用户注册/登录
     */
    @RequestMapping("/registOrLogin")
    public IMoocJSONResult registOrLogin(Users user) throws Exception {
        System.out.println("快进来11"+user.getUsername()+"::"+user.getPassword());
        // 0. 判断用户名和密码不能为空
        if (StringUtils.isBlank(user.getUsername())
                || StringUtils.isBlank(user.getPassword())) {
            return IMoocJSONResult.errorMsg("user or password is empty...");
        }

        // 1. 判断用户名是否存在，如果存在就登录，如果不存在则注册
        boolean usernameIsExist = userService.queryUsernameIsExist(user.getUsername());
        Users userResult = null;
        if (usernameIsExist) {
            // 1.1 登录
            userResult = userService.queryUserForLogin(user.getUsername(),
                    MD5Utils.getMD5Str(user.getPassword()));
            if (userResult == null) {
                return IMoocJSONResult.errorMsg("user or password is error...");
            }
        } else {
            // 1.2 注册
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
     * @Description: 上传用户头像
     */
//    @RequestMapping("/uploadFaceBase64", consumes = "application/json")
    @RequestMapping(value = "/uploadFaceBase64", method = RequestMethod.POST, consumes = "application/json")
    public IMoocJSONResult uploadFaceBase64(@RequestBody(required=false) UsersBO userBO,@RequestBody(required=false)MultipartFile file) throws Exception {
        System.out.println("快进来mmm"+userBO.getUserId()+"::"+userBO.getFaceData()+file);
        // 获取前端传过来的base64字符串, 然后转换为文件对象再上传
        String base64Data = userBO.getFaceData();
        String userFacePath = "C:\\" + userBO.getUserId() + "userface64.png";
        FileUtils.base64ToFile(userFacePath, base64Data);
        // 上传文件到fastdfs
        MultipartFile faceFile = FileUtils.fileToMultipart(userFacePath);
        String uploadFile = FastDFSTool.uploadFile(faceFile.getBytes(),
                userFacePath);
        String imagePath= Constants.FDFS_SERVER + uploadFile;
//        String url = fastDFSClient.uploadBase64(faceFile);
//		"dhawuidhwaiuh3u89u98432.png"
//		"dhawuidhwaiuh3u89u98432_80x80.png"
        // 将文件上传到分布式文件系统，并返回文件的存储路径及名称
        // 返回json格式的字符串（图片的绝对网络存放地址）
//        HashMap<String, String> hashMap = new HashMap<String, String>();
//        hashMap.put("path", Constants.FDFS_SERVER + uploadFile);
//        // 获取缩略图的url
//        String thump = "_80x80.";
//        String arr[] = uploadFile.split("\\.");
//        String thumpImgUrl = arr[0] + thump + arr[1];
//        System.err.println(thumpImgUrl);
        // 更细用户头像
        Users user = new Users();
        user.setId(userBO.getUserId());
        user.setFaceImage(imagePath);
        user.setFaceImageBig(imagePath);

        Users result = userService.updateUserInfo(user);

        return IMoocJSONResult.ok(result);
    }

    /**
     * @Description: 设置用户昵称
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
     * @Description: 搜索好友接口, 根据账号做匹配查询而不是模糊查询
     */
    @RequestMapping("/search")
    public IMoocJSONResult searchUser(String myUserId, String friendUsername)
            throws Exception {
        System.out.println("[search]---start---");
        // 0. 判断 myUserId friendUsername 不能为空
        if (StringUtils.isBlank(myUserId)
                || StringUtils.isBlank(friendUsername)) {
            return IMoocJSONResult.errorMsg("");
        }

        // 前置条件 - 1. 搜索的用户如果不存在，返回[无此用户]
        // 前置条件 - 2. 搜索账号是你自己，返回[不能添加自己]
        // 前置条件 - 3. 搜索的朋友已经是你的好友，返回[该用户已经是你的好友]
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
