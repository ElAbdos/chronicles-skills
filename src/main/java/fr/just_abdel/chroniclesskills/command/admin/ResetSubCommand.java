package fr.just_abdel.chroniclesskills.command.admin;

import fr.just_abdel.chroniclescore.api.commands.CommandContext;
import fr.just_abdel.chroniclescore.api.commands.CoreSubCommand;
import fr.just_abdel.chroniclescore.api.messaging.CoreMessenger;
import fr.just_abdel.chroniclesskills.config.MessagesConfig;
import fr.just_abdel.chroniclesskills.manager.MasteryManager;
import fr.just_abdel.chroniclesskills.model.WeaponType;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class ResetSubCommand implements CoreSubCommand {

    private final MasteryManager masteryManager;
    private final MessagesConfig messages;

    public ResetSubCommand(MasteryManager masteryManager) {
        this.masteryManager = masteryManager;
        this.messages = masteryManager.getMessages();
    }

    @Override
    public String getName() {
        return "reset";
    }

    @Override
    public String getPermission() {
        return "chronicles.mastery.admin.reset";
    }

    @Override
    public String getDescription() {
        return "Réinitialise les données de maîtrise d'un joueur";
    }

    @Override
    public String getUsage() {
        return "/masteryadmin reset <player> [type]";
    }

    @Override
    public void execute(CommandContext ctx) {
        if (ctx.argCount() < 1) {
            CoreMessenger.get().send(ctx.sender(), "<color:#E57373>Usage: " + getUsage());
            return;
        }

        String playerName = ctx.args().get(0);
        WeaponType type = null;

        if (ctx.argCount() >= 2) {
            type = CommandUtils.getValidWeaponType(ctx.sender(), ctx.args().get(1), messages);
            if (type == null) return;
        }

        OfflinePlayer target = CommandUtils.getValidPlayer(ctx.sender(), playerName, messages);
        if (target == null) return;

        masteryManager.reset(target.getUniqueId(), type);

        String message = messages.getResetSuccess()
                .replace("<player>", playerName);

        CoreMessenger.get().send(ctx.sender(), message);
    }

    @Override
    public List<String> onTabComplete(CommandContext ctx) {
        if (ctx.argCount() == 1) {
            return null;
        } else if (ctx.argCount() == 2) {
            return CommandUtils.getWeaponTypeCompletions();
        }
        return List.of();
    }
}
