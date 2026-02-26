package fr.just_abdel.chroniclesskills.listener;

import fr.just_abdel.chroniclescore.api.scheduler.CoreScheduler;
import fr.just_abdel.chroniclesskills.manager.MasteryManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class PlayerJoinQuitListener implements Listener {

    private final MasteryManager masteryManager;

    public PlayerJoinQuitListener(MasteryManager masteryManager) {
        this.masteryManager = masteryManager;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        CoreScheduler.get().runAsync(() -> masteryManager.cachePlayer(event.getPlayer().getUniqueId()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        masteryManager.uncachePlayer(event.getPlayer().getUniqueId());
    }
}
