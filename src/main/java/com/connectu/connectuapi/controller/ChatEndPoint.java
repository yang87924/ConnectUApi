package com.connectu.connectuapi.controller;

import com.connectu.connectuapi.config.GetHttpSessionConfig;
import com.connectu.connectuapi.controller.util.Code;
import com.connectu.connectuapi.controller.util.Result;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/chat", configurator = GetHttpSessionConfig.class)
@Component
public class ChatEndPoint {
    private static final Map<String, Session> onlineUsers = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        onlineUsers.put((String) httpSession.getAttribute("userId"), session);
//        broadCastAllUsers(getFriends());
    }

    public Set getFriends() {
        Set<String> set = onlineUsers.keySet();
        return set;
    }

    public static String getMessage(boolean isSysMsg, String fromName, Object message) {
        Result result = new Result(Code.CHAT_OK, message, isSysMsg?"1":"0");
        return "";
    }

    public void broadCastAllUsers(String sysMsg) {
        try {
            Set<Map.Entry<String, Session>> entries = onlineUsers.entrySet();
            for (Map.Entry<String, Session> entry : entries) {
                Session session = entry.getValue();
                session.getBasicRemote().sendText(sysMsg);
            }
        } catch (Exception e) {
            System.out.println(e);
        }


    }

    @OnMessage
    public void onMessage(String msg) {

    }

    @OnClose
    public void onClose(Session session) {

    }
}
