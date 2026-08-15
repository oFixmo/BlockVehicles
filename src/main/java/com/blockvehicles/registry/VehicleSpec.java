package com.blockvehicles.registry;

import org.bukkit.Material;

public class VehicleSpec {
    private final String id;
    private final String name;
    private final String origin;
    private final VehicleCategory category;
    private final Material primaryMaterial;
    private final Material secondaryMaterial;
    private final Material glassMaterial;
    private final double speed;
    private final double maxFuel;
    private final double fuelBurnRate;
    private final int storageRows;
    private final boolean flying;
    private final boolean canHover;
    private final boolean canDrill;
    private final boolean hasSolar;
    private final float length;
    private final float width;
    private final float height;

    public VehicleSpec(String id, String name, String origin, VehicleCategory category,
                       Material primaryMaterial, Material secondaryMaterial, Material glassMaterial,
                       double speed, double maxFuel, double fuelBurnRate, int storageRows,
                       boolean flying, boolean canHover, boolean canDrill, boolean hasSolar,
                       float length, float width, float height) {
        this.id = id;
        this.name = name;
        this.origin = origin;
        this.category = category;
        this.primaryMaterial = primaryMaterial;
        this.secondaryMaterial = secondaryMaterial;
        this.glassMaterial = glassMaterial;
        this.speed = speed;
        this.maxFuel = maxFuel;
        this.fuelBurnRate = fuelBurnRate;
        this.storageRows = storageRows;
        this.flying = flying;
        this.canHover = canHover;
        this.canDrill = canDrill;
        this.hasSolar = hasSolar;
        this.length = length;
        this.width = width;
        this.height = height;
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
    public boolean canDrill() { return canDrill; }
    public boolean hasSolar() { return hasSolar; }
    public float getLength() { return length; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
}
