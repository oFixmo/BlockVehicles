package com.blockvehicles.listeners;

import com.blockvehicles.BlockVehiclesPlugin;
import com.blockvehicles.registry.VehicleRegistry;
import com.blockvehicles.registry.VehicleSpec;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldLootListener implements Listener {
    private final BlockVehiclesPlugin plugin;
    private final Random random = new Random();

    public WorldLootListener(BlockVehiclesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Material type = event.getBlock().getType();
        double roll = random.nextDouble();

        if (type == Material.COPPER_ORE || type == Material.DEEPSLATE_COPPER_ORE) {
            if (roll < 0.25) {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), plugin.getCustomItemRegistry().getItem("COPPER_WIRING"));
            }
            if (roll < 0.10) {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), plugin.getCustomItemRegistry().getItem("LITHIUM_BATTERY"));
            }
        } else if (type == Material.IRON_ORE || type == Material.DEEPSLATE_IRON_ORE) {
            if (roll < 0.15) {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), plugin.getCustomItemRegistry().getItem("STEEL_INGOT"));
            }
        } else if (type == Material.DIAMOND_ORE || type == Material.DEEPSLATE_DIAMOND_ORE) {
            if (roll < 0.05) {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), plugin.getCustomItemRegistry().getItem("DIAMOND_DRILL_HEAD"));
            }
        } else if (type == Material.ANCIENT_DEBRIS) {
            if (roll < 0.20) {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), plugin.getCustomItemRegistry().getItem("ARMOR_PLATING"));
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        EntityType type = event.getEntityType();
        double roll = random.nextDouble();

        if (type == EntityType.IRON_GOLEM) {
            if (roll < 0.40) {
                event.getDrops().add(plugin.getCustomItemRegistry().getItem("ENGINE_V6"));
            }
            if (roll < 0.20) {
                event.getDrops().add(plugin.getCustomItemRegistry().getItem("ARMOR_PLATING"));
            }
        } else if (type == EntityType.RAVAGER) {
            if (roll < 0.50) {
                event.getDrops().add(plugin.getCustomItemRegistry().getItem("ENGINE_V8_TURBO"));
            }
            if (roll < 0.35) {
                event.getDrops().add(plugin.getCustomItemRegistry().getItem("HIGH_CAPACITY_BATTERY"));
            }
        } else if (type == EntityType.WITHER) {
            event.getDrops().add(plugin.getCustomItemRegistry().getItem("TURBOFAN_JET_ENGINE"));
            event.getDrops().add(plugin.getCustomItemRegistry().getItem("AVIONICS_COMPUTER"));
        }
    }

    @EventHandler
    public void onLootGenerate(LootGenerateEvent event) {
        double roll = random.nextDouble();
        if (roll < 0.35) {
            List<String> items = List.of("STEEL_INGOT", "TIRE_ALL_TERRAIN", "LITHIUM_BATTERY", "SOLAR_PANEL", "ENGINE_V6");
            String randomItem = items.get(random.nextInt(items.size()));
            ItemStack drop = plugin.getCustomItemRegistry().getItem(randomItem);
            if (drop != null) event.getLoot().add(drop);
        }

        if (roll < 0.08) {
            List<VehicleSpec> all = new ArrayList<>(VehicleRegistry.getAll());
            if (!all.isEmpty()) {
                VehicleSpec randomVehicle = all.get(random.nextInt(all.size()));
                ItemStack key = plugin.getCustomItemRegistry().createVehicleKey(randomVehicle);
                event.getLoot().add(key);
            }
        }
    }
}
