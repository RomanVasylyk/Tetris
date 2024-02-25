package application;

import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;
import java.util.Random;

public class Tetromino {
    private int[][] shape;
    private Color color;
    private static int columns;
    private int x, y;
    private static final Random random = new Random();
    private static int blockSize;

    public Tetromino(int[][] shape, Color color, int blockSize, int columns) {
        this.shape = shape;
        this.color = color;
        this.columns = columns;
        this.blockSize = blockSize;
        this.x = columns / 2 - shape[0].length / 2;
        this.y = -shape.length;
    }

    public int[][] getShape() {
        return shape;
    }

    public int getColorId() {
        if (color.equals(Color.CYAN)) return 1;
        else if (color.equals(Color.BLUE)) return 2;
        else if (color.equals(Color.ORANGE)) return 3;
        else if (color.equals(Color.YELLOW)) return 4;
        else if (color.equals(Color.GREEN)) return 5;
        else if (color.equals(Color.PURPLE)) return 6;
        else if (color.equals(Color.RED)) return 7;
        return 0; 
    }

    public static Color getColorById(int id) {
        switch (id) {
            case 1: return Color.CYAN;
            case 2: return Color.BLUE;
            case 3: return Color.ORANGE;
            case 4: return Color.YELLOW;
            case 5: return Color.GREEN;
            case 6: return Color.PURPLE;
            case 7: return Color.RED;
            default: return Color.BLACK; 
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void moveLeft(int[][] gameBoard) {
        if (canMove(-1, 0, gameBoard)) {
            x--;
        }
    }

    public void moveRight(int[][] gameBoard) {
        if (canMove(1, 0, gameBoard)) {
            x++;
        }
    }

    public void rotate(int[][] gameBoard) {
        int[][] rotatedShape = rotateShape(shape);
        if (canPlace(rotatedShape, x, y, gameBoard)) {
            shape = rotatedShape;
        }
    }

    public boolean moveDown(int[][] gameBoard) {
        if (canMove(0, 1, gameBoard)) {
            y++;
            return true;
        }
        return false;
    }


    public boolean canPlace(int[][] gameBoard) {
        return canPlace(shape, x, y, gameBoard);
    }

    private boolean canMove(int deltaX, int deltaY, int[][] gameBoard) {
        return canPlace(shape, x + deltaX, y + deltaY, gameBoard);
    }

    private boolean canPlace(int[][] shape, int x, int y, int[][] gameBoard) {
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    int newX = x + col;
                    int newY = y + row;
                    if (newX < 0 || newX >= columns || newY >= gameBoard.length) {
                        return false;
                    }
                    if (newY >= 0 && gameBoard[newY][newX] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    private int[][] rotateShape(int[][] originalShape) {
        int[][] newShape = new int[originalShape[0].length][originalShape.length];
        for (int row = 0; row < originalShape.length; row++) {
            for (int col = 0; col < originalShape[row].length; col++) {
                newShape[col][originalShape.length - row - 1] = originalShape[row][col];
            }
        }
        return newShape;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    gc.fillRect((x + col) * blockSize, (y + row) * blockSize, blockSize, blockSize);
                }
            }
        }
    }

    public static Tetromino getRandomTetromino() {
        int type = random.nextInt(7);
        switch (type) {
            case 0: return new Tetromino(Shapes.I_SHAPE, Color.CYAN, blockSize, columns);
            case 1: return new Tetromino(Shapes.J_SHAPE, Color.BLUE, blockSize, columns);
            case 2: return new Tetromino(Shapes.L_SHAPE, Color.ORANGE, blockSize, columns);
            case 3: return new Tetromino(Shapes.O_SHAPE, Color.YELLOW, blockSize, columns);
            case 4: return new Tetromino(Shapes.S_SHAPE, Color.GREEN, blockSize, columns);
            case 5: return new Tetromino(Shapes.T_SHAPE, Color.PURPLE, blockSize, columns);
            case 6: return new Tetromino(Shapes.Z_SHAPE, Color.RED, blockSize, columns);
            default: throw new IllegalStateException("Invalid Tetromino type");
        }
    }
}
