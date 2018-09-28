package com.sakatakoichi;

import java.nio.file.Paths;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import net.bytebuddy.utility.JavaConstant;

public class CondySample2 {
    public static void main(String[] args) throws Exception {
        Class<?> bootstrap = Class.forName("com.sakatakoichi.DynamicConstantBootstrap");
        DynamicType.Unloaded<Foo> unloaded = new ByteBuddy()
                .subclass(Foo.class)
                .method(isDeclaredBy(Foo.class))
                .intercept(FixedValue.value(JavaConstant.Dynamic.bootstrap("foo", bootstrap.getMethod("bootstrap",
                        Class.forName("java.lang.invoke.MethodHandles$Lookup"),
                        String.class,
                        Class.class,
                        int.class,
                        long.class,
                        float.class,
                        double.class,
                        String.class,
                        Class.class,
                        Class.forName("java.lang.invoke.MethodHandle"),
                        Class.forName("java.lang.invoke.MethodType")), 42, 42L, 42f, 42d, "foo", Object.class, methodHandle(), methodType())))
                        .make();
        unloaded.saveIn(Paths.get("./temp").toFile());
    }

    public static class Foo {

        public Object foo() {
            return null;
        }
    }

    private static Object methodHandle() throws Exception {
        return Class.forName("java.lang.invoke.MethodHandles$Lookup")
                .getMethod("findConstructor", Class.class, Class.forName("java.lang.invoke.MethodType"))
                .invoke(Class.forName("java.lang.invoke.MethodHandles").getMethod("lookup").invoke(null), CondySample2.class, methodType());
    }

    private static Object methodType() throws Exception {
        return Class.forName("java.lang.invoke.MethodType").getMethod("methodType", Class.class).invoke(null, void.class);
    }

}
