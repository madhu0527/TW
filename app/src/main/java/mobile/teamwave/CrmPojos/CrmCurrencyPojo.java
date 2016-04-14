package mobile.teamwave.CrmPojos;

/**
 * Created by goodworklabs on 27/02/2016.
 */
public class CrmCurrencyPojo {
    private int id;

    private String symbol;

    private String name;

    private String code;

    private boolean is_custom;

    private boolean is_trashed;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean is_custom() {
        return is_custom;
    }

    public void setIs_custom(boolean is_custom) {
        this.is_custom = is_custom;
    }

    public boolean is_trashed() {
        return is_trashed;
    }

    public void setIs_trashed(boolean is_trashed) {
        this.is_trashed = is_trashed;
    }


}
