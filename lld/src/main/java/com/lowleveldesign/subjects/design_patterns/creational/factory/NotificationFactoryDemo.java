package com.lowleveldesign.subjects.design_patterns.creational.factory;

public final class NotificationFactoryDemo {
    private NotificationFactoryDemo() {
    }

    public static void run() {
        Notification notification = NotificationFactory.create("email");
        System.out.println("Factory: " + notification.send("learner@example.com", "Welcome"));
    }
}
