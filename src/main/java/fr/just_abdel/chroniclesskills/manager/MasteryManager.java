package fr.just_abdel.chroniclesskills.manager;

import fr.just_abdel.chroniclescore.api.toasts.ChroniclesToastAPI;
import fr.just_abdel.chroniclesskills.config.GUIConfig;
import fr.just_abdel.chroniclesskills.config.MasteryConfig;
import fr.just_abdel.chroniclesskills.config.MessagesConfig;
import fr.just_abdel.chroniclesskills.model.MasteryData;
import fr.just_abdel.chroniclesskills.model.WeaponType;
import fr.just_abdel.chroniclesskills.repository.MasteryRepository;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MasteryManager {

    private final MasteryRepository repository;
    @Getter private final MasteryConfig config;
    @Getter private final MessagesConfig messages;
    @Getter private final GUIConfig guiConfig;
    @Getter private final XPManager xpManager;
    private final Map<UUID, MasteryData> cache = new ConcurrentHashMap<>();

    public MasteryManager(MasteryRepository repository, MasteryConfig config, MessagesConfig messages, GUIConfig guiConfig) {
        this.repository = repository;
        this.config = config;
        this.messages = messages;
        this.guiConfig = guiConfig;
        this.xpManager = new XPManager(config);
    }

    /**
     * Charge les données de maîtrise d'un joueur depuis la base de données.
     */
    public MasteryData loadPlayerData(UUID playerUuid) {
        return repository.getOrCreate(playerUuid).join();
    }


    /**
     * Récupère les données de maîtrise d'un joueur depuis le cache, ou les charge si elles ne sont pas présentes.
     */
    public MasteryData getData(UUID playerUuid) {
        MasteryData data = cache.get(playerUuid);
        if (data == null) {
            data = loadPlayerData(playerUuid);
            cache.put(playerUuid, data);
        }
        return data;
    }

    /**
     * Charge les données de maîtrise d'un joueur et les met en cache (appelé à la connexion).
     */
    public void cachePlayer(UUID playerUuid) {
        MasteryData data = loadPlayerData(playerUuid);
        cache.put(playerUuid, data);
    }

    /**
     * Supprime les données de maîtrise d'un joueur du cache et les sauvegarde (appelé à la déconnexion).
     */
    public void uncachePlayer(UUID playerUuid) {
        MasteryData data = cache.remove(playerUuid);
        if (data != null) {
            savePlayerData(data);
        }
    }

    /**
     * Sauvegarde les données de maîtrise d'un joueur dans la base de données.
     */
    public void savePlayerData(MasteryData data) {
        repository.save(data).join();
    }

    /**
     * Sauvegarde les données de maîtrise de tous les joueurs en cache dans la base de données.
     */
    public void saveAll() {
        for (MasteryData data : cache.values()) {
            savePlayerData(data);
        }
    }

    /**
     * Donne de l'XP à un joueur pour un type d'arme donné, en gérant les niveaux et les notifications.
     */
    public void giveXp(Player player, WeaponType type, double amount) {
        MasteryData data = getData(player.getUniqueId());
        if (data == null) return;

        int currentLevel = data.getLevel(type);
        int maxLevel = config.getMaxLevelForWorld(player.getWorld().getName());

        if (currentLevel >= maxLevel) {
            return;
        }

        data.addXp(type, amount);
        checkLevelUp(player, data, type, maxLevel);
    }

    /**
     * Vérifie si un joueur a suffisamment d'XP pour monter de niveau, et g
     */
    private void checkLevelUp(Player player, MasteryData data, WeaponType type, int maxLevel) {
        int currentLevel = data.getLevel(type);
        double currentXp = data.getXp(type);
        double requiredXp = xpManager.getRequiredXp(currentLevel);

        while (currentXp >= requiredXp && currentLevel < maxLevel) {
            currentLevel++;
            currentXp -= requiredXp;

            data.setLevel(type, currentLevel);
            data.setXp(type, currentXp);

            sendLevelUpNotification(player, type, currentLevel);
            requiredXp = xpManager.getRequiredXp(currentLevel);
        }
        if (currentLevel >= maxLevel) {
            data.setXp(type, 0);
        }
    }

    /**
     * Envoie une notification de montée de niveau au joueur, à la fois en toast et en chat.
     */
    private void sendLevelUpNotification(Player player, WeaponType type, int newLevel) {
        ChroniclesToastAPI toast = Bukkit.getServicesManager().load(ChroniclesToastAPI.class);
        if (toast != null) {
            String subtitle = messages.getToastLevelUpSubtitle()
                    .replace("<weapon>", type.getDisplayName())
                    .replace("<level>", String.valueOf(newLevel));
            toast.sendToast(player, Component.text(subtitle), Material.DIAMOND_SWORD, 0, ChroniclesToastAPI.FrameType.GOAL);
        }

        String message = messages.getLevelUp()
                .replace("<prefix>", messages.getPrefix())
                .replace("<level>", String.valueOf(newLevel))
                .replace("<weapon>", type.getDisplayName());

        player.sendRichMessage(message);
    }

    /**
     * Set le niveau de maîtrise d'un joueur pour un type d'arme donné, en réinitialisant l'XP.
     */
    public void setLevel(UUID playerUuid, WeaponType type, int level) {
        MasteryData data = getData(playerUuid);
        if (data != null) {
            data.setLevel(type, Math.max(1, level));
            data.setXp(type, 0);
        }
    }

    /**
     * Ajoute de l'XP de maîtrise à un joueur pour un type d'arme donné, sans gérer les niveaux (utilisé pour les commandes d'administration).
     */
    public void addXp(UUID playerUuid, WeaponType type, double amount) {
        MasteryData data = getData(playerUuid);
        if (data != null) {
            data.addXp(type, amount);
        }
    }

    /**
     * Reset le niveau de maîtrise d'un joueur pour un type d'arme donné, ou tous les types si le type est null.
     */
    public void reset(UUID playerUuid, WeaponType type) {
        MasteryData data = getData(playerUuid);
        if (data != null) {
            if (type == null) {
                data.resetAll();
            } else {
                data.reset(type);
            }
        }
    }
}
