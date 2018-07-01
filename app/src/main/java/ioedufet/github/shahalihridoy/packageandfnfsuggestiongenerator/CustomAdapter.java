package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
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
    //    int[] sign;
    public static LayoutInflater inflater = null;

    public CustomAdapter(MainActivity mainActivity, List<String> fnf) {
        this.context = mainActivity;
        this.mainActivity = mainActivity;
        this.fnf = fnf;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return fnf.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return fnf.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Helper helper = new Helper();
        View view = inflater.inflate(R.layout.list, null);

//        helper.imageView = (ImageView) view.findViewById(R.id.list_image);
        helper.no = (TextView) view.findViewById(R.id.no);
        helper.textView = (TextView) view.findViewById(R.id.list_text);
        helper.addFnF = (Button) view.findViewById(R.id.addFnF);

//        helper.imageView.setImageResource(sign[position]);
        helper.no.setText(Integer.toString(position + 1));
        helper.no.append(".");
        helper.textView.setText(fnf.get(position));

        helper.addFnF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    sending sms
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + 4444));
                    intent.putExtra("sms_body", "P");
                    mainActivity.startActivity(intent);


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
                System.out.println("you clicked on "+fnf.get(position));
            }
        });

        return view;
    }

    public class Helper{
        TextView textView;
        TextView no;
        Button addFnF;
//        ImageView imageView;
    }
}
