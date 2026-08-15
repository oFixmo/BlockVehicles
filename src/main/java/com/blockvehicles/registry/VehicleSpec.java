package com.blockvehicles.registry;

import org.bukkit.Material;

public class VehicleSpec {
    public enum DrillMode { NONE, COMPACT_2X2, TUNNEL_3X3, MEGA_5X5, SHAFT_DOWN_3X3, SHAFT_DOWN_5X5 }

    private final String id, name, origin;
    private final VehicleCategory category;
    private final Material primaryMaterial, secondaryMaterial, glassMaterial;
    private final double speed, maxFuel, fuelBurnRate;
    private final int storageRows;
    private final boolean flying, canHover, hasSolar;
    private final DrillMode drillMode;
    private final float length, width, height;

    public VehicleSpec(String id, String name, String origin, VehicleCategory category,
                       Material primaryMaterial, Material secondaryMaterial, Material glassMaterial,
                       double speed, double maxFuel, double fuelBurnRate, int storageRows,
                       boolean flying, boolean canHover, DrillMode drillMode, boolean hasSolar,
                       float length, float width, float height) {
        this.id = id; this.name = name; this.origin = origin; this.category = category;
        this.primaryMaterial = primaryMaterial; this.secondaryMaterial = secondaryMaterial; this.glassMaterial = glassMaterial;
        this.speed = speed; this.maxFuel = maxFuel; this.fuelBurnRate = fuelBurnRate; this.storageRows = storageRows;
        this.flying = flying; this.canHover = canHover; this.drillMode = drillMode; this.hasSolar = hasSolar;
        this.length = length; this.width = width; this.height = height;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getOrigin() { return origin; }
    public VehicleCategory getCategory() { return category; }
    public Material getPrimaryMaterial() { return primaryMaterial; }
    public Material getSecondaryMaterial() { return secondaryMaterial; }
    public Material getGlassMaterial() { return glassMaterial; }
    public double getSpeed() { return speed; }
    public double getMaxFuel() { return maxFuel; }
    public double getFuelBurnRate() { return fuelBurnRate; }
    public int getStorageRows() { return storageRows; }
    public boolean isFlying() { return flying; }
    public boolean canHover() { return canHover; }
    public boolean canDrill() { return drillMode != DrillMode.NONE; }
    public DrillMode getDrillMode() { return drillMode; }
    public boolean hasSolar() { return hasSolar; }
    public float getLength() { return length; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
}
