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

import java.util.Arrays;
import java.util.List;

public class VehicleCommand implements CommandExecutor {
    private final BlockVehiclesPlugin plugin;

    public VehicleCommand(BlockVehiclesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<String> argList = Arrays.asList(args);

        if (argList.isEmpty() || argList.get(0).equalsIgnoreCase("menu") || argList.get(0).equalsIgnoreCase("catalog") || argList.get(0).equalsIgnoreCase("gui")) {
            if (sender instanceof Player player) {
                VehicleCatalogGUI.openCatalog(player, null, 0);
            } else {
                sender.sendMessage(ChatColor.GOLD + "BlockVehicles 5.0 | Total: " + VehicleRegistry.getAll().size() + " vehicles");
            }
            return true;
        }

        // Direct spawn alias: /vehicle spawn <car|helicopter|bike|tractor|plane|drill|tank>
        if ((argList.get(0).equalsIgnoreCase("spawn") || argList.get(0).equalsIgnoreCase("get")) && sender instanceof Player player) {
            if (!player.hasPermission("blockvehicles.use")) {
                player.sendMessage(ChatColor.RED + "No permission.");
                return true;
            }
            if (argList.size() < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /vehicle spawn <helicopter|car|bike|tractor|plane|drill|tank>");
                return true;
            }
            String query = argList.get(1).toUpperCase();
            VehicleSpec spec = VehicleRegistry.findBestMatch(query);
            if (spec == null) {
                player.sendMessage(ChatColor.RED + "Unknown vehicle! Type /vehicle menu to browse all.");
                return true;
            }
            ItemStack key = plugin.getCustomItemRegistry().createVehicleKey(spec);
            player.getInventory().addItem(key);
            player.sendMessage(ChatColor.GREEN + "Received " + spec.getName() + " Key! Right-click ground to deploy.");
            return true;
        }

        if (argList.get(0).equalsIgnoreCase("give") && sender.hasPermission("blockvehicles.admin")) {
            if (argList.size() < 3) {
                sender.sendMessage(ChatColor.RED + "Usage: /vehicle give <player> <specId>");
                return true;
            }
            Player target = Bukkit.getPlayer(argList.get(1));
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player not found!");
                return true;
            }
            VehicleSpec spec = VehicleRegistry.findBestMatch(argList.get(2));
            if (spec == null) {
                sender.sendMessage(ChatColor.RED + "Invalid vehicle! Use /vehicle menu to browse.");
                return true;
            }
            ItemStack key = plugin.getCustomItemRegistry().createVehicleKey(spec);
            target.getInventory().addItem(key);
            sender.sendMessage(ChatColor.GREEN + "Gave " + spec.getName() + " to " + target.getName());
            return true;
        }

        if (argList.get(0).equalsIgnoreCase("item") && sender instanceof Player player && player.hasPermission("blockvehicles.admin")) {
            if (argList.size() < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /vehicle item <itemId>");
                return true;
            }
            String itemId = argList.get(1).toUpperCase();
            ItemStack item = plugin.getCustomItemRegistry().getItem(itemId);
            if (item != null) {
                player.getInventory().addItem(item);
                player.sendMessage(ChatColor.GREEN + "Given " + item.getItemMeta().getDisplayName());
            } else {
                player.sendMessage(ChatColor.RED + "Item not found.");
            }
            return true;
        }

        return true;
    }
}
