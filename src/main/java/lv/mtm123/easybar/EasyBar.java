package lv.mtm123.easybar;

import lv.mtm123.easybar.api.BossBarManager;
import lv.mtm123.easybar.impl.BossBarManagerImpl;
import org.bukkit.plugin.java.JavaPlugin;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.ViaAPI;

import java.util.logging.Level;

public final class EasyBar extends JavaPlugin {

    private static EasyBar plugin;

    private BossBarManager bossBarManager;

    @Override
    public void onEnable() {
        plugin = this;

        if (!getServer().getPluginManager().isPluginEnabled("ViaVersion")) {
            getLogger().log(Level.SEVERE, "ViaVersion not found! Plugin will not work!");
            return;
        }

        if (!getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
            getLogger().log(Level.SEVERE, "ProtocolLib not found! Plugin will not work!");
            return;
        }

        bossBarManager = new BossBarManagerImpl(plugin, Via.getAPI());

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

    }

    @Override
    public void onDisable() {
        plugin = null;
    }

    public BossBarManager getBossBarManager() {
        return bossBarManager;
    }

    public static EasyBar getInstance() {
        return plugin;
    }

}
