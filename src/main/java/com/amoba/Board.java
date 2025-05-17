package com.amoba;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int[] board = new int[9];
    private List<int[]> history = new ArrayList<>();

    public boolean makeMove(int pos, int player) {
        if (pos < 0 || pos > 8 || board[pos] != 0) return false;
        board[pos] = player;
        history.add(board.clone());
        return true;
    }

    public boolean isGameOver() {
        return checkWinner() != 0 || getAvailableMoves().isEmpty();
    }

    public int checkWinner() {
        int[][] wins = {
            {0,1,2}, {3,4,5}, {6,7,8},
            {0,3,6}, {1,4,7}, {2,5,8},
            {0,4,8}, {2,4,6}
        };
        for (int[] line : wins) {
            int sum = board[line[0]] + board[line[1]] + board[line[2]];
            if (sum == 3) return 1;
            if (sum == -3) return -1;
        }
        return 0;
    }

    public List<Integer> getAvailableMoves() {
        List<Integer> moves = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (board[i] == 0) moves.add(i);
        }
        return moves;
    }

    public int[] getBoard() {
        return board.clone();
    }

    public List<int[]> getHistory() {
        return history;
    }

    public void printBoard() {
        for (int i = 0; i < 9; i++) {
            char c = switch (board[i]) {
                case 1 -> 'X';
                case -1 -> 'O';
                default -> '.';
            };
            System.out.print(c + ((i % 3 == 2) ? "\n" : " "));
        }
        System.out.println();
    }
}
