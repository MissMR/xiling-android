package com.dodomall.ddmall.ddui.config.poster;

import com.dodomall.ddmall.ddui.config.UIConfig;

import java.io.Serializable;

public class PosterElement implements Serializable {

    private int height = 0;
    private int width = 0;
    private int x = 0;
    private int y = 0;

    public PosterElement() {

    }

    public PosterElement(int x, int y, int width, int height) {
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
