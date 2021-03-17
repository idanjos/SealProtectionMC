package com.helloworld;



import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;


import javax.security.auth.login.LoginException;

public class HelloWorldG extends JavaPlugin {
    @Override
    public void onEnable(){

        Bot b = new Bot("Nzk5ODIwNjkwNDc1NzEyNTQy.YAJJEA.yIP2f-eOfrWEbRb9j2Lrv_llzBg");
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[HelloWorld] HelloWorld enabled!");
    }

    @Override
    public void onDisable(){getServer().getConsoleSender().sendMessage(ChatColor.RED + "[HelloWorld] HelloWorld disabled!"); }

    public static void main(String[] args) {

    }
}
