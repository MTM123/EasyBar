package lv.mtm123.easybar;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.lang.reflect.InvocationTargetException;

public class PlayerListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
    }


    private void sendPacket(Player player, PacketContainer packet) {
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet, false);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
