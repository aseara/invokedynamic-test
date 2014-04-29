package com.github.aseara.lambda;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: aseara
 * Date: 2014/4/29
 * Time: 10:07
 */
public class LambdaTest {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("1", "12", "123");
        List<Integer> lengths = names.stream()
                .map(String::length)
                .collect(Collectors.toList());
        System.out.println(lengths);
    }
}
