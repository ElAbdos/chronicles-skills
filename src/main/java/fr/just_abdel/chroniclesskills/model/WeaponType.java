package fr.just_abdel.chroniclesskills.model;


import lombok.Getter;

@Getter
public enum WeaponType {
    LOURDE("lourde", "Arme Lourde"),
    MOYENNE("moyenne", "Arme Moyenne"),
    LEGERE("legere", "Arme Legere");

    private final String key;
    private final String displayName;

    WeaponType(String key, String displayName) {
        this.key = key;
        this.displayName = displayName;
    }

    public static WeaponType fromKey(String key) {
        for (WeaponType type : values()) {
            if (type.key.equalsIgnoreCase(key)) {
                return type;
            }
        }
        return null;
    }
}
