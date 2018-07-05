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
    TextView currentPackage;

    public BestPackage(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_best_package, container, false);
        packageName = (TextView) view.findViewById(R.id.package_name);
        currentPackage = (TextView) view.findViewById(R.id.fnfDetails);

        currentPackage.setText("Super FnF : "+(MainActivity.bestPackage.superFnf.charAt(0)=='N' ? "0" : MainActivity.bestPackage.superFnf)+"\n");
        currentPackage.append("General FnF : "+(MainActivity.bestPackage.fnf.get(0).charAt(0)=='N' ? "0" : MainActivity.bestPackage.fnf.size())+"\n");
        currentPackage.append("Current Package : "+MainActivity.currentPackage);
        packageName.setText(MainActivity.bestPackage.packageName);

        return view;
    }
}
