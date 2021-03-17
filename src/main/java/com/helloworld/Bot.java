package com.helloworld;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.entity.Player;

import javax.security.auth.login.LoginException;

import static org.bukkit.Bukkit.getServer;

public class Bot extends ListenerAdapter {
    public Bot(String token) {
        try {
            JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                    .addEventListeners(this)
                    .setActivity(Activity.playing("Type !ping"))
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Message msg = event.getMessage();
        if (msg.getContentRaw().equals("!ping"))
        {
            MessageChannel channel = event.getChannel();
            long time = System.currentTimeMillis();
            channel.sendMessage("Pong!") /* => RestAction<Message> */
                    .queue(response /* => Message */ -> {
                        response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time).queue();
                    });
        }
        if(msg.getContentRaw().equals("!players")){
            String output = "";
            if(getServer().getOnlinePlayers().size() == 0){
                event.getChannel().sendMessage("No players online").queue();;
                return;
            }
            for (Player x : getServer().getOnlinePlayers()) {
                output += x.getDisplayName()+"\n";
            }
            event.getChannel().sendMessage(output).queue();

        }
        if(msg.getContentRaw().equals("!shutdown")){
            event.getChannel().sendMessage("Shutting down server").queue();;
            getServer().shutdown();
        }

    }
}
