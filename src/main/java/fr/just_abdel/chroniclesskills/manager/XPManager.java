package fr.just_abdel.chroniclesskills.manager;

import fr.just_abdel.chroniclesskills.config.MasteryConfig;

public class XPManager {

    private final MasteryConfig config;

    public XPManager(MasteryConfig config) {
        this.config = config;
    }

    /**
     * Calcul l'XP requis pour atteindre le niveau suivant en fonction du niveau actuel.
     */
    public double getRequiredXp(int currentLevel) {
        double base = config.getXpBase();
        double multiplier = config.getXpMultiplier();
        double exponent = config.getXpExponent();

        return base * Math.pow(multiplier, currentLevel - 1) * Math.pow(currentLevel, exponent);
    }

    /**
     * Récupère l'XP gagnée pour tuer un mob, en vérifiant d'abord les configurations spécifiques aux MythicMobs.
     */
    public double getMythicMobXp(String mythicMobType) {
        Double specificXp = config.getMythicMobXp().get(mythicMobType);
        if (specificXp != null) {
            return specificXp;
        }
        return config.getDefaultMobXp();
    }

    /**
     * Calcul le pourcentage de progression vers le niveau suivant en fonction de l'XP actuelle et du niveau actuel.
     */
    public double getProgressPercentage(double currentXp, int currentLevel) {
        double required = getRequiredXp(currentLevel);
        if (required <= 0) return 1.0;
        return Math.min(currentXp / required, 1.0);
    }

    /**
     * Créé une barre de progression textuelle pour afficher la progression vers le niveau suivant, en utilisant des caractères colorés pour représenter la partie remplie et la partie vide.
     */
    public String getProgressBar(double currentXp, int currentLevel, int length) {
        double progress = getProgressPercentage(currentXp, currentLevel);
        int filled = (int) (progress * length);
        int empty = length - filled;

        return "<b><color:#92bed8>" +
                "█".repeat(Math.max(0, filled)) +
                "</color><color:#4a4a4a>" +
                "█".repeat(Math.max(0, empty)) +
                "</color></b>";
    }
}
