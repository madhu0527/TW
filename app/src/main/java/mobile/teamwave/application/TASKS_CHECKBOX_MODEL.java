package mobile.teamwave.application;

public class TASKS_CHECKBOX_MODEL {

	String name = null,taskGrpid,proId,taskId,createdBy,taskCreatedDate,cmntCount;
	boolean selected = false;
	boolean complete;

	public TASKS_CHECKBOX_MODEL(String taskGrpid,String proId,String taskId,String name, boolean selected, boolean complete,String createdBy,String taskCreatedDate, String cmntCount) {
		super();
		this.taskGrpid =taskGrpid;
		this.proId =proId;
		this.taskId=taskId;
		this.name = name;
		this.selected = selected;
		this.complete = complete;
		this.createdBy = createdBy;
		this.taskCreatedDate = taskCreatedDate;
		this.cmntCount = cmntCount;
	}
	public String getCmntCount() {
		return cmntCount;
	}
	public void setCmntCount(String cmntCount) {
		this.cmntCount = cmntCount;
	}
	public String getTaskCreatedDate() {
		return taskCreatedDate;
	}
	public void setTaskCreatedDate(String taskCreatedDate) {
		this.taskCreatedDate = taskCreatedDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	
	public String getproId() {
		return proId;
	}

	public void setproId(String proId) {
		this.proId = proId;
	}


	public String getGrpId() {
		return taskGrpid;
	}

	public void setId(String id) {
		this.taskGrpid = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public boolean Iscomplete() {
		return complete;
	}

	public void setcomplete(boolean complete) {
		this.complete = complete;
	}

}