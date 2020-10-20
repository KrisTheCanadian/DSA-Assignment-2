package com.company;

public class Node {

    public Node ParentNode = null;

    public Node WestChild = null;
    public Node EastChild = null;
    public Node NorthChild = null;
    public Node SouthChild = null;

    public int Value;
    public int Position;

    Node(Node parentNode, int position, int value){
        ParentNode = parentNode;
        Position = position;
        Value = value;
    }
}
