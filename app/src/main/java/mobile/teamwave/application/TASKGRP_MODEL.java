package mobile.teamwave.application;

public class TASKGRP_MODEL {
	String taskGrpid,proId,taskId, taskGrpName, taskName,createdBy,createdOn,commentCount;
	

	boolean selected = false;
	
	public TASKGRP_MODEL(String taskGrpid,String proId,String taskId, String taskGrpName,
			String taskName,String createdBy,String createdOn, String commentCount, boolean selected) {
		super();
		this.taskGrpid =taskGrpid;
		this.proId =proId;
		this.taskId=taskId;
		this.taskGrpName = taskGrpName;
		this.taskName = taskName;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.commentCount = commentCount;
	}
	
	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
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
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
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

	public String getTaskGrpName() {
		return taskGrpName;
	}

	public void setTaskGrpName(String taskGrpName) {
		this.taskGrpName = taskGrpName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

}
