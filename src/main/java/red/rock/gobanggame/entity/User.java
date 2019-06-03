package red.rock.gobanggame.entity;

import lombok.Data;
import red.rock.gobanggame.controller.WebSocket;

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
    private WebSocket webSocket;

    public User(){

    }

    public User(String username,String password){
        this.password=password;
        this.username=username;
    }

}
