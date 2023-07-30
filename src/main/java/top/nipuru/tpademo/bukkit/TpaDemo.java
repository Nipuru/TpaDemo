package top.nipuru.tpademo.bukkit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.afyer.afybroker.client.Broker;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import top.nipuru.tpademo.bukkit.command.TpaAcceptCommand;
import top.nipuru.tpademo.bukkit.command.TpaCommand;
import top.nipuru.tpademo.bukkit.command.TpaDenyCommand;
import top.nipuru.tpademo.bukkit.command.TpaHereCommand;
import top.nipuru.tpademo.bukkit.listener.PlayerSpawnLocationListener;
import top.nipuru.tpademo.bukkit.processor.CacheOrTeleportBukkitProcessor;
import top.nipuru.tpademo.bukkit.processor.TpaRequestBukkitProcessor;
import top.nipuru.tpademo.bukkit.processor.TpaResponseBukkitProcessor;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class TpaDemo extends JavaPlugin {

    // 存放传送信息的缓存，key 为要进行传送的玩家，value 传送坐标
    public final Cache<String, Location> tpCache = CacheBuilder.newBuilder()
            .expireAfterWrite(5L, TimeUnit.MINUTES)     //若五分钟内没被处理则过期
            .build();
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerSpawnLocationListener(this), this);
        Broker.registerUserProcessor(new CacheOrTeleportBukkitProcessor(this));
        Broker.registerUserProcessor(new TpaRequestBukkitProcessor());
        Broker.registerUserProcessor(new TpaResponseBukkitProcessor());

        getCommand("tpa").setExecutor(new TpaCommand(this));
        getCommand("tpahere").setExecutor(new TpaHereCommand(this));
        getCommand("tpaccept").setExecutor(new TpaAcceptCommand(this));
        getCommand("tpadeny").setExecutor(new TpaDenyCommand(this));
    }
}
