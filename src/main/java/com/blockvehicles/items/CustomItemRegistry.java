package com.blockvehicles.items;

import com.blockvehicles.BlockVehiclesPlugin;
import com.blockvehicles.registry.VehicleRegistry;
import com.blockvehicles.registry.VehicleSpec;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
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
        ItemStack speedTuner = new ItemStack(Material.REDSTONE_TORCH);
        ItemMeta tMeta = speedTuner.getItemMeta();
        if (tMeta != null) {
            tMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "⚡ Vehicle Speed & Gear Controller");
            tMeta.setLore(Arrays.asList(
                    ChatColor.YELLOW + "Hold in hand while driving to tune performance.",
                    ChatColor.AQUA + "Right-Click to Shift Gears:",
                    ChatColor.GRAY + "  ECO: 30% Speed",
                    ChatColor.GRAY + "  [2] CRUISE: 60% Speed",
                    ChatColor.GRAY + "  [3] SPORT: 100% Speed",
                    ChatColor.GRAY + "  [4] NITRO: 160% Speed"
            ));
            tMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            tMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            tMeta.getPersistentDataContainer().set(ITEM_ID_KEY, PersistentDataType.STRING, "SPEED_CONTROLLER");
            speedTuner.setItemMeta(tMeta);
        }
        customItems.put("SPEED_CONTROLLER", speedTuner);

        regItem("STEEL_INGOT", Material.IRON_INGOT, "Industrial Steel Ingot", ChatColor.WHITE, "Reinforced structural alloy");
        regItem("RUBBER_SHEET", Material.DRIED_KELP, "Vulcanized Rubber Sheet", ChatColor.DARK_GRAY, "Used for tires and seals");
        regItem("COPPER_WIRING", Material.STRING, "Insulated Copper Wiring", ChatColor.GOLD, "Electrical wiring harness");
        regItem("TIRE_ALL_TERRAIN", Material.COAL_BLOCK, "Heavy All-Terrain Tire", ChatColor.DARK_GRAY, "High-grip durable wheel");
        regItem("ENGINE_V6", Material.PISTON, "V6 Combustion Engine Block", ChatColor.GOLD, "Standard automotive powertrain");
        regItem("ENGINE_V8_TURBO", Material.BLAST_FURNACE, "Twin-Turbo V8 Engine", ChatColor.RED, "High performance supercar powertrain");
        regItem("LITHIUM_BATTERY", Material.COPPER_BLOCK, "Lithium-Ion Battery Cell", ChatColor.GREEN, "Compact energy storage (+450 Fuel)");
        regItem("SOLAR_PANEL", Material.DAYLIGHT_DETECTOR, "Photovoltaic Solar Array", ChatColor.YELLOW, "Recharges vehicle under open sunlight");
        regItem("DIAMOND_DRILL_HEAD", Material.ANVIL, "Industrial Diamond Drill Head", ChatColor.AQUA, "Excavates rock and minerals in mining mode");
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
            if (spec.isFlying()) lore.add(ChatColor.LIGHT_PURPLE + "✦ Flight Capable");
            if (spec.canDrill()) lore.add(ChatColor.GOLD + "✦ Mining Rig Equipped");
            if (spec.hasSolar()) lore.add(ChatColor.GREEN + "✦ Solar Recharging Array");
            lore.add(ChatColor.GRAY + "------------------------");
            lore.add(ChatColor.GREEN + "Right-Click on ground to deploy vehicle!");
            meta.setLore(lore);
            meta.getPersistentDataContainer().set(VEHICLE_SPEC_KEY, PersistentDataType.STRING, spec.getId());
            key.setItemMeta(meta);
        }
        return key;
    }

    public void registerAllRecipes() {
        ShapedRecipe tuner = new ShapedRecipe(new NamespacedKey(plugin, "recipe_speed_controller"), getItem("SPEED_CONTROLLER"));
        tuner.shape(" R ", " R ", " G ");
        tuner.setIngredient((char) 82, Material.REDSTONE_TORCH);
        tuner.setIngredient((char) 71, Material.GOLD_INGOT);
        plugin.getServer().addRecipe(tuner);
    }
}
