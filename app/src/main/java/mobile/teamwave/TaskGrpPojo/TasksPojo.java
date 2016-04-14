package mobile.teamwave.TaskGrpPojo;

public class TasksPojo {
	int id, index, taskgroup_id, comment_count;
	String name, due_date,
			completed_date, timelog, created_on, modified_on, taskgroup_name,
			resource_url, tmp_due_date;
	public boolean is_completed;
	boolean taskgroup_is_private;
	boolean taskgroup_is_trashed;
	boolean is_trashed;
	AssignedToTasks assigned_to;
	TasksModifiedBy modified_by;
	TaskCompletedBy completed_by;
	CreatedByPojo created_by;
	public CreatedByPojo getCreated_by() {
		return created_by;
	}

	public void setCreated_by(CreatedByPojo created_by) {
		this.created_by = created_by;
	}

	public AssignedToTasks getAssigned_to() {
		return assigned_to;
	}

	public void setAssigned_to(AssignedToTasks assigned_to) {
		this.assigned_to = assigned_to;
	}

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

	public int getTaskgroup_id() {
		return taskgroup_id;
	}

	public void setTaskgroup_id(int taskgroup_id) {
		this.taskgroup_id = taskgroup_id;
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

	

	public String getDue_date() {
		return due_date;
	}

	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}

	public TaskCompletedBy getCompleted_by() {
		return completed_by;
	}

	public void setCompleted_by(TaskCompletedBy completed_by) {
		this.completed_by = completed_by;
	}

	public TasksModifiedBy getModified_by() {
		return modified_by;
	}

	public void setModified_by(TasksModifiedBy modified_by) {
		this.modified_by = modified_by;
	}

	public String getCompleted_date() {
		return completed_date;
	}

	public void setCompleted_date(String completed_date) {
		this.completed_date = completed_date;
	}

	public String getTimelog() {
		return timelog;
	}

	public void setTimelog(String timelog) {
		this.timelog = timelog;
	}

	public String getCreated_on() {
		return created_on;
	}

	public void setCreated_on(String created_on) {
		this.created_on = created_on;
	}

	public String getModified_on() {
		return modified_on;
	}

	public void setModified_on(String modified_on) {
		this.modified_on = modified_on;
	}

	public String getTaskgroup_name() {
		return taskgroup_name;
	}

	public void setTaskgroup_name(String taskgroup_name) {
		this.taskgroup_name = taskgroup_name;
	}

	public String getResource_url() {
		return resource_url;
	}

	public void setResource_url(String resource_url) {
		this.resource_url = resource_url;
	}

	public String getTmp_due_date() {
		return tmp_due_date;
	}

	public void setTmp_due_date(String tmp_due_date) {
		this.tmp_due_date = tmp_due_date;
	}

	public boolean isIs_completed() {
		return is_completed;
	}

	public void setIs_completed(boolean is_completed) {
		this.is_completed = is_completed;
	}

	public boolean isTaskgroup_is_private() {
		return taskgroup_is_private;
	}

	public void setTaskgroup_is_private(boolean taskgroup_is_private) {
		this.taskgroup_is_private = taskgroup_is_private;
	}

	public boolean isTaskgroup_is_trashed() {
		return taskgroup_is_trashed;
	}

	public void setTaskgroup_is_trashed(boolean taskgroup_is_trashed) {
		this.taskgroup_is_trashed = taskgroup_is_trashed;
	}

	public boolean isIs_trashed() {
		return is_trashed;
	}

	public void setIs_trashed(boolean is_trashed) {
		this.is_trashed = is_trashed;
	}
}
