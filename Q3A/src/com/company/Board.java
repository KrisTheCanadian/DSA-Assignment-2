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

    public enum direction {
        North,
        South,
        East,
        West
    }

    public Board(){
        Random rnd = new Random();
        //d = 5 + rnd.nextInt(16); //<- TRUE BOARD
        d = 4; //for testing (simplification)
        size = d*d;
        checkerboard = new ArrayList<>(d*d);
        fillBoard(0);
        location = 0;
        moves = 0;
        solve(new ArrayList<Integer>(), 0, direction.North);
    }
    private void printBorder(){
        System.out.println("=====================================");
    }

    public boolean getSolvable(){return solvable;}

    public void displayBoard(){
        printBorder();
        System.out.println("Current Moves: " + moves);
        displayBoard(0, location);
        printBorder();
    }

    private void displayBoard(int i, int loc){
        if(i == loc){
            System.out.print( "\t" + ">" + checkerboard.get(i++));
        }
        else{
            System.out.print( "\t" + checkerboard.get(i++));
        }
        if(i%d == 0){
            System.out.println("");
        }
        if(i < size){
            displayBoard(i, loc);
        }
    }

    public void getInput() {
        System.out.println("Which Direction would you like to go in? (North, South, East or West)");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        switch (input) {
            case "North" -> move(direction.North);
            case "South" -> move(direction.South);
            case "East" -> move(direction.East);
            case "West" -> move(direction.West);
        }

    }

    private void fillBoard(int i){
        Random rnd = new Random();
        checkerboard.add(1 + rnd.nextInt(d -1));
        i++;
        if(i < size){
            fillBoard(i);
        }
        else{
            targetLocation = 1 + rnd.nextInt(size - 1); //we do not want the first start on a zero.
            checkerboard.set(targetLocation, 0);
            return;
        }
    }
    public boolean checkWin(){
        return checkerboard.get(location) == 0;

    }

    private void solve(ArrayList<Integer> lastLocations, int _location, direction lastDir){
        if(solvable){
            return;
        }
        if(checkerboard.get(_location) == 0){
            displayBoard(0, _location); //for testing (must remove)
            printBorder(); // for testing (must remove)
            solvable = true;
            return;
        }
        int count = 0;
        for (int location : lastLocations){
            if(location == _location){
                count++;
                if(count == 50){
                    return;
                }
            }
        }

        lastLocations.add(_location);
        int temp = 0;
        if( lastDir != direction.South && (temp = fakeMove(direction.North, _location)) >= 0){
            solve(lastLocations, temp, direction.North);
        }
        if( lastDir != direction.East && (temp = fakeMove(direction.West, _location)) >= 0){
            solve(lastLocations, temp, direction.West);
        }
        if( lastDir != direction.West && (temp = fakeMove(direction.East, _location)) >= 0){
            solve(lastLocations, temp, direction.East);
        }
        if( lastDir != direction.North && (temp = fakeMove(direction.South, _location)) >= 0){
            solve(lastLocations, temp, direction.South);
        }
        if(!solvable){
            solve(lastLocations, 0, null);
        }
    }

    private boolean validateMove(direction dir, int oldLocation, int newLocation){
        if(newLocation < 0 || newLocation > size - 1){
            return false;
        }
        else if(dir == direction.East || dir == direction.West){
            if((oldLocation+1)/d != (newLocation+ 1) /d )
            {
                return false;
            }
        }
        return true;
    }

    private int fakeMove(direction dir, int _location){
        int steps = checkerboard.get(_location);
        switch(dir){
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
        if(!validateMove(dir, _location, newLocation)){
            return -1;
        }
        checkWin();
        return newLocation;
    }

    public void move(direction dir){
        int steps = checkerboard.get(location);
        switch(dir){
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
        if(!validateMove(dir, location, newLocation)){
            return;
        }
        location = newLocation;
        moves++;
        checkWin();
    }
}
