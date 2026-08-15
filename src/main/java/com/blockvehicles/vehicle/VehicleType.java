package com.blockvehicles.vehicle;

import org.bukkit.Material;

public enum VehicleType {
    CAR("Car", Material.MINECART, false, false, false),
    TRUCK("Cargo Truck", Material.CHEST_MINECART, false, false, false),
    VAN("Passenger Van", Material.MINECART, false, false, false),
    DRILLING_MACHINE("Drilling Machine", Material.HOPPER_MINECART, false, true, false),
    PLANE("Airplane", Material.FEATHER, true, false, false),
    HELICOPTER("Helicopter", Material.COMPASS, true, false, true);

    private final String displayName;
    private final Material icon;
    private final boolean flying;
    private final boolean canDrill;
    private final boolean canHover;

    VehicleType(String displayName, Material icon, boolean flying, boolean canDrill, boolean canHover) {
        this.displayName = displayName;
        this.icon = icon;
        this.flying = flying;
        this.canDrill = canDrill;
        this.canHover = canHover;
    }

    public String getDisplayName() { return displayName; }
    public Material getIcon() { return icon; }
    public boolean isFlying() { return flying; }
    public boolean canDrill() { return canDrill; }
    public boolean canHover() { return canHover; }
}
