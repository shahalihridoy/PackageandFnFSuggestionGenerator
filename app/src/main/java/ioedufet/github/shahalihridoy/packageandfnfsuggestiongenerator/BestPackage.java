package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BestPackage extends Fragment {

    static TextView packageName;
    static TextView bestPackageDetails;
    static TextView currentPackageDetails;
    static TextView save;
    Button addFnF;

    LinearLayout costLayout;
    TextView packageCost, packageNames;

    public BestPackage() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_best_package, container, false);

        packageName = (TextView) view.findViewById(R.id.package_name);
        bestPackageDetails = (TextView) view.findViewById(R.id.bestPackageDetails);
        currentPackageDetails = (TextView) view.findViewById(R.id.currentPackageDetails);


        save = (TextView) view.findViewById(R.id.save);
        addFnF = (Button) view.findViewById(R.id.activate);

        setVariable();

        addFnF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("Confirmation");
                dialog.setMessage("Are you sure to migrate to "+MainActivity.bestPackage.packageName+"?");

                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //<editor-fold desc="Send sms for activating package">
                        try {
                            SmsManager smsManager = SmsManager.getDefault();
                            String ussdCode;
                            switch (MainActivity.operator.toUpperCase().charAt(0)) {
                                case 'A':
                                    ussdCode = MainActivity.bestPackage.code + Uri.encode("#");
                                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussdCode)));
                                    break;
                                case 'G':
                                    smsManager.sendTextMessage("4444", null, MainActivity.bestPackage.code, null, null);
                                    break;
                                case 'R':
                                    ussdCode = MainActivity.bestPackage.code + Uri.encode("#");
                                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussdCode)));
                                    break;
                                case 'B':
                                    if (MainActivity.bestPackage.code.charAt(0) == '*') {
                                        ussdCode = MainActivity.bestPackage.code + Uri.encode("#");
                                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussdCode)));
                                    } else {
                                        smsManager.sendTextMessage("9999", null, MainActivity.bestPackage.code, null, null);
                                    }
                                    break;
                                case 'T':
                                    Toast.makeText(getActivity(), MainActivity.bestPackage.code, Toast.LENGTH_LONG).show();
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

                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

//                something may be wrong here
//                if so, try with handler
                dialog.show();

            }
        });

////        adding custom view one by one using loop
//        costLayout = view.findViewById(R.id.costLayuot);
//        Helper helper = MainActivity.bestPackage;
//        for(int i = 0;i<helper.packageList.size();i++){
//            View customView = inflater.inflate(R.layout.cost_list, container, false);
//            packageCost = (TextView) customView.findViewById(R.id.packageCost);
//            packageNames = (TextView) customView.findViewById(R.id.packageName);
//            packageNames.setText(helper.packageList.get(i).packageName);
//            packageCost.setText(Long.toString(Math.round(helper.packageList.get(i).cost)));
//            costLayout.addView(customView);
//
//            System.out.println(helper.packageList.get(i).packageName);
//        }

        CostAdapter costAdapter = new CostAdapter(getContext(),MainActivity.bestPackage.packageList);
        ListView lv = (ListView) view.findViewById(R.id.costList);
        lv.setAdapter(costAdapter);

        FnF.setListViewHeightBasedOnChildren(lv);

        return view;
    }

    static void setVariable() {
        packageName.setText(MainActivity.bestPackage.packageName);
        save.setText("Using "+MainActivity.bestPackage.packageName+" you could save " + MainActivity.save + " taka");
        bestPackageDetails.setText("Super FnF : " + (MainActivity.bestPackage.superFnf.charAt(0) == 'N' ? "0" : "1") + "\n");
        bestPackageDetails.append("General FnF : " + (MainActivity.bestPackage.fnf.get(0).charAt(0) == 'N' ? "0" : MainActivity.bestPackage.fnf.size()));
        currentPackageDetails.setText(MainActivity.currentPackage);
//        currentPackageDetails.setText("No details is available now\nThis will be added soon\nMy Precioussss !!!");
    }
}
