package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class BestOperator extends Fragment {

    TextView packageName;
    TextView packageDetails;
    TextView superfnf;
    ListView fnfList;
    Button addFnf;
    TextView save;
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
        save = (TextView) view.findViewById(R.id.save);
        packageDetails = (TextView) view.findViewById(R.id.packageDetails);

        packageName.setText(MainActivity.bestOperator.packageName);
        packageDetails.setText("Super FnF : " + (MainActivity.bestOperator.superFnf.charAt(0) == 'N' ? "0" : "1") + "\n");
        packageDetails.append("General FnF : " + (MainActivity.bestOperator.fnf.get(0).charAt(0) == 'N' ? "0" : MainActivity.bestOperator.fnf.size()));
        superfnf.setText(MainActivity.bestOperator.superFnf);
        fnfList.setAdapter(new CustomAdapter((MainActivity) getActivity(), MainActivity.bestOperator.fnf));

        operator.setText(MainActivity.bestOperator.operator);
        save.setText("Using "+MainActivity.bestOperator.operator+" "+MainActivity.bestOperator.packageName+" you could save "+Long.toString(Math.round(MainActivity.bestPackage.cost+Double.parseDouble(MainActivity.save)-MainActivity.bestOperator.cost))+" taka");

        ListView lv = (ListView) view.findViewById(R.id.costList);
        lv.setAdapter(new CostAdapter(getContext(),MainActivity.bestOperator.packageList));

        FnF.setListViewHeightBasedOnChildren(lv);
        FnF.setListViewHeightBasedOnChildren(fnfList);
        return view;
    }

}
