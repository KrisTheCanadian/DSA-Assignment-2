package com.company;

public class Main {


    public static void main(String[] args) {
        Board board = new Board();
        //System.out.println(board.getSolvable());
        board.displayBoard();
        //board.displaySolution();
        System.out.println((board.getSolvable()));
        //gameLoop(board);
    }
    public static void gameLoop(Board board){
        board.displayBoard();
        board.getInput();
        if(!board.checkWin()){
            gameLoop(board);
        }
        else{
            System.out.println("YOU HAVE WON!!!!!");
        }
    }
}
