package fr.just_abdel.chroniclesskills.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class MasteryData {

    private final UUID playerUuid;
    @Setter
    private int lourdeLevel;
    @Setter
    private double lourdeXp;
    @Setter
    private int moyenneLevel;
    @Setter
    private double moyenneXp;
    @Setter
    private int legereLevel;
    @Setter
    private double legereXp;

    public MasteryData(UUID playerUuid) {
        this.playerUuid = playerUuid;
        this.lourdeLevel = 1;
        this.lourdeXp = 0.0;
        this.moyenneLevel = 1;
        this.moyenneXp = 0.0;
        this.legereLevel = 1;
        this.legereXp = 0.0;
    }

    /**
     * Récupère le niveau de maîtrise pour un type d'arme donné.
     */
    public int getLevel(WeaponType type) {
        return switch (type) {
            case LOURDE -> lourdeLevel;
            case MOYENNE -> moyenneLevel;
            case LEGERE -> legereLevel;
        };
    }

    /**
     * Définit le niveau de maîtrise pour un type d'arme donné.
     */
    public void setLevel(WeaponType type, int level) {
        switch (type) {
            case LOURDE -> lourdeLevel = level;
            case MOYENNE -> moyenneLevel = level;
            case LEGERE -> legereLevel = level;
        }
    }

    /**
     * Récupère l'expérience de maîtrise pour un type d'arme donné.
     */
    public double getXp(WeaponType type) {
        return switch (type) {
            case LOURDE -> lourdeXp;
            case MOYENNE -> moyenneXp;
            case LEGERE -> legereXp;
        };
    }

    /**
     * Définit l'expérience de maîtrise pour un type d'arme donné.
     */
    public void setXp(WeaponType type, double xp) {
        switch (type) {
            case LOURDE -> lourdeXp = xp;
            case MOYENNE -> moyenneXp = xp;
            case LEGERE -> legereXp = xp;
        }
    }

    /**
     * Ajoute de l'expérience de maîtrise pour un type d'arme donné.
     */
    public void addXp(WeaponType type, double amount) {
        setXp(type, getXp(type) + amount);
    }

    /**
     * Réinitialise le niveau et l'expérience de maîtrise pour un type d'arme donné.
     */
    public void reset(WeaponType type) {
        setLevel(type, 1);
        setXp(type, 0.0);
    }

    /**
     * Réinitialise le niveau et l'expérience de maîtrise pour tous les types d'armes.
     */
    public void resetAll() {
        for (WeaponType type : WeaponType.values()) {
            reset(type);
        }
    }
}
