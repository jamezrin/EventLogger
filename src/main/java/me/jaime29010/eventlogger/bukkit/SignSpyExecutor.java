package me.jaime29010.eventlogger.bukkit;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SignSpyExecutor implements CommandExecutor {
    private final Main main;
    public SignSpyExecutor(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.RED + "This command cannot be executed by players");
        } else {
            if (args.length != 0) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("BroadcastMessage");
                String message = StringUtils.join(args, ' ');
                out.writeUTF(message);
                Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
                if (player == null) {
                    sender.sendMessage("This command requires a player to be online in this server");
                    return true;
                }
                player.sendPluginMessage(main, "EventLogger", out.toByteArray());
            } else {
                sender.sendMessage("Usage for this command: /signspy <message>");
            }
        }
        return true;
    }
}
