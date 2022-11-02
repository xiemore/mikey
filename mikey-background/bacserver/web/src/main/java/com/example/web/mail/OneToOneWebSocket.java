package com.example.web.mail;

import com.example.web.config.RedisUtil;
import com.example.web.config.WebSocketUtil;
import com.example.web.service.MsgService;
import com.example.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

//这是一个客户端给另一个客户端发送消息的WebSocket
@Slf4j
@ServerEndpoint(value = "/chat/one_to_one/{token}/{to_user_name}")
@Component
public class OneToOneWebSocket {


    //websocket无法自动注入redis，通过redis配置类实现


    /** 记录当前在线连接数 */
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    /** 存放所有在线的客户端
     ** 让Session和user_name一一对应
     ** 从redis中获取token和用户名的对应
     **/
    private static Map<String,  Session> clients = new ConcurrentHashMap<>();

    /**
     ** 连接建立成功调用的方法
     **/
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "token")String token) {
        onlineCount.incrementAndGet(); // 在线数加1
        String user_name = RedisUtil.use_stringRedisTemplate.opsForValue().get(token);
        clients.put(user_name, session);
        System.out.println("有新连接加入:"+session.getId()+",加入用户为"+user_name+",当前在线人数为"+onlineCount.get());
    }

    /**
     ** 连接关闭调用的方法
     **/
    @OnClose
    public void onClose(Session session, @PathParam(value = "token")String token) {
        onlineCount.decrementAndGet(); // 在线数减1
        String user_name = RedisUtil.use_stringRedisTemplate.opsForValue().get(token);
        clients.remove(user_name);
        System.out.println("有连接关闭:"+session.getId()+",退出用户为"+user_name+",当前在线人数为"+onlineCount.get());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     *            客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam(value = "token")String token,
                          @PathParam(value = "to_user_name")String to_user_name) {
        log.info("服务端收到客户端[{}]的消息[{}]", session.getId(), message);
        try {

            if (message != null) {
                Session toSession = clients.get(to_user_name);
                if (toSession != null) {
                    this.sendMessage(message, toSession);
                }
                Map map1 = JSON.parseObject(message, Map.class);
                System.out.println(map1.get("message"));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                String date_str = sdf.format(date);
                String user_name = RedisUtil.use_stringRedisTemplate.opsForValue().get(token);
                int to_user_id = WebSocketUtil.use_userService.find_by_user_name(to_user_name).getId();
                int from_user_id = WebSocketUtil.use_userService.find_by_user_name(user_name).getId();
                WebSocketUtil.use_msgService.addMsg(from_user_id, to_user_id, message, date_str);
            }
        } catch (Exception e) {
            log.error("解析失败: ", e);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 服务端发送消息给客户端
     */
    private void sendMessage(String message, Session toSession) {
        try {
            log.info("服务端给客户端[{}]发送消息[{}]", toSession.getId(), message);
            toSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败:", e);
        }
    }
}
