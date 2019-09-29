package org.smm.cfg;

import sun.awt.image.ImageWatched;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Application {

    private static LinkedList<Node> nodes = new LinkedList<Node>();
    private static int nodeNumbers = 0;
    private static int lineNo = 0;

    public static void main(String[] args) throws IOException {

        Application app = new Application();
        app.readProgram();

    }

    private StringBuilder readProgram() throws IOException {
        File file = new File("program.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder builder = new StringBuilder();
        parseProgram(reader, nodes, builder);

        reader.close();
        return builder;
    }

    private LinkedList<Node> parseProgram(BufferedReader reader, LinkedList<Node> linkedList, StringBuilder builder) throws IOException {
        String line = "";

        Node root = new Node(nodeNumbers, 0);
        nodeNumbers++;
        createChildNode(reader, root);
        print_nodes(root);


        /*while ((line = reader.readLine()) !=null){
            if(line.isEmpty()){
                continue;
            }

            if(line.contains("{")){ //startNode
                // If it is statement of nodes then create one block of nodes
                Node newNode = new Node(nodeNumbers,lineNo);
                nodeNumbers+=1;


                if(builder.length() != 0){
                    node = new Node(linkedList.size(), builder.toString());
                    node.setPrevious(linkedList.getLast());
                    linkedList.add(node);
                    builder = new StringBuilder();
                }

                if (linkedList.size() == 0){
                    builder.append("start Node").append("\n");
                    linkedList.add(node);
                    node.setPrevious(null);
                }

                builder.append(line.trim());
                node = new Node(linkedList.size(), builder.toString());
                node.setPrevious(linkedList.getLast());
                linkedList.add(node);
                return parseProgram(reader, linkedList, new StringBuilder());
            }

            if(line.contains("}")){
                if(builder.length() != 0){
                    Node node = new Node(linkedList.size(), builder.toString());
                    linkedList.add(node);
                    node.setPrevious(linkedList.getLast());
                }

                return parseProgram(reader, linkedList, new StringBuilder());
                //Do something if it reaches the end of the block
            }

            if(line.charAt(line.length()-1) == ';'){
                builder.append(line.trim()).append("\n");
                return parseProgram(reader, linkedList, builder);
            }
        }

        return linkedList;
    }*/
        return null;
    }


    private Node createChildNode(BufferedReader reader, Node parent) throws IOException {

        Node statement_node = null ;
        ArrayList<Node> lastNodesInBranch = new ArrayList<>();
        lineNo++;

        String line = "";
        while ((line = reader.readLine().trim())!=null){
            if(lineNo == 1){
                if(line.startsWith("{")){
                    lineNo++;
                    continue;
                }
            }
            //Create new Node when we find "{" in the code
            if(line.endsWith("{")) {
                if(line.contains("else") && !line.contains("else if")){
                    Node newNode = createChildNode(reader, parent);
                    if(newNode == null) return null;
                    lastNodesInBranch.add(newNode);
                }else {
                    Node newNode = new Node(nodeNumbers, lineNo);
                    nodeNumbers += 1;
                    if (!lastNodesInBranch.isEmpty()) {
                        connectParents(newNode, lastNodesInBranch);
                        lastNodesInBranch.clear();
                    } else {
                        parent.childNodes.add(newNode);
                    }

                    parent = newNode;
                    newNode = createChildNode(reader, newNode);

                    if (newNode == null) {
                        return null;
                    }

                    lastNodesInBranch.add(newNode);
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

    private void print_nodes(Node root){
        if(root.childNodes == null || root.childNodes.size() == 0){
            return;
        }else{
            for(Node node: root.childNodes){
                System.out.println("Node From : "+root.nodeNumber + "-->" + node.nodeNumber + " Code Lines "+ node.codeLines);
                print_nodes(node);
            }
        }
    }
}
