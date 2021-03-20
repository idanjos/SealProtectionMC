package com.helloworld.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class SP_Player implements Serializable {
    private UUID id;
    private ArrayList<SP_Plot> plots;
    private boolean isProtected;
    private ArrayList<SP_Chest> chests;

    public boolean isProtected() {
        return isProtected;
    }

    public SP_Player(UUID id){
        plots = new ArrayList<>();
        chests = new ArrayList<>();
        this.id = id;
        isProtected = true;
    }
    public void Broken(){
        isProtected = false;
    }

    public void addChest(SP_Chest chest){
        chests.add(chest);
    }

    public void setProtected(boolean aProtected) {
        isProtected = aProtected;
    }

    public void addPlot(){

    }
}
