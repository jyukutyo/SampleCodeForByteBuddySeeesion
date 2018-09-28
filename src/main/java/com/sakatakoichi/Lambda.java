package com.sakatakoichi;

import java.util.function.Consumer;

public class Lambda {
    public static void main(String[] args) {
        Consumer c = (s) -> System.out.println("test");
        c.accept("test");
    }
}