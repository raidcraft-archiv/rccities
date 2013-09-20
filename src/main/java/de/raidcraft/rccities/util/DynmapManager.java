package de.raidcraft.rccities.util;

import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.api.city.City;
import org.bukkit.Bukkit;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

/**
 * Author: Philip
 * Date: 12.12.12 - 22:16
 * Description:
 */
public class DynmapManager {

    private DynmapAPI api;
    private MarkerAPI markerAPI = null;
    private MarkerSet farmsSet = null;

    public DynmapManager() {

        api = (DynmapAPI) Bukkit.getServer().getPluginManager().getPlugin("dynmap");
        if(api == null) {
            return;
        }
        markerAPI = api.getMarkerAPI();
        farmsSet = markerAPI.getMarkerSet("spielerstaedte");
    }

    public void addCityMarker(City city) {


        if (markerAPI == null || farmsSet == null) {
            RaidCraft.LOGGER.warning("Dynmap not installed or 'spielerstaedte' marker set not available!");
            return;
        }

        removeMarker(city);

        farmsSet.createMarker(city.getName().toLowerCase().replace(" ", "_")
                , city.getFriendlyName()
                , city.getSpawn().getWorld().getName()
                , city.getSpawn().getBlockX()
                , city.getSpawn().getBlockY()
                , city.getSpawn().getBlockZ()
                , markerAPI.getMarkerIcon("bighouse")
                , true);
    }

    public void removeMarker(City city) {

        if (farmsSet == null) {
            return;
        }
        for (Marker marker : farmsSet.getMarkers()) {
            if (marker.getLabel().equalsIgnoreCase(city.getFriendlyName())) {
                marker.deleteMarker();
            }
        }
    }
}
