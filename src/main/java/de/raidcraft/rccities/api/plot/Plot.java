package de.raidcraft.rccities.api.plot;

import com.sk89q.worldedit.regions.Region;
import de.raidcraft.rccities.api.resident.Resident;

import java.util.Set;

/**
 * @author Philip Urban
 */
public interface Plot {

    public Region getRegion();

    public Set<Resident> getOwner();
}
