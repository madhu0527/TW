package mobile.teamwave.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mobile.teamwave.CrmPojos.CrmDealPipelinesPojo;
import mobile.teamwave.CrmPojos.CrmLinkPersons;
import teamwave.android.mobile.teamwave.R;

/**
 * Created by goodworklabs on 26/02/2016.
 */
public class LinkPersonAdapter extends BaseAdapter {
    private Context context;
    List<CrmLinkPersons> crmLinkPersonsList;
    private LayoutInflater inflator;

    public LinkPersonAdapter(Activity context,
                             List<CrmLinkPersons> crmLinkPersonsListsPojo) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.crmLinkPersonsList = crmLinkPersonsListsPojo;
        inflator = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return crmLinkPersonsList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return crmLinkPersonsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return crmLinkPersonsList.get(position).hashCode();
    }

    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflator.inflate(R.layout.crm_people_list_row, null);
            holder = new ViewHolder();
            holder.personFullNameTxt = (TextView) convertView
                    .findViewById(R.id.namePeopleTxt);
            holder.companyNameTxt = (TextView) convertView
                    .findViewById(R.id.compnyNameTxt);
            holder.userImg = (ImageView) convertView
                    .findViewById(R.id.imagePeople);
            holder.userImg.setVisibility(View.GONE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.personFullNameTxt.setText(crmLinkPersonsList.get(position).getFull_name());
        holder.companyNameTxt.setText(crmLinkPersonsList.get(position).getCompany_name());
        return convertView;
    }

    public class ViewHolder {
        public TextView personFullNameTxt,companyNameTxt;
        ImageView userImg;
    }
}

