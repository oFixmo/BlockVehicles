package com.blockvehicles;

import com.blockvehicles.commands.VehicleCommand;
import com.blockvehicles.items.CustomItemRegistry;
import com.blockvehicles.listeners.VehicleListener;
import com.blockvehicles.physics.VehicleControllerTask;
import com.blockvehicles.vehicle.VehicleManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockVehiclesPlugin extends JavaPlugin {
    private static BlockVehiclesPlugin instance;
    private CustomItemRegistry customItemRegistry;
    private VehicleManager vehicleManager;
    private VehicleControllerTask controllerTask;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.customItemRegistry = new CustomItemRegistry(this);
        this.customItemRegistry.registerAllRecipes();

        this.vehicleManager = new VehicleManager(this);

        getServer().getPluginManager().registerEvents(new VehicleListener(this), this);
        getCommand("vehicle").setExecutor(new VehicleCommand(this));

        this.controllerTask = new VehicleControllerTask(this);
        this.controllerTask.runTaskTimer(this, 1L, 1L);

        getLogger().info("BlockVehicles 2.0 initialized with 100 World Vehicles!");
    }

    @Override
    public void onDisable() {
        if (this.vehicleManager != null) {
            this.vehicleManager.despawnAll();
        }
    }

    public static BlockVehiclesPlugin getInstance() { return instance; }
    public CustomItemRegistry getCustomItemRegistry() { return customItemRegistry; }
    public VehicleManager getVehicleManager() { return vehicleManager; }
}
