package red.rock.gobanggame.entity;

import lombok.Data;

/**
 * TODO
 *
 * @author tudou
 * @version 1.0
 * @date 2019/6/1 10:59
 **/

@Data
public class User {

    private String username;
    private String password;
    private UserRecord userRecord;

    public User(){

    }

    public User(String username,String password){
        this.password=password;
        this.username=username;
    }

}
