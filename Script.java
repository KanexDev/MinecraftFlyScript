import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class FlyPlugin extends JavaPlugin implements Listener, CommandExecutor {

    private List<String> allowedPlayers = new ArrayList<>();
    private double flySpeed = 1.0;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("fly").setExecutor(this);
        getCommand("unfly").setExecutor(this);
        getCommand("setflyspeed").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("fly")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (hasPermission(player)) {
                    toggleFlight(player, true);
                } else {
                    player.sendMessage("You don't have permission to use this command.");
                }
            } else {
                sender.sendMessage("This command can only be used by players.");
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("unfly")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (hasPermission(player)) {
                    toggleFlight(player, false);
                } else {
                    player.sendMessage("You don't have permission to use this command.");
                }
            } else {
                sender.sendMessage("This command can only be used by players.");
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("setflyspeed")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 1) {
                    if (hasPermission(player)) {
                        try {
                            double speed = Double.parseDouble(args[0]);
                            setFlySpeed(player, speed);
                            player.sendMessage("Fly speed set to " + speed);
                        } catch (NumberFormatException e) {
                            player.sendMessage("Invalid fly speed value. Please provide a number.");
                        }
                    } else {
                        player.sendMessage("You don't have permission to use this command.");
                    }
                } else {
                    player.sendMessage("Usage: /setflyspeed <speed>");
                }
            } else {
                sender.sendMessage("This command can only be used by players.");
            }
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.CREATIVE) {
            if (!hasPermission(player)) {
                player.sendMessage("You don't have permission to fly.");
                event.setCancelled(true);
                player.setAllowFlight(false);
                player.setFlying(false);
                return;
            }

            if (event.isFlying()) {
                event.setCancelled(true);
                player.setAllowFlight(false);
                player.setFlying(false);
                player.setVelocity(player.getLocation().getDirection().multiply(flySpeed).setY(1));
            }
        }
    }

    private boolean hasPermission(Player player) {
        return allowedPlayers.contains(player.getName());
    }

    private void toggleFlight(Player player, boolean enableFlight) {
        player.setAllowFlight(enableFlight);
        player.setFlying(enableFlight);
        player.sendMessage("Flight mode " + (enableFlight ? "enabled" : "disabled"));
    }

    private void setFlySpeed(Player player, double speed) {
        flySpeed = speed;
    }

    // You can load the allowed players from a configuration file or database
    private void loadAllowedPlayers() {
        allowedPlayers.add("Bredzio");
        allowedPlayers.add("Player2");
    }
}
