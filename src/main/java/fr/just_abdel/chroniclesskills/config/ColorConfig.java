package fr.just_abdel.chroniclesskills.config;

import fr.just_abdel.chroniclescore.api.config.CoreConfig;
import org.bukkit.plugin.Plugin;

public class ColorConfig extends CoreConfig {

    public ColorConfig(Plugin plugin) {
        super(plugin, "config.yml");
    }

    public String getPrimary() {
        return get().getString("colors.primary", "d1a9ff");
    }

    public String getSecondary() {
        return get().getString("colors.secondary", "92bed8");
    }

    public String getSuccess() {
        return get().getString("colors.success", "bfe86d");
    }

    public String getError() {
        return get().getString("colors.error", "E57373");
    }

    public String getAccent() {
        return get().getString("colors.accent", "ffacd5");
    }

    public String getGold() {
        return get().getString("colors.gold", "FFF6A8");
    }

    public String getGray() {
        return get().getString("colors.gray", "8c8c8c");
    }

    /**
     * Remplace les placeholders de couleur par les vraies couleurs configurées.
     */
    public String applyColors(String message) {
        if (message == null) return null;
        return message
                .replace("<primary>", "<color:#" + getPrimary() + ">")
                .replace("<secondary>", "<color:#" + getSecondary() + ">")
                .replace("<success>", "<color:#" + getSuccess() + ">")
                .replace("<error>", "<color:#" + getError() + ">")
                .replace("<accent>", "<color:#" + getAccent() + ">")
                .replace("<gold>", "<color:#" + getGold() + ">")
                .replace("<gray>", "<color:#" + getGray() + ">");
    }
}
