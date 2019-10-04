package org.smm.cfg;

import java.io.*;
import java.util.*;

public class Application {

    private static int nodeNumbers = 0;
    private static int lineNo = -1;
    private static TreeMap<Integer, String> codeLineMap = new TreeMap<>();
    private static HashMap<Integer, ArrayList<String>> nodeStatementMap = new HashMap<>();

    public Application() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        Application app = new Application();
        app.readProgram();

    }

    private StringBuilder readProgram() throws IOException {
        File file = new File("program.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder builder = new StringBuilder();
        readCode(reader);
        parseProgram();

        reader.close();
        return builder;
    }


    private void readCode(BufferedReader reader) throws IOException {
        String line = "";
        int lineCount = 0;
        while ((line = reader.readLine()) != null){
            if(line.isEmpty()){
                continue;
            }
            codeLineMap.put(lineCount, line.trim());
            lineCount++;
        }
    }

    private static ArrayList<Node> elseIfLadderNodes = new ArrayList<>();
    private static ArrayList<Node> switchCaseNodes = new ArrayList<>();

    private void parseProgram() throws IOException {

        Node root = new Node(nodeNumbers, 0);
        ArrayList<String> statement = new ArrayList<>();
        statement.add("start");
        nodeStatementMap.put(0, statement);
        nodeNumbers++;
        createChildNode(root);
        print_nodes(root);

        LinkedList<String> commands = new LinkedList<>();

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", "dot -Tpng cfg.dot -o cfg.png").start();


        //System.out.println(builder.toString());
        System.out.println(codeLineMap);
    }


    private static boolean isElseIfladder = false;
    private static LinkedList<Node> switchNode = new LinkedList<>();
    private Node createChildNode(Node parent) {

        Node statement_node = null ;
        ArrayList<Node> lastNodesInBranch = new ArrayList<>();
        lineNo++;

        for(;lineNo<codeLineMap.size();){
            String line = codeLineMap.get(lineNo).trim();
            //Create new Node when we find "{" in the code
            //Make sure the each curly brace ends in new line.
            if(line.startsWith("//")){
                lineNo++;
                continue;
            }

            if(line.endsWith("{") || line.contains("case") || line.contains("default")) {

                if(line.contains("switch")){
                    Node newNode = new Node(nodeNumbers, lineNo);
                    nodeNumbers += 1;
                    switchNode.add(newNode);
                    if (!lastNodesInBranch.isEmpty()) {
                        connectParents(newNode, lastNodesInBranch);
                        lastNodesInBranch.clear();
                    } else {
                        parent.childNodes.add(newNode);
                    }
                    parent = newNode;
                    newNode = createChildNode(parent);
                    if (newNode == null) return null;
                    connectParents(newNode, switchCaseNodes);
                    switchNode.removeLast();
                    lastNodesInBranch.add(newNode);
                }

                else if (line.contains("else") && !line.contains("else if") && !isElseIfladder) {

                    Node newNode = createChildNode(parent);
                    if (newNode == null) return null;
                    lastNodesInBranch.add(newNode);
                }
                else if(line.contains("else") && isElseIfladder && !line.contains("else if")){
                    Node newNode = createChildNode(parent);
                    isElseIfladder = false;
                    //Create New Node as end of the switch statement
                    Node endOfSwitchStatement = new Node(nodeNumbers, lineNo);
                    nodeNumbers++;
                    elseIfLadderNodes.add(newNode);
                    connectParents(endOfSwitchStatement, elseIfLadderNodes);
                    elseIfLadderNodes.clear();
                    lastNodesInBranch.clear();
                    parent = endOfSwitchStatement;
                    //lastNodesInBranch.add(parent);
                    lastNodesInBranch.add(parent);
                }else{
                    Node newNode = new Node(nodeNumbers, lineNo);
                    nodeNumbers += 1;


                    if (!lastNodesInBranch.isEmpty()) {
                        connectParents(newNode, lastNodesInBranch);
                        lastNodesInBranch.clear();
                    } else {
                        parent.childNodes.add(newNode);
                    }

                    parent = newNode;

                    if(line.contains("for") || (line.contains("while") && !line.contains("}while"))){
                        lastNodesInBranch.add(parent);
                    }

                    newNode = createChildNode(newNode);

                    if (newNode == null) {
                        return null;
                    }

                    if(line.contains("else if")){
                        isElseIfladder = true;
                        elseIfLadderNodes.add(newNode);
                    }

                    if(line.contains("for") || (line.contains("while") && !line.contains("}while")) || line.contains("do")){
                        newNode.childNodes.add(parent);
                        parent = newNode;
                    }

                    if(line.contains("else if")){
                        lastNodesInBranch.add(parent);
                    }
                    else if(line.contains("if")){
                        if(!codeLineMap.get(lineNo+1).contains("else")){
                            lastNodesInBranch.add(parent);
                        }
                        if(codeLineMap.get(lineNo+1).contains("else if")){
                            lastNodesInBranch.add(parent);
                            elseIfLadderNodes.add(newNode);
                        }else {
                            lastNodesInBranch.add(newNode);
                        }
                    }

                    if(line.contains("case")){
                        switchCaseNodes.add(newNode);
                        lastNodesInBranch.add(switchNode.getLast());
                    }else if(line.contains("default")){
                        return newNode;
                    }
                    statement_node = null;
                }
            } else{
                if(line.trim().isEmpty()){
                    lineNo++; continue;
                }

                if(statement_node == null){
                    statement_node = new Node(nodeNumbers, lineNo);
                    nodeNumbers++;
                    if(!lastNodesInBranch.isEmpty()){
                        connectParents(statement_node, lastNodesInBranch);
                        lastNodesInBranch.clear();
                    }
                    else {
                        parent.childNodes.add(statement_node);
                    }
                    parent = statement_node;
                }else{
                    statement_node.codeLines.add(lineNo);
                }

                if(line.startsWith("}")) {
                    return statement_node;
                }

                if(line.contains("break;")){
                    return statement_node;
                }

                if(line.contains("continue;")){
                    return statement_node;
                }

            }
            lineNo++;
        }
        return statement_node;
    }

    private void connectParents(Node newNode, ArrayList<Node> lastNodesInBranch) {

        for(int i=0; i<lastNodesInBranch.size();i++){
            lastNodesInBranch.get(i).childNodes.add(newNode);
        }
    }

    public ArrayList<Integer> printChild(Node node){
        ArrayList<Integer> nodes = new ArrayList<>();
        System.out.print(node.nodeNumber+"-->(");
        for(int i=0;i<node.childNodes.size();i++){
            System.out.print(node.childNodes.get(i).nodeNumber);
            if(node.childNodes.size() - i >1)System.out.print(",");
            nodes.add(node.childNodes.get(i).nodeNumber);
        }
        System.out.println(")");
        System.out.print(node.nodeNumber);
        System.out.println(node.codeLines);
        return nodes;
    }


    private static HashSet<String> visitedEdges = new HashSet<>();
    File file = new File("cfg.dot");
    FileWriter writer = new FileWriter(file);
    private void print_nodes(Node root) throws IOException {

            writer.append("digraph G {").append("\n").append("label = \"Control Flow Graph\";\n");
            for(Node node: root.childNodes){
                if (visitedEdges.contains(root.nodeNumber + "->" + node.nodeNumber)) {
                    continue;
                }
                    visitedEdges.add(root.nodeNumber + "->" + node.nodeNumber);
                    /*System.out.print(root.nodeNumber+" -> ");
                    for(Node codeline : node.childNodes)
                        System.out.print(codeLineMap.get(codeline.nodeNumber));*/
                        ArrayList<String> statements = new ArrayList<>();
                        for(Integer codeLine : node.codeLines){
                            statements.add(codeLineMap.get(codeLine));
                        }

                            nodeStatementMap.put(node.nodeNumber, statements);

                        System.out.println(nodeStatementMap.get(root.nodeNumber) + "->" + nodeStatementMap.get(node.nodeNumber));
                        writer.append(nodeStatementMap.get(root.nodeNumber)+ " -> "+nodeStatementMap.get(node.nodeNumber) );
                        writer.append("\n");
                        //System.out.println(/*"Node From : " + */root.nodeNumber + " -> " + node.nodeNumber + " Code Lines " + node.codeLines);
                      // System.out.println(codeLineMap.get(root.nodeNumber) + " -> " + codeLineMap.get(node.nodeNumber));
                print_nodes(node);
        }
            writer.append("}");
            writer.flush();
    }
}
