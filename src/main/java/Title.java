package com.example.snake11;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Title extends Pane {
    private Text text;

    public void snakeTitle(String name) {
        String spread = "";
        for (char c : name.toCharArray()) {
            spread += c + " ";
        }
        text = new Text(spread);
        text.setFont(Font.font("comic.ttf"));
        text.setFill(Color.WHITE);
        text.setEffect(new DropShadow(30, Color.BLACK));
        getChildren().addAll(text);
    }
    public double getTitleWidth() {return text.getLayoutBounds().getWidth();}
    public double getTitleHeight() {return text.getLayoutBounds().getHeight();}
}


