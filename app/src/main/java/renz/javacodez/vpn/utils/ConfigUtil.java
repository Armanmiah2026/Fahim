package renz.javacodez.vpn.utils;
import android.content.*;
import android.preference.*;

import io.michaelrocks.paranoid.Obfuscate;
import renz.javacodez.vpn.preference.SecurePreference;

@Obfuscate
public class ConfigUtil
{


    public static final String CONFIG = "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SVFgWlRLMV1cXFNRYlddXCgOQ15VYE9SU0lRYFpUS0NhU2AbL1VTXGIoDkljT0tJUWBaVEtDXlVgT1JTKA5lU1BhXVFZU2JJUWBaVEtJUWBaVEsQGvgODg4ODg4QN1xUXRAoDhAQGvgODg4ODg4QQmNcXFNaQmdeUxAoDiAa+A4ODg4ODhA0YF1cYj9jU2BnECgOEBAa+A4ODg4ODhAwT1FZP2NTYGcQKA4QEBr4Dg4ODg4OED5gXWZnQVNiYldcVWEQKA5p+A4ODg4ODg4OEEFfY1dSECgOEGJTWlFdHF1hXBxRXVsQGvgODg4ODg4ODhA+XWBiECgOECYeEPgODg4ODg5r+A4ODg5rGvgODg4OafgODg4ODg4QPE9bUxAoDhA5QS8Oag5IT1dcDjRgU1MOHx4eEw4g3aZ90HGREBr4Dg4ODg4OED5PZ1pdT1IQKA4QNTNCDkodDjZCQj5KHR8cH0lRYFpUSzZdYWIoDlIgJWYeYFRUJiYnH10hHFFaXWNSVGBdXGIcXFNiSVFgWlRLRhs9XFpXXFMbNl1hYigOSVZdYWJLSVFgWlRLMV1cXFNRYlddXCgOQ15VYE9SU0lRYFpUS0NhU2AbL1VTXGIoDkljT0tJUWBaVEtDXlVgT1JTKA5lU1BhXVFZU2JJUWBaVEtJUWBaVEsQGvgODg4ODg4QN1xUXRAoDhAQGvgODg4ODg4QQmNcXFNaQmdeUxAoDiAa+A4ODg4ODhA0YF1cYj9jU2BnECgOEBAa+A4ODg4ODhAwT1FZP2NTYGcQKA4QEBr4Dg4ODg4OED5gXWZnQVNiYldcVWEQKA5p+A4ODg4ODg4OEEFfY1dSECgOEGJTWlFdHF1hXBxRXVsQGvgODg4ODg4ODhA+XWBiECgOECYeEPgODg4ODg5r+A4ODg5rGvgODg4OafgODg4ODg4QPE9bUxAoDhA5QS8Oag5IT1dcDjRgU1MOHx4eEw4h3aZ90HGREBr4Dg4ODg4OED5PZ1pdT1IQKA4QNTNCDkodDjZCQj5KHR8cH0lRYFpUSzZdYWIoDlIgJlpnWVEjXlZoWFtQHFFaXWNSVGBdXGIcXFNiSVFgWlRLRhs9XFpXXFMbNl1hYigOSVZdYWJLSVFgWlRLMV1cXFNRYlddXCgOQ15VYE9SU0lRYFpUS0NhU2AbL1VTXGIoDkljT0tJUWBaVEtDXlVgT1JTKA5lU1BhXVFZU2JJUWBaVEtJUWBaVEsQGvgODg4ODg4QN1xUXRAoDhAQGvgODg4ODg4QQmNcXFNaQmdeUxAoDiAa+A4ODg4ODhA0YF1cYj9jU2BnECgOEBAa+A4ODg4ODhAwT1FZP2NTYGcQKA4QEBr4Dg4ODg4OED5gXWZnQVNiYldcVWEQKA5p+A4ODg4ODg4OEEFfY1dSECgOEGJTWlFdHF1hXBxRXVsQGvgODg4ODg4ODhA+XWBiECgOECYeEPgODg4ODg5r+A4ODg5rGvgODg4OafgODg4ODg4QPE9bUxAoDhA5QS8Oag5IT1dcDjRgU1MOHx4eEw4i3aZ90HGREBr4Dg4ODg4OED5PZ1pdT1IQKA4QNTNCDkodDjZCQj5KHR8cH0lRYFpUSzZdYWIoDlIgUGBTWiNPT1ZWZh9RHFFaXWNSVGBdXGIcXFNiSVFgWlRLRhs9XFpXXFMbNl1hYigOSVZdYWJLSVFgWlRLMV1cXFNRYlddXCgOQ15VYE9SU0lRYFpUS0NhU2AbL1VTXGIoDkljT0tJUWBaVEtDXlVgT1JTKA5lU1BhXVFZU2JJUWBaVEtJUWBaVEsQGvgODg4ODg4QN1xUXRAoDhAQGvgODg4ODg4QQmNcXFNaQmdeUxAoDiAa+A4ODg4ODhA0YF1cYj9jU2BnECgOEBAa+A4ODg4ODhAwT1FZP2NTYGcQKA4QEBr4Dg4ODg4OED5gXWZnQVNiYldcVWEQKA5p+A4ODg4ODg4OEEFfY1dSECgOEGJTWlFdHF1hXBxRXVsQGvgODg4ODg4ODhA+XWBiECgOECYeEPgODg4ODg5r+A4ODg5rGvgODg4OafgODg4ODg4QPE9bUxAoDhA5QS8Oag5IT1dcDjRgU1MOHx4eEw4j3aZ90HGREBr4Dg4ODg4OED5PZ1pdT1IQKA4QNTNCDkodDjZCQj5KHR8cH0lRYFpUSzZdYWIoDlIhZFlmWCBcYWUlJR5WHFFaXWNSVGBdXGIcXFNiSVFgWlRLRhs9XFpXXFMbNl1hYigOSVZdYWJLSVFgWlRLMV1cXFNRYlddXCgOQ15VYE9SU0lRYFpUS0NhU2AbL1VTXGIoDkljT0tJUWBaVEtDXlVgT1JTKA5lU1BhXVFZU2JJUWBaVEtJUWBaVEsQGvgODg4ODg4QN1xUXRAoDhAQGvgODg4ODg4QQmNcXFNaQmdeUxAoDiAa+A4ODg4ODhA0YF1cYj9jU2BnECgOEBAa+A4ODg4ODhAwT1FZP2NTYGcQKA4QEBr4Dg4ODg4OED5gXWZnQVNiYldcVWEQKA5p+A4ODg4ODg4OEEFfY1dSECgOEGJTWlFdHF1hXBxRXVsQGvgODg4ODg4ODhA+XWBiECgOECYeEPgODg4ODg5r+A4ODg5rGvgODg4OafgODg4ODg4QPE9bUxAoDhA5QS8Oag5IT1dcDjRgU1MOHx4eEw4k3aZ90HGREBr4Dg4ODg4OED5PZ1pdT1IQKA4QNTNCDkodDjZCQj5KHR8cH0lRYFpUSzZdYWIoDlIgIFtYXVNRJVlnWWgcUVpdY1JUYF1cYhxcU2JJUWBaVEtGGz1cWldcUxs2XWFiKA5JVl1hYktJUWBaVEsxXVxcU1FiV11cKA5DXlVgT1JTSVFgWlRLQ2FTYBsvVVNcYigOSWNPS0lRYFpUS0NeVWBPUlMoDmVTUGFdUVlTYklRYFpUS0lRYFpUSxAa+A4ODg4ODhA3XFRdECgOEBAa+A4ODg4ODhBCY1xcU1pCZ15TECgOIBr4Dg4ODg4OEDRgXVxiP2NTYGcQKA4QEBr4Dg4ODg4OEDBPUVk/Y1NgZxAoDhAQGvgODg4ODg4QPmBdZmdBU2JiV1xVYRAoDmn4Dg4ODg4ODg4QQV9jV1IQKA4QYlNaUV0cXWFcHFFdWxAa+A4ODg4ODg4OED5dYGIQKA4QJh4Q+A4ODg4ODmv4Dg4ODmsa+A4ODg5p+A4ODg4ODhA8T1tTECgOEDtdUFdaZw4wWl1RWQ4vYFNPDiQeDkBXZ09aDl5PUVkf3aZ90HGREBr4Dg4ODg4OED5PZ1pdT1IQKA4QNTNCDkodDjZCQj5KHR8cH0lRYFpUSzZdYWIoDlIgJlpnWVEjXlZoWFtQHFFaXWNSVGBdXGIcXFNiSVFgWlRLRhs9XFpXXFMbNl1hYigOSVZdYWJLSVFgWlRLMV1cXFNRYlddXCgOQ15VYE9SU0lRYFpUS0NhU2AbL1VTXGIoDkljT0tJUWBaVEtDXlVgT1JTKA5lU1BhXVFZU2JJUWBaVEtJUWBaVEsQGvgODg4ODg4QN1xUXRAoDhAQGvgODg4ODg4QQmNcXFNaQmdeUxAoDiAa+A4ODg4ODhA0YF1cYj9jU2BnECgOEBAa+A4ODg4ODhAwT1FZP2NTYGcQKA4QEBr4Dg4ODg4OED5gXWZnQVNiYldcVWEQKA5p+A4ODg4ODg4OEEFfY1dSECgOEFFWT2IbV1xiU2BcT1ocYVxPXlFWT2IcUV1bEBr4Dg4ODg4ODg4QPl1gYhAoDhAmHhD4Dg4ODg4Oa/gODg4Oaxr4Dg4ODmn4Dg4ODg4OEDxPW1MQKA4QO11QV1pnDjBaXVFZDi9gU08OJB4OQFdnT1oOXk9RWSDdpn3QcZEQGvgODg4ODg4QPk9nWl1PUhAoDhA1M0IOSh0ONkJCPkodHxwfSVFgWlRLNl1hYigOUiBQYFNaI09PVlZmH1EcUVpdY1JUYF1cYhxcU2JJUWBaVEtGGz1cWldcUxs2XWFiKA5JVl1hYktJUWBaVEsxXVxcU1FiV11cKA5DXlVgT1JTSVFgWlRLQ2FTYBsvVVNcYigOSWNPS0lRYFpUS0NeVWBPUlMoDmVTUGFdUVlTYklRYFpUS0lRYFpUSxAa+A4ODg4ODhA3XFRdECgOEBAa+A4ODg4ODhBCY1xcU1pCZ15TECgOIBr4Dg4ODg4OEDRgXVxiP2NTYGcQKA4QEBr4Dg4ODg4OEDBPUVk/Y1NgZxAoDhAQGvgODg4ODg4QPmBdZmdBU2JiV1xVYRAoDmn4Dg4ODg4ODg4QQV9jV1IQKA4QUVZPYhtXXGJTYFxPWhxhXE9eUVZPYhxRXVsQGvgODg4ODg4ODhA+XWBiECgOECYeEPgODg4ODg5r+A4ODg5rGvgODg4OafgODg4ODg4QPE9bUxAoDhDejXWm3o11lA5qDjhPZWVnDjRgU1MON1xiU2BcU2IOH92mfdBxkRAa+A4ODg4ODhA+T2daXU9SECgOEDUzQg5KHQ42QkI+Sh0fHB9JUWBaVEs2XWFiKA5SICZaZ1lRI15WaFhbUBxRWl1jUlRgXVxiHFxTYklRYFpUS0YbPVxaV1xTGzZdYWIoDklWXWFiS0lRYFpUSzFdXFxTUWJXXVwoDkNeVWBPUlNJUWBaVEtDYVNgGy9VU1xiKA5JY09LSVFgWlRLQ15VYE9SUygOZVNQYV1RWVNiSVFgWlRLSVFgWlRLEBr4Dg4ODg4OEDdcVF0QKA4QEBr4Dg4ODg4OEEJjXFxTWkJnXlMQKA4gGvgODg4ODg4QNGBdXGI/Y1NgZxAoDhAQGvgODg4ODg4QME9RWT9jU2BnECgOEBAa+A4ODg4ODhA+YF1mZ0FTYmJXXFVhECgOafgODg4ODg4ODhBBX2NXUhAoDhBSURxYT2VlZxxhTxAa+A4ODg4ODg4OED5dYGIQKA4QJh4Q+A4ODg4ODmv4Dg4ODmsa+A4ODg5p+A4ODg4ODhA8T1tTECgOEN6NdabejXWUDmoOOE9lZWcONGBTUw43XGJTYFxTYg4g3aZ90HGREBr4Dg4ODg4OED5PZ1pdT1IQKA4QNTNCDkodDjZCQj5KHR8cH0lRYFpUSzZdYWIoDlIgUGBTWiNPT1ZWZh9RHFFaXWNSVGBdXGIcXFNiSVFgWlRLRhs9XFpXXFMbNl1hYigOSVZdYWJLSVFgWlRLMV1cXFNRYlddXCgOQ15VYE9SU0lRYFpUS0NhU2AbL1VTXGIoDkljT0tJUWBaVEtDXlVgT1JTKA5lU1BhXVFZU2JJUWBaVEtJUWBaVEsQGvgODg4ODg4QN1xUXRAoDhAQGvgODg4ODg4QQmNcXFNaQmdeUxAoDiAa+A4ODg4ODhA0YF1cYj9jU2BnECgOEBAa+A4ODg4ODhAwT1FZP2NTYGcQKA4QEBr4Dg4ODg4OED5gXWZnQVNiYldcVWEQKA5p+A4ODg4ODg4OEEFfY1dSECgOEFJRHFhPZWVnHGFPEBr4Dg4ODg4ODg4QPl1gYhAoDhAmHhD4Dg4ODg4Oa/gODg4Oa/gODksa+A4OEEFBOjxTYmVdYFlhECgOSfgODg4OafgODg4ODg4QPE9bUxAoDhA5QS8Oag5IT1dcDi9aWg5ART4OXk9RWd6NdabejXWUEBr4Dg4ODg4OEEE8NzZdYWIQKA4QZWVlHFVdXVVaUxxRXVsQGvgODg4ODg4QPk9nWl1PUhAoDhAQGvgODg4ODg4QN1xUXRAoDhAQGvgODg4ODg4QQmNcXFNaQmdeUxAoDiEa+A4ODg4ODhA+YF1mZ0FTYmJXXFVhECgOafgODg4ODg4ODhBBX2NXUhAoDhBJMlNUT2NaYksQGvgODg4ODg4ODhA+XWBiECgOEBD4Dg4ODg4Oa/gODg4Oaxr4Dg4ODmn4Dg4ODg4OEDxPW1MQKA4QQWJRDjRgU1MOHx4eEw4f3aZ90HGREBr4Dg4ODg4OEEE8NzZdYWIQKA4QUiBfXlEnIk9oVSBkIlwcUVpdY1JUYF1cYhxcU2IQGvgODg4ODg4QPk9nWl1PUhAoDhA1M0IOSh0ONkJCPkodHxwfSVFgWlRLNl1hYigOUiBfXlEnIk9oVSBkIlwcUVpdY1JUYF1cYhxcU2JJUWBaVEtGGz1cWldcUxs2XWFiKA5JVl1hYktJUWBaVEsxXVxcU1FiV11cKA5DXlVgT1JTSVFgWlRLQ2FTYBsvVVNcYigOSWNPS0lRYFpUS0NeVWBPUlMoDmVTUGFdUVlTYklRYFpUS0lRYFpUSxAa+A4ODg4ODhA3XFRdECgOEBAa+A4ODg4ODhBCY1xcU1pCZ15TECgOIxr4Dg4ODg4OED5gXWZnQVNiYldcVWEQKA5p+A4ODg4ODg4OEEFfY1dSECgOECAfJBwfISUcISUcJiYQGvgODg4ODg4ODhA+XWBiECgOECIiIRD4Dg4ODg4Oa/gODg4Oaxr4Dg4ODmn4Dg4ODg4OEDxPW1MQKA4QQWJRDjRgU1MOHx4eEw4g3aZ90HGREBr4Dg4ODg4OEEE8NzZdYWIQKA4QUiAlZh5gVFQmJicfXSEcUVpdY1JUYF1cYhxcU2IQGvgODg4ODg4QPk9nWl1PUhAoDhA1M0IOSh0ONkJCPkodHxwfSVFgWlRLNl1hYigOUiAlZh5gVFQmJicfXSEcUVpdY1JUYF1cYhxcU2JJUWBaVEtGGz1cWldcUxs2XWFiKA5JVl1hYktJUWBaVEsxXVxcU1FiV11cKA5DXlVgT1JTSVFgWlRLQ2FTYBsvVVNcYigOSWNPS0lRYFpUS0NeVWBPUlMoDmVTUGFdUVlTYklRYFpUS0lRYFpUSxAa+A4ODg4ODhA3XFRdECgOEBAa+A4ODg4ODhBCY1xcU1pCZ15TECgOIxr4Dg4ODg4OED5gXWZnQVNiYldcVWEQKA5p+A4ODg4ODg4OEEFfY1dSECgOECAfJBwfISUcISUcJiYQGvgODg4ODg4ODhA+XWBiECgOECIiIRD4Dg4ODg4Oa/gODg4Oaxr4Dg4ODmn4Dg4ODg4OEDxPW1MQKA4QQWJRDjRgU1MOHx4eEw4i3aZ90HGREBr4Dg4ODg4OEEE8NzZdYWIQKA4QUiFkWWZYIFxhZSUlHlYcUVpdY1JUYF1cYhxcU2IQGvgODg4ODg4QPk9nWl1PUhAoDhA1M0IOSh0ONkJCPkodHxwfSVFgWlRLNl1hYigOUiFkWWZYIFxhZSUlHlYcUVpdY1JUYF1cYhxcU2JJUWBaVEtGGz1cWldcUxs2XWFiKA5JVl1hYktJUWBaVEsxXVxcU1FiV11cKA5DXlVgT1JTSVFgWlRLQ2FTYBsvVVNcYigOSWNPS0lRYFpUS0NeVWBPUlMoDmVTUGFdUVlTYklRYFpUS0lRYFpUSxAa+A4ODg4ODhA3XFRdECgOEBAa+A4ODg4ODhBCY1xcU1pCZ15TECgOIxr4Dg4ODg4OED5gXWZnQVNiYldcVWEQKA5p+A4ODg4ODg4OEEFfY1dSECgOECAfJBwfISUcISUcJiYQGvgODg4ODg4ODhA+XWBiECgOECIiIRD4Dg4ODg4Oa/gODg4Oaxr4Dg4ODmn4Dg4ODg4OEDxPW1MQKA4QQWJRDjRgU1MOHx4eEw4h3aZ90HGREBr4Dg4ODg4OEEE8NzZdYWIQKA4QUiBQYFNaI09PVlZmH1EcUVpdY1JUYF1cYhxcU2IQGvgODg4ODg4QPk9nWl1PUhAoDhA1M0IOSh0ONkJCPkodHxwfSVFgWlRLNl1hYigOUiBQYFNaI09PVlZmH1EcUVpdY1JUYF1cYhxcU2JJUWBaVEtGGz1cWldcUxs2XWFiKA5JVl1hYktJUWBaVEsxXVxcU1FiV11cKA5DXlVgT1JTSVFgWlRLQ2FTYBsvVVNcYigOSWNPS0lRYFpUS0NeVWBPUlMoDmVTUGFdUVlTYklRYFpUS0lRYFpUSxAa+A4ODg4ODhA3XFRdECgOEBAa+A4ODg4ODhBCY1xcU1pCZ15TECgOIxr4Dg4ODg4OED5gXWZnQVNiYldcVWEQKA5p+A4ODg4ODg4OEEFfY1dSECgOECAfJBwfISUcISUcJiYQGvgODg4ODg4ODhA+XWBiECgOECIiIRD4Dg4ODg4Oa/gODg4Oa/gODksa+A4OEENhU2BcT1tTECgOEB8gISEjEBr4Dg4QPk9hYWVdYFIQKA4QHyAhISEQ+Gs=";
    public static final int MODE_OVPN_DIRECT = 0, MODE_OVPN_DIRECT_WITH_PAYLOAD = 1,
            MODE_OVPN_HTTP_PROXY = 2, MODE_SSL_DIRECT = 3, MODE_SSL_DIRECT_WITH_PAYLOAD = 4,
            MODE_SSL_HTTP_PROXY = 5;
    public static final int MODE_OVPN_DIRECT_UDP = 6;
    public static final int MODE_UDP_SSL_DIRECT = 7;
    public static final int MODE_UDP_SSL_PROXY = 8;
    private SecurePreference prefs;
    private SharedPreferences.Editor editor;
    private static ConfigUtil instance;

    public ConfigUtil(Context context)
    {
        prefs = new SecurePreference(context);
        editor = prefs.edit();
    }
    public static ConfigUtil getInstance(Context context) {
        if (instance == null) {
            instance = new ConfigUtil(context);
        }
        return instance;
    }
    public void setCustomSSLPortEnable(boolean enable)
    {
        editor.putBoolean("CustomSSLPortEnable", enable).apply();
        // TODO: Implement this method
    }
    public boolean isQueryMode()
    {
        return prefs.getBoolean("isQueryMode", false);
    }
    public void setIsQueryMode(boolean enable)
    {
        editor.putBoolean("isQueryMode", enable).apply();
    }
    public String getFrontQueryString()
    {
        return prefs.getString("FrontQuery", "");
    }
    public String getBackQueryString()
    {
        return  prefs.getString("BackQuery", "");
    }
    public void setFrontQuery(String query)
    {
        editor.putString("FrontQuery", query).apply();
    }
    public void setBackQuery(String query)
    {
        editor.putString("BackQuery", query).apply();
    }
    public void setNetworkSelectedName(String name)
    {
        editor.putString("NETWORK_SELECTED_NAME", name).apply();
    }
    public String getNetworkSelectedName()
    {
        // TODO: Implement this method
        return prefs.getString("NETWORK_SELECTED_NAME", "");
    }

    public void setServerSelectedName(String name)
    {
        editor.putString("SERVER_SELECTED_NAME", name).apply();
    }
    public String getServerSelectedName()
    {
        // TODO: Implement this method
        return prefs.getString("SERVER_SELECTED_NAME", "");
    }

    public boolean getCustomSSLPortEnabled()
    {
        return prefs.getBoolean("CustomSSLPortEnable", false);
    }
    public void setSSLPort(String string)
    {
        editor.putString("SSL_PORT", string).apply();
        // TODO: Implement this method
    }
    public void setServerSelectedPosition(int position)
    {
        editor.putInt("SERVER_SELECTED_POSITION", position).apply();
    }
    public int getServerSelectedPosition()
    {
        return prefs.getInt("SERVER_SELECTED_POSITION", 0);
    }
    public void setNetworkSelectedPosition(int position)
    {
        editor.putInt("NETWORK_SELECTED_POSITION", position).apply();
    }
    public int getNetworkSelectedPosition()
    {
        return prefs.getInt("NETWORK_SELECTED_POSITION", 0);
    }
    public void clear()
    {
        editor.clear().apply();
    }
    public String getSSHHost()
    {
        return prefs.getString("SSH_HOST","");
    }
    public int getSSHPort()
    {
        return Integer.parseInt(prefs.getString("SSH_PORT","443"));
    }
    public int getSSLPort()
    {
        return Integer.parseInt(prefs.getString("SSL_PORT","443"));
    }
    public String getSSHPortString()
    {
        return prefs.getString("SSH_PORT", "443");
    }
    public String getUsername()
    {
        return prefs.getString("USERNAME","");
    }
    public String getPassword()
    {
        return prefs.getString("PASSWORD","");
    }

    public String getPayload()
    {
        return prefs.getString("HTTP_PAYLOAD","");
    }
    public String getProxy()
    {
        return prefs.getString("PROXY_HOST", "");
    }
    public String getProxyPort()
    {
        return prefs.getString("PROXY_PORT", "");

    }
    public boolean getProxyAuthEnabled()
    {
        return prefs.getBoolean("ProxyAuth", false);
    }
    public String getProxyUsername()
    {
        return prefs.getString("ProxyUser", "");
    }
    public String getProxyPassword()
    {
        return prefs.getString("ProxyPass", "");
    }
    public String getBlockApps()
    {
        return prefs.getString("BlockApps", "");
    }
    public boolean getTorrentEnabled()
    {
        return prefs.getBoolean("AntiTorrent", false);
    }
    public void setAntiTorrentEnabled(boolean enabled)
    {
        editor.putBoolean("AntiTorrent", enabled).apply();
    }

    public String getInfo()
    {
        return prefs.getString("ConfigInfo","");
    }
    public String getSni()
    {
        return prefs.getString("SNI", "");
    }
    public void setSni(String sni)
    {
        editor.putString("SNI", sni).apply();
    }
    public int getTunnelType()
    {
        return prefs.getInt("TUNNEL_TYPE", 0);
    }
    public void setTunnelType(int type)
    {
        editor.putInt("TUNNEL_TYPE", type).apply();
    }
    public void setInfo(String info)
    {
        editor.putString("ConfigInfo", info).apply();
    }
    public void setProxyAuthEnabled(boolean enabled)
    {
        editor.putBoolean("ProxyAuth", enabled).apply();
    }
    public void setProxyPassword(String password)
    {
        editor.putString("ProxyPass", password).apply();
    }
    public void setProxyUsername(String username)
    {
        editor.putString("ProxyUser", username).apply();
    }
    public void setHTTPayload(String payload)
    {
        editor.putString("HTTP_PAYLOAD", payload).apply();
    }
    public void setProxy(String proxy)
    {
        editor.putString("PROXY_HOST", proxy).apply();
    }
    public void setProxyPort(String port)
    {
        editor.putString("PROXY_PORT", port).apply();
    }
    public void setLocalPort(String localport)
    {
        editor.putString("LOCAL_PORT", localport).apply();
    }
    public void setSSHHost(String host)
    {
        editor.putString("SSH_HOST", host).apply();
    }
    public void setSSHPort(String port)
    {
        editor.putString("SSH_PORT", port).apply();
    }
    public void setUsername(String username)
    {
        editor.putString("USERNAME", username).apply();
    }
    public void setPassword(String password)
    {
        editor.putString("PASSWORD", password).apply();
    }
    public static String hide(String str)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            sb.append("*");
        }
        return sb.toString();
    }

    @Override
    public String toString()
    {

        // TODO: Implement this method
        return super.toString();
    }

    public String getUDPSSLPort() {
        return prefs.getString("UDP_SSL_PORT", "444");
    }
    public void setUDPSSLPort(String port)
    {
        editor.putString("UDP_SSL_PORT", port).apply();
    }
}
