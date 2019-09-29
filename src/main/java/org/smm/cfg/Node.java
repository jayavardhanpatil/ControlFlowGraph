package org.smm.cfg;

import java.util.HashSet;
import java.util.LinkedList;

public class Node {

    public int nodeNumber;
    public LinkedList<Node> childNodes = new LinkedList<>();
    public HashSet<Integer> codeLines = new HashSet<Integer>();

    public Node(int num, int line) {
        this.nodeNumber = num;
        this.codeLines.add(line);
    }



}
