package com.connectu.connectuapi.controller;

import javax.servlet.http.HttpSession;

public class BaseController {

    protected final Integer getUserIdFromSession(HttpSession session) {
        return Integer.valueOf(session.getAttribute("userId").toString());
    }


    protected final String getUserNameFromSession(HttpSession session) {
        return session.getAttribute("userName").toString();
    }

    protected final String getEmailFromSession(HttpSession session) {
        return session.getAttribute("email").toString();
    }
}
