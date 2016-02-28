package com.equidais.mybeacon.controller.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.equidais.mybeacon.R;
import com.equidais.mybeacon.common.GlobalFunc;
import com.equidais.mybeacon.model.MessageResult;
import com.equidais.mybeacon.model.VisitEntriesResult;

import java.util.List;

public class MessageListAdapter extends BaseAdapter {

    private List<MessageResult> listdata;
    private LayoutInflater inflater=null;
    private Context mContext;




    public MessageListAdapter(Context context, List<MessageResult> _listData) {

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
            vi = inflater.inflate(R.layout.row_message, null);
        }

        MessageResult data = listdata.get(position);
        TextView txtTitle = (TextView)vi.findViewById(R.id.txt_title);
        txtTitle.setText(data.Title);

        return vi;

    }
    
    
}