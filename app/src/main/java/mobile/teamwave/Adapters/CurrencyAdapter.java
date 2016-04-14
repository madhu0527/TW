package mobile.teamwave.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mobile.teamwave.CrmPojos.CrmCurrencyPojo;
import mobile.teamwave.CrmPojos.CrmStagesPOjo;
import teamwave.android.mobile.teamwave.R;

/**
 * Created by goodworklabs on 26/02/2016.
 */
public class CurrencyAdapter extends BaseAdapter {
    private Context context;
    List<CrmCurrencyPojo> curencyArr;
    private LayoutInflater inflator;

    public CurrencyAdapter(Activity context,
                           List<CrmCurrencyPojo> curncyArry) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.curencyArr = curncyArry;
        inflator = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return curencyArr.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return curencyArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return curencyArr.get(position).hashCode();
    }

    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflator.inflate(R.layout.crm_pipeline_row, null);
            holder = new ViewHolder();
            holder.pipleLineNameTxt = (TextView) convertView
                    .findViewById(R.id.txtTitle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.pipleLineNameTxt.setText(curencyArr.get(position).getName());

        return convertView;
    }

    public class ViewHolder {
        public TextView pipleLineNameTxt;
    }
}
