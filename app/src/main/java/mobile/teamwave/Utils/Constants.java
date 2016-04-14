package mobile.teamwave.Utils;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by goodworklabs on 26/02/2016.
 */
public class Constants {
    public static final List<String> currencyArr = new ArrayList<String>();

    public static List<String> getCurtencyArr() {
        currencyArr.add("US Dollar");
        currencyArr.add("UK Pound");
        currencyArr.add("Euro");
        currencyArr.add("Indian Rupee");
        currencyArr.add("Canadian Dollar");
        return currencyArr;
    }
}
