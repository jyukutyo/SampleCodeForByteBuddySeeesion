package com.sakatakoichi.logging;

import com.sakatakoichi.mock.Service;
import com.sakatakoichi.mock.Target;
import net.bytebuddy.agent.ByteBuddyAgent;

public class Main {
    public static void main(String[] args) {
        System.out.println(new Service(new Target()).doService());
    }
}
