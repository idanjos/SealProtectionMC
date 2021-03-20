package com.helloworld;

import com.helloworld.classes.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import com.helloworld.SealProtector;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import com.helloworld.classes.SP_Plot;
public class PlotMaker implements Listener {
    public static UUID PVP = new UUID(0,0);
    public static UUID STRUCTURES = new UUID(1,1);
    HashMap<UUID, SP_Player> database = new HashMap<>();
    HashMap<UUID, SP_PlotMaker> plotMaker = new HashMap<>();
    HashMap<UUID, ArrayList<SP_Plot>> plots = new HashMap<>();
    Plugin plugin;

    /**
     * Reads, if the file exists, Plots.dat for plots created by the users.
     *
     * @param  main Main plugin which is in.
     * @see         Plugin
     */
    public void load(Plugin main) {
        plugin = main;
        plots.put(PVP,new ArrayList<SP_Plot>());
        plots.put(STRUCTURES,new ArrayList<SP_Plot>());
        plotMaker.put(PVP,new SP_PlotMaker(PVP));
        plotMaker.put(STRUCTURES,new SP_PlotMaker(STRUCTURES));
        try {
            File f = new File("Plots.dat");
            if (!f.exists()) {
                return;
            }
            FileInputStream fi = new FileInputStream(new File("Plots.dat"));
            ObjectInputStream oi = new ObjectInputStream(fi);

            // Read objects
            database = (HashMap<UUID, SP_Player>) oi.readObject();
            plotMaker = (HashMap<UUID, SP_PlotMaker>) oi.readObject();
            plots = (HashMap<UUID, ArrayList<SP_Plot>>) oi.readObject();

            oi.close();
            fi.close();
            if(!plots.containsKey(PVP) || plots.get(PVP) == null || !plotMaker.containsKey(PVP) || plotMaker.get(PVP) == null){
                plots.put(PVP,new ArrayList<SP_Plot>());
                plots.put(STRUCTURES,new ArrayList<SP_Plot>());
                plotMaker.put(PVP,new SP_PlotMaker(PVP));
                plotMaker.put(STRUCTURES,new SP_PlotMaker(STRUCTURES));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Saves the current plots, player seals and player plot maker.
     *
     * @see         Plugin
     */
    public void save() {
        try {
            FileOutputStream f = new FileOutputStream(new File("Plots.dat"));
            ObjectOutputStream o = new ObjectOutputStream(f);

            // Write objects to file
            o.writeObject(database);
            o.writeObject(plotMaker);
            o.writeObject(plots);
            o.close();
            f.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }

    }

    /**
     * Checks if the player is allowed to manipulate this location.
     * @param player Player UUID, unique id of the player.
     * @param block  Block/Player location
     * @return true if the given player and given location is allowed to manipulate.
     */
    public boolean manipulatePlot(UUID player, Location block) {
        for (UUID id : plots.keySet()) {

            for (SP_Plot plot : plots.get(id)) {
                if (plot.getLeft().getX() > block.getX() || plot.getLeft().getZ() > block.getZ() || plot.getRight().getX() < block.getX() || plot.getRight().getZ() < block.getZ()) {
                    continue;
                }
                //Special area
                if(!database.containsKey(id))
                    return false;
                if (!id.equals(player) && database.get(id).isProtected())
                    return false;
                return true;
            }

        }
        return true;
    }

    /**
     * Checks if the block/player location is inside a plot.
     * @param player Player UUID, unique id of the player.
     * @param block  Block/Player location
     * @return true if the given location is inside a plot
     */
    public boolean insidePlot(UUID player, Location block) {
        for (SP_Plot plot : plots.get(player)) {
            if (plot.getLeft().getX() > block.getX() || plot.getLeft().getZ() > block.getZ() || plot.getRight().getX() < block.getX() || plot.getRight().getZ() < block.getZ()) {
                continue;
            }
            return true;
        }
        return false;
    }

    /**
     * Creates and adds a chest to the player's plot.
     * @param player Player UUID, unique id of the player.
     * @param block  Block/Player location
     */
    public void addChest(UUID player, Location block) {
        for (SP_Plot plot : plots.get(player)) {
            if (plot.getLeft().getX() > block.getX() || plot.getLeft().getZ() > block.getZ() || plot.getRight().getX() < block.getX() || plot.getRight().getZ() < block.getZ()) {
                continue;
            }
            plot.addChest(new SP_Chest(player, new SP_Location(block)));
        }
    }

    /**
     * Removes the chest from the player's plot.
     * @param player Player UUID, unique id of the player.
     * @param block  Block/Player location
     */
    public void removeChest(UUID player, Location block) {
        for (SP_Plot plot : plots.get(player)) {
            if (plot.getLeft().getX() > block.getX() || plot.getLeft().getZ() > block.getZ() || plot.getRight().getX() < block.getX() || plot.getRight().getZ() < block.getZ()) {
                continue;
            }
            for (SP_Chest chest : plot.getChests()) {
                if (chest.isEqual(block)) {
                    plot.getChests().remove(chest);
                    return;
                }
            }
        }
    }

    /**
     * Checks and creates a plot from a given plotmaker of a player.
     * @param player Player UUID, unique id of the player.
     * @param sp  Player plotmaker tool, holds information of the plot.
     * @return true if the plot is valid and be created, false otherwise.
     */
    public boolean addPlot(UUID player, SP_PlotMaker sp) {
        SP_Plot spp = new SP_Plot(sp);
        for (UUID key : plots.keySet()) {
            for (SP_Plot plot : plots.get(key)) {
                if (SP_PlotMaker.isOverlapped(spp, plot)) {
                    return false;
                }
            }
        }
        if (!plots.containsKey(player)) {
            plots.put(player, new ArrayList<>());
        }
        plots.get(player).add(spp);
        return true;
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK && p.getEquipment().getItemInMainHand().getItemMeta() != null && p.getEquipment().getItemInMainHand().getItemMeta().getDisplayName().contains("Structure")) {
            switch (plotMaker.get(STRUCTURES).validBlock(event.getClickedBlock().getLocation())) {
                case 0:
                    event.getPlayer().sendMessage("[PlotMaker Structure]" + String.format("First block selected %f %f %f",
                            event.getClickedBlock().getLocation().getX(), event.getClickedBlock().getLocation().getY(),
                            event.getClickedBlock().getLocation().getZ()));
                    plotMaker.get(STRUCTURES).setLeft(event.getClickedBlock().getLocation());
                    //event.getClickedBlock().setType(Material.GLOWSTONE);
                    break;

                case 1:
                    event.getPlayer().sendMessage("[PlotMaker Structure]" + String.format("Block must not have the name X or Z coordenates of the first one. In emergency do /plotreset"));
                    break;

                case 2:
                    event.getPlayer().sendMessage("[PlotMaker Structure]" + String.format("Second block selected %f %f %f",
                            event.getClickedBlock().getLocation().getX(), event.getClickedBlock().getLocation().getY(),
                            event.getClickedBlock().getLocation().getZ()));
                    plotMaker.get(STRUCTURES).setRight(event.getClickedBlock().getLocation());
                    SP_PlotMaker sp = plotMaker.get(STRUCTURES);
                    double area = (Math.abs(sp.getLeft().getX() - sp.getRight().getX()) + 1) * (Math.abs(sp.getLeft().getZ() - sp.getRight().getZ()) + 1);
                    sp.setWidth(Math.abs(sp.getLeft().getX() - sp.getRight().getX()) + 1);
                    sp.setHeight(Math.abs(sp.getLeft().getX() - sp.getRight().getX()) + 1);
                    event.getPlayer().sendMessage("[PlotMaker Structure] " + String.format("Plot area of %f blocks", area));
                    if (addPlot(STRUCTURES, sp)) {
                        event.getPlayer().sendMessage("[PlotMaker Structure] " + String.format("Successfully created a plot!"));
                    } else {
                        event.getPlayer().sendMessage("[PlotMaker Structure] " + String.format("Failed to create plot"));
                    }
                    plotMaker.get(STRUCTURES).clear();
                    //event.getClickedBlock().setType(Material.GLOWSTONE);
                    break;
                case 4:
                    //event.getPlayer().sendMessage("[PlotMaker]" + String.format("Plot already selected. In emergency do /plotreset"));
                    break;

            }
        }
        else if (event.getAction() == Action.LEFT_CLICK_BLOCK && p.getEquipment().getItemInMainHand().getItemMeta() != null && p.getEquipment().getItemInMainHand().getItemMeta().getDisplayName().contains("PVP")) {
            switch (plotMaker.get(PVP).validBlock(event.getClickedBlock().getLocation())) {
                case 0:
                    event.getPlayer().sendMessage("[PlotMaker PVP]" + String.format("First block selected %f %f %f",
                            event.getClickedBlock().getLocation().getX(), event.getClickedBlock().getLocation().getY(),
                            event.getClickedBlock().getLocation().getZ()));
                    plotMaker.get(PVP).setLeft(event.getClickedBlock().getLocation());
                    //event.getClickedBlock().setType(Material.GLOWSTONE);
                    break;

                case 1:
                    event.getPlayer().sendMessage("[PlotMaker PVP]" + String.format("Block must not have the name X or Z coordenates of the first one. In emergency do /plotreset"));
                    break;

                case 2:
                    event.getPlayer().sendMessage("[PlotMaker PVP]" + String.format("Second block selected %f %f %f",
                            event.getClickedBlock().getLocation().getX(), event.getClickedBlock().getLocation().getY(),
                            event.getClickedBlock().getLocation().getZ()));
                    plotMaker.get(PVP).setRight(event.getClickedBlock().getLocation());
                    SP_PlotMaker sp = plotMaker.get(PVP);
                    double area = (Math.abs(sp.getLeft().getX() - sp.getRight().getX()) + 1) * (Math.abs(sp.getLeft().getZ() - sp.getRight().getZ()) + 1);
                    sp.setWidth(Math.abs(sp.getLeft().getX() - sp.getRight().getX()) + 1);
                    sp.setHeight(Math.abs(sp.getLeft().getX() - sp.getRight().getX()) + 1);
                    event.getPlayer().sendMessage("[PlotMaker PVP] " + String.format("Plot area of %f blocks", area));
                    if (addPlot(PVP, sp)) {
                        event.getPlayer().sendMessage("[PlotMaker PVP] " + String.format("Successfully created a plot!"));
                    } else {
                        event.getPlayer().sendMessage("[PlotMaker PVP] " + String.format("Failed to create plot"));
                    }
                    plotMaker.get(PVP).clear();
                    //event.getClickedBlock().setType(Material.GLOWSTONE);
                    break;
                case 4:
                    //event.getPlayer().sendMessage("[PlotMaker]" + String.format("Plot already selected. In emergency do /plotreset"));
                    break;

            }
        } else if (event.getAction() == Action.LEFT_CLICK_BLOCK && p.getEquipment().getItemInMainHand().getItemMeta() != null && p.getEquipment().getItemInMainHand().getItemMeta().getDisplayName().contains("Tool")) {
            if (event.getClickedBlock().getType() == Material.CHEST && plots.containsKey(event.getPlayer().getUniqueId())) {

                if (event.getClickedBlock().hasMetadata("Owner")) {
                    Player owner = Bukkit.getPlayer(UUID.fromString(event.getClickedBlock().getMetadata("Owner").get(0).asString()));
                    if (owner.getUniqueId() == p.getUniqueId()) {
                        event.getClickedBlock().removeMetadata("Owner", plugin);
                        event.getPlayer().sendMessage("[PlotMaker] You removed ownership of this chest!");
                        removeChest(p.getUniqueId(), event.getClickedBlock().getLocation());
                    }


                } else {
                    if (insidePlot(p.getUniqueId(), event.getClickedBlock().getLocation())) {
                        event.getClickedBlock().setMetadata("Owner", new FixedMetadataValue(plugin, p.getUniqueId()));
                        event.getPlayer().sendMessage("[PlotMaker] Hey traveller, you are now the owner!");
                        addChest(p.getUniqueId(), event.getClickedBlock().getLocation());
                    } else {
                        event.getPlayer().sendMessage("[PlotMaker] Chest is not inside a plot you own.");
                    }


                }
            }
            else if (plotMaker.containsKey(event.getPlayer().getUniqueId())) {
                switch (plotMaker.get(event.getPlayer().getUniqueId()).validBlock(event.getClickedBlock().getLocation())) {
                    case 0:
                        event.getPlayer().sendMessage("[PlotMaker]" + String.format("First block selected %f %f %f",
                                event.getClickedBlock().getLocation().getX(), event.getClickedBlock().getLocation().getY(),
                                event.getClickedBlock().getLocation().getZ()));
                        plotMaker.get(event.getPlayer().getUniqueId()).setLeft(event.getClickedBlock().getLocation());
                        //event.getClickedBlock().setType(Material.GLOWSTONE);
                        break;

                    case 1:
                        event.getPlayer().sendMessage("[PlotMaker]" + String.format("Block must not have the name X or Z coordenates of the first one. In emergency do /plotreset"));
                        break;

                    case 2:
                        event.getPlayer().sendMessage("[PlotMaker]" + String.format("Second block selected %f %f %f",
                                event.getClickedBlock().getLocation().getX(), event.getClickedBlock().getLocation().getY(),
                                event.getClickedBlock().getLocation().getZ()));
                        plotMaker.get(event.getPlayer().getUniqueId()).setRight(event.getClickedBlock().getLocation());
                        SP_PlotMaker sp = plotMaker.get(event.getPlayer().getUniqueId());
                        double area = (Math.abs(sp.getLeft().getX() - sp.getRight().getX()) + 1) * (Math.abs(sp.getLeft().getZ() - sp.getRight().getZ()) + 1);
                        sp.setWidth(Math.abs(sp.getLeft().getX() - sp.getRight().getX()) + 1);
                        sp.setHeight(Math.abs(sp.getLeft().getX() - sp.getRight().getX()) + 1);
                        event.getPlayer().sendMessage("[PlotMaker] " + String.format("Plot area of %f blocks", area));
                        if (addPlot(p.getUniqueId(), sp)) {
                            event.getPlayer().sendMessage("[PlotMaker] " + String.format("Successfully created a plot!"));
                        } else {
                            event.getPlayer().sendMessage("[PlotMaker] " + String.format("Failed to create plot"));
                        }
                        plotMaker.get(event.getPlayer().getUniqueId()).clear();
                        //event.getClickedBlock().setType(Material.GLOWSTONE);
                        break;
                    case 4:
                        //event.getPlayer().sendMessage("[PlotMaker]" + String.format("Plot already selected. In emergency do /plotreset"));
                        break;

                }
            }
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.CHEST) {
            if (event.getClickedBlock().hasMetadata("Owner")) {
                Player owner = Bukkit.getPlayer(UUID.fromString(event.getClickedBlock().getMetadata("Owner").get(0).asString()));
                if (owner.getUniqueId() != p.getUniqueId()) {
                    event.getPlayer().sendMessage("[PlotMaker] This chest belongs to " + owner.getDisplayName());
                    event.setCancelled(true);
                }
            }
        }


    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        if (!manipulatePlot(p.getUniqueId(), event.getBlock().getLocation())) {
            event.getPlayer().sendMessage("[PlotMaker] You need to own the plot to break this block");
            event.setCancelled(true);
            return;
        }
        if (event.getBlock().hasMetadata("Owner")) {
            Player owner = Bukkit.getPlayer(UUID.fromString(event.getBlock().getMetadata("Owner").get(0).asString()));
            if (owner.getUniqueId() == p.getUniqueId()) {
                event.getBlock().removeMetadata("Owner", plugin);
                event.getPlayer().sendMessage("[PlotMaker] You removed ownership of this chest!");
                removeChest(p.getUniqueId(), event.getBlock().getLocation());
            }
        }
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (!manipulatePlot(p.getUniqueId(), e.getBlock().getLocation())) {
            e.getPlayer().sendMessage("[PlotMaker] You need to own the plot to place this block");
            e.setCancelled(true);
            return;
        }
    }

}
