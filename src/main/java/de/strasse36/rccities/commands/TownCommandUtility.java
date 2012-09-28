package de.strasse36.rccities.commands;

import com.silthus.raidcraft.util.RCLogger;
import com.silthus.raidcraft.util.RCMessaging;
import org.bukkit.command.CommandSender;

/**
 * Author: Philip Urban
 * Date: 26.02.12 - 20:14
 * Description:
 */
public class TownCommandUtility
{
    public static void help(CommandSender sender)
    {
        RCMessaging.send(sender, RCMessaging.green("-----------------------------------------------------"), false);
        RCMessaging.send(sender, RCMessaging.green("RCCities: Verfügbare '/town' Hauptparameter:"), false);
        RCMessaging.send(sender, RCMessaging.yellow("list, spawn, setspawn, promote, kick, invite, setdesc, greetings, withdraw, pvp, create, demolish, setname"), false);
        RCMessaging.send(sender, RCMessaging.green("Die Beschreibung der einzelnen Parameter findest Du unter:"), false);
        //TODO update wiki link
        RCMessaging.send(sender, "http://raid-craft.de/commands", false);
        RCMessaging.send(sender, RCMessaging.green("-----------------------------------------------------"), false);
    }

    //no player found message
    public static void noPlayerFound(CommandSender sender)
    {
        RCMessaging.warn(sender, "Der Spieler wurde nicht gefunden.");
    }

    //no city found message
    public static void noCityFound(CommandSender sender)
    {
        RCMessaging.warn(sender, "Die Stadt wurde nicht gefunden.");
    }

    //no resident found
    public static void selectNoResident(CommandSender sender)
    {
        RCMessaging.warn(sender, "Der gewählte Spieler ist kein Einwohner!");
    }

    //player is no resident in any town
    public static void selectNoResidentAny(CommandSender sender)
    {
        RCMessaging.warn(sender, "Der gewählte Spieler muss zumindest in einer Stadt Einwohner sein!");
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

    //no leadership
    public static void noLeadership(CommandSender sender)
    {
        RCMessaging.warn(sender, "Du gehörst nicht zur Stadtleitung!");
    }

    //no permissions
    public static void onlyStaff(CommandSender sender)
    {
        RCMessaging.warn(sender, "Nur Mitglieder der Stadtverwaltung dürfen das!");
    }

    //self action
    public static void selfAction(CommandSender sender)
    {
        RCMessaging.warn(sender, "Du kannst diese Aktion nicht auf dich selbst anwenden!");
    }

    //internal error
    public static void internalError(CommandSender sender)
    {
        RCMessaging.warn(sender, "Ein interner Fehler ist aufgetreten!");
        RCLogger.warning("RCCities: Fehler aufgetreten!");
    }

    //wrong world
    public static void wrongWorld(CommandSender sender)
    {
        RCMessaging.warn(sender, "Du befindest Dich auf der falschen Welt!");
    }

    //wrong amount
    public static void wrongAmount(CommandSender sender)
    {
        RCMessaging.warn(sender, "Der eingegebene Betrag war keine positive Zahl!");
    }

    public static void lessAmount(CommandSender sender)
    {
        RCMessaging.warn(sender, "Der Betrag muss mindestens 0.01c betragen!");
    }

    public static void noMayorSkill(CommandSender sender)
    {
        RCMessaging.warn(sender, "Der Gewählte Spieler hat keinen Bürgermeister Skill!");
    }

    public static void futureMayorNotOnline(CommandSender sender)
    {
        RCMessaging.warn(sender, "Der zukünftigte Bürgermeister muss bei seiener Erennung Online sein!");
    }

    public static void selectedAlreadyResident(CommandSender sender)
    {
        RCMessaging.warn(sender, "Der Gewählte Spieler ist bereits Einwohner in dieser Stadt!");
    }

}
