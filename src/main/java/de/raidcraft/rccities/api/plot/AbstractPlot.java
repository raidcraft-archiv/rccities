package de.raidcraft.rccities.api.plot;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import org.bukkit.Chunk;
import org.bukkit.Location;

/**
 * @author Philip Urban
 */
public abstract class AbstractPlot implements Plot {

    private int id;
    private Location location;
    private ProtectedRegion region;
    private City city;

    protected AbstractPlot() {}

    protected AbstractPlot(Location location, City city) {

        Location simpleLocation = new Location(location.getWorld(), location.getChunk().getX()*16 + 8, 0, location.getChunk().getZ()*16 + 8);
        this.location = simpleLocation;
        this.city = city;

        save();
        createRegion();
    }

    protected void setId(int id) {

        this.id = id;
    }

    protected void setLocation(Location location) {

        this.location = location;
    }

    protected void setRegion(ProtectedRegion region) {

        this.region = region;
    }

    protected void setCity(City city) {

        this.city = city;
    }

    @Override
    public final int getId() {

        return id;
    }

    @Override
    public final String getRegionName() {

        return city.getName() + "_" + getId();
    }

    @Override
    public final Location getLocation() {

        return location;
    }

    @Override
    public final ProtectedRegion getRegion() {

        return region;
    }

    @Override
    public final City getCity() {

        return city;
    }

    @Override
    public final void createRegion() {

        RegionManager regionManager = RaidCraft.getComponent(RCCitiesPlugin.class).getWorldGuard().getRegionManager(location.getWorld());
        if(regionManager.getRegion(getRegionName()) != null) {
            regionManager.removeRegion(getRegionName());
        }

        Chunk chunk = location.getChunk();
        BlockVector vector1 = new BlockVector(
                chunk.getX()*16,
                0,
                chunk.getZ()*16
        );
        BlockVector vector2 = new BlockVector(
                (chunk.getX()*16)+15,
                location.getWorld().getMaxHeight(),
                (chunk.getZ()*16)+15
        );

        ProtectedCuboidRegion protectedCuboidRegion = new ProtectedCuboidRegion(getRegionName(), vector1, vector2);
        regionManager.addRegion(protectedCuboidRegion);
        region = protectedCuboidRegion;
    }

    @Override
    public void delete() {

        RaidCraft.getComponent(RCCitiesPlugin.class).getWorldGuard().getRegionManager(location.getWorld()).removeRegion(getRegionName());
    }
}
