package dev.j0hny.rconglobal.mains;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BGMain extends Plugin {
    @Override
    public void onEnable() {
        BungeeCord.getInstance().getConsole().sendMessage(new TextComponent("§6A api do Rcon BungeeCord foi ativada com sucesso!"));
    }
}
