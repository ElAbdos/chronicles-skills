package fr.just_abdel.chroniclesskills.listener;

import fr.just_abdel.chroniclesskills.config.MessagesConfig;
import fr.just_abdel.chroniclesskills.integration.MythicMobsIntegration;
import fr.just_abdel.chroniclesskills.manager.MasteryManager;
import fr.just_abdel.chroniclesskills.manager.XPManager;
import fr.just_abdel.chroniclesskills.model.MasteryData;
import fr.just_abdel.chroniclesskills.model.WeaponType;
import fr.just_abdel.chroniclesskills.util.WeaponUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class CombatListener implements Listener {

    private final MasteryManager masteryManager;
    private final WeaponUtils weaponUtils;
    private final MessagesConfig messages;
    private final MythicMobsIntegration mythicMobsIntegration;
    private final Plugin plugin;
    private final Map<UUID, CoalescedBar> actionBarTasks = new ConcurrentHashMap<>();
    private static final long ACTION_BAR_TICKS = 4L;
    private static final int ACTION_BAR_DURATION = 15;

    private static class CoalescedBar {
        volatile Component message;
        final AtomicInteger remaining;

        CoalescedBar(Component message, int remaining) {
                this.message = message;
                this.remaining = new AtomicInteger(remaining);
            }
        }

    public CombatListener(MasteryManager masteryManager, WeaponUtils weaponUtils,
                         MessagesConfig messages, MythicMobsIntegration mythicMobsIntegration, Plugin plugin) {
        this.masteryManager = masteryManager;
        this.weaponUtils = weaponUtils;
        this.messages = messages;
        this.mythicMobsIntegration = mythicMobsIntegration;
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMobKill(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();

        if (killer == null) {
            return;
        }

        if (entity instanceof Player) {
            return;
        }

        ItemStack weapon = killer.getInventory().getItemInMainHand();
        WeaponType weaponType = weaponUtils.detectWeaponType(weapon);

        if (weaponType == null) {
            return;
        }

        XPManager xpManager = masteryManager.getXpManager();
        double xp = 0;

        // Vérifier si c'est un MythicMob
        if (mythicMobsIntegration != null && mythicMobsIntegration.isEnabled()) {
            String mythicMobType = mythicMobsIntegration.getMythicMobName(entity);
            if (mythicMobType != null) {
                plugin.getLogger().info("[DEBUG] MythicMob détecté: " + mythicMobType);
                xp = xpManager.getMythicMobXp(mythicMobType);
            }
        }

        // Si pas de XP MythicMob, utiliser le XP par défaut pour les mobs vanilla
        if (xp <= 0) {
            xp = masteryManager.getConfig().getDefaultMobXp();
        }

        if (xp <= 0) {
            return;
        }

        masteryManager.giveXp(killer, weaponType, xp);
        showXpGainActionBar(killer, xp, weaponType);
    }

    /**
     * Montre l'xp gagné dans l'action bar avec les informations de progression.
     */
    private void showXpGainActionBar(Player player, double xp, WeaponType weaponType) {
        MasteryData data = masteryManager.getData(player.getUniqueId());
        XPManager xpManager = masteryManager.getXpManager();

        int level = data.getLevel(weaponType);
        double currentXp = data.getXp(weaponType);
        double requiredXp = xpManager.getRequiredXp(level);

        String message = messages.getXpGainActionBar()
                .replace("<xp>", String.format("%.0f", xp))
                .replace("<weapon>", weaponType.getDisplayName())
                .replace("<level>", String.valueOf(level))
                .replace("<current_xp>", String.format("%.0f", currentXp))
                .replace("<required_xp>", String.format("%.0f", requiredXp));

        Component actionBarMessage = MiniMessage.miniMessage().deserialize(message);
        sendCoalescedActionBar(player, actionBarMessage);
    }

    /**
     * Envoie un message dans l'action bar en coalesçant les mises à jour pour éviter les conflits et les rafraîchissements rapides.
     */
    private void sendCoalescedActionBar(Player player, Component message) {
        if (!player.isOnline()) {
            return;
        }

        UUID playerId = player.getUniqueId();

        CoalescedBar existing = actionBarTasks.putIfAbsent(playerId, new CoalescedBar(message, ACTION_BAR_DURATION));

        if (existing == null) {
            CoalescedBar bar = actionBarTasks.get(playerId);
            if (bar == null) {
                return;
            }

            player.sendActionBar(bar.message);
            bar.remaining.decrementAndGet();

            if (bar.remaining.get() <= 0) {
                actionBarTasks.remove(playerId);
                return;
            }

            scheduleActionBarTick(player, playerId);
        } else {
            existing.message = message;
            if (existing.remaining.get() < ACTION_BAR_DURATION) {
                existing.remaining.set(ACTION_BAR_DURATION);
            }
        }
    }

    /**
     * Planifie une tâche pour mettre à jour l'action bar du joueur à intervalles réguliers, en vérifiant si le joueur est toujours en ligne et en gérant la durée restante de l'affichage.
     */
    private void scheduleActionBarTick(Player player, UUID playerId) {
        player.getScheduler().runDelayed(plugin, task -> {
            CoalescedBar bar = actionBarTasks.get(playerId);
            if (bar == null) {
                return;
            }

            if (!player.isOnline()) {
                actionBarTasks.remove(playerId);
                return;
            }

            player.sendActionBar(bar.message);
            bar.remaining.decrementAndGet();

            if (bar.remaining.get() <= 0) {
                actionBarTasks.remove(playerId);
                return;
            }

            scheduleActionBarTick(player, playerId);
        }, null, ACTION_BAR_TICKS);
    }
}
