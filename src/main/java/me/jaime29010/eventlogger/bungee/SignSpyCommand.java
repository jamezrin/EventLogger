package me.jaime29010.eventlogger.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.apache.commons.lang3.StringUtils;

public class SignSpyCommand extends Command {
    private final Main main;
    public SignSpyCommand(Main main) {
        super("signspy", "", "ss");
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (args.length >= 1) {
                switch (args[0].toLowerCase()) {
                    case "reload": {
                        if (player.hasPermission("signspy.reload")) {
                            main.reloadConfig();
                            player.sendMessage(main.getConfigMessage("ReloadMessage"));
                        } else {
                            player.sendMessage(new ComponentBuilder("You don't have enough permissions to execute this command").color(ChatColor.RED).create());
                        }
                        break;
                    }
                    case "help": {
                        player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&',
                                "&b/signspy, /ss &7-&r Toggle SignSpy notifications on or off\n" +
                                "&b/signspy <message>, /ss <message> &7-&r Send a SignSpy notification to everyone with SignSpy notification permission\n" +
                                "&b/sb <message> &7-&r Reload the SignSpy configuration (&conly works from console&r)\n" +
                                "&b/signspy <reload>, /ss <reload> &7-&r Reload the SignSpy configuration")));
                        break;
                    }
                    default: {
                        if (player.hasPermission("signspy.message")) {
                            String message = StringUtils.join(args, ' ');
                            main.broadcast(new TextComponent(message));
                            player.sendMessage(main.getConfigMessage("MessageSentConfirmation"));
                        } else {
                            player.sendMessage(new ComponentBuilder("You don't have enough permissions to execute this command").color(ChatColor.RED).create());
                        }
                    }
                }
            } else {
                if (player.hasPermission("signspy.notify")) {
                    if (main.getStorage().containsKey(player.getUniqueId())) {
                        if (main.getStorage().get(player.getUniqueId())) {
                            main.getStorage().put(player.getUniqueId(), false);
                            player.sendMessage(main.getConfigMessage("SignSpyOffMessage"));
                        } else {
                            main.getStorage().put(player.getUniqueId(), true);
                            player.sendMessage(main.getConfigMessage("SignSpyOnMessage"));
                        }
                    } else {
                        main.getStorage().put(player.getUniqueId(), true);
                        player.sendMessage(main.getConfigMessage("SignSpyOnMessage"));
                    }
                } else {
                    player.sendMessage(new ComponentBuilder("You don't have enough permissions to execute this command").color(ChatColor.RED).create());
                }
            }
        } else {
            sender.sendMessage(new TextComponent("This command can only be executed by a player"));
        }
    }
}
