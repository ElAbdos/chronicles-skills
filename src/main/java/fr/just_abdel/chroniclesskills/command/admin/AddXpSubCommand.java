package fr.just_abdel.chroniclesskills.command.admin;

import fr.just_abdel.chroniclescore.api.commands.CommandContext;
import fr.just_abdel.chroniclescore.api.commands.CoreSubCommand;
import fr.just_abdel.chroniclescore.api.messaging.CoreMessenger;
import fr.just_abdel.chroniclesskills.config.MessagesConfig;
import fr.just_abdel.chroniclesskills.manager.MasteryManager;
import fr.just_abdel.chroniclesskills.model.WeaponType;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class AddXpSubCommand implements CoreSubCommand {

    private final MasteryManager masteryManager;
    private final MessagesConfig messages;

    public AddXpSubCommand(MasteryManager masteryManager) {
        this.masteryManager = masteryManager;
        this.messages = masteryManager.getMessages();
    }

    @Override
    public String getName() {
        return "addxp";
    }

    @Override
    public String getPermission() {
        return "chronicles.mastery.admin.addxp";
    }

    @Override
    public String getDescription() {
        return "Ajoute de l'XP à un joueur";
    }

    @Override
    public String getUsage() {
        return "/masteryadmin addxp <player> <type> <amount>";
    }

    @Override
    public void execute(CommandContext ctx) {
        if (ctx.argCount() < 3) {
            CoreMessenger.get().send(ctx.sender(), "<color:#E57373>Usage: " + getUsage());
            return;
        }

        String playerName = ctx.args().get(0);
        String typeName = ctx.args().get(1);
        double amount;

        try {
            amount = Double.parseDouble(ctx.args().get(2));
        } catch (NumberFormatException e) {
            CoreMessenger.get().send(ctx.sender(), "<color:#E57373>Le montant doit être un nombre!");
            return;
        }

        WeaponType type = CommandUtils.getValidWeaponType(ctx.sender(), typeName, messages);
        if (type == null) return;

        OfflinePlayer target = CommandUtils.getValidPlayer(ctx.sender(), playerName, messages);
        if (target == null) return;

        masteryManager.addXp(target.getUniqueId(), type, amount);

        String message = messages.getAddXpSuccess()
                .replace("<player>", playerName)
                .replace("<weapon>", type.getDisplayName())
                .replace("<xp>", String.format("%.0f", amount));

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
