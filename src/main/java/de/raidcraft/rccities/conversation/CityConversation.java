package de.raidcraft.rccities.conversation;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.conversations.conversation.ConversationTemplate;
import de.raidcraft.api.conversations.host.ConversationHost;
import de.raidcraft.api.economy.AccountType;
import de.raidcraft.api.economy.Economy;
import de.raidcraft.conversations.conversations.PlayerConversation;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.flags.CityFlag;
import de.raidcraft.rccities.api.plot.Plot;
import de.raidcraft.rccities.api.request.JoinRequest;
import de.raidcraft.rccities.flags.city.JoinCostsCityFlag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;

@Data
@EqualsAndHashCode(callSuper = true)
public class CityConversation extends PlayerConversation {

    private final City city;

    public CityConversation(Player player, ConversationTemplate conversationTemplate, ConversationHost conversationHost) {

        super(player, conversationTemplate, conversationHost);

        Plot plot = RaidCraft.getComponent(RCCitiesPlugin.class).getPlotManager().getPlot(player.getLocation().getChunk());
        if (plot == null) {
            city = null;
            return;
        }
        city = plot.getCity();
        Economy economy = RaidCraft.getEconomy();

        set("city_friendlyname", city.getFriendlyName());
        set("city_name", city.getName());
        set("city_money", economy.getBalance(AccountType.CITY, city.getBankAccountName()));
        set("city_money_formatted", economy.getFormattedAmount(economy.getBalance(AccountType.CITY, city.getBankAccountName())));
        set("city_bankaccount", city.getBankAccountName());

        int openRequests = 0;
        for (JoinRequest joinRequest : city.getJoinRequests()) {
            if (!joinRequest.isRejected()) openRequests++;
        }
        set("city_open_requests", openRequests);

        double joinCosts = 0;
        CityFlag joinCostsCityFlag = RaidCraft.getComponent(RCCitiesPlugin.class).getFlagManager().getCityFlag(city, JoinCostsCityFlag.class);
        if (joinCostsCityFlag != null) {
            joinCosts = joinCostsCityFlag.getType().convertToMoney(joinCostsCityFlag.getValue());
        }
        set("city_join_costs_friendly", economy.getFormattedAmount(joinCosts));
        set("city_join_costs", joinCosts);
    }
}
