mkdir -p src/main/resources src/main/java/com/blockvehicles/{items,models,vehicle,physics,gui,listeners,commands}

cat << 'INNER_EOF' > pom.xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.blockvehicles</groupId>
    <artifactId>BlockVehicles</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    <name>BlockVehicles</name>
    <properties>
        <java.version>17</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    <repositories>
        <repository>
            <id>papermc</id>
            <url>https://repo.papermc.io/repository/maven-public/</url>
        </repository>
    </repositories>
    <dependencies>
        <dependency>
            <groupId>io.papermc.paper</groupId>
            <artifactId>paper-api</artifactId>
            <version>1.20.4-R0.1-SNAPSHOT</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>${java.version}</source>
                    <target>${java.version}</target>
                </configuration>
            </plugin>
        </plugins>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <filtering>true</filtering>
            </resource>
        </resources>
    </build>
</project>
INNER_EOF

cat << 'INNER_EOF' > src/main/resources/plugin.yml
name: BlockVehicles
version: 1.0.0
main: com.blockvehicles.BlockVehiclesPlugin
api-version: '1.20'
author: Assistant
commands:
  vehicle:
    description: Main vehicle command
    aliases: [vehicles, v]
    permission: blockvehicles.use
permissions:
  blockvehicles.use:
    default: true
  blockvehicles.admin:
    default: op
INNER_EOF

cat << 'INNER_EOF' > src/main/resources/config.yml
fuel_rates:
  COAL: 25.0
  CHARCOAL: 25.0
  COAL_BLOCK: 250.0
  LAVA_BUCKET: 500.0
  REDSTONE: 15.0
  REDSTONE_BLOCK: 150.0
  BATTERY: 300.0

solar:
  recharge_per_second: 1.5
  requires_direct_sunlight: true

vehicles:
  CAR:
    speed: 0.45
    max_fuel: 500.0
    fuel_burn_per_tick: 0.1
    storage_rows: 2
  TRUCK:
    speed: 0.32
    max_fuel: 1000.0
    fuel_burn_per_tick: 0.15
    storage_rows: 6
  VAN:
    speed: 0.38
    max_fuel: 750.0
    fuel_burn_per_tick: 0.12
    storage_rows: 3
  DRILLING_MACHINE:
    speed: 0.25
    max_fuel: 1200.0
    fuel_burn_per_tick: 0.25
    storage_rows: 6
    drill_radius: 1
  PLANE:
    speed: 0.70
    max_fuel: 800.0
    fuel_burn_per_tick: 0.18
    storage_rows: 2
  HELICOPTER:
    speed: 0.50
    lift_speed: 0.25
    max_fuel: 800.0
    fuel_burn_per_tick: 0.20
    storage_rows: 2
INNER_EOF

cat << 'INNER_EOF' > src/main/java/com/blockvehicles/BlockVehiclesPlugin.java
package com.blockvehicles;

import com.blockvehicles.commands.VehicleCommand;
import com.blockvehicles.items.CustomItems;
import com.blockvehicles.listeners.VehicleListener;
import com.blockvehicles.physics.VehicleControllerTask;
import com.blockvehicles.vehicle.VehicleManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockVehiclesPlugin extends JavaPlugin {
    private static BlockVehiclesPlugin instance;
    private CustomItems customItems;
    private VehicleManager vehicleManager;
    private VehicleControllerTask controllerTask;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.customItems = new CustomItems(this);
        this.customItems.registerRecipes();
        this.vehicleManager = new VehicleManager(this);
        getServer().getPluginManager().registerEvents(new VehicleListener(this), this);
        getCommand("vehicle").setExecutor(new VehicleCommand(this));
        this.controllerTask = new VehicleControllerTask(this);
        this.controllerTask.runTaskTimer(this, 1L, 1L);
    }

    @Override
    public void onDisable() {
        if (this.vehicleManager != null) {
            this.vehicleManager.despawnAll();
        }
    }

    public static BlockVehiclesPlugin getInstance() { return instance; }
    public CustomItems getCustomItems() { return customItems; }
    public VehicleManager getVehicleManager() { return vehicleManager; }
}
INNER_EOF

cat << 'INNER_EOF' > src/main/java/com/blockvehicles/items/CustomItems.java
package com.blockvehicles.items;

import com.blockvehicles.BlockVehiclesPlugin;
import com.blockvehicles.vehicle.VehicleType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import java.util.Arrays;
import java.util.List;

public class CustomItems {
    private final BlockVehiclesPlugin plugin;
    public final NamespacedKey ITEM_KEY;
    public final NamespacedKey VEHICLE_KEY;

    public CustomItems(BlockVehiclesPlugin plugin) {
        this.plugin = plugin;
        this.ITEM_KEY = new NamespacedKey(plugin, "custom_item_id");
        this.VEHICLE_KEY = new NamespacedKey(plugin, "vehicle_type");
    }

    public ItemStack createItem(Material mat, String name, String customId, String... lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + name);
            List<String> loreList = Arrays.stream(lore).map(l -> ChatColor.GRAY + l).toList();
            meta.setLore(loreList);
            meta.getPersistentDataContainer().set(ITEM_KEY, PersistentDataType.STRING, customId);
            item.setItemMeta(meta);
        }
        return item;
    }

    public ItemStack createVehicleKey(VehicleType type) {
        ItemStack item = new ItemStack(type.getIcon());
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + type.getDisplayName() + " Key");
            meta.setLore(Arrays.asList(
                    ChatColor.YELLOW + "Place on the ground to deploy your vehicle.",
                    ChatColor.GRAY + "Type: " + type.name(),
                    ChatColor.DARK_GRAY + "No resource pack required!"
            ));
            meta.getPersistentDataContainer().set(VEHICLE_KEY, PersistentDataType.STRING, type.name());
            item.setItemMeta(meta);
        }
        return item;
    }

    public void registerRecipes() {
        ItemStack engine = createItem(Material.PISTON, "Combustion Engine", "ENGINE", "Provides drive power");
        ShapedRecipe rEngine = new ShapedRecipe(new NamespacedKey(plugin, "engine_recipe"), engine);
        rEngine.shape("III", "PRP", "III");
        rEngine.setIngredient('I', Material.IRON_INGOT);
        rEngine.setIngredient('P', Material.PISTON);
        rEngine.setIngredient('R', Material.REDSTONE_BLOCK);
        plugin.getServer().addRecipe(rEngine);

        ItemStack wheel = createItem(Material.COAL_BLOCK, "Vehicle Wheel", "WHEEL", "Reinforced tire");
        ShapedRecipe rWheel = new ShapedRecipe(new NamespacedKey(plugin, "wheel_recipe"), wheel);
        rWheel.shape(" L ", "LCL", " L ");
        rWheel.setIngredient('L', Material.LEATHER);
        rWheel.setIngredient('C', Material.COAL_BLOCK);
        plugin.getServer().addRecipe(rWheel);

        ItemStack battery = createItem(Material.COPPER_BLOCK, "Lithium Battery", "BATTERY", "High capacity electric cell");
        ShapedRecipe rBattery = new ShapedRecipe(new NamespacedKey(plugin, "battery_recipe"), battery);
        rBattery.shape(" C ", "RGR", " C ");
        rBattery.setIngredient('C', Material.COPPER_INGOT);
        rBattery.setIngredient('R', Material.REDSTONE);
        rBattery.setIngredient('G', Material.GOLD_INGOT);
        plugin.getServer().addRecipe(rBattery);

        ItemStack solar = createItem(Material.DAYLIGHT_DETECTOR, "Vehicle Solar Panel", "SOLAR_PANEL", "Recharges under sunlight");
        ShapedRecipe rSolar = new ShapedRecipe(new NamespacedKey(plugin, "solar_recipe"), solar);
        rSolar.shape("GGG", "QQQ", "RRR");
        rSolar.setIngredient('G', Material.GLASS);
        rSolar.setIngredient('Q', Material.QUARTZ);
        rSolar.setIngredient('R', Material.REDSTONE);
        plugin.getServer().addRecipe(rSolar);

        ItemStack drill = createItem(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE, "Industrial Drill Head", "DRILL_HEAD", "Excavates blocks");
        ShapedRecipe rDrill = new ShapedRecipe(new NamespacedKey(plugin, "drill_recipe"), drill);
        rDrill.shape(" D ", "DID", " I ");
        rDrill.setIngredient('D', Material.DIAMOND);
        rDrill.setIngredient('I', Material.IRON_BLOCK);
        plugin.getServer().addRecipe(rDrill);

        ItemStack carKey = createVehicleKey(VehicleType.CAR);
        ShapedRecipe rCar = new ShapedRecipe(new NamespacedKey(plugin, "car_recipe"), carKey);
        rCar.shape("IGI", "E C", "W W");
        rCar.setIngredient('I', Material.IRON_BLOCK);
        rCar.setIngredient('G', Material.GLASS);
        rCar.setIngredient('E', Material.PISTON);
        rCar.setIngredient('C', Material.CHEST);
        rCar.setIngredient('W', Material.COAL_BLOCK);
        plugin.getServer().addRecipe(rCar);

        ItemStack drillKey = createVehicleKey(VehicleType.DRILLING_MACHINE);
        ShapedRecipe rDrillMachine = new ShapedRecipe(new NamespacedKey(plugin, "drilling_machine_recipe"), drillKey);
        rDrillMachine.shape("IDI", "ECE", "WWW");
        rDrillMachine.setIngredient('I', Material.IRON_BLOCK);
        rDrillMachine.setIngredient('D', Material.DIAMOND_BLOCK);
        rDrillMachine.setIngredient('E', Material.REDSTONE_BLOCK);
        rDrillMachine.setIngredient('C', Material.CHEST);
        rDrillMachine.setIngredient('W', Material.COAL_BLOCK);
        plugin.getServer().addRecipe(rDrillMachine);
    }
}
INNER_EOF

cat << 'INNER_EOF' > src/main/java/com/blockvehicles/vehicle/VehicleType.java
package com.blockvehicles.vehicle;

import org.bukkit.Material;

public enum VehicleType {
    CAR("Car", Material.MINECART, false, false, false),
    TRUCK("Cargo Truck", Material.CHEST_MINECART, false, false, false),
    VAN("Passenger Van", Material.MINECART, false, false, false),
    DRILLING_MACHINE("Drilling Machine", Material.HOPPER_MINECART, false, true, false),
    PLANE("Airplane", Material.FEATHER, true, false, false),
    HELICOPTER("Helicopter", Material.COMPASS, true, false, true);

    private final String displayName;
    private final Material icon;
    private final boolean flying;
    private final boolean canDrill;
    private final boolean canHover;

    VehicleType(String displayName, Material icon, boolean flying, boolean canDrill, boolean canHover) {
        this.displayName = displayName;
        this.icon = icon;
        this.flying = flying;
        this.canDrill = canDrill;
        this.canHover = canHover;
    }

    public String getDisplayName() { return displayName; }
    public Material getIcon() { return icon; }
    public boolean isFlying() { return flying; }
    public boolean canDrill() { return canDrill; }
    public boolean canHover() { return canHover; }
}
INNER_EOF

cat << 'INNER_EOF' > src/main/java/com/blockvehicles/models/VehicleModelBuilder.java
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
INNER_EOF

cat << 'INNER_EOF' > src/main/java/com/blockvehicles/vehicle/VehicleInstance.java
package com.blockvehicles.vehicle;

import com.blockvehicles.BlockVehiclesPlugin;
import com.blockvehicles.models.VehicleModelBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.UUID;

public class VehicleInstance {
    private final UUID id;
    private final VehicleType type;
    private final UUID ownerUUID;
    private final ArmorStand seatEntity;
    private final List<BlockDisplay> modelParts;
    private final Inventory trunk;
    private double fuel;
    private final double maxFuel;
    private final double speed;
    private boolean locked = false;

    public VehicleInstance(BlockVehiclesPlugin plugin, Location spawnLoc, VehicleType type, UUID ownerUUID) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.ownerUUID = ownerUUID;
        this.maxFuel = plugin.getConfig().getDouble("vehicles." + type.name() + ".max_fuel", 500.0);
        this.fuel = this.maxFuel * 0.5;
        this.speed = plugin.getConfig().getDouble("vehicles." + type.name() + ".speed", 0.4);

        int rows = plugin.getConfig().getInt("vehicles." + type.name() + ".storage_rows", 2);
        this.trunk = Bukkit.createInventory(null, rows * 9, type.getDisplayName() + " Trunk");

        this.seatEntity = (ArmorStand) spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.ARMOR_STAND);
        this.seatEntity.setVisible(false);
        this.seatEntity.setGravity(false);
        this.seatEntity.setSmall(true);
        this.seatEntity.setMarker(true);
        this.seatEntity.setInvulnerable(true);

        this.modelParts = VehicleModelBuilder.buildModel(spawnLoc.getWorld(), spawnLoc, type);
    }

    public void updateModelPositions() {
        Location seatLoc = seatEntity.getLocation();
        for (BlockDisplay part : modelParts) {
            part.teleport(seatLoc);
        }
    }

    public void remove() {
        if (seatEntity != null && !seatEntity.isDead()) seatEntity.remove();
        for (BlockDisplay part : modelParts) {
            if (part != null && !part.isDead()) part.remove();
        }
    }

    public Player getDriver() {
        if (seatEntity.getPassengers().isEmpty()) return null;
        if (seatEntity.getPassengers().get(0) instanceof Player p) return p;
        return null;
    }

    public UUID getId() { return id; }
    public VehicleType getType() { return type; }
    public UUID getOwnerUUID() { return ownerUUID; }
    public ArmorStand getSeatEntity() { return seatEntity; }
    public Inventory getTrunk() { return trunk; }
    public double getFuel() { return fuel; }
    public void setFuel(double fuel) { this.fuel = Math.min(maxFuel, Math.max(0, fuel)); }
    public double getMaxFuel() { return maxFuel; }
    public double getSpeed() { return speed; }
    public boolean isLocked() { return locked; }
    public void setLocked(boolean locked) { this.locked = locked; }
}
INNER_EOF

cat << 'INNER_EOF' > src/main/java/com/blockvehicles/vehicle/VehicleManager.java
package com.blockvehicles.vehicle;

import com.blockvehicles.BlockVehiclesPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.*;

public class VehicleManager {
    private final BlockVehiclesPlugin plugin;
    private final Map<UUID, VehicleInstance> activeVehicles = new HashMap<>();

    public VehicleManager(BlockVehiclesPlugin plugin) {
        this.plugin = plugin;
    }

    public VehicleInstance spawnVehicle(Location loc, VehicleType type, UUID owner) {
        VehicleInstance vehicle = new VehicleInstance(plugin, loc, type, owner);
        activeVehicles.put(vehicle.getId(), vehicle);
        return vehicle;
    }

    public VehicleInstance getBySeat(Entity seat) {
        for (VehicleInstance v : activeVehicles.values()) {
            if (v.getSeatEntity().getUniqueId().equals(seat.getUniqueId())) {
                return v;
            }
        }
        return null;
    }

    public void removeVehicle(UUID id) {
        VehicleInstance v = activeVehicles.remove(id);
        if (v != null) v.remove();
    }

    public void despawnAll() {
        for (VehicleInstance v : activeVehicles.values()) v.remove();
        activeVehicles.clear();
    }

    public Collection<VehicleInstance> getActiveVehicles() {
        return activeVehicles.values();
    }
}
INNER_EOF

cat << 'INNER_EOF' > src/main/java/com/blockvehicles/physics/VehicleControllerTask.java
package com.blockvehicles.physics;

import com.blockvehicles.BlockVehiclesPlugin;
import com.blockvehicles.vehicle.VehicleInstance;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collection;

public class VehicleControllerTask extends BukkitRunnable {
    private final BlockVehiclesPlugin plugin;

    public VehicleControllerTask(BlockVehiclesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (VehicleInstance v : plugin.getVehicleManager().getActiveVehicles()) {
            handleVehicleTick(v);
        }
    }

    private void handleVehicleTick(VehicleInstance vehicle) {
        Player driver = vehicle.getDriver();
        Location currentLoc = vehicle.getSeatEntity().getLocation();

        if (currentLoc.getWorld().isDayTime() && currentLoc.getBlock().getLightFromSky() == 15) {
            double solarRate = plugin.getConfig().getDouble("solar.recharge_per_second", 1.0) / 20.0;
            vehicle.setFuel(vehicle.getFuel() + solarRate);
        }

        if (driver == null) {
            vehicle.updateModelPositions();
            return;
        }

        if (vehicle.getFuel() <= 0) {
            driver.sendActionBar(ChatColor.RED + "⚠ Tank Empty! Shift + Right-Click to refuel.");
            vehicle.updateModelPositions();
            return;
        }

        Vector lookDir = driver.getLocation().getDirection();
        Vector moveVector = new Vector(0, 0, 0);
        double speed = vehicle.getSpeed();
        double burnRate = plugin.getConfig().getDouble("vehicles." + vehicle.getType().name() + ".fuel_burn_per_tick", 0.1);

        if (vehicle.getType().isFlying()) {
            if (vehicle.getType().canHover()) {
                if (driver.isSneaking()) moveVector.setY(-0.25);
                else if (driver.getLocation().getPitch() < -15) moveVector.setY(0.25);
                Vector horizontal = lookDir.clone().setY(0).normalize().multiply(speed);
                moveVector.add(horizontal);
            } else {
                moveVector = lookDir.clone().multiply(speed);
            }
        } else {
            Vector horizontal = lookDir.clone().setY(0).normalize().multiply(speed);
            moveVector.add(horizontal);

            Location frontCheck = currentLoc.clone().add(horizontal.clone().normalize().multiply(0.8));
            if (frontCheck.getBlock().getType().isSolid()) {
                Location oneUp = frontCheck.clone().add(0, 1, 0);
                if (!oneUp.getBlock().getType().isSolid()) moveVector.setY(0.4);
            } else {
                Location below = currentLoc.clone().subtract(0, 0.5, 0);
                if (!below.getBlock().getType().isSolid()) moveVector.setY(-0.35);
            }

            if (vehicle.getType().canDrill()) {
                performDrilling(currentLoc, lookDir, vehicle);
            }
        }

        Location newLoc = currentLoc.clone().add(moveVector);
        newLoc.setYaw(driver.getLocation().getYaw());
        newLoc.setPitch(vehicle.getType().isFlying() ? driver.getLocation().getPitch() : 0);

        vehicle.getSeatEntity().teleport(newLoc);
        vehicle.updateModelPositions();

        vehicle.setFuel(vehicle.getFuel() - burnRate);

        int fuelPercent = (int) ((vehicle.getFuel() / vehicle.getMaxFuel()) * 100);
        ChatColor color = fuelPercent > 50 ? ChatColor.GREEN : (fuelPercent > 20 ? ChatColor.YELLOW : ChatColor.RED);
        driver.sendActionBar(ChatColor.WHITE + vehicle.getType().getDisplayName() + " | Fuel: " + color + fuelPercent + "%");
    }

    private void performDrilling(Location loc, Vector dir, VehicleInstance vehicle) {
        Location drillPoint = loc.clone().add(dir.setY(0).normalize().multiply(1.5)).add(0, 0.5, 0);
        World world = loc.getWorld();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Block block = drillPoint.clone().add(x, y, z).getBlock();
                    Material mat = block.getType();
                    if (mat != Material.AIR && mat != Material.BEDROCK && mat != Material.BARRIER && mat.getHardness() >= 0) {
                        Collection<ItemStack> drops = block.getDrops();
                        for (ItemStack drop : drops) {
                            vehicle.getTrunk().addItem(drop);
                        }
                        block.setType(Material.AIR);
                        world.spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(0.5, 0.5, 0.5), 10, mat.createBlockData());
                        world.playSound(block.getLocation(), Sound.BLOCK_STONE_BREAK, 0.5f, 1.0f);
                    }
                }
            }
        }
    }
}
INNER_EOF

cat << 'INNER_EOF' > src/main/java/com/blockvehicles/gui/VehicleGUI.java
package com.blockvehicles.gui;

import com.blockvehicles.vehicle.VehicleInstance;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;

public class VehicleGUI {
    public static final String GUI_TITLE = ChatColor.DARK_BLUE + "Vehicle Control Dashboard";

    public static void openDashboard(Player player, VehicleInstance vehicle) {
        Inventory inv = Bukkit.createInventory(null, 27, GUI_TITLE);

        ItemStack trunkBtn = new ItemStack(Material.CHEST);
        ItemMeta trunkMeta = trunkBtn.getItemMeta();
        trunkMeta.setDisplayName(ChatColor.GOLD + "Open Trunk Storage");
        trunkBtn.setItemMeta(trunkMeta);
        inv.setItem(11, trunkBtn);

        int fuelPercent = (int) ((vehicle.getFuel() / vehicle.getMaxFuel()) * 100);
        ItemStack fuelBtn = new ItemStack(Material.BLAZE_POWDER);
        ItemMeta fuelMeta = fuelBtn.getItemMeta();
        fuelMeta.setDisplayName(ChatColor.YELLOW + "Fuel Level: " + fuelPercent + "%");
        fuelMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Current: " + String.format("%.1f", vehicle.getFuel()) + " / " + vehicle.getMaxFuel(),
                ChatColor.AQUA + "Click with Coal, Lava, Redstone or",
                ChatColor.AQUA + "Battery to refuel."
        ));
        fuelBtn.setItemMeta(fuelMeta);
        inv.setItem(13, fuelBtn);

        ItemStack lockBtn = new ItemStack(vehicle.isLocked() ? Material.IRON_DOOR : Material.OAK_DOOR);
        ItemMeta lockMeta = lockBtn.getItemMeta();
        lockMeta.setDisplayName(ChatColor.GREEN + "Lock Status: " + (vehicle.isLocked() ? ChatColor.RED + "LOCKED" : ChatColor.GREEN + "UNLOCKED"));
        lockBtn.setItemMeta(lockMeta);
        inv.setItem(15, lockBtn);

        ItemStack pickupBtn = new ItemStack(Material.BARRIER);
        ItemMeta pMeta = pickupBtn.getItemMeta();
        pMeta.setDisplayName(ChatColor.RED + "Dismantle & Pick Up");
        pickupBtn.setItemMeta(pMeta);
        inv.setItem(26, pickupBtn);

        player.openInventory(inv);
    }
}
INNER_EOF

cat << 'INNER_EOF' > src/main/java/com/blockvehicles/listeners/VehicleListener.java
package com.blockvehicles.listeners;

import com.blockvehicles.BlockVehiclesPlugin;
import com.blockvehicles.gui.VehicleGUI;
import com.blockvehicles.vehicle.VehicleInstance;
import com.blockvehicles.vehicle.VehicleType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class VehicleListener implements Listener {
    private final BlockVehiclesPlugin plugin;

    public VehicleListener(BlockVehiclesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeployVehicle(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        ItemStack item = event.getItem();
        if (item == null || !item.hasItemMeta()) return;

        String typeStr = item.getItemMeta().getPersistentDataContainer().get(plugin.getCustomItems().VEHICLE_KEY, PersistentDataType.STRING);
        if (typeStr == null) return;

        event.setCancelled(true);
        VehicleType type = VehicleType.valueOf(typeStr);
        plugin.getVehicleManager().spawnVehicle(event.getClickedBlock().getLocation().add(0.5, 1, 0.5), type, event.getPlayer().getUniqueId());
        item.setAmount(item.getAmount() - 1);
        event.getPlayer().sendMessage(ChatColor.GREEN + "Deployed " + type.getDisplayName() + "!");
    }

    @EventHandler
    public void onInteractVehicle(PlayerInteractAtEntityEvent event) {
        Entity entity = event.getRightClicked();
        VehicleInstance vehicle = null;

        if (entity instanceof ArmorStand) {
            vehicle = plugin.getVehicleManager().getBySeat(entity);
        } else if (entity instanceof BlockDisplay) {
            for (VehicleInstance v : plugin.getVehicleManager().getActiveVehicles()) {
                if (v.getSeatEntity().getLocation().distanceSquared(entity.getLocation()) < 9.0) {
                    vehicle = v;
                    break;
                }
            }
        }

        if (vehicle == null) return;
        event.setCancelled(true);
        Player player = event.getPlayer();

        if (player.isSneaking()) {
            VehicleGUI.openDashboard(player, vehicle);
        } else {
            if (vehicle.isLocked() && !vehicle.getOwnerUUID().equals(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "This vehicle is locked by its owner!");
                return;
            }
            vehicle.getSeatEntity().addPassenger(player);
        }
    }

    @EventHandler
    public void onGuiClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(VehicleGUI.GUI_TITLE)) return;
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();

        VehicleInstance vehicle = plugin.getVehicleManager().getActiveVehicles().stream()
                .filter(v -> v.getSeatEntity().getLocation().distanceSquared(player.getLocation()) < 16.0)
                .findFirst().orElse(null);

        if (vehicle == null) return;

        if (slot == 11) {
            player.openInventory(vehicle.getTrunk());
        } else if (slot == 13) {
            ItemStack cursor = event.getCursor();
            if (cursor != null && cursor.getType() != Material.AIR) {
                double fuelAmount = plugin.getConfig().getDouble("fuel_rates." + cursor.getType().name(), 0.0);
                if (fuelAmount > 0) {
                    vehicle.setFuel(vehicle.getFuel() + fuelAmount);
                    cursor.setAmount(cursor.getAmount() - 1);
                    player.sendMessage(ChatColor.GREEN + "Added " + fuelAmount + " fuel!");
                    VehicleGUI.openDashboard(player, vehicle);
                }
            }
        } else if (slot == 15) {
            if (vehicle.getOwnerUUID().equals(player.getUniqueId())) {
                vehicle.setLocked(!vehicle.isLocked());
                VehicleGUI.openDashboard(player, vehicle);
            }
        } else if (slot == 26) {
            plugin.getVehicleManager().removeVehicle(vehicle.getId());
            player.getInventory().addItem(plugin.getCustomItems().createVehicleKey(vehicle.getType()));
            player.closeInventory();
            player.sendMessage(ChatColor.YELLOW + "Vehicle picked up.");
        }
    }
}
INNER_EOF

cat << 'INNER_EOF' > src/main/java/com/blockvehicles/commands/VehicleCommand.java
package com.blockvehicles.commands;

import com.blockvehicles.BlockVehiclesPlugin;
import com.blockvehicles.vehicle.VehicleType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VehicleCommand implements CommandExecutor {
    private final BlockVehiclesPlugin plugin;

    public VehicleCommand(BlockVehiclesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GOLD + "--- BlockVehicles Commands ---");
            sender.sendMessage(ChatColor.YELLOW + "/vehicle get <type> " + ChatColor.GRAY + "- Get a vehicle key");
            sender.sendMessage(ChatColor.YELLOW + "/vehicle give <player> <type> " + ChatColor.GRAY + "- Give key to player");
            sender.sendMessage(ChatColor.YELLOW + "/vehicle list " + ChatColor.GRAY + "- List all vehicle types");
            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {
            sender.sendMessage(ChatColor.GOLD + "Available Vehicles:");
            for (VehicleType type : VehicleType.values()) {
                sender.sendMessage(ChatColor.AQUA + "- " + type.name());
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("get") && sender instanceof Player player) {
            if (!player.hasPermission("blockvehicles.admin")) {
                player.sendMessage(ChatColor.RED + "No permission.");
                return true;
            }
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /vehicle get <type>");
                return true;
            }
            try {
                VehicleType type = VehicleType.valueOf(args[1].toUpperCase());
                ItemStack key = plugin.getCustomItems().createVehicleKey(type);
                player.getInventory().addItem(key);
                player.sendMessage(ChatColor.GREEN + "Received " + type.getDisplayName() + " Key!");
            } catch (IllegalArgumentException e) {
                player.sendMessage(ChatColor.RED + "Invalid vehicle type! Use /vehicle list");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("give")) {
            if (!sender.hasPermission("blockvehicles.admin")) {
                sender.sendMessage(ChatColor.RED + "No permission.");
                return true;
            }
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "Usage: /vehicle give <player> <type>");
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player not found!");
                return true;
            }
            try {
                VehicleType type = VehicleType.valueOf(args[2].toUpperCase());
                ItemStack key = plugin.getCustomItems().createVehicleKey(type);
                target.getInventory().addItem(key);
                sender.sendMessage(ChatColor.GREEN + "Gave " + type.getDisplayName() + " to " + target.getName());
            } catch (IllegalArgumentException e) {
                sender.sendMessage(ChatColor.RED + "Invalid vehicle type!");
            }
            return true;
        }
        return true;
    }
}
INNER_EOF

echo "All project files created successfully!"
