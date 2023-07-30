package top.nipuru.tpademo.broker.processor;

import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.SyncUserProcessor;
import net.afyer.afybroker.server.Broker;
import net.afyer.afybroker.server.proxy.BrokerClientProxy;
import net.afyer.afybroker.server.proxy.BrokerPlayer;
import top.nipuru.tpademo.broker.TpaDemo;
import top.nipuru.tpademo.commom.TpaRequestMessage;

/**
 * @author Nipuru
 * @since 2023/07/30 17:17
 */
public class TpaRequestBrokerProcessor extends SyncUserProcessor<TpaRequestMessage> {

    private final TpaDemo plugin;

    public TpaRequestBrokerProcessor(TpaDemo plugin) {
        this.plugin = plugin;
    }

    //可以返回任何可被序列化的对象
    @Override
    public Object handleRequest(BizContext bizCtx, TpaRequestMessage request) throws Exception {
        String sender = request.getSender();
        String receiver = request.getReceiver();

        BrokerPlayer senderPlayer = Broker.getPlayer(sender);
        BrokerPlayer receiverPlayer = Broker.getPlayer(receiver);
        if (senderPlayer == null || receiverPlayer == null) {
            return TpaRequestMessage.NOT_ONLINE;
        }

        String key = sender + ":" + receiver;
        if (plugin.tpMap.containsKey(key)) {
            return TpaRequestMessage.ALREADY_REQUEST;
        }

        request.setTime(System.currentTimeMillis());

        plugin.tpMap.put(key, request);
        BrokerClientProxy receiverBukkit = receiverPlayer.getBukkitClientProxy();
        if (receiverBukkit != null) {
            /**
             * @see top.nipuru.tpademo.bukkit.processor.TpaRequestBukkitProcessor
             */
            receiverBukkit.oneway(request); // 通知对方传送请求消息
        }

        return TpaRequestMessage.SUCCESS; // 请求成功
    }

    @Override
    public String interest() {
        return TpaRequestMessage.class.getName();
    }
}
