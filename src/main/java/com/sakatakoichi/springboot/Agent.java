package com.sakatakoichi.springboot;

import java.lang.instrument.Instrumentation;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

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

        Map<String, Class> classMap = Arrays.stream(inst.getAllLoadedClasses()).collect(Collectors.toMap(c -> c.getName(), c -> c, (c1, c2) -> c1));
        String target = "sample.jsp.WelcomeController";

        // we need to use a classloader that the target application is using
        ClassLoader classLoader = classMap.get(target).getClassLoader();

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
