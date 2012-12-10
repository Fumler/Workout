package no.whg.workout;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	public static StrongLiftsCalculator SLCalc = new StrongLiftsCalculator();

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * sections. We use a {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will
     * keep every loaded fragment in memory. If this becomes too memory intensive, it may be best
     * to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    
    TextView testText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @SuppressWarnings("deprecation")
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	if(item.getItemId() == R.id.menu_settings) {
    		startActivity(new Intent(this, SettingsActivity.class));
    	}
    	
    	if(item.getItemId() == R.id.menu_guide) {
    		startActivity(new Intent(this, GuideActivity.class));
    	}
    	
    	if(item.getItemId() == R.id.menu_music) {	//THIS DOES NOT WANT TO WORK ON 4.0
    		try {
    			Intent i = new Intent(Intent.ACTION_MAIN);
        		i.addCategory(Intent.CATEGORY_APP_MUSIC);
        		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		startActivity(i);
    		} catch (Exception e){
    			try {
	        		Intent i = new Intent(Intent.ACTION_VIEW);
	        		i.setAction(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
	        		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        		startActivity(i);
    			} catch (Exception x) {
    				//TOAST
    			}
    		}
    	}
    	
    	if(item.getItemId() == R.id.menu_help) {
    		DialogFragment dialog = new HelpDialog(mViewPager.getCurrentItem());
    		dialog.show(getFragmentManager(), "Æ");
    	}
    	
        return true;
        
    }
        
    public void videoCapture(View view){
//    	String lift = "SL_VID_";
//    	Intent intent = new Intent(this, MediaCaptureActivity.class);
//    	intent.putExtra("MEDIA_TYPE", 2);
//		ImageButton ib = (ImageButton) view;
//		if (ib.equals(findViewById(R.id.btn_video0))) {
//			//lift += exercises[0].getName();
//		} else if (ib.equals(findViewById(R.id.btn_video1))) {
//			//lift += exercises[1].getName();
//		} else if (ib.equals(findViewById(R.id.btn_video2))) {
//			//lift += exercises[2].getName();
//		} else {
//			// oops!
//			
//		}
//		
//    	intent.putExtra("lift", lift);
//
//		this.startActivity(intent);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new MainFragment();
            Bundle args = new Bundle();
            args.putInt(MainFragment.ARG_SECTION_NUMBER, i);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return getString(R.string.title_section1).toUpperCase();
                case 1: return getString(R.string.title_section2).toUpperCase();
                case 2: return getString(R.string.title_section3).toUpperCase();
                case 3: return getString(R.string.title_section4).toUpperCase();
            }
            return null;
        }
    }


    public static class MainFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	super.onCreate(savedInstanceState);
        	Bundle args = getArguments();
        	int position = args.getInt(ARG_SECTION_NUMBER);
        	int tabLayout = 1;

        	switch(position) {
        	case 0:
        		tabLayout = R.layout.tab1;
        		break;
        	case 1:
        		tabLayout = R.layout.tab2;
        		break;
        	case 2:
        		tabLayout = R.layout.tab3;
        		break;
        	case 3:
        		tabLayout = R.layout.tab4;
        		break;
        		
        	}
        	
        	View view = inflater.inflate(tabLayout, container, false);

            return view;
            //return textView;
        }

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
			
        	TextView tv = (TextView) getActivity().findViewById(R.id.stats_squats);
        	tv.setText("DEPRSTgjhndkpgfhn");
		}
    }
}
