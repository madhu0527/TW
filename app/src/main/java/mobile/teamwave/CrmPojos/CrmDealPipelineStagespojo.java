package mobile.teamwave.CrmPojos;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class CrmDealPipelineStagespojo {
		private int id;

	    private int rotting_days;

	    private String title;

	    private int index;

	    private int minutes;

	    private int days;

	    private int hours;
	    
	    @SerializedName("people")
		private List<CrmDealPipelineStagespojo> dealPipeStagesPojo;
	    
	    public List<CrmDealPipelineStagespojo> getDealPipeStagesPojo() {
			return dealPipeStagesPojo;
		}

		public void setDealPipeStagesPojo(
				List<CrmDealPipelineStagespojo> dealPipeStagesPojo) {
			this.dealPipeStagesPojo = dealPipeStagesPojo;
		}


	    public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getRotting_days() {
			return rotting_days;
		}

		public void setRotting_days(int rotting_days) {
			this.rotting_days = rotting_days;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public int getMinutes() {
			return minutes;
		}

		public void setMinutes(int minutes) {
			this.minutes = minutes;
		}

		public int getDays() {
			return days;
		}

		public void setDays(int days) {
			this.days = days;
		}

		public int getHours() {
			return hours;
		}

		public void setHours(int hours) {
			this.hours = hours;
		}

		public String getIs_deal_rotting() {
			return is_deal_rotting;
		}

		public void setIs_deal_rotting(String is_deal_rotting) {
			this.is_deal_rotting = is_deal_rotting;
		}

		public String getIs_trashed() {
			return is_trashed;
		}

		public void setIs_trashed(String is_trashed) {
			this.is_trashed = is_trashed;
		}

		private String is_deal_rotting;

	    private String is_trashed;
}
