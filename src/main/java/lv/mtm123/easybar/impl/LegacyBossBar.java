package lv.mtm123.easybar.impl;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLib;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import lv.mtm123.easybar.api.BossBar;
import lv.mtm123.easybar.packetwrapper.WrapperPlayServerEntityMetadata;
import lv.mtm123.easybar.packetwrapper.WrapperPlayServerSpawnEntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class LegacyBossBar implements BossBar {

    private static int ENTITY_ID;
    private static final UUID ENTITY_UUID;
    private static WrappedDataWatcher DATA_WATCHER;

    private static final Set<LegacyBossBar> BOSS_BARS;

    static {
/*        int id;
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            Field field = Class.forName("net.minecraft.server." + version + ".Entity").getDeclaredField("entityCount");
            field.setAccessible(true);
            int count = (int) field.get(null) + 1;
            field.set(null, count);
            id = count;
        } catch (Exception e) {
            id = -1;
            e.printStackTrace();
        }

        ENTITY_ID = id;*/
        ENTITY_UUID = UUID.randomUUID();
        BOSS_BARS = new HashSet<>();

    }

    private final WrapperPlayServerSpawnEntityLiving wrapper;
    private final WrapperPlayServerEntityMetadata metadata;
    private final WrappedDataWatcher datawatcher;

    private final Set<Player> players;

    private String text;
    private float progress;

    private boolean visible;

    public LegacyBossBar() {
        this("", 1f);
    }

    public LegacyBossBar(String text, float progress) {
        this.text = ChatColor.translateAlternateColorCodes('&', text);

        this.wrapper = new WrapperPlayServerSpawnEntityLiving();
        wrapper.setX(0);
        wrapper.setY(0);
        wrapper.setZ(0);

        wrapper.setType(EntityType.ENDER_DRAGON);

        World w = Bukkit.getWorlds().get(0);
        EnderDragon entity = (EnderDragon) w.spawnEntity(new Location(w, 0, 1000, 0), EntityType.ENDER_DRAGON);
        entity.setInvulnerable(true);
        entity.setSilent(true);
        entity.setGravity(false);
        entity.setAI(false);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 10, false, false));
        entity.setCustomName(ChatColor.translateAlternateColorCodes('&', "&4Testing&5blyat"));
        entity.remove();

        ENTITY_ID = entity.getEntityId();
        wrapper.setEntityID(entity.getEntityId());

        this.datawatcher = WrappedDataWatcher.getEntityWatcher(entity).deepClone();

        //this.datawatcher.setObject(0, (byte) 0x20);
        this.datawatcher.setObject(7, 0.5f * 300);
        this.datawatcher.setObject(8, 0);
        this.datawatcher.setObject(9, false);
        //this.datawatcher.setObject(15, 1000);
        this.datawatcher.setObject(2, this.text);
        this.datawatcher.setObject(7, progress * 300);


        this.metadata = new WrapperPlayServerEntityMetadata();
        this.metadata.setEntityID(entity.getEntityId());

        metadata.setMetadata(datawatcher.getWatchableObjects());

        players = new HashSet<>();
    }


    @Override
    public void setText(String text) {
        this.text = ChatColor.translateAlternateColorCodes('&', text);
    }

    @Override
    public void setProgress(float progress) {
        //Value clamp
        this.progress = Math.max(0, Math.min(1, Math.abs(progress)));
    }

    @Override
    public void setColor(BarColor color) {
    }

    @Override
    public void setStyle(BarStyle style) {
    }

    @Override
    public void addPlayer(Player player) {
        players.add(player);

        if (visible) {
            spawn(player);
        }

        if (players.size() == 1) {
            BOSS_BARS.add(this);
        }
    }

    @Override
    public void removePlayer(Player player) {
        players.remove(player);
        despawn(player);

        if (players.size() == 0) {
            BOSS_BARS.remove(this);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;

        if (visible) {
            players.forEach(this::spawn);
        } else {
            players.forEach(this::despawn);
        }
    }

    public void update(Player player) {
//        despawn(player);
        teleport(player);
        spawn(player);
    }

    public void updateAll() {

        if (!visible) {
            return;
        }

        players.forEach(this::update);

    }

    private void spawn(Player player) {

        Location loc = player.getLocation();
        //loc = loc.add(loc.getDirection().multiply(20));

        wrapper.setX(loc.getX());
        wrapper.setY(0);
        wrapper.setZ(loc.getZ());

        datawatcher.setObject(2, text);
        datawatcher.setObject(7, progress * 300);

        wrapper.sendPacket(player);
        metadata.sendPacket(player);

    }

    private void teleport(Player player) {

        Location loc = player.getLocation();
        //loc = loc.add(loc.getDirection().multiply(20));

        PacketContainer teleport = new PacketContainer(PacketType.Play.Server.ENTITY_TELEPORT);
        teleport.getIntegers().write(0, ENTITY_ID);
        teleport.getDoubles().write(0, loc.getX());
        teleport.getDoubles().write(1, 0d);
        teleport.getDoubles().write(2, loc.getZ());
        teleport.getBytes().write(0, (byte) 0);
        teleport.getBytes().write(1, (byte) 1);
        teleport.getBooleans().write(0, false);

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, teleport, false);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private void despawn(Player player) {

        PacketContainer despawnMob = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        despawnMob.getIntegerArrays().write(0, new int[]{ENTITY_ID});

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, despawnMob, false);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private WrappedDataWatcher getDataWatcher() {
        datawatcher.setObject(2, text);
        datawatcher.setObject(6, Math.round(300 * progress));
        datawatcher.setObject(10, text);
        datawatcher.setObject(20, 881);
        return datawatcher;
    }

    private static void initialize() {

    }

    static Set<LegacyBossBar> getUpdatableBossBars() {
        return BOSS_BARS;
    }

}
