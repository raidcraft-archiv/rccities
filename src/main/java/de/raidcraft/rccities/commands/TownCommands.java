package de.raidcraft.rccities.commands;

import com.sk89q.minecraft.util.commands.*;
import de.raidcraft.api.RaidCraftException;
import de.raidcraft.api.commands.QueuedCaptchaCommand;
import de.raidcraft.rccities.DatabasePlot;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.flags.CityFlag;
import de.raidcraft.rccities.api.plot.Plot;
import de.raidcraft.rccities.api.resident.Resident;
import de.raidcraft.rccities.api.resident.RolePermission;
import de.raidcraft.rccities.flags.city.GreetingsCityFlag;
import de.raidcraft.rccities.flags.city.JoinCostsCityFlag;
import de.raidcraft.rccities.flags.city.PvpCityFlag;
import de.raidcraft.rccities.flags.city.admin.InviteCityFlag;
import de.raidcraft.util.CaseInsensitiveMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Philip Urban
 */
public class TownCommands {

    private RCCitiesPlugin plugin;

    public TownCommands(RCCitiesPlugin plugin) {

        this.plugin = plugin;
    }

    @Command(
            aliases = {"gilde", "guild", "rccities", "town", "towns", "city"},
            desc = "Town commands"
    )
    @NestedCommand(value = NestedCommands.class, executeBody = true)
    public void town(CommandContext args, CommandSender sender) throws CommandException {

        if(sender instanceof ConsoleCommandSender) throw new CommandException("Player required!");
        Player player = (Player)sender;

        List<Resident> citizenships = plugin.getResidentManager().getCitizenships(player.getName());
        if(citizenships == null || citizenships.size() > 1) {
            throw new CommandException("Mehrere Gilden gefunden: Nutze /gilde info <Gildenname>!");
        }
        try {
            plugin.getCityManager().printCityInfo(citizenships.get(0).getCity().getName(), sender);
        } catch (RaidCraftException e) {
            throw new CommandException(e.getMessage());
        }
    }

    public static class NestedCommands {

        private final RCCitiesPlugin plugin;
        private Map<String, City> invites = new CaseInsensitiveMap<>();

        public NestedCommands(RCCitiesPlugin plugin) {

            this.plugin = plugin;
        }

        @Command(
                aliases = {"reload"},
                desc = "Reloads the plugin"
        )
        @CommandPermissions("rccities.town.reload")
        public void reload(CommandContext args, CommandSender sender) {

            plugin.reload();

            for(City city : plugin.getCityManager().getCities()) {
                plugin.getDynmapManager().addCityMarker(city);
            }

            plugin.getCityManager().clearCache();
            plugin.getPlotManager().clearCache();
            plugin.getResidentManager().clearCache();
            plugin.getFlagManager().clearCache();

            sender.sendMessage(ChatColor.GREEN + "RCCities wurde neugeladen und alle Caches geleert!");
        }

        @Command(
                aliases = {"create"},
                desc = "Create a new city",
                min = 1,
                usage = "<Stadtname>"
        )
         @CommandPermissions("rccities.town.create")
         public void create(CommandContext args, CommandSender sender) throws CommandException {

            if(sender instanceof ConsoleCommandSender) throw new CommandException("Player required!");
            Player player = (Player)sender;

            City city;
            try {
                city = plugin.getCityManager().createCity(args.getJoinedStrings(0), player.getLocation(), player.getName());

                // default flags

                // create initial plot
                Plot plot = new DatabasePlot(player.getLocation(), city);

                // create schematic
                try {
                    plugin.getSchematicManager().createSchematic(plot);
                } catch (RaidCraftException e) {
                    throw new CommandException(e.getMessage());
                }

                // set flags at the end because of possible errors
                plugin.getFlagManager().setCityFlag(city, player, PvpCityFlag.class, false);        // disable pvp
                plugin.getFlagManager().setCityFlag(city, player, InviteCityFlag.class, false);     // disable invites
                plugin.getFlagManager().setCityFlag(city, player, GreetingsCityFlag.class, true);   // enable greetings
                plugin.getFlagManager().setCityFlag(city, player, JoinCostsCityFlag.class, plugin.getConfig().joinCosts);   // default join costs

                plugin.getDynmapManager().addCityMarker(city);

            } catch (RaidCraftException e) {
                throw new CommandException(e.getMessage());
            }
            Bukkit.broadcastMessage(ChatColor.GOLD + "Es wurde die Stadt '" + city.getFriendlyName() + "' gegründet!");
        }

        @Command(
                aliases = {"delete"},
                desc = "Delete an existing city",
                min = 1,
                flags = "r",
                usage = "<Stadtname>"
        )
        @CommandPermissions("rccities.town.delete")
        public void delete(CommandContext args, CommandSender sender) throws CommandException {

            City city = plugin.getCityManager().getCity(args.getString(0));
            if(city == null) {
                throw new CommandException("Es wurde keine Stadt mit diesem namen gefunden!");
            }

            boolean restoreSchematics= false;
            if(args.hasFlag('r')) {
                restoreSchematics = true;
            }

            try {
                if(restoreSchematics) {
                    sender.sendMessage(ChatColor.DARK_RED + "Bei der Löschung der Stadt werden vorhandene Plots zurückgesetzt!");
                }
                else {
                    sender.sendMessage(ChatColor.DARK_RED + "Bei der Löschung der Stadt werden vorhandene Plots NICHT zurückgesetzt!");
                }
                new QueuedCaptchaCommand(sender, this, "deleteCity", sender, city, restoreSchematics);
            } catch (NoSuchMethodException e) {
                throw new CommandException(e.getMessage());
            }
        }

        @Command(
                aliases = {"spawn"},
                desc = "Teleport to town spawn",
                usage = "[Stadtname]"
        )
        @CommandPermissions("rccities.town.spawn")
        public void spawn(CommandContext args, CommandSender sender) throws CommandException {

            if(sender instanceof ConsoleCommandSender) throw new CommandException("Player required!");
            Player player = (Player)sender;

            City city;
            if(args.argsLength() > 0) {
                city = plugin.getCityManager().getCity(args.getString(0));
                if(city == null) {
                    throw new CommandException("Es gibt keine Stadt mit dem Name '" + args.getString(0) + "'!");
                }
                if(!player.hasPermission("rccities.town.spawn.all")) {
                    Resident resident = plugin.getResidentManager().getResident(player.getName(), city);
                    if(resident == null || !resident.getRole().hasPermission(RolePermission.SPAWN_TELEPORT)) {
                        throw new CommandException("Du darfst dich nicht zum Spawn der Stadt '" + city.getFriendlyName() + "' porten!");
                    }
                }
            }
            else {
                List<Resident> citizenships = plugin.getResidentManager().getCitizenships(player.getName(), RolePermission.SPAWN_TELEPORT);

                if(citizenships == null) {
                    throw new CommandException("Du besitzt in keiner Stadt das Recht dich zum Spawn zu porten!");
                }
                if(citizenships.size() > 1) {
                    throw new CommandException("Du besitzt in mehreren Städten das Recht dich zum Spawn zu porten! Gebe die gewünschte Stadt als Parameter an.");
                }
                city = citizenships.get(0).getCity();
            }

            if(!city.getSpawn().getWorld().equals(player.getWorld())) {
                throw new CommandException("Du befindest dich auf der falschen Welt!");
            }

            player.teleport(city.getSpawn());
            player.sendMessage(ChatColor.YELLOW + "Du wurdest zum Stadtspawn von '" + city.getFriendlyName() + "' teleportiert!");
        }

        @Command(
                aliases = {"setspawn"},
                desc = "Redefine the town spawn location",
                usage = "[Stadtname]"
        )
        @CommandPermissions("rccities.town.setspawn")
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
                        throw new CommandException("Du darfst von der Stadt '" + city.getFriendlyName() + "' den Spawn nicht versetzen!");
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
            plugin.getResidentManager().broadcastCityMessage(city, "Der Stadtspawn wurde versetzt!");
        }

        @Command(
                aliases = {"setdescription", "setdesc"},
                desc = "Change city description",
                min = 2,
                usage = "<Stadtname> <Beschreibung>"
        )
        @CommandPermissions("rccities.town.setdescription")
        public void setDescription(CommandContext args, CommandSender sender) throws CommandException {

            if(sender instanceof ConsoleCommandSender) throw new CommandException("Player required!");
            Player player = (Player)sender;

            City city = plugin.getCityManager().getCity(args.getString(0));
            if(city == null) {
                throw new CommandException("Es gibt keine Stadt mit dem Name '" + args.getString(0) + "'!");
            }
            if(!player.hasPermission("rccities.setspawn.all")) {
                Resident resident = plugin.getResidentManager().getResident(player.getName(), city);
                if(resident == null || !resident.getRole().hasPermission(RolePermission.SET_DESCRIPTION)) {
                    throw new CommandException("Du darfst von der Stadt '" + city.getFriendlyName() + "' die Beschreibung nicht ändern!");
                }
            }
            String description = args.getJoinedStrings(1);

            city.setDescription(description);
            player.sendMessage(ChatColor.GREEN + "Du hast die Beschreibung der Stadt '" + city.getFriendlyName() + "' geändert!");
            plugin.getResidentManager().broadcastCityMessage(city, "Die Beschreibung der Stadt wurde geändert!");
        }

        @Command(
                aliases = {"info"},
                desc = "Shows city info",
                min = 1,
                usage = "<Stadtname>"
        )
        @CommandPermissions("rccities.town.info")
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
        @CommandPermissions("rccities.town.list")
        public void list(CommandContext args, CommandSender sender) throws CommandException {

            Collection<City> cities = plugin.getCityManager().getCities();
            sender.sendMessage(ChatColor.GOLD + "Es gibt derzeit " + ChatColor.YELLOW + cities.size() + ChatColor.GOLD + " Städte auf dem Server:");
            String cityList = "";
            for(City city : cities) {
                if(!cityList.isEmpty()) cityList += ChatColor.GOLD + ", ";
                cityList += ChatColor.YELLOW + city.getFriendlyName();
            }
            sender.sendMessage(cityList);
        }

        @Command(
                aliases = {"flag"},
                desc = "Change city flag",
                min = 2,
                usage = "<Stadtname> <Flag> [Parameter]"
        )
        @CommandPermissions("rccities.town.flag")
        public void flag(CommandContext args, CommandSender sender) throws CommandException {

            if(sender instanceof ConsoleCommandSender) throw new CommandException("Player required!");
            Player player = (Player)sender;

            City city;
            String flagValue = null;
            String flagName = args.getString(1);
            if(args.argsLength() > 2) {
                flagValue = args.getString(2);
            }
            city = plugin.getCityManager().getCity(args.getString(0));
            if(city == null) {
                throw new CommandException("Es gibt keine Stadt mit dem Name '" + args.getString(0) + "'!");
            }
            if(!player.hasPermission("rccities.town.flag.all")) {
                Resident resident = plugin.getResidentManager().getResident(player.getName(), city);
                if(resident == null || !resident.getRole().hasPermission(RolePermission.CITY_FLAG_MODIFICATION)) {
                    throw new CommandException("Du darfst von der Stadt '" + city.getFriendlyName() + "' keine Flags ändern!");
                }
            }

            try {
                city.setFlag(player, flagName, flagValue);
            } catch (RaidCraftException e) {
                throw new CommandException(e.getMessage());
            }
            player.sendMessage(ChatColor.GREEN + "Du hast erfolgreich die Flag '" + ChatColor.YELLOW + flagName.toUpperCase()
                    + ChatColor.GREEN + "' auf den Wert '" + ChatColor.YELLOW + flagValue.toUpperCase() + ChatColor.GREEN + "' gesetzt!");
        }

        @Command(
                aliases = {"invite"},
                desc = "Invites an player as resident",
                min = 1,
                usage = "[Stadtname] <Spielername>"
        )
                 @CommandPermissions("rccities.town.invite")
                 public void invite(CommandContext args, CommandSender sender) throws CommandException {

            if(sender instanceof ConsoleCommandSender) throw new CommandException("Player required!");
            Player player = (Player)sender;

            City city;
            Player targetPlayer;
            if(args.argsLength() > 1) {
                targetPlayer = Bukkit.getPlayer(args.getString(1));
                if(targetPlayer == null) {
                    throw new CommandException("Der gewählte Spieler muss online sein!");
                }
                city = plugin.getCityManager().getCity(args.getString(0));
                if(city == null) {
                    throw new CommandException("Es gibt keine Stadt mit dem Name '" + args.getString(0) + "'!");
                }
                if(!player.hasPermission("rccities.town.invite.all")) {
                    Resident resident = plugin.getResidentManager().getResident(player.getName(), city);
                    if(resident == null || !resident.getRole().hasPermission(RolePermission.INVITE)) {
                        throw new CommandException("Du darfst in die Stadt '" + city.getFriendlyName() + "' keine Bürger einladen!");
                    }
                }
            }
            else {
                targetPlayer = Bukkit.getPlayer(args.getString(0));
                if(targetPlayer == null) {
                    throw new CommandException("Der gewählte Spieler muss online sein!");
                }

                List<Resident> citizenships = plugin.getResidentManager().getCitizenships(player.getName(), RolePermission.INVITE);

                if(citizenships == null) {
                    throw new CommandException("Du besitzt in keiner Stadt das Recht Spieler einzuladen!");
                }
                if(citizenships.size() > 1) {
                    throw new CommandException("Du besitzt in mehreren Städten das Recht Spieler einzuladen! Gebe die gewünschte Stadt als Parameter an.");
                }
                city = citizenships.get(0).getCity();
            }

            if(player.getName().equalsIgnoreCase(targetPlayer.getName())) {
                throw new CommandException("Du kannst dich nicht selbst in die Stadt einladen!");
            }

            // invite is locked
            CityFlag inviteFlag = plugin.getFlagManager().getCityFlag(city, InviteCityFlag.class);
            if(inviteFlag != null && !inviteFlag.getType().convertToBoolean(inviteFlag.getValue())) {
                throw new CommandException("Deine Stadt darf zurzeit keine neuen Spieler einladen!");
            }

            invites.put(targetPlayer.getName(), city);
            targetPlayer.sendMessage(ChatColor.GOLD + "Du wurdest in die Stadt '" + city.getFriendlyName() + "' eingeladen!");
            targetPlayer.sendMessage(ChatColor.GOLD + "Bestätige die Einladung mit '/town accept'");
            player.sendMessage(ChatColor.GREEN + "Du hast " + targetPlayer.getName() + " in die Stadt '" + city.getFriendlyName() + "' eingeladen!");
        }

        @Command(
                aliases = {"accept"},
                desc = "Accept an invite"
        )
        @CommandPermissions("rccities.town.invite")
        public void accept(CommandContext args, CommandSender sender) throws CommandException {

            if(sender instanceof ConsoleCommandSender) throw new CommandException("Player required!");
            Player player = (Player)sender;

            if(!invites.containsKey(player.getName())) {
                throw new CommandException("Du hast keine offenen Einladungen!");
            }

            City city = invites.get(player.getName());
            try {
                plugin.getResidentManager().addResident(city, player);
            } catch (RaidCraftException e) {
                throw new CommandException(e.getMessage());
            }
            Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + " ist nun Einwohner von '" + city.getFriendlyName() + "'!");
        }

        @Command(
                aliases = {"leave"},
                desc = "Leaves a city",
                flags = "f",
                usage = "[Stadtname]"
        )
        @CommandPermissions("rccities.town.leave")
        public void leave(CommandContext args, CommandSender sender) throws CommandException {

            if(sender instanceof ConsoleCommandSender) throw new CommandException("Player required!");
            Player player = (Player)sender;

            City city;
            if(args.argsLength() > 0) {
                city = plugin.getCityManager().getCity(args.getString(0));
                if(city == null) {
                    throw new CommandException("Es gibt keine Stadt mit dem Name '" + args.getString(0) + "'!");
                }
                if(!player.hasPermission("rccities.town.leave.all")) {
                    Resident resident = plugin.getResidentManager().getResident(player.getName(), city);
                    if(resident == null) {
                        throw new CommandException("Du bist kein Einwohner der Stadt '" + city.getFriendlyName() + "'!");
                    }
                    else if(!resident.getRole().hasPermission(RolePermission.LEAVE)) {
                        throw new CommandException("Du darfst die Stadt '" + city.getFriendlyName() + "' nicht verlassen!");
                    }
                }
            }
            else {
                List<Resident> citizenships = plugin.getResidentManager().getCitizenships(player.getName(), RolePermission.LEAVE);

                if(citizenships == null) {
                    throw new CommandException("Du besitzt in keiner Stadt das Recht diese zu verlassen!");
                }
                if(citizenships.size() > 1) {
                    throw new CommandException("Du bist Bürger von mehreren Städten. Gebe die gewünschte Stadt als Parameter an.");
                }
                city = citizenships.get(0).getCity();
            }

            Resident resident = plugin.getResidentManager().getResident(player.getName(), city);
            if(resident == null) {
                throw new CommandException("Du bist kein Einwohner der Stadt '" + city.getFriendlyName() + "'!");
            }

            if(args.hasFlag('f')) {
                leaveCity(resident);
            }
            else {
                try {
                    new QueuedCaptchaCommand(sender, this, "leaveCity", resident);
                } catch (NoSuchMethodException e) {
                    throw new CommandException(e.getMessage());
                }
            }
        }

        @Command(
                aliases = {"kick"},
                desc = "Kicks a resident",
                flags = "f",
                usage = "[Stadtname] <Spieler>"
        )
        @CommandPermissions("rccities.town.kick")
        public void kick(CommandContext args, CommandSender sender) throws CommandException {

            if(sender instanceof ConsoleCommandSender) throw new CommandException("Player required!");
            Player player = (Player)sender;

            City city;
            String target;
            if(args.argsLength() > 1) {
                target = args.getString(1);
                city = plugin.getCityManager().getCity(args.getString(0));
                if(city == null) {
                    throw new CommandException("Es gibt keine Stadt mit dem Name '" + args.getString(0) + "'!");
                }
                if(!player.hasPermission("rccities.town.kick.all")) {
                    Resident resident = plugin.getResidentManager().getResident(player.getName(), city);
                    if(resident == null || !resident.getRole().hasPermission(RolePermission.KICK)) {
                        throw new CommandException("Du darfst keine Bürger aus der Stadt '" + city.getFriendlyName() + "' schmeissen!");
                    }
                }
            }
            else {
                target = args.getString(0);
                List<Resident> citizenships = plugin.getResidentManager().getCitizenships(player.getName(), RolePermission.KICK);
                if(citizenships == null) {
                    throw new CommandException("Du besitzt in keiner Stadt das Recht Spieler rauszuschmeissen!");
                }
                if(citizenships.size() > 1) {
                    throw new CommandException("Du besitzt in mehreren Städten das Recht Spieler rauszuschmeissen! Gebe die gewünschte Stadt als Parameter an.");
                }
                city = citizenships.get(0).getCity();
            }

            if(!args.hasFlag('f') && player.getName().equalsIgnoreCase(target)) {
                throw new CommandException("Du kannst dich nicht selbst aus der Stadt schmeissen!");
            }

            Resident resident = plugin.getResidentManager().getResident(target, city);
            if(resident == null) {
                throw new CommandException(target + " ist kein Einwohner von '" + city.getFriendlyName() + "'!");
            }

            resident.delete();
            Bukkit.broadcastMessage(ChatColor.GOLD + target + " wurde aus der Stadt '" + city.getFriendlyName() + "' geschmissen!");
        }

        /*
         ***********************************************************************************************************************************
         */

        public void deleteCity(CommandSender sender, City city, boolean restoreSchematics) {

            if(restoreSchematics) {
                try {
                    plugin.getSchematicManager().restoreCity(city);
                } catch (RaidCraftException e) {
                    sender.sendMessage(ChatColor.RED + "Es ist ein Fehler beim wiederherstellen der Plots aufgetreten! (" + e.getMessage() + ")");
                }
            }

            city.delete();
            Bukkit.broadcastMessage(ChatColor.GOLD + "Die Stadt '" + city.getFriendlyName() + "' wurde gelöscht!");
        }

        public void leaveCity(Resident resident) {

            Bukkit.broadcastMessage(ChatColor.GOLD + resident.getName() + " hat die Stadt '" + resident.getCity().getFriendlyName() + "' verlassen!");
            resident.delete();
        }
    }

}
