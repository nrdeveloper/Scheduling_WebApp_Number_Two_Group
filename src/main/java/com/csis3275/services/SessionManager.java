package com.csis3275.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.csis3275.models.UserModel;

public class SessionManager {
    private static final Map<String, UserModel> sessions = new HashMap<>();

    public static String createSession(String name, String email, String password) {
        UserModel userModel = new UserModel(name, email, password);
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, userModel);
        return sessionId;
    }

    public static UserModel getSessionData(String sessionId) {
        return sessions.get(sessionId);
    }

    public static void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }
}