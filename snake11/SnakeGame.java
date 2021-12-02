package com.example.snake11;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class SnakeGame extends Application {

    private static final int WIDTH = 900;
    private static final int HEIGHT = 900;
    private static final int ROWS = 30;
    private static final int COLUMNS = 30;
    private static final int SQUARE_SIZE = WIDTH / ROWS;

    static Image image1;
    static Image image2;
    static Image image3;
    static Image image4;
    static Image image5;
    static Image image6;
    static Image image7;
    static Image image8;
    {
        try {
            image1 = new Image(new FileInputStream("src/SnakeFruits/ic_apple.png"));
            image2 = new Image(new FileInputStream("src/SnakeFruits/ic_berry.png"));
            image3 = new Image(new FileInputStream("src/SnakeFruits/ic_cherry.png"));
            image4 = new Image(new FileInputStream("src/SnakeFruits/ic_coconut_.png"));
            image5 = new Image(new FileInputStream("src/SnakeFruits/ic_orange.png"));
            image6 = new Image(new FileInputStream("src/SnakeFruits/ic_peach.png"));
            image7 = new Image(new FileInputStream("src/SnakeFruits/ic_pomegranate.png"));
            image8 = new Image(new FileInputStream("src/SnakeFruits/ic_watermelon.png"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static final ArrayList<Image> FOOD_IMAGE = new ArrayList<Image>();


    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;

    private GraphicsContext gc;
    private ArrayList<Point> snakeBody = new ArrayList();
    private Point snakeHead;
    private Image foodImage;
    private int foodX;
    private int foodY;
    private boolean gameOver;
    private int currentDirection;
    private int score = 0;


    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Snake");
        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        stage.setFullScreen(false);  // to discuss later on
        gc = canvas.getGraphicsContext2D();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode code = event.getCode();
                if (code == KeyCode.RIGHT || code == KeyCode.D) {
                    if (currentDirection != LEFT) {
                        currentDirection = RIGHT;
                    }
                } else if (code == KeyCode.LEFT || code == KeyCode.A) {
                    if (currentDirection != RIGHT) {
                        currentDirection = LEFT;
                    }
                } else if (code == KeyCode.UP || code == KeyCode.W) {
                    if (currentDirection != DOWN) {
                        currentDirection = UP;
                    }
                } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                    if (currentDirection != UP) {
                        currentDirection = DOWN;
                    }
                }
            }
        });
        for (int i=0; i<5; i++) {
            snakeBody.add(new Point(4, ROWS/2));
        }
        generateFood();
        snakeHead = snakeBody.get(0);

        Timeline timeLine = new Timeline(new KeyFrame(Duration.millis(50), e -> run(gc)));
        timeLine.setCycleCount(Animation.INDEFINITE);
        timeLine.play();
    }

    private void run(GraphicsContext gc) {
        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("Digital-7", 70));
            gc.fillText("Game Over", WIDTH/3.5, HEIGHT/2);
            return;
        }
        drawBackground(gc);
        drawFood(gc);
        drawSnake(gc);
        drawScore();

        for (int i = snakeBody.size()-1; i>= 1; i--) {
            snakeBody.get(i).x = snakeBody.get(i-1).x;
            snakeBody.get(i).y = snakeBody.get(i-1).y;
        }

        switch (currentDirection) {
            case RIGHT:
                moveRight();
                break;
            case LEFT:
                moveLeft();
                break;
            case UP:
                moveUp();
                break;
            case DOWN:
                moveDown();
                break;
        }

        eatFood();
        gameOver();

    }

    private void drawBackground(GraphicsContext gc) {
        for (int i=0; i<ROWS; i++) {
            for (int j=0; j<COLUMNS; j++){
                if ((i+j) % 2 == 0) {
                    gc.setFill(Color.web("AAD751"));
                } else {
                    gc.setFill(Color.web("A2D149"));
                }
                gc.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }

    private void generateFood() {
        start:
        while (true) {
            foodX = (int) (Math.random() * ROWS);
            foodY = (int) (Math.random() * COLUMNS);

            for (Point snake : snakeBody){
                if (snake.getX() == foodX && snake.getY() == foodY) {
                    continue start;
                }
            }

            FOOD_IMAGE.add(image1);
            FOOD_IMAGE.add(image2);
            FOOD_IMAGE.add(image3);
            FOOD_IMAGE.add(image4);
            FOOD_IMAGE.add(image5);
            FOOD_IMAGE.add(image6);
            FOOD_IMAGE.add(image7);
            FOOD_IMAGE.add(image8);

            foodImage = FOOD_IMAGE.get((int) (Math.random() * FOOD_IMAGE.size()));

            break;
        }
    }

    private void drawFood(GraphicsContext gc) {

        gc.drawImage(foodImage, foodX * SQUARE_SIZE, foodY * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

    }

    private void drawSnake(GraphicsContext gc) {
        gc.setFill(Color.web("4674E9"));
        gc.fillRoundRect(snakeHead.getX() * SQUARE_SIZE, snakeHead.getY() * SQUARE_SIZE, SQUARE_SIZE-1, SQUARE_SIZE-1,35, 35);

        for (int i=1; i< snakeBody.size(); i++) {
            gc.fillRoundRect(snakeBody.get(i).getX() * SQUARE_SIZE, snakeBody.get(i).getY() * SQUARE_SIZE, SQUARE_SIZE-1,
                    SQUARE_SIZE-1, 20,20);
        }
    }

    private void moveRight() {
        snakeHead.x++;
    }

    private void moveLeft() {
        snakeHead.x--;
    }

    private void moveUp() {
        snakeHead.y--;
    }

    private void moveDown() {
        snakeHead.y++;
    }

    public void gameOver() {
        if (snakeHead.x < 0 || snakeHead.y < 0 || snakeHead.x * SQUARE_SIZE >= WIDTH || snakeHead.y * SQUARE_SIZE >= HEIGHT) {
            gameOver = true;
        }

        for (int i=1; i<snakeBody.size(); i++) {
            if (snakeHead.x == snakeBody.get(i).getX() && snakeHead.y == snakeBody.get(i).getY()) {
                gameOver = true;
                break;
            }
        }
    }

    private void eatFood() {
        if (snakeHead.getX() == foodX && snakeHead.getY() == foodY) {
            snakeBody.add(new Point(-1,-1));
            generateFood();
            score += 5;
        }
    }

    private void drawScore() {
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Digital-7", 35));
        gc.fillText("Score: " + score,10,35);
    }

    public static void main(String[] args) {
        launch(args);
    }
}