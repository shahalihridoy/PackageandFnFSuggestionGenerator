package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;


/**
 * A simple {@link Fragment} subclass.
 */
public class BestOperator extends Fragment {

    TextView packageName;
    TextView superfnf;
    ListView fnfList;
    Button addFnf;
    TextView operator;

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
        operator = (TextView) view.findViewById(R.id.operator);

        packageName.setText(MainActivity.bestOperator.packageName);
        superfnf.setText(MainActivity.bestOperator.superFnf);
        fnfList.setAdapter(new CustomAdapter((MainActivity) getActivity(), MainActivity.bestOperator.fnf));

        operator.setText(MainActivity.bestOperator.operator);
        operator.append("\nYou could save xxx taka");

        return view;
    }

}
