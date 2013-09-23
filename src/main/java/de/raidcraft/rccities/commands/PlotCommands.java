package de.raidcraft.rccities.commands;

import com.sk89q.minecraft.util.commands.*;
import de.raidcraft.api.RaidCraftException;
import de.raidcraft.api.commands.QueuedCaptchaCommand;
import de.raidcraft.rccities.DatabasePlot;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.plot.Plot;
import de.raidcraft.rccities.api.resident.Resident;
import de.raidcraft.rccities.api.resident.RolePermission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
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
        public void take(CommandContext args, CommandSender sender) throws CommandException {

            //TODO
        }

        @Command(
                aliases = {"give"},
                desc = "Distributes a plot"
        )
        @CommandPermissions("rccities.plot.give")
        public void give(CommandContext args, CommandSender sender) throws CommandException {

            //TODO
        }

        @Command(
                aliases = {"claim"},
                desc = "Claims a plot"
        )
        @CommandPermissions("rccities.plot.claim")
        public void claim(CommandContext args, CommandSender sender) throws CommandException {

            if(sender instanceof ConsoleCommandSender) throw new CommandException("Player required!");
            Player player = (Player)sender;

            // check if here is a wrong region
            if(!plugin.getWorldGuardManager().claimable(player.getLocation())) {
                throw new CommandException("An dieser Stelle befindet sich bereits eine andere Region!");
            }

            // get neighbor plot and city
            Chunk chunk = player.getLocation().getChunk();
            Plot[] neighborPlots = new Plot[8];
            neighborPlots[0] = plugin.getPlotManager().getPlot(player.getWorld().getChunkAt(chunk.getX(), chunk.getZ() + 1));
            neighborPlots[1] = plugin.getPlotManager().getPlot(player.getWorld().getChunkAt(chunk.getX() - 1, chunk.getZ() + 1));
            neighborPlots[2] = plugin.getPlotManager().getPlot(player.getWorld().getChunkAt(chunk.getX() + 1, chunk.getZ() + 1));
            neighborPlots[3] = plugin.getPlotManager().getPlot(player.getWorld().getChunkAt(chunk.getX(), chunk.getZ() - 1));
            neighborPlots[4] = plugin.getPlotManager().getPlot(player.getWorld().getChunkAt(chunk.getX() - 1, chunk.getZ() - 1));
            neighborPlots[5] = plugin.getPlotManager().getPlot(player.getWorld().getChunkAt(chunk.getX() + 1, chunk.getZ() - 1));
            neighborPlots[6] = plugin.getPlotManager().getPlot(player.getWorld().getChunkAt(chunk.getX() - 1, chunk.getZ()));
            neighborPlots[7] = plugin.getPlotManager().getPlot(player.getWorld().getChunkAt(chunk.getX() + 1, chunk.getZ()));

            City city = null;
            for(Plot plot : neighborPlots) {
                if(plot == null) continue;
                if(city != null && !city.equals(plot.getCity())) {
                    throw new CommandException("Dieser Plot liegt zu dicht an einer anderen Stadt!");
                }
                city = plot.getCity();
            }
            if(city == null) {
                throw new CommandException("Neue Plots müssen an bestehende anknüpfen!");
            }

            // check if resident has permission
            Resident resident = plugin.getResidentManager().getResident(player.getName(), city);
            if(resident == null || !resident.getRole().hasPermission(RolePermission.PLOT_CLAIM)) {
                throw new CommandException("Du hast in der Stadt '" + city.getFriendlyName() + "' nicht die Berechtigung Plots zu claimen!");
            }

            // check plot credit
            if(city.getPlotCredit() == 0) {
                throw new CommandException("Deine Stadt hat keine freien Plots zum claimen!");
            }

            // check max radius
            Location plotCenter = new Location(chunk.getWorld(), chunk.getX()*16 + 8, 0, chunk.getZ()*16 + 8);
            Location fixedSpawn = city.getSpawn().clone();
            fixedSpawn.setY(0);
            if(city.getSpawn().distance(plotCenter) > city.getMaxRadius()) {
                throw new CommandException("Deine Stadt darf nur im Umkreis von " + city.getMaxRadius() + " Blöcken um den Spawn claimen!");
            }

            Plot plot = new DatabasePlot(plotCenter, city);

            // create schematic
            try {
                plugin.getSchematicManager().createSchematic(plot);
            } catch (RaidCraftException e) {
                throw new CommandException(e.getMessage());
            }

            // withdraw plot credit
            city.setPlotCredit(city.getPlotCredit() - 1);

            player.sendMessage(ChatColor.GREEN + "Der Plot wurde erfolgreich geclaimt! (Restliches Guthaben: " + city.getPlotCredit() + " Plots)");
        }

        @Command(
                aliases = {"unclaim"},
                desc = "Unclaims a plot",
                flags = "r"
        )
        @CommandPermissions("rccities.plot.unclaim")
        public void unclaim(CommandContext args, CommandSender sender) throws CommandException {

            if(sender instanceof ConsoleCommandSender) throw new CommandException("Player required!");
            Player player = (Player)sender;

            boolean restoreSchematics= false;
            if(args.hasFlag('r')) {
                restoreSchematics = true;
            }

            Plot plot = plugin.getPlotManager().getPlot(player.getLocation().getChunk());
            if(plot == null) {
                throw new CommandException("Hier befindet sich kein Chunk zum unclaimen!");
            }

            try {
                if(restoreSchematics) {
                    sender.sendMessage(ChatColor.DARK_RED + "Bei der Löschung des Plots wird die Landschaft zurückgesetzt!");
                }
                else {
                    sender.sendMessage(ChatColor.DARK_RED + "Bei der Löschung des Plots wird die Landschaft NICHT zurückgesetzt!");
                }
                new QueuedCaptchaCommand(sender, this, "unclaimPlot", sender, plot, restoreSchematics);
            } catch (NoSuchMethodException e) {
                throw new CommandException(e.getMessage());
            }
        }

        /*
         ***********************************************************************************************************************************
         */

        public void unclaimPlot(CommandSender sender, Plot plot, boolean restoreSchematics) {

            if(restoreSchematics) {
                try {
                    plugin.getSchematicManager().restorePlot(plot);
                } catch (RaidCraftException e) {
                    sender.sendMessage(ChatColor.RED + "Es ist ein Fehler beim wiederherstellen des Plots aufgetreten! (" + e.getMessage() + ")");
                }
            }

            plot.delete();
            Bukkit.broadcastMessage(ChatColor.GOLD + "Der Plot '" + plot.getRegionName() + "' wurde gelöscht!");
        }
    }

}
