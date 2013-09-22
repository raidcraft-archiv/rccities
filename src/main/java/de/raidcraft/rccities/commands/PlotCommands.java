package de.raidcraft.rccities.commands;

import com.sk89q.minecraft.util.commands.*;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.plot.Plot;
import org.bukkit.Chunk;
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

            // check max radius

        }

        @Command(
                aliases = {"unclaim"},
                desc = "Unclaims a plot"
        )
        @CommandPermissions("rccities.plot.unclaim")
        public void unclaim(CommandContext args, CommandSender sender) throws CommandException {

            //TODO
        }
    }

}
