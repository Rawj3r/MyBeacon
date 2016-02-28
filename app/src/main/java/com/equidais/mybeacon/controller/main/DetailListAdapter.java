package com.equidais.mybeacon.controller.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.equidais.mybeacon.R;
import com.equidais.mybeacon.common.GlobalFunc;
import com.equidais.mybeacon.model.VisitEntriesResult;


import java.util.List;

public class DetailListAdapter extends BaseAdapter {

    private List<VisitEntriesResult> listdata;
    private LayoutInflater inflater=null;
    private Context mContext;




    public DetailListAdapter(Context context, List<VisitEntriesResult> _listData) {

        listdata=_listData;

        mContext = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);



    }



    public int getCount() {

        return listdata.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {



        View vi = convertView;
        if (vi == null) {
            vi = inflater.inflate(R.layout.row_visit_entries, null);
        }

        VisitEntriesResult data = listdata.get(position);
        TextView txtGym = (TextView)vi.findViewById(R.id.txt_gym);
        TextView txtDate = (TextView)vi.findViewById(R.id.txt_date);
        TextView txtDuration = (TextView)vi.findViewById(R.id.txt_duration);

        txtGym.setText(data.Gym);
        txtDate.setText(mContext.getResources().getString(R.string.intime) + " " + GlobalFunc.getDateString(data.TimeIn) + " " +
            mContext.getResources().getString(R.string.outtime) + " " + GlobalFunc.getDateString(data.TimeOut));
        txtDuration.setText(GlobalFunc.getDurationFrom2Dates(data.TimeIn, data.TimeOut));
        return vi;

    }
    
    
}