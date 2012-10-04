package de.strasse36.rccities.listeners;

import com.silthus.raidcraft.util.RCMessaging;
import com.silthus.raidcraft.util.SignUtils;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.strasse36.rccities.City;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.bukkit.RCCitiesPlugin;
import de.strasse36.rccities.commands.ResidentCommands;
import de.strasse36.rccities.config.MainConfig;
import de.strasse36.rccities.database.CityTable;
import de.strasse36.rccities.database.RCCitiesDatabase;
import de.strasse36.rccities.util.ResidentUtil;
import de.strasse36.rccities.util.TableHandler;
import de.strasse36.rccities.util.WorldGuardManager;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Author: Philip Urban
 * Date: 26.02.12 - 20:15
 * Description:
 */
public class PlayerListener implements Listener
{
    @EventHandler( ignoreCancelled = true )
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if(event.getClickedBlock() == null) {
            return;
        }

        //check town signs
        if(event.getClickedBlock().getState() instanceof Sign) {
            Sign sign  = (Sign)event.getClickedBlock().getState();

            if(ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase(MainConfig.getRCCitiesSignTag())) {
                City city = RCCitiesDatabase.get().getTable(CityTable.class).getCity(ChatColor.stripColor(sign.getLine(1)));
                if(city == null) {
                    RCMessaging.warn(event.getPlayer(), "Die Stadt wurde nicht gefunden. Schild veraltet?");
                    return;
                }
                //invitation sign
                if(ChatColor.stripColor(sign.getLine(2)).equalsIgnoreCase(MainConfig.getSignTopicJoinTown())) {
                    sign.setLine(2, ChatColor.DARK_RED + MainConfig.getSignTopicJoinTown());
                    sign.update();
                    ResidentUtil.sentInvitation(event.getPlayer(), city);
                }

                //balance sign
                if(ChatColor.stripColor(sign.getLine(2)).equalsIgnoreCase(MainConfig.getSignTopicTownBank())) {
                    sign.setLine(3, ChatColor.GOLD
                            + String.valueOf(RCCitiesPlugin.get().getEconomy().getBalance(city.getBankAccount())));
                    sign.update();
                }
            }
        }

        //check world
        if(!event.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase(MainConfig.getCityWorld()))
            return;
        
        //check if location is region
        ApplicableRegionSet regionSet = WorldGuardManager.getLocalRegions(event.getClickedBlock().getLocation());
        if (regionSet.size() != 0)
            return;

        if (event.getPlayer().hasPermission("rccities.build.place"))
            return;

        //prevent lava and water placement
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if(event.getItem() != null && (event.getItem().getType() == Material.WATER_BUCKET || event.getItem().getType() == Material.LAVA_BUCKET)) {
                event.setCancelled(true);
                RCMessaging.warn(event.getPlayer(), "Du hast hier keine Baurechte!");
            }
        }
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Resident resident = TableHandler.get().getResidentTable().getResident(event.getPlayer().getName());
        if(resident != null && resident.getCity() != null && resident.isMayor() && !event.getPlayer().hasPermission("rccities.skill.mayor"))
        {
            resident.setProfession("vicemayor");
            TableHandler.get().getResidentTable().updateResident(resident);
            RCMessaging.send(event.getPlayer(), RCMessaging.blue("Du bist Bürgermeister ohne den passenden Skill!"));
            RCMessaging.send(event.getPlayer(), RCMessaging.blue("Deshalb wurdest du nun zum Vize-Bürgermeister dekradiert!"));
        }
    }

    @EventHandler( ignoreCancelled = true )
    public void onMove(PlayerMoveEvent event) {
        //track only large moves
        if(event.getFrom().getBlockX() == event.getTo().getBlockX()
                && event.getFrom().getBlockY() == event.getTo().getBlockY()
                && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }

        if(!ResidentCommands.isWarmup(event.getPlayer())) {
            return;
        }

        ResidentCommands.cancelWarmup(event.getPlayer());
        RCMessaging.send(event.getPlayer(), RCMessaging.blue("Town Spawn Teleport abgebrochen!"), false);
    }

    @EventHandler(ignoreCancelled = true)
    public void onSignChange(SignChangeEvent event) {
        if(!ChatColor.stripColor(event.getLine(0)).equalsIgnoreCase(MainConfig.getRCCitiesSignTag())){
            return;
        }
        
        String topic = ChatColor.stripColor(event.getLine(2));

        //join town sign
        if(topic.equalsIgnoreCase(MainConfig.getSignTopicJoinTown())) {
            if(!event.getPlayer().hasPermission("rccities.sign.jointown.build")) {
                RCMessaging.noPermission(event.getPlayer());
                event.setCancelled(true);
                return;
            }

            Resident resident = TableHandler.get().getResidentTable().getResident(event.getPlayer().getName());
            
            if(resident == null || !resident.isMayor()) {
                RCMessaging.warn(event.getPlayer(), "Dieses Schild können nur Bürgermeister aufstellen!");
                event.setCancelled(true);
                return;
            }

            event.setLine(0, ChatColor.GREEN + MainConfig.getRCCitiesSignTag());
            event.setLine(1, resident.getCity().getName());
            event.setLine(2, ChatColor.DARK_RED + MainConfig.getSignTopicJoinTown());
            event.setLine(3, ChatColor.GRAY + "(klick mich)");
        }

        //balance sign
        if(topic.equalsIgnoreCase(MainConfig.getSignTopicTownBank())) {
            if(!event.getPlayer().hasPermission("rccities.sign.townbank.build")) {
                RCMessaging.noPermission(event.getPlayer());
                event.setCancelled(true);
                return;
            }

            Resident resident = TableHandler.get().getResidentTable().getResident(event.getPlayer().getName());

            if(resident == null || !resident.isMayor()) {
                RCMessaging.warn(event.getPlayer(), "Dieses Schild können nur Bürgermeister aufstellen!");
                event.setCancelled(true);
                return;
            }

            event.setLine(0, ChatColor.GREEN + MainConfig.getRCCitiesSignTag());
            event.setLine(1, resident.getCity().getName());
            event.setLine(2, ChatColor.DARK_RED + MainConfig.getSignTopicTownBank());
            event.setLine(3, ChatColor.DARK_BLUE
                    + String.valueOf(RCCitiesPlugin.get().getEconomy().getBalance(resident.getCity().getBankAccount())));
        }
    }
}
