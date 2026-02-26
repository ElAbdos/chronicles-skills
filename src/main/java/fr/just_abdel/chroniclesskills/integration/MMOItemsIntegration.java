package fr.just_abdel.chroniclesskills.integration;

import fr.just_abdel.chroniclesskills.config.MasteryConfig;
import fr.just_abdel.chroniclesskills.model.WeaponType;
import lombok.Getter;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class MMOItemsIntegration {

    private final MasteryConfig config;
    @Getter
    private boolean enabled = false;

    public MMOItemsIntegration(MasteryConfig config) {
        this.config = config;
        checkEnabled();
    }

    /**
     * Vérifie si MMOItems est présent et active l'intégration en conséquence.
     */
    private void checkEnabled() {
        enabled = Bukkit.getPluginManager().getPlugin("MMOItems") != null;
    }

    /**
     * Récupère le type d'arme d'un item en utilisant les mappings définis dans la configuration.
     */
    public WeaponType getWeaponType(ItemStack item) {
        if (!enabled || item == null) {
            return null;
        }

        Type mmoItemType = MMOItems.getType(item);
        String mmoItemId = MMOItems.getID(item);

        if (mmoItemType == null || mmoItemId == null) {
            return null;
        }

        String typeId = mmoItemType.getId();

        Map<String, String> mappings = config.getMmoItemsMappings();

        String fullId = typeId + ":" + mmoItemId;
        String mappedType = mappings.get(fullId);
        if (mappedType != null) {
            return WeaponType.fromKey(mappedType);
        }

        mappedType = mappings.get(typeId);
        if (mappedType != null) {
            return WeaponType.fromKey(mappedType);
        }

        return null;
    }
}
