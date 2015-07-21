package de.raidcraft.rccities.conversation;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.RaidCraftException;
import de.raidcraft.api.action.action.Action;
import de.raidcraft.api.conversations.Conversations;
import de.raidcraft.api.conversations.conversation.ConversationEndReason;
import de.raidcraft.api.conversations.conversation.ConversationVariable;
import de.raidcraft.api.economy.AccountType;
import de.raidcraft.api.economy.BalanceSource;
import de.raidcraft.api.economy.Economy;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rcconversations.api.action.AbstractAction;
import de.raidcraft.rcconversations.api.action.ActionArgumentList;
import de.raidcraft.rcconversations.api.action.ActionInformation;
import de.raidcraft.rcconversations.api.action.WrongArgumentValueException;
import de.raidcraft.rcconversations.api.conversation.Conversation;
import de.raidcraft.rcconversations.util.ParseString;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * @author Philip Urban
 */
@ActionInformation(name = "CITY_DEPOSIT")
public class DepositAction implements Action<Player> {

    @Override
    @Information(
            value = "city.deposit",
            desc = "Deposits money from the players account into the city.",
            aliases = "CITY_DEPOSIT",
            conf = {
                    "city: to deposit money into",
                    "amount: to deposit"
            }
    )
    public void accept(Player player, ConfigurationSection config) {

        Economy economy = RaidCraft.getEconomy();

        String cityName = ConversationVariable.getString(player, "city").orElse(config.getString("city"));
        double amount = economy.parseCurrencyInput(ConversationVariable.getString(player, "amount").orElse(config.getString("amount")));

        City city = RaidCraft.getComponent(RCCitiesPlugin.class).getCityManager().getCity(cityName);
        if (city == null) {
            player.sendMessage(ChatColor.RED + "Ungültige Stadt ausgewählt!");
            Conversations.endActiveConversation(player, ConversationEndReason.ERROR);
            return;
        }

        if (amount == 0) {
            changeStage(conversation, failure);
            return;
        }

        economy.add(AccountType.CITY, city.getBankAccountName(), amount,
                BalanceSource.GUILD, "Einzahlung von " + conversation.getPlayer().getName());
        economy.substract(conversation.getPlayer().getUniqueId(), amount,
                BalanceSource.GUILD, "Einzahlung in Gildenkasse");

        conversation.getPlayer().sendMessage(ChatColor.GREEN + "Du hast " + economy.getFormattedAmount(amount) + ChatColor.GREEN + " in die Stadtkasse eingezahlt!");
        RaidCraft.getComponent(RCCitiesPlugin.class).getResidentManager().broadcastCityMessage(city, conversation.getPlayer().getName()
                + " hat " + economy.getFormattedAmount(amount) + ChatColor.GOLD + " in die Stadtkasse eingezahlt!");

        changeStage(conversation, success);
    }

    private void changeStage(Conversation conversation, String stage) {

        if (stage != null) {
            conversation.setCurrentStage(stage);
            conversation.triggerCurrentStage();
        }
    }
}
