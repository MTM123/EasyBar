package lv.mtm123.easybar;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedAttribute;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

/*        System.out.println("test");


        WrapperPlayServerSpawnEntityLiving packet = new WrapperPlayServerSpawnEntityLiving();
        Location loc = event.getPlayer().getLocation();

        packet.setX(loc.getX());
        packet.setY(-10);
        packet.setZ(loc.getZ());

        packet.setType(EntityType.WITHER);

        Wither entity = (Wither) loc.getWorld().spawnEntity(new Location(loc.getWorld(), loc.getX(), -10, loc.getZ()), EntityType.WITHER);
        //entity.setInvulnerable(true);
        entity.setSilent(true);
        entity.setGravity(false);
        entity.setAI(false);
        //entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 10, false, false));
        entity.setCustomNameVisible(true);
        entity.setCustomName(ChatColor.translateAlternateColorCodes('&', "&4Testing&5blyat"));
        entity.remove();

        packet.setUniqueId(entity.getUniqueId());

        packet.setEntityID(entity.getEntityId());




        WrappedDataWatcher dw = WrappedDataWatcher.getEntityWatcher(entity).deepClone();
        //dw.setObject(0, (byte) 0x20);
        dw.setObject(1, 0);
        dw.setObject(3, true);

        dw.setObject(7, 0.5f * 300);
        dw.setObject(8, 1);
        //dw.setObject(8, 0);
        //dw.setObject(9, false);
        //dw.setObject(12, 0);
        //dw.setObject(15, 1000);

        dw.forEach(wo -> System.out.println(String.format("%d: %s", wo.getIndex(), wo.getValue())));

        dw.setObject(2, ChatColor.translateAlternateColorCodes('&', "&6" + UUID.randomUUID().toString()));
        //dw.setObject(7, ThreadLocalRandom.current().nextFloat() * 300);
        dw.setObject(11, (byte) 1);

        packet.setMetadata(dw);
        packet.sendPacket(event.getPlayer());



        WrapperPlayServerEntityMetadata metadata = new WrapperPlayServerEntityMetadata();
        metadata.setEntityID(entity.getEntityId());
        metadata.setMetadata(dw.getWatchableObjects());

        metadata.sendPacket(event.getPlayer());

        PacketContainer teleport = new PacketContainer(PacketType.Play.Server.ENTITY_TELEPORT);
        teleport.getIntegers().write(0, entity.getEntityId());
        teleport.getDoubles().write(0, loc.getX());
        teleport.getDoubles().write(1, 0d);
        teleport.getDoubles().write(2, loc.getZ());
        teleport.getBytes().write(0, (byte) 0);
        teleport.getBytes().write(1, (byte) 1);
        teleport.getBooleans().write(0, false);

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(event.getPlayer(), teleport, false);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }*/

        Player player = event.getPlayer();

        Location loc = player.getLocation();

        PacketContainer fakeEntity = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
        fakeEntity.getIntegers().write(0, 15000);
        fakeEntity.getUUIDs().write(0, UUID.randomUUID());
        fakeEntity.getIntegers().write(1, 64);
        fakeEntity.getDoubles().write(0, loc.getX());
        fakeEntity.getDoubles().write(1, -1d);
        fakeEntity.getDoubles().write(2, loc.getZ());
        fakeEntity.getBytes().write(0, (byte) 0);
        fakeEntity.getBytes().write(1, (byte) 0);

        List<WrappedWatchableObject> dwl = new ArrayList<>(Arrays.asList(
                new WrappedWatchableObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x20),
                new WrappedWatchableObject(new WrappedDataWatcher.WrappedDataWatcherObject(1, WrappedDataWatcher.Registry.get(Integer.class)), 0),
                new WrappedWatchableObject(
                        new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.get(String.class)),
                        "Test"),
                new WrappedWatchableObject(new WrappedDataWatcher.WrappedDataWatcherObject(8, WrappedDataWatcher.Registry.get(Integer.class)), 0),
                new WrappedWatchableObject(new WrappedDataWatcher.WrappedDataWatcherObject(9, WrappedDataWatcher.Registry.get(Boolean.class)), true),
                new WrappedWatchableObject(new WrappedDataWatcher.WrappedDataWatcherObject(10, WrappedDataWatcher.Registry.get(Integer.class)), 0),
                new WrappedWatchableObject(new WrappedDataWatcher.WrappedDataWatcherObject(15, WrappedDataWatcher.Registry.get(Integer.class)), 0),
                new WrappedWatchableObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)), true),
                new WrappedWatchableObject(new WrappedDataWatcher.WrappedDataWatcherObject(7, WrappedDataWatcher.Registry.get(Float.class)), 50f)
        ));

        WrappedDataWatcher dt = new WrappedDataWatcher(dwl);

        fakeEntity.getDataWatcherModifier().write(0, dt);

        sendPacket(player, fakeEntity);


        PacketContainer meta = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        meta.getIntegers().write(0, 15000);
        meta.getWatchableCollectionModifier().write(0, dwl);

        sendPacket(player, meta);


        PacketContainer meta2 = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        meta2.getIntegers().write(0, 15000);

        List<WrappedWatchableObject> nl = dwl.stream().filter(w -> w.getIndex() != 1).collect(Collectors.toList());
        nl.get(0).setValue((byte) 300);
        meta2.getWatchableCollectionModifier().write(0, nl);

        sendPacket(player, meta2);


        PacketContainer prop = new PacketContainer(PacketType.Play.Server.UPDATE_ATTRIBUTES);

        List<WrappedAttribute> attrs = new ArrayList<>();
        attrs.add(WrappedAttribute.newBuilder().attributeKey("generic.movementSpeed").baseValue(0.6000000238418579).packet(prop).build());
        attrs.add(WrappedAttribute.newBuilder().attributeKey("generic.maxHealth").baseValue(300.0).packet(prop).build());

        prop.getAttributeCollectionModifier().write(0, attrs);
        prop.getIntegers().write(0, 15000);

        sendPacket(player, prop);


        PacketContainer entitylook = new PacketContainer(PacketType.Play.Server.ENTITY_LOOK);
        entitylook.getIntegers().write(0, 15000);
        entitylook.getBytes().write(0, (byte) 0);

        sendPacket(player, entitylook);


        PacketContainer veloc = new PacketContainer(PacketType.Play.Server.ENTITY_VELOCITY);
        veloc.getIntegers().write(0, 15000);
        veloc.getIntegers().write(1, 0);
        veloc.getIntegers().write(2, 1);
        veloc.getIntegers().write(3, 2);

        sendPacket(player, veloc);


        sendPacket(player, meta2);

        PacketContainer teleport = new PacketContainer(PacketType.Play.Server.ENTITY_TELEPORT);
        teleport.getIntegers().write(0, 15000);
        teleport.getDoubles().write(0, loc.getX());
        teleport.getDoubles().write(1, 0d);
        teleport.getDoubles().write(2, loc.getZ());
        teleport.getBytes().write(0, (byte) 0);
        teleport.getBytes().write(1, (byte) 1);
        teleport.getBooleans().write(0, false);

        sendPacket(player, teleport);


/*        Bukkit.getScheduler().scheduleSyncRepeatingTask(EasyBar.getInstance(), () -> {
            PacketContainer teleport = new PacketContainer(PacketType.Play.Server.ENTITY_TELEPORT);
            teleport.getIntegers().write(0, 15000);
            teleport.getDoubles().write(0, loc.getX());
            teleport.getDoubles().write(1, 0d);
            teleport.getDoubles().write(2, loc.getZ());
            teleport.getBytes().write(0, (byte) 0);
            teleport.getBytes().write(1, (byte) 1);
            teleport.getBooleans().write(0, false);

            sendPacket(player, teleport);
        }, 0L, 20L);*/
    }


    private void sendPacket(Player player, PacketContainer packet) {
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet, false);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
