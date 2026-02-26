package fr.just_abdel.chroniclesskills.command;

import fr.just_abdel.chroniclescore.api.commands.CoreCommand;
import fr.just_abdel.chroniclesskills.command.admin.AddXpSubCommand;
import fr.just_abdel.chroniclesskills.command.admin.InfoSubCommand;
import fr.just_abdel.chroniclesskills.command.admin.ReloadSubCommand;
import fr.just_abdel.chroniclesskills.command.admin.ResetSubCommand;
import fr.just_abdel.chroniclesskills.command.admin.SetLevelSubCommand;
import fr.just_abdel.chroniclesskills.manager.MasteryManager;
import org.bukkit.plugin.Plugin;

public class MasteryAdminCommand extends CoreCommand {

    private final MasteryManager masteryManager;

    public MasteryAdminCommand(Plugin plugin, MasteryManager masteryManager) {
        super(plugin, "masteryadmin", "chronicles.mastery.admin");
        this.masteryManager = masteryManager;
    }

    @Override
    protected void registerSubCommands() {
        registerSubCommand(new SetLevelSubCommand(masteryManager));
        registerSubCommand(new AddXpSubCommand(masteryManager));
        registerSubCommand(new ResetSubCommand(masteryManager));
        registerSubCommand(new InfoSubCommand(masteryManager));
        registerSubCommand(new ReloadSubCommand(masteryManager));
    }
}
