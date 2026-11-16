package org.freedomos.blocker;
import android.app.Activity;
import android.os.Bundle;
import android.content.*;
import android.content.pm.*;
import android.provider.Settings;
import android.widget.*;
import java.util.*;
public class MainActivity extends Activity {
 @Override public void onCreate(Bundle b) {super.onCreate(b);
 SharedPreferences p=getSharedPreferences("rules",MODE_PRIVATE);
 LinearLayout root=new LinearLayout(this); root.setOrientation(1); root.setPadding(24,24,24,24); setContentView(root);
 TextView title=new TextView(this); title.setText("FreedomOS — App blocker"); title.setTextSize(24); root.addView(title);
 Button settings=new Button(this); settings.setText("Enable accessibility service"); settings.setOnClickListener(v->startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))); root.addView(settings);
 Switch enabled=new Switch(this); enabled.setText("Enable blocking"); enabled.setChecked(p.getBoolean("enabled",false)); enabled.setOnCheckedChangeListener((v,c)->p.edit().putBoolean("enabled",c).apply()); root.addView(enabled);
 TextView hint=new TextView(this); hint.setText("Select distracting apps. Keep your launcher, phone and essential apps unblocked. You can disable blocking at any time in this prototype."); root.addView(hint);
 ScrollView scroll=new ScrollView(this); root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1)); LinearLayout list=new LinearLayout(this); list.setOrientation(1); scroll.addView(list);
 Intent launch=new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER);
 PackageManager pm=getPackageManager(); List<ResolveInfo> apps=pm.queryIntentActivities(launch,0); apps.sort((a,z)->a.loadLabel(pm).toString().compareToIgnoreCase(z.loadLabel(pm).toString()));
 Set<String> seen=new HashSet<>(); HashSet<String> selected=new HashSet<>(p.getStringSet("blocked",Collections.emptySet()));
 Intent home=new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME); Set<String> protectedApps=new HashSet<>(); for(ResolveInfo r:pm.queryIntentActivities(home,0)) protectedApps.add(r.activityInfo.packageName); protectedApps.add(getPackageName()); protectedApps.add("com.android.settings"); protectedApps.add("com.google.android.dialer"); protectedApps.add("com.android.dialer");
 for(ResolveInfo r:apps){String pkg=r.activityInfo.packageName; if(!seen.add(pkg)||protectedApps.contains(pkg))continue; CheckBox c=new CheckBox(this); c.setText(r.loadLabel(pm)+"\n"+pkg); c.setChecked(selected.contains(pkg)); c.setOnCheckedChangeListener((v,on)->{if(on) selected.add(pkg); else selected.remove(pkg); p.edit().putStringSet("blocked",new HashSet<>(selected)).apply();}); list.addView(c);}
 }
}