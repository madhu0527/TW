package mobile.teamwave.CrmPojos;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CrmDealPipelinesPojo {
	private int id;

    private String title;
	private String is_trashed;
    @SerializedName("stages")
	List<CrmStagesPOjo> crmStagesPojo;

    public List<CrmStagesPOjo> getCrmStagePojo() {
        return crmStagesPojo;
    }

    public void setCrmDealsPojo(List<CrmStagesPOjo> crmStagePojo) {
        this.crmStagesPojo = crmStagePojo;
    }
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIs_trashed() {
		return is_trashed;
	}

	public void setIs_trashed(String is_trashed) {
		this.is_trashed = is_trashed;
	}
}
