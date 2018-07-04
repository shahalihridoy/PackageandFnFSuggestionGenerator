package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class BestOperator extends Fragment {

    TextView packageName;
    TextView superfnf;
    ListView fnfList;

    public BestOperator() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_best_operator, container, false);
        packageName = (TextView) view.findViewById(R.id.package_name);
        superfnf = (TextView) view.findViewById(R.id.super_fnf);
        fnfList = (ListView) view.findViewById(R.id.fnf_list);

        packageName.setText(MainActivity.bestOperator.packageName);
        superfnf.setText(MainActivity.bestOperator.superFnf);
        fnfList.setAdapter(new CustomAdapter((MainActivity) getActivity(), MainActivity.bestOperator.fnf));
        return view;
    }

}
