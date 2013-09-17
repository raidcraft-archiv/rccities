package de.raidcraft.rccities.commands;

import com.sk89q.minecraft.util.commands.*;
import de.raidcraft.rccities.RCCitiesPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * @author Philip Urban
 */
public class PlotCommands {

    private RCCitiesPlugin plugin;

    public PlotCommands(RCCitiesPlugin plugin) {

        this.plugin = plugin;
    }

    @Command(
            aliases = {"plot"},
            desc = "Plot commands"
    )
    @NestedCommand(value = NestedCommands.class, executeBody = true)
    public void plot(CommandContext args, CommandSender sender) throws CommandException {

        if(sender instanceof ConsoleCommandSender) throw new CommandException("Player required!");
        Player player = (Player)sender;

        //TODO print plot info
    }

    public static class NestedCommands {

        private final RCCitiesPlugin plugin;

        public NestedCommands(RCCitiesPlugin plugin) {

            this.plugin = plugin;
        }

        @Command(
                aliases = {"take"},
                desc = "Take a plot from player"
        )
        @CommandPermissions("rccities.plot.take")
        public void take(CommandContext args, CommandSender sender) {

            //TODO
        }

        @Command(
                aliases = {"give"},
                desc = "Distributes a plot"
        )
        @CommandPermissions("rccities.plot.give")
        public void give(CommandContext args, CommandSender sender) {

            //TODO
        }

        @Command(
                aliases = {"claim"},
                desc = "Claims a plot"
        )
        @CommandPermissions("rccities.plot.claim")
        public void claim(CommandContext args, CommandSender sender) {

            //TODO
        }

        @Command(
                aliases = {"unclaim"},
                desc = "Unclaims a plot"
        )
        @CommandPermissions("rccities.plot.unclaim")
        public void unclaim(CommandContext args, CommandSender sender) {

            //TODO
        }
    }

}
