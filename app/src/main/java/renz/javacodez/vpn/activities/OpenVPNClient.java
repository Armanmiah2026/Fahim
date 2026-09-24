package renz.javacodez.vpn.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.VpnService;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import renz.javacodez.vpn.service.OpenVPNService.*;

import android.widget.*;
import java.io.*;
import renz.javacodez.vpn.core.*;
import org.json.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import renz.javacodez.vpn.adapter.Adapter;
import renz.javacodez.vpn.adapter.Adapter.*;
import renz.javacodez.vpn.service.*;
import renz.javacodez.vpn.utils.*;
import renz.javacodez.vpn.json.JsonManager.*;
import android.content.*;

import net.openvpn.openvpn.*;


import android.*;
import com.github.angads25.filepicker.model.*;
import com.github.angads25.filepicker.view.*;
import com.github.angads25.filepicker.controller.*;
import android.content.pm.*;
import app.dev.shapla.vpn.R;
import android.os.*;
import java.net.*;

import android.app.ActivityManager;

import renz.javacodez.vpn.view.*;

import android.provider.*;
import android.net.*;
import com.android.volley.toolbox.*;
import com.android.volley.*;
import android.graphics.*;
import me.wangyuwei.flipshare.*;

import androidx.core.content.*;
import androidx.core.app.*;
import com.google.android.material.bottomnavigation.*;
import androidx.appcompat.app.*;
import androidx.appcompat.widget.Toolbar;
import renz.javacodez.vpn.helper.*;

import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class OpenVPNClient extends OpenVPNClientBase implements ServerUpdate.OnUpdateListener, /*OnRequestPermissionsResultCallback, */OnClickListener, OnTouchListener, OnItemSelectedListener, OnEditorActionListener,ExpireDate.ExpireDateListener,
/*NetworkAdapter.OnNetworkSelectedListener,*/ RenzGenerator.GeneratorListener,
RadioGroup.OnCheckedChangeListener, BottomNavigationView.OnNavigationItemSelectedListener,
/*ServerAdapter.OnServerSelectedListener,*/ GeneratorHelper.GeneratorListener
{
    private static final int REQUEST_IMPORT_PKCS12 = 3;
    private static final int REQUEST_IMPORT_PROFILE = 2;
    private static final int REQUEST_VPN_ACTOR_RIGHTS = 1;
    private static final boolean RETAIN_AUTH = false;
    private static final int S_BIND_CALLED = 1;
    private static final int S_ONSTART_CALLED = 2;
    private static final String TAG = "OpenVPNClient";
    private static final int UIF_PROFILE_SETTING_FROM_SPINNER = 262144;
    private static final int UIF_REFLECTED = 131072;
    private static final int UIF_RESET = 65536;
    private static final boolean UI_OVERLOADED = false;
    private String autostart_profile_name;
    private View button_group;
    private TextView bytes_in_view;
    private TextView bytes_out_view;
    private TextView challenge_view;
    private View conn_details_group;
    private Button connect_button;
    private Button disconnect_button;
    private View cr_group;
    private FinishOnConnect delayed_finish_on_connect = FinishOnConnect.DISABLED;
    private TextView details_more_less;
    
    private TextView duration_view;
    private FinishOnConnect finish_on_connect = FinishOnConnect.DISABLED;
    private View info_group;
    private boolean last_active = RETAIN_AUTH;
    private TextView last_pkt_recv_view;
    private ScrollView main_scroll_view;
    private EditText password_edit;
    private View password_group;
    private CheckBox password_save_checkbox;
    private EditText pk_password_edit;
    private View pk_password_group;
    private CheckBox pk_password_save_checkbox;
    private View post_import_help_blurb;
    private PrefUtil prefs;
    private ImageButton profile_edit;
    private View profile_group;
    private Spinner profile_spin;
    private ProgressBar progress_bar;
    private ImageButton proxy_edit;
    private View proxy_group;
    private Spinner proxy_spin;
    private PasswordUtil pwds;
    private EditText response_edit;
    private View server_group;
    private Spinner server_spin;
    private int startup_state = 0;
    private View stats_expansion_group;
    private View stats_group;
    private Handler stats_timer_handler = new Handler();
    private Runnable stats_timer_task = new Runnable() {
        public void run() {
            OpenVPNClient.this.show_stats();
            OpenVPNClient.this.schedule_stats();
        }
    };
    private ImageView status_icon_view;
    private TextView status_view;
    private boolean stop_service_on_client_exit = RETAIN_AUTH;
    private View[] textgroups;
    private TextView[] textviews;
    private Handler ui_reset_timer_handler = new Handler();
    private Runnable ui_reset_timer_task = new Runnable() {
        public void run() {
            if (!OpenVPNClient.this.is_active()) {
                OpenVPNClient.this.ui_setup(OpenVPNClient.RETAIN_AUTH, OpenVPNClient.UIF_RESET, null);
            }
        }
    };
    private EditText username_edit;
    private View username_group;
    private ConfigUtil config;
    private boolean showNoUpdate;
    private EditText vpn_username, vpn_password;
    private ArrayList<JSONObject> listNetwork;
    public NetworkAdapter networkAdapter;
    public Adapter.ServerAdapter mServerAdapter;
    public ArrayList<String> listProfiles;
    private int mRandomServer;
    private Toolbar mToolbar;
    private EditText mCustomTweakEdit;
    private boolean autoUpdate;
    private Spinner network_spin;
    private enum FinishOnConnect {
        DISABLED,
        ENABLED,
        ENABLED_ACROSS_ONSTART,
        PENDING
    }

    private enum ProfileSource {
        UNDEF,
        SERVICE,
        PRIORITY,
        PREFERENCES,
        SPINNER,
        LIST0
    }
    private static final int REQUEST_OFFLINE_UPDATE = 99;
    public static String USERNAME = "VPN_USERNAME";
    public static String PASSWORD = "VPN_PASSWORD";
    public static String SELECTED_PROFILE = "SELECTED_PROFILE";
    public static String SELECTED_NETWORK = "SELECTED_NETWORK";
    private SharedPreferences myPrefs;
    private SharedPreferences.Editor editor;
    private CircleProgressBar progress;
    
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandlerUtils(this));
        Intent intent = getIntent();
        String str = TAG;
        Object[] objArr = new Object[S_BIND_CALLED];
        objArr[0] = intent.toString();
        Log.d(str, String.format("CLI: onCreate intent=%s", objArr));
        setContentView(R.layout.form);
        //setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }*/
        myPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = myPrefs.edit();
        this.prefs = new PrefUtil(PreferenceManager.getDefaultSharedPreferences(this));
        this.pwds = new PasswordUtil(PreferenceManager.getDefaultSharedPreferences(this));
        init_default_preferences(this.prefs);
        
        load_ui_elements();
        load();
        doBindService();
        dobindInjector();
        
    }

    private void load()
    {
        showNoUpdate = false;
        config = ConfigUtil.getInstance(this);
        
        //mServerLayout = findViewById(R.id.server_layout);

        progress = (CircleProgressBar) findViewById(R.id.custom_progressBar);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        
        listProfiles = new ArrayList<String>();
        mServerAdapter = new ServerAdapter(this, listProfiles);
        profile_spin.setAdapter(mServerAdapter);
        loadServers();

        listNetwork = new ArrayList<JSONObject>();
        network_spin = (Spinner) findViewById(R.id.networks);
        networkAdapter = new NetworkAdapter(this, listNetwork);
        network_spin.setAdapter(networkAdapter);
        loadNetworks();


        vpn_username = (EditText) findViewById(R.id.vpn_username);
        vpn_password = (EditText) findViewById(R.id.vpn_password);
        vpn_username.setText(config.getUsername());
        vpn_password.setText(config.getPassword());

       
        profile_spin.setSelection(getServerSelected());
        network_spin.setSelection(myPrefs.getInt(SELECTED_NETWORK,0));


        network_spin.setOnItemSelectedListener(this);
        findViewById(R.id.menu_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });
        autoUpdate = true;
        showNoUpdate = false;
        checkUpdates();
        //Utils.checkSign(this);
        AppRemote ar = new AppRemote(this);
        ar.setListener(new AppRemote.OnFinishListener() {

                @Override
                public void onFinish(boolean isDestroy, String message)
                {
                    if (isDestroy) {
                        showDialog(message);
                    }
                    // TODO: Implement this method
                }


            });
        try {
            ar.start();
        } catch (Exception e) {

        }
        
    }
    private void showMenu(View v) {
        PopupMenu menu = new PopupMenu(this, v);
        menu.inflate(R.menu.main_menu);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_update:
                        showNoUpdate = true;
                        checkUpdates();
                        break;
                    case R.id.menu_clear_date:
                        showClearDataDialog();
                        break;
                    case R.id.menu_custom_tweak:
                        showCustomTweakDialog();
                        break;
                    case R.id.menu_exit:
                        finish();
                        break;
                }
                return true;
            }
        });
        menu.show();
    }
    public void loadNetworks()
    {
        try {
            if (listNetwork.size() > 0) {
                listNetwork.clear();
            }

            JSONObject js = new JSONObject();
            js.put("Name", "UDP Direct");
            js.put("Info", "Direct UDP Connection");
            js.put("TunnelType", ConfigUtil.MODE_OVPN_DIRECT_UDP);
            listNetwork.add(js);
            JSONArray network = getNetworksArray();
            for (int i = 0; i < network.length(); i++) {
                listNetwork.add(network.getJSONObject(i));
            }
            JSONArray sslnetwork = getSSLNetworks();
            for (int i = 0; i < sslnetwork.length(); i++) {
                listNetwork.add(sslnetwork.getJSONObject(i));
            }
            //Collections.sort(listNetwork, NetworkNameComparator());
            networkAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            showToast(e.getMessage());
        }
        // TODO: Implement this method
    }

    
    private void showFlipShareView(View v)
    {
        FlipShareView share=  new FlipShareView.Builder(this, v)
            //.addItem(new ShareItem("Logout", Color.WHITE, 0xff43549C, BitmapFactory.decodeResource(getResources(), R.drawable.ic_user)))
            .addItem(new ShareItem("Check Update", Color.WHITE, 0xff009900, BitmapFactory.decodeResource(getResources(), R.drawable.ic_updates)))
            .addItem(new ShareItem("Clear data", Color.WHITE, 0xffD9392D, BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete)))
            .addItem(new ShareItem("Exit", Color.WHITE, 0xff57708A, BitmapFactory.decodeResource(getResources(), R.drawable.ic_exit)))
            //.addItem(new ShareItem("Logout", Color.WHITE, 0xff0280ff, BitmapFactory.decodeResource(getResources(), R.drawable.ic_power_on)))
            .setItemDuration(200)
            .setBackgroundColor(0x60000000)
            .setAnimType(FlipShareView.TYPE_HORIZONTAL)
            .create();

        share.setOnFlipClickListener(new FlipShareView.OnFlipClickListener() {

                @Override
                public void dismiss() {
                }


                @Override
                public void onItemClick(int n)
                {
                    switch (n) 
                    {

                        case 0:
                            {
                                showUpdateDialog();
                                return;

                            }
                        case 1:
                            showClearDataDialog();
                            break;
                        case 2:
                            finish();
                            break;

                    }
                }

            });


    }
    private String getConfigVersion()
    {
        String version = "Config Version: %s";
        // TODO: Implement this method
        try
        {
            return getJSONObject().getString("Version");
        }
        catch (JSONException e)
        {}
        return "1.0";
    }
    
    private CompoundButton.OnCheckedChangeListener OnCheckedChanged()
    {
        // TODO: Implement this method
        return new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton p1, boolean p2)
            {
                editor.putBoolean("RememberMe", p2).apply();
                // TODO: Implement this method
            }
            
            
        };
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem p1)
    {
        /*switch (p1.getItemId()) {
            case R.id.menu_offline_update:
                showOfflineUpdate();
                break;
            case R.id.menu_clear_data:
                showClearDataDialog();
                break;
            case R.id.menu_add_tweak:
                showCustomTweakDialog();
                break;
            
        }*/
        // TODO: Implement this method
        return true;
    }

    
    

    void showUpdateDialog()
    {
        DialogInterface.OnClickListener DialogListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2)
            {
                switch (p2) {
                    case DialogInterface.BUTTON_NEUTRAL:
                        showClearDataDialog();
                        break;
                    case DialogInterface.BUTTON_POSITIVE:
                        showNoUpdate = true;
                        autoUpdate = false;
                        showToast("Checking Updates");
                        checkUpdates();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        showOfflineUpdate();
                        break;
                }
                // TODO: Implement this method
            }
            

        };
        
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("Config Updater");
        dialog.setMessage("1.) Online Update - Requires internet connection.\n\n2.) Offline Update - Needs to import .rj or .js file.\n\n3.) Clear Data - Clear all saved configs.");
        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Clear Data", DialogListener);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Online", DialogListener);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Offline", DialogListener);
        dialog.show();
        
    }
    private void showAccountLogin()
    {
        View v = getLayoutInflater().inflate(R.layout.login_activity, null);
        final EditText mUsername = (EditText)v.findViewById(R.id.login_username);
        final EditText mPassword = (EditText)v.findViewById(R.id.login_password);
        Button mLoginBtn = (Button)v.findViewById(R.id.login_button);
        
        mUsername.setText(prefs.get_string(USERNAME));
        mPassword.setText(prefs.get_string(PASSWORD));
        
        final AlertDialog builder = new AlertDialog.Builder(this).create();
        builder.setView(v);
        
        mLoginBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View p1)
                {
                    String user = mUsername.getText().toString();
                    String pass = mPassword.getText().toString();
                    if (user.isEmpty() || pass.isEmpty()) {
                        showToast("Username or Password is empty");
                        return;
                    }
                    prefs.set_string(USERNAME, user);
                    prefs.set_string(PASSWORD, pass);
                    builder.dismiss();
                    // TODO: Implement this method
                }
                
            
        });
        builder.show();
        // TODO: Implement this method
    }
    
    private void showOfflineUpdate()
    {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(OpenVPNClient.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_OFFLINE_UPDATE);
        } else {
            showFilePicker();
        }
    }
    private void showCustomTweakDialog() {
        View v = getLayoutInflater().inflate(R.layout.custom_tweak_dialog, null);

        final EditText mCustomTweakName = (EditText) v.findViewById(R.id.custom_tweak_name);
        final Spinner mTweakMode = (Spinner) v.findViewById(R.id.custom_tweak_mode);
        final TextView mTweakTitle = (TextView) v.findViewById(R.id.tweak_mode_title);
        mCustomTweakEdit = (EditText) v.findViewById(R.id.custom_tweak_edit);
        final EditText mCustomTweakProxy = (EditText) v.findViewById(R.id.custom_tweak_proxy);
        final EditText mCustomTweakProxyPort = (EditText) v.findViewById(R.id.custom_tweak_proxy_port);
        final Switch mUseDefProxy = (Switch) v.findViewById(R.id.custom_tweak_default_proxy);
        final View mcustom_proxy_layout = v.findViewById(R.id.custom_tweak_proxy_layout);
        final ImageView mCustomTweakGenerate = (ImageView) v.findViewById(R.id.custom_tweak_generate);

        String[] tweaks = {"HTTP", "SSL"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, tweaks);
        mTweakMode.setAdapter(adapter);
        mTweakMode.setSelection(0, false);
        //mTweakMode.setSelection(myPrefs.getInt("CustomTweakMode", 0));
        mTweakMode.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                if (p3 == 1) {
                    mCustomTweakGenerate.setVisibility(View.GONE);
                    mTweakTitle.setText("SNI Host");
                    mCustomTweakEdit.setHint("Ex: m.google.com");
                    mcustom_proxy_layout.setVisibility(View.GONE);
                } else {
                    mCustomTweakGenerate.setVisibility(View.VISIBLE);
                    mTweakTitle.setText("Payload");
                    mCustomTweakEdit.setHint(getString(R.string.payload_hint));
                    mcustom_proxy_layout.setVisibility(View.VISIBLE);
                }
                editor.putInt("CustomTweakMode", p3).apply();
                // TODO: Implement this method
            }

            @Override
            public void onNothingSelected(AdapterView<?> p1) {
                // TODO: Implement this method
            }


        });


        mUseDefProxy.setChecked(myPrefs.getBoolean("UseDefProxy", false));
        if (myPrefs.getBoolean("UseDefProxy", false)) {
            mcustom_proxy_layout.setVisibility(View.GONE);
        } else {
            mcustom_proxy_layout.setVisibility(View.VISIBLE);
        }
        mUseDefProxy.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton p1, boolean p2) {
                if (p1.isChecked()) {
                    mcustom_proxy_layout.setVisibility(View.GONE);
                } else {
                    mcustom_proxy_layout.setVisibility(View.VISIBLE);
                }
                try {
                    JSONObject server = getServer();
                    if (p2) {
                        mCustomTweakProxy.setText("[Default]");
                        mCustomTweakProxyPort.setText("80");
                    } else {
                        mCustomTweakProxy.setText("");
                        mCustomTweakProxyPort.setText("");
                    }
                    editor.putBoolean("UseDefProxy", p1.isChecked()).apply();
                } catch (Exception e) {

                }

                // TODO: Implement this method
            }


        });
        //mCustomTweakEdit.setText(myPrefs.getString("CustomTweakEdit", ""));
        //mCustomTweakProxy.setText(myPrefs.getString("CustomTweakProxy", ""));
        //mCustomTweakProxyPort.setText(myPrefs.getString("CustomTweakProxyPort", ""));

        Button save = (Button) v.findViewById(R.id.custok_tweak_save);

        final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(this).create();
        dialog.setTitle("Custom Tweak");
        dialog.setView(v);

        mCustomTweakGenerate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View p1) {
                GeneratorHelper gh = new GeneratorHelper(OpenVPNClient.this);
                gh.setCancelListener(OpenVPNClient.this);
                gh.show();
                // TODO: Implement this method
            }


        });

        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View p1) {
                String name = mCustomTweakName.getText().toString();
                if (name.isEmpty()) {
                    showToast("Tweak Name is empty");
                } else {
                    int tweak_mode = mTweakMode.getSelectedItemPosition();
                    if (tweak_mode == 0) {
                        String payload = mCustomTweakEdit.getText().toString();
                        String proxy = mCustomTweakProxy.getText().toString();
                        String port = mCustomTweakProxyPort.getText().toString();

                        if (payload.isEmpty() || proxy.isEmpty() || port.isEmpty()) {
                            showToast("Pleass full fill the fields");
                        } else {
                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("Name", name);
                                obj.put("Payload", payload);
                                obj.put("TunnelType", ConfigUtil.MODE_OVPN_HTTP_PROXY);
                                if (!mUseDefProxy.isChecked()) {
                                    JSONObject proxySettings = new JSONObject();
                                    proxySettings.put("Squid", proxy);
                                    proxySettings.put("Port", port);
                                    obj.put("ProxySettings", proxySettings);
                                }
                                JSONObject json = getJSONObject();
                                JSONArray networks = json.getJSONArray("Networks");
                                networks.put(obj);

                                OutputStream output = new FileOutputStream(new File(getFilesDir(), "Servers.js"));
                                String encrypted = Utils.Parser.encryptToString(json.toString(2));
                                output.write(encrypted.getBytes());
                                output.flush();
                                output.close();

                                showToast("Added Tweak Successfully!");
                                editor.putString("CustomTweakProxy", proxy);
                                editor.putString("CustomTweakProxyPort", port);
                                editor.putString("CustomTweakEdit", payload);
                                editor.apply();
                                dialog.dismiss();
                                loadNetworks();

                                for (int i = 0; i < listNetwork.size(); i++) {
                                    if (name.equals(listNetwork.get(i).getString("Name"))) {
                                        network_spin.setSelection(i);
                                    }
                                }
                            } catch (Exception e) {
                                showToast("Add Tweak Error: " + e.getMessage());
                            }
                        }

                    } else {
                        String sni = mCustomTweakEdit.getText().toString();
                        if (name.isEmpty() || sni.isEmpty()) {
                            showToast("Please full fill the fields");
                        } else {
                            try {
                                JSONObject obj = new JSONObject();
                                obj.put("Name", name);
                                obj.put("SNIHost", sni);
                                obj.put("TunnelType", ConfigUtil.MODE_SSL_DIRECT);
                                JSONObject json = getJSONObject();
                                JSONArray networks = json.getJSONArray("SSLNetworks");
                                networks.put(obj);

                                OutputStream output = new FileOutputStream(new File(getFilesDir(), "Servers.js"));
                                String encrypted = Utils.Parser.encryptToString(json.toString(2));
                                output.write(encrypted.getBytes());
                                output.flush();
                                output.close();

                                showToast("Added Tweak Successfully!");
                                editor.putString("CustomTweakEdit", sni).apply();
                                dialog.dismiss();
                                loadNetworks();

                                for (int i = 0; i < listNetwork.size(); i++) {
                                    if (name.equals(listNetwork.get(i).getString("Name"))) {
                                        network_spin.setSelection(i);
                                    }
                                }
                            } catch (Exception e) {
                                showToast("Add Tweak Error: " + e.getMessage());
                            }

                        }
                    }
                }
                // TODO: Implement this method
            }


        });
        dialog.show();
    }

    @Override
    public void onCancel()
    {
       // mCustomTweakSw.setChecked(false);
        // TODO: Implement this method
    }

    @Override
    public void onGenerate(String payload)
    {
        if (mCustomTweakEdit == null) {
            return;
        }
        mCustomTweakEdit.setText(payload);
        // TODO: Implement this method
    }
    

    @Override
    public JSONArray getNetworksArray()
    {
        // TODO: Implement this method
        return super.getNetworksArray();
    }

    @Override
    public JSONArray getSSLNetworks()
    {
        // TODO: Implement this method
        return super.getSSLNetworks();
    }
    private int getServerSelected()
    {
        try {
            for (int i = 0; i < listProfiles.size(); i++) {
                if (myPrefs.getString(SELECTED_PROFILE, "").equals(listProfiles.get(i))) {
                    return i;
                }
            }
        } catch (Exception e) {

        }
        // TODO: Implement this method
        return 0;
    }
    @Override
    public void onCheckedChanged(RadioGroup p1, int p2)
    {
        
        // TODO: Implement this method
    }
    
    protected void showDialog(String msg)
    {
        new AlertDialog.Builder(this).setTitle("Attention").
            setMessage(msg).
            setCancelable(false).
            setPositiveButton("Ok",new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface p1, int p2)
                {
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                    finish();
                    // TODO: Implement this method
                }


            }).show();
    }
    
    

    private Comparator<JSONObject> NetworkNameComparator()
    {
        // TODO: Implement this method
        return new Comparator<JSONObject>() {

            @Override
            public int compare(JSONObject p1, JSONObject p2)
            {
                // TODO: Implement this method
                try
                {
                    return p1.getString("Name").compareTo(p2.getString("Name"));
                } catch (JSONException e)
                {}
                return 0;
            }
            
            
        };
    }
    public void loadServers()
    {
        try {
            for (File f: getFilesDir().listFiles()) {
                if (f.getAbsolutePath().toLowerCase().endsWith(".ovpn")) {
                    f.delete();
                }
            }
            if (listProfiles.size() > 0) {
                listProfiles.clear();
            }
            listProfiles.add("Auto Select Server");
            
            ConfigParser parser = new ConfigParser();
            parser.parseConfig(new InputStreamReader(getResources().openRawResource(R.raw.config)));
            VpnProfile vp = parser.convertProfile();
            JSONArray serversArray = getServersArray();
            for (int i = 0; i < serversArray.length(); i++) {
                JSONObject server = serversArray.getJSONObject(i);
                String server_name = server.getString("Name");
                String server_ip = server.getString("ServerIPHost");
                String server_port = server.getString("OpenVPNTCPPort");
                
                Connection mConnection = vp.mConnections[0];
                mConnection.mServerName = server_ip;
                mConnection.mServerPort = server_port;
                mConnection.mUseCustomConfig = true;
                //mConnection.mCustomConfiguration = String.format("http-retry 1\nhttp-rertry-max 3\nhttp-proxy %s", "127.0.0.1 8989");

                String encoded_name = String.format("%s.ovpn", URLEncoder.encode(server_name, "UTF-8"));
                String config = vp.getConfigFile(this, true);
                File dir = new File(getFilesDir(), encoded_name);
                OutputStream out = new FileOutputStream(dir);
                String encrypted = Utils.Parser.encryptToString(config);
                out.write(encrypted.getBytes());
                out.flush();
                out.close();
                
                listProfiles.add(serversArray.getJSONObject(i).getString("Name"));
                mServerAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            showToast("Server Error: " + e.getMessage());
        }
    }
    
    public void checkUpdates()
    {
        final ServerUpdate su = new ServerUpdate(this); 
        su.setURL("https://saimonbd.com/updater/be2cc6c88692cfb04892c6e81a95f3.json");
        try
        {
            su.setCurrentVersion(getJSONObject().getString("Version"));
        }
        catch (JSONException e)
        {}
        su.setUpdateListener(this);
        su.start();
        
    }
    

    private void showFilePicker()
    {
        DialogProperties dp = new DialogProperties();

        dp.selection_mode = DialogConfigs.SINGLE_MODE;
        dp.selection_type = DialogConfigs.FILE_SELECT;
        dp.root = Environment.getExternalStorageDirectory();

        FilePickerDialog picker = new FilePickerDialog(this, dp);
        picker.setTitle("Select JsonFile");
        picker.setDialogSelectionListener(new DialogSelectionListener()
            {

                @Override
                public void onSelectedFilePaths(String[] files)
                {
                    for (String file: files) {
                        if (file.endsWith(".js") || file.endsWith(".rj")) {
                            try {
                                File jsFile = new File(file);
                                try {
                                    String securedJs = readStream(new FileInputStream(jsFile));
                                    String js = Utils.Parser.decryptString(securedJs);
                                    OutputStream output = new FileOutputStream(new File(getFilesDir(), "Servers.js"));
                                    output.write(js.toString().getBytes());
                                    output.flush();
                                    output.close();
                                
                                    
                                    restart();
                                } catch (Exception e) {
                                    showToast(String.format("Update Error: %s - %s", e.getClass().getName(), e.getMessage()));
                                }
                                
                            } catch (Exception e) {
                                showToast("Error deleting old files");
                            }
                        } else {
                            showToast("The file extension must end with .js");
                        }
                    }
                    
                    // TODO: Implement this method
                }


            });
        picker.setPositiveBtnName("Select");
        picker.setNegativeBtnName("Cancel");
        picker.show();
        // TODO: Implement this method
    }
    @Override
    public void onShowUpdate(final String newVersion)
    {
        if (autoUpdate) {
            for (File f: getFilesDir().listFiles()) {
                if (f.getAbsolutePath().endsWith(".ovpn")) {
                    f.delete();
                }
            }
            restart();
            return;
        }
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("Update Available");
        ab.setMessage("New Server Update Available Version: "+newVersion +" do you want to update this?");
        ab.setPositiveButton("Update",new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface p0, int p1)
                {
                    try {
                        restart();
                    } catch (Exception e) {
                        showToast("Error deleting old files");
                    }
                    
                }
            });
        ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface p0, int p1)
                {
                    File file = new File(getFilesDir(), "Servers.js");
                    file.delete();
                }
            });
        ab.setCancelable(false);
        ab.show();
    }
    @Override
    public void onNoUpdateAvailable(String oldVersion)
    {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("No Update");
        ab.setMessage("Sorry there's no Update Available. Your current Server Version is " + oldVersion);
        ab.setPositiveButton("Ok", null);
        if (showNoUpdate) {
            ab.show();
        }
    }

    @Override
    public void onUpdateError(String error)
    {
        //showToast(error);
        // TODO: Implement this method
    }
    private void restart()
    {
        int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_CANCEL_CURRENT : PendingIntent.FLAG_CANCEL_CURRENT;
        Intent mStartActivity = new Intent(this, OpenVPNClient.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId,    mStartActivity, flags);
        android.app.AlarmManager mgr = (android.app.AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        mgr.set(android.app.AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String str = TAG;
        Object[] objArr = new Object[S_BIND_CALLED];
        objArr[0] = intent.toString();
        Log.d(str, String.format("CLI: onNewIntent intent=%s", objArr));
        setIntent(intent);
    }
    
    protected void post_bind() {
        Log.d(TAG, "CLI: post bind");
        this.startup_state |= S_BIND_CALLED;
        process_autostart_intent(is_active());
        render_last_event();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        //getMenuInflater().inflate(R.menu.main_menu, menu);
        
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            
            case R.id.menu_update:
                showNoUpdate = true;
                showToast("Checking Updates");
                checkUpdates();
                return true;
            case R.id.menu_clear_data:
                showClearDataDialog();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
    
    public void event(EventMsg ev) {
        render_event(ev, RETAIN_AUTH, is_active(), RETAIN_AUTH);
    }

    private void render_last_event() {
        boolean active = is_active();
        EventMsg ev = get_last_event();
        if (ev != null) {
            render_event(ev, true, active, true);
        } else if (n_profiles_loaded() > 0) {
            render_event(EventMsg.disconnected(), true, active, true);
        } else {
            hide_status();
            ui_setup(active, UIF_RESET, null);
            show_progress(0, active);
        }
        EventMsg pev = get_last_event_prof_manage();
        if (pev != null) {
            render_event(pev, true, active, true);
        }
    }

    private boolean show_conn_info_field(String text, int field_id, int row_id) {
        int i = 0;
        boolean vis = text.length() > 0 ? true : RETAIN_AUTH;
        TextView tv = (TextView) findViewById(field_id);
        View row = findViewById(row_id);
        tv.setText(text);
        if (!vis) {
            i = 8;
        }
        row.setVisibility(i);
        return vis;
    }

    private void reset_conn_info() {
        show_conn_info(new ClientAPI_ConnectionInfo());
    }

    private void show_conn_info(ClientAPI_ConnectionInfo ci) {
        this.info_group.setVisibility((((((((RETAIN_AUTH | show_conn_info_field(ci.getVpnIp4(), R.id.ipv4_addr, R.id.ipv4_addr_row)) | show_conn_info_field(ci.getVpnIp6(), R.id.ipv6_addr, R.id.ipv6_addr_row)) | show_conn_info_field(ci.getUser(), R.id.user, R.id.user_row)) | show_conn_info_field(ci.getClientIp(), R.id.client_ip, R.id.client_ip_row)) | show_conn_info_field(ci.getServerHost(), R.id.server_host, R.id.server_host_row)) | show_conn_info_field(ci.getServerIp(), R.id.server_ip, R.id.server_ip_row)) | show_conn_info_field(ci.getServerPort(), R.id.server_port, R.id.server_port_row)) | show_conn_info_field(ci.getServerProto(), R.id.server_proto, R.id.server_proto_row) ? 0 : 8);
        set_visibility_stats_expansion_group();
    }

    private void set_visibility_stats_expansion_group() {
        int i = 0;
        boolean expand_stats = this.prefs.get_boolean("expand_stats", RETAIN_AUTH);
        View view = this.stats_expansion_group;
        if (!expand_stats) {
            i = 8;
        }
        view.setVisibility(i);
        this.details_more_less.setText(expand_stats ? R.string.touch_less : R.string.touch_more);
    }

    private void render_event(EventMsg ev, boolean reset, boolean active, boolean cached) {
        int flags = ev.flags;
        if (ev.is_reflected(this)) {
            flags |= UIF_REFLECTED;
        }
        if (reset || (flags & 8) != 0 || ev.profile_override != null) {
            ui_setup(active, UIF_RESET | flags, ev.profile_override);
            
        } else if (ev.res_id == R.string.core_thread_active) {
            active = true;
            ui_setup(true, flags, null);
            enabledWidgets(false);
        } else if (ev.res_id == R.string.core_thread_inactive) {
            active = RETAIN_AUTH;
            ui_setup(RETAIN_AUTH, flags, null);
            if (!(config.getTunnelType() == ConfigUtil.MODE_OVPN_DIRECT_UDP)) {
                enabledWidgets(InjectorService.isRunning ? false : true);
            } else {
                enabledWidgets(true);
            }
        }
        status_view.setTypeface(null, Typeface.NORMAL);
        
        switch (ev.res_id) {
            case R.string.connected /*2131034168*/:
                status_view.setTypeface(null, Typeface.BOLD);
                this.main_scroll_view.fullScroll(33);
                enabledWidgets(false);
                break;
            case R.string.auth_failed:
                stopVPN();
                showAuthFailedDialog();
                status_view.setTextColor(Color.RED);
                status_view.setText("Invalid Account!");
                break;
            case R.string.info_msg /*2131034237*/:
                if (ev.info.startsWith("OPEN_URL:")) {
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(ev.info.substring(9)));
                    intent.putExtra("com.android.browser.application_id", getPackageName());
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                        break;
                    }
                }
                break;
            case R.string.tap_not_supported /*2131034362*/:
                if (!cached) {
                    ok_dialog(resString(R.string.tap_unsupported_title), resString(R.string.tap_unsupported_error));
                    break;
                }
                break;
            case R.string.tun_iface_create /*2131034371*/:
                if (!cached) {
                    ok_dialog(resString(R.string.tun_ko_title), resString(R.string.tun_ko_error));
                    break;
                }
                break;
            case R.string.warn_msg /*2131034390*/:
                this.delayed_finish_on_connect = FinishOnConnect.PENDING;
                final AppCompatActivity self = this;
                ok_dialog(resString(R.string.warning_title), ev.info, new Runnable() {
                    public void run() {
                        if (!(OpenVPNClient.this.delayed_finish_on_connect == FinishOnConnect.PENDING || OpenVPNClient.this.delayed_finish_on_connect == FinishOnConnect.DISABLED)) {
                            self.finish();
                        }
                        OpenVPNClient.this.delayed_finish_on_connect = FinishOnConnect.DISABLED;
                    }
                });
                break;
        }
        if (ev.priority >= S_BIND_CALLED) {
            if (ev.icon_res_id >= 0) {
                show_status_icon(ev.icon_res_id);
            }
            if (ev.res_id == R.string.connected) {
                showExpireDate();
                show_status(ev.res_id);
                if (ev.conn_info != null) {
                    show_conn_info(ev.conn_info);
                }
            } else if (ev.info.length() > 0) {
                Object[] objArr = new Object[S_ONSTART_CALLED];
                objArr[0] = resString(ev.res_id);
                objArr[S_BIND_CALLED] = ev.info;
                show_status(String.format("%s : %s", objArr));
            } else {
                show_status(ev.res_id);
            }
        }
    
        show_progress(ev.progress, active);
        show_stats();
        if (ev.res_id == R.string.connected && this.finish_on_connect != FinishOnConnect.DISABLED) {
            if (this.prefs.get_boolean("autostart_finish_on_connect", RETAIN_AUTH)) {
               //Activity self = this;
                if (this.delayed_finish_on_connect == FinishOnConnect.PENDING) {
                    this.delayed_finish_on_connect = this.finish_on_connect;
                    return;
                }
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if (OpenVPNClient.this.finish_on_connect != FinishOnConnect.DISABLED) {
                            finish();
                        }
                    }
                }, 1000);
                return;
            }
            this.finish_on_connect = FinishOnConnect.DISABLED;
        }
    }

    private void stop_service() {
        submitDisconnectIntent(true);
    }

    private void stop() {
        cancel_stats();
        doUnbindService();
        unbindInjector();
        if (this.stop_service_on_client_exit) {
            Log.d(TAG, "CLI: stopping service");
            stop_service();
        }
    }

    protected void onStop() {
        Log.d(TAG, "CLI: onStop");
        cancel_stats();
        super.onStop();
    }

    @Override
    protected void onResume()
    {
        /*timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

                @Override
                public void run()
                {
                    runOnUiThread(new Runnable() {

                            @Override
                            public void run()
                            {
                                String ipAddr = String.format("%s IP: %s", getNetworkType(), Utils.getIPAddress(true));
                                mIpAddr.setText(ipAddr);
                                // TODO: Implement this method
                            }
                            
                        
                    });
                    // TODO: Implement this method
                }
                
            
        }, 0, 1000);*/
        // TODO: Implement this method
       // enabledWidgets(is_active());
        String version = getConfigVersion();
        TextView configversion = (TextView) findViewById(R.id.config_version);
        configversion.setText(version);
        super.onResume();
    }
    private String getNetworkType()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if (info != null) {
            return info.getTypeName();
        }
        return "";
    }
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "CLI: onStart");
        this.startup_state |= S_ONSTART_CALLED;
        if (this.finish_on_connect == FinishOnConnect.ENABLED) {
            this.finish_on_connect = FinishOnConnect.ENABLED_ACROSS_ONSTART;
        }
        boolean active = is_active();
        if (active) {
            schedule_stats();
        }
        if (process_autostart_intent(active)) {
            ui_setup(active, UIF_RESET, null);
        }
    }

    protected void onDestroy() {
        stop();
        Log.d(TAG, "CLI: onDestroy called");
        super.onDestroy();
    }

    private boolean process_autostart_intent(boolean active) {
        if ((this.startup_state & REQUEST_IMPORT_PKCS12) == REQUEST_IMPORT_PKCS12) {
            Intent intent = getIntent();
            String apn_key = "net.openvpn.openvpn.AUTOSTART_PROFILE_NAME";
            String apn = intent.getStringExtra(apn_key);
            if (apn != null) {
                this.autostart_profile_name = null;
                String str = TAG;
                Object[] objArr = new Object[S_BIND_CALLED];
                objArr[0] = apn;
                Log.d(str, String.format("CLI: autostart: %s", objArr));
                intent.removeExtra(apn_key);
                if (!active) {
                    ProfileList proflist = profile_list();
                    if (proflist == null || proflist.get_profile_by_name(apn) == null) {
                        ok_dialog(resString(R.string.profile_not_found), apn);
                    } else {
                        this.autostart_profile_name = apn;
                        return true;
                    }
                } else if (!current_profile().get_name().equals(apn)) {
                    this.autostart_profile_name = apn;
                    submitDisconnectIntent(RETAIN_AUTH);
                }
            }
        }
        return RETAIN_AUTH;
    }

    private void cancel_ui_reset() {
        this.ui_reset_timer_handler.removeCallbacks(this.ui_reset_timer_task);
    }

    private void schedule_ui_reset(long delay) {
        cancel_ui_reset();
        this.ui_reset_timer_handler.postDelayed(this.ui_reset_timer_task, delay);
    }

    private void hide_status() {
        this.status_view.setVisibility(View.VISIBLE);
    }

    private void show_status(String text) {
        this.status_view.setVisibility(View.VISIBLE);
        if (!text.contains("127.0.0.1")) {
            this.status_view.setText(text);
        }
        if (text.equals( getString(R.string.auth_failed))) {
            status_view.setTextColor(Color.RED);
            status_view.setText("Invalid Account!");
            return;
        }
        if (text.equals(getString(R.string.disconnected)) || text.equals(getString(R.string.auth_failed))) {
            status_view.setTextColor(getResources().getColor(R.color.accent_color));
            status_view.setText("Disconnected");
            progress.setProgressWithAnimation(0.0f);
            return;
        }
        if (text.equals(getString(R.string.connected))) {
            status_view.setTextColor(Color.GREEN);
            progress.setProgressWithAnimation(100.0f);
            return;
        }
        status_view.setTextColor(getResources().getColor(R.color.accent_color));
    }

    private void show_status(int res_id) {
        TextView swStatus = (TextView)findViewById(R.id.switch_status) ;

        this.status_view.setVisibility(0);
        if (!getString(res_id).contains("127.0.0.1")) {
            this.status_view.setText(res_id);
            swStatus.setText(getString(res_id));
            if (res_id == R.string.connected) {
                swStatus.setText("Connected");
            } else if (res_id == R.string.disconnected) {
                swStatus.setText("Disconnected");
            }
        }
        //disconnect_button.setTextColor(Color.BLACK);
       // disconnect_button.setBackgroundResource(R.drawable.button_connect);
        //disconnect_button.setText(getString(res_id));

        if (res_id == R.string.connected) {
            progress.setColor(getResources().getColor(R.color.color2));
        } else {
            progress.setColor(getResources().getColor(R.color.color1));
        }

        if (res_id == R.string.auth_failed) {
            status_view.setTextColor(Color.RED);
            status_view.setText("Invalid Account!");
            return;
        }
        if (res_id == R.string.disconnected || res_id == R.string.auth_failed) {
            status_view.setTextColor(getResources().getColor(R.color.accent_color));
            status_view.setText("Disconnected");
           // disconnect_button.setText("Disconnect");
            return;
        }
        if (res_id == R.string.connected) {
            //disconnect_button.setTextColor(Color.WHITE);
           // disconnect_button.setBackgroundResource(R.drawable.button_disconnect);
            status_view.setTextColor(Color.GREEN);
            return;
        }
        status_view.setTextColor(getResources().getColor(R.color.accent_color));

    }

    private void show_status_icon(int res_id) {
        this.status_icon_view.setImageResource(res_id);
    }

    private void show_progress(int progress, boolean active) {
        this.progress.setProgressWithAnimation((float) progress);
        if (progress <= 0 || progress >= 99) {
            this.progress_bar.setVisibility(8);
            return;
        }
        this.progress_bar.setVisibility(0);
        this.progress_bar.setProgress(progress);
    }

    private void cancel_stats() {
        this.stats_timer_handler.removeCallbacks(this.stats_timer_task);
    }

    private void schedule_stats() {
        cancel_stats();
        this.stats_timer_handler.postDelayed(this.stats_timer_task, 1000);
    }

    private static String render_bandwidth(long bw) {
        String postfix;
        float div;
        Object[] objArr;
        float bwf = (float) bw;
        if (bwf >= 1.0E12f) {
            postfix = "TB";
            div = 1.09951163E12f;
        } else if (bwf >= 1.0E9f) {
            postfix = "GB";
            div = 1.07374182E9f;
        } else if (bwf >= 1000000.0f) {
            postfix = "MB";
            div = 1048576.0f;
        } else if (bwf >= 1000.0f) {
            postfix = "KB";
            div = 1024.0f;
        } else {
            objArr = new Object[S_BIND_CALLED];
            objArr[0] = Float.valueOf(bwf);
            return String.format("%.0f", objArr);
        }
        objArr = new Object[S_ONSTART_CALLED];
        objArr[0] = Float.valueOf(bwf / div);
        objArr[S_BIND_CALLED] = postfix;
        return String.format("%.2f %s", objArr);
    }

    private String render_last_pkt_recv(int sec) {
        if (sec >= 3600) {
            return resString(R.string.lpr_gt_1_hour_ago);
        }
        String resString;
        Object[] objArr;
        if (sec >= 120) {
            resString = resString(R.string.lpr_gt_n_min_ago);
            objArr = new Object[S_BIND_CALLED];
            objArr[0] = Integer.valueOf(sec / 60);
            return String.format(resString, objArr);
        } else if (sec >= S_ONSTART_CALLED) {
            resString = resString(R.string.lpr_n_sec_ago);
            objArr = new Object[S_BIND_CALLED];
            objArr[0] = Integer.valueOf(sec);
            return String.format(resString, objArr);
        } else if (sec == S_BIND_CALLED) {
            return resString(R.string.lpr_1_sec_ago);
        } else {
            if (sec == 0) {
                return resString(R.string.lpr_lt_1_sec_ago);
            }
            return BuildConfig.FLAVOR;
        }
    }

    private void show_stats() {
        if (is_active()) {
            ConnectionStats stats = get_connection_stats();
            this.last_pkt_recv_view.setText(render_last_pkt_recv(stats.last_packet_received));
            this.duration_view.setText(OpenVPNClientBase.render_duration(stats.duration));
            this.bytes_in_view.setText(render_bandwidth(stats.bytes_in));
            this.bytes_out_view.setText(render_bandwidth(stats.bytes_out));
        }
    }

    private void clear_stats() {
        this.last_pkt_recv_view.setText(BuildConfig.FLAVOR);
        this.duration_view.setText(BuildConfig.FLAVOR);
        this.bytes_in_view.setText(BuildConfig.FLAVOR);
        this.bytes_out_view.setText(BuildConfig.FLAVOR);
        reset_conn_info();
    }

    private int n_profiles_loaded() {
        ProfileList proflist = profile_list();
        if (proflist != null) {
            return proflist.size();
        }
        return 0;
    }

    private String selected_profile_name() {
        String ret = null;
        ProfileList proflist = profile_list();
        if (SpinUtil.get_spinner_selected_item(profile_spin).contains("Auto")) {
            try
            {
                return getServersArray().getJSONObject(mRandomServer).getString("Name");
            }
            catch (JSONException e)
            {}
        }
        if (proflist != null && proflist.size() > 0) {
            ret = proflist.size() == S_BIND_CALLED ? ((Profile) proflist.get(0)).get_name() : SpinUtil.get_spinner_selected_item(profile_spin);
        }
        if (ret == null) {
            return "UNDEFINED_PROFILE";
        }
        return ret;
    }

    private Profile selected_profile() {
        ProfileList proflist = profile_list();
        if (proflist != null) {
            return proflist.get_profile_by_name(selected_profile_name());
        }
        return null;
    }

    private void clear_auth() {
        this.username_edit.setText(BuildConfig.FLAVOR);
        this.pk_password_edit.setText(BuildConfig.FLAVOR);
        this.password_edit.setText(BuildConfig.FLAVOR);
        this.response_edit.setText(BuildConfig.FLAVOR);
    }

    private void ui_setup(boolean active, int flags, String profile_override) {
        boolean orig_active = active;
        boolean autostart = RETAIN_AUTH;
        cancel_ui_reset();
        if (!((UIF_RESET & flags) == 0 && orig_active == this.last_active)) {
            clear_auth();
            if (!(active || this.autostart_profile_name == null)) {
                autostart = true;
                profile_override = this.autostart_profile_name;
                this.autostart_profile_name = null;
            }
            ProfileList proflist = profile_list();
            Profile prof = null;
            if (proflist == null || proflist.size() <= 0) {
                this.profile_group.setVisibility(8);
            } else {
                ProfileSource ps = ProfileSource.UNDEF;
                
                
                //SpinUtil.show_spinner(this, this.profile_spin, proflist.profile_names());
                
                if (active) {
                    ps = ProfileSource.SERVICE;
                    prof = current_profile();
                }
                if (prof == null && profile_override != null) {
                    ps = ProfileSource.PRIORITY;
                    prof = proflist.get_profile_by_name(profile_override);
                    if (prof == null) {
                        Log.d(TAG, "CLI: profile override not found");
                        autostart = RETAIN_AUTH;
                    }
                }
                /*if (prof == null) {
                    if ((UIF_PROFILE_SETTING_FROM_SPINNER & flags) != 0) {
                        ps = ProfileSource.SPINNER;
                        prof = proflist.get_profile_by_name(SpinUtil.get_spinner_selected_item(this.profile_spin));
                    } else {
                        ps = ProfileSource.PREFERENCES;
                        prof = proflist.get_profile_by_name(this.prefs.get_string("profile"));
                    }
                }*/
                if (SpinUtil.get_spinner_selected_item(profile_spin).contains("Auto")) {
                    try
                    {
                        prof = proflist.get_profile_by_name(getServersArray().getJSONObject(mRandomServer).getString("Name"));
                    }
                    catch (JSONException e)
                    {}
                }
 else
 {
                    prof = proflist.get_profile_by_name(SpinUtil.get_spinner_selected_item(profile_spin));
                }
                if (prof == null) {
                    ps = ProfileSource.LIST0;
                    prof = (Profile) proflist.get(0);
                }
                if (ps != ProfileSource.PREFERENCES && (UIF_REFLECTED & flags) == 0) {
                    this.prefs.set_string("profile", prof.get_name());
                    gen_ui_reset_event(true);
                }
               /* if (ps != ProfileSource.SPINNER) {
                    SpinUtil.set_spinner_selected_item(this.profile_spin, prof.get_name());
                }*/
                this.profile_group.setVisibility(0);
                this.profile_spin.setEnabled(!active ? true : RETAIN_AUTH);
                this.profile_edit.setVisibility(active ? 8 : 0);
            }
            if (prof != null) {
                if ((UIF_RESET & flags) != 0) {
                    prof.reset_dynamic_challenge();
                }
                EditText focus = null;
                if (!active && (flags & 32) != 0) {
                    this.post_import_help_blurb.setVisibility(0);
                } else if (active) {
                    this.post_import_help_blurb.setVisibility(8);
                }
                ProxyList proxy_list = get_proxy_list();
                if (active || proxy_list.size() <= 0) {
                    this.proxy_group.setVisibility(8);
                } else {
                    SpinUtil.show_spinner(this, this.proxy_spin, proxy_list.get_name_list(true));
                    String name = proxy_list.get_enabled(true);
                    if (name != null) {
                        SpinUtil.set_spinner_selected_item(this.proxy_spin, name);
                    }
                    this.proxy_group.setVisibility(0);
                }
                if (active || !prof.server_list_defined()) {
                    this.server_group.setVisibility(8);
                } else {
                    SpinUtil.show_spinner(this, this.server_spin, prof.get_server_list().display_names());
                    String server = this.prefs.get_string_by_profile(prof.get_name(), "server");
                    if (server != null) {
                        SpinUtil.set_spinner_selected_item(this.server_spin, server);
                    }
                    this.server_group.setVisibility(0);
                }
                if (active) {
                    this.username_group.setVisibility(8);
                    this.pk_password_group.setVisibility(8);
                    this.password_group.setVisibility(8);
                } else {
                    boolean is_pwd_save;
                    String saved_pwd;
                    boolean udef = prof.userlocked_username_defined();
                    boolean autologin = prof.get_autologin();
                    boolean pk_pwd_req = prof.get_private_key_password_required();
                    boolean dynamic_challenge = prof.is_dynamic_challenge();
                    if ((!autologin || (autologin && udef)) && !dynamic_challenge) {
                        if (udef) {
                            this.username_edit.setText(prof.get_userlocked_username());
                            set_enabled(this.username_edit, RETAIN_AUTH);
                        } else {
                            set_enabled(this.username_edit, true);
                            String pref_username = this.prefs.get_string_by_profile(prof.get_name(), "username");
                            if (pref_username != null) {
                                this.username_edit.setText(pref_username);
                            } else if (null == null) {
                                focus = this.username_edit;
                            }
                        }
                        this.username_group.setVisibility(View.VISIBLE);
                    } else {
                        this.username_group.setVisibility(View.INVISIBLE);
                    }
                    if (pk_pwd_req) {
                        is_pwd_save = this.prefs.get_boolean_by_profile(prof.get_name(), "pk_password_save", RETAIN_AUTH);
                        saved_pwd = null;
                        this.pk_password_group.setVisibility(View.GONE);
                        this.pk_password_save_checkbox.setChecked(is_pwd_save);
                        if (is_pwd_save) {
                            saved_pwd = this.pwds.get("pk", prof.get_name());
                        }
                        if (saved_pwd != null) {
                            this.pk_password_edit.setText(saved_pwd);
                        } else if (focus == null) {
                            focus = this.pk_password_edit;
                        }
                    } else {
                        this.pk_password_group.setVisibility(View.VISIBLE);
                    }
                    if (autologin || dynamic_challenge) {
                        this.password_group.setVisibility(View.GONE);
                    } else {
                        boolean is_auth_pw_save = prof.get_allow_password_save();
                        is_pwd_save = (is_auth_pw_save && this.prefs.get_boolean_by_profile(prof.get_name(), "auth_password_save", RETAIN_AUTH)) ? true : RETAIN_AUTH;
                        saved_pwd = null;
                        this.password_group.setVisibility(View.GONE);
                        this.password_save_checkbox.setEnabled(is_auth_pw_save);
                        this.password_save_checkbox.setChecked(is_pwd_save);
                        if (is_pwd_save) {
                            saved_pwd = this.pwds.get("auth", prof.get_name());
                        }
                        if (saved_pwd != null) {
                            this.password_edit.setText(saved_pwd);
                        } else if (focus == null) {
                            focus = this.password_edit;
                        }
                    }
                }
                if (active || prof.get_autologin() || !prof.challenge_defined()) {
                    this.cr_group.setVisibility(View.VISIBLE);
                } else {
                    this.cr_group.setVisibility(View.VISIBLE);
                    Challenge chal = prof.get_challenge();
                    this.challenge_view.setText(chal.get_challenge());
                    this.challenge_view.setVisibility(View.VISIBLE);
                    if (chal.get_response_required()) {
                        if (chal.get_echo()) {
                            this.response_edit.setTransformationMethod(SingleLineTransformationMethod.getInstance());
                        } else {
                            this.response_edit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }
                        this.response_edit.setVisibility(View.VISIBLE);
                        if (focus == null) {
                            focus = this.response_edit;
                        }
                    } else {
                        this.response_edit.setVisibility(View.VISIBLE);
                    }
                    if (prof.is_dynamic_challenge()) {
                        schedule_ui_reset(prof.get_dynamic_challenge_expire_delay());
                    }
                }
                this.button_group.setVisibility(View.VISIBLE);
                if (orig_active) {
                    this.conn_details_group.setVisibility(View.VISIBLE);
                    this.connect_button.setVisibility(View.VISIBLE);
                    this.disconnect_button.setVisibility(View.VISIBLE);
                } else {
                    this.conn_details_group.setVisibility(View.VISIBLE);
                    this.connect_button.setVisibility(View.VISIBLE);
                    this.disconnect_button.setVisibility(View.VISIBLE);
                }
                if (focus != null) {
                    autostart = RETAIN_AUTH;
                }
                req_focus(focus);
            } else {
                this.post_import_help_blurb.setVisibility(View.VISIBLE);
                this.proxy_group.setVisibility(View.VISIBLE);
                this.server_group.setVisibility(View.VISIBLE);
                this.username_group.setVisibility(View.VISIBLE);
                this.pk_password_group.setVisibility(View.VISIBLE);
                this.password_group.setVisibility(View.VISIBLE);
                this.cr_group.setVisibility(8);
                this.conn_details_group.setVisibility(8);
                this.button_group.setVisibility(8);
                show_status_icon(R.drawable.info);
                show_status(R.string.no_profiles_loaded);
            }
            if (orig_active) {
                schedule_stats();
            } else {
                cancel_stats();
            }
        }
        this.last_active = orig_active;
        if (autostart && !this.last_active) {
            this.finish_on_connect = FinishOnConnect.ENABLED;
            start_connect();
        }
    }
    private void enabledWidgets(boolean enabled)
    {
        if (enabled) {
            disconnect_button.setVisibility(View.GONE);
            connect_button.setVisibility(View.VISIBLE);
        } else {
            disconnect_button.setVisibility(View.VISIBLE);
            connect_button.setVisibility(View.GONE);
        }
        //mServerLayout.setEnabled(enabled);
        network_spin.setEnabled(enabled);
        profile_spin.setEnabled(enabled);
        vpn_username.setEnabled(enabled);
        vpn_password.setEnabled(enabled);
          // mCustomTweakSw.setEnabled(enabled);
    }

    private void set_enabled(EditText editText, boolean state) {
        editText.setEnabled(state);
        editText.setFocusable(state);
        editText.setFocusableInTouchMode(state);
    }

    private void raise_file_selection_dialog(int requestCode) {
        switch (requestCode) {
            case S_ONSTART_CALLED /*2*/:
                raise_file_selection_dialog(S_ONSTART_CALLED, R.string.select_profile);
                return;
            case REQUEST_IMPORT_PKCS12 /*3*/:
                raise_file_selection_dialog(REQUEST_IMPORT_PKCS12, R.string.select_pkcs12);
                return;
            default:
                return;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length != 0) {
            switch (requestCode) {
                case REQUEST_OFFLINE_UPDATE:
                    if (grantResults.length > 0) {
                        for (int grantResult : grantResults) {
                            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                                showFilePicker();
                            } else {
                                showToast("You need to Grant the permission to use Offline update");
                            }
                        }
                    }
                    return;
                case S_ONSTART_CALLED /*2*/:
                case REQUEST_IMPORT_PKCS12 /*3*/:
                    int i = 0;
                    while (i < grantResults.length) {
                        if (permissions[i].equals("android.permission.READ_EXTERNAL_STORAGE") && grantResults[i] == 0) {
                            raise_file_selection_dialog(requestCode);
                        }
                        i += S_BIND_CALLED;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private void request_file_selection_dialog(int requestCode) {
        if (ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            raise_file_selection_dialog(requestCode);
            return;
        }
        String[] perms = new String[S_BIND_CALLED];
        perms[0] = "android.permission.READ_EXTERNAL_STORAGE";
        ActivityCompat.requestPermissions(this, perms, requestCode);
    }

    

    

    public void onClick(View v) {
        cancel_ui_reset();
        this.autostart_profile_name = null;
        this.finish_on_connect = FinishOnConnect.DISABLED;
        int viewid = v.getId();
        if (viewid == R.id.connect) {
            status_view.setTextColor(Color.BLACK);
            String user = vpn_username.getText().toString();
            String pass = vpn_password.getText().toString();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(OpenVPNClient.this, "Please input Username/Password!",Toast.LENGTH_SHORT).show();
               // showAccountLogin();
                return;
            }
            mRandomServer = new Random().nextInt(getServersArray().length());
            config.setUsername(user);
            config.setPassword(pass);
            showExpireDate();
            startInjector();
        }
 else if (viewid == R.id.disconnect)
 {
          
            stopVPN();
        } else if (viewid == R.id.profile_edit || viewid == R.id.proxy_edit) {
            openContextMenu(v);
        } 
    }
    private void stopVPN()
    { 
        status_view.setTextColor(Color.RED);
        show_status("Disconnected");
        stopInjector();
        enabledWidgets(true);
        submitDisconnectIntent(true);
    }
    
    

    @Override
    public void onGeneratePayload(String payload)
    {
        
        // TODO: Implement this method
    }
    @Override
    public void onGeneratorClose()
    {
        // TODO: Implement this method
    }

    @Override
    public void showToast(String str)
    {
        // TODO: Implement this method
        super.showToast(str);
    }
    
    private void showClearDataDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Clear Data");
        builder.setMessage("Are you sure you want to Clear App Data?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface p1, int p2)
                {
                    try {
                        // clearing app data
                        if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                            ((ActivityManager)getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!
                        } else {
                            String packageName = getApplicationContext().getPackageName();
                            Runtime runtime = Runtime.getRuntime();
                            runtime.exec("pm clear "+packageName);
                        }
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    } 
                    // TODO: Implement this method
                }


            }).setNegativeButton("Cancel", null).show();
        // TODO: Implement this method
    }
    
    
    public static void stopInjector()
    {
        StatisticsGraphData.getStatisticData().getDataTransferStats().stop();
        if (mInjector != null && InjectorService.isRunning) {
            mInjector.stopInjector();
        }
        OpenVPNApplication.getContext().stopService(new Intent(OpenVPNApplication.getContext(), InjectorService.class));
    }
    private void startInjector()
    {
        try {
            //showLog();


            ConfigUtil config = ConfigUtil.getInstance(this);

            boolean isCustomPayload = false;
            JSONObject server = null;
            if (SpinUtil.get_spinner_selected_item(profile_spin).contains("Auto")) {
                server = getServersArray().getJSONObject(mRandomServer);
            } else {
                server = getServer();
            }
            String serverName = server.getString("Name");
            String sshHost = server.getString("ServerIPHost");
            config.setServerSelectedName(serverName);
            config.setSSHHost(sshHost);
            config.setSSHPort((server.getString("OpenVPNTCPPort")));
            config.setSSLPort(server.getString("OpenVPNSSLPort"));

            if (server.has("UDPSSLPort")) {
                config.setUDPSSLPort(server.getString("UDPSSLPort"));
            }

            if (!isCustomPayload) {
                JSONObject payloadJs = (JSONObject) network_spin.getSelectedItem();
                int tunnel_type = payloadJs.getInt("TunnelType");
                config.setTunnelType(tunnel_type);
                config.setNetworkSelectedName(payloadJs.getString("Name"));

                if (tunnel_type == ConfigUtil.MODE_OVPN_DIRECT_UDP) {
                    start_connect();
                    return;
                }
                if (payloadJs.has("FrontQuery") && payloadJs.has("BackQuery")) {
                    String front_query = payloadJs.getString("FrontQuery");
                    String back_query = payloadJs.getString("BackQuery");
                    if (front_query.isEmpty() && back_query.isEmpty()) {
                        config.setIsQueryMode(false);
                    } else if(!front_query.isEmpty()) {
                        config.setIsQueryMode(true);
                        config.setFrontQuery(front_query);
                        config.setBackQuery("");
                    } else if (!back_query.isEmpty()) {
                        config.setIsQueryMode(true);
                        config.setBackQuery(back_query);
                        config.setFrontQuery("");
                    }
                } else {
                    config.setIsQueryMode(false);
                }
                if (tunnel_type == ConfigUtil.MODE_OVPN_HTTP_PROXY || tunnel_type == ConfigUtil.MODE_SSL_HTTP_PROXY || config.getTunnelType() == ConfigUtil.MODE_UDP_SSL_PROXY) {
                    if (payloadJs.has("ProxySettings")) {
                        JSONObject proxySettings = payloadJs.getJSONObject("ProxySettings");
                        String proxy = proxySettings.getString("Squid");
                        config.setProxyPort(proxySettings.getString("Port"));
                        if (proxy.contains("Default") || proxy.isEmpty()) {
                            config.setProxy(sshHost);
                        } else {
                            config.setProxy(proxy);
                        }
                    } else {
                        config.setProxy(server.getString("ServerIPHost"));
                        config.setProxyPort("80");
                    }
                }
                if (tunnel_type == ConfigUtil.MODE_SSL_HTTP_PROXY || tunnel_type == ConfigUtil.MODE_UDP_SSL_PROXY) {
                    config.setCustomSSLPortEnable(payloadJs.has("CustomSSLPort"));
                    if (payloadJs.has("CustomSSLPort")) {
                        config.setSSLPort(payloadJs.getString("CustomSSLPort"));
                    } else {
                        config.setSSLPort(server.getString("OpenVPNSSLPort"));
                    }
                } else {
                    config.setCustomSSLPortEnable(false);
                }
                if (payloadJs.has("SNIHost")) {
                    config.setSni((payloadJs.getString("SNIHost")));
                }

                if (payloadJs.has("Payload")) {
                    config.setHTTPayload(payloadJs.getString("Payload"));
                }
            }
            startService(new Intent(this, InjectorService.class).setAction("START"));
        } catch (Exception e) {
            showToast(e.getMessage());
        }
    }

    private JSONObject getNetworkSelectedJson() throws JSONException
    {
        for (int i = 0; i < listNetwork.size(); i++) {
            if (listNetwork.get(i).getString("Name").equals(myPrefs.getString(SELECTED_NETWORK, ""))) {
                return  listNetwork.get(i);
            }
        }
        /*for (int i = 0; i < getNetworksArray().length(); i++) {
            JSONObject js = getNetworksArray().getJSONObject(i);
            if (js.getString("Name").equals(myPrefs.getString(SELECTED_NETWORK, ""))) {
                return js;
            }
        }
        for (int i = 0; i < getSSLNetworks().length(); i++) {
            JSONObject js = getSSLNetworks().getJSONObject(i);
            if (js.getString("Name").equals(myPrefs.getString(SELECTED_NETWORK, ""))) {
                return js;
            }
        }*/
        return listNetwork.get(0);
        // TODO: Implement this method
        
    }

    private void showLog()
    {
        startActivity(new Intent(this, LogActivity.class));
        // TODO: Implement this method
    }

    private JSONObject getServer() throws JSONException
    {
        JSONArray array = getServersArray();
        for (int i = 0; i < array.length(); i++) {
            JSONObject server = array.getJSONObject(i);
            if (server.getString("Name").equals(SpinUtil.get_spinner_selected_item(profile_spin))) {
                return server;
            }
        }
        // TODO: Implement this method
        return null;
    }

    private void showExpireDate() {


        String format = "https://saimonbd.com/api/auth.php?username=%s&password=%s&device_id=%s&device_model=%s";
        String user = vpn_username.getText().toString();
        String pass = vpn_password.getText().toString();

        if (user.isEmpty() || pass.isEmpty()) {
            return;
        }

        String model = Build.MODEL;
        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        String jsonUrl = String.format(format, user, pass, id, model);

        //((ClipboardManager)getSystemService(CLIPBOARD_SERVICE)).setText(jsonUrl);

        StringRequest req = new StringRequest(jsonUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //showToast(response);
                            JSONObject js = new JSONObject(response);
                            if (js.getString("device_match").equals("none")) {
                                stopVPN();
                                showAuthFailedDialog();

                                return;
                            }
                            if (js.getString("device_match").equals("false")) {
                                showDeviceIdNotMatch();
                                return;
                            }
                            onExpireDate(js.getString("expiry"));
                        } catch (Exception e) {
                            //onError(e.getClass().getSimpleName() + ": " +e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onError("Expire Date: " + error.getMessage());
            }

        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);
        // TODO: Implement this method
    }

    void showAuthFailedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Authentication failed!");
        builder.setMessage("Wrong username or passoword!, Please recheck your account");
        builder.setPositiveButton("Reset Account", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                vpn_username.setText("");
                vpn_password.setText("");
                editor.putString(USERNAME, "").apply();
                editor.putString(PASSWORD, "").apply();
                // TODO: Implement this method
            }


        });
        builder.show();
    }

    void showDeviceIdNotMatch() {
        stopVPN();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning!");
        builder.setMessage("Account is used in another device, Please recheck your account");
        builder.setPositiveButton("Reset Account", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                vpn_username.setText("");
                vpn_password.setText("");
                editor.putString(USERNAME, "").apply();
                editor.putString(PASSWORD, "").apply();
                // TODO: Implement this method
            }


        });
        builder.show();
    }

    @Override
    public void onExpireDate(String expiry) {
        TextView mExpireDate = (TextView) findViewById(R.id.expire_date);
        if (mExpireDate == null) {
            return;
        }
        if (expiry.equals("none")) {
            mExpireDate.setText("none");
        } else {
            mExpireDate.setText(getDaysLeft(expiry));
        }
        // TODO: Implement this method
    }

    @Override
    public void onDeviceNotMatch(String message) {
        stopVPN();

        //Snackbar.make(connect_button, message, Snackbar.LENGTH_LONG).show();
        // TODO: Implement this method
    }

    private String getDaysLeft(String thatDate) {
		/*if (thatDate.contains(" ")) {
			thatDate = thatDate.split(" ")[0];
		}
		String[] split = thatDate.split("-");
		Calendar instance = Calendar.getInstance();
		instance.set(Integer.valueOf(split[0]).intValue(), Integer.valueOf(split[1]).intValue() - 1, Integer.valueOf(split[2]).intValue());
		return String.format("%s Days Left", new Object[]{(instance.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) / ((long) 86400000)});
	   */

        String datetime = "";
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat d = new SimpleDateFormat("MMM/dd/yyyy");
        try {
            Date convertedDate = inputFormat.parse(thatDate);
            datetime = d.format(convertedDate);
        } catch (ParseException | java.text.ParseException e) {
            return thatDate;
        }
        return datetime;

        //return thatDate;
    }

    @Override
    public void onAuthFailed(String message) {
        stopVPN();
        status_view.setTextColor(Color.RED);
        status_view.setText("Invalid Account!");
        //Snackbar.make(connect_button, "Invalid Account!", Snackbar.LENGTH_LONG).show();
        // TODO: Implement this method
    }

    @Override
    public void onError(String error) {
        //showExpireDate(); 
        //Snackbar.make(connect_button, error, Snackbar.LENGTH_LONG).show();
        // TODO: Implement this method
    }
    @Override
    public void startOpenVPN()
    {
        start_connect();
        // TODO: Implement this method
        super.startOpenVPN();
    }
    private void start_connect() {
        cancel_ui_reset();
        Intent intent = VpnService.prepare(this);
        if (intent != null) {
            try {
                Log.d(TAG, "CLI: requesting VPN actor rights");
                startActivityForResult(intent, S_BIND_CALLED);
                return;
            } catch (ActivityNotFoundException e) {
                Log.e(TAG, "CLI: requesting VPN actor rights failed", e);
                ok_dialog(resString(R.string.vpn_permission_dialog_missing_title), resString(R.string.vpn_permission_dialog_missing_text));
                return;
            }
        }
        Log.d(TAG, "CLI: app is already authorized as VPN actor");
        resolve_epki_alias_then_connect();
    }

    public boolean onTouch(View v, MotionEvent event) {
        boolean new_expand_stats = RETAIN_AUTH;
        if (v.getId() != R.id.conn_details_boxed || event.getAction() != 0) {
            return RETAIN_AUTH;
        }
        if (!this.prefs.get_boolean("expand_stats", RETAIN_AUTH)) {
            new_expand_stats = true;
        }
        this.prefs.set_boolean("expand_stats", new_expand_stats);
        set_visibility_stats_expansion_group();
        return true;
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        cancel_ui_reset();
        int viewid = parent.getId();
        if (viewid == R.id.profile) {
            ui_setup(is_active(), 327680, null);
            try {
                String server_name = listProfiles.get(position);
                editor.putString(SELECTED_PROFILE, server_name).apply();
            } catch (Exception e) {

            }
        } else if (viewid == R.id.proxy) {
            ProxyList proxy_list = get_proxy_list();
            if (proxy_list != null) {
                proxy_list.set_enabled(SpinUtil.get_spinner_list_item(this.proxy_spin, position));
                proxy_list.save();
                gen_ui_reset_event(true);
            }
        } else if (viewid == R.id.server) {
            String server = SpinUtil.get_spinner_list_item(this.server_spin, position);
            this.prefs.set_string_by_profile(SpinUtil.get_spinner_selected_item(this.profile_spin), "server", server);
            gen_ui_reset_event(true);
        }  else if (viewid == R.id.networks) {
            editor.putInt(SELECTED_NETWORK, position).apply();
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void menu_add(ContextMenu menu, int id, boolean enabled, String menu_key) {
        MenuItem item = menu.add(0, id, 0, id).setEnabled(enabled);
        if (menu_key != null) {
            item.setIntent(new Intent().putExtra("net.openvpn.openvpn.MENU_KEY", menu_key));
        }
    }

    private String get_menu_key(MenuItem item) {
        if (item != null) {
            Intent intent = item.getIntent();
            if (intent != null) {
                return intent.getStringExtra("net.openvpn.openvpn.MENU_KEY");
            }
        }
        return null;
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        boolean z = RETAIN_AUTH;
        Log.d(TAG, "CLI: onCreateContextMenu");
        super.onCreateContextMenu(menu, v, menuInfo);
        int viewid = v.getId();
        if (!is_active() && (viewid == R.id.profile || viewid == R.id.profile_edit)) {
            Profile prof = selected_profile();
            if (prof != null) {
                String profile_name = prof.get_name();
                menu.setHeaderTitle(profile_name);
                if (SpinUtil.get_spinner_count(this.profile_spin) > S_BIND_CALLED) {
                    z = true;
                }
                menu_add(menu, R.string.profile_context_menu_change_profile, z, null);
                menu_add(menu, R.string.profile_context_menu_create_shortcut, true, profile_name);
                menu_add(menu, R.string.profile_context_menu_delete, prof.is_deleteable(), profile_name);
                menu_add(menu, R.string.profile_context_menu_rename, prof.is_renameable(), profile_name);
                menu_add(menu, R.string.profile_context_forget_creds, true, profile_name);
            } else {
                menu.setHeaderTitle(R.string.profile_context_none_selected);
            }
            menu_add(menu, R.string.profile_context_cancel, true, null);
        } else if (!is_active()) {
            if (viewid == R.id.proxy || viewid == R.id.proxy_edit) {
                ProxyList proxy_list = get_proxy_list();
                if (proxy_list != null) {
                    String proxy_name = proxy_list.get_enabled(true);
                    boolean is_none = proxy_list.is_none(proxy_name);
                    menu.setHeaderTitle(proxy_name);
                    menu_add(menu, R.string.proxy_context_change_proxy, SpinUtil.get_spinner_count(this.proxy_spin) > S_BIND_CALLED ? true : RETAIN_AUTH, null);
                    menu_add(menu, R.string.proxy_context_edit, !is_none ? true : RETAIN_AUTH, proxy_name);
                    if (!is_none) {
                        z = true;
                    }
                    menu_add(menu, R.string.proxy_context_delete, z, proxy_name);
                    menu_add(menu, R.string.proxy_context_forget_creds, proxy_list.has_saved_creds(proxy_name), proxy_name);
                } else {
                    menu.setHeaderTitle(R.string.proxy_context_none_selected);
                }
                menu_add(menu, R.string.proxy_context_cancel, true, null);
            }
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        Log.d(TAG, "CLI: onContextItemSelected");
        String prof_name;
        String proxy_name;
        switch (item.getItemId()) {
            case R.string.profile_context_cancel /*2131034278*/:
            case R.string.proxy_context_cancel /*2131034308*/:
                return true;
            case R.string.profile_context_forget_creds /*2131034279*/:
                ProfileList proflist = profile_list();
                if (proflist == null) {
                    return true;
                }
                Profile prof = proflist.get_profile_by_name(get_menu_key(item));
                if (prof == null) {
                    return true;
                }
                prof_name = prof.get_name();
                this.pwds.remove("pk", prof_name);
                this.pwds.remove("auth", prof_name);
                prof.forget_cert();
                ui_setup(is_active(), UIF_RESET, null);
                return true;
            case R.string.profile_context_menu_change_profile /*2131034280*/:
                this.profile_spin.performClick();
                return true;
            case R.string.profile_context_menu_create_shortcut /*2131034281*/:
                prof_name = get_menu_key(item);
                if (prof_name == null) {
                    return true;
                }
                launch_create_profile_shortcut_dialog(prof_name);
                return true;
            case R.string.profile_context_menu_delete /*2131034282*/:
                prof_name = get_menu_key(item);
                if (prof_name == null) {
                    return true;
                }
                submitDeleteProfileIntentWithConfirm(prof_name);
                return true;
            case R.string.profile_context_menu_rename /*2131034283*/:
                prof_name = get_menu_key(item);
                if (prof_name == null) {
                    return true;
                }
                launch_rename_profile_dialog(prof_name);
                return true;
            case R.string.proxy_context_change_proxy /*2131034309*/:
                this.proxy_spin.performClick();
                return true;
            case R.string.proxy_context_delete /*2131034310*/:
                delete_proxy_with_confirm(get_menu_key(item));
                return true;
            
            case R.string.proxy_context_forget_creds /*2131034313*/:
                proxy_name = get_menu_key(item);
                ProxyList proxy_list = get_proxy_list();
                if (proxy_list == null) {
                    return true;
                }
                proxy_list.forget_creds(proxy_name);
                proxy_list.save();
                return true;
            default:
                return RETAIN_AUTH;
        }
    }

    private void launch_create_profile_shortcut_dialog(final String prof_name) {
        View view = getLayoutInflater().inflate(R.layout.create_shortcut_dialog, null);
        final EditText name_field = (EditText) view.findViewById(R.id.shortcut_name);
        name_field.setText(prof_name);
        name_field.selectAll();
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case -1:
                        OpenVPNClient.this.createConnectShortcut(prof_name, name_field.getText().toString());
                        return;
                    default:
                        return;
                }
            }
        };
        new Builder(this).setTitle(R.string.create_shortcut_title).setView(view).setPositiveButton(R.string.create_shortcut_yes, dialogClickListener).setNegativeButton(R.string.create_shortcut_cancel, dialogClickListener).show();
    }

    private void launch_rename_profile_dialog(final String orig_prof_name) {
        View view = getLayoutInflater().inflate(R.layout.rename_profile_dialog, null);
        final EditText name_field = (EditText) view.findViewById(R.id.rename_profile_name);
        name_field.setText(orig_prof_name);
        name_field.selectAll();
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case -1:
                        OpenVPNClient.this.submitRenameProfileIntent(orig_prof_name, name_field.getText().toString());
                        return;
                    default:
                        return;
                }
            }
        };
        new Builder(this).setTitle(R.string.rename_profile_title).setView(view).setPositiveButton(R.string.rename_profile_yes, dialogClickListener).setNegativeButton(R.string.rename_profile_cancel, dialogClickListener).show();
    }

    private void delete_proxy_with_confirm(final String proxy_name) {
        final ProxyList proxy_list = get_proxy_list();
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case -1:
                        if (proxy_list != null) {
                            proxy_list.remove(proxy_name);
                            proxy_list.save();
                            OpenVPNClient.this.gen_ui_reset_event(OpenVPNClient.RETAIN_AUTH);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        };
        new Builder(this).setTitle(R.string.proxy_delete_confirm_title).setMessage(proxy_name).setPositiveButton(R.string.proxy_delete_confirm_yes, dialogClickListener).setNegativeButton(R.string.proxy_delete_confirm_cancel, dialogClickListener).show();
    }

    

    public PendingIntent get_configure_intent(int requestCode) {
        int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_MUTABLE | 268435456 : 268435456;
        return PendingIntent.getActivity(this, requestCode, getIntent(), flags);
    }

    private void resolve_epki_alias_then_connect() {
        resolveExternalPkiAlias(selected_profile(), new EpkiPost() {
            public void post_dispatch(String alias) {
                OpenVPNClient.this.do_connect(alias);
            }
        });
    }

    private void do_connect(String epki_alias) {
        String app_name = "net.openvpn.connect.android";
        String proxy_name = null;
        String server = null;
        String username = config.getUsername();
        String password = config.getPassword();
        String pk_password = null;
        String response = null;
        boolean is_auth_pwd_save = RETAIN_AUTH;
        String profile_name = selected_profile_name();
        if (this.proxy_group.getVisibility() == 0) {
            ProxyList proxy_list = get_proxy_list();
            if (proxy_list != null) {
                proxy_name = proxy_list.get_enabled(RETAIN_AUTH);
            }
        }
        if (this.server_group.getVisibility() == 0) {
            server = SpinUtil.get_spinner_selected_item(this.server_spin);
        }
        
        if (this.pk_password_group.getVisibility() == 0) {
            pk_password = this.pk_password_edit.getText().toString();
            boolean is_pk_pwd_save = this.pk_password_save_checkbox.isChecked();
            this.prefs.set_boolean_by_profile(profile_name, "pk_password_save", is_pk_pwd_save);
            if (is_pk_pwd_save) {
                this.pwds.set("pk", profile_name, pk_password);
            } else {
                this.pwds.remove("pk", profile_name);
            }
        }
        
        if (this.cr_group.getVisibility() == 0) {
            response = this.response_edit.getText().toString();
        }
        clear_auth();
        String vpn_proto = this.prefs.get_string("vpn_proto");
        String ipv6 = this.prefs.get_string("ipv6");
        String conn_timeout = "480";
        String compression_mode = this.prefs.get_string("compression_mode");
        clear_stats();
        
        submitConnectIntent(profile_name, server, vpn_proto, ipv6, conn_timeout, username, password, is_auth_pwd_save, pk_password, response, epki_alias, compression_mode, proxy_name, null, null, true, get_gui_version(app_name));
    }

    
    private void import_profile(String path) {
        submitImportProfileViaPathIntent(path);
    }

    protected void onActivityResult(int request, int result, Intent data) {
        String str = TAG;
        Object[] objArr = new Object[S_ONSTART_CALLED];
        objArr[0] = Integer.valueOf(request);
        objArr[S_BIND_CALLED] = Integer.valueOf(result);
        Log.d(str, String.format("CLI: onActivityResult request=%d result=%d", objArr));
        String path;
        
        switch (request) {
            case S_BIND_CALLED /*1*/:
                if (result == -1) {
                    resolve_epki_alias_then_connect();
                    return;
                } else if (result != 0) {
                    return;
                } else {
                    if (this.finish_on_connect == FinishOnConnect.ENABLED) {
                        finish();
                        return;
                    } else if (this.finish_on_connect == FinishOnConnect.ENABLED_ACROSS_ONSTART) {
                        this.finish_on_connect = FinishOnConnect.ENABLED;
                        start_connect();
                        return;
                    } else {
                        return;
                    }
                }
            case S_ONSTART_CALLED /*2*/:
                if (result == -1) {
                    path = data.getStringExtra(FileDialog.RESULT_PATH);
                    str = TAG;
                    objArr = new Object[S_BIND_CALLED];
                    objArr[0] = path;
                    Log.d(str, String.format("CLI: IMPORT_PROFILE: %s", objArr));
                    import_config(path);
                    return;
                }
                return;
            case REQUEST_IMPORT_PKCS12 /*3*/:
                if (result == -1) {
                    path = data.getStringExtra(FileDialog.RESULT_PATH);
                    str = TAG;
                    objArr = new Object[S_BIND_CALLED];
                    objArr[0] = path;
                    Log.d(str, String.format("CLI: IMPORT_PKCS12: %s", objArr));
                    import_pkcs12(path);
                    return;
                }
                return;
            default:
                super.onActivityResult(request, result, data);
                return;
        }
    }

    private void import_config(String path)
    {
        try {
            File file = new File(path);
            if (file.getPath().endsWith(".ovpn")) {
                ConfigParser parser = new ConfigParser();
                parser.parseConfig(new InputStreamReader(new FileInputStream(path)));
                VpnProfile vp = parser.convertProfile();
                vp.mName = file.getName();
                if (vp.mConnections[0].mUseCustomConfig) {
                    String proxy = "http-proxy-retry 1\nhttp-proxy 127.0.0.1 8989";
                    vp.mConnections[0].mCustomConfiguration = proxy;
                }
                String file_name = vp.mName;
                String vp_content = String.format("imported\n%s", vp.getConfigFile(this, false));
                if (getOpenVPNService() != null) {
                    getOpenVPNService().addProfile(file_name, vp_content);
                }
                showToast("Import Success!");
            }
        } catch (Exception e) {
            showToast("Import Profile Error: " + e.getMessage());
        }
        // TODO: Implement this method
    }

    private TextView last_visible_edittext() {
        for (int i = 0; i < this.textgroups.length; i += S_BIND_CALLED) {
            if (this.textgroups[i].getVisibility() == 0) {
                return this.textviews[i];
            }
        }
        return null;
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
       /* if (v != last_visible_edittext()) {
            return RETAIN_AUTH;
        }
        if (action_enter(actionId, event) && this.connect_button.getVisibility() == 0) {
            onClick(this.connect_button);
        }*/
        return true;
    }

    private void req_focus(EditText editText) {
        /*boolean auto_keyboard = this.prefs.get_boolean("auto_keyboard", RETAIN_AUTH);
        if (editText != null) {
            editText.requestFocus();
            if (auto_keyboard) {
                raise_keyboard(editText);
                return;
            }
            return;
        }
        this.main_scroll_view.requestFocus();
        if (auto_keyboard) {
            dismiss_keyboard();
        }*/
    }

    private void raise_keyboard(EditText editText) {
        /*InputMethodManager mgr = (InputMethodManager) getSystemService("input_method");
        if (mgr != null) {
            mgr.showSoftInput(editText, S_BIND_CALLED);
        }*/
    }

    private void dismiss_keyboard() {
       /* InputMethodManager mgr = (InputMethodManager) getSystemService("input_method");
        if (mgr != null) {
            TextView[] textViewArr = this.textviews;
            int length = textViewArr.length;
            for (int i = 0; i < length; i += S_BIND_CALLED) {
                mgr.hideSoftInputFromWindow(textViewArr[i].getWindowToken(), 0);
            }
        }*/
    }

    private void load_ui_elements() {
        this.main_scroll_view = (ScrollView) findViewById(R.id.main_scroll_view);
        this.post_import_help_blurb = findViewById(R.id.post_import_help_blurb);
        this.profile_group = findViewById(R.id.profile_group);
        this.proxy_group = findViewById(R.id.proxy_group);
        this.server_group = findViewById(R.id.server_group);
        this.username_group = findViewById(R.id.username_group);
        this.password_group = findViewById(R.id.password_group);
        this.pk_password_group = findViewById(R.id.pk_password_group);
        this.cr_group = findViewById(R.id.cr_group);
        this.conn_details_group = findViewById(R.id.conn_details_group);
        this.stats_group = findViewById(R.id.stats_group);
        this.stats_expansion_group = findViewById(R.id.stats_expansion_group);
        this.info_group = findViewById(R.id.info_group);
        this.button_group = findViewById(R.id.button_group);
        this.profile_spin = (Spinner) findViewById(R.id.profile);
        this.profile_edit = (ImageButton) findViewById(R.id.profile_edit);
        this.proxy_spin = (Spinner) findViewById(R.id.proxy);
        this.proxy_edit = (ImageButton) findViewById(R.id.proxy_edit);
        this.server_spin = (Spinner) findViewById(R.id.server);
        this.challenge_view = (TextView) findViewById(R.id.challenge);
        this.username_edit = (EditText) findViewById(R.id.username);
        this.password_edit = (EditText) findViewById(R.id.password);
        this.pk_password_edit = (EditText) findViewById(R.id.pk_password);
        this.response_edit = (EditText) findViewById(R.id.response);
        this.password_save_checkbox = (CheckBox) findViewById(R.id.password_save);
        this.pk_password_save_checkbox = (CheckBox) findViewById(R.id.pk_password_save);
        this.status_view = (TextView) findViewById(R.id.status);
        this.status_icon_view = (ImageView) findViewById(R.id.status_icon);
        this.progress_bar = (ProgressBar) findViewById(R.id.progress);
        this.connect_button = (Button) findViewById(R.id.connect);
        this.disconnect_button = (Button) findViewById(R.id.disconnect);
        this.details_more_less = (TextView) findViewById(R.id.details_more_less);
        this.last_pkt_recv_view = (TextView) findViewById(R.id.last_pkt_recv);
        this.duration_view = (TextView) findViewById(R.id.duration);
        this.bytes_in_view = (TextView) findViewById(R.id.bytes_in);
        this.bytes_out_view = (TextView) findViewById(R.id.bytes_out);
        this.connect_button.setOnClickListener(this);
        this.disconnect_button.setOnClickListener(this);
        this.profile_spin.setOnItemSelectedListener(this);
        this.proxy_spin.setOnItemSelectedListener(this);
        this.server_spin.setOnItemSelectedListener(this);
        registerForContextMenu(this.profile_spin);
        registerForContextMenu(this.proxy_spin);
        findViewById(R.id.conn_details_boxed).setOnTouchListener(this);
        this.profile_edit.setOnClickListener(this);
        registerForContextMenu(this.profile_edit);
        this.proxy_edit.setOnClickListener(this);
        registerForContextMenu(this.proxy_edit);
        this.username_edit.setOnEditorActionListener(this);
        this.password_edit.setOnEditorActionListener(this);
        this.pk_password_edit.setOnEditorActionListener(this);
        this.response_edit.setOnEditorActionListener(this);
        this.textgroups = new View[]{this.cr_group, this.password_group, this.pk_password_group, this.username_group};
        this.textviews = new EditText[]{this.response_edit, this.password_edit, this.pk_password_edit, this.username_edit}; XMLRPC.initialize(this);
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        super.onBackPressed();
    }

}
