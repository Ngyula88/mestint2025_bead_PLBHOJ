package com.amoba;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AmobaApp extends Application {
    private static final int SIZE = 3;
    private static final int TILE = 100;

    private Board board = new Board();
    private AmobaAI ai = new AmobaAI();
    private Canvas canvas = new Canvas(SIZE * TILE, SIZE * TILE);

    @Override
    public void start(Stage stage) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawBoard(gc);

        canvas.setOnMouseClicked(this::handleClick);

        StackPane root = new StackPane(canvas);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, SIZE * TILE, SIZE * TILE);

        stage.setTitle("Amőba AI - JavaFX");
        stage.setScene(scene);
        stage.show();
    }

    private void handleClick(MouseEvent event) {
        if (board.isGameOver()) return;

        int col = (int) (event.getX() / TILE);
        int row = (int) (event.getY() / TILE);
        int index = row * SIZE + col;

        if (board.makeMove(index, -1)) {
            drawBoard(canvas.getGraphicsContext2D());

            if (!board.isGameOver()) {
                int aiMove = ai.chooseMove(board.getBoard());
                board.makeMove(aiMove, 1);
                drawBoard(canvas.getGraphicsContext2D());
            }

            if (board.isGameOver()) {
                int result = board.checkWinner();
                System.out.println(result == 1 ? "AI nyert!" : result == -1 ? "Te nyertél!" : "Döntetlen!");
                ai.train(board.getHistory());
            }
        }
    }

    private void drawBoard(GraphicsContext gc) {
        gc.setFill(Color.BEIGE);
        gc.fillRect(0, 0, SIZE * TILE, SIZE * TILE);

        gc.setStroke(Color.BLACK);
        for (int i = 1; i < SIZE; i++) {
            gc.strokeLine(i * TILE, 0, i * TILE, SIZE * TILE);
            gc.strokeLine(0, i * TILE, SIZE * TILE, i * TILE);
        }

        gc.setFont(new Font(60));
        for (int i = 0; i < 9; i++) {
            int val = board.getBoard()[i];
            if (val != 0) {
                String text = val == 1 ? "X" : "O";
                double x = (i % 3) * TILE + 25;
                double y = (i / 3) * TILE + 70;
                gc.setFill(val == 1 ? Color.RED : Color.BLUE);
                gc.fillText(text, x, y);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
