package com.example.javafxapp.Helpper;

import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MyWebSocketServer extends WebSocketServer {
    private final Set<WebSocket> clients = Collections.synchronizedSet(new HashSet<>());

    public MyWebSocketServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        clients.add(conn);
        System.out.println("Client đã kết nối: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        clients.remove(conn);
        System.out.println("Client ngắt kết nối: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {}

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("Lỗi WebSocket: " + ex.getMessage());
    }

    @Override
    public void onStart() {
        System.out.println("WebSocket Server đang chạy...");
    }

    public void sendMessageToAll(String message) {
        for (WebSocket client : clients) {
            client.send(message);
        }
    }
}
