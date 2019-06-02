package red.rock.gobanggame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import red.rock.gobanggame.entity.User;
import red.rock.gobanggame.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * TODO
 *
 * @author tudou
 * @version 1.0
 * @date 2019/6/1 12:53
 **/

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login")
    public void login(@RequestParam(name = "username",required = false)String username, @RequestParam(name = "password",required = false)String password, HttpSession session,HttpServletResponse response)throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-type", "text/html;charset=UTF-8");
        if(username.equals("")||password.equals("")){
            response.getWriter().write("请输入账号或者密码");
        }
        boolean flag=userService.login(username,password);
        if(!flag){
            response.getWriter().print("<script>alert('用户名或密码错误');history.back();</script>");
        }else {
            session.setAttribute("user", username);
            response.getWriter().print("<script>alert('用户登陆成功');window.location='/websocket/" + username + "';</script>");

        }
    }

    @RequestMapping(value = "/register")
    public String register(@RequestParam("username")String username,@RequestParam("password")String password){
        if(username.equals("")||password.equals("")){
            return "请输入相关信息";
        }
        User user=new User(username,password);
        boolean flag=userService.registerUser(user);
        if(!flag){
            return "用户名重复,请重新输入";
        }
        return "注册成功";
    }

    @RequestMapping(value = "/createRoom")
    public String createRoom(@RequestParam("roomName")String roomName, HttpSession session)throws IOException{
        String username= (String) session.getAttribute("user");
        if(username==null){
            return "请先登录";
        }else{
            session.setAttribute("roomName",roomName);
            return "创建房间成功";
        }
    }

}
