package mobile.teamwave.CrmPojos;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class CrmDealDetail {
	private String tags;

	private int id;

	private String next_activity_on;

	private String index;

	private String title;

	private String deal_value;

	private String status;

	private String created_on;
	@SerializedName("expected_close")
	private String expected_close;

	private String last_activity_on;

	private String is_trashed;
	
	@SerializedName("created_by")
	private CrmPeopleCreatedByPojo dealCreatedByPojo;
	
	@SerializedName("owner")
	private CrmPeopleOwnerPojo dealOwnerPojo;
	
	@SerializedName("stage")
	private CrmDealStagePojo dealStagePojo;
	
	@SerializedName("currency")
	private CrmDealCurrencyPojo dealCurrencyPojo;
	
	@SerializedName("people")
	private List<CrmPeopleListPojo> dealPeoplePojo;
	
	public CrmPeopleCreatedByPojo getDealCreatedByPojo() {
		return dealCreatedByPojo;
	}

	public void setDealCreatedByPojo(CrmPeopleCreatedByPojo dealCreatedByPojo) {
		this.dealCreatedByPojo = dealCreatedByPojo;
	}

	public CrmPeopleOwnerPojo getDealOwnerPojo() {
		return dealOwnerPojo;
	}

	public void setDealOwnerPojo(CrmPeopleOwnerPojo dealOwnerPojo) {
		this.dealOwnerPojo = dealOwnerPojo;
	}

	public CrmDealStagePojo getDealStagePojo() {
		return dealStagePojo;
	}

	public void setDealStagePojo(CrmDealStagePojo dealStagePojo) {
		this.dealStagePojo = dealStagePojo;
	}

	public CrmDealCurrencyPojo getDealCurrencyPojo() {
		return dealCurrencyPojo;
	}

	public void setDealCurrencyPojo(CrmDealCurrencyPojo dealCurrencyPojo) {
		this.dealCurrencyPojo = dealCurrencyPojo;
	}

	public List<CrmPeopleListPojo> getDealPeoplePojo() {
		return dealPeoplePojo;
	}

	public void setDealPeoplePojo(List<CrmPeopleListPojo> dealPeoplePojo) {
		this.dealPeoplePojo = dealPeoplePojo;
	}

	public CrmActivitiesCompanyPojo getDealCompanyPojo() {
		return dealCompanyPojo;
	}

	public void setDealCompanyPojo(CrmActivitiesCompanyPojo dealCompanyPojo) {
		this.dealCompanyPojo = dealCompanyPojo;
	}

	public CrmDealPipelinesPojo getDealPeplinePojo() {
		return dealPeplinePojo;
	}

	public void setDealPeplinePojo(CrmDealPipelinesPojo dealPeplinePojo) {
		this.dealPeplinePojo = dealPeplinePojo;
	}

	@SerializedName("company")
	private CrmActivitiesCompanyPojo dealCompanyPojo;
	
	@SerializedName("pipeline")
	private CrmDealPipelinesPojo dealPeplinePojo;
	
	
	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNext_activity_on() {
		return next_activity_on;
	}

	public void setNext_activity_on(String next_activity_on) {
		this.next_activity_on = next_activity_on;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDeal_value() {
		return deal_value;
	}

	public void setDeal_value(String deal_value) {
		this.deal_value = deal_value;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreated_on() {
		return created_on;
	}

	public void setCreated_on(String created_on) {
		this.created_on = created_on;
	}

	public String getExpected_close() {
		return expected_close;
	}

	public void setExpected_close(String expected_close) {
		this.expected_close = expected_close;
	}

	public String getLast_activity_on() {
		return last_activity_on;
	}

	public void setLast_activity_on(String last_activity_on) {
		this.last_activity_on = last_activity_on;
	}

	public String getIs_trashed() {
		return is_trashed;
	}

	public void setIs_trashed(String is_trashed) {
		this.is_trashed = is_trashed;
	}

}
