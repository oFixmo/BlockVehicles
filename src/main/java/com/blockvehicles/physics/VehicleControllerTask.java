package com.blockvehicles.physics;

import com.blockvehicles.BlockVehiclesPlugin;
import com.blockvehicles.registry.VehicleSpec;
import com.blockvehicles.vehicle.VehicleInstance;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collection;

public class VehicleControllerTask extends BukkitRunnable {
    private final BlockVehiclesPlugin plugin;

    public VehicleControllerTask(BlockVehiclesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (VehicleInstance v : plugin.getVehicleManager().getActiveVehicles()) {
            handleVehicleTick(v);
        }
    }

    private void handleVehicleTick(VehicleInstance vehicle) {
        Player driver = vehicle.getDriver();
        Location currentLoc = vehicle.getSeatEntity().getLocation();

        // 1. Solar Recharging
        if (vehicle.getSpec().hasSolar() && currentLoc.getWorld().isDayTime() && currentLoc.getBlock().getLightFromSky() == 15) {
            double solarRate = plugin.getConfig().getDouble("solar.recharge_per_second", 2.0) / 20.0;
            vehicle.setFuel(vehicle.getFuel() + solarRate);
        }

        if (driver == null) {
            vehicle.updateModelPositions();
            return;
        }

        // 2. Fuel Depletion Check
        if (vehicle.getFuel() <= 0) {
            driver.sendActionBar(ChatColor.RED + "⚠ Fuel Empty! Shift + Right-Click to refuel.");
            vehicle.updateModelPositions();
            return;
        }

        Vector lookDir = driver.getLocation().getDirection();
        Vector moveVector = new Vector(0, 0, 0);
        double speed = vehicle.getSpec().getSpeed();
        double burnRate = vehicle.getSpec().getFuelBurnRate();

        if (vehicle.getSpec().isFlying()) {
            if (vehicle.getSpec().canHover()) {
                if (driver.isSneaking()) moveVector.setY(-0.30);
                else if (driver.getLocation().getPitch() < -15) moveVector.setY(0.30);
                Vector horizontal = lookDir.clone().setY(0).normalize().multiply(speed);
                moveVector.add(horizontal);
            } else {
                moveVector = lookDir.clone().multiply(speed);
            }
        } else {
            // Ground Mechanics
            Vector horizontal = lookDir.clone().setY(0).normalize().multiply(speed);

            // True Solid Collision Detection
            Location frontCheck = currentLoc.clone().add(horizontal.clone().normalize().multiply(0.85));
            Block footBlock = frontCheck.getBlock();
            Block headBlock = frontCheck.clone().add(0, 1, 0).getBlock();

            // Downward Shaft Drilling vs Horizontal
            if (vehicle.getSpec().canDrill()) {
                performExcavation(currentLoc, lookDir, vehicle);
            }

            if (footBlock.getType().isSolid()) {
                if (!headBlock.getType().isSolid() && !frontCheck.clone().add(0, 2, 0).getBlock().getType().isSolid()) {
                    // Step up 1 block / slab
                    moveVector.setY(0.42);
                    moveVector.add(horizontal.multiply(0.7));
                } else if (!vehicle.getSpec().canDrill()) {
                    // Blocked by wall: STOP completely, do not phase through
                    horizontal.zero();
                }
            } else {
                // Gravity / Slope
                Location below = currentLoc.clone().subtract(0, 0.4, 0);
                if (!below.getBlock().getType().isSolid()) {
                    moveVector.setY(-0.35);
                }
            }
            moveVector.add(horizontal);
        }

        Location newLoc = currentLoc.clone().add(moveVector);
        newLoc.setYaw(driver.getLocation().getYaw());
        newLoc.setPitch(vehicle.getSpec().isFlying() ? driver.getLocation().getPitch() : 0);

        vehicle.getSeatEntity().teleport(newLoc);
        vehicle.updateModelPositions();

        vehicle.setFuel(vehicle.getFuel() - burnRate);

        int fuelPercent = (int) ((vehicle.getFuel() / vehicle.getSpec().getMaxFuel()) * 100);
        ChatColor color = fuelPercent > 50 ? ChatColor.GREEN : (fuelPercent > 20 ? ChatColor.YELLOW : ChatColor.RED);
        driver.sendActionBar(ChatColor.GOLD + vehicle.getSpec().getName() + " | Fuel: " + color + fuelPercent + "% " + ChatColor.DARK_GRAY + "(" + String.format("%.0f", vehicle.getFuel()) + ")");
    }

    private void performExcavation(Location loc, Vector dir, VehicleInstance vehicle) {
        World world = loc.getWorld();
        VehicleSpec.DrillMode mode = vehicle.getSpec().getDrillMode();

        int minX = -1, maxX = 1, minY = 0, maxY = 2, minZ = -1, maxZ = 1;
        Location targetCenter;

        switch (mode) {
            case SHAFT_DOWN_3X3 -> {
                targetCenter = loc.clone().subtract(0, 1.0, 0);
                minX = -1; maxX = 1; minY = -2; maxY = 0; minZ = -1; maxZ = 1;
            }
            case SHAFT_DOWN_5X5 -> {
                targetCenter = loc.clone().subtract(0, 1.0, 0);
                minX = -2; maxX = 2; minY = -2; maxY = 0; minZ = -2; maxZ = 2;
            }
            case COMPACT_2X2 -> {
                targetCenter = loc.clone().add(dir.setY(0).normalize().multiply(1.5));
                minX = 0; maxX = 1; minY = 0; maxY = 1; minZ = 0; maxZ = 1;
            }
            case MEGA_5X5 -> {
                targetCenter = loc.clone().add(dir.setY(0).normalize().multiply(2.2));
                minX = -2; maxX = 2; minY = 0; maxY = 4; minZ = -2; maxZ = 2;
            }
            default -> { // TUNNEL_3X3
                targetCenter = loc.clone().add(dir.setY(0).normalize().multiply(1.8));
                minX = -1; maxX = 1; minY = 0; maxY = 2; minZ = -1; maxZ = 1;
            }
        }

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = targetCenter.clone().add(x, y, z).getBlock();
                    Material mat = block.getType();
                    if (mat != Material.AIR && mat != Material.BEDROCK && mat != Material.BARRIER && mat.getHardness() >= 0) {
                        Collection<ItemStack> drops = block.getDrops();
                        for (ItemStack drop : drops) {
                            vehicle.getTrunk().addItem(drop);
                        }
                        block.setType(Material.AIR);
                        world.spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(0.5, 0.5, 0.5), 6, mat.createBlockData());
                        world.playSound(block.getLocation(), Sound.BLOCK_STONE_BREAK, 0.3f, 1.0f);
                    }
                }
            }
        }
    }
}
