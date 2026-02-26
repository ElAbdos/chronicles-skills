package fr.just_abdel.chroniclesskills.gui.holder;

import fr.just_abdel.chroniclescore.api.gui.CoreHolder;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MasteryHolder extends CoreHolder {

    public MasteryHolder(UUID viewerId) {
        super("mastery_menu", viewerId);
    }

    @Override
    public void refresh(Player player) {}
}
