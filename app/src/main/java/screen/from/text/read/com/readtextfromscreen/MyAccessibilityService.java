package screen.from.text.read.com.readtextfromscreen;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.FloatProperty;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.attr.x;
import static screen.from.text.read.com.readtextfromscreen.Converter.Unit.m;


public class MyAccessibilityService extends AccessibilityService {

    AccessibilityEvent event1;
    private WindowManager windowManager;
    private RelativeLayout chatheadView;
    RelativeLayout promptsView;
    RelativeLayout conversionLayout;
    Button closeButton;
    String number;
    RecyclerView recyclerView;
    AccessibilityNodeInfo nodeInfo;

    String text;
    String text2;
    String text3;
    String text4;
    String savedText;
    String key;
    String wordToReplace;

    SharedPreference sharedPreference;

    ArrayList<Shortcuts> shortcuts;
    ArrayList<String> names;
    ArrayList<HashMap<String, String>> contacts;

    WindowManager.LayoutParams paramsButton;
    WindowManager.LayoutParams paramsLayout;
    WindowManager.LayoutParams paramsLayoutconversion;

    private String getEventText(AccessibilityEvent event) {
        StringBuilder sb = new StringBuilder();
        for (CharSequence s : event.getText()) {
            sb.append(s);
        }
        return sb.toString();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        text = getEventText(event);
        Bundle bundle = new Bundle();

        if (event.getSource() != null && event.getSource().getText() != null && !event.isPassword())
            if (event.getClassName().equals("android.widget.EditText")) {
                String[] array1 = text.split(" ");
                loadFavorites();
                if (shortcuts != null)
                    for (String wordFromEditText : array1)
                        for (Shortcuts savedWords : shortcuts) {
                            if (wordFromEditText.equals(savedWords.shortcut)) {
                                String newText = text.replace(wordFromEditText, savedWords.phrase);
                                bundle.putCharSequence(nodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, newText);
                                nodeInfo.performAction(2097152, bundle);
                                Bundle bundle2 = new Bundle();
                                bundle2.putInt("ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT", 4);
                                nodeInfo.performAction(256, bundle2);
                                nodeInfo.recycle();
                            }
                        }
            }


        if (event.getSource() != null && event.getSource().getText() != null && !event.isPassword())
            if (event.getClassName().equals("android.widget.EditText")) {
                if (text.contains("@@")) {
                    String words[] = text.split(" ");
                    for (String i : words)
                        if (i.charAt(0) == '@' && i.charAt(1) == '@') {
                            key = i.replace("@@", "");
                            wordToReplace = i;
                        }

                    nodeInfo = event.getSource();
                    chatheadView.setVisibility(View.VISIBLE);
                    text2 = getEventText(event);
                    event1 = event;
                }
            } else chatheadView.setVisibility(View.GONE);


        if (event.getSource() != null && event.getSource().getText() != null && event.getClassName().equals("android.widget.EditText")) {
            nodeInfo = event.getSource();
            text3 = getEventText(event);
            String[] parts = text3.split(" ");
            nodeInfo = event.getSource();

            ArrayList<String> array = new ArrayList<>();
            array.add("in".toLowerCase());
            array.add("inch".toLowerCase());
            array.add("inches".toLowerCase());

            array.add("cm".toLowerCase());
            array.add("centimeter".toLowerCase());
            array.add("centimeters".toLowerCase());

            array.add("ft".toLowerCase());
            array.add("foot".toLowerCase());
            array.add("feet".toLowerCase());

            array.add("yd".toLowerCase());
            array.add("yard".toLowerCase());
            array.add("yards".toLowerCase());

            array.add("m".toLowerCase());
            array.add("meter".toLowerCase());
            array.add("meters".toLowerCase());

            array.add("mi".toLowerCase());
            array.add("mile".toLowerCase());
            array.add("miles".toLowerCase());

            array.add("km".toLowerCase());
            array.add("kilometer".toLowerCase());
            array.add("kilometers".toLowerCase());

            for (String i : parts) {
                if (i.contains("to")) {
                    String x = i;
                    i = i.replace("to", " to ");

                    savedText = i;

                    Log.e("SIZE", String.valueOf(parts.length));

                    String[] parts2 = i.split(" ");

                    if (parts2.length > 2) {

                        String fromUnit = "";
                        String toUnit = "";

                        Log.e("SIZE2", String.valueOf(parts2.length));

                        toUnit = parts2[2];
                        int digit = 0;
                        for (int j = 0; j < parts2[0].length(); j++) {
                            if (Character.isDigit(parts2[0].charAt(j)))
                                digit = (digit * 10) + Integer.parseInt(String.valueOf(parts2[0].charAt(j)));
                        }
                        for (int j = 0; j < parts2[0].length(); j++) {
                            if (!Character.isDigit(parts2[0].charAt(j)))
                                fromUnit = fromUnit.concat(String.valueOf(parts2[0].charAt(j)));
                        }

                        for (String h : array)
                            for (String k : array)
                                if (fromUnit.equals(h) && toUnit.equals(k)) {
                                    text4 = text3.replace(x, savedText);
                                    text3 = text3.replace(x, convert(String.valueOf(digit), fromUnit, toUnit));
                                    conversionLayout.setVisibility(View.VISIBLE);
                                }
                    }
                }
            }
        }
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);
    }

    public void loadFavorites() {
        shortcuts = sharedPreference.loadFavorites(this);
    }

    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        sharedPreference = new SharedPreference();

        names = new ArrayList<>();
        loadFavorites();

        chatheadView = (RelativeLayout) LayoutInflater.from(MyAccessibilityService.this).inflate(R.layout.contacts_layout, null);
        //chatheadView = new ImageView(this);
        //chatheadView.setBackground(getResources().getDrawable(R.drawable.circle));
        //chatheadView.setElevation(20);

        chatheadView.setVisibility(View.GONE);
        paramsButton = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        paramsButton.height = 80 * (int) metrics.density;
        paramsButton.width = 80 * (int) metrics.density;
        paramsButton.gravity = Gravity.CENTER;
        paramsButton.x = 0;
        paramsButton.y = 100;

        windowManager.addView(chatheadView, paramsButton);

        promptsView = (RelativeLayout) LayoutInflater.from(MyAccessibilityService.this).inflate(R.layout.dialog_box, null);
        paramsLayout = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        paramsLayout.y = -300;
        paramsLayout.height = 300 * (int) metrics.density;
        paramsLayout.width = 300 * (int) metrics.density;
        paramsLayout.alpha = 0.98f;

        conversionLayout = (RelativeLayout) LayoutInflater.from(MyAccessibilityService.this).inflate(R.layout.conversion_layout, null);
        paramsLayoutconversion = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        paramsLayoutconversion.alpha = 0.98f;
        paramsLayoutconversion.y = 150;
        windowManager.addView(conversionLayout, paramsLayoutconversion);
        conversionLayout.setVisibility(View.GONE);


        RelativeLayout button1 = (RelativeLayout) conversionLayout.findViewById(R.id.fab1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                if (nodeInfo != null) {
                    bundle.putCharSequence(nodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text3);
                    nodeInfo.performAction(2097152, bundle);
                    Bundle bundle2 = new Bundle();
                    bundle2.putInt("ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT", 4);
                    nodeInfo.performAction(256, bundle2);
                    nodeInfo.recycle();
                }
                conversionLayout.setVisibility(View.GONE);
            }
        });

        RelativeLayout button2 = (RelativeLayout) conversionLayout.findViewById(R.id.fab2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                if (nodeInfo != null) {
                    bundle.putCharSequence(nodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text4);
                    nodeInfo.performAction(2097152, bundle);
                    Bundle bundle2 = new Bundle();
                    bundle2.putInt("ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT", 4);
                    nodeInfo.performAction(256, bundle2);
                    nodeInfo.recycle();
                }
                conversionLayout.setVisibility(View.GONE);
            }
        });

        closeButton = (Button) promptsView.findViewById(R.id.close_button);

        chatheadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                windowManager.addView(promptsView, paramsLayout);
                getContactsByKey();
                stopSelf();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                windowManager.removeView(promptsView);
                chatheadView.setVisibility(View.GONE);
            }
        });

        recyclerView = (RecyclerView) promptsView.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new Wrap(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        getContacts();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        number = intent.getStringExtra("number");

        Bundle bundle = new Bundle();
        text2 = text2.replace(wordToReplace, number);

        if (intent.getStringExtra("action").equals("replace"))
            if (nodeInfo != null) {
                bundle.putCharSequence(nodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text2);
                nodeInfo.performAction(2097152, bundle);
                Bundle bundle2 = new Bundle();
                bundle2.putInt("ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT", 4);
                nodeInfo.performAction(256, bundle2);
                nodeInfo.recycle();
            }

        if (intent.getStringExtra("action").equals("copy")) {
            ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", number);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
        }

        windowManager.removeView(promptsView);
        chatheadView.setVisibility(View.GONE);

        return START_STICKY;
    }

    public void getContacts() {
        ArrayList<HashMap<String, String>> contactData = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            try {
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                    while (phones.moveToNext()) {
                        //if (name.toLowerCase().contains(key.toLowerCase())) {
                        names.add(name);
                        String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        HashMap<String, String> map = new HashMap<>();
                        if (phoneNumber.contains(" "))
                            phoneNumber = phoneNumber.replace(" ", "");
                        map.put("name", name);
                        map.put("number", phoneNumber);
                        contactData.add(map);
                    }
                    //}
                    phones.close();
                }
            } catch (Exception e) {
            }
        }
        contacts = contactData;
    }

    ArrayList<HashMap<String, String>> array;

    public void getContactsByKey() {
        array = new ArrayList<>();
        Log.e("Tag", String.valueOf(contacts.size()));
        for (HashMap<String, String> i : contacts)
            if (i.get("name").toLowerCase().contains(key.toLowerCase()))
                array.add(i);

        if (array.size() > 0)
            recyclerView.setAdapter(new Adapter_Contacts(array));
        else recyclerView.setAdapter(new Adapter_Contacts(contacts));
    }

    public String convert(String input1, String fromString, String toString) {
        double input = Double.valueOf(input1);
        // Convert the strings to something in our Unit enu,
        Converter.Unit fromUnit = Converter.Unit.fromString(fromString);
        Converter.Unit toUnit = Converter.Unit.fromString(toString);

        // Create a converter object and convert!
        Converter converter = new Converter(fromUnit, toUnit);
        String result = converter.convert(input);

        return result;
    }
}