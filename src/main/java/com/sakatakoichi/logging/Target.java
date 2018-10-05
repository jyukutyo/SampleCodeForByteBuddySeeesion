package com.sakatakoichi.logging;

public class Target {

    public void foo() {
        System.out.println("foo!");
    }

    public static void main(String[] args) {
        new Target().foo();
    }
}
