package top.nipuru.tpademo.bukkit.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import top.nipuru.tpademo.commom.TpaResponseMessage;

/**
 * @author Nipuru
 * @since 2023/07/30 17:18
 */
public class TpaResponseBukkitProcessor extends AsyncUserProcessor<TpaResponseMessage> {

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, TpaResponseMessage request) {
        Player player = Bukkit.getPlayer(request.getReceiver());
        if (player == null) return;

        if (request.isAccept()) {
            player.sendMessage(request.getSender() + " 同意了你的传送请求");
        } else {
            player.sendMessage(request.getSender() + " 拒绝了你的传送请求");
        }
    }

    @Override
    public String interest() {
        return TpaResponseMessage.class.getName();
    }
}
