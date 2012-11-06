package no.whg.workout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

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
        mViewPager.setCurrentItem(1);
        
        // THIS DOES NOT WORK
        if(mViewPager.getCurrentItem() == 3) {
        	 // set up gridview
            GridView gridview = (GridView) findViewById(R.id.tab4);
            gridview.setAdapter(new ImageAdapter(this));
            
            
            gridview.setOnItemClickListener(new OnItemClickListener() {
            	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            		Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            		
            	}
            });
        	
        }
       

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	if(item.getItemId() == R.id.menu_settings) {
    		startActivity(new Intent(this, SettingsActivity.class));
    	}
    	
    	if(item.getItemId() == R.id.menu_guide) {
    		startActivity(new Intent(this, GuideActivity.class));
    	}
    	
    	if(item.getItemId() == R.id.menu_music) {
    		// start the music player
    	}
    	
        return true;
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
        	
            
            return inflater.inflate(tabLayout, container, false);
            //return textView;
        }
    }
    
    public class ImageAdapter extends BaseAdapter {
    	private Context mContext;
    	
		Integer[] mThumbIds = {
				R.drawable.ic_action_play, R.drawable.ic_action_search,
				R.drawable.ic_launcher, R.drawable.topbaricon
				
		};
    	
    	public ImageAdapter(Context c) {
    		mContext = c;
    	}
    	
    	public int getCount() {
    		return mThumbIds.length;
    	}
    	
    	public Object getItem(int position) {
    		return null;
    	}
    	
    	public long getItemId(int position) {
    		return 0;
    	}
    	
    	public View getView(int position, View convertView, ViewGroup parent) {
    		ImageView imageView;
    		if(convertView == null) {
    			imageView = new ImageView(mContext);
    			imageView.setLayoutParams(new GridView.LayoutParams(85,85));
    			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    			imageView.setPadding(8, 8, 8, 8);
    			
    		} else {
    			imageView = (ImageView) convertView;
    		}
    		imageView.setImageResource(mThumbIds[position]);
			return imageView;

    	}
    }
}
