package com.sakatakoichi.logging;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;

public class LoggingAgent {

    public static void premain(String args, Instrumentation inst) {

        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
                // className is delimited by '/' not '.'
                if (className.startsWith("com/sakatakoichi/logging/Target")) {
                    TypePool pool = TypePool.Default.ofClassPath();
                    return new ByteBuddy()
                            // we can't write Target.class because this code load Target class
                            .rebase(pool.describe(className.replace('/', '.')).resolve(),
                                    ClassFileLocator.ForClassLoader.ofClassPath())
                            .method(ElementMatchers.named("foo"))
                            .intercept(MethodDelegation.to(LoggingInterceptor.class).andThen(SuperMethodCall.INSTANCE))
                            .make()
                            .getBytes();
                }

                return classfileBuffer;
            }
        });
//        new AgentBuilder.Default()
//                .type(ElementMatchers.named("com.sakatakoichi.logging.Target"))
//                .transform(new AgentBuilder.Transformer() {
//                    @Override
//                    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule) {
//                        return builder.method(ElementMatchers.named("foo"))
//                                .intercept(MethodDelegation.to(LoggingInterceptor.class).andThen(SuperMethodCall.INSTANCE));
//                    }
//                })
//                .installOn(inst);
    }

    public static class LoggingInterceptor {
        public static void intercept(@Origin Method m) {
            System.out.println("Call " + m.getName() + " method");
        }
    }
}
