package dev.j0hny.rconglobal.logger;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;


public final class LoggerModule implements log {

    @Override
    public void sendMessage(String var1) {
        try {
            Class.forName("net.md_5.bungee.api.ProxyServer");
            ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§6[sRconGlobal] " + var1));
        } catch (ClassNotFoundException e) {
            Bukkit.getConsoleSender().sendMessage("§6[sRconGlobal] " + var1);
        }
    }

    @Override
    public void sendMessageModule(String module, String name) {
        try {
            Class.forName("net.md_5.bungee.api.ProxyServer");
            ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§6[sRconGlobal] " + module + ": " + name));
        } catch (ClassNotFoundException e) {
            Bukkit.getConsoleSender().sendMessage("§6[sRconGlobal] " + module + ": " + name);
        }
    }


}
interface log {
    void sendMessage(String var1);
    void sendMessageModule(String module, String name);
}