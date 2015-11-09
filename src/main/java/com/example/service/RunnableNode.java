package com.example.service;


import com.example.model.Node;

import java.io.IOException;

public class RunnableNode extends Thread {

    private Node node;

    public RunnableNode(Node node) {
        this.node = node;
    }

    @Override
    public void run() {
        try {
            long time = RequestMeasurer.getTimeOfRequest(node.getUrl());
            node.addSuccessfulAttempt(time);
        } catch (IOException e) {
            node.addUnsuccessfulAttempt();
        }
    }

    public Node getNode() {
        return node;
    }
}
