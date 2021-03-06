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
        super("eventlogger", "", "el");
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (args.length >= 1) {
                switch (args[0].toLowerCase()) {
                    case "reload": {
                        if (player.hasPermission("eventlogger.reload")) {
                            main.reloadConfig();
                            player.sendMessage(main.getComponentMessage("ReloadMessage"));
                        } else {
                            player.sendMessage(new ComponentBuilder("You don't have enough permissions to execute this command").color(ChatColor.RED).create());
                        }
                        break;
                    }
                    case "help": {
                        player.sendMessage(new TextComponent(main.color(
                                "&b/signspy, /ss &7-&r Toggle SignSpy notifications on or off\n" +
                                "&b/signspy <message>, /ss <message> &7-&r Send a SignSpy notification to everyone with SignSpy notifications enabled\n" +
                                "&b/signspy <reload>, /ss <reload> &7-&r Reload the SignSpy configuration")));
                        break;
                    }
                    default: {
                        if (player.hasPermission("eventlogger.broadcast")) {
                            String message = StringUtils.join(args, ' ');
                            main.broadcast(main.color(message));
                            player.sendMessage(main.getComponentMessage("MessageSentConfirmation"));
                        } else {
                            player.sendMessage(new ComponentBuilder("You don't have enough permissions to execute this command").color(ChatColor.RED).create());
                        }
                    }
                }
            } else {
                if (player.hasPermission("eventlogger.toggle")) {
                    if (main.getStorage().containsKey(player.getUniqueId())) {
                        if (main.getStorage().get(player.getUniqueId())) {
                            main.getStorage().put(player.getUniqueId(), false);
                            player.sendMessage(main.getComponentMessage("SignSpyOffMessage"));
                        } else {
                            main.getStorage().put(player.getUniqueId(), true);
                            player.sendMessage(main.getComponentMessage("SignSpyOnMessage"));
                        }
                    } else {
                        main.getStorage().put(player.getUniqueId(), true);
                        player.sendMessage(main.getComponentMessage("SignSpyOnMessage"));
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
