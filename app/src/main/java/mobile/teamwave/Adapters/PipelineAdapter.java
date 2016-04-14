package mobile.teamwave.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mobile.teamwave.CrmPojos.CrmDealPipelinesPojo;
import teamwave.android.mobile.teamwave.R;

/**
 * Created by goodworklabs on 25/02/2016.
 */
public class PipelineAdapter extends BaseAdapter {
    private Context context;
    List<CrmDealPipelinesPojo> crmDealPipelinesPojos;
    private LayoutInflater inflator;

    public PipelineAdapter(Activity context,
                           List<CrmDealPipelinesPojo> crmDealPipelinesPojo) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.crmDealPipelinesPojos = crmDealPipelinesPojo;
        inflator = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return crmDealPipelinesPojos.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return crmDealPipelinesPojos.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return crmDealPipelinesPojos.get(position).hashCode();
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
        holder.pipleLineNameTxt.setText(crmDealPipelinesPojos.get(position)
                .getTitle());

        return convertView;
    }

    public class ViewHolder {
        public TextView pipleLineNameTxt;
    }
}
