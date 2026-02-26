package fr.just_abdel.chroniclesskills.config;

import fr.just_abdel.chroniclescore.api.config.CoreConfig;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class GUIConfig extends CoreConfig {

    public GUIConfig(Plugin plugin) {
        super(plugin, "config.yml");
    }

    public String getTitle() {
        return get().getString("gui.title", "<color:#FFF6A8>Weapon Mastery");
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
        return get().getString("gui.info.name", "<color:#FFF6A8>Informations");
    }

    public List<String> getInfoLore() {
        return get().getStringList("gui.info.lore");
    }

    public List<String> getDefaultInfoLore() {
        return List.of(
                "<color:#92bed8>Vos points de maitrise",
                "<color:#92bed8>augmentent en tuant",
                "<color:#92bed8>des monstres avec",
                "<color:#92bed8>le type d'arme correspondant."
        );
    }

    public int getWeaponSlot(String weaponType) {
        return get().getInt("gui.weapons." + weaponType + ".slot", getDefaultWeaponSlot(weaponType));
    }

    /**
     * Fournit une valeur par défaut pour le slot d'un type d'arme donné, basée sur des conventions de nommage. Les types d'armes "lourde", "moyenne" et "legere" sont mappés à des slots spécifiques, tandis que les types d'armes non spécifiés utilisent un slot par défaut.
     */
    private int getDefaultWeaponSlot(String weaponType) {
        return switch (weaponType.toLowerCase()) {
            case "lourde" -> 11;
            case "legere" -> 15;
            default -> 13;
        };
    }

    /**
     * Récupère le matériau pour un type d'arme donné, en utilisant une valeur par défaut si le type d'arme n'est pas spécifié dans la configuration.
     */
    public String getWeaponMaterial(String weaponType) {
        return get().getString("gui.weapons." + weaponType + ".material", getDefaultWeaponMaterial(weaponType));
    }

    /**
     * Fournit une valeur par défaut pour le matériau d'un type d'arme donné, basée sur des conventions de nommage.
     */
    private String getDefaultWeaponMaterial(String weaponType) {
        return switch (weaponType.toLowerCase()) {
            case "lourde" -> "DIAMOND_AXE";
            case "legere" -> "DIAMOND_HOE";
            default -> "DIAMOND_SWORD";
        };
    }

    /**
     * Récupère les données de modèle personnalisé pour un type d'arme donné, en utilisant une valeur par défaut de 0 si le type d'arme n'est pas spécifié dans la configuration.
     */
    public int getWeaponCustomModelData(String weaponType) {
        return get().getInt("gui.weapons." + weaponType + ".custom-model-data", 0);
    }

    /**
     * Récupère le nom affiché pour un type d'arme donné, en utilisant une valeur par défaut si le type d'arme n'est pas spécifié dans la configuration
     */
    public String getWeaponName(String weaponType) {
        return get().getString("gui.weapons." + weaponType + ".name", "<color:#FFF6A8><weapon>");
    }

    /**
     * Récupère la lore pour un type d'arme donné, en utilisant une valeur par défaut si le type d'arme n'est pas spécifié dans la configuration. La lore par défaut inclut des placeholders pour le niveau, l'XP, la barre de progression et le pourcentage de progression.
     */
    public List<String> getWeaponLore() {
        List<String> lore = get().getStringList("gui.weapons.lore");
        if (lore.isEmpty()) {
            return List.of(
                    "<color:#8c8c8c>━━━━━━━━━━━━━━━━━━━━",
                    "<color:#92bed8>Level: <level>/<max>",
                    "<color:#92bed8>XP: <xp>/<required>",
                    "",
                    "<progress> <color:#8c8c8c><percent>%",
                    "<color:#8c8c8c>━━━━━━━━━━━━━━━━━━━━"
            );
        }
        return lore;
    }

    /**
     * Détermine si l'item d'un type d'arme donné doit avoir un effet de glow, en utilisant une valeur par défaut de true si le type d'arme n'est pas spécifié dans la configuration.
     */
    public boolean isWeaponGlowing(String weaponType) {
        return get().getBoolean("gui.weapons." + weaponType + ".glowing", true);
    }

    /**
     * Récupère la longueur de la barre de progression utilisée pour représenter visuellement le progrès de l'XP, en utilisant une valeur par défaut de 10 si elle n'est pas spécifiée dans la configuration.
     */
    public int getProgressBarLength() {
        return get().getInt("gui.progress-bar.length", 10);
    }

    /**
     * Récupère le caractère utilisé pour représenter la partie remplie de la barre de progression, en utilisant une valeur par défaut de "█" si elle n'est pas spécifiée dans la configuration.
     */
    public String getProgressBarFilledChar() {
        return get().getString("gui.progress-bar.filled-char", "█");
    }

    /**
     * Récupère le caractère utilisé pour représenter la partie vide de la barre de progression, en utilisant une valeur par défaut de "█" si elle n'est pas spécifiée dans la configuration. Notez que le même caractère est utilisé pour les parties remplies et vides, mais ils peuvent être différenciés par les couleurs définies dans la configuration.
     */
    public String getProgressBarEmptyChar() {
        return get().getString("gui.progress-bar.empty-char", "█");
    }

    /**
     * Récupère la couleur utilisée pour la partie remplie de la barre de progression, en utilisant une valeur par défaut de "<color:#92bed8>" si elle n'est pas spécifiée dans la configuration. Cette couleur sera appliquée au caractère défini pour la partie remplie de la barre de progression.
     */
    public String getProgressBarFilledColor() {
        return get().getString("gui.progress-bar.filled-color", "<color:#92bed8>");
    }

    /**
     * Récupère la couleur utilisée pour la partie vide de la barre de progression, en utilisant une valeur par défaut de "<color:#4a4a4a>" si elle n'est pas spécifiée dans la configuration. Cette couleur sera appliquée au caractère défini pour la partie vide de la barre de progression.
     */
    public String getProgressBarEmptyColor() {
        return get().getString("gui.progress-bar.empty-color", "<color:#4a4a4a>");
    }

    /**
     * Récupère le message affiché au joueur lorsqu'il clique sur une arme dans le GUI, en utilisant une valeur par défaut si elle n'est pas spécifiée dans la configuration. Le message par défaut inclut un placeholder pour le nom de l'arme cliquée.
     */
    public String getClickMessage() {
        return get().getString("gui.click-message", "<color:#92bed8>Clique sur <weapon>");
    }


    /**
     * Récupère un matériau à partir de son nom, en utilisant une valeur par défaut de Material.STONE si le nom du matériau n'est pas valide ou n'est pas spécifié dans la configuration. Cette méthode permet de garantir que le plugin ne plantera pas en cas de mauvaise configuration du matériau.
     */
    public Material getMaterialSafe(String materialName) {
        try {
            return Material.valueOf(materialName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Material.STONE;
        }
    }
}
