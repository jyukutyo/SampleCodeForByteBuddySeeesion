package com.sakatakoichi.mock;

import java.util.Random;

public class Target {

    Random r = new Random();

    public int doRandom() {
        return r.nextInt(10);
    }

}