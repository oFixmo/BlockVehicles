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
