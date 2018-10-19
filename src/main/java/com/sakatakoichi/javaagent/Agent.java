package com.sakatakoichi.javaagent;

import java.lang.instrument.Instrumentation;

public class Agent {
    public static void premain(String args, Instrumentation inst) {
        System.out.println("premain method");
    }
}
