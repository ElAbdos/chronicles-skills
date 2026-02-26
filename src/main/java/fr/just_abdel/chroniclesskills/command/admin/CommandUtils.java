package fr.just_abdel.chroniclesskills.command.admin;

import fr.just_abdel.chroniclescore.api.messaging.CoreMessenger;
import fr.just_abdel.chroniclesskills.config.MessagesConfig;
import fr.just_abdel.chroniclesskills.model.WeaponType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class CommandUtils {

    private CommandUtils() {}

    public static OfflinePlayer getValidPlayer(CommandSender sender, String playerName, MessagesConfig messages) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);
        if (!target.hasPlayedBefore() && !target.isOnline()) {
            String message = messages.getPlayerNotFound().replace("<prefix>", messages.getPrefix());
            CoreMessenger.get().send(sender, message);
            return null;
        }
        return target;
    }

    public static WeaponType getValidWeaponType(CommandSender sender, String typeName, MessagesConfig messages) {
        WeaponType type = WeaponType.fromKey(typeName);
        if (type == null) {
            String message = messages.getInvalidWeaponType().replace("<prefix>", messages.getPrefix());
            CoreMessenger.get().send(sender, message);
            return null;
        }
        return type;
    }

    public static List<String> getWeaponTypeCompletions() {
        return Arrays.stream(WeaponType.values())
                .map(WeaponType::getKey)
                .collect(Collectors.toList());
    }
}
