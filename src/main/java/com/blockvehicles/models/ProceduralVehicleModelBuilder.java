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
