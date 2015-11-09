package com.example.model;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "node")
@XmlAccessorType(XmlAccessType.FIELD)
public class Node {

    private static final Sequence ID_SEQUENCE = new Sequence();

    @XmlElement
    private int id;
    @XmlElement
    private String name;
    @XmlElement
    private String url;
    @XmlElement
    private int attempts;
    @XmlElement
    private int successfulAttempts;
    @XmlElement
    private long lastTime;
    @XmlElement
    private long bestTime;
    @XmlElement
    private long worstTime;

    public Node() {}

    public Node(String name, String url) {
        this.name = name;
        this.url = url;
        attempts = 0;
        successfulAttempts = 0;
        lastTime = 0;
        bestTime = 0;
        worstTime = 0;
        id = ID_SEQUENCE.getNext();
    }

    public void addSuccessfulAttempt(long time) {
        attempts++;
        successfulAttempts++;
        lastTime = time;

        if (bestTime > time || attempts == 1) {
            bestTime = time;
        }

        if (worstTime < time) {
            worstTime = time;
        }
    }

    public void addUnsuccessfulAttempt() {
        attempts++;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getAttempts() {
        return attempts;
    }

    public int getSuccessfulAttempts() {
        return successfulAttempts;
    }

    public long getLastTime() {
        return lastTime;
    }

    public long getBestTime() {
        return bestTime;
    }

    public long getWorstTime() {
        return worstTime;
    }

    public int getId() {
        return id;
    }

    protected void setDynamicParameters(int attempts, int successfulAttempts, long lastTime, long bestTime, long worstTime) {
        this.attempts = attempts;
        this.successfulAttempts = successfulAttempts;
        this.lastTime = lastTime;
        this.bestTime = bestTime;
        this.worstTime = worstTime;
    }

    @Override
    public String toString() {
        return "Node {" +
                " id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", attempts=" + attempts +
                ", successfulAttempts=" + successfulAttempts +
                ", lastTime=" + lastTime +
                ", bestTime=" + bestTime +
                ", worstTime=" + worstTime +
                '}';
    }
}
