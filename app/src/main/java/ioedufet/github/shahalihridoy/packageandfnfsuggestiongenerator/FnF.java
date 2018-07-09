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
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FnF extends Fragment {

    TextView superfnf;
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

        return view;
    }

}
