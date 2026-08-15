#!/bin/bash
set -e

echo "=== Fixing pom.xml Compiler & Java Syntax ==="

# 1. Update pom.xml to maven-compiler-plugin 3.13.0 with forking
cat << 'INNER_EOF' > pom.xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.blockvehicles</groupId>
    <artifactId>BlockVehicles</artifactId>
    <version>2.0.0</version>
    <packaging>jar</packaging>
    <name>BlockVehicles</name>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <maven.compiler.release>17</maven.compiler.release>
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
                <version>3.13.0</version>
                <configuration>
                    <release>17</release>
                    <fork>true</fork>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
INNER_EOF

# 2. Fix CustomItemRegistry.java
cat << 'INNER_EOF' > src/main/java/com/blockvehicles/items/CustomItemRegistry.java
package com.blockvehicles.items;

import com.blockvehicles.BlockVehiclesPlugin;
import com.blockvehicles.registry.VehicleRegistry;
import com.blockvehicles.registry.VehicleSpec;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class CustomItemRegistry {
    private final BlockVehiclesPlugin plugin;
    public final NamespacedKey ITEM_ID_KEY;
    public final NamespacedKey VEHICLE_SPEC_KEY;

    private final Map<String, ItemStack> customItems = new HashMap<>();

    public CustomItemRegistry(BlockVehiclesPlugin plugin) {
        this.plugin = plugin;
        this.ITEM_ID_KEY = new NamespacedKey(plugin, "custom_item_id");
        this.VEHICLE_SPEC_KEY = new NamespacedKey(plugin, "vehicle_spec_id");
        initCustomItems();
    }

    private void initCustomItems() {
        regItem("STEEL_INGOT", Material.IRON_INGOT, "Industrial Steel Ingot", ChatColor.WHITE, "Reinforced structural alloy");
        regItem("RUBBER_SHEET", Material.DRIED_KELP, "Vulcanized Rubber Sheet", ChatColor.DARK_GRAY, "Used for tires and seals");
        regItem("COPPER_WIRING", Material.STRING, "Insulated Copper Wiring", ChatColor.GOLD, "Electrical wiring harness");
        regItem("REINFORCED_GLASS", Material.TINTED_GLASS, "Ballistic Reinforced Glass", ChatColor.AQUA, "Impact and pressure resistant");
        regItem("CARBON_FIBER", Material.NETHERITE_SCRAP, "Carbon Fiber Composite", ChatColor.DARK_AQUA, "Ultra-lightweight high-strength body panel");
        
        regItem("TIRE_ALL_TERRAIN", Material.COAL_BLOCK, "Heavy All-Terrain Tire", ChatColor.DARK_GRAY, "High-grip durable wheel");
        regItem("ENGINE_V6", Material.PISTON, "V6 Combustion Engine Block", ChatColor.GOLD, "Standard automotive powertrain");
        regItem("ENGINE_V8_TURBO", Material.BLAST_FURNACE, "Twin-Turbo V8 Engine", ChatColor.RED, "High performance supercar powertrain");
        regItem("ELECTRIC_MOTOR", Material.COPPER_BLOCK, "High-Torque Electric Motor", ChatColor.AQUA, "Instant acceleration electric drive");
        regItem("TURBOFAN_JET_ENGINE", Material.DISPENSER, "High-Bypass Turbofan Jet Engine", ChatColor.DARK_AQUA, "Aviation propulsion unit");
        regItem("HELICOPTER_ROTOR", Material.IRON_BARS, "Turboshaft Rotor Assembly", ChatColor.LIGHT_PURPLE, "Rotary wing lift mechanism");
        regItem("TRANSMISSION_GEARBOX", Material.HOPPER, "Heavy Duty Gearbox", ChatColor.GRAY, "Transfers mechanical torque");

        regItem("LITHIUM_BATTERY", Material.COPPER_BLOCK, "Lithium-Ion Battery Cell", ChatColor.GREEN, "Compact energy storage (+450 Fuel)");
        regItem("HIGH_CAPACITY_BATTERY", Material.GOLD_BLOCK, "High-Density Energy Core", ChatColor.DARK_GREEN, "Industrial power unit (+1200 Fuel)");
        regItem("SOLAR_PANEL", Material.DAYLIGHT_DETECTOR, "Photovoltaic Solar Array", ChatColor.YELLOW, "Recharges vehicle under open sunlight");
        regItem("AVIONICS_COMPUTER", Material.COMPASS, "Avionics Flight Computer", ChatColor.DARK_AQUA, "Navigation, pitch & roll stabilization");

        regItem("DIAMOND_DRILL_HEAD", Material.ANVIL, "Industrial Diamond Drill Head", ChatColor.AQUA, "Excavates rock and minerals in mining mode");
        regItem("ARMOR_PLATING", Material.NETHERITE_INGOT, "Heavy Composite Armor Plating", ChatColor.DARK_GRAY, "Protective plating for military vehicles");
        regItem("HYDRAULIC_CYLINDER", Material.STICKY_PISTON, "Heavy Hydraulic Cylinder", ChatColor.YELLOW, "Powers loaders, plows and suspension");
        regItem("AIRCRAFT_WING", Material.FEATHER, "Aerodynamic Aircraft Wing Rib", ChatColor.WHITE, "Generates high speed lift");
    }

    private void regItem(String id, Material mat, String name, ChatColor color, String lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(color + "" + ChatColor.BOLD + name);
            meta.setLore(Arrays.asList(ChatColor.GRAY + lore, ChatColor.DARK_GRAY + "ID: " + id));
            meta.getPersistentDataContainer().set(ITEM_ID_KEY, PersistentDataType.STRING, id);
            item.setItemMeta(meta);
        }
        customItems.put(id, item);
    }

    public ItemStack getItem(String id) {
        ItemStack item = customItems.get(id);
        return item != null ? item.clone() : null;
    }

    public ItemStack createVehicleKey(VehicleSpec spec) {
        ItemStack key = new ItemStack(spec.getCategory().getIcon());
        ItemMeta meta = key.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(spec.getCategory().getColor() + "" + ChatColor.BOLD + spec.getName() + " Key");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.YELLOW + "Origin: " + ChatColor.WHITE + spec.getOrigin());
            lore.add(ChatColor.YELLOW + "Category: " + ChatColor.WHITE + spec.getCategory().getTitle());
            lore.add(ChatColor.GRAY + "------------------------");
            lore.add(ChatColor.AQUA + "Max Speed: " + ChatColor.WHITE + String.format("%.1f", spec.getSpeed() * 100) + " km/h");
            lore.add(ChatColor.AQUA + "Fuel Capacity: " + ChatColor.WHITE + spec.getMaxFuel() + " units");
            lore.add(ChatColor.AQUA + "Trunk Storage: " + ChatColor.WHITE + (spec.getStorageRows() * 9) + " Slots");
            if (spec.isFlying()) lore.add(ChatColor.LIGHT_PURPLE + "✦ Flight Capable " + (spec.canHover() ? "(VTOL / Hover)" : "(Fixed Wing)"));
            if (spec.canDrill()) lore.add(ChatColor.GOLD + "✦ Excavation Mining Rig Equipped");
            if (spec.hasSolar()) lore.add(ChatColor.GREEN + "✦ Solar Eco-Recharging Array");
            lore.add(ChatColor.GRAY + "------------------------");
            lore.add(ChatColor.GREEN + "Right-Click on ground to deploy vehicle!");
            meta.setLore(lore);
            meta.getPersistentDataContainer().set(VEHICLE_SPEC_KEY, PersistentDataType.STRING, spec.getId());
            key.setItemMeta(meta);
        }
        return key;
    }

    public void registerAllRecipes() {
        ShapelessRecipe steel = new ShapelessRecipe(new NamespacedKey(plugin, "recipe_steel"), getItem("STEEL_INGOT"));
        steel.addIngredient(Material.IRON_INGOT);
        steel.addIngredient(Material.COAL);
        steel.addIngredient(Material.IRON_INGOT);
        plugin.getServer().addRecipe(steel);

        ShapelessRecipe rubber = new ShapelessRecipe(new NamespacedKey(plugin, "recipe_rubber"), getItem("RUBBER_SHEET"));
        rubber.addIngredient(Material.DRIED_KELP);
        rubber.addIngredient(Material.SLIME_BALL);
        plugin.getServer().addRecipe(rubber);

        ShapelessRecipe wire = new ShapelessRecipe(new NamespacedKey(plugin, "recipe_wire"), getItem("COPPER_WIRING"));
        wire.addIngredient(Material.COPPER_INGOT);
        wire.addIngredient(Material.STRING);
        plugin.getServer().addRecipe(wire);

        ShapedRecipe tire = new ShapedRecipe(new NamespacedKey(plugin, "recipe_tire"), getItem("TIRE_ALL_TERRAIN"));
        tire.shape(" R ", "RCR", " R ");
        tire.setIngredient('R', Material.DRIED_KELP);
        tire.setIngredient('C', Material.COAL_BLOCK);
        plugin.getServer().addRecipe(tire);

        ShapedRecipe engineV6 = new ShapedRecipe(new NamespacedKey(plugin, "recipe_engine_v6"), getItem("ENGINE_V6"));
        engineV6.shape("III", "PRP", "III");
        engineV6.setIngredient('I', Material.IRON_INGOT);
        engineV6.setIngredient('P', Material.PISTON);
        engineV6.setIngredient('R', Material.REDSTONE_BLOCK);
        plugin.getServer().addRecipe(engineV6);

        ShapedRecipe engineV8 = new ShapedRecipe(new NamespacedKey(plugin, "recipe_engine_v8"), getItem("ENGINE_V8_TURBO"));
        engineV8.shape("BIB", "IEI", "BIB");
        engineV8.setIngredient('B', Material.BLAZE_POWDER);
        engineV8.setIngredient('I', Material.IRON_BLOCK);
        engineV8.setIngredient('E', Material.PISTON);
        plugin.getServer().addRecipe(engineV8);

        ShapedRecipe battery = new ShapedRecipe(new NamespacedKey(plugin, "recipe_battery"), getItem("LITHIUM_BATTERY"));
        battery.shape(" C ", "RGR", " C ");
        battery.setIngredient('C', Material.COPPER_INGOT);
        battery.setIngredient('R', Material.REDSTONE);
        battery.setIngredient('G', Material.GOLD_INGOT);
        plugin.getServer().addRecipe(battery);

        ShapedRecipe solar = new ShapedRecipe(new NamespacedKey(plugin, "recipe_solar"), getItem("SOLAR_PANEL"));
        solar.shape("GGG", "QQQ", "RRR");
        solar.setIngredient('G', Material.GLASS);
        solar.setIngredient('Q', Material.QUARTZ);
        solar.setIngredient('R', Material.REDSTONE);
        plugin.getServer().addRecipe(solar);

        ShapedRecipe drill = new ShapedRecipe(new NamespacedKey(plugin, "recipe_drill_head"), getItem("DIAMOND_DRILL_HEAD"));
        drill.shape(" D ", "DID", " I ");
        drill.setIngredient('D', Material.DIAMOND_BLOCK);
        drill.setIngredient('I', Material.IRON_BLOCK);
        plugin.getServer().addRecipe(drill);

        ShapedRecipe jet = new ShapedRecipe(new NamespacedKey(plugin, "recipe_turbofan"), getItem("TURBOFAN_JET_ENGINE"));
        jet.shape("IBI", "BRB", "IBI");
        jet.setIngredient('I', Material.IRON_BLOCK);
        jet.setIngredient('B', Material.BLAZE_ROD);
        jet.setIngredient('R', Material.REDSTONE_BLOCK);
        plugin.getServer().addRecipe(jet);

        registerVehicleRecipe("BUGATTI_CHIRON", Material.BLUE_CONCRETE, Material.CHEST, Material.BLAST_FURNACE, Material.COAL_BLOCK);
        registerVehicleRecipe("TOYOTA_COROLLA", Material.WHITE_CONCRETE, Material.GLASS, Material.PISTON, Material.COAL_BLOCK);
        registerVehicleRecipe("CAT_349_EXCAVATOR", Material.YELLOW_CONCRETE, Material.DIAMOND_BLOCK, Material.REDSTONE_BLOCK, Material.COAL_BLOCK);
        registerVehicleRecipe("AH64_APACHE", Material.IRON_BARS, Material.GRAY_CONCRETE, Material.GLASS, Material.BLAST_FURNACE);
        registerVehicleRecipe("F22_RAPTOR", Material.FEATHER, Material.GRAY_CONCRETE, Material.DISPENSER, Material.BLAZE_ROD);
    }

    private void registerVehicleRecipe(String id, Material m1, Material m2, Material m3, Material m4) {
        VehicleSpec spec = VehicleRegistry.get(id);
        if (spec == null) return;
        ItemStack key = createVehicleKey(spec);
        NamespacedKey rKey = new NamespacedKey(plugin, "craft_" + id.toLowerCase());
        ShapedRecipe recipe = new ShapedRecipe(rKey, key);
        recipe.shape("ABA", "CDC", "EFE");
        recipe.setIngredient('A', m1);
        recipe.setIngredient('B', m2);
        recipe.setIngredient('C', m3);
        recipe.setIngredient('D', Material.CHEST);
        recipe.setIngredient('E', m4);
        recipe.setIngredient('F', Material.REDSTONE_BLOCK);
        try {
            plugin.getServer().addRecipe(recipe);
        } catch (Exception ignored) {}
    }
}
INNER_EOF

# 3. Fix VehicleCommand.java
cat << 'INNER_EOF' > src/main/java/com/blockvehicles/commands/VehicleCommand.java
package com.blockvehicles.commands;

import com.blockvehicles.BlockVehiclesPlugin;
import com.blockvehicles.gui.VehicleCatalogGUI;
import com.blockvehicles.registry.VehicleRegistry;
import com.blockvehicles.registry.VehicleSpec;
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
            if (sender instanceof Player player) {
                VehicleCatalogGUI.openCatalog(player, null, 0);
            } else {
                sender.sendMessage(ChatColor.GOLD + "BlockVehicles 2.0 | Total Vehicles: " + VehicleRegistry.getAll().size());
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("menu") || args[0].equalsIgnoreCase("catalog") || args[0].equalsIgnoreCase("gui")) {
            if (sender instanceof Player player) {
                VehicleCatalogGUI.openCatalog(player, null, 0);
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("give") && sender.hasPermission("blockvehicles.admin")) {
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "Usage: /vehicle give <player> <specId>");
                return true;
            }
            Player target = Bukkit.getPlayer(args);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player not found!");
                return true;
            }
            VehicleSpec spec = VehicleRegistry.get(args[2].toUpperCase());
            if (spec == null) {
                sender.sendMessage(ChatColor.RED + "Invalid vehicle! Use /vehicle menu to browse.");
                return true;
            }
            ItemStack key = plugin.getCustomItemRegistry().createVehicleKey(spec);
            target.getInventory().addItem(key);
            sender.sendMessage(ChatColor.GREEN + "Gave " + spec.getName() + " to " + target.getName());
            return true;
        }

        if (args[0].equalsIgnoreCase("item") && sender instanceof Player player && player.hasPermission("blockvehicles.admin")) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /vehicle item <itemId>");
                return true;
            }
            ItemStack item = plugin.getCustomItemRegistry().getItem(args.toUpperCase());
            if (item != null) {
                player.getInventory().addItem(item);
                player.sendMessage(ChatColor.GREEN + "Given " + item.getItemMeta().getDisplayName());
            } else {
                player.sendMessage(ChatColor.RED + "Item ID not found.");
            }
            return true;
        }

        return true;
    }
}
INNER_EOF

# 4. Clean and compile
mvn clean package

# 5. Push to GitHub
cp target/BlockVehicles-2.0.0.jar ./BlockVehicles.jar
git add .
git commit -m "Fix compiler ConcurrentModificationException and recipe syntax"
git push

echo "=== Success! BlockVehicles.jar built and updated in your repository! ==="
