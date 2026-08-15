package com.blockvehicles.models;

import com.blockvehicles.vehicle.VehicleType;
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

public class VehicleModelBuilder {

    public static List<BlockDisplay> buildModel(World world, Location baseLoc, VehicleType type) {
        List<BlockDisplay> displays = new ArrayList<>();
        switch (type) {
            case CAR -> {
                displays.add(createPart(world, baseLoc, Material.RED_CONCRETE, new Vector3f(-0.75f, 0.2f, -1.2f), new Vector3f(1.5f, 0.5f, 2.4f)));
                displays.add(createPart(world, baseLoc, Material.TINTED_GLASS, new Vector3f(-0.6f, 0.7f, -0.4f), new Vector3f(1.2f, 0.6f, 1.0f)));
                displays.add(createPart(world, baseLoc, Material.SMOOTH_STONE_SLAB, new Vector3f(-0.6f, 1.3f, -0.4f), new Vector3f(1.2f, 0.1f, 1.0f)));
                displays.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(-0.9f, 0.0f, 0.6f), new Vector3f(0.3f, 0.6f, 0.6f)));
                displays.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(0.6f, 0.0f, 0.6f), new Vector3f(0.3f, 0.6f, 0.6f)));
                displays.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(-0.9f, 0.0f, -0.9f), new Vector3f(0.3f, 0.6f, 0.6f)));
                displays.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(0.6f, 0.0f, -0.9f), new Vector3f(0.3f, 0.6f, 0.6f)));
                displays.add(createPart(world, baseLoc, Material.SEA_LANTERN, new Vector3f(-0.65f, 0.35f, 1.15f), new Vector3f(0.3f, 0.3f, 0.1f)));
                displays.add(createPart(world, baseLoc, Material.SEA_LANTERN, new Vector3f(0.35f, 0.35f, 1.15f), new Vector3f(0.3f, 0.3f, 0.1f)));
            }
            case TRUCK -> {
                displays.add(createPart(world, baseLoc, Material.BLUE_TERRACOTTA, new Vector3f(-0.9f, 0.2f, 0.4f), new Vector3f(1.8f, 1.4f, 1.2f)));
                displays.add(createPart(world, baseLoc, Material.IRON_BLOCK, new Vector3f(-0.9f, 0.2f, -2.2f), new Vector3f(1.8f, 1.0f, 2.5f)));
                displays.add(createPart(world, baseLoc, Material.GLASS, new Vector3f(-0.8f, 0.9f, 0.9f), new Vector3f(1.6f, 0.6f, 0.6f)));
                displays.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(-1.1f, 0.0f, 0.8f), new Vector3f(0.4f, 0.7f, 0.7f)));
                displays.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(0.7f, 0.0f, 0.8f), new Vector3f(0.4f, 0.7f, 0.7f)));
                displays.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(-1.1f, 0.0f, -0.7f), new Vector3f(0.4f, 0.7f, 0.7f)));
                displays.add(createPart(world, baseLoc, Material.COAL_BLOCK, new Vector3f(0.7f, 0.0f, -0.7f), new Vector3f(0.4f, 0.7f, 0.7f)));
            }
            case DRILLING_MACHINE -> {
                displays.add(createPart(world, baseLoc, Material.GRAY_CONCRETE, new Vector3f(-0.9f, 0.2f, -1.0f), new Vector3f(1.8f, 0.9f, 2.0f)));
                displays.add(createPart(world, baseLoc, Material.DIAMOND_BLOCK, new Vector3f(-0.45f, 0.1f, 1.1f), new Vector3f(0.9f, 0.9f, 0.9f)));
                displays.add(createPart(world, baseLoc, Material.YELLOW_STAINED_GLASS, new Vector3f(-0.6f, 1.1f, -0.3f), new Vector3f(1.2f, 0.8f, 0.9f)));
                displays.add(createPart(world, baseLoc, Material.OBSIDIAN, new Vector3f(-1.1f, 0.0f, -1.2f), new Vector3f(0.3f, 0.5f, 2.4f)));
                displays.add(createPart(world, baseLoc, Material.OBSIDIAN, new Vector3f(0.8f, 0.0f, -1.2f), new Vector3f(0.3f, 0.5f, 2.4f)));
            }
            case PLANE -> {
                displays.add(createPart(world, baseLoc, Material.WHITE_CONCRETE, new Vector3f(-0.5f, 0.2f, -1.8f), new Vector3f(1.0f, 0.8f, 3.6f)));
                displays.add(createPart(world, baseLoc, Material.SMOOTH_QUARTZ_SLAB, new Vector3f(-3.0f, 0.4f, 0.2f), new Vector3f(6.0f, 0.1f, 1.2f)));
                displays.add(createPart(world, baseLoc, Material.RED_CONCRETE, new Vector3f(-0.1f, 1.0f, -1.7f), new Vector3f(0.2f, 1.0f, 0.8f)));
                displays.add(createPart(world, baseLoc, Material.IRON_BARS, new Vector3f(-0.7f, 0.1f, 1.9f), new Vector3f(1.4f, 1.0f, 0.1f)));
            }
            case HELICOPTER -> {
                displays.add(createPart(world, baseLoc, Material.BLACK_CONCRETE, new Vector3f(-0.75f, 0.3f, -0.6f), new Vector3f(1.5f, 1.2f, 1.8f)));
                displays.add(createPart(world, baseLoc, Material.CYAN_STAINED_GLASS, new Vector3f(-0.65f, 0.6f, 0.4f), new Vector3f(1.3f, 0.8f, 0.7f)));
                displays.add(createPart(world, baseLoc, Material.IRON_BLOCK, new Vector3f(-0.2f, 0.6f, -2.4f), new Vector3f(0.4f, 0.4f, 1.9f)));
                displays.add(createPart(world, baseLoc, Material.IRON_BARS, new Vector3f(-2.2f, 1.7f, -0.1f), new Vector3f(4.4f, 0.1f, 0.3f)));
            }
            default -> displays.add(createPart(world, baseLoc, Material.WHITE_CONCRETE, new Vector3f(-0.5f, 0.0f, -0.5f), new Vector3f(1.0f, 1.0f, 1.0f)));
        }
        return displays;
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
