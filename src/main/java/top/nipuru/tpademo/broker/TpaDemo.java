package top.nipuru.tpademo.broker;

import com.google.common.collect.Maps;
import net.afyer.afybroker.server.BrokerServer;
import net.afyer.afybroker.server.plugin.Plugin;
import top.nipuru.tpademo.broker.processor.TpaRequestBrokerProcessor;
import top.nipuru.tpademo.broker.processor.TpaResponseBrokerProcessor;
import top.nipuru.tpademo.commom.TpaRequestMessage;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Nipuru
 * @since 2023/07/30 15:48
 */
public class TpaDemo extends Plugin {

    public final static long EXPIRE_MILLIS = TimeUnit.MINUTES.toMillis(2L); //传送过期时间

    /** 传送消息缓存 key=sender:receiver */
    public final Map<String, TpaRequestMessage> tpMap = Maps.newConcurrentMap();

    @Override
    public void onEnable() {

        getServer().registerUserProcessor(new TpaRequestBrokerProcessor(this));
        getServer().registerUserProcessor(new TpaResponseBrokerProcessor(this));

        getServer().getScheduler().schedule(this, ()-> { //清理过期请求
            long current = System.currentTimeMillis();
            for (Map.Entry<String, TpaRequestMessage> entry : tpMap.entrySet()) {
                if (entry.getValue().getTime() + EXPIRE_MILLIS < current) {
                    tpMap.remove(entry.getKey());
                }
            }
        }, 1L, 1L, TimeUnit.SECONDS);
    }
}
