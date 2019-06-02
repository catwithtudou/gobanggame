package red.rock.gobanggame.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import red.rock.gobanggame.entity.User;

/**
 * TODO
 *
 * @author tudou
 * @version 1.0
 * @date 2019/6/1 11:02
 **/

@Repository
@Mapper
public interface UserMapper {

    /**
     * 添加用户
     * @param user
     * @return boolean
     */
    @Insert("insert into user(username,password) values(#{username},#{password})")
    boolean addUser(User user);

    /**
     * 查询用户信息
     * @param username
     * @return User
     */
    @Select("select * from user where username=#{username}")
    @Results({
            @Result(property = "username",column = "username"),
            @Result(property = "password",column = "password"),
    })
    User getUser(String username);



}
