package de.raidcraft.rccities.commands;

import com.sk89q.minecraft.util.commands.*;
import de.raidcraft.rccities.RCCitiesPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * @author Philip Urban
 */
public class ResidentCommands {

    private RCCitiesPlugin plugin;

    public ResidentCommands(RCCitiesPlugin plugin) {

        this.plugin = plugin;
    }

    @Command(
            aliases = {"resident", "einwohner"},
            desc = "Resident commands"
    )
    @NestedCommand(value = NestedCommands.class, executeBody = true)
    public void resident(CommandContext args, CommandSender sender) throws CommandException {

        if(sender instanceof ConsoleCommandSender) throw new CommandException("Player required!");
        Player player = (Player)sender;

        plugin.getResidentManager().printResidentInfo(player.getName(), sender);
    }

    public static class NestedCommands {

        private final RCCitiesPlugin plugin;

        public NestedCommands(RCCitiesPlugin plugin) {

            this.plugin = plugin;
        }

        @Command(
                aliases = {"info"},
                desc = "Shows info about a resident",
                min = 1,
                usage = "<Resident Name>"
        )
        @CommandPermissions("rccities.resident.info")
        public void info(CommandContext args, CommandSender sender) throws CommandException {

            plugin.getResidentManager().printResidentInfo(args.getString(0), sender);
        }
    }

}
