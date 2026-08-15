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

        // 1. Lower Chassis Floor
        parts.add(createPart(world, baseLoc, primary, new Vector3f(-w / 2f, 0.2f, -l / 2f), new Vector3f(w, 0.35f, l)));

        // 2. 3D Glass Cabin Enclosure around the driver seat
        // Front Windshield (Angled Glass)
        parts.add(createPart(world, baseLoc, glass, new Vector3f(-w * 0.42f, 0.55f, l * 0.12f), new Vector3f(w * 0.84f, h * 0.55f, 0.15f)));
        // Left Side Window
        parts.add(createPart(world, baseLoc, glass, new Vector3f(-w * 0.45f, 0.55f, -l * 0.22f), new Vector3f(0.12f, h * 0.55f, l * 0.35f)));
        // Right Side Window
        parts.add(createPart(world, baseLoc, glass, new Vector3f(w * 0.45f - 0.12f, 0.55f, -l * 0.22f), new Vector3f(0.12f, h * 0.55f, l * 0.35f)));
        // Rear Windshield
        parts.add(createPart(world, baseLoc, glass, new Vector3f(-w * 0.42f, 0.55f, -l * 0.23f), new Vector3f(w * 0.84f, h * 0.55f, 0.15f)));
        // Glass Sunroof / Canopy
        parts.add(createPart(world, baseLoc, glass, new Vector3f(-w * 0.42f, 0.55f + h * 0.55f, -l * 0.22f), new Vector3f(w * 0.84f, 0.1f, l * 0.35f)));

        // 3. Vehicle Hood & Trunk Bodies
        parts.add(createPart(world, baseLoc, primary, new Vector3f(-w / 2f, 0.45f, l * 0.15f), new Vector3f(w, h * 0.35f, l * 0.35f)));
        parts.add(createPart(world, baseLoc, primary, new Vector3f(-w / 2f, 0.45f, -l / 2f), new Vector3f(w, h * 0.35f, l * 0.28f)));

        // 4. Wheels or Aircraft Wings
        if (spec.isFlying()) {
            parts.add(createPart(world, baseLoc, secondary, new Vector3f(-w * 1.6f, 0.6f, -l * 0.1f), new Vector3f(w * 3.2f, 0.15f, l * 0.35f)));
            parts.add(createPart(world, baseLoc, Material.IRON_BARS, new Vector3f(-w * 0.6f, h + 0.4f, 0.0f), new Vector3f(w * 1.2f, 0.1f, l * 0.7f)));
        } else {
            // 4 Ground Tires
            parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(-w / 2f - 0.15f, 0.0f, l * 0.24f), new Vector3f(0.35f, 0.65f, 0.65f)));
            parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(w / 2f - 0.20f, 0.0f, l * 0.24f), new Vector3f(0.35f, 0.65f, 0.65f)));
            parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(-w / 2f - 0.15f, 0.0f, -l * 0.30f), new Vector3f(0.35f, 0.65f, 0.65f)));
            parts.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(w / 2f - 0.20f, 0.0f, -l * 0.30f), new Vector3f(0.35f, 0.65f, 0.65f)));
        }

        // 5. Specialized Multi-Size Drill Heads
        if (spec.canDrill()) {
            switch (spec.getDrillMode()) {
                case SHAFT_DOWN_3X3, SHAFT_DOWN_5X5 -> {
                    // Downward Heavy Core Drill
                    parts.add(createPart(world, baseLoc, Material.DIAMOND_BLOCK, new Vector3f(-0.7f, -0.4f, -0.7f), new Vector3f(1.4f, 0.6f, 1.4f)));
                    parts.add(createPart(world, baseLoc, Material.ANVIL, new Vector3f(-0.5f, -0.8f, -0.5f), new Vector3f(1.0f, 0.5f, 1.0f)));
                }
                case MEGA_5X5 -> {
                    // 5x5 Massive Rotary Shield
                    parts.add(createPart(world, baseLoc, Material.NETHERITE_BLOCK, new Vector3f(-2.2f, 0.2f, l / 2f), new Vector3f(4.4f, 4.4f, 1.2f)));
                    parts.add(createPart(world, baseLoc, Material.DIAMOND_BLOCK, new Vector3f(-1.5f, 0.9f, l / 2f + 1.2f), new Vector3f(3.0f, 3.0f, 0.8f)));
                }
                default -> {
                    // 2x2 or 3x3 Tunnel Drill Head
                    parts.add(createPart(world, baseLoc, Material.DIAMOND_BLOCK, new Vector3f(-0.6f, 0.3f, l / 2f), new Vector3f(1.2f, 1.2f, 1.2f)));
                    parts.add(createPart(world, baseLoc, Material.ANVIL, new Vector3f(-0.4f, 0.4f, l / 2f + 1.2f), new Vector3f(0.8f, 0.8f, 0.6f)));
                }
            }
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
