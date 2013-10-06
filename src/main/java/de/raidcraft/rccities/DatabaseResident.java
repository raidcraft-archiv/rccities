package de.raidcraft.rccities;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.player.UnknownPlayerException;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.plot.Plot;
import de.raidcraft.rccities.api.resident.AbstractResident;
import de.raidcraft.rccities.api.resident.Role;
import de.raidcraft.rccities.tables.TResident;
import de.raidcraft.skills.SkillsPlugin;
import de.raidcraft.skills.api.exceptions.UnknownSkillException;
import de.raidcraft.skills.api.hero.Hero;
import de.raidcraft.skills.api.skill.Skill;

/**
 * @author Philip Urban
 */
public class DatabaseResident extends AbstractResident {

    public DatabaseResident(String name, Role profession, City city) {

        super(name, profession, city);

        try {
            Hero hero = RaidCraft.getComponent(SkillsPlugin.class).getCharacterManager().getHero(getName());
            Skill skill = RaidCraft.getComponent(SkillsPlugin.class).getSkillManager().getSkill(hero, hero.getVirtualProfession(), "prefix-" + city.getName().toLowerCase());
            if (skill.isUnlocked()) {
                return;
            }
            hero.addSkill(skill);
        } catch (UnknownPlayerException | UnknownSkillException e) {}
    }

    public DatabaseResident(TResident tResident) {

        //XXX setter call order is important!!!
        this.id = tResident.getId();

        City city = RaidCraft.getComponent(RCCitiesPlugin.class).getCityManager().getCity(tResident.getCity().getName());
        assert city != null : "City of resident is null!";
        this.city = city;
        this.name = tResident.getName();
        setRole(Role.valueOf(tResident.getProfession()));
    }

    @Override
    public void save() {

        // save new resident
        if(getId() == 0) {
            TResident tResident = new TResident();
            tResident.setCity(getCity());
            tResident.setName(getName());
            tResident.setProfession(getRole().name());
            RaidCraft.getDatabase(RCCitiesPlugin.class).save(tResident);
            this.id = tResident.getId();
        }
        // update existing resident
        else {
            TResident tResident = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TResident.class, getId());
            tResident.setProfession(getRole().name());
            RaidCraft.getDatabase(RCCitiesPlugin.class).update(tResident);
        }
    }

    @Override
    public void delete() {

        RCCitiesPlugin plugin = RaidCraft.getComponent(RCCitiesPlugin.class);

        for(Plot plot : plugin.getPlotManager().getPlots(city)) {
            plot.removeResident(this);
            plot.updateRegion(false);
        }

        // remove prefix skill
        try {
            RaidCraft.getComponent(SkillsPlugin.class).getCharacterManager().getHero(getName()).getSkill("prefix-" + city.getName().toLowerCase()).remove();
        } catch (UnknownSkillException e) {
            RaidCraft.LOGGER.warning("No prefix skill found!");
            e.printStackTrace();
        }
        catch (UnknownPlayerException e) {
        }

        plugin.getResidentManager().removeFromCache(this);
        TResident tResident = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TResident.class, getId());
        RaidCraft.getDatabase(RCCitiesPlugin.class).delete(tResident);
    }
}
