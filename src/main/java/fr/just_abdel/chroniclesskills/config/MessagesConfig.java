package fr.just_abdel.chroniclesskills.config;

import fr.just_abdel.chroniclescore.api.config.CoreConfig;
import org.bukkit.plugin.Plugin;


public class MessagesConfig extends CoreConfig {

    public MessagesConfig(Plugin plugin) {
        super(plugin, "messages.yml");
    }

    public String getPrefix() {
        return get().getString("prefix", "<color:#92bed8>[Mastery]</color> ");
    }

    public String getLevelUp() {
        return get().getString("level-up", "<prefix><color:#FFF6A8>You reached level <level> in <weapon>!");
    }

    public String getXpGain() {
        return get().getString("xp-gain", "<color:#92bed8>+<xp> XP</color>");
    }

    public String getPlayerNotFound() {
        return get().getString("player-not-found", "<prefix><color:#E57373>Player not found!");
    }

    public String getInvalidWeaponType() {
        return get().getString("invalid-weapon-type", "<prefix><color:#E57373>Invalid weapon type!");
    }

    public String getReloadSuccess() {
        return get().getString("reload-success", "<prefix><color:#92bed8>Configuration reloaded!");
    }

    public String getResetSuccess() {
        return get().getString("reset-success", "<prefix><color:#92bed8>Reset <player>'s mastery data!");
    }

    public String getSetLevelSuccess() {
        return get().getString("set-level-success", "<prefix><color:#92bed8>Set <player>'s <weapon> level to <level>!");
    }

    public String getAddXpSuccess() {
        return get().getString("add-xp-success", "<prefix><color:#92bed8>Added <xp> XP to <player>'s <weapon> mastery!");
    }

    public String getStatsHeader() {
        return get().getString("stats-header", "<color:#FFF6A8>=== Mastery Stats ===");
    }

    public String getStatsLine() {
        return get().getString("stats-line", "<color:#92bed8><weapon>: Level <level> (<xp>/<required> XP)");
    }

    public String getToastLevelUpSubtitle() {
        return get().getString("toast.level-up-subtitle", "<weapon> Level <level>");
    }
}
