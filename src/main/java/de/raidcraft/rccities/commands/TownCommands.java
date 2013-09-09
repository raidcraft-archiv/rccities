package de.raidcraft.rccities.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.NestedCommand;
import de.raidcraft.rccities.RCCitiesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * @author Philip Urban
 */
public class TownCommands {

    private RCCitiesPlugin plugin;

    public TownCommands(RCCitiesPlugin plugin) {

        this.plugin = plugin;
    }

    @Command(
            aliases = {"rccities", "town", "towns"},
            desc = "Town commands"
    )
    @NestedCommand(value = NestedCommands.class)
    public void town(CommandContext args, CommandSender sender) {
    }

    public static class NestedCommands {

        private final RCCitiesPlugin plugin;

        public NestedCommands(RCCitiesPlugin plugin) {

            this.plugin = plugin;
        }

        @Command(
                aliases = {"reload"},
                desc = "Reloads the plugin"
        )
        @CommandPermissions("rccities.admin")
        public void reload(CommandContext args, CommandSender sender) {

            plugin.reload();
            sender.sendMessage(ChatColor.GREEN + "RCCities wurde neugeladen.");
        }
    }

}
