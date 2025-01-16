package com.frogger;
import javafx.scene.image.Image;

public class GameElement {

    int x,y,width,height;
    Image sprite;

    public GameElement(int x, int y, int width, int height, String pathToImage) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        sprite = new Image(pathToImage);
    }

}
