package com.example.model;

public class Sequence {
    private int uniqueId;

    public Sequence() {
        uniqueId = 0;
    }

    public int getNext() {
        return uniqueId++;
    }
}
