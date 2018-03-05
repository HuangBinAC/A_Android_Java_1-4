package com.example.v_bihuan.androidjava;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.distribute.Distribute;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.microsoft.appcenter.push.Push;
import com.microsoft.appcenter.crashes.CrashesListener;
import com.microsoft.appcenter.crashes.ingestion.models.ErrorAttachmentLog;
import com.microsoft.appcenter.crashes.model.ErrorReport;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Push.setListener(new MyPushListener());
        Push.setSenderId("243527024264");
        // AppCenter.setLogUrl("https://in-staging-south-centralus.staging.avalanch.es");
        AppCenter.start(getApplication(), "6d44c9f3-d509-42e0-80da-4531aadf369f",
                Distribute.class, Analytics.class, Crashes.class, Push.class);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//        LinearLayout layout = (LinearLayout) findViewById(R.id.crash);
//
//        Button bn = new Button(this);
//        bn.setText("点击");
//        bn.setLayoutParams(new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));
//
//        layout.addView(bn);
//
//        bn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                ComponentName comp = new ComponentName(MainActivity.this, MainActivity.class);
//                Intent intent = new Intent();
//                intent.setComponent(comp);
//                startActivity(intent);
//                Crashes.notifyUserConfirmation(Crashes.SEND); //+
//            }
//        });



        // CrashesListener //+
        CrashesListener customListener = new CrashesListener() {
            // Implement all callbacks here.
            @Override
            public boolean shouldProcess(ErrorReport report) {
                Crashes.notifyUserConfirmation(Crashes.ALWAYS_SEND); //+
                return true; // return true if the crash report should be processed, otherwise false.

            }

            @Override
            public boolean shouldAwaitUserConfirmation() {
                // Build your own UI to ask for user consent here. SDK does not provide one by default.

                // Return true if you just built a UI for user consent and are waiting for user input on that custom UI, otherwise false.
                Crashes.notifyUserConfirmation(Crashes.SEND); //+
                return true;

            }

            @Override
            public Iterable<ErrorAttachmentLog> getErrorAttachments(ErrorReport report) {

                /* Attach some text. */
                ErrorAttachmentLog textLog = ErrorAttachmentLog.attachmentWithText("This is a text attachment.", "text.txt");

                /* Attach app icon. */
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitMapData = stream.toByteArray();
                ErrorAttachmentLog binaryLog = ErrorAttachmentLog.attachmentWithBinary(bitMapData, "ic_launcher.jpeg", "image/jpeg");

                /* Return attachments as list. */
                return Arrays.asList(textLog, binaryLog);
            }

            @Override
            public void onBeforeSending(ErrorReport report) {
                // Your code, e.g. to present a custom UI.
            }

            @Override
            public void onSendingFailed(ErrorReport report, Exception e) {
                // Your code goes here.
            }

            @Override
            public void onSendingSucceeded(ErrorReport report) {
                // Your code, e.g. to hide the custom UI.
            }
        };
        Crashes.setListener(customListener); //+

    }

//    private Button btnAnalytics,btnCrash;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        btnAnalytics = (Button) findViewById(R.id.analytics);
//        btnCrash = (Button) findViewById(R.id.crash);
//        btnCrash.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(btnTwo.getText().toString().equals("按钮不可用")){
//                    btnOne.setEnabled(false);
//                    btnTwo.setText("按钮可用");
//                }else{
//                    btnOne.setEnabled(true);
//                    btnTwo.setText("按钮不可用");
//                }
//            }
//            Crashes.notifyUserConfirmation(Crashes.SEND);
//        });
////        btnAnalytics.setOnClickListener(new View.OnClickListener() {
////
////            @Override
////            public void onClick(View v) {
////
////            }
////        });
//
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        Analytics.trackEvent("Video clicked"); //+
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Analytics.trackEvent("Video clicked"); //+
//            Crashes.generateTestCrash(); //+
            int i = 5/0;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            Crashes.hasCrashedInLastSession();//+

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Crashes.getLastSessionCrashReport(); //+
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }



    }


}
