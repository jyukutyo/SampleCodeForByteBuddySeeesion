package com.sakatakoichi.mock;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ServiceTest {

    @Test
    void doService_True_Without_Mock() {
        assertTrue(new Service(new Target()).doService());
    }

    @Test
    void doService_True_With_Mock() throws Exception {
        Target t = new ByteBuddy()
                .subclass(Target.class)
                .method(ElementMatchers.named("doRandom"))
                .intercept(FixedValue.value(0))
                .make()
                .load(getClass().getClassLoader())
                .getLoaded()
                .getDeclaredConstructor().newInstance();

        assertTrue(new Service(t).doService());
    }
}