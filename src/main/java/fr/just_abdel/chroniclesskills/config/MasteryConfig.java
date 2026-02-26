package fr.just_abdel.chroniclesskills.config;

import fr.just_abdel.chroniclescore.api.config.CoreConfig;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class MasteryConfig extends CoreConfig {

    public MasteryConfig(Plugin plugin) {
        super(plugin, "config.yml");
    }

    public double getXpBase() {
        return get().getDouble("xp.base", 50.0);
    }

    public double getXpMultiplier() {
        return get().getDouble("xp.multiplier", 1.12);
    }

    public double getXpExponent() {
        return get().getDouble("xp.exponent", 1.5);
    }

    public int getDefaultMaxLevel() {
        return get().getInt("max-levels.default", 100);
    }

    /**
     * Récupère les niveaux maximum par monde avec support des patterns.
     * Supporte le wildcard * (ex: "Palier*") pour matcher "Palier1", "Palier2", etc.
     */
    public int getMaxLevelForWorld(String worldName) {
        Map<String, Integer> worldLevels = getWorldMaxLevels();
        for (Map.Entry<String, Integer> entry : worldLevels.entrySet()) {
            String pattern = entry.getKey();
            if (matchesPattern(worldName, pattern)) {
                return entry.getValue();
            }
        }
        return getDefaultMaxLevel();
    }

    /**
     * Vérifie si un nom de monde correspond à un pattern.
     * Supporte le wildcard * à la fin du pattern.
     */
    private boolean matchesPattern(String worldName, String pattern) {
        if (pattern.endsWith("*")) {
            String prefix = pattern.substring(0, pattern.length() - 1);
            return worldName.toLowerCase().startsWith(prefix.toLowerCase());
        }
        return worldName.equalsIgnoreCase(pattern);
    }

    public Map<String, Integer> getWorldMaxLevels() {
        Map<String, Integer> worldLevels = new HashMap<>();
        if (get().contains("max-levels.worlds")) {
            for (String world : get().getConfigurationSection("max-levels.worlds").getKeys(false)) {
                worldLevels.put(world, get().getInt("max-levels.worlds." + world));
            }
        }
        return worldLevels;
    }

    public double getDefaultMobXp() {
        return get().getDouble("default-mob-xp", 10.0);
    }

    public Map<String, String> getMmoItemsMappings() {
        Map<String, String> mappings = new HashMap<>();
        if (get().contains("mmoitems.mappings")) {
            for (String type : get().getConfigurationSection("mmoitems.mappings").getKeys(false)) {
                mappings.put(type, get().getString("mmoitems.mappings." + type));
            }
        }
        return mappings;
    }

    public String getWeaponTypeForMmoItem(String mmoItemType) {
        return getMmoItemsMappings().getOrDefault(mmoItemType, "moyenne");
    }

    public boolean isMythicMobsEnabled() {
        return get().getBoolean("mythicmobs.enabled", true);
    }

    public Map<String, Double> getMythicMobXp() {
        Map<String, Double> mobXp = new HashMap<>();
        if (get().contains("mythicmobs.xp")) {
            for (String mob : get().getConfigurationSection("mythicmobs.xp").getKeys(false)) {
                mobXp.put(mob, get().getDouble("mythicmobs.xp." + mob));
            }
        }
        return mobXp;
    }

    public double getMythicMobXpForType(String mobType) {
        return getMythicMobXp().getOrDefault(mobType, getDefaultMobXp());
    }

    public int getAutoSaveInterval() {
        return get().getInt("auto-save-interval", 5);
    }
}
