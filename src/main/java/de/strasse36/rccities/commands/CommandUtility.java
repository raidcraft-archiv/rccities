package de.strasse36.rccities.commands;

import com.silthus.raidcraft.util.RCLogger;
import com.silthus.raidcraft.util.RCMessaging;
import org.bukkit.command.CommandSender;

/**
 * Author: Philip Urban
 * Date: 26.02.12 - 20:14
 * Description:
 */
public class CommandUtility
{

    //TODO command help
    //shows command help in chat
    public static void commandHelp(CommandSender sender)
    {


    }

    //shows no player found message
    public static void noPlayerFound(CommandSender sender)
    {
        RCMessaging.warn(sender, "Der Spieler wurde nicht gefunden.");
    }

    //shows no city found message
    public static void noCityFound(CommandSender sender)
    {
        RCMessaging.warn(sender, "Die Stadt wurde nicht gefunden.");
    }

    //no resident
    public static void noResident(CommandSender sender)
    {
        RCMessaging.warn(sender, "Du bist kein Einwohner!");
    }

    //no mayor
    public static void noMayor(CommandSender sender)
    {
        RCMessaging.warn(sender, "Du bist kein Bürgermeister!");
    }

    //no permissions
    public static void onlyStaff(CommandSender sender)
    {
        RCMessaging.warn(sender, "Nur Mitglieder der Stadtverwaltung dürfen das!");
    }

    //internal error
    public static void internalError(CommandSender sender)
    {
        RCMessaging.warn(sender, "Ein interner Fehler ist aufgetreten!");
        RCLogger.warning("RCCities: Fehler aufgetreten!");
    }
}
