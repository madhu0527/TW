package mobile.teamwave.application;

import android.app.Application;

public class Hostname extends Application {
//	private String globalVariable = "http://192.168.1.92:8000/";
	private String globalVariable = "https://app.goteamwave.com/api/";
	
	public String globalVariable() {
		return globalVariable;
	}

	public void setglobalVariableArticlesList(String globalVariable) {
		this.globalVariable = globalVariable;
	}

	@Override
	public void onCreate() {
		// reinitialize variable
	}
}
