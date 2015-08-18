package de.raidcraft.rccities.actions;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.action.action.Action;
import de.raidcraft.api.conversations.Conversations;
import de.raidcraft.api.conversations.conversation.ConversationEndReason;
import de.raidcraft.api.conversations.conversation.ConversationVariable;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.request.JoinRequest;
import de.raidcraft.rccities.api.resident.RolePermission;
import de.raidcraft.util.UUIDUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * @author Philip Urban
 */
public class RejectJoinRequestAction implements Action<Player> {

    @Override
    @Information(
            value = "city.join-request.reject",
            desc = "Rejects the join request of the given player with the given reason.",
            aliases = "REJECT_CITY_JOIN_REQUEST",
            conf = {
                    "player: to accept join request for",
                    "reason: to reject request with",
                    "city: with the join request"
            }
    )
    public void accept(Player player, ConfigurationSection config) {

        RCCitiesPlugin plugin = RaidCraft.getComponent(RCCitiesPlugin.class);

        String cityName = ConversationVariable.getString(player, "city").orElse(config.getString("city"));
        String candidate = ConversationVariable.getString(player, "join_request").orElse(config.getString("player"));

        City city = plugin.getCityManager().getCity(cityName);
        if (city == null) {
            player.sendMessage(ChatColor.RED + "Ungültige Stadt ausgewählt!");
            Conversations.endActiveConversation(player, ConversationEndReason.ERROR);
            return;
        }

        JoinRequest joinRequest = city.getJoinRequest(UUIDUtil.convertPlayer(candidate));
        if (joinRequest == null) return;

        String reason = ConversationVariable.getString(player, "join_request_reject_reason").orElse(config.getString("reason"));
        joinRequest.reject(reason);

        RaidCraft.getComponent(RCCitiesPlugin.class).getResidentManager()
                .broadcastCityMessage(city, "Der Beitrittsantrag von " + joinRequest.getPlayer() + " wurde abgelehnt! (" + reason + ")", RolePermission.STAFF);
        Player targetPlayer = Bukkit.getPlayer(joinRequest.getPlayer());
        if (targetPlayer == null) return;
        targetPlayer.sendMessage(ChatColor.DARK_RED + "Dein Mitgliedsantrag bei '" + ChatColor.GOLD + joinRequest.getCity().getFriendlyName() + ChatColor.DARK_RED + "' wurde abgelehnt!");
    }
}
