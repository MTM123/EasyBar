package lv.mtm123.easybar.impl;

import lv.mtm123.easybar.EasyBar;
import lv.mtm123.easybar.api.BossBar;
import lv.mtm123.easybar.api.BossBarManager;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import us.myles.ViaVersion.api.ViaAPI;

public class BossBarManagerImpl implements BossBarManager {

    private final ViaAPI viaAPI;

    public BossBarManagerImpl(EasyBar plugin, ViaAPI viaAPI) {
        this.viaAPI = viaAPI;
    }

    @Override
    public BossBar createBossBar(Player player) {
        BossBar bar = createBossBar(getBossBarType(player));
        bar.addPlayer(player);

        return bar;
    }

    @Override
    public BossBar createBossBar(BossBar.BossBarType type) {
        return type == BossBar.BossBarType.LEGACY ? new LegacyBossBar() : new ModernBossBar();
    }

    @Override
    public BossBar createBossBar(Player player, String text, BarColor color, BarStyle style, float progress) {
        BossBar bar = createBossBar(getBossBarType(player), text, color, style, progress);
        bar.addPlayer(player);

        return bar;
    }

    @Override
    public BossBar createBossBar(BossBar.BossBarType type, String text, BarColor color, BarStyle style, float progress) {
        return type == BossBar.BossBarType.LEGACY ? new LegacyBossBar(text, progress) : new ModernBossBar(text, color, style, progress);
    }

    private BossBar.BossBarType getBossBarType(Player player) {
        return viaAPI.getPlayerVersion(player.getUniqueId()) > 47 ? BossBar.BossBarType.MODERN : BossBar.BossBarType.LEGACY;
    }

}
