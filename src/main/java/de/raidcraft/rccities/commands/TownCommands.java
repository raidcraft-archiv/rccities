package de.raidcraft.rccities.commands;

import com.sk89q.minecraft.util.commands.*;
import de.raidcraft.api.RaidCraftException;
import de.raidcraft.api.commands.QueuedCaptchaCommand;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.resident.Resident;
import de.raidcraft.rccities.api.resident.RolePermission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

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
    @NestedCommand(value = NestedCommands.class, executeBody = true)
    public void town(CommandContext args, CommandSender sender) throws CommandException {

        if(sender instanceof ConsoleCommandSender) throw new CommandException("Player required!");
        Player player = (Player)sender;

        List<Resident> citizenships = plugin.getResidentManager().getCitizenships(player.getName());
        if(citizenships == null || citizenships.size() > 1) {
            throw new CommandException("Nutze /town info <Stadtname>!");
        }
        try {
            plugin.getCityManager().printCityInfo(citizenships.get(0).getName(), sender);
        } catch (RaidCraftException e) {
            throw new CommandException(e.getMessage());
        }
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
            plugin.getPlotManager().clearCache();
            plugin.getResidentManager().clearCache();
            sender.sendMessage(ChatColor.GREEN + "RCCities wurde neugeladen und alle Caches geleert!");
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

            //TODO claim first plot

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

            City city;
            if(args.argsLength() > 0) {
                city = plugin.getCityManager().getCity(args.getString(0));
                if(city == null) {
                    throw new CommandException("Es gibt keine Stadt mit dem Name '" + args.getString(0) + "'!");
                }
                if(!player.hasPermission("rccities.setspawn.all")) {
                    Resident resident = plugin.getResidentManager().getResident(player.getName(), city);
                    if(resident == null || !resident.getRole().hasPermission(RolePermission.SET_SPAWN)) {
                        throw new CommandException("Du darfst von dieser Stadt den Spawn nicht versetzen!");
                    }
                }
            }
            else {
                List<Resident> citizenships = plugin.getResidentManager().getCitizenships(player.getName(), RolePermission.SET_SPAWN);

                if(citizenships == null) {
                    throw new CommandException("Du besitzt in keiner Stadt das Recht den Stadtspawn zu versetzen!");
                }
                if(citizenships.size() > 1) {
                    throw new CommandException("Du besitzt in mehreren Städten das Recht den Stadtspawn zu verändern! Gebe die gewünschte Stadt als Parameter an.");
                }
                city = citizenships.get(0).getCity();
            }

            if(!city.getSpawn().getWorld().equals(player.getWorld())) {
                throw new CommandException("Der Spawn muss sich auf der selben Welt wie die Stadt befinden!");
            }

            city.setSpawn(player.getLocation());
            plugin.getResidentManager().broadcastCityMessage(city, "Der Stadtspawn von '" + city.getFriendlyName() + "' wurde versetzt!");
        }

        @Command(
                aliases = {"setdescription", "setdesc"},
                desc = "Change city description",
                min = 2,
                usage = "<Stadtname> <Beschreibung>"
        )
        @CommandPermissions("rccities.setdescription")
        public void setDescription(CommandContext args, CommandSender sender) throws CommandException {

            if(sender instanceof ConsoleCommandSender) throw new CommandException("Player required!");
            Player player = (Player)sender;

            City city = plugin.getCityManager().getCity(args.getString(0));
            if(city == null) {
                throw new CommandException("Es gibt keine Stadt mit dem Name '" + args.getString(0) + "'!");
            }
            if(!player.hasPermission("rccities.setspawn.all")) {
                Resident resident = plugin.getResidentManager().getResident(player.getName(), city);
                if(resident == null || !resident.getRole().hasPermission(RolePermission.SET_SPAWN)) {
                    throw new CommandException("Du darfst von dieser Stadt die Beschreibung nicht ändern!");
                }
            }
            String description = args.getJoinedStrings(1);

            city.setDescription(description);
            plugin.getResidentManager().broadcastCityMessage(city, "Die Beschreibung der Stadt '" + city.getFriendlyName() + "' wurde geändert!");
        }

        @Command(
                aliases = {"info"},
                desc = "Shows city info",
                min = 1,
                usage = "<Stadtname>"
        )
        @CommandPermissions("rccities.info")
        public void info(CommandContext args, CommandSender sender) throws CommandException {

            try {
                plugin.getCityManager().printCityInfo(args.getString(0), sender);
            } catch (RaidCraftException e) {
                throw new CommandException(e.getMessage());
            }
        }

        @Command(
                aliases = {"list"},
                desc = "List all existing cities"
        )
        @CommandPermissions("rccities.list")
        public void list(CommandContext args, CommandSender sender) throws CommandException {

            Collection<City> cities = plugin.getCityManager().getCities();
            sender.sendMessage(ChatColor.BLUE + "Es gibt derzeit " + ChatColor.AQUA + cities.size() + ChatColor.BLUE + " Städte auf dem Server:");
            String cityList = "";
            for(City city : cities) {
                if(!cityList.isEmpty()) cityList += ChatColor.WHITE + ", ";
                cityList += ChatColor.BLUE + city.getFriendlyName();
            }
            sender.sendMessage(cityList);
        }




        /*
         ***********************************************************************************************************************************
         */

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
