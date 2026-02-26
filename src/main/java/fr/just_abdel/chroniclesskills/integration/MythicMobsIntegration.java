package fr.just_abdel.chroniclesskills.integration;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

import java.util.Optional;

public class MythicMobsIntegration {

    @Getter private boolean enabled = false;

    public MythicMobsIntegration() {
        checkEnabled();
    }

    /**
     * Vérifie si MythicMobs est présent et active l'intégration en conséquence.
     */
    private void checkEnabled() {
        enabled = Bukkit.getPluginManager().getPlugin("MythicMobs") != null;
        if (enabled) {
            Bukkit.getLogger().info("[ChroniclesSkills] MythicMobs detecte et integre avec succes");
        } else {
            Bukkit.getLogger().info("[ChroniclesSkills] MythicMobs non installe - integration desactivee");
        }
    }

    /**
     * Récupère le nom du type MythicMob d'une entité.
     */
    public String getMythicMobName(LivingEntity entity) {
        if (!enabled || entity == null) {
            return null;
        }

        Optional<ActiveMob> activeMob = MythicBukkit.inst().getMobManager().getActiveMob(entity.getUniqueId());
        return activeMob.map(mob -> mob.getType().getInternalName()).orElse(null);
    }
}
