package red.rock.gobanggame.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import red.rock.gobanggame.config.HttpSessionConfigurator;
import red.rock.gobanggame.config.MyApplicationContextAware;
import red.rock.gobanggame.entity.Room;
import red.rock.gobanggame.entity.SeatRecord;
import red.rock.gobanggame.entity.User;
import red.rock.gobanggame.entity.UserRecord;
import red.rock.gobanggame.service.RoomService;
import red.rock.gobanggame.service.UserService;
import red.rock.gobanggame.utils.GoBangUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.PipedReader;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * TODO
 *
 * @author tudou
 * @version 1.0
 * @date 2019/6/1 14:44
 **/
@Component
@ServerEndpoint(value = "/websocket/{roomName}/{username}",configurator = HttpSessionConfigurator.class)
public class WebSocket {

    protected RoomService roomService=(RoomService) MyApplicationContextAware.getApplicationContext().getBean("roomService");

    /**
     * 房间号码
     */
    private String roomName;


    /**
     * WebSocket对象
     */
    private static final CopyOnWriteArraySet<WebSocket> WEB_SOCKETS=new CopyOnWriteArraySet<>();

    /**
     * 房间Map对象
     */
    private static final Map<String,CopyOnWriteArraySet<WebSocket>> ROOM_MAP=new HashMap<>();

    /**
     * 在线人数
     */
    private static  int  count=0;

    /**
     * 会话
     */
    private Session session;

    /**
     * 用户信息
     */
    private User user=new User();


    /**
     * 房间棋子信息
     */
    private final static Map<String,List<SeatRecord>> ROOM_LIST=new HashMap<>();


    /**
     * 是否为房间内玩家
     */
    private boolean isUser;

    @OnOpen
    public void onOpen(@PathParam("username")String username,@PathParam("roomName")String roomName,Session  session,EndpointConfig config)throws IOException{
        this.session=session;
        HttpSession httpSession=(HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String isUserName=(String) httpSession.getAttribute("user");
        if(!isUserName.equals(username)){
            try{
                Map<String, Object> map1 = Maps.newHashMap();
                map1.put("messageType", 5);
                map1.put("message", 5);
                WEB_SOCKETS.add(this);
                sendMessageToMe(JSON.toJSONString(map1));
                WEB_SOCKETS.remove(this);
                return;
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        this.user.setUsername(username);
        this.roomName=roomName;
        this.user.setWebSocket(this);
        //服务端messageType 1:进入房间  2:退出房间 3.开始游戏 4.结束游戏 5.错误 6.在线人数名单 7,棋子情况
        //错误 : 1:游戏人数已满  2:此位置已被选中 3:对方还没有落棋 4:部分玩家未准备
        try{
            if(roomService.isEnough(roomName)){
                //房间人数已满
                WEB_SOCKETS.add(this);
                Map<String,Object> map=Maps.newHashMap();
                map.put("messageType",5);
                map.put("message",1);
                isUser=false;
                sendMessageToMe(JSON.toJSONString(map));
                WEB_SOCKETS.remove(this);
            }else{
                WEB_SOCKETS.add(this);
                count++;
                isUser=true;
                Room room=roomService.getRoom(roomName);
                if(null == room){
                    boolean flag=roomService.createRoom(roomName,username);
                    if(!flag){
                        onClose();
                    }
                }else {
                    boolean flag = roomService.joinRoom(roomName,username);
                    if(!flag){
                        onClose();
                    }
                }

                if(!ROOM_MAP.containsKey(this.roomName)){
                    CopyOnWriteArraySet<WebSocket> set=new CopyOnWriteArraySet<>();
                    set.add(this);
                     ROOM_MAP.put(roomName,set);
                }else{
                    CopyOnWriteArraySet<WebSocket> set=ROOM_MAP.get(this.roomName);
                    set.add(this);
                    ROOM_MAP.put(roomName,set);
                }

                this.user.setUserRecord(new UserRecord(-1,-1));
                Map<String,Object> map1=Maps.newHashMap();
                map1.put("messageType",1);
                map1.put("username",username);
                sendMessageAll(JSON.toJSONString(map1));
                Map<String,Object> map2=Maps.newHashMap();
                map2.put("messageType",6);
                map2.put("username",username);
                map2.put("otherName",otherName());
                //System.out.println(otherName());
                sendMessageAll(JSON.toJSONString(map2));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session)throws Exception{
        try{
            JSONObject jsonObject=JSON.parseObject(message);
            //客户端messageType 1:准备工作 2:下棋工作 3:开始游戏
            String messageType=jsonObject.getString("messageType");
            if("3".equals(messageType)) {
                count++;
                if (isStart()) {
                    Map<String, Object> map1 = Maps.newHashMap();
                    map1.put("messageType", 3);
                    map1.put("message", "游戏开始");
                    StartSort();
                    map1.put("color", user.getUserRecord().getColor());
                    map1.put("sort", user.getUserRecord().getIsAllow());
                    Map<String,Object> map2=Maps.newHashMap();
                    map2.put("messageType",3);
                    map2.put("message","游戏开始");
                    map2.put("color",-user.getUserRecord().getColor());
                    map2.put("sort",-user.getUserRecord().getIsAllow());
                    sendMessageTo(JSON.toJSONString(map1),this.user.getUsername());
                    sendMessageTo(JSON.toJSONString(map2),otherName());
                } else {
                    Map<String, Object> map = Maps.newHashMap();
                    map.put("messageType", 5);
                    map.put("message", 4);
                    sendMessageAll(JSON.toJSONString(map));
                }
            }
            else if("1".equals(messageType)){
                String isReady=jsonObject.getString("is");
                int ready=Integer.parseInt(isReady);
                user.getUserRecord().setIsReady(ready);
            }
            else if("2".equals(messageType)){
                    if(user.getUserRecord().getIsAllow()==1){
                        String x=jsonObject.getString("x");
                        String y=jsonObject.getString("y");
                        String color=jsonObject.getString("color");
                        SeatRecord seatRecord=new SeatRecord(Integer.parseInt(x),Integer.parseInt(y),Integer.parseInt(color));
                        List<SeatRecord> seatRecords=new ArrayList<>();
                        if(ROOM_LIST.containsKey(roomName)){
                            seatRecords=ROOM_LIST.get(roomName);
                        }
                        if( !isExist(seatRecords,seatRecord)){
                            seatRecords.add(seatRecord);
                            Map<String,Object> map1=Maps.newHashMap();
                            map1.put("messageType",7);
                            map1.put("x",x);
                            map1.put("y",y);
                            map1.put("color",color);
                            changeSort();
                            sendMessageAll(JSON.toJSONString(map1));
                            ROOM_LIST.put(roomName,seatRecords);
                            boolean flag=GoBangUtil.goBang(seatRecords,seatRecord);
                            if(flag){
                                Map<String,Object> map=Maps.newHashMap();
                                map.put("messageType",4);
                                map.put("username",user.getUsername());
                                sendMessageAll(JSON.toJSONString(map));
                            }
                        }else{
                            Map<String,Object> map=Maps.newHashMap();
                            map.put("messageType",5);
                            map.put("message",2);
                            sendMessageTo(JSON.toJSONString(map),this.user.getUsername());
                        }
                    }else{
                        Map<String,Object> map=Maps.newHashMap();
                        map.put("messageType",5);
                        map.put("message",3);
                        sendMessageTo(JSON.toJSONString(map),this.user.getUsername());
                    }
                }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @OnError
    public void onError(Session session,Throwable error){
        error.printStackTrace();
    }

    @OnClose
    public void onClose()throws IOException{
        //messageType 1:进入房间  2:退出房间
            if(isUser) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("messageType", 2);
                map.put("username", user.getUsername());
                String message="1";
                Room room=roomService.getRoom(this.roomName);


                if(null == room){
                }
                else if(this.user.getUsername().equals(room.getUser())){
                    roomService.deleteRoom(this.roomName);
                    message="";
                    map.put("otherName",otherName());
                    map.put("message",message);
                    sendMessageTo(JSON.toJSONString(map),otherName());
                    ROOM_MAP.remove(this.roomName);
                    ROOM_LIST.remove(this.roomName);
                }
                else{
                    map.put("message",message);
                    sendMessageAll(JSON.toJSONString(map));
                    CopyOnWriteArraySet<WebSocket> set=ROOM_MAP.get(this.roomName);
                    set.remove(this);
                    ROOM_MAP.put(this.roomName,set);
                    if (this.user.getUserRecord().getIsReady()==1&&otherReady()==1) {
                        Map<String, Object> map1 = Maps.newHashMap();
                        map1.put("messageType", 4);
                        map1.put("isUser", true);
                        map1.put("username",otherName());
                        sendMessageTo(JSON.toJSONString(map1),otherName());
                    }

                    ROOM_LIST.remove(this.roomName);
                    roomService.getOut(user.getUsername(),this.roomName);
                }

                WEB_SOCKETS.remove(this);
                session.close();
                count--;
            }
    }



    private  void sendMessageAll(String message) throws IOException{
        CopyOnWriteArraySet<WebSocket> webSockets=ROOM_MAP.get(this.roomName);
        for (WebSocket webSocket : webSockets) {
            if(webSocket.session.isOpen()) {
                synchronized (webSocket) {
                    webSocket.session.getBasicRemote().sendText(message);
                }
            }
        }
    }


    private void sendMessageToMe(String message)throws IOException{
        for (WebSocket webSocket:WEB_SOCKETS){
            if(webSocket==this){
                webSocket.session.getBasicRemote().sendText(message);
            }
        }
    }


    private void sendMessageTo(String message,String toUsername)throws IOException{
        if(null !=toUsername) {
            CopyOnWriteArraySet<WebSocket> webSockets = ROOM_MAP.get(this.roomName);
            for (WebSocket webSocket : webSockets) {
                if (toUsername.equals(webSocket.user.getUsername())) {
                    synchronized (webSocket) {
                        webSocket.session.getAsyncRemote().sendText(message);
                    }
                }
            }
        }
    }

    private boolean isStart(){
        int countUser=0;
        CopyOnWriteArraySet<WebSocket>webSockets=ROOM_MAP.get(this.roomName);
        for(WebSocket webSocket:webSockets){
            if(webSocket.user.getUserRecord().getIsReady()==1){
                countUser++;
            }
        }
        boolean flag=false;
        if(countUser==2){
            flag=true;
        }
        return flag;
    }

    private void StartSort(){
        CopyOnWriteArraySet<WebSocket> webSockets=ROOM_MAP.get(this.roomName);
        int sort=GoBangUtil.randomStart();
        int i=0;
        if(sort==0){
            sort=-1;
        }
        for(WebSocket webSocket:webSockets){
            if(i==0){
                webSocket.user.getUserRecord().setColor(sort);
                webSocket.user.getUserRecord().setIsAllow(sort);
                i++;
            }else{
                webSocket.user.getUserRecord().setIsAllow(-sort);
                webSocket.user.getUserRecord().setColor(-sort);
            }
        }
    }

    private boolean isExist(List<SeatRecord> seatRecords1,SeatRecord seatRecord){
        boolean flag=false;
        int x=seatRecord.getX();
        int y=seatRecord.getY();
        for(SeatRecord seatRecordd:seatRecords1){
            if(seatRecordd.getX()==x&& seatRecordd.getY()==y){
                flag=true;
            }
        }
        return flag;
    }

    private String otherName(){
        CopyOnWriteArraySet<WebSocket> webSockets=ROOM_MAP.get(this.roomName);
        String anotherName=null;
        if(webSockets.size()>0) {
            for (WebSocket webSocket : webSockets) {
                if (!this.user.getUsername().equals(webSocket.user.getUsername())) {
                    anotherName = webSocket.user.getUsername();
                }
            }
        }
        return anotherName;
    }

    private void changeSort(){
        CopyOnWriteArraySet<WebSocket> webSockets=ROOM_MAP.get(this.roomName);
        for(WebSocket webSocket:webSockets){
            synchronized (webSocket) {
                webSocket.user.getUserRecord().setIsAllow(-webSocket.user.getUserRecord().getIsAllow());
            }
        }
    }

    private int otherReady(){
        CopyOnWriteArraySet<WebSocket> webSockets=ROOM_MAP.get(this.roomName);
        int otherReady=0;
        if(webSockets.size()>0) {
            for (WebSocket webSocket : webSockets) {
                if (!this.user.getUsername().equals(webSocket.user.getUsername())) {
                    otherReady = webSocket.user.getUserRecord().getIsReady();
                }
            }
        }
        return otherReady;
    }


}


