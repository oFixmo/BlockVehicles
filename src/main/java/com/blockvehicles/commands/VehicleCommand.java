package com.blockvehicles.commands;

import com.blockvehicles.BlockVehiclesPlugin;
import com.blockvehicles.gui.VehicleCatalogGUI;
import com.blockvehicles.registry.VehicleRegistry;
import com.blockvehicles.registry.VehicleSpec;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VehicleCommand implements CommandExecutor {
    private final BlockVehiclesPlugin plugin;

    public VehicleCommand(BlockVehiclesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player player) {
                VehicleCatalogGUI.openCatalog(player, null, 0);
            } else {
                sender.sendMessage(ChatColor.GOLD + "BlockVehicles 2.0 | Total Vehicles: " + VehicleRegistry.getAll().size());
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("menu") || args[0].equalsIgnoreCase("catalog") || args[0].equalsIgnoreCase("gui")) {
            if (sender instanceof Player player) {
                VehicleCatalogGUI.openCatalog(player, null, 0);
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("give") && sender.hasPermission("blockvehicles.admin")) {
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "Usage: /vehicle give <player> <specId>");
                return true;
            }
            Player target = Bukkit.getPlayer(args);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player not found!");
                return true;
            }
            VehicleSpec spec = VehicleRegistry.get(args[2].toUpperCase());
            if (spec == null) {
                sender.sendMessage(ChatColor.RED + "Invalid vehicle! Use /vehicle menu to browse.");
                return true;
            }
            ItemStack key = plugin.getCustomItemRegistry().createVehicleKey(spec);
            target.getInventory().addItem(key);
            sender.sendMessage(ChatColor.GREEN + "Gave " + spec.getName() + " to " + target.getName());
            return true;
        }

        if (args[0].equalsIgnoreCase("item") && sender instanceof Player player && player.hasPermission("blockvehicles.admin")) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /vehicle item <itemId>");
                return true;
            }
            ItemStack item = plugin.getCustomItemRegistry().getItem(args.toUpperCase());
            if (item != null) {
                player.getInventory().addItem(item);
                player.sendMessage(ChatColor.GREEN + "Given " + item.getItemMeta().getDisplayName());
            } else {
                player.sendMessage(ChatColor.RED + "Item ID not found.");
            }
            return true;
        }

        return true;
    }
}
