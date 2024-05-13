package me.messdaniel.simplejumppads.listener;

import me.messdaniel.simplejumppads.SimpleJumpPads;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EntityBlockEvent implements Listener {


    @EventHandler
    public void onEntityChangeBlockEvent(EntityChangeBlockEvent event) {
        if (event != null && SimpleJumpPads.getInstance().getJumpPadManager().getJumpPadsFallingEntityUUIDS().contains(event.getEntity().getUniqueId())) {
            event.setCancelled(true);
        }
    }

}
