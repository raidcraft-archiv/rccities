package de.raidcraft.rccities.actions;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.action.action.Action;
import de.raidcraft.api.config.builder.ConfigGenerator;
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
public class WithdrawAction implements Action<Player> {

    @Override
    @ConfigGenerator.Information(
            value = "city.withdraw",
            desc = "Withdraws money from the citys account into the players account.",
            aliases = "CITY_WITHDRAW",
            conf = {
                    "city: to withdraw money from",
                    "amount: to withdraw"
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

        if (!economy.hasEnough(AccountType.CITY, city.getBankAccountName(), amount)) {
            player.sendMessage(ChatColor.RED + "Die Stadt hat nicht genug Geld um " + economy.getFormattedAmount(amount) + " aus der Stadtkasse abzuheben.");
            Conversations.changeStage(player, config.getString("onfailure"));
            return;
        }

        economy.substract(AccountType.CITY, city.getBankAccountName(), amount,
                BalanceSource.GUILD, "Auszahlung an " + player.getName());
        economy.add(player.getUniqueId(), amount,
                BalanceSource.GUILD, "Auszahlung aus Gildenkasse");

        player.sendMessage(ChatColor.GREEN + "Du hast " + economy.getFormattedAmount(amount) + ChatColor.GREEN + " aus der Stadtkasse abgehoben!");
        RaidCraft.getComponent(RCCitiesPlugin.class).getResidentManager().broadcastCityMessage(city, player.getName()
                + " hat " + economy.getFormattedAmount(amount) + ChatColor.GOLD + " aus der Stadtkasse abgehoben!");

        Conversations.changeStage(player, config.getString("onsuccess"));
    }
}
