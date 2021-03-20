package com.helloworld.classes;

import org.bukkit.Location;

import java.io.Serializable;
import java.util.UUID;

public class SP_PlotMaker implements Serializable {
    private UUID player;
    private SP_Location left;
    private SP_Location right;
    private double width;
    private double height;

    private static boolean valueInRange(double value, double min, double max) {
        return (value >= min) && (value <= max);
    }

    public static boolean isOverlapped(SP_Plot A, SP_Plot B) {
        boolean xOverlap = valueInRange(A.getMark().getX(), B.getMark().getX(), B.getMark().getX() + B.getWidth()) ||
                valueInRange(B.getMark().getX(), A.getMark().getX(), A.getMark().getX() + A.getWidth());

        boolean yOverlap = valueInRange(A.getMark().getZ(), B.getMark().getZ(), B.getMark().getZ() + B.getHeight()) ||
                valueInRange(B.getMark().getZ(), A.getMark().getZ(), A.getMark().getZ() + A.getHeight());
        return xOverlap && yOverlap;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    private int lastInt = 0;

    public SP_PlotMaker(UUID player) {
        this.player = player;
    }

    public UUID getPlayer() {
        return player;
    }

    public void setPlayer(UUID player) {
        this.player = player;
    }

    public SP_Location getLeft() {
        return left;
    }

    public void setLeft(Location left) {
        this.left = new SP_Location(left.getX(), left.getY(), left.getZ());
    }

    public SP_Location getRight() {
        return right;
    }

    public void setRight(Location block) {
        this.right = new SP_Location(block.getX(), block.getY(), block.getZ());

        if(left.getX()>right.getX()){
            double temp = right.getX();
            right.setX(left.getX());
            left.setX(temp);
        }
        if(left.getZ()>right.getZ()){
            double temp = right.getZ();
            right.setZ(left.getZ());
            left.setZ(temp);
        }

    }

    // The event triggers twice
    public int validBlock(Location block) {
        if (left != null && right != null) {


            return 4;
        }

        if (left == null) {

            return 0;
        }

        if (left.getX() == block.getX() || left.getZ() == block.getZ()) {

            return 1;
        }

        if (right == null) {

            return 2;
        }


        return 3;
    }


    public void clear() {
        this.left = this.right = null;
        this.width = 0;
        this.height = 0;
    }


}
