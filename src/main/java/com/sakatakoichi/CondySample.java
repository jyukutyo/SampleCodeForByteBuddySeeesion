package com.sakatakoichi;

import java.nio.file.Paths;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import net.bytebuddy.utility.JavaConstant;

public class CondySample {

    public static void main(String[] args) throws Exception {
        DynamicType.Unloaded<TargetClass> unloaded = new ByteBuddy()
                .subclass(TargetClass.class)
                .method(isDeclaredBy(TargetClass.class))
                .intercept(FixedValue.value(JavaConstant.Dynamic.ofInvocation(CondyClass.class.getMethod("make"))))
                .make();
        unloaded
                .saveIn(Paths.get("./temp").toFile());

        TargetClass targetClass = unloaded
                .load(TargetClass.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded()
                .getDeclaredConstructor()
                .newInstance();

        System.out.println(targetClass.targetMethod());
    }

    public static class TargetClass {
        public Object targetMethod() {
            return null;
        }
    }

    public static class CondyClass {
        public static CondyClass make() {
            return new CondyClass();
        }
    }

}
