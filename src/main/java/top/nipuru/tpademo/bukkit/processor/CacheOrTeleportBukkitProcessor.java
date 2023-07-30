package top.nipuru.tpademo.bukkit.processor;

import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.SyncUserProcessor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import top.nipuru.tpademo.bukkit.TpaDemo;
import top.nipuru.tpademo.commom.CacheOrTeleportMessage;

/**
 * @author Nipuru
 * @since 2023/07/30 17:00
 */
public class CacheOrTeleportBukkitProcessor extends SyncUserProcessor<CacheOrTeleportMessage> {

    private final TpaDemo plugin;

    public CacheOrTeleportBukkitProcessor(TpaDemo plugin) {
        this.plugin = plugin;
    }

    //可以返回任何可被序列化的对象
    @Override
    public Object handleRequest(BizContext bizCtx, CacheOrTeleportMessage request) {
        System.out.println(request);
        Player to = Bukkit.getPlayer(request.getTo());

        //表示玩家不在线
        if (to == null) return false;

        Player from = Bukkit.getPlayer(request.getFrom());
        if (from != null) { //如果两个玩家在同一个服务器则直接传送
            Bukkit.getScheduler().runTask(plugin, () -> {
                from.teleport(to);
            });
        } else { //对玩家坐标进行缓存
            plugin.tpCache.put(request.getFrom(), to.getLocation());
        }
        return true;
    }

    @Override
    public String interest() {
        return CacheOrTeleportMessage.class.getName();
    }
}
