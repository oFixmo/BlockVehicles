#!/bin/bash
set -e

echo "=== Upgrading BlockVehicles to v2.0 (100 Real-World Vehicles & Custom Crafting) ==="

# 1. Update pom.xml
cat << 'INNER_EOF' > pom.xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.blockvehicles</groupId>
    <artifactId>BlockVehicles</artifactId>
    <version>2.0.0</version>
    <packaging>jar</packaging>
    <name>BlockVehicles</name>
    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <maven.compiler.release>17</maven.compiler.release>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    <repositories>
        <repository>
            <id>papermc</id>
            <url>https://repo.papermc.io/repository/maven-public/</url>
        </repository>
    </repositories>
    <dependencies>
        <dependency>
            <groupId>io.papermc.paper</groupId>
            <artifactId>paper-api</artifactId>
            <version>1.20.4-R0.1-SNAPSHOT</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <release>17</release>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
INNER_EOF

# 2. Make directories
mkdir -p src/main/resources src/main/java/com/blockvehicles/{items,models,registry,vehicle,physics,gui,listeners,commands}

# 3. Create plugin.yml
cat << 'INNER_EOF' > src/main/resources/plugin.yml
name: BlockVehicles
version: 2.0.0
main: com.blockvehicles.BlockVehiclesPlugin
api-version: '1.20'
author: Assistant
description: 100 World Vehicles with real scale, display models, drilling, flight, fuel & custom crafting.
commands:
  vehicle:
    description: Main vehicle command
    aliases: [vehicles, v]
    permission: blockvehicles.use
permissions:
  blockvehicles.use:
    default: true
  blockvehicles.admin:
    default: op
INNER_EOF

# 4. Create config.yml
cat << 'INNER_EOF' > src/main/resources/config.yml
# BlockVehicles 2.0 Global Settings

fuel_rates:
  COAL: 30.0
  CHARCOAL: 30.0
  COAL_BLOCK: 300.0
  LAVA_BUCKET: 600.0
  REDSTONE: 20.0
  REDSTONE_BLOCK: 200.0
  BATTERY: 450.0
  HIGH_CAPACITY_BATTERY: 1200.0

solar:
  recharge_per_second: 2.0
  requires_direct_sunlight: true

physics:
  max_climb_step: 1.1
  gravity_acceleration: 0.35
  flight_pitch_multiplier: 1.2
INNER_EOF

# 5. Create VehicleCategory.java
cat << 'INNER_EOF' > src/main/java/com/blockvehicles/registry/VehicleCategory.java
package com.blockvehicles.registry;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum VehicleCategory {
    SUPERCAR("Supercars & Hypercars", Material.REDSTONE_BLOCK, ChatColor.RED),
    CIVILIAN_CAR("Everyday & Classic Cars", Material.IRON_BLOCK, ChatColor.WHITE),
    TRUCK_SUV("Trucks, Pickups & SUVs", Material.COPPER_BLOCK, ChatColor.GOLD),
    SEMI_HEAVY("Semi-Trucks & Heavy Haulers", Material.NETHERITE_BLOCK, ChatColor.DARK_GRAY),
    PUBLIC_TRANSIT("Buses & Public Transit", Material.GOLD_BLOCK, ChatColor.YELLOW),
    EMERGENCY("Emergency & Service", Material.LAPIS_BLOCK, ChatColor.BLUE),
    CONSTRUCTION("Construction & Mining", Material.DIAMOND_BLOCK, ChatColor.AQUA),
    AGRICULTURE("Agricultural & Industrial", Material.HAY_BLOCK, ChatColor.GREEN),
    MILITARY("Military & Armored", Material.OBSIDIAN, ChatColor.DARK_GREEN),
    ROTORCRAFT("Helicopters & Gyros", Material.FEATHER, ChatColor.LIGHT_PURPLE),
    FIXED_WING("Planes & Supersonic Jets", Material.FIREWORK_ROCKET, ChatColor.DARK_AQUA);

    private final String title;
    private final Material icon;
    private final ChatColor color;

    VehicleCategory(String title, Material icon, ChatColor color) {
        this.title = title;
        this.icon = icon;
        this.color = color;
    }

    public String getTitle() { return title; }
    public Material getIcon() { return icon; }
    public ChatColor getColor() { return color; }
}
INNER_EOF

# 6. Create VehicleSpec.java
cat << 'INNER_EOF' > src/main/java/com/blockvehicles/registry/VehicleSpec.java
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
INNER_EOF

# 7. Create VehicleRegistry.java (100 Distinct Real-World Vehicles)
cat << 'INNER_EOF' > src/main/java/com/blockvehicles/registry/VehicleRegistry.java
package com.blockvehicles.registry;

import org.bukkit.Material;
import java.util.*;

public class VehicleRegistry {
    private static final Map<String, VehicleSpec> VEHICLES = new LinkedHashMap<>();

    static {
        // --- 1. SUPERCARS & HYPERCARS (1-10) ---
        reg("BUGATTI_CHIRON", "Bugatti Chiron", "France", VehicleCategory.SUPERCAR, Material.BLUE_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.95, 800, 0.22, 2, false, false, false, false, 4.6f, 2.1f, 1.25f);
        reg("FERRARI_488", "Ferrari 488 Pista", "Italy", VehicleCategory.SUPERCAR, Material.RED_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.90, 750, 0.20, 2, false, false, false, false, 4.5f, 2.0f, 1.20f);
        reg("LAMBO_AVENTADOR", "Lamborghini Aventador", "Italy", VehicleCategory.SUPERCAR, Material.YELLOW_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.92, 780, 0.21, 2, false, false, false, false, 4.7f, 2.1f, 1.15f);
        reg("PORSCHE_911_GT3", "Porsche 911 GT3 RS", "Germany", VehicleCategory.SUPERCAR, Material.LIME_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.88, 700, 0.19, 2, false, false, false, false, 4.5f, 1.9f, 1.30f);
        reg("MCLAREN_P1", "McLaren P1 Hybrid", "UK", VehicleCategory.SUPERCAR, Material.ORANGE_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.93, 760, 0.18, 2, false, false, false, true, 4.6f, 2.0f, 1.20f);
        reg("KOENIGSEGG_JESKO", "Koenigsegg Jesko", "Sweden", VehicleCategory.SUPERCAR, Material.WHITE_CONCRETE, Material.ORANGE_CONCRETE, Material.TINTED_GLASS, 0.98, 850, 0.24, 2, false, false, false, false, 4.8f, 2.1f, 1.22f);
        reg("NISSAN_GTR_R35", "Nissan GT-R Nismo", "Japan", VehicleCategory.SUPERCAR, Material.WHITE_CONCRETE, Material.RED_CONCRETE, Material.TINTED_GLASS, 0.86, 720, 0.18, 3, false, false, false, false, 4.7f, 1.9f, 1.38f);
        reg("FORD_GT", "Ford GT Carbon", "USA", VehicleCategory.SUPERCAR, Material.BLUE_CONCRETE, Material.WHITE_CONCRETE, Material.TINTED_GLASS, 0.89, 740, 0.20, 2, false, false, false, false, 4.7f, 2.0f, 1.15f);
        reg("ASTON_MARTIN_DB11", "Aston Martin DB11", "UK", VehicleCategory.SUPERCAR, Material.CYAN_TERRACOTTA, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.85, 750, 0.18, 3, false, false, false, false, 4.7f, 2.0f, 1.30f);
        reg("CORVETTE_C8", "Chevrolet Corvette C8 Z06", "USA", VehicleCategory.SUPERCAR, Material.YELLOW_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.87, 720, 0.19, 2, false, false, false, false, 4.6f, 2.0f, 1.25f);

        // --- 2. EVERYDAY & CLASSIC CARS (11-20) ---
        reg("TOYOTA_COROLLA", "Toyota Corolla Altis", "Japan", VehicleCategory.CIVILIAN_CAR, Material.WHITE_CONCRETE, Material.GRAY_CONCRETE, Material.GLASS, 0.50, 600, 0.10, 3, false, false, false, false, 4.6f, 1.8f, 1.45f);
        reg("HONDA_CIVIC_TYPE_R", "Honda Civic Type R", "Japan", VehicleCategory.CIVILIAN_CAR, Material.RED_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.65, 620, 0.12, 3, false, false, false, false, 4.6f, 1.9f, 1.40f);
        reg("VW_BEETLE_1968", "Volkswagen Classic Beetle", "Germany", VehicleCategory.CIVILIAN_CAR, Material.LIGHT_BLUE_CONCRETE, Material.WHITE_CONCRETE, Material.GLASS, 0.40, 450, 0.08, 2, false, false, false, false, 4.0f, 1.6f, 1.50f);
        reg("MINI_COOPER_S", "Mini Cooper S", "UK", VehicleCategory.CIVILIAN_CAR, Material.GREEN_CONCRETE, Material.WHITE_CONCRETE, Material.GLASS, 0.55, 500, 0.09, 2, false, false, false, false, 3.8f, 1.7f, 1.42f);
        reg("TESLA_MODEL_S", "Tesla Model S Plaid", "USA", VehicleCategory.CIVILIAN_CAR, Material.RED_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.85, 900, 0.10, 4, false, false, false, true, 4.9f, 2.0f, 1.44f);
        reg("FORD_MUSTANG_1969", "1969 Ford Mustang Boss 429", "USA", VehicleCategory.CIVILIAN_CAR, Material.BLACK_CONCRETE, Material.WHITE_CONCRETE, Material.GLASS, 0.70, 650, 0.16, 3, false, false, false, false, 4.8f, 1.9f, 1.35f);
        reg("TOYOTA_SUPRA_MK4", "Toyota Supra MK4 Turbo", "Japan", VehicleCategory.CIVILIAN_CAR, Material.ORANGE_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.78, 680, 0.15, 3, false, false, false, false, 4.5f, 1.8f, 1.28f);
        reg("BMW_M3_E46", "BMW M3 GTR E46", "Germany", VehicleCategory.CIVILIAN_CAR, Material.LIGHT_GRAY_CONCRETE, Material.BLUE_CONCRETE, Material.TINTED_GLASS, 0.72, 650, 0.14, 3, false, false, false, false, 4.5f, 1.8f, 1.38f);
        reg("TATA_NANO", "Tata Nano City Car", "India", VehicleCategory.CIVILIAN_CAR, Material.YELLOW_CONCRETE, Material.GRAY_CONCRETE, Material.GLASS, 0.38, 350, 0.06, 2, false, false, false, false, 3.1f, 1.5f, 1.65f);
        reg("PREMIER_PADMINI", "Premier Padmini Classic", "India", VehicleCategory.CIVILIAN_CAR, Material.BLACK_CONCRETE, Material.YELLOW_CONCRETE, Material.GLASS, 0.42, 450, 0.08, 3, false, false, false, false, 4.1f, 1.6f, 1.48f);

        // --- 3. TRUCKS, PICKUPS & SUVS (21-30) ---
        reg("FORD_F150_RAPTOR", "Ford F-150 Raptor R", "USA", VehicleCategory.TRUCK_SUV, Material.BLUE_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.60, 950, 0.16, 5, false, false, false, false, 5.8f, 2.2f, 2.05f);
        reg("TESLA_CYBERTRUCK", "Tesla Cybertruck Tri-Motor", "USA", VehicleCategory.TRUCK_SUV, Material.IRON_BLOCK, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.75, 1100, 0.12, 5, false, false, false, true, 5.7f, 2.2f, 1.95f);
        reg("MERCEDES_G63_AMG", "Mercedes-Benz G63 AMG 6x6", "Germany", VehicleCategory.TRUCK_SUV, Material.BLACK_CONCRETE, Material.IRON_BLOCK, Material.TINTED_GLASS, 0.65, 1000, 0.18, 5, false, false, false, false, 5.8f, 2.2f, 2.20f);
        reg("JEEP_WRANGLER", "Jeep Wrangler Rubicon", "USA", VehicleCategory.TRUCK_SUV, Material.RED_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.52, 750, 0.13, 4, false, false, false, false, 4.7f, 1.9f, 1.85f);
        reg("TOYOTA_LAND_CRUISER", "Toyota Land Cruiser 300", "Japan", VehicleCategory.TRUCK_SUV, Material.WHITE_CONCRETE, Material.GRAY_CONCRETE, Material.TINTED_GLASS, 0.56, 900, 0.14, 5, false, false, false, false, 5.0f, 2.0f, 1.95f);
        reg("RAM_1500_TRX", "RAM 1500 TRX Supercharged", "USA", VehicleCategory.TRUCK_SUV, Material.RED_TERRACOTTA, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.64, 980, 0.18, 5, false, false, false, false, 5.9f, 2.2f, 2.08f);
        reg("LAND_ROVER_DEFENDER", "Land Rover Defender 110", "UK", VehicleCategory.TRUCK_SUV, Material.GREEN_TERRACOTTA, Material.WHITE_CONCRETE, Material.TINTED_GLASS, 0.54, 850, 0.14, 5, false, false, false, false, 5.0f, 2.0f, 1.97f);
        reg("RIVIAN_R1T", "Rivian R1T Adventure EV", "USA", VehicleCategory.TRUCK_SUV, Material.CYAN_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.68, 1050, 0.11, 5, false, false, false, true, 5.5f, 2.1f, 1.92f);
        reg("CHEVY_SILVERADO", "Chevrolet Silverado High Country", "USA", VehicleCategory.TRUCK_SUV, Material.BROWN_CONCRETE, Material.IRON_BLOCK, Material.TINTED_GLASS, 0.55, 920, 0.15, 5, false, false, false, false, 5.8f, 2.1f, 1.98f);
        reg("MAHINDRA_THAR", "Mahindra Thar 4x4", "India", VehicleCategory.TRUCK_SUV, Material.BLACK_CONCRETE, Material.RED_CONCRETE, Material.TINTED_GLASS, 0.50, 700, 0.12, 3, false, false, false, false, 4.0f, 1.8f, 1.84f);

        // --- 4. SEMI-TRUCKS & HEAVY HAULERS (31-40) ---
        reg("SCANIA_R730", "Scania R730 V8 Heavy Hauler", "Sweden", VehicleCategory.SEMI_HEAVY, Material.RED_CONCRETE, Material.IRON_BLOCK, Material.TINTED_GLASS, 0.40, 1800, 0.22, 6, false, false, false, false, 8.5f, 2.5f, 3.80f);
        reg("VOLVO_FH16", "Volvo FH16 750 Globetrotter", "Sweden", VehicleCategory.SEMI_HEAVY, Material.BLUE_CONCRETE, Material.IRON_BLOCK, Material.TINTED_GLASS, 0.42, 1850, 0.22, 6, false, false, false, false, 8.6f, 2.5f, 3.85f);
        reg("PETERBILT_379", "Peterbilt 379 American Classic", "USA", VehicleCategory.SEMI_HEAVY, Material.PURPLE_CONCRETE, Material.IRON_BLOCK, Material.GLASS, 0.45, 1900, 0.25, 6, false, false, false, false, 9.2f, 2.6f, 3.90f);
        reg("KENWORTH_W900", "Kenworth W900 Long Haul", "USA", VehicleCategory.SEMI_HEAVY, Material.WHITE_CONCRETE, Material.RED_CONCRETE, Material.GLASS, 0.44, 1900, 0.25, 6, false, false, false, false, 9.4f, 2.6f, 3.95f);
        reg("MERCEDES_ACTROS", "Mercedes-Benz Actros GigaSpace", "Germany", VehicleCategory.SEMI_HEAVY, Material.LIGHT_GRAY_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.42, 1750, 0.20, 6, false, false, false, false, 8.2f, 2.5f, 3.80f);
        reg("MACK_ANTHEM", "Mack Anthem Highway Titan", "USA", VehicleCategory.SEMI_HEAVY, Material.BLACK_CONCRETE, Material.GOLD_BLOCK, Material.TINTED_GLASS, 0.43, 1850, 0.23, 6, false, false, false, false, 8.8f, 2.5f, 3.85f);
        reg("MAN_TGX", "MAN TGX Individual Lion S", "Germany", VehicleCategory.SEMI_HEAVY, Material.RED_TERRACOTTA, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.41, 1800, 0.21, 6, false, false, false, false, 8.4f, 2.5f, 3.80f);
        reg("IVECO_STRALIS", "Iveco S-Way Heavy Transport", "Italy", VehicleCategory.SEMI_HEAVY, Material.YELLOW_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.40, 1700, 0.20, 6, false, false, false, false, 8.0f, 2.5f, 3.75f);
        reg("TATA_PRIMA", "Tata Prima 5530.S Heavy Hauler", "India", VehicleCategory.SEMI_HEAVY, Material.ORANGE_CONCRETE, Material.GRAY_CONCRETE, Material.GLASS, 0.38, 1600, 0.20, 6, false, false, false, false, 7.8f, 2.5f, 3.65f);
        reg("KAMAZ_MASTER", "Kamaz 43509 Dakar Rally Racer", "Russia", VehicleCategory.SEMI_HEAVY, Material.BLUE_CONCRETE, Material.WHITE_CONCRETE, Material.GLASS, 0.58, 2200, 0.28, 6, false, false, false, false, 7.2f, 2.6f, 3.40f);

        // --- 5. PUBLIC TRANSIT & BUSES (41-50) ---
        reg("LONDON_ROUTEMASTER", "London AEC Routemaster Double-Decker", "UK", VehicleCategory.PUBLIC_TRANSIT, Material.RED_CONCRETE, Material.WHITE_CONCRETE, Material.GLASS, 0.35, 1400, 0.16, 6, false, false, false, false, 9.1f, 2.5f, 4.40f);
        reg("CITY_TRANSIT_BUS", "Mercedes-Benz Citaro Articulated Bus", "Germany", VehicleCategory.PUBLIC_TRANSIT, Material.LIGHT_BLUE_CONCRETE, Material.WHITE_CONCRETE, Material.GLASS, 0.38, 1600, 0.18, 6, false, false, false, false, 12.0f, 2.5f, 3.20f);
        reg("AMERICAN_SCHOOL_BUS", "Blue Bird Yellow School Bus", "USA", VehicleCategory.PUBLIC_TRANSIT, Material.YELLOW_CONCRETE, Material.BLACK_CONCRETE, Material.GLASS, 0.40, 1300, 0.15, 5, false, false, false, false, 10.5f, 2.5f, 3.30f);
        reg("AUTO_RICKSHAW", "Bajaj RE Auto Rickshaw (Tuk Tuk)", "India", VehicleCategory.PUBLIC_TRANSIT, Material.GREEN_CONCRETE, Material.YELLOW_CONCRETE, Material.GLASS, 0.42, 350, 0.05, 3, false, false, false, false, 2.6f, 1.4f, 1.80f);
        reg("BANGKOK_TUK_TUK", "Bangkok Tourist Tuk Tuk", "Thailand", VehicleCategory.PUBLIC_TRANSIT, Material.BLUE_CONCRETE, Material.RED_CONCRETE, Material.GLASS, 0.44, 360, 0.05, 3, false, false, false, false, 2.7f, 1.4f, 1.82f);
        reg("NYC_YELLOW_CAB", "NYC Ford Crown Victoria Taxi", "USA", VehicleCategory.PUBLIC_TRANSIT, Material.YELLOW_CONCRETE, Material.BLACK_CONCRETE, Material.GLASS, 0.55, 650, 0.12, 4, false, false, false, false, 5.4f, 1.9f, 1.50f);
        reg("VENICE_WATER_TAXI", "Venetian Mahogany Water Limousine", "Italy", VehicleCategory.PUBLIC_TRANSIT, Material.SPRUCE_PLANKS, Material.WHITE_CONCRETE, Material.GLASS, 0.48, 800, 0.14, 4, false, false, false, false, 9.2f, 2.3f, 1.80f);
        reg("SAN_FRAN_CABLE_CAR", "San Francisco Historic Cable Car", "USA", VehicleCategory.PUBLIC_TRANSIT, Material.OAK_PLANKS, Material.RED_CONCRETE, Material.GLASS, 0.32, 1000, 0.10, 6, false, false, false, true, 8.5f, 2.4f, 3.20f);
        reg("GREYHOUND_COACH", "MCI J4500 Intercity Luxury Coach", "USA", VehicleCategory.PUBLIC_TRANSIT, Material.BLUE_CONCRETE, Material.IRON_BLOCK, Material.TINTED_GLASS, 0.48, 1600, 0.18, 6, false, false, false, false, 13.5f, 2.6f, 3.80f);
        reg("AIRPORT_APRON_BUS", "Cobus 3000 Airport Passenger Shuttle", "Germany", VehicleCategory.PUBLIC_TRANSIT, Material.WHITE_CONCRETE, Material.BLUE_CONCRETE, Material.TINTED_GLASS, 0.32, 1200, 0.12, 6, false, false, false, true, 14.0f, 3.0f, 3.10f);

        // --- 6. EMERGENCY & SERVICE (51-60) ---
        reg("POLICE_INTERCEPTOR", "Ford Police Interceptor Utility", "USA", VehicleCategory.EMERGENCY, Material.BLACK_CONCRETE, Material.WHITE_CONCRETE, Material.TINTED_GLASS, 0.72, 850, 0.15, 4, false, false, false, false, 5.1f, 2.0f, 1.80f);
        reg("HEAVY_FIRE_ENGINE", "Pierce Enforcer Pumper Fire Truck", "USA", VehicleCategory.EMERGENCY, Material.RED_CONCRETE, Material.WHITE_CONCRETE, Material.GLASS, 0.42, 1800, 0.24, 6, false, false, false, false, 9.8f, 2.6f, 3.40f);
        reg("LADDER_FIRE_TRUCK", "Rosenbauer 105ft Aerial Ladder Rig", "Austria", VehicleCategory.EMERGENCY, Material.RED_CONCRETE, Material.IRON_BLOCK, Material.GLASS, 0.38, 2000, 0.28, 6, false, false, false, false, 12.5f, 2.6f, 3.80f);
        reg("TYPE3_AMBULANCE", "Ford F-450 Super Duty ICU Ambulance", "USA", VehicleCategory.EMERGENCY, Material.WHITE_CONCRETE, Material.RED_CONCRETE, Material.TINTED_GLASS, 0.55, 1100, 0.16, 5, false, false, false, false, 6.8f, 2.4f, 2.80f);
        reg("AIRPORT_CRASH_TENDER", "Oshkosh Striker 8x8 ARFF Tender", "USA", VehicleCategory.EMERGENCY, Material.LIME_CONCRETE, Material.WHITE_CONCRETE, Material.TINTED_GLASS, 0.52, 2500, 0.35, 6, false, false, false, false, 12.0f, 3.2f, 3.90f);
        reg("SWAT_BEARCAT", "Lenco BearCat G3 Armored Tactical", "USA", VehicleCategory.EMERGENCY, Material.BLACK_CONCRETE, Material.OBSIDIAN, Material.TINTED_GLASS, 0.48, 1400, 0.20, 5, false, false, false, false, 6.3f, 2.4f, 2.70f);
        reg("COAST_GUARD_CUTTER", "45-Foot Response Boat-Medium (RB-M)", "USA", VehicleCategory.EMERGENCY, Material.IRON_BLOCK, Material.RED_CONCRETE, Material.TINTED_GLASS, 0.62, 1600, 0.22, 5, false, false, false, false, 13.5f, 4.2f, 3.50f);
        reg("HIGHWAY_SNOWPLOW", "Mack Granite Heavy Duty V-Snowplow", "USA", VehicleCategory.EMERGENCY, Material.ORANGE_CONCRETE, Material.YELLOW_CONCRETE, Material.GLASS, 0.38, 1600, 0.22, 6, false, false, false, false, 9.5f, 3.2f, 3.40f);
        reg("STREET_SWEEPER", "Elgin Pelican Three-Wheel Sweeper", "USA", VehicleCategory.EMERGENCY, Material.WHITE_CONCRETE, Material.YELLOW_CONCRETE, Material.GLASS, 0.30, 800, 0.10, 4, false, false, false, false, 4.8f, 2.2f, 2.60f);
        reg("RECOVERY_TOW_TRUCK", "Century 1150 Heavy Rotator Wrecker", "USA", VehicleCategory.EMERGENCY, Material.BLUE_CONCRETE, Material.YELLOW_CONCRETE, Material.GLASS, 0.38, 2000, 0.25, 6, false, false, false, false, 11.2f, 2.6f, 3.80f);

        // --- 7. CONSTRUCTION & MINING (61-70) ---
        reg("CAT_349_EXCAVATOR", "Caterpillar 349 Heavy Track Excavator", "USA", VehicleCategory.CONSTRUCTION, Material.YELLOW_CONCRETE, Material.BLACK_CONCRETE, Material.GLASS, 0.22, 2200, 0.30, 6, false, false, true, false, 11.5f, 3.6f, 3.80f);
        reg("CAT_D11_BULLDOZER", "Caterpillar D11 Heavy Track Dozer", "USA", VehicleCategory.CONSTRUCTION, Material.YELLOW_CONCRETE, Material.IRON_BLOCK, Material.GLASS, 0.24, 2400, 0.35, 6, false, false, true, false, 10.5f, 4.4f, 4.60f);
        reg("KOMATSU_HAUL_TRUCK", "Komatsu HD785-7 Mining Quarry Dumper", "Japan", VehicleCategory.CONSTRUCTION, Material.YELLOW_CONCRETE, Material.BLACK_CONCRETE, Material.GLASS, 0.32, 3500, 0.45, 6, false, false, false, false, 10.2f, 5.5f, 5.20f);
        reg("TUNNEL_BORING_RIG", "Herrenknecht Megadrill TBM Excavator", "Germany", VehicleCategory.CONSTRUCTION, Material.GRAY_CONCRETE, Material.DIAMOND_BLOCK, Material.GLASS, 0.18, 4000, 0.50, 6, false, false, true, false, 14.0f, 4.0f, 4.00f);
        reg("LIEBHERR_MOBILE_CRANE", "Liebherr LTM 11200 All-Terrain Crane", "Germany", VehicleCategory.CONSTRUCTION, Material.YELLOW_CONCRETE, Material.GRAY_CONCRETE, Material.GLASS, 0.28, 2800, 0.35, 6, false, false, false, false, 16.0f, 3.0f, 4.20f);
        reg("ASPHALT_ROAD_ROLLER", "Dynapac CC6200 Dual Drum Roller", "Sweden", VehicleCategory.CONSTRUCTION, Material.YELLOW_CONCRETE, Material.IRON_BLOCK, Material.GLASS, 0.25, 1200, 0.18, 4, false, false, false, false, 5.0f, 2.4f, 3.00f);
        reg("CONCRETE_MIXER_TRUCK", "Schwing Stetter Heavy Mixer Truck", "Germany", VehicleCategory.CONSTRUCTION, Material.WHITE_CONCRETE, Material.BLUE_CONCRETE, Material.GLASS, 0.36, 1600, 0.22, 6, false, false, false, false, 8.8f, 2.5f, 3.80f);
        reg("JCB_3CX_BACKHOE", "JCB 3CX Eco Backhoe Loader", "UK", VehicleCategory.CONSTRUCTION, Material.YELLOW_CONCRETE, Material.BLACK_CONCRETE, Material.GLASS, 0.32, 1100, 0.16, 5, false, false, true, false, 5.9f, 2.3f, 3.60f);
        reg("BOBCAT_SKID_STEER", "Bobcat S76 Compact Track Loader", "USA", VehicleCategory.CONSTRUCTION, Material.WHITE_CONCRETE, Material.ORANGE_CONCRETE, Material.GLASS, 0.35, 800, 0.12, 4, false, false, true, false, 3.7f, 1.8f, 2.10f);
        reg("UNDERGROUND_CORE_DRILL", "Sandvik DD422i Underground Jumbo Drill", "Sweden", VehicleCategory.CONSTRUCTION, Material.ORANGE_CONCRETE, Material.NETHERITE_BLOCK, Material.TINTED_GLASS, 0.20, 3000, 0.40, 6, false, false, true, false, 12.8f, 2.8f, 3.20f);

        // --- 8. AGRICULTURAL & INDUSTRIAL (71-80) ---
        reg("JOHN_DEERE_9RX", "John Deere 9RX 640 Quad-Track Tractor", "USA", VehicleCategory.AGRICULTURE, Material.LIME_CONCRETE, Material.YELLOW_CONCRETE, Material.GLASS, 0.34, 2200, 0.28, 6, false, false, false, false, 8.2f, 3.4f, 4.00f);
        reg("CASE_COMBINE_HARVESTER", "Case IH Axial-Flow 9250 Harvester", "USA", VehicleCategory.AGRICULTURE, Material.RED_CONCRETE, Material.BLACK_CONCRETE, Material.GLASS, 0.28, 2500, 0.32, 6, false, false, false, false, 10.5f, 4.8f, 4.40f);
        reg("CLAAS_JAGUAR_FORAGER", "Claas Jaguar 990 Forage Harvester", "Germany", VehicleCategory.AGRICULTURE, Material.LIME_CONCRETE, Material.WHITE_CONCRETE, Material.GLASS, 0.30, 2400, 0.30, 6, false, false, false, false, 9.8f, 3.8f, 4.10f);
        reg("HORSCH_CROP_SPRAYER", "Horsch Leeb PT 350 Self-Propelled Sprayer", "Germany", VehicleCategory.AGRICULTURE, Material.RED_CONCRETE, Material.GRAY_CONCRETE, Material.GLASS, 0.42, 1600, 0.20, 6, false, false, false, false, 9.2f, 4.2f, 3.90f);
        reg("PONSE_ELEPHANT_FORWARDER", "Ponsse Elephant King Forestry Forwarder", "Finland", VehicleCategory.AGRICULTURE, Material.YELLOW_CONCRETE, Material.BLACK_CONCRETE, Material.GLASS, 0.26, 2600, 0.34, 6, false, false, true, false, 10.8f, 3.1f, 3.90f);
        reg("TOYOTA_3T_FORKLIFT", "Toyota 8-Series 3-Ton Industrial Forklift", "Japan", VehicleCategory.AGRICULTURE, Material.ORANGE_CONCRETE, Material.BLACK_CONCRETE, Material.GLASS, 0.32, 650, 0.08, 3, false, false, false, false, 3.6f, 1.4f, 2.20f);
        reg("KALMAR_REACHSTACKER", "Kalmar Gloria Container Port Reachstacker", "Sweden", VehicleCategory.AGRICULTURE, Material.RED_CONCRETE, Material.GRAY_CONCRETE, Material.GLASS, 0.25, 3000, 0.38, 6, false, false, false, false, 12.0f, 4.2f, 4.80f);
        reg("NEW_HOLLAND_T9", "New Holland T9.700 SmartTrax Tractor", "USA", VehicleCategory.AGRICULTURE, Material.BLUE_CONCRETE, Material.YELLOW_CONCRETE, Material.GLASS, 0.35, 2300, 0.29, 6, false, false, false, false, 8.4f, 3.5f, 4.10f);
        reg("MAHINDRA_YUVO", "Mahindra Yuvo Tech+ 585 Tractor", "India", VehicleCategory.AGRICULTURE, Material.RED_CONCRETE, Material.BLACK_CONCRETE, Material.GLASS, 0.32, 900, 0.12, 4, false, false, false, false, 3.8f, 1.9f, 2.40f);
        reg("FENDT_1050_VARIO", "Fendt 1050 Vario Master Tractor", "Germany", VehicleCategory.AGRICULTURE, Material.GREEN_CONCRETE, Material.RED_CONCRETE, Material.GLASS, 0.36, 2200, 0.26, 6, false, false, false, false, 7.0f, 2.8f, 3.60f);

        // --- 9. MILITARY & ARMORED VEHICLES (81-90) ---
        reg("M1A2_ABRAMS", "M1A2 SEPv3 Abrams Main Battle Tank", "USA", VehicleCategory.MILITARY, Material.TERRACOTTA, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.42, 3000, 0.40, 6, false, false, true, false, 9.8f, 3.7f, 2.60f);
        reg("LEOPARD_2A7", "Leopard 2A7+ Heavy Combat Tank", "Germany", VehicleCategory.MILITARY, Material.GREEN_TERRACOTTA, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.44, 2900, 0.38, 6, false, false, true, false, 9.9f, 3.8f, 2.80f);
        reg("T90_BHISHMA", "T-90MS Bhishma Main Battle Tank", "India/Russia", VehicleCategory.MILITARY, Material.BROWN_TERRACOTTA, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.40, 2800, 0.36, 6, false, false, true, false, 9.5f, 3.6f, 2.40f);
        reg("M142_HIMARS", "M142 HIMARS Mobile Rocket Artillery", "USA", VehicleCategory.MILITARY, Material.GREEN_TERRACOTTA, Material.GRAY_CONCRETE, Material.TINTED_GLASS, 0.52, 2200, 0.30, 6, false, false, false, false, 7.2f, 2.5f, 3.20f);
        reg("HUMVEE_M1151", "HMMWV Humvee Heavy Armored Weapon Carrier", "USA", VehicleCategory.MILITARY, Material.TERRACOTTA, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.56, 1200, 0.18, 5, false, false, false, false, 4.9f, 2.2f, 2.00f);
        reg("BTR_82A", "BTR-82A 8x8 Amphibious Armored Carrier", "Russia", VehicleCategory.MILITARY, Material.GREEN_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.50, 2000, 0.28, 6, false, false, false, false, 7.8f, 2.9f, 2.80f);
        reg("STRYKER_ICV", "M1126 Stryker Combat Infantry Vehicle", "USA", VehicleCategory.MILITARY, Material.GRAY_TERRACOTTA, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.58, 2100, 0.28, 6, false, false, false, false, 7.0f, 2.8f, 2.70f);
        reg("CHALLENGER_2", "Challenger 2 TES Megatron Tank", "UK", VehicleCategory.MILITARY, Material.SANDSTONE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.38, 3000, 0.40, 6, false, false, true, false, 11.5f, 3.8f, 2.50f);
        reg("K2_BLACK_PANTHER", "K2 Black Panther Main Battle Tank", "South Korea", VehicleCategory.MILITARY, Material.GREEN_TERRACOTTA, Material.BROWN_CONCRETE, Material.TINTED_GLASS, 0.45, 2900, 0.38, 6, false, false, true, false, 10.8f, 3.6f, 2.40f);
        reg("PL_01_STEALTH_TANK", "PL-01 Concept Stealth Battle Tank", "Poland", VehicleCategory.MILITARY, Material.BLACK_CONCRETE, Material.GRAY_CONCRETE, Material.TINTED_GLASS, 0.48, 2800, 0.35, 6, false, false, true, false, 7.0f, 3.8f, 2.80f);

        // --- 10. ROTORCRAFT & HELICOPTERS (91-95) ---
        reg("AH64_APACHE", "Boeing AH-64E Apache Guardian Gunship", "USA", VehicleCategory.ROTORCRAFT, Material.GRAY_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.72, 1800, 0.26, 4, true, true, false, false, 14.5f, 4.8f, 4.10f);
        reg("UH60_BLACKHAWK", "Sikorsky UH-60M Black Hawk Tactical", "USA", VehicleCategory.ROTORCRAFT, Material.BLACK_CONCRETE, Material.GRAY_CONCRETE, Material.TINTED_GLASS, 0.70, 2000, 0.28, 6, true, true, false, false, 16.0f, 4.2f, 4.30f);
        reg("CH47_CHINOOK", "Boeing CH-47F Chinook Heavy Tandem Rotor", "USA", VehicleCategory.ROTORCRAFT, Material.GREEN_TERRACOTTA, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.65, 3200, 0.38, 6, true, true, false, false, 24.0f, 4.8f, 5.70f);
        reg("BELL_206_JETRANGER", "Bell 206L LongRanger Luxury Helicopter", "USA", VehicleCategory.ROTORCRAFT, Material.WHITE_CONCRETE, Material.BLUE_CONCRETE, Material.GLASS, 0.62, 1200, 0.18, 4, true, true, false, false, 11.8f, 2.4f, 3.20f);
        reg("MIL_MI24_HIND", "Mil Mi-24P Hind Attack Helicopter", "Russia", VehicleCategory.ROTORCRAFT, Material.BROWN_TERRACOTTA, Material.GREEN_CONCRETE, Material.TINTED_GLASS, 0.75, 2400, 0.32, 6, true, true, false, false, 17.5f, 6.5f, 4.50f);

        // --- 11. FIXED-WING AIRCRAFT & SUPERSONIC JETS (96-100) ---
        reg("CESSNA_172", "Cessna 172 Skyhawk General Aviation", "USA", VehicleCategory.FIXED_WING, Material.WHITE_CONCRETE, Material.BLUE_CONCRETE, Material.GLASS, 0.65, 1100, 0.15, 3, true, false, false, false, 8.2f, 11.0f, 2.70f);
        reg("SUPERMARINE_SPITFIRE", "Supermarine Spitfire Mk.IX WWII Fighter", "UK", VehicleCategory.FIXED_WING, Material.GREEN_TERRACOTTA, Material.BROWN_TERRACOTTA, Material.GLASS, 0.78, 1400, 0.22, 3, true, false, false, false, 9.1f, 11.2f, 3.80f);
        reg("F22_RAPTOR", "Lockheed Martin F-22A Raptor Stealth Jet", "USA", VehicleCategory.FIXED_WING, Material.GRAY_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 1.25, 2600, 0.38, 4, true, false, false, false, 18.9f, 13.5f, 5.08f);
        reg("CONCORDE_SUPERSONIC", "Aérospatiale/BAC Concorde Supersonic", "UK/France", VehicleCategory.FIXED_WING, Material.WHITE_CONCRETE, Material.IRON_BLOCK, Material.TINTED_GLASS, 1.45, 4500, 0.55, 6, true, false, false, false, 32.0f, 14.0f, 6.20f);
        reg("AN225_MRIYA", "Antonov An-225 Mriya Super Cargo Plane", "Ukraine", VehicleCategory.FIXED_WING, Material.WHITE_CONCRETE, Material.BLUE_CONCRETE, Material.GLASS, 0.82, 6000, 0.65, 6, true, false, false, false, 42.0f, 44.0f, 9.50f);
    }

    private static void reg(String id, String name, String origin, VehicleCategory category,
                            Material primaryMaterial, Material secondaryMaterial, Material glassMaterial,
                            double speed, double maxFuel, double fuelBurnRate, int storageRows,
                            boolean flying, boolean canHover, boolean canDrill, boolean hasSolar,
                            float length, float width, float height) {
        VEHICLES.put(id, new VehicleSpec(id, name, origin, category, primaryMaterial, secondaryMaterial, glassMaterial,
                speed, maxFuel, fuelBurnRate, storageRows, flying, canHover, canDrill, hasSolar, length, width, height));
    }

    public static VehicleSpec get(String id) { return VEHICLES.get(id); }
    public static Collection<VehicleSpec> getAll() { return VEHICLES.values(); }
    public static List<VehicleSpec> getByCategory(VehicleCategory category) {
        List<VehicleSpec> list = new ArrayList<>();
        for (VehicleSpec spec : VEHICLES.values()) {
            if (spec.getCategory() == category) list.add(spec);
        }
        return list;
    }
}
INNER_EOF

# 8. Create CustomItemRegistry.java (Comprehensive Crafting Engine)
cat << 'INNER_EOF' > src/main/java/com/blockvehicles/items/CustomItemRegistry.java
package com.blockvehicles.items;

import com.blockvehicles.BlockVehiclesPlugin;
import com.blockvehicles.registry.VehicleRegistry;
import com.blockvehicles.registry.VehicleSpec;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class CustomItemRegistry {
    private final BlockVehiclesPlugin plugin;
    public final NamespacedKey ITEM_ID_KEY;
    public final NamespacedKey VEHICLE_SPEC_KEY;

    private final Map<String, ItemStack> customItems = new HashMap<>();

    public CustomItemRegistry(BlockVehiclesPlugin plugin) {
        this.plugin = plugin;
        this.ITEM_ID_KEY = new NamespacedKey(plugin, "custom_item_id");
        this.VEHICLE_SPEC_KEY = new NamespacedKey(plugin, "vehicle_spec_id");
        initCustomItems();
    }

    private void initCustomItems() {
        // Raw & Intermediate Parts
        regItem("STEEL_INGOT", Material.IRON_INGOT, "Industrial Steel Ingot", ChatColor.WHITE, "Reinforced structural alloy");
        regItem("RUBBER_SHEET", Material.DRIED_KELP, "Vulcanized Rubber Sheet", ChatColor.DARK_GRAY, "Used for tires and seals");
        regItem("COPPER_WIRING", Material.STRING, "Insulated Copper Wiring", ChatColor.GOLD, "Electrical wiring harness");
        regItem("REINFORCED_GLASS", Material.TINTED_GLASS, "Ballistic Reinforced Glass", ChatColor.AQUA, "Impact and pressure resistant");
        regItem("CARBON_FIBER", Material.NETHERITE_SCRAP, "Carbon Fiber Composite", ChatColor.DARK_AQUA, "Ultra-lightweight high-strength body panel");
        
        // Mechanical & Drive Units
        regItem("TIRE_ALL_TERRAIN", Material.COAL_BLOCK, "Heavy All-Terrain Tire", ChatColor.DARK_GRAY, "High-grip durable wheel");
        regItem("ENGINE_V6", Material.PISTON, "V6 Combustion Engine Block", ChatColor.GOLD, "Standard automotive powertrain");
        regItem("ENGINE_V8_TURBO", Material.BLAST_FURNACE, "Twin-Turbo V8 Engine", ChatColor.RED, "High performance supercar powertrain");
        regItem("ELECTRIC_MOTOR", Material.COPPER_BLOCK, "High-Torque Electric Motor", ChatColor.AQUA, "Instant acceleration electric drive");
        regItem("TURBOFAN_JET_ENGINE", Material.DISPENSER, "High-Bypass Turbofan Jet Engine", ChatColor.DARK_AQUA, "Aviation propulsion unit");
        regItem("HELICOPTER_ROTOR", Material.IRON_BARS, "Turboshaft Rotor Assembly", ChatColor.LIGHT_PURPLE, "Rotary wing lift mechanism");
        regItem("TRANSMISSION_GEARBOX", Material.HOPPER, "Heavy Duty Gearbox", ChatColor.GRAY, "Transfers mechanical torque");

        // Electronic & Energy
        regItem("LITHIUM_BATTERY", Material.COPPER_BLOCK, "Lithium-Ion Battery Cell", ChatColor.GREEN, "Compact energy storage (+450 Fuel)");
        regItem("HIGH_CAPACITY_BATTERY", Material.GOLD_BLOCK, "High-Density Energy Core", ChatColor.DARK_GREEN, "Industrial power unit (+1200 Fuel)");
        regItem("SOLAR_PANEL", Material.DAYLIGHT_DETECTOR, "Photovoltaic Solar Array", ChatColor.YELLOW, "Recharges vehicle under open sunlight");
        regItem("AVIONICS_COMPUTER", Material.RECOVERY_COMPASS, "Avionics Flight Computer", ChatColor.DARK_AQUA, "Navigation, pitch & roll stabilization");

        // Specialized Heavy Tools
        regItem("DIAMOND_DRILL_HEAD", Material.HEAVY_CORE != null ? Material.ANVIL : Material.ANVIL, "Industrial Diamond Drill Head", ChatColor.AQUA, "Excavates rock and minerals in mining mode");
        regItem("ARMOR_PLATING", Material.NETHERITE_INGOT, "Heavy Composite Armor Plating", ChatColor.DARK_GRAY, "Protective plating for military vehicles");
        regItem("HYDRAULIC_CYLINDER", Material.STICKY_PISTON, "Heavy Hydraulic Cylinder", ChatColor.YELLOW, "Powers loaders, plows and suspension");
        regItem("AIRCRAFT_WING", Material.FEATHER, "Aerodynamic Aircraft Wing Rib", ChatColor.WHITE, "Generates high speed lift");
    }

    private void regItem(String id, Material mat, String name, ChatColor color, String lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(color + "" + ChatColor.BOLD + name);
            meta.setLore(Arrays.asList(ChatColor.GRAY + lore, ChatColor.DARK_GRAY + "ID: " + id));
            meta.getPersistentDataContainer().set(ITEM_ID_KEY, PersistentDataType.STRING, id);
            item.setItemMeta(meta);
        }
        customItems.put(id, item);
    }

    public ItemStack getItem(String id) {
        ItemStack item = customItems.get(id);
        return item != null ? item.clone() : null;
    }

    public ItemStack createVehicleKey(VehicleSpec spec) {
        ItemStack key = new ItemStack(spec.getCategory().getIcon());
        ItemMeta meta = key.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(spec.getCategory().getColor() + "" + ChatColor.BOLD + spec.getName() + " Key");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.YELLOW + "Origin: " + ChatColor.WHITE + spec.getOrigin());
            lore.add(ChatColor.YELLOW + "Category: " + ChatColor.WHITE + spec.getCategory().getTitle());
            lore.add(ChatColor.GRAY + "------------------------");
            lore.add(ChatColor.AQUA + "Max Speed: " + ChatColor.WHITE + String.format("%.1f", spec.getSpeed() * 100) + " km/h");
            lore.add(ChatColor.AQUA + "Fuel Capacity: " + ChatColor.WHITE + spec.getMaxFuel() + " units");
            lore.add(ChatColor.AQUA + "Trunk Storage: " + ChatColor.WHITE + (spec.getStorageRows() * 9) + " Slots");
            if (spec.isFlying()) lore.add(ChatColor.LIGHT_PURPLE + "✦ Flight Capable " + (spec.canHover() ? "(VTOL / Hover)" : "(Fixed Wing)"));
            if (spec.canDrill()) lore.add(ChatColor.GOLD + "✦ Excavation Mining Rig Equipped");
            if (spec.hasSolar()) lore.add(ChatColor.GREEN + "✦ Solar Eco-Recharging Array");
            lore.add(ChatColor.GRAY + "------------------------");
            lore.add(ChatColor.GREEN + "Right-Click on ground to deploy vehicle!");
            meta.setLore(lore);
            meta.getPersistentDataContainer().set(VEHICLE_SPEC_KEY, PersistentDataType.STRING, spec.getId());
            key.setItemMeta(meta);
        }
        return key;
    }

    public void registerAllRecipes() {
        // 1. Steel Ingot (Iron Ingot + Coal + Iron Ingot)
        ShapelessRecipe steel = new ShapelessRecipe(new NamespacedKey(plugin, "recipe_steel"), getItem("STEEL_INGOT"));
        steel.addIngredient(Material.IRON_INGOT);
        steel.addIngredient(Material.COAL);
        steel.addIngredient(Material.IRON_INGOT);
        plugin.getServer().addRecipe(steel);

        // 2. Rubber Sheet (Dried Kelp + Slimeball)
        ShapelessRecipe rubber = new ShapelessRecipe(new NamespacedKey(plugin, "recipe_rubber"), getItem("RUBBER_SHEET"));
        rubber.addIngredient(Material.DRIED_KELP);
        rubber.addIngredient(Material.SLIME_BALL);
        plugin.getServer().addRecipe(rubber);

        // 3. Copper Wire
        ShapelessRecipe wire = new ShapelessRecipe(new NamespacedKey(plugin, "recipe_wire"), getItem("COPPER_WIRING"));
        wire.addIngredient(Material.COPPER_INGOT);
        wire.addIngredient(Material.STRING);
        plugin.getServer().addRecipe(wire);

        // 4. Tire
        ShapedRecipe tire = new ShapedRecipe(new NamespacedKey(plugin, "recipe_tire"), getItem("TIRE_ALL_TERRAIN"));
        tire.shape(" R ", "RCR", " R ");
        tire.setIngredient('R', Material.DRIED_KELP);
        tire.setIngredient('C', Material.COAL_BLOCK);
        plugin.getServer().addRecipe(tire);

        // 5. Engine V6
        ShapedRecipe engineV6 = new ShapedRecipe(new NamespacedKey(plugin, "recipe_engine_v6"), getItem("ENGINE_V6"));
        engineV6.shape("III", "PRP", "III");
        engineV6.setIngredient('I', Material.IRON_INGOT);
        engineV6.setIngredient('P', Material.PISTON);
        engineV6.setIngredient('R', Material.REDSTONE_BLOCK);
        plugin.getServer().addRecipe(engineV6);

        // 6. Engine V8 Turbo
        ShapedRecipe engineV8 = new ShapedRecipe(new NamespacedKey(plugin, "recipe_engine_v8"), getItem("ENGINE_V8_TURBO"));
        engineV8.shape("BIB", "IEI", "BIB");
        engineV8.setIngredient('B', Material.BLAZE_POWDER);
        engineV8.setIngredient('I', Material.IRON_BLOCK);
        engineV8.setIngredient('E', Material.PISTON);
        plugin.getServer().addRecipe(engineV8);

        // 7. Battery
        ShapedRecipe battery = new ShapedRecipe(new NamespacedKey(plugin, "recipe_battery"), getItem("LITHIUM_BATTERY"));
        battery.shape(" C ", "RGR", " C ");
        battery.setIngredient('C', Material.COPPER_INGOT);
        battery.setIngredient('R', Material.REDSTONE);
        battery.setIngredient('G', Material.GOLD_INGOT);
        plugin.getServer().addRecipe(battery);

        // 8. Solar Panel
        ShapedRecipe solar = new ShapedRecipe(new NamespacedKey(plugin, "recipe_solar"), getItem("SOLAR_PANEL"));
        solar.shape("GGG", "QQQ", "RRR");
        solar.setIngredient('G', Material.GLASS);
        solar.setIngredient('Q', Material.QUARTZ);
        solar.setIngredient('R', Material.REDSTONE);
        plugin.getServer().addRecipe(solar);

        // 9. Diamond Drill
        ShapedRecipe drill = new ShapedRecipe(new NamespacedKey(plugin, "recipe_drill_head"), getItem("DIAMOND_DRILL_HEAD"));
        drill.shape(" D ", "DID", " I ");
        drill.setIngredient('D', Material.DIAMOND_BLOCK);
        drill.setIngredient('I', Material.IRON_BLOCK);
        plugin.getServer().addRecipe(drill);

        // 10. Turbofan Jet Engine
        ShapedRecipe jet = new ShapedRecipe(new NamespacedKey(plugin, "recipe_turbofan"), getItem("TURBOFAN_JET_ENGINE"));
        jet.shape("IBI", "BRB", "IBI");
        jet.setIngredient('I', Material.IRON_BLOCK);
        jet.setIngredient('B', Material.BLAZE_ROD);
        jet.setIngredient('R', Material.REDSTONE_BLOCK);
        plugin.getServer().addRecipe(jet);

        // Register default craftable keys for top vehicles
        registerVehicleRecipe("BUGATTI_CHIRON", "RCR", "E E", "W W", Material.BLUE_CONCRETE, Material.CHEST, Material.BLAST_FURNACE, Material.COAL_BLOCK);
        registerVehicleRecipe("TOYOTA_COROLLA", "IGI", "ECE", "W W", Material.WHITE_CONCRETE, Material.GLASS, Material.PISTON, Material.COAL_BLOCK);
        registerVehicleRecipe("CAT_349_EXCAVATOR", "YDY", "ECE", "WWW", Material.YELLOW_CONCRETE, Material.DIAMOND_BLOCK, Material.REDSTONE_BLOCK, Material.COAL_BLOCK);
        registerVehicleRecipe("AH64_APACHE", " R ", "GCG", "E E", Material.IRON_BARS, Material.GRAY_CONCRETE, Material.GLASS, Material.BLAST_FURNACE);
        registerVehicleRecipe("F22_RAPTOR", " F ", "GAG", "J J", Material.FEATHER, Material.GRAY_CONCRETE, Material.DISPENSER, Material.BLAZE_ROD);
    }

    private void registerVehicleRecipe(String id, String s1, String s2, String s3, Material m1, Material m2, Material m3, Material m4) {
        VehicleSpec spec = VehicleRegistry.get(id);
        if (spec == null) return;
        ItemStack key = createVehicleKey(spec);
        NamespacedKey rKey = new NamespacedKey(plugin, "craft_" + id.toLowerCase());
        ShapedRecipe recipe = new ShapedRecipe(rKey, key);
        recipe.shape(s1, s2, s3);
        recipe.setIngredient(s1.charAt(0) != ' ' ? s1.charAt(0) : s1.charAt(1), m1);
        recipe.setIngredient(s2.charAt(1), m2);
        recipe.setIngredient(s2.charAt(0) != ' ' ? s2.charAt(0) : s2.charAt(2), m3);
        recipe.setIngredient(s3.charAt(0) != ' ' ? s3.charAt(0) : s3.charAt(1), m4);
        try {
            plugin.getServer().addRecipe(recipe);
        } catch (Exception ignored) {}
    }
}
INNER_EOF

# 9. Create ProceduralVehicleModelBuilder.java (Real Human-Scale 3D Geometry)
cat << 'INNER_EOF' > src/main/java/com/blockvehicles/models/ProceduralVehicleModelBuilder.java
package com.blockvehicles.models;

import com.blockvehicles.registry.VehicleSpec;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class ProceduralVehicleModelBuilder {

    public static List<BlockDisplay> buildModel(World world, Location baseLoc, VehicleSpec spec) {
        List<BlockDisplay> parts = new ArrayList<>();

        float l = spec.getLength();
        float w = spec.getWidth();
        float h = spec.getHeight();
        Material primary = spec.getPrimaryMaterial();
        Material secondary = spec.getSecondaryMaterial();
        Material glass = spec.getGlassMaterial();

        switch (spec.getCategory()) {
            case SUPERCAR, CIVILIAN_CAR -> {
                // Main Lower Body Chassis
                parts.add(createPart(world, baseLoc, primary, new Vector3f(-w / 2f, 0.25f, -l / 2f), new Vector3f(w, h * 0.45f, l)));
                // Sleek Cockpit / Glass Canopy
                parts.add(createPart(world, baseLoc, glass, new Vector3f(-w * 0.4f, 0.25f + h * 0.45f, -l * 0.2f), new Vector3f(w * 0.8f, h * 0.45f, l * 0.5f)));
                // Roof Cap
                parts.add(createPart(world, baseLoc, secondary, new Vector3f(-w * 0.4f, 0.25f + h * 0.90f, -l * 0.15f), new Vector3f(w * 0.8f, 0.1f, l * 0.4f)));
                // 4 Real Proportion Wheels
                parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(-w / 2f - 0.15f, 0.0f, l * 0.22f), new Vector3f(0.35f, 0.65f, 0.65f)));
                parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(w / 2f - 0.20f, 0.0f, l * 0.22f), new Vector3f(0.35f, 0.65f, 0.65f)));
                parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(-w / 2f - 0.15f, 0.0f, -l * 0.30f), new Vector3f(0.35f, 0.65f, 0.65f)));
                parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(w / 2f - 0.20f, 0.0f, -l * 0.30f), new Vector3f(0.35f, 0.65f, 0.65f)));
                // High-beam Headlights
                parts.add(createPart(world, baseLoc, Material.SEA_LANTERN, new Vector3f(-w * 0.42f, 0.35f, l / 2f - 0.05f), new Vector3f(0.3f, 0.25f, 0.1f)));
                parts.add(createPart(world, baseLoc, Material.SEA_LANTERN, new Vector3f(w * 0.42f - 0.3f, 0.35f, l / 2f - 0.05f), new Vector3f(0.3f, 0.25f, 0.1f)));
            }
            case TRUCK_SUV, SEMI_HEAVY -> {
                // Heavy Frame Base
                parts.add(createPart(world, baseLoc, secondary, new Vector3f(-w / 2f, 0.35f, -l / 2f), new Vector3f(w, h * 0.35f, l)));
                // Elevated Driver Cabin
                parts.add(createPart(world, baseLoc, primary, new Vector3f(-w / 2f, 0.35f + h * 0.35f, l * 0.05f), new Vector3f(w, h * 0.60f, l * 0.45f)));
                // Panoramic Windshield
                parts.add(createPart(world, baseLoc, glass, new Vector3f(-w * 0.45f, 0.35f + h * 0.50f, l * 0.15f), new Vector3f(w * 0.9f, h * 0.42f, l * 0.32f)));
                // Rear Cargo Bed or Semi Trailer Hitch
                parts.add(createPart(world, baseLoc, Material.IRON_BLOCK, new Vector3f(-w * 0.48f, 0.45f, -l * 0.48f), new Vector3f(w * 0.96f, h * 0.50f, l * 0.50f)));
                // Heavy Duty Wheels (6 or 8 wheels)
                for (float zOffset : new float[]{l * 0.25f, -l * 0.15f, -l * 0.38f}) {
                    parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(-w / 2f - 0.2f, 0.0f, zOffset), new Vector3f(0.45f, 0.85f, 0.85f)));
                    parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(w / 2f - 0.25f, 0.0f, zOffset), new Vector3f(0.45f, 0.85f, 0.85f)));
                }
            }
            case PUBLIC_TRANSIT, EMERGENCY -> {
                // Long Passenger / Utility Body
                parts.add(createPart(world, baseLoc, primary, new Vector3f(-w / 2f, 0.35f, -l / 2f), new Vector3f(w, h * 0.85f, l)));
                // Continuous Window Band
                parts.add(createPart(world, baseLoc, glass, new Vector3f(-w / 2f - 0.02f, h * 0.45f, -l * 0.45f), new Vector3f(w + 0.04f, h * 0.35f, l * 0.90f)));
                // Emergency Beacon / Roof Rails
                parts.add(createPart(world, baseLoc, Material.REDSTONE_BLOCK, new Vector3f(-0.3f, h + 0.35f, l * 0.3f), new Vector3f(0.6f, 0.25f, 0.6f)));
                // 4-6 Heavy Bus Wheels
                parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(-w / 2f - 0.15f, 0.0f, l * 0.32f), new Vector3f(0.4f, 0.8f, 0.8f)));
                parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(w / 2f - 0.25f, 0.0f, l * 0.32f), new Vector3f(0.4f, 0.8f, 0.8f)));
                parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(-w / 2f - 0.15f, 0.0f, -l * 0.35f), new Vector3f(0.4f, 0.8f, 0.8f)));
                parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(w / 2f - 0.25f, 0.0f, -l * 0.35f), new Vector3f(0.4f, 0.8f, 0.8f)));
            }
            case CONSTRUCTION, AGRICULTURE -> {
                // Armored Chassis
                parts.add(createPart(world, baseLoc, primary, new Vector3f(-w / 2f, 0.4f, -l * 0.35f), new Vector3f(w, h * 0.65f, l * 0.7f)));
                // High Cabin
                parts.add(createPart(world, baseLoc, glass, new Vector3f(-w * 0.35f, h * 0.7f, -l * 0.1f), new Vector3f(w * 0.7f, h * 0.5f, l * 0.4f)));
                // Continuous Heavy Tracks or Big Tractor Tires
                parts.add(createPart(world, baseLoc, Material.OBSIDIAN, new Vector3f(-w / 2f - 0.3f, 0.0f, -l / 2f), new Vector3f(0.5f, 0.9f, l)));
                parts.add(createPart(world, baseLoc, Material.OBSIDIAN, new Vector3f(w / 2f - 0.2f, 0.0f, -l / 2f), new Vector3f(0.5f, 0.9f, l)));
                // Front Excavator Boom or Drill Head
                if (spec.canDrill()) {
                    parts.add(createPart(world, baseLoc, Material.DIAMOND_BLOCK, new Vector3f(-0.6f, 0.3f, l / 2f), new Vector3f(1.2f, 1.2f, 1.4f)));
                    parts.add(createPart(world, baseLoc, Material.ANVIL, new Vector3f(-0.4f, 0.4f, l / 2f + 1.4f), new Vector3f(0.8f, 0.8f, 0.8f)));
                }
            }
            case MILITARY -> {
                // Low Profile Armored Hull
                parts.add(createPart(world, baseLoc, primary, new Vector3f(-w / 2f, 0.4f, -l / 2f), new Vector3f(w, h * 0.45f, l)));
                // 360° Rotating Turret
                parts.add(createPart(world, baseLoc, secondary, new Vector3f(-w * 0.35f, 0.4f + h * 0.45f, -l * 0.15f), new Vector3f(w * 0.7f, h * 0.45f, l * 0.5f)));
                // Main Cannon Barrel
                parts.add(createPart(world, baseLoc, Material.IRON_BLOCK, new Vector3f(-0.15f, 0.4f + h * 0.65f, l * 0.35f), new Vector3f(0.3f, 0.3f, l * 0.65f)));
                // Heavy Armored Tracks & Skirts
                parts.add(createPart(world, baseLoc, Material.NETHERITE_BLOCK, new Vector3f(-w / 2f - 0.25f, 0.0f, -l / 2f), new Vector3f(0.45f, 0.85f, l)));
                parts.add(createPart(world, baseLoc, Material.NETHERITE_BLOCK, new Vector3f(w / 2f - 0.20f, 0.0f, -l / 2f), new Vector3f(0.45f, 0.85f, l)));
            }
            case ROTORCRAFT -> {
                // Streamlined Helicopter Pod
                parts.add(createPart(world, baseLoc, primary, new Vector3f(-w / 2f, 0.4f, -l * 0.25f), new Vector3f(w, h * 0.7f, l * 0.65f)));
                // Bubble Cockpit Canopy
                parts.add(createPart(world, baseLoc, glass, new Vector3f(-w * 0.45f, 0.6f, l * 0.1f), new Vector3f(w * 0.9f, h * 0.6f, l * 0.3f)));
                // Tail Boom
                parts.add(createPart(world, baseLoc, secondary, new Vector3f(-0.25f, 0.8f, -l * 0.9f), new Vector3f(0.5f, 0.5f, l * 0.7f)));
                // Tail Rotor
                parts.add(createPart(world, baseLoc, Material.IRON_BARS, new Vector3f(0.28f, 0.6f, -l * 0.92f), new Vector3f(0.1f, 1.4f, 0.4f)));
                // Main Overhead Rotor Blades (Huge 8m span)
                parts.add(createPart(world, baseLoc, Material.IRON_BARS, new Vector3f(-w * 2.2f, h + 0.6f, -0.2f), new Vector3f(w * 4.4f, 0.1f, 0.4f)));
                // Landing Skids
                parts.add(createPart(world, baseLoc, Material.END_ROD, new Vector3f(-w * 0.45f, 0.0f, -l * 0.25f), new Vector3f(0.2f, 0.2f, l * 0.6f)));
                parts.add(createPart(world, baseLoc, Material.END_ROD, new Vector3f(w * 0.45f - 0.2f, 0.0f, -l * 0.25f), new Vector3f(0.2f, 0.2f, l * 0.6f)));
            }
            case FIXED_WING -> {
                // Aerodynamic Fuselage
                parts.add(createPart(world, baseLoc, primary, new Vector3f(-w * 0.3f, 0.4f, -l / 2f), new Vector3f(w * 0.6f, h * 0.6f, l)));
                // High Speed Wingspan (10-16 blocks wide)
                parts.add(createPart(world, baseLoc, secondary, new Vector3f(-w / 2f, 0.6f, -l * 0.1f), new Vector3f(w, 0.15f, l * 0.35f)));
                // Cockpit Windscreen
                parts.add(createPart(world, baseLoc, glass, new Vector3f(-w * 0.25f, 0.4f + h * 0.5f, l * 0.15f), new Vector3f(w * 0.5f, h * 0.4f, l * 0.25f)));
                // Vertical Stabilizer / Tail Fin
                parts.add(createPart(world, baseLoc, primary, new Vector3f(-0.1f, 0.4f + h * 0.6f, -l * 0.48f), new Vector3f(0.2f, h * 0.9f, l * 0.25f)));
                // Jet Engines / Propeller
                parts.add(createPart(world, baseLoc, Material.DISPENSER, new Vector3f(-w * 0.4f, 0.35f, -l * 0.15f), new Vector3f(0.6f, 0.6f, 1.2f)));
                parts.add(createPart(world, baseLoc, Material.DISPENSER, new Vector3f(w * 0.4f - 0.6f, 0.35f, -l * 0.15f), new Vector3f(0.6f, 0.6f, 1.2f)));
            }
        }

        return parts;
    }

    private static BlockDisplay createPart(World world, Location loc, Material mat, Vector3f offset, Vector3f scale) {
        BlockDisplay display = (BlockDisplay) world.spawnEntity(loc, EntityType.BLOCK_DISPLAY);
        display.setBlock(mat.createBlockData());
        display.setBillboard(Display.Billboard.FIXED);
        Transformation transform = new Transformation(offset, new AxisAngle4f(0, 0, 0, 1), scale, new AxisAngle4f(0, 0, 0, 1));
        display.setTransformation(transform);
        display.setInterpolationDuration(1);
        display.setInterpolationDelay(0);
        return display;
    }
}
INNER_EOF

# 10. Create VehicleInstance.java
cat << 'INNER_EOF' > src/main/java/com/blockvehicles/vehicle/VehicleInstance.java
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
INNER_EOF

# 11. Create VehicleManager.java
cat << 'INNER_EOF' > src/main/java/com/blockvehicles/vehicle/VehicleManager.java
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
INNER_EOF

# 12. Create VehicleCatalogGUI.java (Showroom Browser for 100 Vehicles)
cat << 'INNER_EOF' > src/main/java/com/blockvehicles/gui/VehicleCatalogGUI.java
package com.blockvehicles.gui;

import com.blockvehicles.BlockVehiclesPlugin;
import com.blockvehicles.registry.VehicleCategory;
import com.blockvehicles.registry.VehicleRegistry;
import com.blockvehicles.registry.VehicleSpec;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class VehicleCatalogGUI {
    public static final String CATALOG_TITLE = ChatColor.DARK_AQUA + "Vehicle Showroom & Catalog";

    public static void openCatalog(Player player, VehicleCategory filterCategory, int page) {
        Inventory inv = Bukkit.createInventory(null, 54, CATALOG_TITLE + " (Page " + (page + 1) + ")");

        List<VehicleSpec> list = filterCategory == null
                ? new ArrayList<>(VehicleRegistry.getAll())
                : VehicleRegistry.getByCategory(filterCategory);

        int itemsPerPage = 36;
        int startIndex = page * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, list.size());

        for (int i = startIndex; i < endIndex; i++) {
            VehicleSpec spec = list.get(i);
            ItemStack item = BlockVehiclesPlugin.getInstance().getCustomItemRegistry().createVehicleKey(spec);
            inv.setItem(i - startIndex, item);
        }

        // Category Filter Selector on Bottom Bar
        inv.setItem(45, makeBtn(Material.COMPASS, ChatColor.GOLD + "All Categories", "Click to show all 100 vehicles"));
        inv.setItem(46, makeBtn(Material.REDSTONE_BLOCK, ChatColor.RED + "Supercars", "Filter: Supercars & Hypercars"));
        inv.setItem(47, makeBtn(Material.IRON_BLOCK, ChatColor.WHITE + "Classics & Autos", "Filter: Everyday & Classics"));
        inv.setItem(48, makeBtn(Material.COPPER_BLOCK, ChatColor.GOLD + "Trucks & SUVs", "Filter: Pickups, Trucks & SUVs"));
        inv.setItem(49, makeBtn(Material.DIAMOND_BLOCK, ChatColor.AQUA + "Mining & Diggers", "Filter: Construction & Drilling"));
        inv.setItem(50, makeBtn(Material.OBSIDIAN, ChatColor.DARK_GREEN + "Armored Military", "Filter: Tanks & Combat"));
        inv.setItem(51, makeBtn(Material.FEATHER, ChatColor.LIGHT_PURPLE + "Aircraft & Jets", "Filter: Planes & Helicopters"));

        // Pagination buttons
        if (page > 0) {
            inv.setItem(52, makeBtn(Material.ARROW, ChatColor.YELLOW + "← Previous Page", "Go to page " + page));
        }
        if (endIndex < list.size()) {
            inv.setItem(53, makeBtn(Material.ARROW, ChatColor.YELLOW + "Next Page →", "Go to page " + (page + 2)));
        }

        player.openInventory(inv);
    }

    private static ItemStack makeBtn(Material mat, String name, String lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Collections.singletonList(ChatColor.GRAY + lore));
            item.setItemMeta(meta);
        }
        return item;
    }
}
INNER_EOF

# 13. Create VehicleGUI.java
cat << 'INNER_EOF' > src/main/java/com/blockvehicles/gui/VehicleGUI.java
package com.blockvehicles.gui;

import com.blockvehicles.vehicle.VehicleInstance;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;

public class VehicleGUI {
    public static final String DASHBOARD_TITLE = ChatColor.DARK_BLUE + "Vehicle Control Dashboard";

    public static void openDashboard(Player player, VehicleInstance vehicle) {
        Inventory inv = Bukkit.createInventory(null, 27, DASHBOARD_TITLE);

        // 1. Cargo Trunk
        ItemStack trunkBtn = new ItemStack(Material.CHEST);
        ItemMeta trunkMeta = trunkBtn.getItemMeta();
        trunkMeta.setDisplayName(ChatColor.GOLD + "Access Cargo Trunk (" + (vehicle.getSpec().getStorageRows() * 9) + " Slots)");
        trunkMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click to open vehicle storage."));
        trunkBtn.setItemMeta(trunkMeta);
        inv.setItem(11, trunkBtn);

        // 2. Fuel Gauge & Deposit
        int fuelPercent = (int) ((vehicle.getFuel() / vehicle.getSpec().getMaxFuel()) * 100);
        ItemStack fuelBtn = new ItemStack(Material.BLAZE_POWDER);
        ItemMeta fuelMeta = fuelBtn.getItemMeta();
        fuelMeta.setDisplayName(ChatColor.YELLOW + "Fuel Gauge: " + fuelPercent + "%");
        fuelMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Capacity: " + String.format("%.1f", vehicle.getFuel()) + " / " + vehicle.getSpec().getMaxFuel(),
                ChatColor.AQUA + "Click with Coal, Lava, Redstone or",
                ChatColor.AQUA + "Batteries to refuel."
        ));
        fuelBtn.setItemMeta(fuelMeta);
        inv.setItem(13, fuelBtn);

        // 3. Security Lock
        ItemStack lockBtn = new ItemStack(vehicle.isLocked() ? Material.IRON_DOOR : Material.OAK_DOOR);
        ItemMeta lockMeta = lockBtn.getItemMeta();
        lockMeta.setDisplayName(ChatColor.GREEN + "Lock Status: " + (vehicle.isLocked() ? ChatColor.RED + "LOCKED" : ChatColor.GREEN + "UNLOCKED"));
        lockBtn.setItemMeta(lockMeta);
        inv.setItem(15, lockBtn);

        // 4. Dismantle & Pick Up
        ItemStack pickupBtn = new ItemStack(Material.BARRIER);
        ItemMeta pMeta = pickupBtn.getItemMeta();
        pMeta.setDisplayName(ChatColor.RED + "Dismantle & Retrieve Vehicle Key");
        pickupBtn.setItemMeta(pMeta);
        inv.setItem(26, pickupBtn);

        player.openInventory(inv);
    }
}
INNER_EOF

# 14. Create VehicleControllerTask.java (Full Flight, Excavator Drill, Solar Charging Physics)
cat << 'INNER_EOF' > src/main/java/com/blockvehicles/physics/VehicleControllerTask.java
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
INNER_EOF

# 15. Create VehicleListener.java
cat << 'INNER_EOF' > src/main/java/com/blockvehicles/listeners/VehicleListener.java
package com.blockvehicles.listeners;

import com.blockvehicles.BlockVehiclesPlugin;
import com.blockvehicles.gui.VehicleCatalogGUI;
import com.blockvehicles.gui.VehicleGUI;
import com.blockvehicles.registry.VehicleCategory;
import com.blockvehicles.registry.VehicleRegistry;
import com.blockvehicles.registry.VehicleSpec;
import com.blockvehicles.vehicle.VehicleInstance;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class VehicleListener implements Listener {
    private final BlockVehiclesPlugin plugin;

    public VehicleListener(BlockVehiclesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeployVehicle(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        ItemStack item = event.getItem();
        if (item == null || !item.hasItemMeta()) return;

        String specId = item.getItemMeta().getPersistentDataContainer().get(plugin.getCustomItemRegistry().VEHICLE_SPEC_KEY, PersistentDataType.STRING);
        if (specId == null) return;

        event.setCancelled(true);
        VehicleSpec spec = VehicleRegistry.get(specId);
        if (spec == null) return;

        plugin.getVehicleManager().spawnVehicle(event.getClickedBlock().getLocation().add(0.5, 0.2, 0.5), spec, event.getPlayer().getUniqueId());
        item.setAmount(item.getAmount() - 1);
        event.getPlayer().sendMessage(ChatColor.GREEN + "Deployed " + spec.getName() + "! Right-Click to drive.");
    }

    @EventHandler
    public void onInteractVehicle(PlayerInteractAtEntityEvent event) {
        Entity entity = event.getRightClicked();
        VehicleInstance vehicle = null;

        if (entity instanceof ArmorStand) {
            vehicle = plugin.getVehicleManager().getBySeat(entity);
        }

        if (vehicle == null) {
            for (VehicleInstance v : plugin.getVehicleManager().getActiveVehicles()) {
                if (v.getSeatEntity().getLocation().distanceSquared(entity.getLocation()) < 6.0) {
                    vehicle = v;
                    break;
                }
            }
        }

        if (vehicle == null) return;
        event.setCancelled(true);
        Player player = event.getPlayer();

        if (player.isSneaking()) {
            VehicleGUI.openDashboard(player, vehicle);
        } else {
            if (vehicle.isLocked() && !vehicle.getOwnerUUID().equals(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "This vehicle is locked by its owner!");
                return;
            }
            vehicle.getSeatEntity().addPassenger(player);
        }
    }

    @EventHandler
    public void onDashboardOrCatalogClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();

        // 1. Showroom Catalog GUI Click
        if (title.startsWith(VehicleCatalogGUI.CATALOG_TITLE)) {
            event.setCancelled(true);
            if (slot >= 45 && slot <= 51) {
                VehicleCategory cat = switch (slot) {
                    case 46 -> VehicleCategory.SUPERCAR;
                    case 47 -> VehicleCategory.CIVILIAN_CAR;
                    case 48 -> VehicleCategory.TRUCK_SUV;
                    case 49 -> VehicleCategory.CONSTRUCTION;
                    case 50 -> VehicleCategory.MILITARY;
                    case 51 -> VehicleCategory.FIXED_WING;
                    default -> null;
                };
                VehicleCatalogGUI.openCatalog(player, cat, 0);
            } else if (slot < 36) {
                ItemStack clicked = event.getCurrentItem();
                if (clicked != null && clicked.hasItemMeta() && player.hasPermission("blockvehicles.admin")) {
                    player.getInventory().addItem(clicked.clone());
                    player.sendMessage(ChatColor.GREEN + "Obtained " + clicked.getItemMeta().getDisplayName());
                }
            }
            return;
        }

        // 2. Control Dashboard GUI Click
        if (title.equals(VehicleGUI.DASHBOARD_TITLE)) {
            event.setCancelled(true);
            VehicleInstance vehicle = plugin.getVehicleManager().getActiveVehicles().stream()
                    .filter(v -> v.getSeatEntity().getLocation().distanceSquared(player.getLocation()) < 16.0)
                    .findFirst().orElse(null);

            if (vehicle == null) return;

            if (slot == 11) {
                player.openInventory(vehicle.getTrunk());
            } else if (slot == 13) {
                ItemStack cursor = event.getCursor();
                if (cursor != null && cursor.getType() != Material.AIR) {
                    double fuelAmount = plugin.getConfig().getDouble("fuel_rates." + cursor.getType().name(), 0.0);
                    if (fuelAmount > 0) {
                        vehicle.setFuel(vehicle.getFuel() + fuelAmount);
                        cursor.setAmount(cursor.getAmount() - 1);
                        player.sendMessage(ChatColor.GREEN + "Added +" + fuelAmount + " fuel!");
                        VehicleGUI.openDashboard(player, vehicle);
                    }
                }
            } else if (slot == 15) {
                if (vehicle.getOwnerUUID().equals(player.getUniqueId())) {
                    vehicle.setLocked(!vehicle.isLocked());
                    VehicleGUI.openDashboard(player, vehicle);
                }
            } else if (slot == 26) {
                plugin.getVehicleManager().removeVehicle(vehicle.getId());
                player.getInventory().addItem(plugin.getCustomItemRegistry().createVehicleKey(vehicle.getSpec()));
                player.closeInventory();
                player.sendMessage(ChatColor.YELLOW + "Vehicle dismantled and retrieved.");
            }
        }
    }
}
INNER_EOF

# 16. Create VehicleCommand.java
cat << 'INNER_EOF' > src/main/java/com/blockvehicles/commands/VehicleCommand.java
package com.blockvehicles.commands;

import com.blockvehicles.BlockVehiclesPlugin;
import com.blockvehicles.gui.VehicleCatalogGUI;
import com.blockvehicles.registry.VehicleRegistry;
import com.blockvehicles.registry.VehicleSpec;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VehicleCommand implements CommandExecutor {
    private final BlockVehiclesPlugin plugin;

    public VehicleCommand(BlockVehiclesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player player) {
                VehicleCatalogGUI.openCatalog(player, null, 0);
            } else {
                sender.sendMessage(ChatColor.GOLD + "BlockVehicles 2.0 | Total Vehicles: " + VehicleRegistry.getAll().size());
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("menu") || args[0].equalsIgnoreCase("catalog") || args[0].equalsIgnoreCase("gui")) {
            if (sender instanceof Player player) {
                VehicleCatalogGUI.openCatalog(player, null, 0);
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("give") && sender.hasPermission("blockvehicles.admin")) {
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "Usage: /vehicle give <player> <specId>");
                return true;
            }
            Player target = Bukkit.getPlayer(args);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player not found!");
                return true;
            }
            VehicleSpec spec = VehicleRegistry.get(args[2].toUpperCase());
            if (spec == null) {
                sender.sendMessage(ChatColor.RED + "Invalid vehicle! Use /vehicle menu to browse.");
                return true;
            }
            ItemStack key = plugin.getCustomItemRegistry().createVehicleKey(spec);
            target.getInventory().addItem(key);
            sender.sendMessage(ChatColor.GREEN + "Gave " + spec.getName() + " to " + target.getName());
            return true;
        }

        if (args[0].equalsIgnoreCase("item") && sender instanceof Player player && player.hasPermission("blockvehicles.admin")) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /vehicle item <itemId>");
                return true;
            }
            ItemStack item = plugin.getCustomItemRegistry().getItem(args.toUpperCase());
            if (item != null) {
                player.getInventory().addItem(item);
                player.sendMessage(ChatColor.GREEN + "Given " + item.getItemMeta().getDisplayName());
            } else {
                player.sendMessage(ChatColor.RED + "Item ID not found.");
            }
            return true;
        }

        return true;
    }
}
INNER_EOF

# 17. Create BlockVehiclesPlugin.java
cat << 'INNER_EOF' > src/main/java/com/blockvehicles/BlockVehiclesPlugin.java
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
INNER_EOF

# 18. Build with Maven and Push to Repo
mvn clean package
cp target/BlockVehicles-2.0.0.jar ./BlockVehicles.jar
git add .
git commit -m "Upgrade to BlockVehicles v2.0 with 100 vehicles and real-scale models"
git push

echo "=== Build Complete! BlockVehicles.jar is ready in your GitHub Repo ==="
