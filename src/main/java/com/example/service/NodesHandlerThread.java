package com.example.service;

import com.example.Configuration;
import com.example.model.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NodesHandlerThread extends Thread {

    private ConcurrentLinkedQueue<ActiveNode> nodesQueue;
    private List<RunnableNode> runnableNodes;
    private boolean isStopped;

    public NodesHandlerThread() {
        nodesQueue = new ConcurrentLinkedQueue<>();
        runnableNodes = new ArrayList<>();
        isStopped = false;
    }

    public NodesHandlerThread(Node... nodes) {
        this();
        for (Node node: nodes) {
            addNode(node);
        }
    }

    private void addNode(Node node) {
        nodesQueue.add(new ActiveNode(node.getName(), node.getUrl(), node.getAttempts(), node.getSuccessfulAttempts(),
                node.getLastTime(), node.getBestTime(), node.getWorstTime()));
    }

    public void addNode(String name, String url) {
        nodesQueue.add(new ActiveNode(name, url));
    }

    public void removeNode(int id) {
        ActiveNode[] nodes = nodesQueue.toArray(new ActiveNode[0]);
        for (ActiveNode node: nodes) {
            if (node.getId() == id) {
                nodesQueue.remove(node);
                break;
            }
        }
    }

    public Node[] getNodes() {
        return nodesQueue.toArray(new Node[0]);
    }

    public Node getNodeById(int id) {
        Node[] nodes = nodesQueue.toArray(new Node[0]);
        for (Node node: nodes) {
            if (node.getId() == id) {
                return node;
            }
        }

        return null;
    }

    public void stopUpdating() {
        isStopped = true;
    }

    @Override
    public void run() {
        while (!isStopped) {
            try {
                Iterator<RunnableNode> iterator = runnableNodes.iterator();
                while (iterator.hasNext()) {
                    RunnableNode runnableNode = iterator.next();
                    if (!runnableNode.isAlive()) {
                        ((ActiveNode) runnableNode.getNode()).setIsActive(false);
                        iterator.remove();
                    }
                }

                while (runnableNodes.size() < Configuration.MAX_THREADS_COUNT && nodesQueue.size() != runnableNodes.size()) {
                    ActiveNode currentNode = nodesQueue.remove();
                    if (currentNode != null) {
                        if (!currentNode.isActive()) {
                            RunnableNode runnableNode = new RunnableNode(currentNode);
                            runnableNodes.add(runnableNode);
                            runnableNode.start();
                            currentNode.setIsActive(true);
                        }
                        nodesQueue.add(currentNode);
                    }
                }

                Thread.sleep(Configuration.UPDATE_TIME);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static class ActiveNode extends Node {

        boolean isActive;

        public ActiveNode(String name, String url) {
            super(name, url);
            isActive = false;
        }

        public ActiveNode(String name, String url, int attempts, int successfulAttempts, long lastTime, long bestTime, long worstTime) {
            super(name, url);
            isActive = false;
            setDynamicParameters(attempts, successfulAttempts, lastTime, bestTime, worstTime);
        }

        public boolean isActive() {
            return isActive;
        }

        public void setIsActive(boolean isActive) {
            this.isActive = isActive;
        }
    }
}
