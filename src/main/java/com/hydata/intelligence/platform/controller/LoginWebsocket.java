package com.hydata.intelligence.platform.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @ClassName LoginWebsocket
 * @Description TODO
 * @Author pyt
 * @Date 2019/6/10 9:58
 * @Version
 */
@ServerEndpoint(value = "/websocket")
@Component
public class LoginWebsocket {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<LoginWebsocket> webSocketSet = new CopyOnWriteArraySet<LoginWebsocket>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    @Getter
    private Session session;
    @Setter
    @Getter
    private String name;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
        System.out.println("用户鉴权：" + (this.name == null ? "无" : this.name));
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("来自客户端的消息:" + message);
        this.name = message;
        //先判断name是否重复,如果重复，踢掉之前的用户
        for (LoginWebsocket item : webSocketSet) {
            if (item.name != null && item.name.equals(message) && item.session != this.session) {
                item.session.getBasicRemote().sendText("You're out!");
                System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
                break;
            }
        }
        System.out.println("用户鉴权：" + (this.name == null ? "无" : this.name));
    }

    /**
     * 发生错误时调用
     *
     * @OnError public void onError(Session session, Throwable error) {
     * System.out.println("发生错误");
     * error.printStackTrace();
     * }
     * <p>
     * <p>
     * public void sendMessage(String message) throws IOException {
     * this.session.getBasicRemote().sendText(message);
     * //this.session.getAsyncRemote().sendText(message);
     * }
     * <p>
     * <p>
     * /**
     * 群发自定义消息
     */
    public static void sendInfo(String message) throws IOException {
        for (LoginWebsocket item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        LoginWebsocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        LoginWebsocket.onlineCount--;
    }
}
