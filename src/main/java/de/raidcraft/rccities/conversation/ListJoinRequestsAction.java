package de.raidcraft.rccities.conversation;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.RaidCraftException;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.request.JoinRequest;
import de.raidcraft.rcconversations.actions.common.StageAction;
import de.raidcraft.rcconversations.actions.player.GiveTargetCompassAction;
import de.raidcraft.rcconversations.api.action.AbstractAction;
import de.raidcraft.rcconversations.api.action.ActionArgumentList;
import de.raidcraft.rcconversations.api.action.ActionInformation;
import de.raidcraft.rcconversations.api.action.WrongArgumentValueException;
import de.raidcraft.rcconversations.api.answer.Answer;
import de.raidcraft.rcconversations.api.answer.SimpleAnswer;
import de.raidcraft.rcconversations.api.conversation.Conversation;
import de.raidcraft.rcconversations.api.stage.SimpleStage;
import de.raidcraft.rcconversations.api.stage.Stage;
import de.raidcraft.rcconversations.util.ParseString;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Philip Urban
 */
@ActionInformation(name="LIST_CITY_JOIN_REQUESTS")
public class ListJoinRequestsAction extends AbstractAction {

    private static final int MAX_PLACES_PER_STAGE = 4;

    @Override
    public void run(Conversation conversation, ActionArgumentList args) throws RaidCraftException {

        String cityName = args.getString("city");
        cityName = ParseString.INST.parse(conversation, cityName);
        String text = args.getString("text");
        text = ParseString.INST.parse(conversation, text);
        String nextStage = args.getString("next-stage", "next");
        int pageSize = args.getInt("pagesize", MAX_PLACES_PER_STAGE);

        City city = RaidCraft.getComponent(RCCitiesPlugin.class).getCityManager().getCity(cityName);
        if(city == null) {
            throw new WrongArgumentValueException("Wrong argument value in action '" + getName() + "': City '" + cityName + "' does not exist!");
        }

        Player player = conversation.getPlayer();
        Stage currentStage = conversation.getCurrentStage();

        List<JoinRequest> joinRequestList = city.getJoinRequests();
        String entranceStage = "join_requests_";


        int pages = (int) (((double) joinRequestList.size() / (double) pageSize) + 0.5);
        if(pages == 0) pages = 1;
        for (int i = 0; i < pages; i++) {

            Stage stage;
            List<Answer> answers = new ArrayList<>();

            int a;

            for (a = 0; a < pageSize; a++) {
                if (joinRequestList.size() <= a + (i * pageSize)) break;
                answers.add(createStationAnswer(conversation.getPlayer(), a, joinRequestList.get(i*pageSize + a), nextStage));
            }
            a++;

            String nextDynamicStage;
            if (pages - 1 == i) {
                nextDynamicStage = entranceStage;
            }
            else {
                nextDynamicStage = entranceStage + "_" + (i + 1);
            }
            String thisStage;
            if(i == 0) {
                thisStage = entranceStage;
            }
            else {
                thisStage = entranceStage + "_" + i;
            }

            if(pages > 1) {
                answers.add(new SimpleAnswer(String.valueOf(a), "&7Nächste Seite", new ActionArgumentList(String.valueOf(a), StageAction.class, "stage", nextDynamicStage)));
            }
            stage = new SimpleStage(thisStage, text + "|&7(Seite " + (i+1) + "/" + pages + ")", answers);

            conversation.addStage(stage);
        }
        conversation.setCurrentStage(entranceStage);
        conversation.triggerCurrentStage();
    }

    private Answer createStationAnswer(Player player, int number, JoinRequest joinRequest, String nextStage) {

        List<ActionArgumentList> actions = new ArrayList<>();
        int i = 0;
        Map<String, Object> data = new HashMap<>();
        data.put("city", joinRequest.getCity().getName());
        data.put("candidate", joinRequest.getPlayer());
        actions.add(new ActionArgumentList(String.valueOf(i++), EditJoinRequestAction.class, data));
        actions.add(new ActionArgumentList(String.valueOf(i++), StageAction.class, "stage", nextStage));

        String crossed = (joinRequest.isRejected()) ? ChatColor.RED + ChatColor.STRIKETHROUGH.toString() : ChatColor.GREEN.toString();
        return new SimpleAnswer(String.valueOf(number + 1), crossed + joinRequest.getPlayer(), actions);
    }
}
