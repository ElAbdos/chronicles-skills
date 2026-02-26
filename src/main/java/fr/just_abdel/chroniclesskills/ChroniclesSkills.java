package fr.just_abdel.chroniclesskills;

import fr.just_abdel.chroniclescore.api.database.CoreDatabase;
import fr.just_abdel.chroniclescore.api.scheduler.CoreScheduler;
import fr.just_abdel.chroniclesskills.command.MasteryAdminCommand;
import fr.just_abdel.chroniclesskills.command.MasteryCommand;
import fr.just_abdel.chroniclesskills.config.MasteryConfig;
import fr.just_abdel.chroniclesskills.config.MessagesConfig;
import fr.just_abdel.chroniclesskills.gui.MasteryMenuGUI;
import fr.just_abdel.chroniclesskills.integration.MMOItemsIntegration;
import fr.just_abdel.chroniclesskills.integration.MythicMobsIntegration;
import fr.just_abdel.chroniclesskills.listener.CombatListener;
import fr.just_abdel.chroniclesskills.listener.PlayerJoinQuitListener;
import fr.just_abdel.chroniclesskills.manager.MasteryManager;
import fr.just_abdel.chroniclesskills.placeholder.MasteryPlaceholderExpansion;
import fr.just_abdel.chroniclesskills.repository.MasteryRepository;
import fr.just_abdel.chroniclesskills.util.WeaponUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.Objects;


@Getter
public final class ChroniclesSkills extends JavaPlugin {

    @Getter private static ChroniclesSkills instance;
    private MasteryConfig masteryConfig;
    private MessagesConfig messagesConfig;
    private MasteryRepository masteryRepository;
    private MasteryManager masteryManager;
    private MMOItemsIntegration mmoItemsIntegration;
    private MythicMobsIntegration mythicMobsIntegration;
    private MasteryMenuGUI masteryMenuGUI;
    private WeaponUtils weaponUtils;

    @Override
    public void onEnable() {
        instance = this;

        if (!CoreDatabase.isAvailable()) {
            getLogger().severe("CoreDatabase n'est pas initialise!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();
        masteryConfig = new MasteryConfig(this);
        messagesConfig = new MessagesConfig(this);


        createDatabaseTable();
        masteryRepository = new MasteryRepository(CoreDatabase.get());
        masteryManager = new MasteryManager(masteryRepository, masteryConfig, messagesConfig);
        weaponUtils = new WeaponUtils();
        initializeIntegrations();

        masteryMenuGUI = new MasteryMenuGUI(this, masteryManager);

        registerCommands();
        registerListeners();
        registerPlaceholders();
        startAutoSaveTask();
        getLogger().info("ChroniclesSkills a ete active avec succes!");
    }

    @Override
    public void onDisable() {
        if (masteryManager != null) {
            masteryManager.saveAll();
            getLogger().info("Toutes les donnees de maitrise ont ete sauvegardees.");
        }

        getLogger().info("ChroniclesSkills a ete desactive.");
    }

    /**
     * Crée la table de base de données pour stocker les données de maîtrise si elle n'existe pas déjà.
     */
    private void createDatabaseTable() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS mastery_data (
                id INT AUTO_INCREMENT PRIMARY KEY,
                player_uuid VARCHAR(36) NOT NULL UNIQUE,
                lourde_level INT NOT NULL DEFAULT 1,
                lourde_xp DOUBLE NOT NULL DEFAULT 0.0,
                moyenne_level INT NOT NULL DEFAULT 1,
                moyenne_xp DOUBLE NOT NULL DEFAULT 0.0,
                legere_level INT NOT NULL DEFAULT 1,
                legere_xp DOUBLE NOT NULL DEFAULT 0.0,
                INDEX idx_player_uuid (player_uuid)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
            """;

        try {
            CoreDatabase.get().executeSync(createTableSQL);
            getLogger().info("Table mastery_data verifiee/creee avec succes.");
        } catch (SQLException e) {
            getLogger().severe("Erreur lors de la creation de la table mastery_data: " + e.getMessage());
        }
    }

    /**
     * Initialise les intégrations avec d'autres plugins (MMOItems, MythicMobs, etc.) en fonction de la configuration.
     */
    private void initializeIntegrations() {
        mmoItemsIntegration = new MMOItemsIntegration(masteryConfig);
        if (mmoItemsIntegration.isEnabled()) {
            weaponUtils.setMmoItemsIntegration(mmoItemsIntegration);
            getLogger().info("MMOItems integre avec succes.");
        }

        mythicMobsIntegration = new MythicMobsIntegration();
        if (mythicMobsIntegration.isEnabled()) {
            getLogger().info("MythicMobs integre avec succes.");
        }
    }

    /**
     * Enregistre les commandes du plugin et leurs tab completers.
     */
    private void registerCommands() {
        MasteryCommand masteryCommand = new MasteryCommand(this, masteryManager, masteryMenuGUI);
        Objects.requireNonNull(getCommand("mastery")).setExecutor(masteryCommand);
        Objects.requireNonNull(getCommand("mastery")).setTabCompleter(masteryCommand);

        MasteryAdminCommand adminCommand = new MasteryAdminCommand(this, masteryManager);
        Objects.requireNonNull(getCommand("masteryadmin")).setExecutor(adminCommand);
        Objects.requireNonNull(getCommand("masteryadmin")).setTabCompleter(adminCommand);
    }

    /**
     * Enregistre les listneers
     */
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new CombatListener(masteryManager, weaponUtils, messagesConfig, mythicMobsIntegration, this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(masteryManager), this);
    }

    /**
     * Enregistre les placeholders
     */
    private void registerPlaceholders() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new MasteryPlaceholderExpansion(this, masteryManager, weaponUtils).register();
            getLogger().info("PlaceholderAPI integre avec succes.");
        }
    }

    /**
     * Demarre la sauvegarde automatique
     */
    private void startAutoSaveTask() {
        int intervalMinutes = masteryConfig.getAutoSaveInterval();
        long intervalTicks = intervalMinutes * 60 * 20L;

        CoreScheduler.get().runAsyncTimerTicks(() -> {
            masteryManager.saveAll();
            getLogger().fine("Auto-sauvegarde des donnees de maitrise effectuee.");
        }, intervalTicks, intervalTicks);

        getLogger().info("Auto-sauvegarde configuree toutes les " + intervalMinutes + " minutes.");
    }
}
