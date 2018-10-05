package com.sakatakoichi.springboot;

import java.io.File;

import net.bytebuddy.agent.ByteBuddyAgent;

public class Main {

    public static void main(String[] args) throws Exception {
        ByteBuddyAgent.attach(new File("./target/springboot-agent-1.0-SNAPSHOT.jar"), args[0]);
    }
}
