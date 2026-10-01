package com.lowleveldesign.subjects.design_patterns.creational.factory;

public final class EmailNotification implements Notification {
    @Override
    public String send(String recipient, String message) {
        return "Email to " + recipient + ": " + message;
    }
}
