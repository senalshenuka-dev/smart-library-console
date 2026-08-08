package com.smartlibrary.model;

import java.time.LocalDateTime;

/**
 * Notification message.
 */
public class Notification {
    private final String title;
    private final String body;
    private final LocalDateTime sentAt;

    public Notification(String title, String body) {
        this.title = title;
        this.body = body;
        this.sentAt = LocalDateTime.now();
    }

    public String getTitle() { return title; }
    public String getBody() { return body; }
    public LocalDateTime getSentAt() { return sentAt; }
}