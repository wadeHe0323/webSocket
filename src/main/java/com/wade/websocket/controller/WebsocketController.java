package com.wade.websocket.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebsocketController {

    private final static Logger log = LoggerFactory.getLogger(WebsocketController.class);

    @RequestMapping("/websocketClient")
    public String sendMessage () {
        try {
            log.info("跳轉到 websocket 頁面");
            return "client";
        } catch (Exception e) {
            log.info("頁面挑轉發生錯誤：{}", e.getMessage());
            return "error";
        }
    }
}
