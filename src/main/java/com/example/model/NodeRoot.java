package com.example.model;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "nodes")
@XmlAccessorType(XmlAccessType.FIELD)
public class NodeRoot {

    @XmlElement(name = "node")
    private Node[] nodes = null;

    public Node[] getNodes() {
        return nodes;
    }

    public void setNodes(Node[] nodes) {
        this.nodes = nodes;
    }
}
