package com.github.aseara.fj;

import org.junit.Test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: aseara
 * Date: 2014/5/4
 * Time: 9:45
 */
public class CountTaskTest {

    @Test
    public void test() throws Exception {
        ForkJoinPool forkJoinPool = new ForkJoinPool(5);
        CountTask task = new CountTask(1, 6);
        Future<Integer> result = forkJoinPool.submit(task);
        System.out.println(result.get());
    }
}
