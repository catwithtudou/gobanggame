package red.rock.gobanggame.entity;

import lombok.Data;
import red.rock.gobanggame.controller.WebSocket;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * TODO
 *
 * @author tudou
 * @version 1.0
 * @date 2019/6/2 15:15
 **/

@Data
public class Room {

    private String roomName;
    private String user;
    private String anotherUser;


}
