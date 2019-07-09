package com.xuecheng.framework.domain.user.response;

import com.xuecheng.framework.domain.user.pojo.Users;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;

/**
 * Created by mrt on 2018/3/31.
 */
@Data
public class UserResult extends ResponseResult {
    Users users;
    public UserResult(ResultCode resultCode, Users users) {
        super(resultCode);
        this.users = users;
    }
}
