package lv.mtm123.easybar.impl;

import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.BossBarStorage;
import lv.mtm123.easybar.api.BossBar;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import us.myles.ViaVersion.api.Via;

import java.util.*;

public class LegacyBossBar implements BossBar {

    private final UUID bossBarId;

    private final Set<Player> players;

    private final Map<UUID, BossBarStorage> viaBossBars;

    private String text;
    private float progress;

    private boolean visible;

    public LegacyBossBar() {
        this("", 1f);
    }

    public LegacyBossBar(String text, float progress) {
        this.bossBarId = UUID.randomUUID();
        this.text = ChatColor.translateAlternateColorCodes('&', text);
        players = new HashSet<>();
        viaBossBars = new HashMap<>();
    }


    @Override
    public void setText(String text) {
        this.text = ChatColor.translateAlternateColorCodes('&', text);
        viaBossBars.forEach((k,v) -> v.updateTitle(bossBarId, this.text));
    }

    @Override
    public void setProgress(float progress) {
        //Value clamp
        this.progress = Math.max(0, Math.min(1, Math.abs(progress)));
        viaBossBars.forEach((k,v) -> v.updateHealth(bossBarId, this.progress * 300));
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

        BossBarStorage storage = Via.getManager().getConnection(player.getUniqueId()).get(BossBarStorage.class);
        if (storage == null) {
            throw new IllegalArgumentException("Incorrect player version!");
        }

        viaBossBars.put(player.getUniqueId(), storage);

        if (visible) {
            spawn(player);
        }

//        if (players.size() == 1) {
//            BOSS_BARS.add(this);
//        }
    }

    @Override
    public void removePlayer(Player player) {
        players.remove(player);
        viaBossBars.remove(player.getUniqueId());
        despawn(player);

//        if (players.size() == 0) {
//            BOSS_BARS.remove(this);
//        }
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
//        teleport(player);
//        spawn(player);
    }

    public void updateAll() {

        if (!visible) {
            return;
        }

        players.forEach(this::update);

    }

    private void spawn(Player player) {
        viaBossBars.get(player.getUniqueId()).add(bossBarId, text, progress);
    }

    private void teleport(Player player) {

/*        Location loc = player.getLocation();
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
        }*/

    }

    private void despawn(Player player) {
        viaBossBars.get(player.getUniqueId()).remove(bossBarId);

/*        PacketContainer despawnMob = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        despawnMob.getIntegerArrays().write(0, new int[]{ENTITY_ID});

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, despawnMob, false);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }*/

    }

    static Set<LegacyBossBar> getUpdatableBossBars() {
        return Collections.emptySet();
    }

}
