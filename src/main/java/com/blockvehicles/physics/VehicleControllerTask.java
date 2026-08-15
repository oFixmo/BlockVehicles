package com.blockvehicles.physics;

import com.blockvehicles.BlockVehiclesPlugin;
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
                // Helicopter Vertical & Horizontal
                if (driver.isSneaking()) moveVector.setY(-0.30);
                else if (driver.getLocation().getPitch() < -15) moveVector.setY(0.30);
                Vector horizontal = lookDir.clone().setY(0).normalize().multiply(speed);
                moveVector.add(horizontal);
            } else {
                // Plane 3D Pitch Vector
                moveVector = lookDir.clone().multiply(speed);
            }
        } else {
            // Ground Mechanics
            Vector horizontal = lookDir.clone().setY(0).normalize().multiply(speed);
            moveVector.add(horizontal);

            // Step Climbing (Auto-steps up slabs/blocks)
            Location frontCheck = currentLoc.clone().add(horizontal.clone().normalize().multiply(1.0));
            if (frontCheck.getBlock().getType().isSolid()) {
                Location oneUp = frontCheck.clone().add(0, 1, 0);
                if (!oneUp.getBlock().getType().isSolid()) moveVector.setY(0.42);
            } else {
                Location below = currentLoc.clone().subtract(0, 0.5, 0);
                if (!below.getBlock().getType().isSolid()) moveVector.setY(-0.35);
            }

            // Mining Drill Execution
            if (vehicle.getSpec().canDrill()) {
                performExcavation(currentLoc, lookDir, vehicle);
            }
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
        Location drillPoint = loc.clone().add(dir.setY(0).normalize().multiply(2.0)).add(0, 0.5, 0);
        World world = loc.getWorld();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Block block = drillPoint.clone().add(x, y, z).getBlock();
                    Material mat = block.getType();
                    if (mat != Material.AIR && mat != Material.BEDROCK && mat != Material.BARRIER && mat.getHardness() >= 0) {
                        Collection<ItemStack> drops = block.getDrops();
                        for (ItemStack drop : drops) {
                            vehicle.getTrunk().addItem(drop);
                        }
                        block.setType(Material.AIR);
                        world.spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(0.5, 0.5, 0.5), 10, mat.createBlockData());
                        world.playSound(block.getLocation(), Sound.BLOCK_STONE_BREAK, 0.5f, 1.0f);
                    }
                }
            }
        }
    }
}
