package com.company;

import java.util.ArrayList;

public class Node {

    public Node ParentNode = null;

    public Node WestChild = null;
    public Node EastChild = null;
    public Node NorthChild = null;
    public Node SouthChild = null;

    public ArrayList<Integer> Path;

    public int Value;
    public int Position;
    public Board.Direction lastDir = null;

    Node(Node parentNode, int position, int value, ArrayList<Integer> pathList){
        ParentNode = parentNode;
        Position = position;
        Value = value;
        Path = pathList;
        Path.add(Position);
    }
}