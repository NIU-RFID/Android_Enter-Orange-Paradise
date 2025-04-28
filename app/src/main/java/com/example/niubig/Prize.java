package com.example.niubig;

public class Prize {
    private String name;
    private double probability;  // 中獎機率

    public Prize(String name, double probability) {
        this.name = name;
        this.probability = probability;
    }

    public String getName() {
        return name;
    }

    public double getProbability() {
        return probability;
    }
}
