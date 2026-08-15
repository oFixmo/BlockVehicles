package com.blockvehicles.models;

import com.blockvehicles.registry.VehicleCategory;
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
        Material p = spec.getPrimaryMaterial();
        Material s = spec.getSecondaryMaterial();
        Material g = spec.getGlassMaterial();

        // -------------------------------------------------------------
        // 1. TRACTORS & HARVESTERS (Big Rear Wheels, Sloped Hood, Exhaust)
        // -------------------------------------------------------------
        if (spec.getId().contains("TRACTOR") || spec.getId().contains("HARVESTER") || spec.getId().contains("9RX")) {
            // Front Small Steer Wheels + Hubs
            parts.add(createPart(world, baseLoc, Material.BLACK_CONCRETE, new Vector3f(-w*0.48f, 0.05f, l*0.35f), new Vector3f(0.32f, 0.65f, 0.65f)));
            parts.add(createPart(world, baseLoc, Material.YELLOW_CONCRETE, new Vector3f(-w*0.50f, 0.22f, l*0.35f + 0.17f), new Vector3f(0.04f, 0.32f, 0.32f)));
            parts.add(createPart(world, baseLoc, Material.BLACK_CONCRETE, new Vector3f(w*0.48f - 0.32f, 0.05f, l*0.35f), new Vector3f(0.32f, 0.65f, 0.65f)));
            parts.add(createPart(world, baseLoc, Material.YELLOW_CONCRETE, new Vector3f(w*0.48f - 0.02f, 0.22f, l*0.35f + 0.17f), new Vector3f(0.04f, 0.32f, 0.32f)));

            // Rear Giant Heavy Wheels + Hubs
            parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(-w*0.54f, 0.05f, -l*0.30f), new Vector3f(0.48f, 1.25f, 1.25f)));
            parts.add(createPart(world, baseLoc, Material.YELLOW_CONCRETE, new Vector3f(-w*0.56f, 0.42f, -l*0.30f + 0.38f), new Vector3f(0.04f, 0.50f, 0.50f)));
            parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(w*0.54f - 0.48f, 0.05f, -l*0.30f), new Vector3f(0.48f, 1.25f, 1.25f)));
            parts.add(createPart(world, baseLoc, Material.YELLOW_CONCRETE, new Vector3f(w*0.54f - 0.02f, 0.42f, -l*0.30f + 0.38f), new Vector3f(0.04f, 0.50f, 0.50f)));

            // Engine Hood & Front Grille
            parts.add(createPart(world, baseLoc, p, new Vector3f(-w*0.32f, 0.50f, 0.05f), new Vector3f(w*0.64f, 0.70f, l*0.45f)));
            parts.add(createPart(world, baseLoc, Material.BLACK_CONCRETE, new Vector3f(-w*0.28f, 0.55f, l*0.50f - 0.02f), new Vector3f(w*0.56f, 0.45f, 0.04f)));

            // Exhaust Smokestack Pipe
            parts.add(createPart(world, baseLoc, Material.ANVIL, new Vector3f(w*0.28f, 1.20f, l*0.25f), new Vector3f(0.18f, 0.85f, 0.18f)));

            // Glass Cabin & Roof Shade
            parts.add(createPart(world, baseLoc, Material.TINTED_GLASS, new Vector3f(-w*0.36f, 1.05f, -l*0.32f), new Vector3f(w*0.72f, 0.85f, l*0.36f)));
            parts.add(createPart(world, baseLoc, p, new Vector3f(-w*0.38f, 1.90f, -l*0.35f), new Vector3f(w*0.76f, 0.12f, l*0.42f)));
            return parts;
        }

        // -------------------------------------------------------------
        // 2. MOTORCYCLES & BIKES (Dual Inline Wheels, Spoke Hubs, Saddle)
        // -------------------------------------------------------------
        if (spec.getCategory() == VehicleCategory.MOTORCYCLE) {
            // Front Wheel + Spoke Disc
            parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(-0.10f, 0.05f, l*0.32f), new Vector3f(0.20f, 0.68f, 0.68f)));
            parts.add(createPart(world, baseLoc, Material.IRON_BLOCK, new Vector3f(-0.11f, 0.24f, l*0.32f + 0.19f), new Vector3f(0.22f, 0.30f, 0.30f)));

            // Rear Wheel + Spoke Disc
            parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(-0.10f, 0.05f, -l*0.32f), new Vector3f(0.20f, 0.68f, 0.68f)));
            parts.add(createPart(world, baseLoc, Material.IRON_BLOCK, new Vector3f(-0.11f, 0.24f, -l*0.32f + 0.19f), new Vector3f(0.22f, 0.30f, 0.30f)));

            // Slender Engine Block & Chrome Exhaust
            parts.add(createPart(world, baseLoc, Material.GRAY_CONCRETE, new Vector3f(-0.18f, 0.32f, -l*0.22f), new Vector3f(0.36f, 0.40f, l*0.48f)));
            parts.add(createPart(world, baseLoc, Material.END_ROD, new Vector3f(0.18f, 0.28f, -l*0.38f), new Vector3f(0.10f, 0.10f, l*0.50f)));

            // Sculpted Fuel Tank
            parts.add(createPart(world, baseLoc, p, new Vector3f(-0.22f, 0.65f, 0.02f), new Vector3f(0.44f, 0.32f, l*0.28f)));

            // Leather Rider Saddle
            parts.add(createPart(world, baseLoc, Material.BLACK_WOOL, new Vector3f(-0.18f, 0.62f, -l*0.22f), new Vector3f(0.36f, 0.18f, l*0.24f)));

            // Handlebars & Cyclops Headlight
            parts.add(createPart(world, baseLoc, Material.LIGHTNING_ROD, new Vector3f(-0.38f, 0.88f, l*0.22f), new Vector3f(0.76f, 0.10f, 0.10f)));
            parts.add(createPart(world, baseLoc, Material.SEA_LANTERN, new Vector3f(-0.12f, 0.65f, l*0.38f), new Vector3f(0.24f, 0.24f, 0.08f)));
            return parts;
        }

        // -------------------------------------------------------------
        // 3. CARS, SUPERCARS & TRUCKS (Aerodynamic Shell & 3D Glass Cabin)
        // -------------------------------------------------------------
        // Lower Aerodynamic Splitter Plate (Ground clearance = 0.18m)
        parts.add(createPart(world, baseLoc, s, new Vector3f(-w/2f, 0.18f, -l/2f), new Vector3f(w, 0.14f, l)));

        // Sculpted Front Hood
        parts.add(createPart(world, baseLoc, p, new Vector3f(-w*0.48f, 0.32f, l*0.10f), new Vector3f(w*0.96f, h*0.32f, l*0.40f)));

        // Rear Trunk & Aerodynamic Spoiler
        parts.add(createPart(world, baseLoc, p, new Vector3f(-w*0.48f, 0.32f, -l/2f), new Vector3f(w*0.96f, h*0.32f, l*0.35f)));
        parts.add(createPart(world, baseLoc, s, new Vector3f(-w*0.48f, 0.32f + h*0.35f, -l*0.48f), new Vector3f(w*0.96f, 0.08f, 0.20f)));

        // Interior Leather Bucket Chair
        parts.add(createPart(world, baseLoc, Material.BLACK_WOOL, new Vector3f(-0.35f, 0.32f, -0.15f), new Vector3f(0.70f, 0.20f, 0.50f)));
        parts.add(createPart(world, baseLoc, Material.BLACK_CONCRETE, new Vector3f(-0.35f, 0.52f, -0.40f), new Vector3f(0.70f, 0.65f, 0.12f)));
        parts.add(createPart(world, baseLoc, Material.LEVER, new Vector3f(-0.15f, 0.52f, 0.12f), new Vector3f(0.30f, 0.30f, 0.10f)));

        // 3D Glass Windshield & Side Windows
        if (g != Material.AIR) {
            parts.add(createPart(world, baseLoc, g, new Vector3f(-w*0.44f, 0.58f, l*0.08f), new Vector3f(w*0.88f, h*0.48f, 0.12f)));
            parts.add(createPart(world, baseLoc, g, new Vector3f(-w*0.46f, 0.58f, -l*0.24f), new Vector3f(0.08f, h*0.48f, l*0.32f)));
            parts.add(createPart(world, baseLoc, g, new Vector3f(w*0.46f - 0.08f, 0.58f, -l*0.24f), new Vector3f(0.08f, h*0.48f, l*0.32f)));
            parts.add(createPart(world, baseLoc, g, new Vector3f(-w*0.44f, 0.58f, -l*0.25f), new Vector3f(w*0.88f, h*0.48f, 0.12f)));
            parts.add(createPart(world, baseLoc, p, new Vector3f(-w*0.44f, 0.58f + h*0.48f, -l*0.24f), new Vector3f(w*0.88f, 0.08f, l*0.32f)));
        }

        // 4 Custom High-Traction Wheels + Metallic Hub Rims
        float[] xSides = {-w/2f - 0.12f, w/2f - 0.24f};
        float[] zAxles = {l*0.24f, -l*0.28f};
        for (float x : xSides) {
            for (float z : zAxles) {
                parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(x, 0.05f, z), new Vector3f(0.36f, 0.68f, 0.68f)));
                float rimX = (x < 0) ? x - 0.02f : x + 0.32f;
                parts.add(createPart(world, baseLoc, Material.SMOOTH_STONE_SLAB, new Vector3f(rimX, 0.24f, z + 0.19f), new Vector3f(0.04f, 0.30f, 0.30f)));
            }
        }

        // Front Grille & Dual Headlights
        parts.add(createPart(world, baseLoc, Material.BLACK_CONCRETE, new Vector3f(-w*0.30f, 0.32f, l/2f - 0.02f), new Vector3f(w*0.60f, 0.22f, 0.04f)));
        parts.add(createPart(world, baseLoc, Material.SEA_LANTERN, new Vector3f(-w*0.44f, 0.36f, l/2f - 0.02f), new Vector3f(0.22f, 0.18f, 0.04f)));
        parts.add(createPart(world, baseLoc, Material.SEA_LANTERN, new Vector3f(w*0.44f - 0.22f, 0.36f, l/2f - 0.02f), new Vector3f(0.22f, 0.18f, 0.04f)));

        // Rear Red Brake Lights
        parts.add(createPart(world, baseLoc, Material.REDSTONE_BLOCK, new Vector3f(-w*0.44f, 0.38f, -l/2f - 0.02f), new Vector3f(0.22f, 0.14f, 0.04f)));
        parts.add(createPart(world, baseLoc, Material.REDSTONE_BLOCK, new Vector3f(w*0.44f - 0.22f, 0.38f, -l/2f - 0.02f), new Vector3f(0.22f, 0.14f, 0.04f)));

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
