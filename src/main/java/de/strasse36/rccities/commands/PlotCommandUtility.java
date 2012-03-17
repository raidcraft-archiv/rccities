package de.strasse36.rccities.commands;

import com.silthus.raidcraft.util.RCMessaging;
import de.strasse36.rccities.config.MainConfig;
import org.bukkit.command.CommandSender;

/**
 * Author: Philip Urban
 * Date: 08.03.12 - 14:05
 * Description:
 */
public class PlotCommandUtility {

    public static void help(CommandSender sender)
    {
        RCMessaging.send(sender, RCMessaging.green("-----------------------------------------------------"), false);
        RCMessaging.send(sender, RCMessaging.green("RCCities: Verfügbare '/plot' Hauptparameter:"), false);
        RCMessaging.send(sender, RCMessaging.yellow("claim, unclaim, mark, give, take, buy, clear, pvp, public, list"), false);
        RCMessaging.send(sender, RCMessaging.green("Die Beschreibung der einzelnen Parameter findest Du unter:"), false);
        //TODO update wiki link
        RCMessaging.send(sender, "http://strasse36.raid-craft.de/rccities/wiki.php", false);
        RCMessaging.send(sender, RCMessaging.green("-----------------------------------------------------"), false);
    }

    public static void successfullyClaimed(CommandSender sender)
    {
        RCMessaging.send(sender, RCMessaging.blue("Der Chunk wurde erfolgreich claimed!"), false);
    }

    public static void successfullyUnclaimed(CommandSender sender)
    {
        RCMessaging.send(sender, RCMessaging.blue("Der Chunk wurde erfolgreich unclaimed!"), false);
    }

    public static void alreadyExist(CommandSender sender)
    {
        RCMessaging.warn(sender, "Interner Fehler: Ein Plot mit diesem Namen existiert bereits!");
    }

    public static void unclaimable(CommandSender sender)
    {
        RCMessaging.warn(sender, "Dieser Chunk kann nicht claimed werden! Bereits vergeben?");
    }

    public static void notEnoughSpace(CommandSender sender)
    {
        RCMessaging.warn(sender, "Das Chunk-Kontigent Deiner Stadt ist erreicht!");
        RCMessaging.warn(sender, "Kaufe mit '/plot buy <Anzahl>' neue Chunks dazu.");
    }

    public static void noplot(CommandSender sender)
    {
        RCMessaging.warn(sender, "Ein Plot mit diesem Namen gibt es nicht!");
    }

    public static void noCitychunk(CommandSender sender)
    {
        RCMessaging.warn(sender, "Dieser Chunk gehört nicht zu Deiner Stadt!");
    }

    public static void selectedNoRegionmember(CommandSender sender)
    {
        RCMessaging.warn(sender, "Der gewählte Einwohner ist nicht Besitzer dieses Plots!");
    }

    public static void notEnoughMoney(CommandSender sender)
    {
        RCMessaging.warn(sender, "Das claimen eines Chunks kostet " + MainConfig.getClaimPrice() + "c!");
        RCMessaging.warn(sender, "Überweise mit '/town deposit <Betrag>' Geld an die Stadt.");
    }

    public static void publicplot(CommandSender sender)
    {
        RCMessaging.warn(sender, "Dieser Plot kann nicht bearbeitet werden da er öffentlich ist!");
    }
}
