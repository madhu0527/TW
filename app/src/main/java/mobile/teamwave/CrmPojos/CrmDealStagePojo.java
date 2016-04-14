package mobile.teamwave.CrmPojos;

public class CrmDealStagePojo {
    private int id;

    private int rotting_days;

    private String title;

    private int index;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getTotal_stage() {
        return total_stage;
    }

    public void setTotal_stage(int total_stage) {
        this.total_stage = total_stage;
    }

    public String getIs_deal_rotting() {
        return is_deal_rotting;
    }

    public void setIs_deal_rotting(String is_deal_rotting) {
        this.is_deal_rotting = is_deal_rotting;
    }

    private int total_stage;

    private String is_deal_rotting;

}
