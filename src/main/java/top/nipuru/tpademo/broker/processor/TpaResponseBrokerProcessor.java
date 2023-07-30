package top.nipuru.tpademo.broker.processor;

import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.SyncUserProcessor;
import net.afyer.afybroker.core.message.ConnectToServerMessage;
import net.afyer.afybroker.server.Broker;
import net.afyer.afybroker.server.BrokerServer;
import net.afyer.afybroker.server.proxy.BrokerClientProxy;
import net.afyer.afybroker.server.proxy.BrokerPlayer;
import top.nipuru.tpademo.broker.TpaDemo;
import top.nipuru.tpademo.commom.CacheOrTeleportMessage;
import top.nipuru.tpademo.commom.TpaRequestMessage;
import top.nipuru.tpademo.commom.TpaResponseMessage;

/**
 * @author Nipuru
 * @since 2023/07/30 17:17
 */
public class TpaResponseBrokerProcessor extends SyncUserProcessor<TpaResponseMessage> {

    private final TpaDemo plugin;

    public TpaResponseBrokerProcessor(TpaDemo plugin) {
        this.plugin = plugin;
    }

    //可以返回任何可被序列化的对象
    @Override
    public Object handleRequest(BizContext bizCtx, TpaResponseMessage tpaResponse) throws Exception {
        String sender = tpaResponse.getSender();
        String receiver = tpaResponse.getReceiver();

        String key = receiver + ":" + sender;
        TpaRequestMessage tpaRequest = plugin.tpMap.remove(key);
        if (tpaRequest == null) {
            return false; //请求不存在
        }

        BrokerPlayer senderPlayer = Broker.getPlayer(sender);
        BrokerPlayer receiverPlayer = Broker.getPlayer(receiver);
        if (senderPlayer == null || receiverPlayer == null) {
            return false; // 有一方下线了
        }

        BrokerClientProxy senderBukkit = senderPlayer.getBukkitClientProxy();
        BrokerClientProxy receiverBukkit = receiverPlayer.getBukkitClientProxy();
        if (senderBukkit == null || receiverBukkit == null) {
            return false; //出现了某种异常
        }

        /**
         * @see top.nipuru.tpademo.bukkit.processor.TpaResponseBukkitProcessor
         */
        receiverBukkit.oneway(tpaResponse); //通知对方回应结果

        if (!tpaResponse.isAccept()) return true;

        if (tpaRequest.isTpa()) {
            /**
             * @see top.nipuru.tpademo.bukkit.processor.CacheOrTeleportBukkitProcessor
             */
            senderBukkit.oneway(new CacheOrTeleportMessage()
                    .setFrom(tpaRequest.getSender())
                    .setTo(tpaRequest.getReceiver()));
            if (senderBukkit != receiverBukkit) {
                receiverPlayer.getBungeeClientProxy().oneway(new ConnectToServerMessage() //将玩家传送到对方所在服务器
                        .setPlayer(receiverPlayer.getUid())
                        .setServer(senderBukkit.getName()));
            }
        } else {
            receiverBukkit.oneway(new CacheOrTeleportMessage()
                    .setFrom(tpaRequest.getReceiver())
                    .setTo(tpaRequest.getSender()));
            if (senderBukkit != receiverBukkit) {
                senderPlayer.getBungeeClientProxy().oneway(new ConnectToServerMessage() //将玩家传送到对方所在服务器
                        .setPlayer(senderPlayer.getUid())
                        .setServer(receiverBukkit.getName()));
            }
        }
        return true;
    }

    @Override
    public String interest() {
        return TpaResponseMessage.class.getName();
    }
}
