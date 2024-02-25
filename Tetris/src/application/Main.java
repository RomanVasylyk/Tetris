package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 600;
    private static final int BLOCK_SIZE = 30;
    private static final int ROWS = HEIGHT / BLOCK_SIZE;
    private static final int COLUMNS = WIDTH / BLOCK_SIZE;
    private static final double BASE_SPEED = 1.0;
    

    private GraphicsContext gc;
    private int[][] gameBoard = new int[ROWS][COLUMNS];
    private Tetromino currentTetromino;
    private int score = 0;
    private int level = 1;
    private boolean gameOver = false;
    private Timeline timeline;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tetris Game");
        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) {
                currentTetromino.moveLeft(gameBoard);
            } else if (e.getCode() == KeyCode.RIGHT) {
                currentTetromino.moveRight(gameBoard);
            } else if (e.getCode() == KeyCode.UP) {
                currentTetromino.rotate(gameBoard);
            } else if (e.getCode() == KeyCode.DOWN) {
                currentTetromino.moveDown(gameBoard);
                drawGame(); 
            }
        });


        primaryStage.setScene(scene);
        primaryStage.show();
        startGame();
    }

    private void startGame() {
        currentTetromino = new Tetromino(Shapes.I_SHAPE, Color.CYAN, BLOCK_SIZE, COLUMNS);
        currentTetromino.draw(gc);
        
        timeline = new Timeline(new KeyFrame(Duration.seconds(BASE_SPEED / level), e -> runGameLoop()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    
    }

    private void runGameLoop() {
    	if (!gameOver) {
            updateGame();
            drawGame(); 
        }
    }

    private void updateGame() {
        if (!currentTetromino.moveDown(gameBoard)) {
            fixTetrominoToBoard();
            checkCompleteRows();
            currentTetromino = Tetromino.getRandomTetromino();
            if (!currentTetromino.moveDown(gameBoard)) {
                gameOver = true;
            }
        }
    }




    private void fixTetrominoToBoard() {
        for (int row = 0; row < currentTetromino.getShape().length; row++) {
            for (int col = 0; col < currentTetromino.getShape()[row].length; col++) {
                if (currentTetromino.getShape()[row][col] != 0) {
                    int boardRow = currentTetromino.getY() + row;
                    int boardCol = currentTetromino.getX() + col;
                    if (boardRow >= 0 && boardRow < ROWS && boardCol >= 0 && boardCol < COLUMNS) {
                        gameBoard[boardRow][boardCol] = currentTetromino.getColorId();
                    }
                }
            }
        }
    }






    private void checkCompleteRows() {
        for (int row = 0; row < ROWS; row++) {
            boolean rowComplete = true;
            for (int col = 0; col < COLUMNS; col++) {
                if (gameBoard[row][col] == 0) {
                    rowComplete = false;
                    break;
                }
            }
            if (rowComplete) {
                removeRow(row);
                score += 10;
                level = score / 100 + 1;
            }
        }
    }

    private void removeRow(int rowToRemove) {
        for (int row = rowToRemove; row > 0; row--) {
            System.arraycopy(gameBoard[row - 1], 0, gameBoard[row], 0, COLUMNS);
        }
        gameBoard[0] = new int[COLUMNS];
    }

    private void drawGame() {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (gameBoard[row][col] != 0) {
                    gc.setFill(Tetromino.getColorById(gameBoard[row][col]));
                    gc.fillRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
        currentTetromino.draw(gc);
        drawGameInfo();
    }

    private void drawGameInfo() {
        gc.setFill(Color.BLACK); 
        gc.setFont(new Font("Arial", 20));
        gc.fillText("Score: " + score, 10, 20);
        gc.fillText("Level: " + level, 10, 40);
        if (gameOver) {
            gc.fillText("Game Over", WIDTH / 2 - 50, HEIGHT / 2);
        }
    }

}
