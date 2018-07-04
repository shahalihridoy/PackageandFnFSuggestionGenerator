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
public class FnF extends Fragment {

    TextView superfnf;
    ListView fnfList;

    public FnF() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fnf, container, false);

        superfnf = (TextView) view.findViewById(R.id.super_fnf);
        fnfList = (ListView) view.findViewById(R.id.fnf_list);

        superfnf.setText(MainActivity.bestPackage.superFnf);
        fnfList.setAdapter(new CustomAdapter((MainActivity) getActivity(),MainActivity.bestPackage.fnf));
        return view;
    }

}
