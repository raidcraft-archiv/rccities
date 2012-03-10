package de.strasse36.rccities.commands;

import com.silthus.raidcraft.util.RCMessaging;
import org.bukkit.command.CommandSender;

/**
 * Author: Philip Urban
 * Date: 08.03.12 - 14:05
 * Description:
 */
public class PlotCommandUtility {

    public static void successfullyClaimed(CommandSender sender)
    {
        RCMessaging.send(sender, RCMessaging.blue("Der Chunk wurde erfolgreich claimed!"));
    }

    public static void successfullyUnclaimed(CommandSender sender)
    {
        RCMessaging.send(sender, RCMessaging.blue("Der Chunk wurde erfolgreich unclaimed!"));
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

}
