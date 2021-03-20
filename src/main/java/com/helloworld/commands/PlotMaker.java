package com.helloworld.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlotMaker implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // Here we need to give items to our player
            if (args.length == 0) {
                ItemStack is = new ItemStack(Material.GOLDEN_HOE, 1);
                ItemMeta newMeta = is.getItemMeta();
                newMeta.setDisplayName("PlotMaker Tool");
                //newMeta.addEnchant(Enchantment.LOYALTY, 4, false);
                is.setItemMeta(newMeta);
                player.getInventory().addItem(is);
                player.sendMessage("[PlotMaker] Right click two opposing corners to make a valid 4 side polygon. I will tell you if the shape is invalid :)");
            }else{
                switch(args[0]){
                    case "pvp":
                        ItemStack is = new ItemStack(Material.GOLDEN_HOE, 1);
                        ItemMeta newMeta = is.getItemMeta();
                        newMeta.setDisplayName("PlotMaker PVP");

                        is.setItemMeta(newMeta);
                        player.getInventory().addItem(is);
                        player.sendMessage("[PlotMaker PVP] Right click two opposing corners to make a valid 4 side polygon. I will tell you if the shape is invalid :)");

                        break;
                    case "structures":
                        ItemStack is2 = new ItemStack(Material.GOLDEN_HOE, 1);
                        ItemMeta newMeta2 = is2.getItemMeta();
                        newMeta2.setDisplayName("PlotMaker Structure");

                        is2.setItemMeta(newMeta2);
                        player.getInventory().addItem(is2);
                        player.sendMessage("[PlotMaker Structure] Right click two opposing corners to make a valid 4 side polygon. I will tell you if the shape is invalid :)");

                        break;

                }
            }

        }
        return true;
    }
}
