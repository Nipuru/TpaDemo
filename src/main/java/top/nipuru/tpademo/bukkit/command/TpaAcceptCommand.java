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
import top.nipuru.tpademo.commom.TpaResponseMessage;

/**
 * @author Nipuru
 * @since 2023/07/30 16:37
 */
public class TpaAcceptCommand implements CommandExecutor {

    private final TpaDemo plugin;

    public TpaAcceptCommand(TpaDemo plugin) {
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
            sender.sendMessage("/tpaaccept <player>");
            return true;
        }
        String receiver = args[0];

        TpaResponseMessage responseMessage = new TpaResponseMessage()
                .setSender(player.getName())
                .setReceiver(receiver)
                .setAccept(true);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                /**
                 * @see top.nipuru.tpademo.broker.processor.TpaResponseBrokerProcessor
                 */
                boolean result = Broker.invokeSync(responseMessage);
                if (result) {
                    player.sendMessage("已同意传送请求");
                } else {
                    player.sendMessage("没有来自玩家 " + receiver + "的传送请求");
                }
            } catch (RemotingException | InterruptedException e) {
                e.printStackTrace();
                sender.sendMessage("请求失败");
            }
        });
        return true;
    }
}
