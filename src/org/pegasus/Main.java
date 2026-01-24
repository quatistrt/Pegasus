package org.pegasus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.pegasus.PegasusRL;

import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener {
    private HashMap<UUID, Long> rightClickCooldown = new HashMap<>();
    private HashMap<UUID, Long> leftClickCooldown = new HashMap<>();
    private FileConfiguration config;

    @Override
    public void onEnable() {
		getLogger().info("Pegasus Baslatilidi. By quatisytt");
	    this.getCommand("pegasus").setExecutor(new PegasusRL(this));
	    saveDefaultConfig(); 
        this.saveDefaultConfig();
        this.config = this.getConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        int slot = config.getInt("firework.slot", 0);
        player.getInventory().setItem(slot, new org.bukkit.inventory.ItemStack(Material.FIREWORK));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (player.getItemInHand().getType() != Material.FIREWORK) {
            return;
        }

        if (event.getAction().toString().contains("RIGHT_CLICK")) {
            if (isOnCooldown(uuid, rightClickCooldown, "firework.cooldown.right")) {
                return;
            }
            player.playSound(player.getLocation(), "fireworks.launch", 1.0f, 1.0f);
            launchUp(player);
            rightClickCooldown.put(uuid, System.currentTimeMillis());
        } else if (event.getAction().toString().contains("LEFT_CLICK")) {
            if (isOnCooldown(uuid, leftClickCooldown, "firework.cooldown.left")) {
                return;
            }
            player.playSound(player.getLocation(), "fireworks.launch", 1.0f, 1.0f);
            launchForward(player);
            leftClickCooldown.put(uuid, System.currentTimeMillis());
        }
    }

    private void launchUp(Player player) {
        double power = config.getDouble("firework.power.up", 1.0);
        player.setVelocity(new Vector(0, power, 0));
    }

    private void launchForward(Player player) {
        double power = config.getDouble("firework.power.forward", 1.5);
        Vector direction = player.getLocation().getDirection().normalize().multiply(power);
        player.setVelocity(direction);
    }

    private boolean isOnCooldown(UUID uuid, HashMap<UUID, Long> cooldownMap, String configPath) {
        long cooldownTime = config.getLong(configPath, 5) * 1000;
        return cooldownMap.containsKey(uuid) && (System.currentTimeMillis() - cooldownMap.get(uuid)) < cooldownTime;
    }
}
