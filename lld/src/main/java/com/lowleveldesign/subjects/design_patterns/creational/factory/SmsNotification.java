package com.lowleveldesign.subjects.design_patterns.creational.factory;

public final class SmsNotification implements Notification {
    @Override
    public String send(String recipient, String message) {
        return "SMS to " + recipient + ": " + message;
    }
}
