package de.raidcraft.rccities.commands;

import com.sk89q.minecraft.util.commands.*;
import de.raidcraft.api.RaidCraftException;
import de.raidcraft.api.commands.QueuedCaptchaCommand;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * @author Philip Urban
 */
public class TownCommands {

    private RCCitiesPlugin plugin;

    public TownCommands(RCCitiesPlugin plugin) {

        this.plugin = plugin;
    }

    @Command(
            aliases = {"rccities", "town", "towns", "city"},
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
        @CommandPermissions("rccities.reload")
        public void reload(CommandContext args, CommandSender sender) {

            plugin.reload();
            plugin.getCityManager().clearCache();
            sender.sendMessage(ChatColor.GREEN + "RCCities wurde neugeladen.");
        }

        @Command(
                aliases = {"create"},
                desc = "Create a new city",
                min = 1,
                usage = "<Stadtname>"
        )
         @CommandPermissions("rccities.create")
         public void create(CommandContext args, CommandSender sender) throws CommandException {

            if(sender instanceof ConsoleCommandSender) throw new CommandException("Player required!");
            Player player = (Player)sender;

            City city;
            try {
                city = plugin.getCityManager().createCity(args.getString(0), player.getLocation(), player.getName());
            } catch (RaidCraftException e) {
                throw new CommandException(e.getMessage());
            }
            Bukkit.broadcastMessage(ChatColor.DARK_BLUE + "Es wurde die Stadt '" + city.getFriendlyName() + "' gegründet!");
        }

        @Command(
                aliases = {"delete"},
                desc = "Delete an existing city",
                min = 1,
                flags = "f",
                usage = "<Stadtname>"
        )
        @CommandPermissions("rccities.delete")
        public void delete(CommandContext args, CommandSender sender) throws CommandException {

            if(args.hasFlag('f')) {
                deleteCity(sender, args.getString(0));
            }
            else {
                try {
                    new QueuedCaptchaCommand(sender, this, "deleteCity", sender, args.getString(0));
                } catch (NoSuchMethodException e) {
                    throw new CommandException(e.getMessage());
                }
            }
        }

        @Command(
                aliases = {"setspawn"},
                desc = "Redefine the town spawn location",
                usage = "[Stadtname]"
        )
        @CommandPermissions("rccities.setspawn")
        public void setSpawn(CommandContext args, CommandSender sender) throws CommandException {

            if(sender instanceof ConsoleCommandSender) throw new CommandException("Player required!");
            Player player = (Player)sender;

            String cityName;
            if(args.argsLength() > 0 && player.hasPermission("rccities.setspawn.all")) {
                cityName = args.getString(0);
            }
            else {

            }

        }

        public void deleteCity(CommandSender sender, String cityName) {

            try {
                plugin.getCityManager().deleteCity(cityName);
            } catch (RaidCraftException e) {
                sender.sendMessage(ChatColor.RED + e.getMessage());
            }
            Bukkit.broadcastMessage(ChatColor.DARK_BLUE + "Die Stadt '" + cityName + "' wurde gelöscht!");
        }
    }

}
