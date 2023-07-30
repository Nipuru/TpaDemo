package top.nipuru.tpademo.bukkit.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import top.nipuru.tpademo.commom.TpaRequestMessage;

/**
 * @author Nipuru
 * @since 2023/07/30 17:19
 */
public class TpaRequestBukkitProcessor extends AsyncUserProcessor<TpaRequestMessage> {

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, TpaRequestMessage request) {
        Player receiver = Bukkit.getPlayer(request.getReceiver());
        if (receiver == null) return;

        BaseComponent acceptBtn = new TextComponent("[同意]"); //同意按钮
        acceptBtn.setColor(ChatColor.GREEN);
        acceptBtn.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept " + request.getSender()));
        BaseComponent denyBtn = new TextComponent("[拒绝]"); //拒绝按钮
        denyBtn.setColor(ChatColor.RED);
        denyBtn.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpadeny " + request.getSender()));

        if (request.isTpa()) {
            receiver.sendMessage(request.getSender() + " 请求传送到你所在的位置");
            receiver.spigot().sendMessage(acceptBtn, denyBtn);
        } else {
            receiver.sendMessage(request.getSender() + " 请求你传送到对方所在的位置");
            receiver.spigot().sendMessage(acceptBtn, denyBtn);
        }
    }

    @Override
    public String interest() {
        return TpaRequestMessage.class.getName();
    }
}
