package com.helloworld;


import com.helloworld.commands.DisownPlot;
import com.helloworld.commands.PlotMaker;
import com.helloworld.commands.PlotReset;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class HelloWorldG extends JavaPlugin {
    SealProtector sp;
    com.helloworld.PlotMaker pm;
    @Override
    public void onEnable() {

        //Bot b = new Bot("Nzk5ODIwNjkwNDc1NzEyNTQy.YAJJEA.yIP2f-eOfrWEbRb9j2Lrv_llzBg");
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[HelloWorld] HelloWorld enabled!");
        pm = new com.helloworld.PlotMaker();
        sp = new SealProtector(pm,this);
        pm.load(this);
        //sp.load(this);
        getServer().getPluginManager().registerEvents(sp, this);
        getServer().getPluginManager().registerEvents(pm, this);
        this.getCommand("plotmaker").setExecutor(new PlotMaker());
        this.getCommand("plotreset").setExecutor(new PlotReset(pm.plotMaker));
        this.getCommand("disownplots").setExecutor(new DisownPlot(pm.plots));
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[HelloWorld] HelloWorld disabled!");
        pm.save();
    }

    public static void main(String[] args) {

    }
}
