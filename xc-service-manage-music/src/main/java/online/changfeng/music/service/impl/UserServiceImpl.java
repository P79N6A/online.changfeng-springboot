package online.changfeng.music.service.impl;

import com.xuecheng.framework.domain.user.pojo.MyFriends;
import com.xuecheng.framework.domain.user.pojo.Users;
import com.xuecheng.framework.domain.user.enums.SearchFriendsStatusEnum;
import ezvcard.parameter.ImageType;
import ezvcard.property.Photo;
import online.changfeng.music.mapper.MyFriendsMapper;
import online.changfeng.music.mapper.UsersMapperCustom;
import online.changfeng.music.utils.FastDFSTool;
import online.changfeng.music.utils.FileUtils;
import online.changfeng.music.utils.QRCodeUtils;
import online.changfeng.music.utils.dictionary.Constants;
import org.n3r.idworker.Sid;
import online.changfeng.music.mapper.UsersMapper;
import online.changfeng.music.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UsersMapper userMapper;

	@Autowired
	private MyFriendsMapper myFriendsMapper;

	@Autowired
	private Sid sid;

	@Autowired
	private QRCodeUtils qrCodeUtils;

	@Autowired
	private UsersMapperCustom usersMapperCustom;



	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public boolean queryUsernameIsExist(String username) {
		
		Users user = new Users();
		user.setUsername(username);
		
		Users result = userMapper.selectOne(user);
		
		return result != null ? true : false;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public Users queryUserForLogin(String username, String pwd) {
		
		Example userExample = new Example(Users.class);
		Criteria criteria = userExample.createCriteria();
		
		criteria.andEqualTo("username", username);
		criteria.andEqualTo("password", pwd);
		
		Users result = userMapper.selectOneByExample(userExample);
		
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Users saveUser(Users user) throws Exception {
		
		String userId = sid.nextShort();


		// Ϊÿ���û�����һ��Ψһ�Ķ�ά��
//		String qrCodePath = "C://user" + userId + "qrcode.png";
//		FileUtils.base64ToFile(qrCodePath, "muxin_qrcode"+user.getUsername());
		// Ϊÿ���û�����һ��Ψһ�Ķ�ά��
		String qrCodePath = "C://user" + userId + "qrcode.png";
		// muxin_qrcode:[username]
		String content = "BEGIN:VCARD\n" + "VERSION:3.0\n" + "N:������\n"
				+ "EMAIL:faker_322@outlook.com\n" + "TEL:18606530927 \n"
				+ "ROLE:ȫջ�ܹ�ʦ\n" + "ADR:��������\n" + "ORG:" + "��������\n"
				+ "TITLE:Java�ܹ�ʦ\n" + "URL: \n" + "NOTE:��ְ��!\n" + "END:VCARD";
		qrCodeUtils.createQRCode(qrCodePath, content);
		MultipartFile qrCodeFile = FileUtils.fileToMultipart(qrCodePath);
		String qrCodeUrl = "";
		try {
			 qrCodeUrl = FastDFSTool.uploadFile(qrCodeFile.getBytes(),
					qrCodePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String imagePath= Constants.FDFS_SERVER + qrCodeUrl;
		System.out.println("��ά���ַ:"+imagePath);
		user.setQrcode(imagePath);
		user.setId(userId);
		userMapper.insert(user);
		
		return user;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Users updateUserInfo(Users user) {
		userMapper.updateByPrimaryKeySelective(user);
		return queryUserById(user.getId());
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public Integer preconditionSearchFriends(String myUserId, String friendUsername) {

		Users user = queryUserInfoByUsername(friendUsername);
        List<Users> users = usersMapperCustom.queryUserByUserName(friendUsername);
        for (Users user1 : users) {
            System.out.println(user1.getUsername());
        }
        // 1. �������û���������ڣ�����[�޴��û�]
		if (user == null) {
			return SearchFriendsStatusEnum.USER_NOT_EXIST.status;
		}

		// 2. �����˺������Լ�������[��������Լ�]
		if (user.getId().equals(myUserId)) {
			return SearchFriendsStatusEnum.NOT_YOURSELF.status;
		}

		// 3. �����������Ѿ�����ĺ��ѣ�����[���û��Ѿ�����ĺ���]
		Example mfe = new Example(MyFriends.class);
		Criteria mfc = mfe.createCriteria();
		mfc.andEqualTo("myUserId", myUserId);
		mfc.andEqualTo("myFriendUserId", user.getId());
		MyFriends myFriendsRel = myFriendsMapper.selectOneByExample(mfe);
		if (myFriendsRel != null) {
			return SearchFriendsStatusEnum.ALREADY_FRIENDS.status;
		}

		return SearchFriendsStatusEnum.SUCCESS.status;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public Users queryUserInfoByUsername(String username) {
		Example ue = new Example(Users.class);
		Criteria uc = ue.createCriteria();
		uc.andLike("username", username);
		return userMapper.selectOneByExample(ue);
	}

	@Override
	public void sendFriendRequest(String myUserId, String friendUsername) {

	}

	@Transactional(propagation = Propagation.SUPPORTS)
	private Users queryUserById(String userId) {
		return userMapper.selectByPrimaryKey(userId);
	}

}
