package mobile.teamwave.CrmPojos;

public class CrmActivitiesActTypePojo {
	private int id;
	private String icon;
	private String title;
	boolean is_trashed;
	public boolean isIs_trashed() {
		return is_trashed;
	}
	public void setIs_trashed(boolean is_trashed) {
		this.is_trashed = is_trashed;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
