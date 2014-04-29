package com.github.aseara.invokedynamic;


import java.lang.invoke.MethodHandle;

import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;

/**
 * Created with IntelliJ IDEA.
 * User: aseara
 * Date: 2014/4/28
 * Time: 15:16
 */
public class MethodHandlerTest {

    static class ClassA {
        @SuppressWarnings("unused")
        public void println(String s) {
            System.out.println(s);
        }
    }

    public static void main(String[] args) throws Throwable{
        Object obj = System.currentTimeMillis() % 2 == 0 ? System.out : new ClassA();
        getPrintlnMH(obj).invokeExact(obj.getClass().getCanonicalName() + " :icyPhoenix");
    }

    public static MethodHandle getPrintlnMH(Object receiver) throws Throwable {
        MethodType mt = MethodType.methodType(void.class, String.class);

        return lookup().findVirtual(receiver.getClass(), "println", mt).bindTo(receiver);
    }
}
