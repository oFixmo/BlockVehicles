package com.blockvehicles.gui;

import com.blockvehicles.vehicle.VehicleInstance;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;

public class VehicleGUI {
    public static final String DASHBOARD_TITLE = ChatColor.DARK_BLUE + "Vehicle Control Dashboard";

    public static void openDashboard(Player player, VehicleInstance vehicle) {
        Inventory inv = Bukkit.createInventory(null, 27, DASHBOARD_TITLE);

        // 1. Cargo Trunk
        ItemStack trunkBtn = new ItemStack(Material.CHEST);
        ItemMeta trunkMeta = trunkBtn.getItemMeta();
        trunkMeta.setDisplayName(ChatColor.GOLD + "Access Cargo Trunk (" + (vehicle.getSpec().getStorageRows() * 9) + " Slots)");
        trunkMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click to open vehicle storage."));
        trunkBtn.setItemMeta(trunkMeta);
        inv.setItem(11, trunkBtn);

        // 2. Fuel Gauge & Deposit
        int fuelPercent = (int) ((vehicle.getFuel() / vehicle.getSpec().getMaxFuel()) * 100);
        ItemStack fuelBtn = new ItemStack(Material.BLAZE_POWDER);
        ItemMeta fuelMeta = fuelBtn.getItemMeta();
        fuelMeta.setDisplayName(ChatColor.YELLOW + "Fuel Gauge: " + fuelPercent + "%");
        fuelMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Capacity: " + String.format("%.1f", vehicle.getFuel()) + " / " + vehicle.getSpec().getMaxFuel(),
                ChatColor.AQUA + "Click with Coal, Lava, Redstone or",
                ChatColor.AQUA + "Batteries to refuel."
        ));
        fuelBtn.setItemMeta(fuelMeta);
        inv.setItem(13, fuelBtn);

        // 3. Security Lock
        ItemStack lockBtn = new ItemStack(vehicle.isLocked() ? Material.IRON_DOOR : Material.OAK_DOOR);
        ItemMeta lockMeta = lockBtn.getItemMeta();
        lockMeta.setDisplayName(ChatColor.GREEN + "Lock Status: " + (vehicle.isLocked() ? ChatColor.RED + "LOCKED" : ChatColor.GREEN + "UNLOCKED"));
        lockBtn.setItemMeta(lockMeta);
        inv.setItem(15, lockBtn);

        // 4. Dismantle & Pick Up
        ItemStack pickupBtn = new ItemStack(Material.BARRIER);
        ItemMeta pMeta = pickupBtn.getItemMeta();
        pMeta.setDisplayName(ChatColor.RED + "Dismantle & Retrieve Vehicle Key");
        pickupBtn.setItemMeta(pMeta);
        inv.setItem(26, pickupBtn);

        player.openInventory(inv);
    }
}
