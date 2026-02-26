package fr.just_abdel.chroniclesskills.config;

import fr.just_abdel.chroniclescore.api.config.CoreConfig;
import org.bukkit.plugin.Plugin;

public class MessagesConfig extends CoreConfig {

    private final ColorConfig colorConfig;

    public MessagesConfig(Plugin plugin, ColorConfig colorConfig) {
        super(plugin, "messages.yml");
        this.colorConfig = colorConfig;
    }

    public String getLevelUp() {
        return colorConfig.applyColors(get().getString("level-up", "<gold>Tu as atteint le niveau <level> en <weapon>!"));
    }

    public String getXpGain() {
        return colorConfig.applyColors(get().getString("xp-gain", "<secondary>+<xp> XP"));
    }

    public String getXpGainActionBar() {
        return colorConfig.applyColors(get().getString("xp-gain-actionbar",
                "<gold>+<xp> XP <gray>| <secondary><weapon> <gray>| <secondary>Niv.<level> <gray>| <gold><current_xp>/<required_xp>"));
    }

    public String getPlayerNotFound() {
        return colorConfig.applyColors(get().getString("player-not-found", "<error>Joueur introuvable!"));
    }

    public String getInvalidWeaponType() {
        return colorConfig.applyColors(get().getString("invalid-weapon-type", "<error>Type d'arme invalide!"));
    }

    public String getReloadSuccess() {
        return colorConfig.applyColors(get().getString("reload-success", "<success>Configuration rechargee!"));
    }

    public String getResetSuccess() {
        return colorConfig.applyColors(get().getString("reset-success", "<success>Donnees de maitrise de <player> reinitialisees!"));
    }

    public String getSetLevelSuccess() {
        return colorConfig.applyColors(get().getString("set-level-success", "<success>Niveau <weapon> de <player> defini a <level>!"));
    }

    public String getAddXpSuccess() {
        return colorConfig.applyColors(get().getString("add-xp-success", "<success><xp> XP ajoutes a la maitrise <weapon> de <player>!"));
    }

    public String getStatsHeader() {
        return colorConfig.applyColors(get().getString("stats-header", "<gold>=== Stats Maitrise ==="));
    }

    public String getStatsLine() {
        return colorConfig.applyColors(get().getString("stats-line", "<secondary><weapon>: Niveau <level> (<xp>/<required> XP)"));
    }

    public String getToastLevelUpSubtitle() {
        return colorConfig.applyColors(get().getString("toast.level-up-subtitle", "<weapon> Niveau <level>"));
    }
}
