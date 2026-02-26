package fr.just_abdel.chroniclesskills.command.admin;

import fr.just_abdel.chroniclescore.api.commands.CommandContext;
import fr.just_abdel.chroniclescore.api.commands.CoreSubCommand;
import fr.just_abdel.chroniclescore.api.messaging.CoreMessenger;
import fr.just_abdel.chroniclesskills.config.MessagesConfig;
import fr.just_abdel.chroniclesskills.manager.MasteryManager;
import fr.just_abdel.chroniclesskills.model.MasteryData;
import fr.just_abdel.chroniclesskills.model.WeaponType;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class InfoSubCommand implements CoreSubCommand {

    private final MasteryManager masteryManager;
    private final MessagesConfig messages;

    public InfoSubCommand(MasteryManager masteryManager) {
        this.masteryManager = masteryManager;
        this.messages = masteryManager.getMessages();
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getPermission() {
        return "chronicles.mastery.admin.info";
    }

    @Override
    public String getDescription() {
        return "Affiche les infos de maîtrise d'un joueur";
    }

    @Override
    public String getUsage() {
        return "/masteryadmin info <player>";
    }

    @Override
    public void execute(CommandContext ctx) {
        if (ctx.argCount() < 1) {
            CoreMessenger.get().send(ctx.sender(), "<color:#E57373>Usage: " + getUsage());
            return;
        }

        String playerName = ctx.args().getFirst();
        OfflinePlayer target = CommandUtils.getValidPlayer(ctx.sender(), playerName, messages);
        if (target == null) return;

        MasteryData data = masteryManager.getData(target.getUniqueId());

        ctx.sender().sendRichMessage("<color:#FFF6A8>=== " + playerName + " - Maîtrise ===");

        for (WeaponType type : WeaponType.values()) {
            int level = data.getLevel(type);
            double xp = data.getXp(type);
            double required = masteryManager.getXpManager().getRequiredXp(level);

            ctx.sender().sendRichMessage("<color:#92bed8>" + type.getDisplayName() +
                    ": Level " + level + " (" + String.format("%.0f", xp) + "/" + String.format("%.0f", required) + " XP)");
        }
    }

    @Override
    public List<String> onTabComplete(CommandContext ctx) {
        if (ctx.argCount() == 1) {
            return null;
        }
        return List.of();
    }
}
