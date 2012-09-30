package de.strasse36.rccities.util;

import com.silthus.raidcraft.util.RCMessaging;
import de.strasse36.rccities.City;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.exceptions.UnknownProfessionException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Author: Philip Urban
 * Date: 29.02.12 - 00:03
 * Description:
 */
public class Profession {

    public static void setMayor(Player player, City city) {
        setMayor(player.getName(), city);
    }

    public static void setMayor(String player, City city)
    {
        Resident resident = new Resident(player, city, "mayor");
        PermissionsManager.addGroup(resident.getName(), city.getName());
        TableHandler.get().getResidentTable().updateResident(resident);
        Player bukkitPlayer = Bukkit.getPlayer(player);
        if(bukkitPlayer != null && bukkitPlayer.isOnline())
            RCMessaging.send(bukkitPlayer, RCMessaging.blue("Du bist nun der Bürgermeister von " + city.getName() + "!"), false);
    }

    public static void changeProfession(Resident resident, String profession) throws UnknownProfessionException
    {
        if( profession.equalsIgnoreCase("mayor") ||
                profession.equalsIgnoreCase("vicemayor") ||
                profession.equalsIgnoreCase("assistant") ||
                profession.equalsIgnoreCase("resident")
                )
        {
            resident.setProfession(profession.toLowerCase());
            TableHandler.get().getResidentTable().updateResident(resident);
        }
        else
            throw new UnknownProfessionException("Die gewählte Berufsgruppe existiert nicht!");
    }

    public static String translateProfession(String profession)
    {
        if(profession.equalsIgnoreCase("mayor"))
            return "Bürgermeister";
        if(profession.equalsIgnoreCase("vicemayor"))
            return "Vize-Bürgermeister";
        if(profession.equalsIgnoreCase("assistant"))
            return "Stadtassistent";
        if(profession.equalsIgnoreCase("resident"))
            return "Bürger";
        return profession;
    }
}
