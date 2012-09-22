package de.strasse36.rccities;

/**
 * Author: Philip Urban
 * Date: 10.03.12 - 14:54
 * Description:
 */
public class Assignment {
    private int plot_id;
    private String player;

    public Assignment(int plot_id, String player) {
        this.plot_id = plot_id;
        this.player = player;
    }

    public int getPlot_id() {
        return plot_id;
    }

    public void setPlot_id(int plot_id) {
        this.plot_id = plot_id;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }
}
