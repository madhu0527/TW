package mobile.teamwave.TaskGrpPojo;

import java.util.ArrayList;

public class TaskGrpPojo {
	int id, index, total, completed, incompleted, comment_count;
	String name, description, created_on, resource_url,
			modified_on;
	ArrayList<TasksPojo> tasks;
	TaskMilestonesPojo milestones;
	CreatedByPojo created_by;
	TaskModifiedByPojo modified_by;
	public TaskModifiedByPojo getModified_by() {
		return modified_by;
	}

	public void setModified_by(TaskModifiedByPojo modified_by) {
		this.modified_by = modified_by;
	}

	boolean is_trashed, is_private;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getCompleted() {
		return completed;
	}

	public void setCompleted(int completed) {
		this.completed = completed;
	}

	public int getIncompleted() {
		return incompleted;
	}

	public void setIncompleted(int incompleted) {
		this.incompleted = incompleted;
	}

	public int getComment_count() {
		return comment_count;
	}

	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TaskMilestonesPojo getMilestones() {
		return milestones;
	}

	public void setMilestones(TaskMilestonesPojo milestones) {
		this.milestones = milestones;
	}

	public String getCreated_on() {
		return created_on;
	}

	public void setCreated_on(String created_on) {
		this.created_on = created_on;
	}

	public String getResource_url() {
		return resource_url;
	}

	public void setResource_url(String resource_url) {
		this.resource_url = resource_url;
	}

	

	public String getModified_on() {
		return modified_on;
	}

	public void setModified_on(String modified_on) {
		this.modified_on = modified_on;
	}

	public ArrayList<TasksPojo> getTasks() {
		return tasks;
	}

	public void setTasks(ArrayList<TasksPojo> tasks) {
		this.tasks = tasks;
	}

	public CreatedByPojo getCreated_by() {
		return created_by;
	}

	public void setCreated_by(CreatedByPojo created_by) {
		this.created_by = created_by;
	}

	public boolean isIs_trashed() {
		return is_trashed;
	}

	public void setIs_trashed(boolean is_trashed) {
		this.is_trashed = is_trashed;
	}

	public boolean isIs_private() {
		return is_private;
	}

	public void setIs_private(boolean is_private) {
		this.is_private = is_private;
	}
}
