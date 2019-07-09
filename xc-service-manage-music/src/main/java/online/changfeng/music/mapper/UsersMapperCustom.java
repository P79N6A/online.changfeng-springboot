package online.changfeng.music.mapper;

import com.xuecheng.framework.domain.user.pojo.Users;
import online.changfeng.music.pojo.vo.FriendRequestVO;
import online.changfeng.music.pojo.vo.MyFriendsVO;
import online.changfeng.music.utils.MyMapper;

import java.util.List;


public interface UsersMapperCustom extends MyMapper<Users> {

    public List<FriendRequestVO> queryFriendRequestList(String acceptUserId);
	
	public List<MyFriendsVO> queryMyFriends(String userId);
	
	public void batchUpdateMsgSigned(List<String> msgIdList);

	public List<Users>  queryUserByUserName(String username);
	
}