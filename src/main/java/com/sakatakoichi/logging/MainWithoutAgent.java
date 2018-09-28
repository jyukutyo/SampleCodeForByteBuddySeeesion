package com.sakatakoichi.logging;

import com.sakatakoichi.mock.Service;
import com.sakatakoichi.mock.Target;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;

public class MainWithoutAgent {
    public static void main(String[] args) {
        ByteBuddyAgent.install();
        new AgentBuilder.Default()
                .type(ElementMatchers.nameStartsWith("com.sakatakoichi.mock"))
                .transform((builder, typeDescription, classLoader, javaModule) -> {
                    return builder.method(ElementMatchers.nameStartsWith("do"))
                            .intercept(MethodDelegation.to(LoggingAgent.LoggingInterceptor.class)
                                    .andThen(SuperMethodCall.INSTANCE));
                })
                .installOnByteBuddyAgent();

        System.out.println(new Service(new Target()).doService());
    }
}
