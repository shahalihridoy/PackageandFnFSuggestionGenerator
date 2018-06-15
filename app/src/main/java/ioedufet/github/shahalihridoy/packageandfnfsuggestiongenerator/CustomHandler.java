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
                PackageAnalyser.Helper helper = new PackageAnalyser(context).analysePackage();
                packageName.setText(helper.packageName);
                superfnf.setText(helper.superFnf);

                int size = helper.fnf.size();

                String[] fnf = new String[size];

                for(int i=0;i<size;i++){
                    fnf[i] = helper.fnf.get(i);
                }
                CustomAdapter adapter = new CustomAdapter(mainActivity,fnf);

//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.list, R.id.list_text, helper.fnf);
                fnfList.setAdapter(adapter);

                dialog.dismiss();
    }
}
