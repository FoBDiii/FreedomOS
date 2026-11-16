package org.freedomos.blocker;
import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import java.util.Collections;
public class Blocker extends AccessibilityService {
 @Override public void onAccessibilityEvent(AccessibilityEvent e) {
 if(e.getPackageName()==null || e.getEventType()!=AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return;
 String pkg=e.getPackageName().toString();
 if(pkg.equals(getPackageName())) return;
 android.content.SharedPreferences p=getSharedPreferences("rules",MODE_PRIVATE);
 if(p.getBoolean("enabled",false) && p.getStringSet("blocked",Collections.emptySet()).contains(pkg)) performGlobalAction(GLOBAL_ACTION_HOME);
 }
 @Override public void onInterrupt() {}
}