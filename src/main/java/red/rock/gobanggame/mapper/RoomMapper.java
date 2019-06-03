package red.rock.gobanggame.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import red.rock.gobanggame.entity.Room;
import red.rock.gobanggame.entity.User;

import java.util.List;

/**
 * TODO
 *
 * @author tudou
 * @version 1.0
 * @date 2019/6/2 15:16
 **/

@Mapper
@Repository
public interface RoomMapper {

    /**
     * 添加房间
     * @param roomName
     * @param user
     * @return boolean
     */
    @Insert("insert into room(room_name,user) values(#{roomName},#{user})")
    void addRoom(String roomName,String user);

    /**
     * 查询房间信息
     * @param roomName
     * @return User
     */
    @Select("select * from room where room_name=#{roomName}")
    @Results({
            @Result(property = "roomName",column = "room_name"),
            @Result(property = "user",column = "user"),
            @Result(property = "anotherUser",column = "another_user")
    })
    Room getRoom(String roomName);

    /**
     * 查询所有房间信息
     */
    @Select("select * from room")
    @Results({
            @Result(property = "roomName",column = "room_name"),
            @Result(property = "user",column = "user"),
            @Result(property = "anotherUser",column = "another_user")
    })
    List<Room> getAll( );

    /**
     *  用户进入房间
     */
    @Update("update room set another_user=#{anotherName} where room_name=#{roomName}")
    void getIn(String roomName,String anotherName);

    /**
     * 删除房间
     */
    @Delete("delete from room where room_name=#{roomName}")
    void deleteRoom(String roomName);

    /**
     * 另一位用户离开房间
     */
    @Update("update room set another_user=null where room_name=#{roomName}")
    void getOut(String anotherName,String roomName);



}
