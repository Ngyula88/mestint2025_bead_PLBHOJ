package com.amoba;

public class AIVsAI {
    public static void main(String[] args) {
        int ai1Wins = 0, ai2Wins = 0, draws = 0;

        for (int game = 0; game < 100; game++) {
            AmobaAI ai1 = new AmobaAI();
            AmobaAI ai2 = new AmobaAI();
            Board board = new Board();
            boolean turn = true;

            while (!board.isGameOver()) {
                int move;
                if (turn) move = ai1.chooseMove(board.getBoard());
                else move = ai2.chooseMove(board.getBoard());

                board.makeMove(move, turn ? 1 : -1);
                turn = !turn;
            }

            int result = board.checkWinner();
            if (result == 1) ai1Wins++;
            else if (result == -1) ai2Wins++;
            else draws++;
        }

        System.out.println("AI1 győzelmek: " + ai1Wins);
        System.out.println("AI2 győzelmek: " + ai2Wins);
        System.out.println("Döntetlenek: " + draws);
    }
}
