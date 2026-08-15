package com.blockvehicles.registry;

import org.bukkit.Material;
import java.util.*;

public class VehicleRegistry {
    private static final Map<String, VehicleSpec> VEHICLES = new LinkedHashMap<>();

    static {
        // --- 1. MOTORCYCLES & SUPERBIKES (NO GLASS - OPEN SEATS) ---
        reg("DUCATI_PANIGALE", "Ducati Panigale V4 Superbike", "Italy", VehicleCategory.MOTORCYCLE, Material.RED_CONCRETE, Material.BLACK_CONCRETE, Material.AIR, 0.95, 450, 0.14, 1, false, false, VehicleSpec.DrillMode.NONE, false, 2.2f, 0.8f, 1.2f);
        reg("KAWASAKI_NINJA_H2R", "Kawasaki Ninja H2R Supercharged", "Japan", VehicleCategory.MOTORCYCLE, Material.LIME_CONCRETE, Material.BLACK_CONCRETE, Material.AIR, 1.05, 500, 0.16, 1, false, false, VehicleSpec.DrillMode.NONE, false, 2.3f, 0.8f, 1.25f);
        reg("HARLEY_FATBOY", "Harley-Davidson Fat Boy Cruiser", "USA", VehicleCategory.MOTORCYCLE, Material.BLACK_CONCRETE, Material.IRON_BLOCK, Material.AIR, 0.70, 550, 0.12, 2, false, false, VehicleSpec.DrillMode.NONE, false, 2.5f, 0.9f, 1.15f);
        reg("BMW_R1250GS", "BMW R1250GS Adventure Tourer", "Germany", VehicleCategory.MOTORCYCLE, Material.LIGHT_BLUE_CONCRETE, Material.GRAY_CONCRETE, Material.AIR, 0.75, 600, 0.12, 3, false, false, VehicleSpec.DrillMode.NONE, false, 2.4f, 0.95f, 1.4f);
        reg("ROYAL_ENFIELD_350", "Royal Enfield Classic 350", "India", VehicleCategory.MOTORCYCLE, Material.BLACK_CONCRETE, Material.GOLD_BLOCK, Material.AIR, 0.55, 400, 0.08, 1, false, false, VehicleSpec.DrillMode.NONE, false, 2.2f, 0.8f, 1.15f);
        reg("VESPA_150", "Vespa Primavera 150 Classic Scooter", "Italy", VehicleCategory.MOTORCYCLE, Material.CYAN_CONCRETE, Material.WHITE_CONCRETE, Material.AIR, 0.45, 300, 0.05, 1, false, false, VehicleSpec.DrillMode.NONE, false, 1.9f, 0.7f, 1.15f);
        reg("KTM_450_DIRTBIKE", "KTM 450 SX-F Motocross Dirt Bike", "Austria", VehicleCategory.MOTORCYCLE, Material.ORANGE_CONCRETE, Material.BLACK_CONCRETE, Material.AIR, 0.80, 420, 0.11, 1, false, false, VehicleSpec.DrillMode.NONE, false, 2.2f, 0.8f, 1.35f);

        // --- 2. SPECIALIZED DRILLS ---
        reg("COMPACT_MINER_2X2", "Bobcat Compact 2x2 Tunnel Miner", "USA", VehicleCategory.CONSTRUCTION, Material.ORANGE_CONCRETE, Material.BLACK_CONCRETE, Material.YELLOW_STAINED_GLASS, 0.35, 1200, 0.15, 4, false, false, VehicleSpec.DrillMode.COMPACT_2X2, false, 4.0f, 2.0f, 2.2f);
        reg("TUNNEL_BORING_RIG", "Herrenknecht 3x3 Tunnel Borer", "Germany", VehicleCategory.CONSTRUCTION, Material.GRAY_CONCRETE, Material.DIAMOND_BLOCK, Material.TINTED_GLASS, 0.25, 3000, 0.30, 6, false, false, VehicleSpec.DrillMode.TUNNEL_3X3, false, 8.5f, 3.2f, 3.2f);
        reg("MEGA_TUNNEL_5X5", "Atlas Copco 5x5 Mega Highway Borer", "Sweden", VehicleCategory.CONSTRUCTION, Material.YELLOW_CONCRETE, Material.NETHERITE_BLOCK, Material.TINTED_GLASS, 0.20, 5000, 0.45, 6, false, false, VehicleSpec.DrillMode.MEGA_5X5, false, 12.0f, 5.2f, 5.2f);
        reg("VERTICAL_SHAFT_3X3", "Sandvik 3x3 Vertical Downward Shaft Miner", "Sweden", VehicleCategory.CONSTRUCTION, Material.RED_TERRACOTTA, Material.DIAMOND_BLOCK, Material.YELLOW_STAINED_GLASS, 0.25, 3500, 0.35, 6, false, false, VehicleSpec.DrillMode.SHAFT_DOWN_3X3, false, 6.5f, 3.2f, 3.8f);
        reg("OPEN_PIT_QUARRY_5X5", "Komatsu 5x5 Deep Quarry Pit Sinker", "Japan", VehicleCategory.CONSTRUCTION, Material.YELLOW_CONCRETE, Material.OBSIDIAN, Material.TINTED_GLASS, 0.18, 6000, 0.50, 6, false, false, VehicleSpec.DrillMode.SHAFT_DOWN_5X5, false, 10.5f, 5.2f, 4.8f);
        reg("CAT_349_EXCAVATOR", "Caterpillar 349 Heavy Track Excavator", "USA", VehicleCategory.CONSTRUCTION, Material.YELLOW_CONCRETE, Material.BLACK_CONCRETE, Material.GLASS, 0.22, 2200, 0.30, 6, false, false, VehicleSpec.DrillMode.TUNNEL_3X3, false, 9.5f, 3.4f, 3.6f);

        // --- 3. SUPERCARS WITH PANORAMIC GLASS & INTERIOR SEATS ---
        reg("BUGATTI_CHIRON", "Bugatti Chiron", "France", VehicleCategory.SUPERCAR, Material.BLUE_CONCRETE, Material.BLACK_CONCRETE, Material.CYAN_STAINED_GLASS, 0.95, 800, 0.22, 2, false, false, VehicleSpec.DrillMode.NONE, false, 4.6f, 2.1f, 1.4f);
        reg("FERRARI_488", "Ferrari 488 Pista", "Italy", VehicleCategory.SUPERCAR, Material.RED_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.90, 750, 0.20, 2, false, false, VehicleSpec.DrillMode.NONE, false, 4.5f, 2.0f, 1.35f);
        reg("LAMBO_AVENTADOR", "Lamborghini Aventador", "Italy", VehicleCategory.SUPERCAR, Material.YELLOW_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.92, 780, 0.21, 2, false, false, VehicleSpec.DrillMode.NONE, false, 4.7f, 2.1f, 1.3f);
        reg("PORSCHE_911_GT3", "Porsche 911 GT3 RS", "Germany", VehicleCategory.SUPERCAR, Material.LIME_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.88, 700, 0.19, 2, false, false, VehicleSpec.DrillMode.NONE, false, 4.5f, 1.9f, 1.4f);
        reg("MCLAREN_P1", "McLaren P1 Hybrid", "UK", VehicleCategory.SUPERCAR, Material.ORANGE_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.93, 760, 0.18, 2, false, false, VehicleSpec.DrillMode.NONE, true, 4.6f, 2.0f, 1.35f);

        // --- 4. EVERYDAY AUTOS & SUVS ---
        reg("TOYOTA_COROLLA", "Toyota Corolla Altis", "Japan", VehicleCategory.CIVILIAN_CAR, Material.WHITE_CONCRETE, Material.GRAY_CONCRETE, Material.GLASS, 0.50, 600, 0.10, 3, false, false, VehicleSpec.DrillMode.NONE, false, 4.6f, 1.9f, 1.55f);
        reg("HONDA_CIVIC_TYPE_R", "Honda Civic Type R", "Japan", VehicleCategory.CIVILIAN_CAR, Material.RED_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.65, 620, 0.12, 3, false, false, VehicleSpec.DrillMode.NONE, false, 4.6f, 1.9f, 1.5f);
        reg("TESLA_CYBERTRUCK", "Tesla Cybertruck Tri-Motor", "USA", VehicleCategory.TRUCK_SUV, Material.IRON_BLOCK, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.75, 1100, 0.12, 5, false, false, VehicleSpec.DrillMode.NONE, true, 5.7f, 2.2f, 2.0f);
        reg("FORD_F150_RAPTOR", "Ford F-150 Raptor R", "USA", VehicleCategory.TRUCK_SUV, Material.BLUE_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.60, 950, 0.16, 5, false, false, VehicleSpec.DrillMode.NONE, false, 5.8f, 2.2f, 2.1f);
        reg("MERCEDES_G63_AMG", "Mercedes-Benz G63 AMG 6x6", "Germany", VehicleCategory.TRUCK_SUV, Material.BLACK_CONCRETE, Material.IRON_BLOCK, Material.TINTED_GLASS, 0.65, 1000, 0.18, 5, false, false, VehicleSpec.DrillMode.NONE, false, 5.8f, 2.2f, 2.2f);
        reg("SCANIA_R730", "Scania R730 V8 Heavy Hauler", "Sweden", VehicleCategory.SEMI_HEAVY, Material.RED_CONCRETE, Material.IRON_BLOCK, Material.TINTED_GLASS, 0.40, 1800, 0.22, 6, false, false, VehicleSpec.DrillMode.NONE, false, 8.5f, 2.5f, 3.8f);

        // --- 5. MILITARY & AIRCRAFT ---
        reg("M1A2_ABRAMS", "M1A2 SEPv3 Abrams Battle Tank", "USA", VehicleCategory.MILITARY, Material.TERRACOTTA, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.42, 3000, 0.40, 6, false, false, VehicleSpec.DrillMode.COMPACT_2X2, false, 9.8f, 3.7f, 2.6f);
        reg("AH64_APACHE", "Boeing AH-64E Apache Gunship", "USA", VehicleCategory.ROTORCRAFT, Material.GRAY_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.72, 1800, 0.26, 4, true, true, VehicleSpec.DrillMode.NONE, false, 14.5f, 4.8f, 4.1f);
        reg("F22_RAPTOR", "F-22A Raptor Stealth Jet", "USA", VehicleCategory.FIXED_WING, Material.GRAY_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 1.25, 2600, 0.38, 4, true, false, VehicleSpec.DrillMode.NONE, false, 18.9f, 13.5f, 5.0f);
        reg("CESSNA_172", "Cessna 172 Skyhawk Aviation", "USA", VehicleCategory.FIXED_WING, Material.WHITE_CONCRETE, Material.BLUE_CONCRETE, Material.GLASS, 0.65, 1100, 0.15, 3, true, false, VehicleSpec.DrillMode.NONE, false, 8.2f, 11.0f, 2.7f);
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
    public static List<VehicleSpec> getByCategory(VehicleCategory category) {
        List<VehicleSpec> list = new ArrayList<>();
        for (VehicleSpec spec : VEHICLES.values()) {
            if (spec.getCategory() == category) list.add(spec);
        }
        return list;
    }
}
