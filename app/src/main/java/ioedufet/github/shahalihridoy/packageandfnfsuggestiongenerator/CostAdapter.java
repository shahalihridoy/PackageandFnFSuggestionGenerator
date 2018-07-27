package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CostAdapter extends BaseAdapter {

    Context context;
    List<CostHelper> list;
    LayoutInflater inflater;

    CostAdapter(Context context, List<CostHelper> list){
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewFinder vf = new ViewFinder();

        View view = inflater.inflate(R.layout.cost_list, null);

        vf.packageName = (TextView) view.findViewById(R.id.packageName);
        vf.packageCost = (TextView) view.findViewById(R.id.packageCost);

        vf.packageName.setText(list.get(position).name);
        vf.packageCost.setText(Long.toString(Math.round(list.get(position).cost))+" tk");

        return view;
    }

    public class ViewFinder {
        TextView packageName;
        TextView packageCost;
    }
}
