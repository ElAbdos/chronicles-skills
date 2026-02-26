package fr.just_abdel.chroniclesskills.placeholder;

import fr.just_abdel.chroniclesskills.ChroniclesSkills;
import fr.just_abdel.chroniclesskills.manager.MasteryManager;
import fr.just_abdel.chroniclesskills.model.MasteryData;
import fr.just_abdel.chroniclesskills.model.WeaponType;
import fr.just_abdel.chroniclesskills.util.WeaponUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Expansion PlaceholderAPI pour ChroniclesSkills.
 * Placeholders :
 * %chroniclesmastery_lourde_level% - Niveau pour les armes lourdes
 * %chroniclesmastery_lourde_xp% - XP actuel pour les armes lourdes
 * %chroniclesmastery_moyenne_level% - Niveau pour les armes moyennes
 * %chroniclesmastery_moyenne_xp% - XP actuel pour les armes moyennes
 * %chroniclesmastery_legere_level% - Niveau pour les armes légères
 * %chroniclesmastery_legere_xp% - XP actuel pour les armes légères
 * %chroniclesmastery_holding_type% - Type d'arme actuellement tenue
 * %chroniclesmastery_holding_level% - Niveau du type d'arme actuellement tenu
 */

public class MasteryPlaceholderExpansion extends PlaceholderExpansion {

    private final ChroniclesSkills plugin;
    private final MasteryManager masteryManager;
    private final WeaponUtils weaponUtils;

    public MasteryPlaceholderExpansion(ChroniclesSkills plugin, MasteryManager masteryManager, WeaponUtils weaponUtils) {
        this.plugin = plugin;
        this.masteryManager = masteryManager;
        this.weaponUtils = weaponUtils;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "chroniclesmastery";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getPluginMeta().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        MasteryData data = masteryManager.getData(player.getUniqueId());
        if (data == null) {
            return "0";
        }

        String lowercaseParams = params.toLowerCase();

        switch (lowercaseParams) {
            case "lourde_level" -> {
                return String.valueOf(data.getLevel(WeaponType.LOURDE));
            }
            case "lourde_xp" -> {
                return String.format("%.0f", data.getXp(WeaponType.LOURDE));
            }
            case "lourde_progress" -> {
                return getProgress(data, WeaponType.LOURDE);
            }


            case "moyenne_level" -> {
                return String.valueOf(data.getLevel(WeaponType.MOYENNE));
            }
            case "moyenne_xp" -> {
                return String.format("%.0f", data.getXp(WeaponType.MOYENNE));
            }
            case "moyenne_progress" -> {
                return getProgress(data, WeaponType.MOYENNE);
            }


            case "legere_level" -> {
                return String.valueOf(data.getLevel(WeaponType.LEGERE));
            }
            case "legere_xp" -> {
                return String.format("%.0f", data.getXp(WeaponType.LEGERE));
            }
            case "legere_progress" -> {
                return getProgress(data, WeaponType.LEGERE);
            }
        }

        if (player.isOnline() && player.getPlayer() != null) {
            Player onlinePlayer = player.getPlayer();

            switch (lowercaseParams) {
                case "holding_type" -> {
                    WeaponType type = weaponUtils.getHeldWeaponType(onlinePlayer);
                    return type != null ? type.getDisplayName() : "Aucune";
                }
                case "holding_level" -> {
                    WeaponType type = weaponUtils.getHeldWeaponType(onlinePlayer);
                    if (type != null) {
                        return String.valueOf(data.getLevel(type));
                    }
                    return "0";
                }
                case "holding_xp" -> {
                    WeaponType type = weaponUtils.getHeldWeaponType(onlinePlayer);
                    if (type != null) {
                        return String.format("%.0f", data.getXp(type));
                    }
                    return "0";
                }
                case "holding_progress" -> {
                    WeaponType type = weaponUtils.getHeldWeaponType(onlinePlayer);
                    if (type != null) {
                        return getProgress(data, type);
                    }
                    return "0%";
                }
            }

        }

        return null;
    }

    /**
     * Récupère le pourcentage de progression vers le prochain niveau pour un type d'arme donné.
     */
    private String getProgress(MasteryData data, WeaponType type) {
        int level = data.getLevel(type);
        double xp = data.getXp(type);
        double progress = masteryManager.getXpManager().getProgressPercentage(xp, level);
        return String.format("%.0f%%", progress * 100);
    }
}
