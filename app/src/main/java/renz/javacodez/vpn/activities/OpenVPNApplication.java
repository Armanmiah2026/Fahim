package renz.javacodez.vpn.activities;

import android.app.Application;
import android.content.Context;
import renz.javacodez.vpn.interfaces.*;

public class OpenVPNApplication extends Application {
    public static Context context;
	private static OnByteCountListener mOnByCountListener;
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
	public static Context getContext() {
		return context;
	}
	public static void setByteCountListener(OnByteCountListener OnByCountListener)
	{
		mOnByCountListener = OnByCountListener;
	}
	public static void updateByteCount(long in, long out)
	{
		mOnByCountListener.onByteCount(in, out);
	}
    public static String resString(int res_id) {
        return context.getString(res_id);
    }
}
