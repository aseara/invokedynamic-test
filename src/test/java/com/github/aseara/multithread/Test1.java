package com.github.aseara.multithread;

import org.junit.Test;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created with IntelliJ IDEA.
 * User: aseara
 * Date: 2014/4/29
 * Time: 16:02
 */

public class Test1 {

    @Test
    public void test() {
        check("a", "b");
        ConcurrentLinkedQueue<Runnable> queue;
    }

    public void check(Object a, Object b) {
        String r = (a != (a = b)) ? "no equal" : "equal";
        System.out.println(r + ": " + a);
    }
}
