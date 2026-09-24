package renz.javacodez.vpn.utils;

import android.content.*;
import java.io.*;
import org.json.*;
import java.lang.reflect.*;
import android.util.*;
import android.app.*;
import android.content.pm.*;

import renz.javacodez.vpn.activities.*;
import app.dev.shapla.vpn.R;
import java.util.*;
import java.net.*;
import com.google.android.material.bottomnavigation.*;

public class Utils
{
	public static final String GOOGLE_SIGN = "3082039830820280a00302010202047ee941e1300d06092a864886f70d010105050030818c310b30090603550406130255533116301406035504080c0d556e69746564205374617465733113301106035504070c0a43616c69666f726e69613116301406035504090c0d4d6f756e7461696e205669657731143012060355040a0c0b476f6f676c6520496e632e3110300e060355040b0c07416e64726f69643110300e06035504030c07416e64726f69643020170d3231313030373039323635315a180f32303531313132393039323635315a30818c310b30090603550406130255533116301406035504080c0d556e69746564205374617465733113301106035504070c0a43616c69666f726e69613116301406035504090c0d4d6f756e7461696e205669657731143012060355040a0c0b476f6f676c6520496e632e3110300e060355040b0c07416e64726f69643110300e06035504030c07416e64726f696430820122300d06092a864886f70d01010105000382010f003082010a0282010100a0613be6bcb6d54aa62e25e3e023c00fbae69a409ee2c1b61e88490de15d90560623c6f33e492ed245246b8441529659e0c811752fba96014021b3807da11a773ca3799fac2e9211f2f7a4c69183198c2b89e32cb3333de58abb98357dbdc606b6f26437970cc5ccc03b9b05b052ae6d3ae1c28d94956e55381f852f5640c63c2d6bee2216b7c1803af3237f68e1fe1e78d9f6f25e11cf83d6b6076ec485760358ae072dd6602d971f8c6c4d611b5b9cc87a029e025a487eb66f2751fedd5dc30849a72fbf11d9ff4ba27295f4592287dfb965fae542ccb5769579b9a6947e2991321e8fbdbe14d200401b30ae1bb11b64aec56208fd90837efd5ea9cf4449270203010001300d06092a864886f70d01010505000382010100776bb34e11b65b4d39d565b75ba78958cd91ee34928423057f4259c5ab4de04d5b224e910008cc04e73e6af51616781e6eae61ea34e1b4cfca2fa47eee2e81a73eb949fdf898d1e7fa319b455ba0a2b7701338238ad4c9a8b49e31725f22bdec61227b8a5e1fa4165796b445aca2c24b4545b09bc57f2cb7a8b54c8b0919c194c617d6884ca3e6117c5d5a0d896eab7f27f62289a3f7e73dc4836c1f146fd5e3d1ae312ad1f3c24c607f8ddc409684b30efed5a1531e4e44bec376be51b78c1fc51fd625a1017ea9dfaa034d74dd97e95e87d4a010e720895ece4a47ac853a7bb8bd1e02910191258cc49b9a73f962becdcf6f2993c92cd81f9da3d10ec32894";
	
	public static String getIPAddress(boolean useIPv4)
	{
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4) 
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "127.0.0.1";
    }
	
	public static String getAppBuildVersion(OpenVPNClient context)
	{
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return String.format("©%s 2021 - v%s (Build %s)", context.getString(R.string.app), info.versionName, String.valueOf(info.versionCode));
		} catch (Exception e) {
			
		}
		// TODO: Implement this method
		return null;
	}
	public static void checkSign(Context context)
	{
		try {
			if (!getAppSignature(context).equals(Utils.GOOGLE_SIGN)) {
				((Activity)context).finish();
			}
		} catch (Exception e) {

		}
		// TODO: Implement this method
	}
	
	public static String getAppSignature(Context context) throws Exception
	{
		StringBuilder sb = new StringBuilder();
		PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
		for (Signature sign: info.signatures) {
			sb.append(sign.toCharsString());
		}
		return sb.toString();
	}
	public static void disableShiftMode(BottomNavigationView view) {
		BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
		try {
			Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
			shiftingMode.setAccessible(true);
			shiftingMode.setBoolean(menuView, false);
			shiftingMode.setAccessible(false);
			for (int i = 0; i < menuView.getChildCount(); i++) {
				BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
				//noinspection RestrictedApi
				//item.setShiftingMode(false);
				// set once again checked value, so view will be updated
				//noinspection RestrictedApi
				item.setChecked(item.getItemData().isChecked());
			}
		} catch (NoSuchFieldException e) {
			Log.e("BNVHelper", "Unable to get shift mode field", e);
		} catch (IllegalAccessException e) {
			Log.e("BNVHelper", "Unable to change value of shift mode", e);
		}
	} 
	public static String readFile(Context context, File file)
	{
		try {
			StringBuilder sb = new StringBuilder();
			Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			char[] buff = new char[1024];
			while (true) {
				int read = reader.read(buff, 0, buff.length);
				if (read <= 0) {
					break;
				}
				sb.append(buff, 0, read);
			}
			return sb.toString();
		} catch (Exception e) {
		}
		return null;
	}
	public static String getConfigVersion(Context context)
	{
		File file = new File(context.getFilesDir(), "Servers.js");
		if (file.exists()) {
			try {
				String str = readFile(context, file);
				JSONObject obj = new JSONObject(str);
				return obj.getString("Version");
			} catch (Exception e) {

			}
		} else {
			return "(Built-in)";
		}
		return "(Built-in)";
	}
	public static class Parser {

        public Parser() {
        }

        public static String encode(byte[] b) {
            if (b == null)
                return null;
			byte[] d = new byte[b.length];
			for (int i = 0; i < d.length; i++) {
				d[i] = (byte)(b[i] -18);
			}
            byte[] data = new byte[d.length + 2];
            System.arraycopy(d, 0, data, 0, d.length);
            byte[] dest = new byte[(data.length / 3) * 4];


            for (int sidx = 0, didx = 0; sidx < d.length; sidx += 3, didx += 4) {
                dest[didx] = (byte) ((data[sidx] >>> 2) & 077);
                dest[didx + 1] = (byte) ((data[sidx + 1] >>> 4) & 017 | (data[sidx] << 4) & 077);
                dest[didx + 2] = (byte) ((data[sidx + 2] >>> 6) & 003 | (data[sidx + 1] << 2) & 077);
                dest[didx + 3] = (byte) (data[sidx + 2] & 077);
            }


            for (int idx = 0; idx < dest.length; idx++) {
                if (dest[idx] < 26)
                    dest[idx] = (byte) (dest[idx] + 'A');
                else if (dest[idx] < 52)
                    dest[idx] = (byte) (dest[idx] + 'a' - 26);
                else if (dest[idx] < 62)
                    dest[idx] = (byte) (dest[idx] + '0' - 52);
                else if (dest[idx] < 63)
                    dest[idx] = (byte) '+';
                else
                    dest[idx] = (byte) '/';
            }


            for (int idx = dest.length - 1; idx > (d.length * 4) / 3; idx--) {
                dest[idx] = (byte) '=';
            }
            return new String(dest);
        }


        public static String encryptToString(String s) {

            return encode(s.getBytes());
        }

        public static String decryptString(String str) {
            if (str == null)
                return null;
			byte[] bytes = decode(str.getBytes());

			byte[] data = new byte[bytes.length];
			for (int i = 0; i < bytes.length; i++) {
				data[i] = (byte)(bytes[i] + 18);
			}

            return new String(data);
        }

        public static byte[] decode(byte[] data) {
            int tail = data.length;
            while (data[tail - 1] == '=')
                tail--;
            byte dest[] = new byte[tail - data.length / 4];

            for (int idx = 0; idx < data.length; idx++) {
                if (data[idx] == '=')
                    data[idx] = 0;
                else if (data[idx] == '/')
                    data[idx] = 63;
                else if (data[idx] == '+')
                    data[idx] = 62;
                else if (data[idx] >= '0' && data[idx] <= '9')
                    data[idx] = (byte) (data[idx] - ('0' - 52));
                else if (data[idx] >= 'a' && data[idx] <= 'z')
                    data[idx] = (byte) (data[idx] - ('a' - 26));
                else if (data[idx] >= 'A' && data[idx] <= 'Z')
                    data[idx] = (byte) (data[idx] - 'A');
            }
            int sidx, didx;
            for (sidx = 0, didx = 0; didx < dest.length - 2; sidx += 4, didx += 3) {
                dest[didx] = (byte) (((data[sidx] << 2) & 255) | ((data[sidx + 1] >>> 4) & 3));
                dest[didx + 1] = (byte) (((data[sidx + 1] << 4) & 255) | ((data[sidx + 2] >>> 2) & 017));
                dest[didx + 2] = (byte) (((data[sidx + 2] << 6) & 255) | (data[sidx + 3] & 077));
            }
            if (didx < dest.length) {
                dest[didx] = (byte) (((data[sidx] << 2) & 255) | ((data[sidx + 1] >>> 4) & 3));
            }
            if (++didx < dest.length) {
                dest[didx] = (byte) (((data[sidx + 1] << 4) & 255) | ((data[sidx + 2] >>> 2) & 017));
            }
            return dest;
        }
    }
}
