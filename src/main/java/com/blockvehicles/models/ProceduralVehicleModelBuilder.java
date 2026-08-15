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

        // -----------------------------------------------------------------
        // 1. TRACTOR (Farm Vehicle - Big Rear Wheels, Sloped Hood, Exhaust)
        // -----------------------------------------------------------------
        if (spec.getId().contains("TRACTOR") || spec.getId().contains("HARVESTER") || spec.getId().contains("9RX")) {
            // Front Small Wheels + Rims
            parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(-w*0.5f, 0.0f, l*0.35f), new Vector3f(0.35f, 0.6f, 0.6f)));
            parts.add(createPart(world, baseLoc, Material.SMOOTH_STONE_SLAB, new Vector3f(-w*0.52f, 0.15f, l*0.35f + 0.15f), new Vector3f(0.05f, 0.3f, 0.3f)));
            parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(w*0.5f - 0.35f, 0.0f, l*0.35f), new Vector3f(0.35f, 0.6f, 0.6f)));
            parts.add(createPart(world, baseLoc, Material.SMOOTH_STONE_SLAB, new Vector3f(w*0.5f - 0.03f, 0.15f, l*0.35f + 0.15f), new Vector3f(0.05f, 0.3f, 0.3f)));

            // Rear Giant Heavy Wheels
            parts.add(createPart(world, baseLoc, Material.BLACK_CONCRETE, new Vector3f(-w*0.55f, 0.0f, -l*0.30f), new Vector3f(0.5f, 1.2f, 1.2f)));
            parts.add(createPart(world, baseLoc, Material.YELLOW_CONCRETE, new Vector3f(-w*0.57f, 0.35f, -l*0.30f + 0.35f), new Vector3f(0.05f, 0.5f, 0.5f)));
            parts.add(createPart(world, baseLoc, Material.BLACK_CONCRETE, new Vector3f(w*0.55f - 0.5f, 0.0f, -l*0.30f), new Vector3f(0.5f, 1.2f, 1.2f)));
            parts.add(createPart(world, baseLoc, Material.YELLOW_CONCRETE, new Vector3f(w*0.55f - 0.03f, 0.35f, -l*0.30f + 0.35f), new Vector3f(0.05f, 0.5f, 0.5f)));

            // Sloped Engine Hood
            parts.add(createPart(world, baseLoc, p, new Vector3f(-w*0.35f, 0.45f, 0.0f), new Vector3f(w*0.7f, 0.65f, l*0.48f)));
            // Vertical Smoke Exhaust Pipe
            parts.add(createPart(world, baseLoc, Material.ANVIL, new Vector3f(w*0.30f, 1.1f, l*0.25f), new Vector3f(0.2f, 0.9f, 0.2f)));
            // High Driver Cabin & Roof
            parts.add(createPart(world, baseLoc, Material.GLASS, new Vector3f(-w*0.38f, 1.0f, -l*0.32f), new Vector3f(w*0.76f, 0.8f, l*0.38f)));
            parts.add(createPart(world, baseLoc, p, new Vector3f(-w*0.4f, 1.8f, -l*0.35f), new Vector3f(w*0.8f, 0.15f, l*0.42f)));
            return parts;
        }

        // -----------------------------------------------------------------
        // 2. MOTORCYCLES & BIKES (Dual Inline Wheels, Spoke Rims, Handlebars)
        // -----------------------------------------------------------------
        if (spec.getCategory() == VehicleCategory.MOTORCYCLE) {
            // Front Wheel + Rim
            parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(-0.1f, 0.0f, l*0.32f), new Vector3f(0.2f, 0.65f, 0.65f)));
            parts.add(createPart(world, baseLoc, Material.IRON_BLOCK, new Vector3f(-0.11f, 0.18f, l*0.32f + 0.18f), new Vector3f(0.22f, 0.3f, 0.3f)));
            // Rear Wheel + Rim
            parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(-0.1f, 0.0f, -l*0.32f), new Vector3f(0.2f, 0.65f, 0.65f)));
            parts.add(createPart(world, baseLoc, Material.IRON_BLOCK, new Vector3f(-0.11f, 0.18f, -l*0.32f + 0.18f), new Vector3f(0.22f, 0.3f, 0.3f)));
            // Slender Chassis / Engine
            parts.add(createPart(world, baseLoc, p, new Vector3f(-0.2f, 0.35f, -l*0.25f), new Vector3f(0.4f, 0.4f, l*0.55f)));
            parts.add(createPart(world, baseLoc, Material.POLISHED_BLACKSTONE_SLAB, new Vector3f(-0.18f, 0.65f, -0.2f), new Vector3f(0.36f, 0.15f, 0.45f)));
            // Handlebars & Headlight
            parts.add(createPart(world, baseLoc, Material.LIGHTNING_ROD, new Vector3f(-0.35f, 0.8f, l*0.20f), new Vector3f(0.7f, 0.1f, 0.1f)));
            parts.add(createPart(world, baseLoc, Material.SEA_LANTERN, new Vector3f(-0.12f, 0.55f, l*0.38f), new Vector3f(0.24f, 0.24f, 0.1f)));
            return parts;
        }

        // -----------------------------------------------------------------
        // 3. CARS, SUPERCARS & TRUCKS (Aerodynamic Multi-Layer + Glass Cabin)
        // -----------------------------------------------------------------
        // Lower Aerodynamic Splitter / Base
        parts.add(createPart(world, baseLoc, s, new Vector3f(-w/2f, 0.18f, -l/2f), new Vector3f(w, 0.12f, l)));
        // Main Body Core
        parts.add(createPart(world, baseLoc, p, new Vector3f(-w*0.48f, 0.30f, -l*0.48f), new Vector3f(w*0.96f, h*0.38f, l*0.96f)));
        // 3D Glass Cabin
        parts.add(createPart(world, baseLoc, g, new Vector3f(-w*0.42f, 0.58f, l*0.10f), new Vector3f(w*0.84f, h*0.48f, 0.15f)));
        parts.add(createPart(world, baseLoc, g, new Vector3f(-w*0.44f, 0.58f, -l*0.22f), new Vector3f(0.12f, h*0.48f, l*0.32f)));
        parts.add(createPart(world, baseLoc, g, new Vector3f(w*0.44f - 0.12f, 0.58f, -l*0.22f), new Vector3f(0.12f, h*0.48f, l*0.32f)));
        parts.add(createPart(world, baseLoc, g, new Vector3f(-w*0.42f, 0.58f, -l*0.23f), new Vector3f(w*0.84f, h*0.48f, 0.15f)));
        parts.add(createPart(world, baseLoc, p, new Vector3f(-w*0.42f, 0.58f + h*0.48f, -l*0.22f), new Vector3f(w*0.84f, 0.08f, l*0.32f)));

        // 4 Custom Wheels with Inner Metal Hub Rims
        float[] xSides = {-w/2f - 0.12f, w/2f - 0.24f};
        float[] zAxles = {l*0.24f, -l*0.28f};
        for (float x : xSides) {
            for (float z : zAxles) {
                parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(x, 0.0f, z), new Vector3f(0.36f, 0.68f, 0.68f)));
                float rimX = (x < 0) ? x - 0.02f : x + 0.32f;
                parts.add(createPart(world, baseLoc, Material.SMOOTH_STONE_SLAB, new Vector3f(rimX, 0.18f, z + 0.18f), new Vector3f(0.04f, 0.32f, 0.32f)));
            }
        }

        // Grille & Headlights
        parts.add(createPart(world, baseLoc, Material.BLACK_CONCRETE, new Vector3f(-w*0.32f, 0.32f, l/2f - 0.02f), new Vector3f(w*0.64f, 0.22f, 0.04f)));
        parts.add(createPart(world, baseLoc, Material.SEA_LANTERN, new Vector3f(-w*0.44f, 0.36f, l/2f - 0.02f), new Vector3f(0.24f, 0.18f, 0.04f)));
        parts.add(createPart(world, baseLoc, Material.SEA_LANTERN, new Vector3f(w*0.44f - 0.24f, 0.36f, l/2f - 0.02f), new Vector3f(0.24f, 0.18f, 0.04f)));

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
