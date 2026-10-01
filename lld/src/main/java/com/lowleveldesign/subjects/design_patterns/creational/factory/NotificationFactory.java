package com.lowleveldesign.subjects.design_patterns.creational.factory;

import java.util.Locale;

public final class NotificationFactory {
    private NotificationFactory() {
    }

    public static Notification create(String channel) {
        return switch (channel.toLowerCase(Locale.ROOT)) {
            case "email" -> new EmailNotification();
            case "sms" -> new SmsNotification();
            default -> throw new IllegalArgumentException("Unsupported channel: " + channel);
        };
    }
}
