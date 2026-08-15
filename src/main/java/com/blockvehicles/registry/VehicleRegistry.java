package com.blockvehicles.registry;

import org.bukkit.Material;
import java.util.*;

public class VehicleRegistry {
    private static final Map<String, VehicleSpec> VEHICLES = new LinkedHashMap<>();

    static {
        // --- 1. HELICOPTERS & ROTORCRAFT ---
        reg("HELICOPTER", "AH-64 Apache Attack Helicopter", "USA", VehicleCategory.ROTORCRAFT, Material.GRAY_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.75, 2000, 0.25, 4, true, true, VehicleSpec.DrillMode.NONE, false, 14.5f, 4.8f, 4.1f);
        reg("BELL_206", "Bell 206 Executive Helicopter", "USA", VehicleCategory.ROTORCRAFT, Material.WHITE_CONCRETE, Material.BLUE_CONCRETE, Material.GLASS, 0.65, 1400, 0.18, 4, true, true, VehicleSpec.DrillMode.NONE, false, 11.5f, 2.8f, 3.4f);
        reg("CH47_CHINOOK", "Boeing CH-47 Chinook Heavy Transport", "USA", VehicleCategory.ROTORCRAFT, Material.GREEN_TERRACOTTA, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.60, 3200, 0.35, 6, true, true, VehicleSpec.DrillMode.NONE, false, 20.0f, 4.8f, 5.2f);

        // --- 2. CARS & SUPERCARS ---
        reg("CAR", "Bugatti Chiron Super Sport", "France", VehicleCategory.SUPERCAR, Material.BLUE_CONCRETE, Material.BLACK_CONCRETE, Material.CYAN_STAINED_GLASS, 0.95, 800, 0.20, 3, false, false, VehicleSpec.DrillMode.NONE, false, 4.6f, 2.1f, 1.4f);
        reg("FERRARI_488", "Ferrari 488 Pista", "Italy", VehicleCategory.SUPERCAR, Material.RED_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.90, 750, 0.20, 2, false, false, VehicleSpec.DrillMode.NONE, false, 4.5f, 2.0f, 1.35f);
        reg("LAMBO_AVENTADOR", "Lamborghini Aventador", "Italy", VehicleCategory.SUPERCAR, Material.YELLOW_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.92, 780, 0.21, 2, false, false, VehicleSpec.DrillMode.NONE, false, 4.7f, 2.1f, 1.3f);
        reg("TOYOTA_COROLLA", "Toyota Corolla Altis", "Japan", VehicleCategory.CIVILIAN_CAR, Material.WHITE_CONCRETE, Material.GRAY_CONCRETE, Material.GLASS, 0.50, 600, 0.10, 3, false, false, VehicleSpec.DrillMode.NONE, false, 4.6f, 1.9f, 1.55f);
        reg("TESLA_CYBERTRUCK", "Tesla Cybertruck Tri-Motor", "USA", VehicleCategory.TRUCK_SUV, Material.IRON_BLOCK, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.75, 1100, 0.12, 5, false, false, VehicleSpec.DrillMode.NONE, true, 5.7f, 2.2f, 2.0f);

        // --- 3. BIKES & MOTORCYCLES ---
        reg("BIKE", "Ducati Panigale V4 Superbike", "Italy", VehicleCategory.MOTORCYCLE, Material.RED_CONCRETE, Material.BLACK_CONCRETE, Material.AIR, 0.95, 450, 0.14, 1, false, false, VehicleSpec.DrillMode.NONE, false, 2.2f, 0.8f, 1.2f);
        reg("HARLEY_FATBOY", "Harley-Davidson Fat Boy Cruiser", "USA", VehicleCategory.MOTORCYCLE, Material.BLACK_CONCRETE, Material.IRON_BLOCK, Material.AIR, 0.70, 550, 0.12, 2, false, false, VehicleSpec.DrillMode.NONE, false, 2.5f, 0.9f, 1.15f);
        reg("KAWASAKI_NINJA", "Kawasaki Ninja H2R Supercharged", "Japan", VehicleCategory.MOTORCYCLE, Material.LIME_CONCRETE, Material.BLACK_CONCRETE, Material.AIR, 1.05, 500, 0.16, 1, false, false, VehicleSpec.DrillMode.NONE, false, 2.3f, 0.8f, 1.25f);

        // --- 4. TRACTORS & HARVESTERS ---
        reg("TRACTOR", "John Deere 9RX Farm Tractor", "USA", VehicleCategory.AGRICULTURE, Material.LIME_CONCRETE, Material.YELLOW_CONCRETE, Material.GLASS, 0.35, 2200, 0.25, 6, false, false, VehicleSpec.DrillMode.NONE, false, 7.5f, 3.4f, 3.8f);

        // --- 5. PLANES & JETS ---
        reg("PLANE", "Cessna 172 Skyhawk Private Aircraft", "USA", VehicleCategory.FIXED_WING, Material.WHITE_CONCRETE, Material.BLUE_CONCRETE, Material.GLASS, 0.70, 1200, 0.18, 4, true, false, VehicleSpec.DrillMode.NONE, false, 8.5f, 11.0f, 2.8f);
        reg("F22_RAPTOR", "F-22A Raptor Stealth Fighter Jet", "USA", VehicleCategory.FIXED_WING, Material.GRAY_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 1.30, 2600, 0.40, 4, true, false, VehicleSpec.DrillMode.NONE, false, 18.9f, 13.5f, 5.0f);

        // --- 6. DRILLS & EXCAVATORS ---
        reg("DRILL", "Herrenknecht 3x3 Tunnel Borer", "Germany", VehicleCategory.CONSTRUCTION, Material.GRAY_CONCRETE, Material.DIAMOND_BLOCK, Material.TINTED_GLASS, 0.25, 3000, 0.30, 6, false, false, VehicleSpec.DrillMode.TUNNEL_3X3, false, 8.5f, 3.2f, 3.2f);
        reg("MEGA_DRILL", "Atlas Copco 5x5 Mega Highway Borer", "Sweden", VehicleCategory.CONSTRUCTION, Material.YELLOW_CONCRETE, Material.NETHERITE_BLOCK, Material.TINTED_GLASS, 0.20, 5000, 0.45, 6, false, false, VehicleSpec.DrillMode.MEGA_5X5, false, 12.0f, 5.2f, 5.2f);
        reg("SHAFT_DRILL", "Sandvik 3x3 Vertical Downward Shaft Miner", "Sweden", VehicleCategory.CONSTRUCTION, Material.RED_TERRACOTTA, Material.DIAMOND_BLOCK, Material.YELLOW_STAINED_GLASS, 0.25, 3500, 0.35, 6, false, false, VehicleSpec.DrillMode.SHAFT_DOWN_3X3, false, 6.5f, 3.2f, 3.8f);

        // --- 7. TANKS ---
        reg("TANK", "M1A2 SEPv3 Abrams Battle Tank", "USA", VehicleCategory.MILITARY, Material.TERRACOTTA, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.42, 3000, 0.40, 6, false, false, VehicleSpec.DrillMode.COMPACT_2X2, false, 9.8f, 3.7f, 2.6f);
    }

    private static void reg(String id, String name, String origin, VehicleCategory category,
                            Material primaryMaterial, Material secondaryMaterial, Material glassMaterial,
                            double speed, double maxFuel, double fuelBurnRate, int storageRows,
                            boolean flying, boolean canHover, VehicleSpec.DrillMode drillMode, boolean hasSolar,
                            float length, float width, float height) {
        VEHICLES.put(id, new VehicleSpec(id, name, origin, category, primaryMaterial, secondaryMaterial, glassMaterial,
                speed, maxFuel, fuelBurnRate, storageRows, flying, canHover, drillMode, hasSolar, length, width, height));
    }

    public static VehicleSpec get(String id) { return VEHICLES.get(id); }
    public static Collection<VehicleSpec> getAll() { return VEHICLES.values(); }

    public static VehicleSpec findBestMatch(String query) {
        String q = query.toUpperCase();
        if (VEHICLES.containsKey(q)) return VEHICLES.get(q);
        for (Map.Entry<String, VehicleSpec> entry : VEHICLES.entrySet()) {
            if (entry.getKey().contains(q) || entry.getValue().getName().toUpperCase().contains(q)) {
                return entry.getValue();
            }
        }
        return VEHICLES.get("CAR");
    }

    public static List<VehicleSpec> getByCategory(VehicleCategory category) {
        List<VehicleSpec> list = new ArrayList<>();
        for (VehicleSpec spec : VEHICLES.values()) {
            if (spec.getCategory() == category) list.add(spec);
        }
        return list;
    }
}
