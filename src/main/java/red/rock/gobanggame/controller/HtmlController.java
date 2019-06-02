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
    @RequestMapping("/websocket/{username}")
    public String websocket(@PathVariable String username ,Model model){
        try{
            model.addAttribute("username",username);
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
}
