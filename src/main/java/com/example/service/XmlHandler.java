package com.example.service;

import com.example.model.Node;
import com.example.model.NodeRoot;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XmlHandler {

    private File file;
    private JAXBContext jaxbContext;

    public XmlHandler() throws JAXBException {
        file = new File("nodes.xml");
        jaxbContext = JAXBContext.newInstance(NodeRoot.class);
    }

    public void saveInFile(Node[] nodes) throws JAXBException {
        NodeRoot nodeRoot = new NodeRoot();
        nodeRoot.setNodes(nodes);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(nodeRoot, file);
    }

    public Node[] retrieveFromFile() throws JAXBException {
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        NodeRoot nodeRoot = (NodeRoot) jaxbUnmarshaller.unmarshal(file);

        return nodeRoot.getNodes();
    }

}
