package com.blockvehicles.vehicle;

import com.blockvehicles.BlockVehiclesPlugin;
import com.blockvehicles.registry.VehicleSpec;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.*;

public class VehicleManager {
    private final BlockVehiclesPlugin plugin;
    private final Map<UUID, VehicleInstance> activeVehicles = new HashMap<>();

    public VehicleManager(BlockVehiclesPlugin plugin) {
        this.plugin = plugin;
    }

    public VehicleInstance spawnVehicle(Location loc, VehicleSpec spec, UUID owner) {
        VehicleInstance vehicle = new VehicleInstance(plugin, loc, spec, owner);
        activeVehicles.put(vehicle.getId(), vehicle);
        return vehicle;
    }

    public VehicleInstance getBySeat(Entity seat) {
        for (VehicleInstance v : activeVehicles.values()) {
            if (v.getSeatEntity().getUniqueId().equals(seat.getUniqueId())) {
                return v;
            }
        }
        return null;
    }

    public void removeVehicle(UUID id) {
        VehicleInstance v = activeVehicles.remove(id);
        if (v != null) v.remove();
    }

    public void despawnAll() {
        for (VehicleInstance v : activeVehicles.values()) v.remove();
        activeVehicles.clear();
    }

    public Collection<VehicleInstance> getActiveVehicles() {
        return activeVehicles.values();
    }
}
