package dev.j0hny.rconglobal.mains;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SPMain extends JavaPlugin {
    @Override
    public void onEnable() {
       Bukkit.getConsoleSender().sendMessage("§6A api do Rcon Spigot foi ativada com sucesso!");
    }
}
