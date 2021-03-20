package com.helloworld.classes;

import org.bukkit.Location;

import java.io.Serializable;
import java.util.UUID;

public class SP_Chest implements Serializable {
    private UUID player;
    private SP_Location location;

    public SP_Location getLocation() {
        return location;
    }

    public void setLocation(SP_Location location) {
        this.location = location;
    }

    public void setPlayer(UUID player) {
        this.player = player;
    }

    public SP_Chest(UUID player, SP_Location location) {
        this.player = player;
        this.location = location;
    }

    public UUID getPlayer() {
        return player;
    }
    public boolean isEqual(Location block){
        return location.getX() == block.getX() && location.getY() == block.getY() && location.getZ() == block.getZ();
    }

}
