package com.magic.ssh.util;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class WSUtil {

    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
    private static ConcurrentHashMap<String, Session> sessionPools = new ConcurrentHashMap<>();
    /*
    Add Session
     */
    public static void add(String WSId, Session session) {
        sessionPools.put(WSId,session);
        log.info("当前连接数 = " + sessionPools.size());
    }

    /*
    Receive Message
     */
    public static void receive(String WSId, String message) {

        log.info("[WebSocketServer] Received Message : WSId = " + WSId + " , message = " + message);
        log.info("当前连接数 = " + sessionPools.size());
    }

    /*
    Remove Session
     */
    public static void remove(String WSId) {
        sessionPools.remove(WSId);
        log.info("当前连接数 = " + sessionPools.size());
    }

    /*
    Remove Session
     */
    public static Integer size() {
        return sessionPools.size();
    }

    /*
    Get Session
     */
    public static boolean sendMessage(String WSId , String message) {
        log.info("当前连接数 = " + sessionPools.size());
        if(sessionPools.get(WSId) == null){
            return false;
        }else{
            sessionPools.get(WSId).getAsyncRemote().sendText(message);
            return true;
        }

    }
}
