package org.smm.cfg;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.smm.cfg.App.codelines;

public class ControlFlow {

    public static int nodeNumber = 0;
    public static int lineNo = 0;

    public Node buildGraph(){
        //First build root Node
        Node root = new Node(nodeNumber, 0);
        nodeNumber++;
        return null;
    }

    public Node childNode(Node parent){
        Node statementNode = null;
        LinkedList<Node> previousNodeIsBranch = new LinkedList<>();
        lineNo++;

        while (true){
            if(lineNo >= codelines.size()){
                return null;
            }

            if(codelines.get(lineNo).contains("if")){
                Node newNode = new Node(nodeNumber,lineNo);
                nodeNumber+=1;

                if(!previousNodeIsBranch.isEmpty()){
                    addParents(newNode, previousNodeIsBranch);
                    previousNodeIsBranch.clear();
                }
                else{
                    parent.childNodes.add(newNode);
                }
                parent = newNode;
                newNode = childNode(newNode);
                if(newNode == null) return null;
                previousNodeIsBranch.add(newNode);
                statementNode = null;
            }
        }
    }

    private void addParents(Node newNode, LinkedList<Node> lastNodesInBranch) {

        for(int i=0; i<lastNodesInBranch.size();i++){
            lastNodesInBranch.get(i).childNodes.add(newNode);
        }

    }

}
