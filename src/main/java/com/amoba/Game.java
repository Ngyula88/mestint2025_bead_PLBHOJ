package com.amoba;

import java.util.Scanner;

public class Game {
    private Board board = new Board();
    private AmobaAI ai = new AmobaAI();
    private Scanner scanner = new Scanner(System.in);

    public void play() {
        System.out.println("Amőba játék - AI vs. Ember");
        board.printBoard();

        while (!board.isGameOver()) {
            // Ember lép
            int humanMove;
            do {
                System.out.print("Ember lép (0-8): ");
                humanMove = scanner.nextInt();
            } while (!board.makeMove(humanMove, -1));
            board.printBoard();

            if (board.isGameOver()) break;

            // AI lép
            int aiMove = ai.chooseMove(board.getBoard());
            board.makeMove(aiMove, 1);
            System.out.println("AI lép: " + aiMove);
            board.printBoard();
        }

        int winner = board.checkWinner();
        if (winner == 1) System.out.println("AI nyert!");
        else if (winner == -1) System.out.println("Ember nyert!");
        else System.out.println("Döntetlen!");

        ai.train(board.getHistory());
    }
}
