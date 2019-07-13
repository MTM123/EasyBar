package lv.mtm123.easybar.api;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

public interface BossBarManager {

    BossBar createBossBar(Player player);

    BossBar createBossBar(BossBar.BossBarType type);

    BossBar createBossBar(Player player, String text, BarColor color, BarStyle style, float progress);

    BossBar createBossBar(BossBar.BossBarType type, String text, BarColor color, BarStyle style, float progress);

}
