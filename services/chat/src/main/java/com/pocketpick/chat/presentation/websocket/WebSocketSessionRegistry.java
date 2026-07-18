package com.pocketpick.chat.presentation.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionRegistry {

    // key: userId, value: 해당 유저의 모든 WebSocketSession
    private final Map<Long, Set<WebSocketSession>> sessions = new ConcurrentHashMap<>();

    public void register(Long userId, WebSocketSession session) {
        sessions.computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet()).add(session);
    }

    public void remove(Long userId, WebSocketSession session) {
        sessions.computeIfPresent(userId, (k, set) -> {
            set.remove(session);
            return set.isEmpty() ? null : set;
        });
    }

    public Set<WebSocketSession> getSessions(Long userId) {
        return sessions.getOrDefault(userId, Set.of());
    }

    public boolean isOnline(Long userId) {
        return sessions.getOrDefault(userId, Set.of())
                .stream()
                .anyMatch(WebSocketSession::isOpen);
    }
}
