package com.sakatakoichi.mock;

public class Service {

    private final Target t;

    public Service(Target t) {
        this.t = t;
    }

    public boolean doService() {
        return t.doRandom() == 0;
    }

}
