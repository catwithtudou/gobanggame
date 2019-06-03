package red.rock.gobanggame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import red.rock.gobanggame.entity.Room;
import red.rock.gobanggame.mapper.RoomMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author tudou
 * @version 1.0
 * @date 2019/6/2 15:26
 **/
@Service
public class RoomService {

    @Autowired
    private RoomMapper roomMapper;

    /**
     * 创建房间
     */
    public boolean createRoom(String roomName,String user){
        Room room=null;
        boolean flag=true;
        room=roomMapper.getRoom(roomName);
        if(room!=null){
            flag=false;
        }else{
            roomMapper.addRoom(roomName,user);
        }
        return flag;
    }

    /**
     * 关闭房间
     */
    public void deleteRoom(String roomName){
         roomMapper.deleteRoom(roomName);
    }

    /**
     * 玩家加入房间
     */
    public boolean joinRoom(String roomName,String anotherUser){
        boolean flag=true;
        Room room=null;
        room=roomMapper.getRoom(roomName);
        if(room==null){
            flag=false;
        }else{
            roomMapper.getIn(roomName,anotherUser);
        }
        return flag;
    }

    /**
     * 获得所有房间信息
     */
    public List<Room> getAllRoom(){
        List<Room> rooms=new ArrayList<>();
        rooms=roomMapper.getAll();
        return rooms;
    }

    /**
     * 获得房间信息
     */
    public Room  getRoom(String roomName){
        Room room=null;
        room=roomMapper.getRoom(roomName);
        return room;
    }

    /**
     * 用户离开房间
     */
    public void getOut(String anotherName,String roomName){
        roomMapper.getOut(anotherName,roomName);
    }

    /**
     * 查询房间人数是否已满
     */
    public boolean isEnough(String roomName){
        Room room=roomMapper.getRoom(roomName);
        boolean flag=true;
        if(null == room){
            flag = false;
        }
        else if(null == room.getUser() || null  ==  room.getAnotherUser()){
            flag=false;
        }
        return flag;
    }
}
