package com.sakatakoichi.hello;

import java.nio.file.Paths;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;

public class HelloWorld {
    public static void main(String[] args) throws Exception {
        DynamicType.Unloaded<Object> unloaded = new ByteBuddy()
                .subclass(Object.class)
                .method(ElementMatchers.named("toString"))
                .intercept(FixedValue.value("Hello World!"))
                .make();

        unloaded
                .saveIn(Paths.get("./temp").toFile());

        Class<?> subClass = unloaded
                .load(HelloWorld.class.getClassLoader())
                .getLoaded();

        System.out.println(subClass.getConstructor().newInstance().toString());
    }
}
