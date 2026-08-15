package com.blockvehicles.registry;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum VehicleCategory {
    SUPERCAR("Supercars & Hypercars", Material.REDSTONE_BLOCK, ChatColor.RED),
    CIVILIAN_CAR("Everyday & Classic Cars", Material.IRON_BLOCK, ChatColor.WHITE),
    TRUCK_SUV("Trucks, Pickups & SUVs", Material.COPPER_BLOCK, ChatColor.GOLD),
    SEMI_HEAVY("Semi-Trucks & Heavy Haulers", Material.NETHERITE_BLOCK, ChatColor.DARK_GRAY),
    PUBLIC_TRANSIT("Buses & Public Transit", Material.GOLD_BLOCK, ChatColor.YELLOW),
    EMERGENCY("Emergency & Service", Material.LAPIS_BLOCK, ChatColor.BLUE),
    CONSTRUCTION("Construction & Mining", Material.DIAMOND_BLOCK, ChatColor.AQUA),
    AGRICULTURE("Agricultural & Industrial", Material.HAY_BLOCK, ChatColor.GREEN),
    MILITARY("Military & Armored", Material.OBSIDIAN, ChatColor.DARK_GREEN),
    ROTORCRAFT("Helicopters & Gyros", Material.FEATHER, ChatColor.LIGHT_PURPLE),
    FIXED_WING("Planes & Supersonic Jets", Material.FIREWORK_ROCKET, ChatColor.DARK_AQUA);

    private final String title;
    private final Material icon;
    private final ChatColor color;

    VehicleCategory(String title, Material icon, ChatColor color) {
        this.title = title;
        this.icon = icon;
        this.color = color;
    }

    public String getTitle() { return title; }
    public Material getIcon() { return icon; }
    public ChatColor getColor() { return color; }
}
