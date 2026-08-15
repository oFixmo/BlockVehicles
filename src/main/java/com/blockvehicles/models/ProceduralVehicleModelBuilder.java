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
        Material primary = spec.getPrimaryMaterial();
        Material secondary = spec.getSecondaryMaterial();
        Material glass = spec.getGlassMaterial();

        // -------------------------------------------------------------
        // MOTORCYCLES & BIKES (NO GLASS - OPEN LEATHER SEAT & HANDLEBARS)
        // -------------------------------------------------------------
        if (spec.getCategory() == VehicleCategory.MOTORCYCLE) {
            // Front & Rear Thin Wheels
            parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(-0.1f, 0.0f, l * 0.32f), new Vector3f(0.2f, 0.65f, 0.65f)));
            parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(-0.1f, 0.0f, -l * 0.32f), new Vector3f(0.2f, 0.65f, 0.65f)));
            // Slender Bike Chassis / Fuel Tank
            parts.add(createPart(world, baseLoc, primary, new Vector3f(-0.25f, 0.35f, -l * 0.30f), new Vector3f(0.5f, 0.45f, l * 0.60f)));
            // Open Leather Driver Saddle
            parts.add(createPart(world, baseLoc, Material.BLACK_WOOL, new Vector3f(-0.2f, 0.55f, -0.25f), new Vector3f(0.4f, 0.2f, 0.45f)));
            // Metallic Handlebars
            parts.add(createPart(world, baseLoc, Material.LIGHTNING_ROD, new Vector3f(-0.4f, 0.75f, l * 0.22f), new Vector3f(0.8f, 0.1f, 0.15f)));
            // Headlight
            parts.add(createPart(world, baseLoc, Material.SEA_LANTERN, new Vector3f(-0.15f, 0.55f, l * 0.38f), new Vector3f(0.3f, 0.3f, 0.1f)));
            return parts;
        }

        // -------------------------------------------------------------
        // CARS, TRUCKS & HEAVY VEHICLES (GLASS CABIN + INTERIOR SEAT)
        // -------------------------------------------------------------
        // 1. Lower Chassis Floor
        parts.add(createPart(world, baseLoc, primary, new Vector3f(-w / 2f, 0.2f, -l / 2f), new Vector3f(w, 0.35f, l)));

        // 2. Interior Driver Chair (Cushion, Backrest & Steering Column)
        parts.add(createPart(world, baseLoc, Material.BLACK_WOOL, new Vector3f(-0.35f, 0.35f, -0.2f), new Vector3f(0.7f, 0.2f, 0.5f)));
        parts.add(createPart(world, baseLoc, Material.BLACK_CONCRETE, new Vector3f(-0.35f, 0.55f, -0.45f), new Vector3f(0.7f, 0.65f, 0.15f)));
        parts.add(createPart(world, baseLoc, Material.LEVER, new Vector3f(-0.15f, 0.55f, 0.15f), new Vector3f(0.3f, 0.3f, 0.1f)));

        // 3. Glass Windshields & Windows
        if (glass != Material.AIR) {
            parts.add(createPart(world, baseLoc, glass, new Vector3f(-w * 0.42f, 0.55f, l * 0.12f), new Vector3f(w * 0.84f, h * 0.55f, 0.15f)));
            parts.add(createPart(world, baseLoc, glass, new Vector3f(-w * 0.45f, 0.55f, -l * 0.22f), new Vector3f(0.12f, h * 0.55f, l * 0.35f)));
            parts.add(createPart(world, baseLoc, glass, new Vector3f(w * 0.45f - 0.12f, 0.55f, -l * 0.22f), new Vector3f(0.12f, h * 0.55f, l * 0.35f)));
            parts.add(createPart(world, baseLoc, glass, new Vector3f(-w * 0.42f, 0.55f, -l * 0.23f), new Vector3f(w * 0.84f, h * 0.55f, 0.15f)));
            parts.add(createPart(world, baseLoc, glass, new Vector3f(-w * 0.42f, 0.55f + h * 0.55f, -l * 0.22f), new Vector3f(w * 0.84f, 0.1f, l * 0.35f)));
        }

        // 4. Outer Body Hood & Trunk
        parts.add(createPart(world, baseLoc, primary, new Vector3f(-w / 2f, 0.45f, l * 0.15f), new Vector3f(w, h * 0.35f, l * 0.35f)));
        parts.add(createPart(world, baseLoc, primary, new Vector3f(-w / 2f, 0.45f, -l / 2f), new Vector3f(w, h * 0.35f, l * 0.28f)));

        // 5. Wheels or Wings
        if (spec.isFlying()) {
            parts.add(createPart(world, baseLoc, secondary, new Vector3f(-w * 1.6f, 0.6f, -l * 0.1f), new Vector3f(w * 3.2f, 0.15f, l * 0.35f)));
            parts.add(createPart(world, baseLoc, Material.IRON_BARS, new Vector3f(-w * 0.6f, h + 0.4f, 0.0f), new Vector3f(w * 1.2f, 0.1f, l * 0.7f)));
        } else {
            parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(-w / 2f - 0.15f, 0.0f, l * 0.24f), new Vector3f(0.35f, 0.65f, 0.65f)));
            parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(w / 2f - 0.20f, 0.0f, l * 0.24f), new Vector3f(0.35f, 0.65f, 0.65f)));
            parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(-w / 2f - 0.15f, 0.0f, -l * 0.30f), new Vector3f(0.35f, 0.65f, 0.65f)));
            parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(w / 2f - 0.20f, 0.0f, -l * 0.30f), new Vector3f(0.35f, 0.65f, 0.65f)));
        }

        // Headlights
        parts.add(createPart(world, baseLoc, Material.SEA_LANTERN, new Vector3f(-w * 0.4f, 0.35f, l / 2f - 0.05f), new Vector3f(0.25f, 0.25f, 0.1f)));
        parts.add(createPart(world, baseLoc, Material.SEA_LANTERN, new Vector3f(w * 0.4f - 0.25f, 0.35f, l / 2f - 0.05f), new Vector3f(0.25f, 0.25f, 0.1f)));

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
