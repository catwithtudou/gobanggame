package red.rock.gobanggame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import red.rock.gobanggame.entity.Room;
import red.rock.gobanggame.service.RoomService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * TODO
 *
 * @author tudou
 * @version 1.0
 * @date 2019/6/2 15:09
 **/
@RestController
public class RoomController {

    @Autowired
    private RoomService roomService;

    @RequestMapping(value = "/createRoom")
    public void createRoom(@RequestParam("roomName")String roomName, HttpSession session, HttpServletResponse response)throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-type", "text/html;charset=UTF-8");
        String username= (String) session.getAttribute("user");
        if( null == username||"".equals(username)){
            response.getWriter().print("<script>alert('请先登录');window.location.href='/login'</script>");
        }else{
            session.setAttribute("roomName",roomName);
            Room room=roomService.getRoom(roomName);
            if(null==room) {
                response.getWriter().print("<script>alert('创建房间成功');window.location.href='/websocket/" + roomName + "/" + username + "';</script>");
            }else{
                response.getWriter().print("<script>alert('房间名已存在,请重新输入');</script>");
            }
        }
    }

    @RequestMapping(value = "/joinRoom")
    public void joinRoom(@RequestParam("roomName")String roomName,HttpSession session,HttpServletResponse response)throws IOException{
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-type", "text/html;charset=UTF-8");
        String username=(String) session.getAttribute("user");
        if(null == username||"".equals(username)){
            response.getWriter().print("<script>alert('请先登录');window.location.href='/login'</script>");
        }else{
               Room room=roomService.getRoom(roomName);
               if(room!=null) {
                   response.getWriter().print("<script>alert('加入房间成功');window.location.href='/websocket/" + roomName + "/" + username + "';</script>");
               }else {
                   response.getWriter().print("<script>alert('房间名不存在')</script>");
               }
        }
    }

    @RequestMapping(value = "/allRoom")
    public String getAllRoom(){
        List<Room> rooms=roomService.getAllRoom();
        if(rooms==null){
            return "无房间存在";
        }
        return rooms.toString();
    }
}
