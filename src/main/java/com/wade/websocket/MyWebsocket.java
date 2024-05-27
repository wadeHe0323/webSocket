package com.wade.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/websocket/{nickname}")
@Component
public class MyWebsocket {

    private final static Logger log = LoggerFactory.getLogger(MyWebsocket.class);

    private static Map<String, Session> map = new HashMap<>();
    private static CopyOnWriteArraySet<MyWebsocket> clients = new CopyOnWriteArraySet<>();

    private Session session;
    private String nickname;

    @OnOpen
    public void onOpen(Session session, @PathParam("nickname") String nickname) {
        this.session = session;
        this.nickname = nickname;

        clients.add(this);
        log.info("新用戶 {} 加入，當前使用人數：{}", nickname, clients.size());
        broadcast(nickname + " 已加入連接，當前人數為：" + clients.size());
    }

    @OnClose
    public void onClose () {
        clients.remove(this);
        log.info("用戶 {} 斷開連結，當前人數為：{}", nickname, clients.size());
        broadcast(nickname + " 斷開連結，當前人數為：" + clients.size());
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("nickname") String nickname) {
        log.info("來自客戶端 {} 發送消息：{}", nickname, message);
        broadcast(nickname + ": " + message);
    }

    @OnError
    public void onError (Session session, Throwable error) {
        log.error("發生錯誤");
        error.printStackTrace();
    }

    // 定義群發消息
    public void broadcast(String message) {
        for (MyWebsocket websocket : clients) {
            // 異步發送消息
            websocket.session.getAsyncRemote().sendText(message);
        }
    }
}
