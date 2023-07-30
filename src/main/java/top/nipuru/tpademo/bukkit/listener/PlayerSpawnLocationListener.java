package top.nipuru.tpademo.bukkit.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;
import top.nipuru.tpademo.bukkit.TpaDemo;

/**
 * 用于在玩家进入服务器时设置其坐标
 *
 * @author Nipuru
 * @since 2023/07/30 16:58
 */
public class PlayerSpawnLocationListener implements Listener {

    private final TpaDemo plugin;

    public PlayerSpawnLocationListener(TpaDemo plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSpawn(PlayerSpawnLocationEvent event) {
        Player player = event.getPlayer();
        Location location = plugin.tpCache.getIfPresent(player.getName());
        if (location == null) return;

        event.setSpawnLocation(location);
        plugin.tpCache.invalidate(player.getName()); //使缓存过期
    }
}
