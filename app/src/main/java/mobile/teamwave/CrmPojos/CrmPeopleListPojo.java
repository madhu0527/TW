package mobile.teamwave.CrmPojos;

import com.google.gson.annotations.SerializedName;

public class CrmPeopleListPojo {

	public CrmPeopleOwnerPojo getCrmPeopleOwner() {
		return crmPeopleOwner;
	}

	public void setCrmPeopleOwner(CrmPeopleOwnerPojo crmPeopleOwner) {
		this.crmPeopleOwner = crmPeopleOwner;
	}

	public CrmActivitiesCompanyPojo getCrmPeopleCompany() {
		return crmPeopleCompany;
	}

	public void setCrmPeopleCompany(CrmActivitiesCompanyPojo crmPeopleCompany) {
		this.crmPeopleCompany = crmPeopleCompany;
	}

	public CrmPeopleCreatedByPojo getCrmPeopleCreatedBy() {
		return crmPeopleCreatedBy;
	}

	public void setCrmPeopleCreatedBy(CrmPeopleCreatedByPojo crmPeopleCreatedBy) {
		this.crmPeopleCreatedBy = crmPeopleCreatedBy;
	}

	private String tags;

	private String first_name;

	private String phone;

	private String email;

	private int id;

	private String created_on;

	private String job_title;

	private String last_name;

	private String full_name;
	
	@SerializedName("owner")
	private CrmPeopleOwnerPojo crmPeopleOwner;
	
	@SerializedName("company")
	private CrmActivitiesCompanyPojo crmPeopleCompany;
	
	@SerializedName("created_by")
	private CrmPeopleCreatedByPojo crmPeopleCreatedBy;

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreated_on() {
		return created_on;
	}

	public void setCreated_on(String created_on) {
		this.created_on = created_on;
	}

	public String getJob_title() {
		return job_title;
	}

	public void setJob_title(String job_title) {
		this.job_title = job_title;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getFull_name() {
		return full_name;
	}

	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}

}
