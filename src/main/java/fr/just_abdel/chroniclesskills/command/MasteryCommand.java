package fr.just_abdel.chroniclesskills.command;

import fr.just_abdel.chroniclescore.api.commands.CommandContext;
import fr.just_abdel.chroniclescore.api.commands.CoreCommand;
import fr.just_abdel.chroniclescore.api.commands.CoreSubCommand;
import fr.just_abdel.chroniclesskills.gui.MasteryMenuGUI;
import fr.just_abdel.chroniclesskills.manager.MasteryManager;
import fr.just_abdel.chroniclesskills.model.MasteryData;
import fr.just_abdel.chroniclesskills.model.WeaponType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class MasteryCommand extends CoreCommand {

    private final MasteryManager masteryManager;
    private final MasteryMenuGUI menuGUI;

    public MasteryCommand(Plugin plugin, MasteryManager masteryManager, MasteryMenuGUI menuGUI) {
        super(plugin, "mastery", "chronicles.mastery");
        this.masteryManager = masteryManager;
        this.menuGUI = menuGUI;
    }

    @Override
    protected void registerSubCommands() {
        registerSubCommand(new GUISubCommand());
        registerSubCommand(new StatsSubCommand());
        registerSubCommand(new HelpSubCommand());
    }

    /**
     * Ouvre le menu de maîtrise pour le joueur.
     */
    private class GUISubCommand implements CoreSubCommand {
        @Override
        public String getName() {
            return "gui";
        }

        @Override
        public String[] getAliases() {
            return new String[]{"menu", "m"};
        }

        @Override
        public String getDescription() {
            return "Ouvre le menu de maîtrise";
        }

        @Override
        public boolean isPlayerOnly() {
            return true;
        }

        @Override
        public void execute(fr.just_abdel.chroniclescore.api.commands.CommandContext ctx) {
            Player player = (Player) ctx.sender();
            menuGUI.open(player);
        }
    }

    /**
     * Montre les statistiques de maîtrise du joueur dans le chat.
     */
    private class StatsSubCommand implements CoreSubCommand {
        @Override
        public String getName() {
            return "stats";
        }

        @Override
        public String[] getAliases() {
            return new String[]{"info", "i"};
        }

        @Override
        public String getDescription() {
            return "Affiche vos statistiques de maîtrise";
        }

        @Override
        public boolean isPlayerOnly() {
            return true;
        }

        @Override
        public void execute(fr.just_abdel.chroniclescore.api.commands.CommandContext ctx) {
            Player player = (Player) ctx.sender();
            MasteryData data = masteryManager.getData(player.getUniqueId());

            String header = masteryManager.getMessages().getStatsHeader();
            String line = masteryManager.getMessages().getStatsLine();

            player.sendRichMessage(header);

            for (WeaponType type : WeaponType.values()) {
                int level = data.getLevel(type);
                double xp = data.getXp(type);
                double required = masteryManager.getXpManager().getRequiredXp(level);

                String formattedLine = line
                        .replace("<weapon>", type.getDisplayName())
                        .replace("<level>", String.valueOf(level))
                        .replace("<xp>", String.format("%.0f", xp))
                        .replace("<required>", String.format("%.0f", required));

                player.sendRichMessage(formattedLine);
            }
        }
    }

    /**
     * Montre l'aide pour la commande de maîtrise.
     */
    private static class HelpSubCommand implements CoreSubCommand {
        @Override
        public String getName() {
            return "help";
        }

        @Override
        public String[] getAliases() {
            return new String[]{"?"};
        }

        @Override
        public String getDescription() {
            return "Affiche l'aide";
        }

        @Override
        public void execute(CommandContext ctx) {
            ctx.sender().sendRichMessage("<color:#FFF6A8>=== Maîtrise d'Armes ===");
            ctx.sender().sendRichMessage("<color:#92bed8>/mastery</color> <color:#8c8c8c>- Ouvre le menu");
            ctx.sender().sendRichMessage("<color:#92bed8>/mastery stats</color> <color:#8c8c8c>- Vos statistiques");
            ctx.sender().sendRichMessage("<color:#92bed8>/mastery gui</color> <color:#8c8c8c>- Ouvre le menu GUI");
        }
    }
}
