package com.blockvehicles.gui;

import com.blockvehicles.BlockVehiclesPlugin;
import com.blockvehicles.registry.VehicleCategory;
import com.blockvehicles.registry.VehicleRegistry;
import com.blockvehicles.registry.VehicleSpec;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class VehicleCatalogGUI {
    public static final String CATALOG_TITLE = ChatColor.DARK_AQUA + "Vehicle Showroom & Catalog";

    public static void openCatalog(Player player, VehicleCategory filterCategory, int page) {
        Inventory inv = Bukkit.createInventory(null, 54, CATALOG_TITLE + " (Page " + (page + 1) + ")");

        List<VehicleSpec> list = filterCategory == null
                ? new ArrayList<>(VehicleRegistry.getAll())
                : VehicleRegistry.getByCategory(filterCategory);

        int itemsPerPage = 36;
        int startIndex = page * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, list.size());

        for (int i = startIndex; i < endIndex; i++) {
            VehicleSpec spec = list.get(i);
            ItemStack item = BlockVehiclesPlugin.getInstance().getCustomItemRegistry().createVehicleKey(spec);
            inv.setItem(i - startIndex, item);
        }

        // Category Filter Selector on Bottom Bar
        inv.setItem(45, makeBtn(Material.COMPASS, ChatColor.GOLD + "All Categories", "Click to show all 100 vehicles"));
        inv.setItem(46, makeBtn(Material.REDSTONE_BLOCK, ChatColor.RED + "Supercars", "Filter: Supercars & Hypercars"));
        inv.setItem(47, makeBtn(Material.IRON_BLOCK, ChatColor.WHITE + "Classics & Autos", "Filter: Everyday & Classics"));
        inv.setItem(48, makeBtn(Material.COPPER_BLOCK, ChatColor.GOLD + "Trucks & SUVs", "Filter: Pickups, Trucks & SUVs"));
        inv.setItem(49, makeBtn(Material.DIAMOND_BLOCK, ChatColor.AQUA + "Mining & Diggers", "Filter: Construction & Drilling"));
        inv.setItem(50, makeBtn(Material.OBSIDIAN, ChatColor.DARK_GREEN + "Armored Military", "Filter: Tanks & Combat"));
        inv.setItem(51, makeBtn(Material.FEATHER, ChatColor.LIGHT_PURPLE + "Aircraft & Jets", "Filter: Planes & Helicopters"));

        // Pagination buttons
        if (page > 0) {
            inv.setItem(52, makeBtn(Material.ARROW, ChatColor.YELLOW + "← Previous Page", "Go to page " + page));
        }
        if (endIndex < list.size()) {
            inv.setItem(53, makeBtn(Material.ARROW, ChatColor.YELLOW + "Next Page →", "Go to page " + (page + 2)));
        }

        player.openInventory(inv);
    }

    private static ItemStack makeBtn(Material mat, String name, String lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Collections.singletonList(ChatColor.GRAY + lore));
            item.setItemMeta(meta);
        }
        return item;
    }
}
