package com.company;

public class Main {

    public static void main(String[] args) {
        Board board = new Board();

        Board.Direction a = Board.Direction.East;

        int val = a.getVal();

        //System.out.println(board.getSolvable());
        board.displayBoard();
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