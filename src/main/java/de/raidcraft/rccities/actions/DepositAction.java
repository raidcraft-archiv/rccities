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
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * @author Philip Urban
 */
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

        if (amount <= 0) {
            Conversations.changeStage(player, config.getString("onfailure"));
            return;
        }

        if (!economy.hasEnough(player.getUniqueId(), amount)) {
            player.sendMessage(ChatColor.RED + "Du hast nicht genug Geld um " + economy.getFormattedAmount(amount) + " in die Stadtkasse einzuzahlen.");
            Conversations.changeStage(player, config.getString("onfailure"));
            return;
        }

        economy.add(AccountType.CITY, city.getBankAccountName(), amount,
                BalanceSource.GUILD, "Einzahlung von " + player.getName());
        economy.substract(player.getUniqueId(), amount,
                BalanceSource.GUILD, "Einzahlung in Gildenkasse");

        player.sendMessage(ChatColor.GREEN + "Du hast " + economy.getFormattedAmount(amount) + ChatColor.GREEN + " in die Stadtkasse eingezahlt!");
        RaidCraft.getComponent(RCCitiesPlugin.class).getResidentManager().broadcastCityMessage(city, player.getName()
                + " hat " + economy.getFormattedAmount(amount) + ChatColor.GOLD + " in die Stadtkasse eingezahlt!");

        Conversations.changeStage(player, config.getString("onsuccess"));
    }
}
