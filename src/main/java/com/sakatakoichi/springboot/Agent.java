package com.sakatakoichi.springboot;

import java.lang.instrument.Instrumentation;
import java.util.Arrays;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;

public class Agent {

    public static void agentmain(String args, Instrumentation inst) {

        System.out.println("agentmain is called");

        String target = "sample.jsp.WelcomeController";
        // we need to use a classloader that the target application is using
        ClassLoader classLoader = Arrays
                .stream(inst.getAllLoadedClasses())
                .filter(c -> target.equals(c.getName()))
                .findAny()
                .map(Class::getClassLoader)
                .get();

        ByteBuddy byteBuddy = new ByteBuddy();
        byteBuddy
                .redefine(TypePool.Default.of(classLoader).describe(target).resolve(),
                        ClassFileLocator.ForClassLoader.of(classLoader))
                .method(ElementMatchers.named("welcome"))
                .intercept(FixedValue.value("transformed"))
                .make()
                .load(classLoader, ClassReloadingStrategy.of(inst));

        new AgentBuilder.Default()
                .with(byteBuddy)
                .installOn(inst);
    }

}
