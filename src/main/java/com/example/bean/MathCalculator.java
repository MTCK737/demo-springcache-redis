package com.example.bean;

import org.springframework.cache.annotation.Cacheable;

public class MathCalculator {

    @Cacheable(value = {"prettyGirl2"}, key = "{#i, #j}", unless = "#result == false")
    public int div(int i, int j){
        System.out.println("MathCalculator.div" + "=====" + i + "=====" +j);
        return i/j;
    }
}
