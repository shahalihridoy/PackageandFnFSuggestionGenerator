package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Toast;

import java.util.List;

public class BestPackage extends Fragment {

    static TextView packageName;
    static TextView currentPackage;
    static TextView save;
    Button addFnF;

    public BestPackage(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_best_package, container, false);

        packageName = (TextView) view.findViewById(R.id.package_name);
        currentPackage = (TextView) view.findViewById(R.id.fnfDetails);
        save = (TextView) view.findViewById(R.id.save);
        addFnF = (Button) view.findViewById(R.id.activate);

        setVariable();

        addFnF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //<editor-fold desc="Send sms for activating package">
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    String ussdCode;
                    switch (MainActivity.operator.toUpperCase().charAt(0)) {
                        case 'A':
                            ussdCode = MainActivity.bestPackage.code+Uri.encode("#");
                            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussdCode)));
                            break;
                        case 'G':
                            smsManager.sendTextMessage("4444", null, MainActivity.bestPackage.code, null, null);
                            break;
                        case 'R':
                            ussdCode = MainActivity.bestPackage.code+Uri.encode("#");
                            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussdCode)));
                            break;
                        case 'B':
                            if(MainActivity.bestPackage.code.charAt(0) == '*'){
                                ussdCode = MainActivity.bestPackage.code+Uri.encode("#");
                                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussdCode)));
                            } else {
                                smsManager.sendTextMessage("9999", null, MainActivity.bestPackage.code, null, null);
                            }
                            break;
                        case 'T':
                            Toast.makeText(getActivity(),MainActivity.bestPackage.code,Toast.LENGTH_LONG).show();
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //</editor-fold>
            }
        });

        return view;
    }

    static void setVariable(){
        packageName.setText(MainActivity.bestPackage.packageName);
        save.setText("You could save "+MainActivity.save+" taka");
        currentPackage.setText("Super FnF : "+(MainActivity.bestPackage.superFnf.charAt(0)=='N' ? "0" : "1")+"\n");
        currentPackage.append("General FnF : "+(MainActivity.bestPackage.fnf.get(0).charAt(0)=='N' ? "0" : MainActivity.bestPackage.fnf.size())+"\n");
        currentPackage.append("Current Package : "+MainActivity.currentPackage);
    }
}
