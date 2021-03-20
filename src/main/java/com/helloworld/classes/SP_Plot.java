package com.helloworld.classes;

import java.io.Serializable;
import java.util.ArrayList;

public class SP_Plot implements Serializable {
    private SP_Location mark;
    private SP_Location left;
    private SP_Location right;
    private double height;
    private ArrayList<SP_Chest> chests;

    public SP_Location getLeft() {
        return left;
    }

    public void setLeft(SP_Location left) {
        this.left = left;
    }

    public SP_Location getRight() {
        return right;
    }

    public void setRight(SP_Location right) {
        this.right = right;
    }

    private double width;

    public SP_Plot(SP_Location mark, double height, double width) {
        this.mark = mark;
        this.height = height;
        this.width = width;
        chests = new ArrayList<>();
    }

    public SP_Plot(SP_PlotMaker sp) {
        this.mark = sp.getLeft();
        this.left = sp.getLeft();
        this.right = sp.getRight();
        this.height = sp.getHeight();
        this.width = sp.getWidth();
        chests = new ArrayList<>();
    }

    public SP_Location getMark() {
        return mark;
    }

    public void setMark(SP_Location mark) {
        this.mark = mark;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void addChest(SP_Chest chest){
        chests.add(chest);
    }

    public ArrayList<SP_Chest> getChests() {
        return chests;
    }
}
