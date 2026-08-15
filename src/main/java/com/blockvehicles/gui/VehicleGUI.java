package com.blockvehicles.gui;

import com.blockvehicles.vehicle.VehicleInstance;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class VehicleGUI {
    public static final String DASHBOARD_TITLE = ChatColor.DARK_AQUA + "⚙ Vehicle Telemetry & Dashboard";

    public static void openDashboard(Player player, VehicleInstance vehicle) {
        Inventory inv = Bukkit.createInventory(null, 36, DASHBOARD_TITLE);

        // Frame border
        ItemStack border = makeBtn(Material.CYAN_STAINED_GLASS_PANE, " ", "");
        for (int i = 0; i < 36; i++) {
            if (i < 9 || i >= 27 || i % 9 == 0 || i % 9 == 8) {
                inv.setItem(i, border);
            }
        }

        // 1. Cargo Trunk Button
        inv.setItem(11, makeBtn(Material.CHEST, ChatColor.GOLD + "Access Cargo Trunk", "Storage capacity: " + (vehicle.getSpec().getStorageRows() * 9) + " slots."));

        // 2. Live Fuel Gauge
        int fuelPercent = (int) ((vehicle.getFuel() / vehicle.getSpec().getMaxFuel()) * 100);
        Material fuelMat = fuelPercent > 50 ? Material.LIME_DYE : (fuelPercent > 20 ? Material.YELLOW_DYE : Material.RED_DYE);
        inv.setItem(13, makeBtn(fuelMat, ChatColor.YELLOW + "Fuel Level: " + fuelPercent + "%",
                "Fuel: " + String.format("%.0f", vehicle.getFuel()) + " / " + vehicle.getSpec().getMaxFuel() + " units.",
                "Click with Coal, Lava, Redstone or Batteries to refill."));

        // 3. Gear Shifter
        String[] gearLabels = {"", "ECO (30%)", "CRUISE (60%)", "SPORT (100%)", "NITRO (160%)"};
        inv.setItem(15, makeBtn(Material.REDSTONE_TORCH, ChatColor.RED + "Gearbox: " + gearLabels[vehicle.getGear()],
                "Click to shift transmission gear."));

        // 4. Security Lock
        inv.setItem(21, makeBtn(vehicle.isLocked() ? Material.IRON_DOOR : Material.OAK_DOOR,
                ChatColor.AQUA + "Lock: " + (vehicle.isLocked() ? ChatColor.RED + "LOCKED" : ChatColor.GREEN + "UNLOCKED"),
                "Toggle passenger security access."));

        // 5. Retrieve / Dismantle
        inv.setItem(23, makeBtn(Material.BARRIER, ChatColor.RED + "Dismantle & Retrieve Key",
                "Picks up the vehicle into your inventory."));

        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.2f);
        player.openInventory(inv);
    }

    private static ItemStack makeBtn(Material mat, String name, String... lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.stream(lore).map(l -> ChatColor.GRAY + l).toList());
            item.setItemMeta(meta);
        }
        return item;
    }
}
