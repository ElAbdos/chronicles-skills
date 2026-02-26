package fr.just_abdel.chroniclesskills.gui;

import fr.just_abdel.chroniclescore.api.gui.CoreGUIBuilder;
import fr.just_abdel.chroniclescore.api.gui.CoreHolder;
import fr.just_abdel.chroniclescore.api.items.CoreItemBuilder;
import fr.just_abdel.chroniclesskills.config.GUIConfig;
import fr.just_abdel.chroniclesskills.gui.holder.MasteryHolder;
import fr.just_abdel.chroniclesskills.manager.MasteryManager;
import fr.just_abdel.chroniclesskills.manager.XPManager;
import fr.just_abdel.chroniclesskills.model.MasteryData;
import fr.just_abdel.chroniclesskills.model.WeaponType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class MasteryMenuGUI implements Listener {

    @Getter private final Plugin plugin;
    private final MasteryManager masteryManager;
    private final GUIConfig guiConfig;
    private final Map<UUID, Inventory> openMenus = new ConcurrentHashMap<>();

    public MasteryMenuGUI(Plugin plugin, MasteryManager masteryManager) {
        this.plugin = plugin;
        this.masteryManager = masteryManager;
        this.guiConfig = masteryManager.getGuiConfig();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Ouvre le menu de maîtrise pour un joueur, en construisant l'inventaire avec les données de maîtrise et les paramètres de configuration.
     */
    public void open(Player player) {
        MasteryData data = masteryManager.getData(player.getUniqueId());
        XPManager xpManager = masteryManager.getXpManager();
        int maxLevel = masteryManager.getConfig().getMaxLevelForWorld(player.getWorld().getName());

        String title = guiConfig.getTitle();
        int rows = guiConfig.getRows();
        CoreHolder holder = new MasteryHolder(player.getUniqueId());

        CoreGUIBuilder builder = CoreGUIBuilder.create(title, rows).holder(holder);

        for (WeaponType type : WeaponType.values()) {
            int slot = guiConfig.getWeaponSlot(type.name().toLowerCase());
            builder.setItem(slot, createWeaponItem(type, data, xpManager, maxLevel, player));
        }

        ItemStack infoItem = createInfoItem();
        builder.setItem(guiConfig.getInfoSlot(), infoItem);
        Inventory inventory = builder.build();
        openMenus.put(player.getUniqueId(), inventory);
        player.openInventory(inventory);
    }

    /**
     * Créer un item d'information pour le menu, en utilisant les paramètres de configuration pour le matériau, le nom, la lore et les données de modèle personnalisé.
     */
    private ItemStack createInfoItem() {
        Material material = guiConfig.getMaterialSafe(guiConfig.getInfoMaterial());
        CoreItemBuilder builder = new CoreItemBuilder(material).name(guiConfig.getInfoName());

        int customModelData = guiConfig.getInfoCustomModelData();
        if (customModelData > 0) {
            builder.customModelData(customModelData);
        }

        List<String> lore = guiConfig.getInfoLore();
        builder.lore(lore.toArray(new String[0]));

        return builder.build();
    }

    /**
     * Crée un item pour une arme spécifique, en utilisant les données de maîtrise du joueur et les paramètres de configuration pour le matériau, le nom, la lore, les données de modèle personnalisé et l'effet de glow.
     */
    private ItemStack createWeaponItem(WeaponType type, MasteryData data, XPManager xpManager, int maxLevel, Player player) {
        int level = data.getLevel(type);
        double currentXp = data.getXp(type);
        double requiredXp = xpManager.getRequiredXp(level);
        double progress = xpManager.getProgressPercentage(currentXp, level);

        String weaponKey = type.name().toLowerCase();
        Material material = guiConfig.getMaterialSafe(guiConfig.getWeaponMaterial(weaponKey));
        String progressBar = createProgressBar(progress);

        List<String> configLore = guiConfig.getWeaponLore();
        List<String> lore = new ArrayList<>();
        for (String line : configLore) {
            lore.add(line
                    .replace("<level>", String.valueOf(level))
                    .replace("<max>", String.valueOf(maxLevel))
                    .replace("<xp>", String.format("%.0f", currentXp))
                    .replace("<required>", String.format("%.0f", requiredXp))
                    .replace("<progress>", progressBar)
                    .replace("<percent>", String.format("%.0f", progress * 100)));
        }

        String name = guiConfig.getWeaponName(weaponKey).replace("<weapon>", type.getDisplayName());

        CoreItemBuilder builder = new CoreItemBuilder(material).name(name).lore(player, lore.toArray(new String[0]));

        int customModelData = guiConfig.getWeaponCustomModelData(weaponKey);
        if (customModelData > 0) {
            builder.customModelData(customModelData);
        }

        if (guiConfig.isWeaponGlowing(weaponKey)) {
            builder.glowing();
        }

        return builder.build(player);
    }

    /**
     * Créer une barre de progression textuelle pour représenter visuellement le progrès de l'XP, en utilisant les caractères et les couleurs définis dans la configuration.
     */
    private String createProgressBar(double progress) {
        int length = guiConfig.getProgressBarLength();
        int filled = (int) (progress * length);

        String filledChar = guiConfig.getProgressBarFilledChar();
        String emptyChar = guiConfig.getProgressBarEmptyChar();
        String filledColor = guiConfig.getProgressBarFilledColor();
        String emptyColor = guiConfig.getProgressBarEmptyColor();

        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < filled; i++) {
            bar.append(filledColor).append(filledChar).append("<shift:-1>");
        }
        for (int i = 0; i < length - filled; i++) {
            bar.append(emptyColor).append(emptyChar).append("<shift:-1>");
        }

        return bar.toString();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        if (!(event.getInventory().getHolder() instanceof MasteryHolder)) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        openMenus.remove(player.getUniqueId());
    }
}
