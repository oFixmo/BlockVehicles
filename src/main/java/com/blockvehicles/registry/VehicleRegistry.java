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
