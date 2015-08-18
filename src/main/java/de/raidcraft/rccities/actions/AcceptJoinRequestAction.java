package de.raidcraft.rccities.actions;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.action.action.Action;
import de.raidcraft.api.conversations.Conversations;
import de.raidcraft.api.conversations.conversation.ConversationEndReason;
import de.raidcraft.api.conversations.conversation.ConversationVariable;
import de.raidcraft.api.economy.AccountType;
import de.raidcraft.api.economy.BalanceSource;
import de.raidcraft.api.economy.Economy;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.flags.CityFlag;
import de.raidcraft.rccities.api.request.JoinRequest;
import de.raidcraft.rccities.api.resident.RolePermission;
import de.raidcraft.rccities.flags.city.JoinCostsCityFlag;
import de.raidcraft.util.UUIDUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * @author Philip Urban
 */
public class AcceptJoinRequestAction implements Action<Player> {

    @Override
    @Information(
            value = "city.join-request.accept",
            desc = "Accepts the join request of the given player.",
            aliases = "ACCEPT_CITY_JOIN_REQUEST",
            conf = {
                    "city: with the join request",
                    "player: to accept join request for"
            }
    )
    public void accept(Player player, ConfigurationSection config) {

        RCCitiesPlugin plugin = RaidCraft.getComponent(RCCitiesPlugin.class);

        String cityName = ConversationVariable.getString(player, "city").orElse(config.getString("city"));
        String candidate = ConversationVariable.getString(player, "player").orElse(config.getString("player"));

        City city = plugin.getCityManager().getCity(cityName);
        if (city == null) {
            player.sendMessage(ChatColor.RED + "Ungültige Stadt ausgewählt!");
            Conversations.endActiveConversation(player, ConversationEndReason.ERROR);
            return;
        }

        JoinRequest joinRequest = city.getJoinRequest(UUIDUtil.convertPlayer(candidate));
        if (joinRequest == null) return;

        Economy economy = RaidCraft.getEconomy();
        double joinCosts = plugin.getCityManager().getServerJoinCosts(city);
        if (!economy.hasEnough(AccountType.CITY, city.getBankAccountName(), joinCosts)) {
            plugin.getResidentManager()
                    .broadcastCityMessage(city, "Die Gilde hat nicht genug Geld um neue Mitglieder anzunehmen! (" + economy.getFormattedAmount(joinCosts) + ChatColor.GOLD + " benötigt)", RolePermission.STAFF);
            return;
        }

        CityFlag joinCostsCityFlag = plugin.getFlagManager().getCityFlag(city, JoinCostsCityFlag.class);
        if (joinCostsCityFlag != null) {
            double customJoinCosts = joinCostsCityFlag.getType().convertToMoney(joinCostsCityFlag.getValue());
            economy.add(AccountType.CITY, city.getBankAccountName(), customJoinCosts, BalanceSource.GUILD, "Beitrittsbeitrag von " + candidate);
            economy.substract(player.getUniqueId(), customJoinCosts, BalanceSource.GUILD, "Beitrittskosten von " + city.getFriendlyName());
        }
        economy.substract(AccountType.CITY, city.getBankAccountName(), joinCosts, BalanceSource.GUILD, "Beitrittssteuern von " + candidate);

        joinRequest.accept();
    }
}
