package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CustomHandler extends Handler {

    Context context;
    MainActivity mainActivity;
    Dialog dialog;


    TextView packageName;
    TextView superfnf;
    ListView fnfList;

    CustomHandler(Context context,Dialog dialog,TextView packageName,TextView superfnf,ListView fnfList){
        this.context = context;
        this.dialog = dialog;

        this.fnfList = fnfList;
        this.packageName = packageName;
        this.superfnf = superfnf;
    }

    @Override
    public void handleMessage(Message msg) {
                Helper helper = new PackageAnalyser(context).analysePackage();
                packageName.setText(helper.packageName);
                superfnf.setText(helper.superFnf);

                CustomAdapter adapter = new CustomAdapter(mainActivity,helper.fnf);

//                ArrayAdapter<String> adapters = new ArrayAdapter<String>(context, R.layout.list, R.id.list_text, helper.fnf);
                fnfList.setAdapter(adapter);

                dialog.dismiss();
    }
}
