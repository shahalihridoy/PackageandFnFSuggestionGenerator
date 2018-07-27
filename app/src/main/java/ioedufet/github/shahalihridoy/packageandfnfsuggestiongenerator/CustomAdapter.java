package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.Manifest;
import android.app.AlertDialog;
import android.app.IntentService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.provider.Telephony;
import android.service.autofill.AutofillService;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStructure;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CustomAdapter extends BaseAdapter {

    Context context;
    MainActivity mainActivity;
    List<String> fnf;

    public static LayoutInflater inflater = null;

    public CustomAdapter(MainActivity mainActivity, List<String> fnf) {
        this.context = mainActivity;
        this.mainActivity = mainActivity;
        this.fnf = fnf;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return fnf.size();
    }

    @Override
    public Object getItem(int position) {
        return fnf.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Helper helper = new Helper();
        View view = inflater.inflate(R.layout.list, null);

//        helper.imageView = (ImageView) view.findViewById(R.id.list_image);
        helper.no = (TextView) view.findViewById(R.id.no);
        helper.textView = (TextView) view.findViewById(R.id.list_text);
        helper.addFnF = (Button) view.findViewById(R.id.addFnF);

//        helper.imageView.setImageResource(sign[position]);
        helper.no.setText(Integer.toString(position + 1));
        helper.no.append(".");
        helper.textView.setText(fnf.get(position));

//        don't add button when fnf is not applicable
        if (fnf.get(position).charAt(0) == 'N')
            helper.addFnF.setVisibility(View.INVISIBLE);

        else
            helper.addFnF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("Confirmation");
                    dialog.setMessage("Would you like to add "+fnf.get(position)+" to your FnF list ?");

                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //<editor-fold desc="Send sms for fnf setup">
                            try {
                                SmsManager smsManager = SmsManager.getDefault();
                                switch (MainActivity.operator.toUpperCase().charAt(0)) {
                                    case 'A':
                                        smsManager.sendTextMessage("8363", null, "ADD "+fnf.get(position), null, null);
                                        helper.addFnF.setVisibility(View.INVISIBLE);
                                        break;
                                    case 'G':
                                        smsManager.sendTextMessage("2888", null, fnf.get(position), null, null);
                                        helper.addFnF.setVisibility(View.INVISIBLE);
                                        break;
                                    case 'R':
                                        smsManager.sendTextMessage("8363", null, "A "+fnf.get(position), null, null);
                                        helper.addFnF.setVisibility(View.INVISIBLE);
                                        break;
                                    case 'B':
                                        smsManager.sendTextMessage("3300", null, "add "+fnf.get(position), null, null);
                                        helper.addFnF.setVisibility(View.INVISIBLE);
                                        break;
                                    case 'T':
                                        smsManager.sendTextMessage("363", null,"reg", null, null);
                                        smsManager.sendTextMessage("363", null, "add "+fnf.get(position), null, null);
                                        helper.addFnF.setVisibility(View.INVISIBLE);
                                        break;
                                    default:
                                        break;
                                }
//                    sending sms
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + 4444));
//                        intent.putExtra("sms_body", "P");
//                        mainActivity.startActivity(intent);


//                    -----------------------------------------
//                    reading sms
//                    Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
//
//                    if (cursor.moveToFirst()) { // must check the result to prevent exception
//                        do {
//                            String msgData = "";
//                            for (int idx = 0; idx < cursor.getColumnCount(); idx++) {
//                                msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
//                            }
//                            System.out.println(msgData);
//                        } while (cursor.moveToNext());
//                    } else {
//                        System.out.println("empty inbox");
//                    }
//
////                    running ussd code
//                    String ussdCode = "*123"+Uri.encode("#");
//                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//
//                        return;
//                    }
//                    mainActivity.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussdCode)));


//                    ------------------

//                    SmsManager smsManager = SmsManager.getDefault();
//                    smsManager.sendTextMessage("01521208815", null, "walllaa", null, null);
//                    Thread.sleep(TimeUnit.SECONDS.toMillis(1));
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

                    dialog.show();
                }
            });

        return view;
    }

    public class Helper {
        TextView textView;
        TextView no;
        Button addFnF;
//        ImageView imageView;
    }
}
