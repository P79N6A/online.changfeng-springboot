package online.changfeng.music.service;


import com.xuecheng.framework.domain.user.pojo.Users;

public interface UserService {

	/**
	 * @Description: �ж��û����Ƿ����
	 */
	public boolean queryUsernameIsExist(String username);

	/**
	 * @Description: ��ѯ�û��Ƿ����
	 */
	public Users queryUserForLogin(String username, String pwd);

	/**
	 * @Description: �û�ע��
	 */
	public Users saveUser(Users user) throws Exception;

	/**
	 * @Description: �޸��û���¼
	 */
	public Users updateUserInfo(Users user);

	/**
	 * @Description: �������ѵ�ǰ������
	 */
	public Integer preconditionSearchFriends(String myUserId, String friendUsername);

	/**
	 * @Description: �����û�����ѯ�û�����
	 */
	public Users queryUserInfoByUsername(String username);

	/**
	 * @Description: ��Ӻ��������¼�����浽���ݿ�
	 */
	public void sendFriendRequest(String myUserId, String friendUsername);
	

}
