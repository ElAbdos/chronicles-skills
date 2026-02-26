package fr.just_abdel.chroniclesskills.command.admin;

import fr.just_abdel.chroniclescore.api.commands.CommandContext;
import fr.just_abdel.chroniclescore.api.commands.CoreSubCommand;
import fr.just_abdel.chroniclescore.api.messaging.CoreMessenger;
import fr.just_abdel.chroniclesskills.config.MessagesConfig;
import fr.just_abdel.chroniclesskills.manager.MasteryManager;

public class ReloadSubCommand implements CoreSubCommand {

    private final MasteryManager masteryManager;
    private final MessagesConfig messages;

    public ReloadSubCommand(MasteryManager masteryManager) {
        this.masteryManager = masteryManager;
        this.messages = masteryManager.getMessages();
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getPermission() {
        return "chronicles.mastery.admin.reload";
    }

    @Override
    public String getDescription() {
        return "Recharge la configuration";
    }

    @Override
    public String getUsage() {
        return "/masteryadmin reload";
    }

    @Override
    public void execute(CommandContext ctx) {
        masteryManager.getConfig().reload();
        masteryManager.getMessages().reload();

        String message = messages.getReloadSuccess().replace("<prefix>", messages.getPrefix());
        CoreMessenger.get().send(ctx.sender(), message);
    }
}
