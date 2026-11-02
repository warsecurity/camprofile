package com.camprofile;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.*;

public class MainActivity extends Activity {
    @SuppressWarnings("deprecation")
    private SharedPreferences prefs() {
        try { return getSharedPreferences("profile", Context.MODE_WORLD_READABLE); }
        catch (Throwable t) {
            Toast.makeText(this, "Module not active in LSPosed yet", Toast.LENGTH_LONG).show();
            return getSharedPreferences("profile", Context.MODE_PRIVATE);
        }
    }

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        final SharedPreferences sp = prefs();
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(32, 48, 32, 32);


        final ListView list = new ListView(this);
        String[] names = new String[Profiles.ALL.length];
        for (int i = 0; i < names.length; i++) names[i] = Profiles.ALL[i][1] + "  |  " + Profiles.ALL[i][0];
        list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, names));
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list.setItemChecked(sp.getInt("idx", 2), true);
        root.addView(list, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));

        Button save = new Button(this);
        save.setText("Save");
        save.setOnClickListener(v -> {
            int idx = list.getCheckedItemPosition();
            if (idx < 0) idx = 2;
            sp.edit().putInt("idx", idx).apply();
            Toast.makeText(this, "Saved: " + Profiles.ALL[idx][0], Toast.LENGTH_SHORT).show();
        });
        root.addView(save);
        setContentView(root);
    }
}
