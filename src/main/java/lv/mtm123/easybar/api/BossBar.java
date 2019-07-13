package lv.mtm123.easybar.api;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

public interface BossBar {

    void setText(String text);

    void setProgress(float progress);

    void setColor(BarColor color);

    void setStyle(BarStyle style);

    void addPlayer(Player player);

    void removePlayer(Player player);

    void setVisible(boolean visible);

    enum BossBarType {
        LEGACY,
        MODERN
    }

}
