package com.helloworld;

import com.helloworld.classes.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class SealProtector implements Listener {

    public HashMap<UUID , ItemStack[]> items = new HashMap<UUID , ItemStack[]>();
    public HashMap<UUID , EntityEquipment> equipment = new HashMap<UUID , EntityEquipment>();
    Plugin plugin;
    PlotMaker pm;

    public SealProtector(PlotMaker pm, Plugin main) {
        this.pm = pm;
        plugin = main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage("Welcome, " + event.getPlayer().getName() + "!");
        if (!pm.database.containsKey(event.getPlayer().getUniqueId())) {
            pm.database.put(event.getPlayer().getUniqueId(), new SP_Player(event.getPlayer().getUniqueId()));

            event.getPlayer().sendMessage("[SealProtection] You are sealed and safe from PvP, looting and griefing. Once you join the Nether or the End the seal will disappear. Goodluck Adventure! ~ Jac");
        }
        if (!pm.plotMaker.containsKey(event.getPlayer().getUniqueId())) {
            pm.plotMaker.put(event.getPlayer().getUniqueId(), new SP_PlotMaker(event.getPlayer().getUniqueId()));

            event.getPlayer().sendMessage("[PlotMaker] Hey traveller, type /plotmaker to get a special tool to make your own plot!");
        }

        if (!pm.plots.containsKey(event.getPlayer().getUniqueId())) {
            pm.plots.put(event.getPlayer().getUniqueId(), new ArrayList<>());


        }
    }

    @EventHandler
    private void onWorldChange(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        if (!pm.database.containsKey(p.getUniqueId()))
            pm.database.put(p.getUniqueId(), new SP_Player(p.getUniqueId()));
        if (pm.database.get(p.getUniqueId()).isProtected()) {
            e.getPlayer().sendMessage("[SealProtector] The seal has been broken");
            pm.database.get(p.getUniqueId()).setProtected(false);
        }

    }

    @EventHandler
    public void onPlayerAttacked(EntityDamageByEntityEvent e) {

        if (e.getEntity().getType() == EntityType.PLAYER && e.getDamager().getType() == EntityType.PLAYER) {
            Player d = (Player) e.getDamager();
            Player t = (Player) e.getEntity();
            if(pm.insidePlot(PlotMaker.PVP,t.getLocation()) && pm.insidePlot(PlotMaker.PVP,d.getLocation())){
                return;
            }
            if (pm.database.get(t.getUniqueId()).isProtected() || pm.database.get(d.getUniqueId()).isProtected() || pm.insidePlot(PlotMaker.STRUCTURES,t.getLocation()) || pm.insidePlot(PlotMaker.STRUCTURES,d.getLocation())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler()
    public void onRespawn(PlayerRespawnEvent event){
        if(items.containsKey(event.getPlayer().getUniqueId())){
            //event.getPlayer().getInventory().clear();
            for(ItemStack stack : items.get(event.getPlayer().getUniqueId())){
                if(stack != null)
                event.getPlayer().getInventory().addItem(stack);
            }
            EntityEquipment ee = equipment.get(event.getPlayer().getUniqueId());
            event.getPlayer().getEquipment().setArmorContents(ee.getArmorContents());
            event.getPlayer().getEquipment().setBoots(ee.getBoots());
            event.getPlayer().getEquipment().setChestplate(ee.getChestplate());
            event.getPlayer().getEquipment().setHelmet(ee.getHelmet());
            event.getPlayer().getEquipment().setItemInMainHand(ee.getItemInMainHand());
            event.getPlayer().getEquipment().setItemInOffHand(ee.getItemInOffHand());
            event.getPlayer().getEquipment().setLeggings(ee.getLeggings());


            items.remove(event.getPlayer().getUniqueId());
            equipment.remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler()
    public void onDeath(PlayerDeathEvent event){
        if(event.getEntity().getType() == EntityType.PLAYER && pm.insidePlot(PlotMaker.PVP,event.getEntity().getLocation())) {
            
            event.getDrops().clear();
            ItemStack[] content = event.getEntity().getInventory().getContents();
            equipment.put(event.getEntity().getUniqueId(),event.getEntity().getEquipment());
            items.put(event.getEntity().getUniqueId(), content);
            event.getEntity().getInventory().clear();
        }
    }



}
