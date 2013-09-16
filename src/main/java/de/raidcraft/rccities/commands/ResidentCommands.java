package de.raidcraft.rccities.commands;

import com.sk89q.minecraft.util.commands.*;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.resident.Resident;
import de.raidcraft.rccities.api.resident.Role;
import de.raidcraft.rccities.api.resident.RolePermission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

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

        @Command(
                aliases = {"setrole", "promote"},
                desc = "Shows info about a resident",
                min = 2,
                usage = "[Stadtname] <Spieler> <Beruf>"
        )
        @CommandPermissions("rccities.resident.promote")
        public void setRole(CommandContext args, CommandSender sender) throws CommandException {

            if(sender instanceof ConsoleCommandSender) throw new CommandException("Player required!");
            Player player = (Player)sender;

            City city;
            String target;
            String roleName;
            if(args.argsLength() > 2) {
                target = args.getString(1);
                roleName = args.getString(2);
                city = plugin.getCityManager().getCity(args.getString(0));
                if(city == null) {
                    throw new CommandException("Es gibt keine Stadt mit dem Name '" + args.getString(0) + "'!");
                }
                if(!player.hasPermission("rccities.resident.promote.all")) {
                    Resident resident = plugin.getResidentManager().getResident(player.getName(), city);
                    if(resident == null || !resident.getRole().hasPermission(RolePermission.KICK)) {
                        throw new CommandException("Du darfst keine Berufe in der Stadt '" + city.getFriendlyName() + "' zuweisen!");
                    }
                }
            }
            else {
                target = args.getString(0);
                roleName = args.getString(1);
                List<Resident> citizenships = plugin.getResidentManager().getCitizenships(player.getName(), RolePermission.PROMOTE);
                if(citizenships == null) {
                    throw new CommandException("Du besitzt in keiner Stadt das Recht Spielern Berufe zuzuteilen!");
                }
                if(citizenships.size() > 1) {
                    throw new CommandException("Du besitzt in mehreren Städten das Recht Spielern Berufe zuzuteilen! Gebe die gewünschte Stadt als Parameter an.");
                }
                city = citizenships.get(0).getCity();
            }

            Role newRole = Role.valueOf(roleName);
            if(newRole == null) {
                throw new CommandException("Es gibt keinen Beruf mit diesem Namen. Verfügbare Berufe: " + Arrays.toString(Role.values()));
            }

            Resident targetResident = plugin.getResidentManager().getResident(target, city);
            if(targetResident == null) {
                throw new CommandException("In dieser Stadt gibt es keinen Einwohner mit dem Namen '" + target + "'");
            }

            targetResident.setRole(newRole);
            Bukkit.broadcastMessage(ChatColor.GOLD + targetResident.getName() + " ist nun " + newRole.getFriendlyName() + " der Stadt '" + city.getFriendlyName() + "'!");
        }
    }

}
