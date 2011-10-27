package com.skype.connector.osx;

import java.util.concurrent.CountDownLatch;

public class SkypeFrameworkTest {
    public static void main(String[] args) throws Exception {
        SkypeFramework.init("Skype4Java");

        System.out.println("isRunning: " + SkypeFramework.isRunning());
        System.out.println("isAvabileable: " + SkypeFramework.isAvailable());
    }
}
