package com.sakatakoichi.logging;

import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;

public class LoggingMainWithoutAgent {
    public static void main(String[] args) {
        ByteBuddyAgent.install();
        new AgentBuilder.Default()
                .type(ElementMatchers.named("com.sakatakoichi.logging.Target"))
                .transform((builder, typeDescription, classLoader, javaModule) -> {
                    return builder.method(ElementMatchers.named("foo"))
                            .intercept(MethodDelegation.to(LoggingAgent.LoggingInterceptor.class)
                                    .andThen(SuperMethodCall.INSTANCE));
                })
                .installOnByteBuddyAgent();

        new Target().foo();
    }
}
