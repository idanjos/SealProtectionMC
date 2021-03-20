package com.helloworld.commands;

import com.helloworld.PlotMaker;
import com.helloworld.SealProtector;
import com.helloworld.classes.SP_Plot;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class DisownPlot implements CommandExecutor {
    HashMap<UUID, ArrayList<SP_Plot>> plots;

    public DisownPlot(HashMap<UUID, ArrayList<SP_Plot>> plots) {
        this.plots = plots;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId(); // this should work
        if(!plots.containsKey(uuid)){
            player.sendMessage("[PlotMaker] "+ String.format("You have 0 plots to disown"));
            return true;
        }
        player.sendMessage("[PlotMaker] "+ String.format("You have disown %d plots",plots.get(uuid).size()));
        plots.put(uuid, new ArrayList<>());
        plots.put(PlotMaker.PVP, new ArrayList<>());
        plots.put(PlotMaker.STRUCTURES, new ArrayList<>());
        return true;
    }
}
