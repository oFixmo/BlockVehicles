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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VehicleControllerTask extends BukkitRunnable {
    private final BlockVehiclesPlugin plugin;
    private final Map<UUID, Location> lastLocations = new HashMap<>();

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

        // Animate Spinning Rotors on Helicopters
        if (vehicle.getSpec().isFlying() && vehicle.getSpec().canHover()) {
            vehicle.spinRotor();
        }

        if (driver == null) {
            lastLocations.remove(vehicle.getId());
            vehicle.updateModelPositions();
            return;
        }

        if (vehicle.getFuel() <= 0) {
            driver.sendActionBar(ChatColor.RED + "⚠ Fuel Empty! Shift + Right-Click to refuel.");
            vehicle.updateModelPositions();
            return;
        }

        // --- RELIABLE W-KEY THROTTLE DETECTION (NO IDLE GHOST MOVEMENT) ---
        // Player moves forward when sprinting or pressing forward
        boolean isForwardPressed = driver.isSprinting() || driver.isSneaking();
        
        // Check if player changed eye pitch or moved relative to previous frame
        Location prev = lastLocations.get(driver.getUniqueId());
        if (prev != null) {
            double delta = driver.getLocation().distanceSquared(prev);
            if (delta > 0.0001) {
                isForwardPressed = true;
            }
        }
        lastLocations.put(driver.getUniqueId(), driver.getLocation());

        Vector lookDir = driver.getLocation().getDirection();
        Vector moveVector = new Vector(0, 0, 0);

        double gearMultiplier = vehicle.getGearMultiplier();
        double speed = vehicle.getSpec().getSpeed() * gearMultiplier;
        double burnRate = vehicle.getSpec().getFuelBurnRate() * (gearMultiplier * 0.9);

        if (vehicle.getSpec().isFlying()) {
            if (vehicle.getSpec().canHover()) {
                // Helicopter Flight Mechanics
                if (driver.isSneaking()) {
                    // Descend to land
                    moveVector.setY(-0.30);
                } else if (driver.getLocation().getPitch() < -20) {
                    // Ascend into the sky
                    moveVector.setY(0.30);
                }

                // Horizontal movement ONLY when pressing W
                if (isForwardPressed) {
                    Vector horizontal = lookDir.clone().setY(0).normalize().multiply(speed);
                    moveVector.add(horizontal);
                }

                // Rotor wash wind particles on ground
                Location groundCheck = currentLoc.clone().subtract(0, 3.0, 0);
                if (groundCheck.getBlock().getType().isSolid()) {
                    currentLoc.getWorld().spawnParticle(Particle.CLOUD, groundCheck.add(0, 1.1, 0), 4, 1.2, 0.1, 1.2, 0.02);
                }
            } else {
                // Airplane Mechanics
                if (isForwardPressed) {
                    moveVector = lookDir.clone().multiply(speed);
                }
            }
        } else {
            // Ground Mechanics (Cars, Bikes, Tractors, Drills)
            if (isForwardPressed) {
                Vector horizontal = lookDir.clone().setY(0).normalize().multiply(speed);

                // Collision Stop
                Location frontCheck = currentLoc.clone().add(horizontal.clone().normalize().multiply(0.85));
                Block footBlock = frontCheck.getBlock();
                Block headBlock = frontCheck.clone().add(0, 1, 0).getBlock();

                if (vehicle.getSpec().canDrill()) {
                    performExcavation(currentLoc, lookDir, vehicle);
                }

                if (footBlock.getType().isSolid()) {
                    if (!headBlock.getType().isSolid() && !frontCheck.clone().add(0, 2, 0).getBlock().getType().isSolid()) {
                        moveVector.setY(0.42);
                        moveVector.add(horizontal.multiply(0.7));
                    } else if (!vehicle.getSpec().canDrill()) {
                        horizontal.zero();
                    }
                } else {
                    Location below = currentLoc.clone().subtract(0, 0.4, 0);
                    if (!below.getBlock().getType().isSolid()) {
                        moveVector.setY(-0.35);
                    }
                }
                moveVector.add(horizontal);
            } else {
                // Completely stopped when idle!
                Location below = currentLoc.clone().subtract(0, 0.4, 0);
                if (!below.getBlock().getType().isSolid()) {
                    moveVector.setY(-0.35);
                }
            }
        }

        Location newLoc = currentLoc.clone().add(moveVector);
        newLoc.setYaw(driver.getLocation().getYaw());
        newLoc.setPitch(vehicle.getSpec().isFlying() ? driver.getLocation().getPitch() : 0);

        vehicle.getSeatEntity().teleport(newLoc);
        vehicle.updateModelPositions();

        if (isForwardPressed) {
            vehicle.setFuel(vehicle.getFuel() - burnRate);
        }

        int fuelPercent = (int) ((vehicle.getFuel() / vehicle.getSpec().getMaxFuel()) * 100);
        ChatColor color = fuelPercent > 50 ? ChatColor.GREEN : (fuelPercent > 20 ? ChatColor.YELLOW : ChatColor.RED);
        String[] gearIcons = {"", "[ECO]", "[CRUISE]", "[SPORT]", "[⚡NITRO]"};
        driver.sendActionBar(ChatColor.GOLD + vehicle.getSpec().getName() + " " + ChatColor.AQUA + gearIcons[vehicle.getGear()] + " | Fuel: " + color + fuelPercent + "%");
    }

    private void performExcavation(Location loc, Vector dir, VehicleInstance vehicle) {
        World world = loc.getWorld();
        Location targetCenter = loc.clone().add(dir.setY(0).normalize().multiply(1.8));

        for (int x = -1; x <= 1; x++) {
            for (int y = 0; y <= 2; y++) {
                for (int z = -1; z <= 1; z++) {
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
