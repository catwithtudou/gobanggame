package red.rock.gobanggame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import red.rock.gobanggame.entity.User;
import red.rock.gobanggame.mapper.UserMapper;

/**
 * TODO
 *
 * @author tudou
 * @version 1.0
 * @date 2019/6/1 11:05
 **/
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 注册用户
     * @param user
     * @return boolean
     */
    public boolean registerUser(User user){
        User user1=userMapper.getUser(user.getUsername());
        boolean flag=false;
        if(user1!=null){
            flag=false;
        }
        else if(userMapper.addUser(user)){
            flag=true;
        }
        return flag;
    }

    /**
     * 登录服务
     * @param  username
     * @param  password
     * @return  boolean
     */
    public boolean login(String username,String password){
        if("".equals(username)||"".equals(password)){
            return false;
        }
        boolean flag=false;
        User user=userMapper.getUser(username);
        if(user!=null){
            String password1=user.getPassword();
            if(password.equals(password1)){
                flag=true;
            }
        }
        return flag;
    }


}
