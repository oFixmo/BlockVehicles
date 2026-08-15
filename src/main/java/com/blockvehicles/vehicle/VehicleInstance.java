package com.blockvehicles.vehicle;

import com.blockvehicles.BlockVehiclesPlugin;
import com.blockvehicles.models.ProceduralVehicleModelBuilder;
import com.blockvehicles.registry.VehicleSpec;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.List;
import java.util.UUID;

public class VehicleInstance {
    private final UUID id;
    private final VehicleSpec spec;
    private final UUID ownerUUID;
    private final ArmorStand seatEntity;
    private final List<BlockDisplay> modelParts;
    private BlockDisplay rotorPart = null;
    private final Inventory trunk;
    private double fuel;
    private int gear = 3;
    private boolean locked = false;
    private float rotorAngle = 0.0f;

    public VehicleInstance(BlockVehiclesPlugin plugin, Location spawnLoc, VehicleSpec spec, UUID ownerUUID) {
        this.id = UUID.randomUUID();
        this.spec = spec;
        this.ownerUUID = ownerUUID;
        this.fuel = spec.getMaxFuel() * 0.5;
        this.trunk = Bukkit.createInventory(null, spec.getStorageRows() * 9, spec.getName() + " Cargo");

        this.seatEntity = (ArmorStand) spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.ARMOR_STAND);
        this.seatEntity.setVisible(false);
        this.seatEntity.setGravity(false);
        this.seatEntity.setInvulnerable(true);
        this.seatEntity.setCustomName(spec.getName());
        this.seatEntity.setCustomNameVisible(false);

        this.modelParts = ProceduralVehicleModelBuilder.buildModel(spawnLoc.getWorld(), spawnLoc, spec);

        if (spec.isFlying() && spec.canHover() && modelParts.size() >= 2) {
            this.rotorPart = modelParts.get(modelParts.size() - 1);
        }
    }

    public void updateModelPositions() {
        Location seatLoc = seatEntity.getLocation();
        for (BlockDisplay part : modelParts) {
            if (part != null && !part.isDead()) {
                part.teleport(seatLoc);
            }
        }
    }

    public void spinRotor() {
        if (rotorPart != null && !rotorPart.isDead()) {
            rotorAngle += 0.65f;
            if (rotorAngle > 6.28f) rotorAngle = 0.0f;
            float w = spec.getWidth();
            float h = spec.getHeight();
            rotorPart.setTransformation(new Transformation(
                    new Vector3f(0.0f, h + 0.4f, 0.0f),
                    new AxisAngle4f(rotorAngle, 0.0f, 1.0f, 0.0f),
                    new Vector3f(w * 2.2f, 0.08f, 0.4f),
                    new AxisAngle4f(0.0f, 0.0f, 0.0f, 1.0f)
            ));
            rotorPart.setInterpolationDuration(1);
        }
    }

    public void cycleGear(Player player) {
        gear++;
        if (gear > 4) gear = 1;
        String[] gearNames = {"", "ECO (30%)", "CRUISE (60%)", "SPORT (100%)", "NITRO OVERDRIVE (160%)"};
        org.bukkit.ChatColor[] gearColors = {org.bukkit.ChatColor.WHITE, org.bukkit.ChatColor.GREEN, org.bukkit.ChatColor.AQUA, org.bukkit.ChatColor.GOLD, org.bukkit.ChatColor.RED};
        player.sendTitle("", gearColors[gear] + "⚡ Shifted to " + gearNames[gear], 5, 20, 5);
        player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_BELL, 0.7f, 1.5f + (gear * 0.2f));
    }

    public double getGearMultiplier() {
        return switch (gear) {
            case 1 -> 0.30;
            case 2 -> 0.60;
            case 4 -> 1.60;
            default -> 1.00;
        };
    }

    public int getGear() { return gear; }

    public void remove() {
        if (seatEntity != null && !seatEntity.isDead()) seatEntity.remove();
        for (BlockDisplay part : modelParts) {
            if (part != null && !part.isDead()) part.remove();
        }
    }

    public Player getDriver() {
        if (seatEntity.getPassengers().isEmpty()) return null;
        if (seatEntity.getPassengers().get(0) instanceof Player p) return p;
        return null;
    }

    public UUID getId() { return id; }
    public VehicleSpec getSpec() { return spec; }
    public UUID getOwnerUUID() { return ownerUUID; }
    public ArmorStand getSeatEntity() { return seatEntity; }
    public Inventory getTrunk() { return trunk; }
    public double getFuel() { return fuel; }
    public void setFuel(double fuel) { this.fuel = Math.min(spec.getMaxFuel(), Math.max(0, fuel)); }
    public boolean isLocked() { return locked; }
    public void setLocked(boolean locked) { this.locked = locked; }
}
