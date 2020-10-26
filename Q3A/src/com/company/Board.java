package com.company;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Board {
    private int d;
    private ArrayList<Integer> checkerboard;
    private int size;
    private int location;
    private int moves;
    private int targetLocation;
    private boolean solvable = false;

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
        d = 5 + rnd.nextInt(16); //<- TRUE BOARD
        size = d * d;
        checkerboard = new ArrayList<>(d * d);
        fillBoard(0);
        location = 0;
        moves = 0;

        displayBoard();

        Node val = SolveNew(new Node(null, 0, checkerboard.get(0)), null, new ArrayList<>());

        System.out.println(val == null ? "No path" : "Has path");
    }

    private void displayPath(Node n) {
        if (n == null) { return; }
        displayBoard(0, n.Position);
        displayPath(n.ParentNode);
    }

    private void printBorder() {
        System.out.println("=====================================");
    }

    public boolean getSolvable() {
        return solvable;
    }

    public void displayBoard() {
        printBorder();
        System.out.println("Current Moves: " + moves);
        displayBoard(0, location);
        printBorder();
    }

    private void displayBoard(int i, int loc) {
        if (i == loc) {
            System.out.print("\t" + ">" + checkerboard.get(i++));
        } else {
            System.out.print("\t" + checkerboard.get(i++));
        }
        if (i % d == 0) {
            System.out.println("");
        }
        if (i < size) {
            displayBoard(i, loc);
        }
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

    private void fillBoard(int i) {
        Random rnd = new Random();
        checkerboard.add(1 + rnd.nextInt(d - 1));
        i++;
        if (i < size) {
            fillBoard(i);
        } else {
            targetLocation = 1 + rnd.nextInt(size - 1); //we do not want the first start on a zero.
            checkerboard.set(targetLocation, 0);
            return;
        }
    }

    public boolean checkWin() {
        return checkerboard.get(location) == 0;

    }


    private Node SolveNew(Node n, Direction lastMove, ArrayList<Integer> posList) {
        int value = n.Value;
        if (value == 0)
        {
            solvable = true;
            return n;
        }

        posList.add(n.Position);

        boolean canGoWest = lastMove != Direction.East && fakeMove(Direction.West, n.Position) > -1 && !posList.contains(fakeMove(Direction.West, n.Position)); // time O(n)
        boolean canGoEast = lastMove != Direction.West && fakeMove(Direction.East, n.Position) > -1 && !posList.contains(fakeMove(Direction.East, n.Position)); // time O(n)
        boolean canGoNorth = lastMove != Direction.South && fakeMove(Direction.North, n.Position) > -1 && !posList.contains(fakeMove(Direction.North, n.Position)); // time O(n)
        boolean canGoSouth = lastMove != Direction.North && fakeMove(Direction.South, n.Position) > -1 && !posList.contains(fakeMove(Direction.South, n.Position)); // time O(n)

        if (canGoWest) {
            int westPos = fakeMove(Direction.West, n.Position);
            int westVal = checkerboard.get(westPos);
            n.WestChild = new Node(n, westPos, westVal);
            var node = SolveNew(n.WestChild, Direction.West, new ArrayList<>(posList)); // time O(n), space O(n)
            if (node != null)
            {
                return node;
            }
            else
            {
                canGoWest = false;
            }
        }

        if (canGoEast) {
            int eastPos = fakeMove(Direction.East, n.Position);
            int eastVal = checkerboard.get(eastPos);
            n.EastChild = new Node(n, eastPos, eastVal);
            Node node = SolveNew(n.EastChild, Direction.East, new ArrayList<>(posList)); // time O(n), space O(n)
            if (node != null) {
                return node;
            }
            else {
                canGoEast = false;
            }
        }

        if (canGoNorth) {
            int northPos = fakeMove(Direction.North, n.Position);
            int northVal = checkerboard.get(northPos);
            n.NorthChild = new Node(n, northPos, northVal);
            Node node = SolveNew(n.NorthChild, Direction.North, new ArrayList<>(posList)); // time O(n), space O(n)
            if (node != null) {
                return node;
            }
            else {
                canGoNorth = false;
            }
        }

        if (canGoSouth) {
            int southPos = fakeMove(Direction.South, n.Position);
            int southVal = checkerboard.get(southPos);
            n.SouthChild = new Node(n, southPos, southVal);
            Node node = SolveNew(n.SouthChild, Direction.South, new ArrayList<>(posList)); // time O(n), space O(n)
            if (node != null) {
                return node;
            }
            else {
                canGoSouth = false;
            }
        }

        if (!canGoWest && !canGoEast && !canGoNorth && !canGoSouth) {
            return null;
        }

        return null;
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
