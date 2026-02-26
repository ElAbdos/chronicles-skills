package fr.just_abdel.chroniclesskills.config;

import fr.just_abdel.chroniclescore.api.config.CoreConfig;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.stream.Collectors;

public class GUIConfig extends CoreConfig {

    private final ColorConfig colorConfig;

    public GUIConfig(Plugin plugin, ColorConfig colorConfig) {
        super(plugin, "config.yml");
        this.colorConfig = colorConfig;
    }

    public String getTitle() {
        return colorConfig.applyColors(get().getString("gui.title", "<gold>Maitrise des Armes"));
    }

    public int getRows() {
        return get().getInt("gui.rows", 5);
    }

    public int getInfoSlot() {
        return get().getInt("gui.info.slot", 22);
    }

    public String getInfoMaterial() {
        return get().getString("gui.info.material", "BOOK");
    }

    public int getInfoCustomModelData() {
        return get().getInt("gui.info.custom-model-data", 0);
    }

    public String getInfoName() {
        return colorConfig.applyColors(get().getString("gui.info.name", "<gold>Informations"));
    }

    public List<String> getInfoLore() {
        List<String> lore = get().getStringList("gui.info.lore");
        if (lore.isEmpty()) {
            lore = List.of(
                    "<secondary>Vos points de maitrise",
                    "<secondary>augmentent en tuant",
                    "<secondary>des monstres avec",
                    "<secondary>le type d'arme correspondant."
            );
        }
        return lore.stream().map(colorConfig::applyColors).collect(Collectors.toList());
    }

    public int getWeaponSlot(String weaponType) {
        return get().getInt("gui.weapons." + weaponType + ".slot", getDefaultWeaponSlot(weaponType));
    }

    private int getDefaultWeaponSlot(String weaponType) {
        return switch (weaponType.toLowerCase()) {
            case "lourde" -> 11;
            case "legere" -> 15;
            default -> 13;
        };
    }

    public String getWeaponMaterial(String weaponType) {
        return get().getString("gui.weapons." + weaponType + ".material", getDefaultWeaponMaterial(weaponType));
    }

    private String getDefaultWeaponMaterial(String weaponType) {
        return switch (weaponType.toLowerCase()) {
            case "lourde" -> "DIAMOND_AXE";
            case "legere" -> "DIAMOND_HOE";
            default -> "DIAMOND_SWORD";
        };
    }

    public int getWeaponCustomModelData(String weaponType) {
        return get().getInt("gui.weapons." + weaponType + ".custom-model-data", 0);
    }

    public String getWeaponName(String weaponType) {
        return colorConfig.applyColors(get().getString("gui.weapons." + weaponType + ".name", "<gold><weapon>"));
    }

    public List<String> getWeaponLore() {
        List<String> lore = get().getStringList("gui.weapons.lore");
        if (lore.isEmpty()) {
            lore = List.of(
                    "<gray>━━━━━━━━━━━━━━━━━━━━",
                    "<secondary>Niveau: <level>/<max>",
                    "<secondary>XP: <xp>/<required>",
                    "",
                    "<progress> <gray><percent>%",
                    "<gray>━━━━━━━━━━━━━━━━━━━━"
            );
        }
        return lore.stream().map(colorConfig::applyColors).collect(Collectors.toList());
    }

    public boolean isWeaponGlowing(String weaponType) {
        return get().getBoolean("gui.weapons." + weaponType + ".glowing", true);
    }

    public int getProgressBarLength() {
        return get().getInt("gui.progress-bar.length", 10);
    }

    public String getProgressBarFilledChar() {
        return get().getString("gui.progress-bar.filled-char", "█");
    }

    public String getProgressBarEmptyChar() {
        return get().getString("gui.progress-bar.empty-char", "█");
    }

    public String getProgressBarFilledColor() {
        return colorConfig.applyColors(get().getString("gui.progress-bar.filled-color", "<secondary>"));
    }

    public String getProgressBarEmptyColor() {
        return colorConfig.applyColors(get().getString("gui.progress-bar.empty-color", "<gray>"));
    }

    public String getClickMessage() {
        return colorConfig.applyColors(get().getString("gui.click-message", "<secondary>Clique sur <weapon>"));
    }

    public Material getMaterialSafe(String materialName) {
        try {
            return Material.valueOf(materialName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Material.STONE;
        }
    }
}
