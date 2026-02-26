package fr.just_abdel.chroniclesskills.config;

import fr.just_abdel.chroniclescore.api.config.CoreConfig;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
     * Récupère les niveaux maximums par monde depuis la configuration.
     */
    public Map<String, Integer> getWorldMaxLevels() {
        Map<String, Integer> worldLevels = new HashMap<>();
        if (get().contains("max-levels.worlds")) {
            for (String world : Objects.requireNonNull(get().getConfigurationSection("max-levels.worlds")).getKeys(false)) {
                worldLevels.put(world, get().getInt("max-levels.worlds." + world));
            }
        }
        return worldLevels;
    }

    /**
     * Récupère le niveau maximum pour un monde donné, en utilisant la valeur par défaut si le monde n'est pas spécifié.
     */
    public int getMaxLevelForWorld(String worldName) {
        return getWorldMaxLevels().getOrDefault(worldName, getDefaultMaxLevel());
    }

    /**
     * Récupère l'XP par défaut pour les mobs, utilisée si aucun mob spécifique n'est défini dans la configuration.
     */
    public double getDefaultMobXp() {
        return get().getDouble("default-mob-xp", 10.0);
    }

    /**
     * Récupère les mappings pour MMOItems depuis la configuration, permettant de lier des types d'items MMOItems à des types d'armes pour le calcul de l'XP.
     */
    public Map<String, String> getMmoItemsMappings() {
        Map<String, String> mappings = new HashMap<>();
        if (get().contains("mmoitems.mappings")) {
            for (String type : Objects.requireNonNull(get().getConfigurationSection("mmoitems.mappings")).getKeys(false)) {
                mappings.put(type, get().getString("mmoitems.mappings." + type));
            }
        }
        return mappings;
    }

    /**
     * Récupère les XP spécifiques pour les mobs MythicMobs depuis la configuration, permettant de définir des valeurs d'XP personnalisées pour chaque type de mob MythicMob.
     */
    public Map<String, Double> getMythicMobXp() {
        Map<String, Double> mobXp = new HashMap<>();
        if (get().contains("mythicmobs.xp")) {
            for (String mob : Objects.requireNonNull(get().getConfigurationSection("mythicmobs.xp")).getKeys(false)) {
                mobXp.put(mob, get().getDouble("mythicmobs.xp." + mob));
            }
        }
        return mobXp;
    }

    /**
     * Récupère l'intervalle de sauvegarde automatique en minutes depuis la configuration, utilisée pour déterminer à quelle fréquence les données de maîtrise des joueurs sont sauvegardées automatiquement.
     */
    public int getAutoSaveInterval() {
        return get().getInt("auto-save-interval", 5);
    }
}
