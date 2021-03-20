package com.helloworld.classes;

import org.bukkit.Location;

import java.io.Serializable;

public class SP_Location implements Serializable {
    private double x,y,z;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public SP_Location(Location l) {
        this.x = l.getX();
        this.y = l.getY();
        this.z = l.getZ();

    }

    public SP_Location(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
