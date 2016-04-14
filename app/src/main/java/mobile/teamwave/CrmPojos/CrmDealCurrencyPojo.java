package mobile.teamwave.CrmPojos;

public class CrmDealCurrencyPojo {
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIs_custom() {
		return is_custom;
	}

	public void setIs_custom(String is_custom) {
		this.is_custom = is_custom;
	}

	private String symbol;

	private String code;

	private String is_custom;
}
