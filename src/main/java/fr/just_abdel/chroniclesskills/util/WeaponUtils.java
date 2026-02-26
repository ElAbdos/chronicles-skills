package fr.just_abdel.chroniclesskills.util;

import fr.just_abdel.chroniclesskills.integration.MMOItemsIntegration;
import fr.just_abdel.chroniclesskills.model.WeaponType;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Setter
public class WeaponUtils {


    private MMOItemsIntegration mmoItemsIntegration;

    public WeaponUtils() {}

    /**
     * Detecte le type d'arme à partir d'un ItemStack
     */
    public WeaponType detectWeaponType(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return null;
        }

        if (mmoItemsIntegration != null && mmoItemsIntegration.isEnabled()) {
            return mmoItemsIntegration.getWeaponType(item);
        }

        return null;
    }

    /**
     * Récupère le type d'arme que le joueur tient en main
     */
    public WeaponType getHeldWeaponType(Player player) {
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        return detectWeaponType(mainHand);
    }
}
