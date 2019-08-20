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

    private final Set<UUID> players;
    private final Map<UUID, BossBarStorage> viaBossBars;

    private String text;
    private float progress;

    private boolean visible;

    public LegacyBossBar() {
        this("", 1f);
    }

    public LegacyBossBar(String text, float progress) {
        this.progress = progress;
        this.bossBarId = UUID.randomUUID();
        this.text = ChatColor.translateAlternateColorCodes('&', text);
        players = new HashSet<>();
        viaBossBars = new HashMap<>();
    }


    @Override
    public void setText(String text) {
        this.text = ChatColor.translateAlternateColorCodes('&', text);
        viaBossBars.forEach((k, v) -> v.updateTitle(bossBarId, this.text));
    }

    @Override
    public void setProgress(float progress) {
        //Value clamp
        this.progress = Math.max(0, Math.min(1, Math.abs(progress)));
        viaBossBars.forEach((k, v) -> v.updateHealth(bossBarId, this.progress * 300));
    }

    @Override
    public void setColor(BarColor color) {
    }

    @Override
    public void setStyle(BarStyle style) {
    }

    @Override
    public void addPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        players.add(uuid);

        BossBarStorage storage = Via.getManager().getConnection(uuid).get(BossBarStorage.class);
        if (storage == null) {
            throw new IllegalArgumentException("Incorrect player version!");
        }

        viaBossBars.put(uuid, storage);

        if (visible) {
            showBar(uuid);
        }
    }

    @Override
    public void removePlayer(Player player) {
        UUID uuid = player.getUniqueId();
        hideBar(uuid);
        viaBossBars.remove(uuid);
        players.remove(uuid);
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;

        if (visible) {
            players.forEach(this::showBar);
        } else {
            players.forEach(this::hideBar);
        }
    }

    private void showBar(UUID playerUuid) {
        viaBossBars.get(playerUuid).add(bossBarId, text, progress);
    }

    private void hideBar(UUID playerUuid) {
        viaBossBars.get(playerUuid).remove(bossBarId);
    }

}
