package de.raidcraft.rccities.flags.plot;

import com.sk89q.worldedit.blocks.BlockType;
import de.raidcraft.RaidCraft;
import de.raidcraft.api.RaidCraftException;
import de.raidcraft.api.economy.Economy;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.flags.AbstractPlotFlag;
import de.raidcraft.rccities.api.flags.FlagInformation;
import de.raidcraft.rccities.api.flags.FlagType;
import de.raidcraft.rccities.api.plot.Plot;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * @author Philip Urban
 */
@FlagInformation(
        name = "MARK",
        type = FlagType.BOOLEAN,
        cooldown = 5
)
public class MarkPlotFlag extends AbstractPlotFlag {

    public MarkPlotFlag(Plot plot) {

        super(plot);
    }

    @Override
    public void refresh() throws RaidCraftException {

        if(getPlot() == null) return;

        boolean currentValue = getType().convertToBoolean(getValue());
        String bankAccount = getPlot().getCity().getBankAccountName();
        double markCost = RaidCraft.getComponent(RCCitiesPlugin.class).getConfig().flagPlotMarkCost;

        if(currentValue) {

            Economy economy = RaidCraft.getEconomy();
            if(!economy.hasEnough(bankAccount, markCost)) {
                throw new RaidCraftException("Es ist nicht genug Geld in der Stadtkasse! (" + economy.getFormattedAmount(markCost) + " benötigt!");
            }

            // withdraw
            economy.substract(bankAccount, markCost);

            //set torches
            Chunk chunk = getPlot().getLocation().getChunk();
            ChunkSnapshot chunkSnapshot = chunk.getChunkSnapshot();
            Block block;
            int i;

            //EAST
            for(i = 0; i<16; i++)
            {
                block = chunk.getBlock(i, chunkSnapshot.getHighestBlockYAt(i, 0), 0);
                setTorch(block);
            }

            //WEST
            for(i = 0; i<16; i++)
            {
                block = chunk.getBlock(i, chunkSnapshot.getHighestBlockYAt(i, 15), 15);
                setTorch(block);
            }

            //NORTH
            for(i = 0; i<16; i++)
            {
                block = chunk.getBlock(0, chunkSnapshot.getHighestBlockYAt(0, i), i);
                setTorch(block);
            }

            //SOUTH
            for(i = 0; i<16; i++)
            {
                block = chunk.getBlock(15, chunkSnapshot.getHighestBlockYAt(15, i), i);
                setTorch(block);
            }
        }
        else {

            //remove torches
            Chunk chunk = getPlot().getLocation().getChunk();
            ChunkSnapshot chunkSnapshot = chunk.getChunkSnapshot();
            Block block;
            int i;

            //EAST
            for(i = 0; i<16; i++)
            {
                block = chunk.getBlock(i, chunkSnapshot.getHighestBlockYAt(i, 0), 0);
                removeTorch(block);
            }

            //WEST
            for(i = 0; i<16; i++)
            {
                block = chunk.getBlock(i, chunkSnapshot.getHighestBlockYAt(i, 15), 15);
                removeTorch(block);
            }

            //NORTH
            for(i = 0; i<16; i++)
            {
                block = chunk.getBlock(0, chunkSnapshot.getHighestBlockYAt(0, i), i);
                removeTorch(block);
            }

            //SOUTH
            for(i = 0; i<16; i++)
            {
                block = chunk.getBlock(15, chunkSnapshot.getHighestBlockYAt(15, i), i);
                removeTorch(block);
            }
        }
    }

    private void setTorch(Block block) {

        Block aboveBlock = block.getRelative(0, 1, 0);

        if(aboveBlock.getType() != Material.AIR) return;
        if(block.isLiquid() || BlockType.canPassThrough(block.getTypeId()) || BlockType.isTranslucent(block.getTypeId())) return;

        aboveBlock.setType(Material.TORCH);
    }

    private void removeTorch(Block block) {

        Block aboveBlock = block.getRelative(0, 1, 0);

        if(aboveBlock.getType() != Material.TORCH) return;

        aboveBlock.setType(Material.AIR);
    }
}
