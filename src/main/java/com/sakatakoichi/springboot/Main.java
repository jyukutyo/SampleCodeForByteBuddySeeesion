package com.sakatakoichi.springboot;

import java.io.File;

import net.bytebuddy.agent.ByteBuddyAgent;

public class Main {

    public static void main(String[] args) throws Exception {
        ByteBuddyAgent.attach(new File("/Users/koichi.sakata/code/oraclecodeone2018/target/springboot-agent-1.0-SNAPSHOT.jar"), args[0]);
//        VirtualMachine vm = VirtualMachine.attach(args[0]);
//        vm.loadAgent("/Users/koichi.sakata/code/oraclecodeone2018/target/springboot-agent-1.0-SNAPSHOT.jar");
//        vm.detach();
    }
}
