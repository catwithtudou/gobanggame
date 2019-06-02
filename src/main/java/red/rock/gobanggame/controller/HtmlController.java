package red.rock.gobanggame.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * TODO
 *
 * @author tudou
 * @version 1.0
 * @date 2019/6/1 16:08
 **/

@Controller
public class HtmlController {
    @RequestMapping("/websocket/{roomName}/{username}")
    public String websocket(@PathVariable String username ,@PathVariable String roomName,Model model){
        try{
            model.addAttribute("username",username);
            model.addAttribute("roomName",roomName);
            return "websocket";
        }catch (Exception e){
            return "error";
        }
    }
    @GetMapping("/login")
    public String login(){
        return "/login.html";
    }

    @GetMapping("/register")
    public String register(){
        return "/register.html";
    }

    @GetMapping("/room")
    public String room(){
        return "room.html";
    }

    @GetMapping("/createRoom")
    public String createRoom(){
        return "createRoom.html";
    }

    @GetMapping("/joinRoom")
    public String joinRoom(){
        return "joinRoom.html";
    }

}
