package lv.mtm123.easybar.impl;

import lv.mtm123.easybar.api.BossBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

public class ModernBossBar implements BossBar {

    private final org.bukkit.boss.BossBar bar;

    public ModernBossBar() {
        this("", BarColor.PURPLE, BarStyle.SOLID, 1f);
    }

    public ModernBossBar(String text, BarColor color, BarStyle style, float progress) {
        this.bar = Bukkit.createBossBar(text, color, style);
        this.bar.setProgress(progress);
    }

    @Override
    public void setText(String text) {
        bar.setTitle(ChatColor.translateAlternateColorCodes('&', text));
    }

    @Override
    public void setProgress(float progress) {
        bar.setProgress(progress);
    }

    @Override
    public void setColor(BarColor color) {
        bar.setColor(color);
    }

    @Override
    public void setStyle(BarStyle style) {
        bar.setStyle(style);
    }

    @Override
    public void addPlayer(Player player) {
        bar.addPlayer(player);
    }

    @Override
    public void removePlayer(Player player) {
        bar.removePlayer(player);
    }

    @Override
    public void setVisible(boolean visible) {
        bar.setVisible(visible);
    }

}
