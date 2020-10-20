package com.company;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class Board {
    private int d;
    private ArrayList<Integer> checkerboard;
    private int size;
    private int location;
    private int moves;
    private int targetLocation;
    private boolean solvable = false;
    private Node startNode;
    private Stack<Node> tree;

    public enum Direction {
        North(0),
        South(1),
        East(2),
        West(3);

        private int val;

        Direction(int i) {
            val = i;
        }

        public int getVal() { return val; }
    }

    public Board() {
        Random rnd = new Random();
        //d=4;
        d = 5 + rnd.nextInt(16); //<- TRUE BOARD
        size = d * d;
        checkerboard = new ArrayList<>(d * d);
        fillBoard();
        location = 0;
        moves = 0;

        displayBoard();
        ArrayList<Integer> _path= new ArrayList<>();
        _path.add(0);
        startNode = new Node(null, 0, checkerboard.get(0), _path);
        tree = new Stack<>();
        solve();


    }

    private void printBorder() {
        System.out.println("=====================================");
    }

    public boolean getSolvable() {
        return solvable;
    }

    public void displaySolution(){
        if(!solvable){
            System.out.println("Error: Cannot display unsolvable board");
            return;
        }

        Stack<Node> solution = tree;
        System.out.println("===========SOLUTION=======================");

        while(!solution.isEmpty()){
            displayBoard(solution.pop().Position);
        }
        System.out.println("===========END OF SOLUTION================");
    }

    public void displayBoard() {
        printBorder();
        System.out.println("Current Moves: " + moves);
        displayBoard(location);
        printBorder();
    }

    private void displayBoard(int loc) {
        for(int i = 0; i < checkerboard.size(); i++){
            if (i % d == 0) {
                System.out.println("");
            }
            if (i == loc) {
                System.out.print("\t" + ">" + checkerboard.get(i));
            } else {
                System.out.print("\t" + checkerboard.get(i));
            }
        }
        System.out.println("");
    }

    public void getInput() {
        System.out.println("Which Direction would you like to go in? (North, South, East or West)");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        switch (input) {
            case "North" -> move(Direction.North);
            case "South" -> move(Direction.South);
            case "East" -> move(Direction.East);
            case "West" -> move(Direction.West);
        }

    }

    private void fillBoard() {
        Random rnd = new Random();
        for(int i = 0; i < size; i++){
            checkerboard.add(1 + rnd.nextInt(d - 1));
        }
        targetLocation = 1 + rnd.nextInt(size - 1);
        checkerboard.set(targetLocation, 0);

    }

    public boolean checkWin() {
        return checkerboard.get(location) == 0;

    }


    private void solve() {

        tree.push(startNode);

        int value = startNode.Value;
        if (value == 0) {
            solvable = true;
            return;
        }

        Node n;
        int fakeMoveIndex;
        while(!solvable) {
            if(tree.isEmpty()){
                return;
            }
            n = tree.peek();
            fakeMoveIndex = fakeMove(Direction.South, n.Position);

            if ( fakeMoveIndex > -1 && (n.lastDir == null || n.lastDir.getVal() < Direction.South.getVal()) && ! n.Path.contains(fakeMoveIndex)) {
                int southPos = fakeMoveIndex;
                int southVal = checkerboard.get(southPos);
                n.SouthChild = new Node(n, southPos, southVal, new ArrayList<>(n.Path));
                n.lastDir = Direction.South;
                tree.push(n.SouthChild);
                if (southVal == 0) {
                    solvable = true;
                    break;
                }
                continue;
            }

            fakeMoveIndex = fakeMove(Direction.West, n.Position);

            if ( fakeMoveIndex > -1 && !n.Path.contains(fakeMoveIndex) && (n.lastDir == null || n.lastDir.getVal() < Direction.West.getVal())) {
                int westPos = fakeMoveIndex;
                int westVal = checkerboard.get(westPos);
                n.WestChild = new Node(n, westPos, westVal, new ArrayList<>(n.Path));
                n.lastDir = Direction.West;
                tree.push(n.WestChild);
                if (westVal == 0) {
                    solvable = true;
                    break;
                }
                continue;
            }

            fakeMoveIndex = fakeMove(Direction.North, n.Position);

            if (fakeMoveIndex > -1 && !n.Path.contains(fakeMoveIndex) && (n.lastDir == null || n.lastDir.getVal() < Direction.North.getVal())) {
                int northPos = fakeMoveIndex;
                int northVal = checkerboard.get(northPos);
                n.NorthChild = new Node(n, northPos, northVal, new ArrayList<>(n.Path));
                n.lastDir = Direction.North;
                tree.push(n.NorthChild);
                if (northVal == 0) {
                    solvable = true;
                    break;
                }
                continue;
            }

            fakeMoveIndex = fakeMove(Direction.East, n.Position);

            if (fakeMoveIndex > -1 && !n.Path.contains(fakeMoveIndex) && (n.lastDir == null || n.lastDir.getVal() < Direction.East.getVal())) {
                int eastPos = fakeMoveIndex;
                int eastVal = checkerboard.get(eastPos);
                n.EastChild = new Node(n, eastPos, eastVal, new ArrayList<>(n.Path));
                n.lastDir = Direction.East;
                tree.push(n.EastChild);
                if (eastVal == 0) {
                    solvable = true;
                    break;
                }
                continue;
            }
            if(!tree.isEmpty()){
                tree.pop();
            }
        }

    }

    private boolean validateMove(Direction dir, int oldLocation, int newLocation) {
        if (newLocation < 0 || newLocation > size - 1) {
            return false;
        } else if (dir == Direction.East || dir == Direction.West) {
            int one = (oldLocation + 1) / d;
            int two = (newLocation) / d;

            if (one != two) {
                return false;
            }
        }
        return true;
    }

    private int fakeMove(Direction dir, int _location) {
        int steps = checkerboard.get(_location);
        switch (dir) {
            case North:
                steps = steps * -d;
                break;
            case South:
                steps = steps * d;
                break;
            case East:
                break;
            case West:
                steps = steps * -1;
                break;
        }
        int newLocation = _location + steps;
        if (!validateMove(dir, _location, newLocation)) {
            return -1;
        }
        checkWin();
        return newLocation;
    }

    public void move(Direction dir) {
        int steps = checkerboard.get(location);
        switch (dir) {
            case North:
                steps = steps * -d;
                break;
            case South:
                steps = steps * d;
                break;
            case East:
                break;
            case West:
                steps = steps * -1;
                break;
        }
        int newLocation = location + steps;
        if (!validateMove(dir, location, newLocation)) {
            return;
        }
        location = newLocation;
        moves++;
        checkWin();
    }
}
