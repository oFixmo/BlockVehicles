package com.blockvehicles.listeners;

import com.blockvehicles.BlockVehiclesPlugin;
import com.blockvehicles.gui.VehicleCatalogGUI;
import com.blockvehicles.gui.VehicleGUI;
import com.blockvehicles.registry.VehicleCategory;
import com.blockvehicles.registry.VehicleRegistry;
import com.blockvehicles.registry.VehicleSpec;
import com.blockvehicles.vehicle.VehicleInstance;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class VehicleListener implements Listener {
    private final BlockVehiclesPlugin plugin;

    public VehicleListener(BlockVehiclesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeployVehicle(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        ItemStack item = event.getItem();
        if (item == null || !item.hasItemMeta()) return;

        String specId = item.getItemMeta().getPersistentDataContainer().get(plugin.getCustomItemRegistry().VEHICLE_SPEC_KEY, PersistentDataType.STRING);
        if (specId == null) return;

        event.setCancelled(true);
        VehicleSpec spec = VehicleRegistry.get(specId);
        if (spec == null) return;

        plugin.getVehicleManager().spawnVehicle(event.getClickedBlock().getLocation().add(0.5, 0.2, 0.5), spec, event.getPlayer().getUniqueId());
        item.setAmount(item.getAmount() - 1);
        event.getPlayer().sendMessage(ChatColor.GREEN + "Deployed " + spec.getName() + "! Right-Click to drive.");
    }

    @EventHandler
    public void onInteractVehicle(PlayerInteractAtEntityEvent event) {
        Entity entity = event.getRightClicked();
        VehicleInstance vehicle = null;

        if (entity instanceof ArmorStand) {
            vehicle = plugin.getVehicleManager().getBySeat(entity);
        }

        if (vehicle == null) {
            for (VehicleInstance v : plugin.getVehicleManager().getActiveVehicles()) {
                if (v.getSeatEntity().getLocation().distanceSquared(entity.getLocation()) < 6.0) {
                    vehicle = v;
                    break;
                }
            }
        }

        if (vehicle == null) return;
        event.setCancelled(true);
        Player player = event.getPlayer();

        if (player.isSneaking()) {
            VehicleGUI.openDashboard(player, vehicle);
        } else {
            if (vehicle.isLocked() && !vehicle.getOwnerUUID().equals(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "This vehicle is locked by its owner!");
                return;
            }
            vehicle.getSeatEntity().addPassenger(player);
        }
    }

    @EventHandler
    public void onDashboardOrCatalogClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();

        // 1. Showroom Catalog GUI Click
        if (title.startsWith(VehicleCatalogGUI.CATALOG_TITLE)) {
            event.setCancelled(true);
            if (slot >= 45 && slot <= 51) {
                VehicleCategory cat = switch (slot) {
                    case 46 -> VehicleCategory.SUPERCAR;
                    case 47 -> VehicleCategory.CIVILIAN_CAR;
                    case 48 -> VehicleCategory.TRUCK_SUV;
                    case 49 -> VehicleCategory.CONSTRUCTION;
                    case 50 -> VehicleCategory.MILITARY;
                    case 51 -> VehicleCategory.FIXED_WING;
                    default -> null;
                };
                VehicleCatalogGUI.openCatalog(player, cat, 0);
            } else if (slot < 36) {
                ItemStack clicked = event.getCurrentItem();
                if (clicked != null && clicked.hasItemMeta() && player.hasPermission("blockvehicles.admin")) {
                    player.getInventory().addItem(clicked.clone());
                    player.sendMessage(ChatColor.GREEN + "Obtained " + clicked.getItemMeta().getDisplayName());
                }
            }
            return;
        }

        // 2. Control Dashboard GUI Click
        if (title.equals(VehicleGUI.DASHBOARD_TITLE)) {
            event.setCancelled(true);
            VehicleInstance vehicle = plugin.getVehicleManager().getActiveVehicles().stream()
                    .filter(v -> v.getSeatEntity().getLocation().distanceSquared(player.getLocation()) < 16.0)
                    .findFirst().orElse(null);

            if (vehicle == null) return;

            if (slot == 11) {
                player.openInventory(vehicle.getTrunk());
            } else if (slot == 13) {
                ItemStack cursor = event.getCursor();
                if (cursor != null && cursor.getType() != Material.AIR) {
                    double fuelAmount = plugin.getConfig().getDouble("fuel_rates." + cursor.getType().name(), 0.0);
                    if (fuelAmount > 0) {
                        vehicle.setFuel(vehicle.getFuel() + fuelAmount);
                        cursor.setAmount(cursor.getAmount() - 1);
                        player.sendMessage(ChatColor.GREEN + "Added +" + fuelAmount + " fuel!");
                        VehicleGUI.openDashboard(player, vehicle);
                    }
                }
            } else if (slot == 15) {
                if (vehicle.getOwnerUUID().equals(player.getUniqueId())) {
                    vehicle.setLocked(!vehicle.isLocked());
                    VehicleGUI.openDashboard(player, vehicle);
                }
            } else if (slot == 26) {
                plugin.getVehicleManager().removeVehicle(vehicle.getId());
                player.getInventory().addItem(plugin.getCustomItemRegistry().createVehicleKey(vehicle.getSpec()));
                player.closeInventory();
                player.sendMessage(ChatColor.YELLOW + "Vehicle dismantled and retrieved.");
            }
        }
    }
}
