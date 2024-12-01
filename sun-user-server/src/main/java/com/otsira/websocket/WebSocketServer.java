package com.otsira.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: WebSocket 服务端
 * @create: 2024/11/26 18:56
 */
@Component
@ServerEndpoint("/ws/{sid}")
@Slf4j
public class WebSocketServer {
    //存放会话对象
    private static final Map<String, Session> SESSION_HASH_MAP = new HashMap<>();

    /**
     * 连接建立成功调用的方法
     * @param session 会话对象
     * @param sid 客户端标识
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        log.info("客户端: sid-{} 建立连接", sid);
        SESSION_HASH_MAP.put(sid, session);
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param sid 客户端标识
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        log.info("收到来自客户端: sid-{} 的信息: {}", sid, message);
    }

    /**
     * 连接关闭调用的方法
     * @param sid 客户端标识
     */
    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        log.info("客户端: sid-{} 断开连接", sid);
        SESSION_HASH_MAP.remove(sid);
    }

    /**
     * 服务器向所有客户端群发消息
     * @param message 消息内容
     * @description: 支付成功, 向商家推送消息, 用法如下:
     *         HashMap<String, Object> map = new HashMap<>();
     *         map.put("type", 1);
     *         map.put("orderId", order.getId());
     *         map.put("content", "订单号: " + outTradeNo);
     *         String jsonString = JSON.toJSONString(map);
     *         webSocketServer.sendToAllClient(jsonString);
     */
    public void sendToAllClient(String message) {
        Collection<Session> sessions = SESSION_HASH_MAP.values();
        for (Session session : sessions) {
            try {
                //服务器向客户端发送消息
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


}
