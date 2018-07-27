package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FnF extends Fragment {

    TextView superfnf,sfnfhead,gfnfhead;
    ListView fnfList;
    Button addFnf;

    public FnF() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fnf, container, false);

        superfnf = (TextView) view.findViewById(R.id.super_fnf);
        fnfList = (ListView) view.findViewById(R.id.fnf_list);
        addFnf = (Button) view.findViewById(R.id.addFnF);

        sfnfhead = (TextView) view.findViewById(R.id.super_fnf_heading);
        gfnfhead = (TextView) view.findViewById(R.id.general_fnf_heading);

        sfnfhead.setText("Super FnF ("+MainActivity.bestPackage.operator+")");
        gfnfhead.setText("General FnF ("+MainActivity.bestPackage.operator+")");

        superfnf.setText(MainActivity.bestPackage.superFnf);
        fnfList.setAdapter(new CustomAdapter((MainActivity) getActivity(),MainActivity.bestPackage.fnf));

        if (MainActivity.bestPackage.superFnf.charAt(0) == 'N')
            addFnf.setVisibility(View.INVISIBLE);
        else addFnf.setOnClickListener(new View.OnClickListener() {

            SmsManager smsManager = SmsManager.getDefault();

            @Override
            public void onClick(View v) {
                switch (MainActivity.operator.toUpperCase().charAt(0)) {
//                    no super fnf in airtel
                    case 'G':
                        smsManager.sendTextMessage("2888",null,("SF "+MainActivity.bestPackage.sfnf),null,null);
                        addFnf.setVisibility(View.INVISIBLE);
                        break;
                    case 'R':
                        smsManager.sendTextMessage("8363",null,("P "+MainActivity.bestPackage.sfnf),null,null);
                        addFnf.setVisibility(View.INVISIBLE);
                        break;
                    case 'B':
                        String ussdCode = "*166*7*" +MainActivity.bestPackage.sfnf+Uri.encode("#");
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussdCode)));
                        addFnf.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        break;
                }

            }
        });

        setListViewHeightBasedOnChildren(fnfList);

        return view;
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


}
