package com.example.appsecurity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.io.File;
import java.util.ArrayList;

public class ExtractAdapterActivity extends ArrayAdapter<File> {
    Context context;
    ExtractAdapterActivity.ViewHolder viewHolder;
    ArrayList<File> all_pdf;
    public ExtractAdapterActivity(@NonNull Context context, ArrayList<File> all_pdf) {
        super(context, R.layout.activity_extractadapter,all_pdf);
        this.context = context;
        this.all_pdf = all_pdf;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {
        if(all_pdf.size()>0){
            return all_pdf.size();
        } else return 1;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.activity_extractadapter,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.text_filename=(TextView)convertView.findViewById(R.id.name_text1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        viewHolder.text_filename.setText(all_pdf.get(position).getName());
        return convertView;
    }
    public class ViewHolder{
        TextView text_filename;
    }
}
