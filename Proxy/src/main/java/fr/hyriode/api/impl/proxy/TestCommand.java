package fr.hyriode.api.impl.proxy;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 * Created by AstFaster
 * on 23/04/2023 at 22:09
 */
public class TestCommand extends Command {

    public TestCommand() {
        super("test");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            final ProxiedPlayer player = (ProxiedPlayer) sender;

            if (player.getName().equals("AstFaster")) {
                final String arg = args[0];

                if (arg.equalsIgnoreCase("start")) {
                    BungeeCord.getInstance().getPluginManager().dispatchCommand(BungeeCord.getInstance().getConsole(), "sparkb profiler start");
                } else if (arg.equalsIgnoreCase("stop")) {
                    BungeeCord.getInstance().getPluginManager().dispatchCommand(BungeeCord.getInstance().getConsole(), "sparkb profiler stop");
                }
            }
        }
    }

}
