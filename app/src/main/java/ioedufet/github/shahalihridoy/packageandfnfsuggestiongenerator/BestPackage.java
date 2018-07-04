package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class BestPackage extends Fragment {

    TextView packageName;
    TextView superfnf;
    ListView fnfList;

    public BestPackage(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_best_package, container, false);
        packageName = (TextView) view.findViewById(R.id.package_name);
        superfnf = (TextView) view.findViewById(R.id.super_fnf);
        fnfList = (ListView) view.findViewById(R.id.fnf_list);

        packageName.setText(MainActivity.bestPackage.packageName);
        superfnf.setText(MainActivity.bestPackage.superFnf);
        fnfList.setAdapter(new CustomAdapter((MainActivity) getActivity(),MainActivity.bestPackage.fnf));
        return view;
    }
}
