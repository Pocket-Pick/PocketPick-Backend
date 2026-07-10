package com.pocketpick.chat.presentation.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionRegistry {

    private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<Long, String> currentRooms = new ConcurrentHashMap<>();

    public void register(Long userId, WebSocketSession session) {
        sessions.put(userId, session);
    }

    public void remove(Long userId) {
        sessions.remove(userId);
        currentRooms.remove(userId);
    }

    public Optional<WebSocketSession> getSession(Long userId) {
        return Optional.ofNullable(sessions.get(userId));
    }

    public boolean isOnline(Long userId) {
        WebSocketSession session = sessions.get(userId);
        return session != null && session.isOpen();
    }

    public void setCurrentRoom(Long userId, String roomId) {
        currentRooms.put(userId, roomId);
    }

    public Optional<String> getCurrentRoom(Long userId) {
        return Optional.ofNullable(currentRooms.get(userId));
    }
}
