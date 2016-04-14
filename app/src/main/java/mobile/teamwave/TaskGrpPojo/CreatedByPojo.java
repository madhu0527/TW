package mobile.teamwave.TaskGrpPojo;

public class CreatedByPojo {
	int id;
	String first_name, last_name, label_txt, image, email, full_name,
			last_login, job_title, time_zone, uuid;
	boolean is_crm_enabled, is_crm_admin, is_pm_enabled, is_owner, is_admin,
			is_active;
	OrganizationPojo organization;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getLabel_txt() {
		return label_txt;
	}

	public void setLabel_txt(String label_txt) {
		this.label_txt = label_txt;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFull_name() {
		return full_name;
	}

	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}

	public String getLast_login() {
		return last_login;
	}

	public void setLast_login(String last_login) {
		this.last_login = last_login;
	}

	public String getJob_title() {
		return job_title;
	}

	public void setJob_title(String job_title) {
		this.job_title = job_title;
	}

	public String getTime_zone() {
		return time_zone;
	}

	public void setTime_zone(String time_zone) {
		this.time_zone = time_zone;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public boolean isIs_crm_enabled() {
		return is_crm_enabled;
	}

	public void setIs_crm_enabled(boolean is_crm_enabled) {
		this.is_crm_enabled = is_crm_enabled;
	}

	public boolean isIs_crm_admin() {
		return is_crm_admin;
	}

	public void setIs_crm_admin(boolean is_crm_admin) {
		this.is_crm_admin = is_crm_admin;
	}

	public boolean isIs_pm_enabled() {
		return is_pm_enabled;
	}

	public void setIs_pm_enabled(boolean is_pm_enabled) {
		this.is_pm_enabled = is_pm_enabled;
	}

	public boolean isIs_owner() {
		return is_owner;
	}

	public void setIs_owner(boolean is_owner) {
		this.is_owner = is_owner;
	}

	public boolean isIs_admin() {
		return is_admin;
	}

	public void setIs_admin(boolean is_admin) {
		this.is_admin = is_admin;
	}

	public boolean isIs_active() {
		return is_active;
	}

	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}

	public OrganizationPojo getOrganization() {
		return organization;
	}

	public void setOrganization(OrganizationPojo organization) {
		this.organization = organization;
	}
}
