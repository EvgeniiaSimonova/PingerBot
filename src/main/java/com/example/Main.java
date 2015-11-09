package com.example;

import com.example.model.Node;
import com.example.service.NodesHandlerThread;
import com.example.service.XmlHandler;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static NodesHandlerThread thread;
    private static XmlHandler xmlHandler;

    public static void main(String[] args) {
        Node[] nodesFromFile = null;
        try {
            xmlHandler = new XmlHandler();
            nodesFromFile = xmlHandler.retrieveFromFile();
        } catch (JAXBException e) {
            System.out.println("An error occurred in process of retrieving nodes: " + e.getMessage());
        }
        if (nodesFromFile != null) {
            thread = new NodesHandlerThread(nodesFromFile);
        } else {
            thread = new NodesHandlerThread();
        }

        thread.start();
        handleCommands(System.in, System.out);
        thread.stopUpdating();
    }

    private static void handleCommands(InputStream in, PrintStream out) {
        Scanner scanner = new Scanner(in);
        boolean exit = false;

        while (!exit) {
            out.print(">");
            String command = scanner.next();
            switch (command) {
                case "help":
                    out.println(getHelp());
                    break;
                case "add":
                    out.print("URL: ");
                    String url = scanner.next();
                    out.print("Name: ");
                    String name = scanner.next();
                    thread.addNode(name, url);
                    break;
                case "delete":
                    try {
                        thread.removeNode(scanner.nextInt());
                    } catch (InputMismatchException e) {
                        out.println("Invalid input of 'delete' command");
                    }
                    break;
                case "get":
                    try {
                        Node n = thread.getNodeById(scanner.nextInt());
                        if (n != null) out.println(getNodeList(n));
                        else out.println("The node was not found");
                    } catch (InputMismatchException e) {
                        out.println("Invalid input of 'get' command");
                    }
                    break;
                case "show":
                    out.println(getNodeList(thread.getNodes()));
                    break;
                case "exit":
                    exit = true;
                case "save":
                    try {
                        xmlHandler.saveInFile(thread.getNodes());
                    } catch (JAXBException e) {
                        System.out.println("An error occurred in process of saving nodes: " + e.getMessage());
                    }
                    break;
            }
        }
        scanner.close();
    }

    private static String getNodeList(Node... nodes) {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%-5s %-15s %-80s %-15s %-15s %-15s %-15s %-15s\n", "ID", "Name", "URL", "Attempts", "OK Attempts", "Last Time", "Best Time", "Worst Time"));
        for (Node node: nodes) {
            result.append(String.format("%-5s %-15s %-80s %-15d %-15d %-15d %-15d %-15d\n",
                    (node.getId() + ") "),
                    node.getName(),
                    node.getUrl(),
                    node.getAttempts(),
                    node.getSuccessfulAttempts(),
                    node.getLastTime(),
                    node.getBestTime(),
                    node.getWorstTime()));
        }

        return result.toString();
    }

    private static String getHelp() {
        return "Url checker is application.\n"
                + "\nadd"
                + "\n\tAdd a new task:"
                + "\n\t-URL (string in format http://google.ru:80/)"
                + "\n\t-name"
                + "\ndelete [id]"
                + "\n\tDelete a task by ID"
                + "\nget [id]"
                + "\n\tShow a task by ID"
                + "\nshow"
                + "\n\tShow all tasks"
                + "\nsave"
                + "\n\tSave TaskList"
                + "\nexit"
                +"\n\tExit the program";
    }

}
