package com.helloworld.commands;

import com.helloworld.PlotMaker;
import com.helloworld.SealProtector;
import com.helloworld.classes.SP_PlotMaker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlotReset implements CommandExecutor {
    HashMap<UUID, SP_PlotMaker> plotmaker;

    public PlotReset(HashMap<UUID, SP_PlotMaker> plotmaker) {
        this.plotmaker = plotmaker;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId(); // this should work
        if(plotmaker.containsKey(uuid)){
            plotmaker.get(uuid).clear();
            plotmaker.get(PlotMaker.PVP).clear();
            plotmaker.get(PlotMaker.STRUCTURES).clear();
            player.sendMessage("[PlotMaker] Plot Reset!");
        }

        return true;
    }
}
