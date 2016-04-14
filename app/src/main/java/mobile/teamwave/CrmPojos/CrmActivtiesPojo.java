package mobile.teamwave.CrmPojos;

public class CrmActivtiesPojo {
	private int id;

	private String duration;

	private String title;

	private String due_time;

	private String act_on;

	private String activity_due_on;

	private String completed_on;

	private boolean is_completed;

	private String due_date;

	private String note;

	private boolean is_trashed;
	
	private CrmActivitiesActTypePojo act_type;
	
	private CrmActivitiesCompanyPojo assigned_to;
	
	private CrmActivitiesPersonoPojo person;
	
	private CrmActivitiesDeal deal;
	
	private CrmActivitiesCompanyPojo company;

	public CrmActivitiesActTypePojo getAct_type() {
		return act_type;
	}

	public void setAct_type(CrmActivitiesActTypePojo act_type) {
		this.act_type = act_type;
	}

	public CrmActivitiesCompanyPojo getAssigned_to() {
		return assigned_to;
	}

	public void setAssigned_to(CrmActivitiesCompanyPojo assigned_to) {
		this.assigned_to = assigned_to;
	}

	public CrmActivitiesPersonoPojo getPerson() {
		return person;
	}

	public void setPerson(CrmActivitiesPersonoPojo person) {
		this.person = person;
	}

	public CrmActivitiesDeal getDeal() {
		return deal;
	}

	public void setDeal(CrmActivitiesDeal deal) {
		this.deal = deal;
	}

	public CrmActivitiesCompanyPojo getCompany() {
		return company;
	}

	public void setCompany(CrmActivitiesCompanyPojo company) {
		this.company = company;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDue_time() {
		return due_time;
	}

	public void setDue_time(String due_time) {
		this.due_time = due_time;
	}

	public String getAct_on() {
		return act_on;
	}

	public void setAct_on(String act_on) {
		this.act_on = act_on;
	}

	public String getActivity_due_on() {
		return activity_due_on;
	}

	public void setActivity_due_on(String activity_due_on) {
		this.activity_due_on = activity_due_on;
	}

	public String getCompleted_on() {
		return completed_on;
	}

	public void setCompleted_on(String completed_on) {
		this.completed_on = completed_on;
	}

	public boolean isIs_completed() {
		return is_completed;
	}

	public void setIs_completed(boolean is_completed) {
		this.is_completed = is_completed;
	}

	public String getDue_date() {
		return due_date;
	}

	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean isIs_trashed() {
		return is_trashed;
	}

	public void setIs_trashed(boolean is_trashed) {
		this.is_trashed = is_trashed;
	}
	
}

