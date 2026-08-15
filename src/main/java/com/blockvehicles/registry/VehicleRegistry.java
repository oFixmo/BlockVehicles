package com.blockvehicles.registry;

import org.bukkit.Material;
import java.util.*;

public class VehicleRegistry {
    private static final Map<String, VehicleSpec> VEHICLES = new LinkedHashMap<>();

    static {
        // --- 1. SPECIALIZED DRILLS & EXCAVATORS ---
        reg("COMPACT_MINER_2X2", "Bobcat Compact 2x2 Tunnel Miner", "USA", VehicleCategory.CONSTRUCTION, Material.ORANGE_CONCRETE, Material.BLACK_CONCRETE, Material.YELLOW_STAINED_GLASS, 0.35, 1200, 0.15, 4, false, false, VehicleSpec.DrillMode.COMPACT_2X2, false, 4.0f, 2.0f, 2.2f);
        reg("TUNNEL_BORING_RIG", "Herrenknecht 3x3 Horizontal Tunnel Bore", "Germany", VehicleCategory.CONSTRUCTION, Material.GRAY_CONCRETE, Material.DIAMOND_BLOCK, Material.TINTED_GLASS, 0.25, 3000, 0.30, 6, false, false, VehicleSpec.DrillMode.TUNNEL_3X3, false, 8.5f, 3.2f, 3.2f);
        reg("MEGA_TUNNEL_5X5", "Atlas Copco 5x5 Mega Highway Borer", "Sweden", VehicleCategory.CONSTRUCTION, Material.YELLOW_CONCRETE, Material.NETHERITE_BLOCK, Material.TINTED_GLASS, 0.20, 5000, 0.45, 6, false, false, VehicleSpec.DrillMode.MEGA_5X5, false, 12.0f, 5.2f, 5.2f);
        reg("VERTICAL_SHAFT_3X3", "Sandvik 3x3 Vertical Downward Shaft Miner", "Sweden", VehicleCategory.CONSTRUCTION, Material.RED_TERRACOTTA, Material.DIAMOND_BLOCK, Material.YELLOW_STAINED_GLASS, 0.25, 3500, 0.35, 6, false, false, VehicleSpec.DrillMode.SHAFT_DOWN_3X3, false, 6.5f, 3.2f, 3.8f);
        reg("OPEN_PIT_QUARRY_5X5", "Komatsu 5x5 Deep Quarry Pit Sinker", "Japan", VehicleCategory.CONSTRUCTION, Material.YELLOW_CONCRETE, Material.OBSIDIAN, Material.TINTED_GLASS, 0.18, 6000, 0.50, 6, false, false, VehicleSpec.DrillMode.SHAFT_DOWN_5X5, false, 10.5f, 5.2f, 4.8f);
        reg("CAT_349_EXCAVATOR", "Caterpillar 349 Heavy Track Excavator", "USA", VehicleCategory.CONSTRUCTION, Material.YELLOW_CONCRETE, Material.BLACK_CONCRETE, Material.GLASS, 0.22, 2200, 0.30, 6, false, false, VehicleSpec.DrillMode.TUNNEL_3X3, false, 9.5f, 3.4f, 3.6f);
        reg("CAT_D11_BULLDOZER", "Caterpillar D11 Heavy Track Dozer", "USA", VehicleCategory.CONSTRUCTION, Material.YELLOW_CONCRETE, Material.IRON_BLOCK, Material.GLASS, 0.24, 2400, 0.35, 6, false, false, VehicleSpec.DrillMode.TUNNEL_3X3, false, 8.5f, 3.8f, 3.6f);

        // --- 2. SUPERCARS WITH PANORAMIC GLASS CABINS ---
        reg("BUGATTI_CHIRON", "Bugatti Chiron", "France", VehicleCategory.SUPERCAR, Material.BLUE_CONCRETE, Material.BLACK_CONCRETE, Material.CYAN_STAINED_GLASS, 0.95, 800, 0.22, 2, false, false, VehicleSpec.DrillMode.NONE, false, 4.6f, 2.1f, 1.4f);
        reg("FERRARI_488", "Ferrari 488 Pista", "Italy", VehicleCategory.SUPERCAR, Material.RED_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.90, 750, 0.20, 2, false, false, VehicleSpec.DrillMode.NONE, false, 4.5f, 2.0f, 1.35f);
        reg("LAMBO_AVENTADOR", "Lamborghini Aventador", "Italy", VehicleCategory.SUPERCAR, Material.YELLOW_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.92, 780, 0.21, 2, false, false, VehicleSpec.DrillMode.NONE, false, 4.7f, 2.1f, 1.3f);
        reg("PORSCHE_911_GT3", "Porsche 911 GT3 RS", "Germany", VehicleCategory.SUPERCAR, Material.LIME_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.88, 700, 0.19, 2, false, false, VehicleSpec.DrillMode.NONE, false, 4.5f, 1.9f, 1.4f);
        reg("MCLAREN_P1", "McLaren P1 Hybrid", "UK", VehicleCategory.SUPERCAR, Material.ORANGE_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.93, 760, 0.18, 2, false, false, VehicleSpec.DrillMode.NONE, true, 4.6f, 2.0f, 1.35f);
        reg("KOENIGSEGG_JESKO", "Koenigsegg Jesko", "Sweden", VehicleCategory.SUPERCAR, Material.WHITE_CONCRETE, Material.ORANGE_CONCRETE, Material.TINTED_GLASS, 0.98, 850, 0.24, 2, false, false, VehicleSpec.DrillMode.NONE, false, 4.8f, 2.1f, 1.35f);
        reg("NISSAN_GTR_R35", "Nissan GT-R Nismo", "Japan", VehicleCategory.SUPERCAR, Material.WHITE_CONCRETE, Material.RED_CONCRETE, Material.TINTED_GLASS, 0.86, 720, 0.18, 3, false, false, VehicleSpec.DrillMode.NONE, false, 4.7f, 1.9f, 1.45f);

        // --- 3. EVERYDAY & CLASSICS ---
        reg("TOYOTA_COROLLA", "Toyota Corolla Altis", "Japan", VehicleCategory.CIVILIAN_CAR, Material.WHITE_CONCRETE, Material.GRAY_CONCRETE, Material.GLASS, 0.50, 600, 0.10, 3, false, false, VehicleSpec.DrillMode.NONE, false, 4.6f, 1.9f, 1.55f);
        reg("HONDA_CIVIC_TYPE_R", "Honda Civic Type R", "Japan", VehicleCategory.CIVILIAN_CAR, Material.RED_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.65, 620, 0.12, 3, false, false, VehicleSpec.DrillMode.NONE, false, 4.6f, 1.9f, 1.5f);
        reg("TESLA_MODEL_S", "Tesla Model S Plaid", "USA", VehicleCategory.CIVILIAN_CAR, Material.RED_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.85, 900, 0.10, 4, false, false, VehicleSpec.DrillMode.NONE, true, 4.9f, 2.0f, 1.5f);
        reg("VW_BEETLE_1968", "Volkswagen Classic Beetle", "Germany", VehicleCategory.CIVILIAN_CAR, Material.LIGHT_BLUE_CONCRETE, Material.WHITE_CONCRETE, Material.GLASS, 0.40, 450, 0.08, 2, false, false, VehicleSpec.DrillMode.NONE, false, 4.0f, 1.7f, 1.6f);
        reg("MINI_COOPER_S", "Mini Cooper S", "UK", VehicleCategory.CIVILIAN_CAR, Material.GREEN_CONCRETE, Material.WHITE_CONCRETE, Material.GLASS, 0.55, 500, 0.09, 2, false, false, VehicleSpec.DrillMode.NONE, false, 3.8f, 1.7f, 1.5f);
        reg("FORD_MUSTANG_1969", "1969 Ford Mustang Boss 429", "USA", VehicleCategory.CIVILIAN_CAR, Material.BLACK_CONCRETE, Material.WHITE_CONCRETE, Material.GLASS, 0.70, 650, 0.16, 3, false, false, VehicleSpec.DrillMode.NONE, false, 4.8f, 1.9f, 1.45f);

        // --- 4. TRUCKS & SUVS ---
        reg("FORD_F150_RAPTOR", "Ford F-150 Raptor R", "USA", VehicleCategory.TRUCK_SUV, Material.BLUE_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.60, 950, 0.16, 5, false, false, VehicleSpec.DrillMode.NONE, false, 5.8f, 2.2f, 2.1f);
        reg("TESLA_CYBERTRUCK", "Tesla Cybertruck Tri-Motor", "USA", VehicleCategory.TRUCK_SUV, Material.IRON_BLOCK, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.75, 1100, 0.12, 5, false, false, VehicleSpec.DrillMode.NONE, true, 5.7f, 2.2f, 2.0f);
        reg("MERCEDES_G63_AMG", "Mercedes-Benz G63 AMG 6x6", "Germany", VehicleCategory.TRUCK_SUV, Material.BLACK_CONCRETE, Material.IRON_BLOCK, Material.TINTED_GLASS, 0.65, 1000, 0.18, 5, false, false, VehicleSpec.DrillMode.NONE, false, 5.8f, 2.2f, 2.2f);
        reg("JEEP_WRANGLER", "Jeep Wrangler Rubicon", "USA", VehicleCategory.TRUCK_SUV, Material.RED_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.52, 750, 0.13, 4, false, false, VehicleSpec.DrillMode.NONE, false, 4.7f, 1.9f, 1.9f);
        reg("TOYOTA_LAND_CRUISER", "Toyota Land Cruiser 300", "Japan", VehicleCategory.TRUCK_SUV, Material.WHITE_CONCRETE, Material.GRAY_CONCRETE, Material.TINTED_GLASS, 0.56, 900, 0.14, 5, false, false, VehicleSpec.DrillMode.NONE, false, 5.0f, 2.0f, 2.0f);

        // --- 5. SEMI-TRUCKS & BUSES ---
        reg("SCANIA_R730", "Scania R730 V8 Heavy Hauler", "Sweden", VehicleCategory.SEMI_HEAVY, Material.RED_CONCRETE, Material.IRON_BLOCK, Material.TINTED_GLASS, 0.40, 1800, 0.22, 6, false, false, VehicleSpec.DrillMode.NONE, false, 8.5f, 2.5f, 3.8f);
        reg("PETERBILT_379", "Peterbilt 379 American Classic", "USA", VehicleCategory.SEMI_HEAVY, Material.PURPLE_CONCRETE, Material.IRON_BLOCK, Material.GLASS, 0.45, 1900, 0.25, 6, false, false, VehicleSpec.DrillMode.NONE, false, 9.2f, 2.6f, 3.9f);
        reg("CITY_TRANSIT_BUS", "Mercedes-Benz Citaro Bus", "Germany", VehicleCategory.PUBLIC_TRANSIT, Material.LIGHT_BLUE_CONCRETE, Material.WHITE_CONCRETE, Material.GLASS, 0.38, 1600, 0.18, 6, false, false, VehicleSpec.DrillMode.NONE, false, 11.5f, 2.5f, 3.2f);
        reg("POLICE_INTERCEPTOR", "Ford Police Interceptor Utility", "USA", VehicleCategory.EMERGENCY, Material.BLACK_CONCRETE, Material.WHITE_CONCRETE, Material.TINTED_GLASS, 0.72, 850, 0.15, 4, false, false, VehicleSpec.DrillMode.NONE, false, 5.1f, 2.0f, 1.8f);
        reg("TYPE3_AMBULANCE", "Ford F-450 Super Duty Ambulance", "USA", VehicleCategory.EMERGENCY, Material.WHITE_CONCRETE, Material.RED_CONCRETE, Material.TINTED_GLASS, 0.55, 1100, 0.16, 5, false, false, VehicleSpec.DrillMode.NONE, false, 6.8f, 2.4f, 2.8f);

        // --- 6. MILITARY TANKS ---
        reg("M1A2_ABRAMS", "M1A2 SEPv3 Abrams Battle Tank", "USA", VehicleCategory.MILITARY, Material.TERRACOTTA, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.42, 3000, 0.40, 6, false, false, VehicleSpec.DrillMode.COMPACT_2X2, false, 9.8f, 3.7f, 2.6f);
        reg("LEOPARD_2A7", "Leopard 2A7+ Combat Tank", "Germany", VehicleCategory.MILITARY, Material.GREEN_TERRACOTTA, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.44, 2900, 0.38, 6, false, false, VehicleSpec.DrillMode.COMPACT_2X2, false, 9.9f, 3.8f, 2.8f);

        // --- 7. AIRCRAFT ---
        reg("AH64_APACHE", "Boeing AH-64E Apache Gunship", "USA", VehicleCategory.ROTORCRAFT, Material.GRAY_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 0.72, 1800, 0.26, 4, true, true, VehicleSpec.DrillMode.NONE, false, 14.5f, 4.8f, 4.1f);
        reg("CESSNA_172", "Cessna 172 Skyhawk Aviation", "USA", VehicleCategory.FIXED_WING, Material.WHITE_CONCRETE, Material.BLUE_CONCRETE, Material.GLASS, 0.65, 1100, 0.15, 3, true, false, VehicleSpec.DrillMode.NONE, false, 8.2f, 11.0f, 2.7f);
        reg("F22_RAPTOR", "F-22A Raptor Stealth Jet", "USA", VehicleCategory.FIXED_WING, Material.GRAY_CONCRETE, Material.BLACK_CONCRETE, Material.TINTED_GLASS, 1.25, 2600, 0.38, 4, true, false, VehicleSpec.DrillMode.NONE, false, 18.9f, 13.5f, 5.0f);
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
