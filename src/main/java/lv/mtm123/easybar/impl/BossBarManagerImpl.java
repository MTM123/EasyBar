package lv.mtm123.easybar.impl;

import lv.mtm123.easybar.EasyBar;
import lv.mtm123.easybar.api.BossBar;
import lv.mtm123.easybar.api.BossBarManager;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import us.myles.ViaVersion.api.ViaAPI;

public class BossBarManagerImpl implements BossBarManager {

    private final ViaAPI viaAPI;

    public BossBarManagerImpl(EasyBar plugin, ViaAPI viaAPI) {
        this.viaAPI = viaAPI;

        scheduleBossBarUpdates(plugin, 5L);
    }

    @Override
    public BossBar createBossBar(Player player) {
        BossBar bar = createBossBar(getBossBarType(player));
        bar.addPlayer(player);

        return bar;
    }

    @Override
    public BossBar createBossBar(BossBar.BossBarType type) {

        if (type == BossBar.BossBarType.LEGACY) {
            return new LegacyBossBar();
        }

        return new ModernBossBar();

    }

    @Override
    public BossBar createBossBar(Player player, String text, BarColor color, BarStyle style, float progress) {
        BossBar bar = createBossBar(getBossBarType(player), text, color, style, progress);
        bar.addPlayer(player);

        return bar;
    }

    @Override
    public BossBar createBossBar(BossBar.BossBarType type, String text, BarColor color, BarStyle style, float progress) {

        if (type == BossBar.BossBarType.LEGACY) {
            return new LegacyBossBar(text, progress);
        }

        return new ModernBossBar(text, color, style, progress);

    }

    private BossBar.BossBarType getBossBarType(Player player) {
        if (viaAPI.getPlayerVersion(player.getUniqueId()) > 47) {
            return BossBar.BossBarType.MODERN;
        } else {
            return BossBar.BossBarType.LEGACY;
        }
    }

    private void scheduleBossBarUpdates(EasyBar plugin, long interval) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () ->
                LegacyBossBar.getUpdatableBossBars().forEach(LegacyBossBar::updateAll), 0L, interval);
    }

}
