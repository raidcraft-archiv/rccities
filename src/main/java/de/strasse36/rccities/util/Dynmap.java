//package de.strasse36.rccities.util;
//
//import com.silthus.raidcraft.util.RCLogger;
//import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
//import org.bukkit.plugin.Plugin;
//import org.bukkit.plugin.PluginManager;
//import org.dynmap.DynmapAPI;
//import org.dynmap.markers.MarkerAPI;
//import org.dynmap.markers.MarkerSet;
//
//import static org.bukkit.Bukkit.getServer;
//
///**
// * Author: Philip Urban
// * Date: 24.03.12 - 18:11
// * Description:
// */
//public class Dynmap {
//    private static Dynmap _self;
//    Plugin dynmap;
//    DynmapAPI api;
//    MarkerAPI markerapi;
//    WorldGuardPlugin wg;
//    MarkerSet set;
//
//    public static void init()
//    {
//        if(_self == null)
//            _self = new Dynmap();
//    }
//
//    private Dynmap(){
//        PluginManager pm = getServer().getPluginManager();
//        /* Get dynmap */
//        dynmap = pm.getPlugin("dynmap");
//        if(dynmap == null) {
//            RCLogger.warning("Cannot find dynmap!");
//            return;
//        }
//        api = (DynmapAPI)dynmap; /* Get API */
//        /* Get WorldGuard */
//        Plugin p = pm.getPlugin("WorldGuard");
//        if(p == null) {
//            RCLogger.warning("Cannot find WorldGuard!");
//            return;
//        }
//        wg = (WorldGuardPlugin)p;
//
//        /* Now, get markers API */
//        markerapi = api.getMarkerAPI();
//        if(markerapi == null) {
//            RCLogger.warning("Error loading dynmap marker API!");
//            return;
//        }
//
//        /* Now, add marker set for mobs (make it transient) */
//        set = markerapi.getMarkerSet("rccities.markerset");
//        if(set == null)
//            set = markerapi.createMarkerSet("rccities.markerset", "RCCities", null, false);
//        else
//            set.setMarkerSetLabel("RCCities");
//        if(set == null) {
//            RCLogger.warning("Error creating marker set");
//            return;
//        }
//    }
//
//
//}
