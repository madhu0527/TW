package mobile.teamwave.application;

public class EVENTS_MODEL {
	String date, eventId, title;

	public EVENTS_MODEL(String eventId, String date, String title) {
		super();
		this.date = date;
		this.eventId = eventId;
		this.title = title;

	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventTitle() {
		return title;
	}

	public void setEventTitle(String title) {
		this.title = title;
	}

}
