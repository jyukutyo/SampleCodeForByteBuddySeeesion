package com.sakatakoichi.logging;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

public class LoggingAgent {

    public static void premain(String args, Instrumentation inst) {

//        inst.addTransformer(new ClassFileTransformer() {
//            @Override
//            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
//
//                // className is delimited '/' not '.'
//                if (className.startsWith("com/sakatakoichi/mock")) {
//                    TypePool pool = TypePool.Default.ofClassPath();
//                    return new ByteBuddy()
//                            .rebase(pool.describe(className.replace('/', '.')).resolve(),
//                                    ClassFileLocator.ForClassLoader.ofClassPath())
//                            .method(ElementMatchers.nameStartsWith("do"))
//                            .intercept(MethodDelegation.to(LoggingInterceptor.class).andThen(SuperMethodCall.INSTANCE))
//                            .make()
//                            .getBytes();
//                }
//
//                return classfileBuffer;
//            }
//        });

        new AgentBuilder.Default()
                .type(ElementMatchers.nameStartsWith("com.sakatakoichi.mock"))
                .transform(new AgentBuilder.Transformer() {
                    @Override
                    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule) {
                        return builder.method(ElementMatchers.nameStartsWith("do"))
                                .intercept(MethodDelegation.to(LoggingInterceptor.class).andThen(SuperMethodCall.INSTANCE));
                    }
                })
                .installOn(inst);
    }

    public static class LoggingInterceptor {
        public static void intercept(@Origin Method m) {
            System.out.println("Call " + m.getName() + " method");
        }
    }
}
