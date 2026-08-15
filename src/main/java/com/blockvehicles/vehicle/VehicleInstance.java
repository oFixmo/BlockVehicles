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

import java.util.List;
import java.util.UUID;

public class VehicleInstance {
    private final UUID id;
    private final VehicleSpec spec;
    private final UUID ownerUUID;
    private final ArmorStand seatEntity;
    private final List<BlockDisplay> modelParts;
    private final Inventory trunk;
    private double fuel;
    private boolean locked = false;

    public VehicleInstance(BlockVehiclesPlugin plugin, Location spawnLoc, VehicleSpec spec, UUID ownerUUID) {
        this.id = UUID.randomUUID();
        this.spec = spec;
        this.ownerUUID = ownerUUID;
        this.fuel = spec.getMaxFuel() * 0.5; // Starts half full
        this.trunk = Bukkit.createInventory(null, spec.getStorageRows() * 9, spec.getName() + " Cargo");

        // Spawn Solid Clickable Seat
        this.seatEntity = (ArmorStand) spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.ARMOR_STAND);
        this.seatEntity.setVisible(false);
        this.seatEntity.setGravity(false);
        this.seatEntity.setInvulnerable(true);
        this.seatEntity.setCustomName(spec.getName());
        this.seatEntity.setCustomNameVisible(false);

        // Build Realistic Multi-part Display Model
        this.modelParts = ProceduralVehicleModelBuilder.buildModel(spawnLoc.getWorld(), spawnLoc, spec);
    }

    public void updateModelPositions() {
        Location seatLoc = seatEntity.getLocation();
        for (BlockDisplay part : modelParts) {
            if (part != null && !part.isDead()) {
                part.teleport(seatLoc);
            }
        }
    }

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
