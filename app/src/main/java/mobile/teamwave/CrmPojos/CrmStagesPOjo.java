package mobile.teamwave.CrmPojos;

/**
 * Created by goodworklabs on 26/02/2016.
 */
public class CrmStagesPOjo {
    private int id;
    private int rotting_days;

    private int index;

    private String title;

    private boolean is_deal_rotting;

    private boolean is_trashed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRotting_days() {
        return rotting_days;
    }

    public void setRotting_days(int rotting_days) {
        this.rotting_days = rotting_days;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean is_deal_rotting() {
        return is_deal_rotting;
    }

    public void setIs_deal_rotting(boolean is_deal_rotting) {
        this.is_deal_rotting = is_deal_rotting;
    }

    public boolean is_trashed() {
        return is_trashed;
    }

    public void setIs_trashed(boolean is_trashed) {
        this.is_trashed = is_trashed;
    }
}
