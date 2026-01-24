package org.pegasus;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PegasusRL implements CommandExecutor {
    private JavaPlugin plugin;

    public PegasusRL(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Bu komut yalnýzca oyuncular tarafýndan kullanýlabilir!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("Kullaným: /pegasus <reload|yapimci>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                plugin.reloadConfig();
                player.sendMessage("Pegasus eklentisinin config dosyasý yeniden yüklendi!");
                break;
                
            case "yapimci":
                player.sendMessage("§aQuatisYT tarafýndan yapýlmýþtýr. §bDiscord: quatisytt");
                break;

            default:
                player.sendMessage("Bilinmeyen komut! Kullaným: /pegasusrl <reload|yapimci>");
                break;
        }
        return true;
    }
}