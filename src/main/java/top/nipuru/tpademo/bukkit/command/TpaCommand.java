package top.nipuru.tpademo.bukkit.command;

import com.alipay.remoting.exception.RemotingException;
import net.afyer.afybroker.client.Broker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.nipuru.tpademo.bukkit.TpaDemo;
import top.nipuru.tpademo.commom.TpaRequestMessage;

/**
 * @author Nipuru
 * @since 2023/07/30 16:37
 */
public class TpaCommand implements CommandExecutor {

    private final TpaDemo plugin;

    public TpaCommand(TpaDemo plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "只有玩家才能执行");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            sender.sendMessage("/tpa <player>");
            return true;
        }
        String receiver = args[0];

        TpaRequestMessage requestMessage = new TpaRequestMessage()
                .setSender(player.getName())
                .setReceiver(receiver)
                .setTpa(true);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                /**
                 * @see top.nipuru.tpademo.broker.processor.TpaRequestBrokerProcessor
                 */
                int result = Broker.invokeSync(requestMessage);
                if (result == TpaRequestMessage.SUCCESS) {
                    player.sendMessage("已发送传送请求");
                } else if (result == TpaRequestMessage.NOT_ONLINE){
                    player.sendMessage("玩家 " + receiver + "不存在");
                } else if (result == TpaRequestMessage.ALREADY_REQUEST) {
                    player.sendMessage("上一条发送给玩家 " + receiver + " 的传送请求还没有被处理");
                }
            } catch (RemotingException | InterruptedException e) {
                e.printStackTrace();
                sender.sendMessage("请求失败");
            }
        });
        return true;
    }
}
