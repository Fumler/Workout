package no.whg.workout;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class GuideActivity extends FragmentActivity {

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

	private static VideoView video;
	private static MediaController mc;
	private static ProgressDialog pd;
	protected static String [] videos;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0);
        videos = new String[] {
        	"http://hum.re/getfit/squat.mp4",
        	"http://hum.re/getfit/bench.mp4",
        	"http://hum.re/getfit/deadlift.mp4",
        	"http://hum.re/getfit/ohp.mp4",
        	"http://hum.re/getfit/rowing.mp4",
        };
        
        
        
        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("youtube-lenke")));
        // Denne vil trigge spørsmål om du vil launche youtube-appen, med mindre default allerede er satt. 
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_guide, menu);
        getActionBar().setDisplayShowTitleEnabled(false);
        return true;
    }

    
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//    	
//    	if(item.getItemId() == R.id.menu_settings) {
//    		startActivity(new Intent(this, SettingsActivity.class));
//    	}
//    	
//    	if(item.getItemId() == R.id.menu_guide) {
//    		startActivity(new Intent(this, GuideActivity.class));
//    	}
//    	
//    	if(item.getItemId() == R.id.menu_music) {
//    		// start the music player
//    	}
//    	
//    	if(item.getItemId() == android.R.id.home) {
//    		Intent intent = new Intent(this, MainActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//    	}
//        return true;
//    }
    
    @SuppressWarnings("deprecation")
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	if(item.getItemId() == R.id.menu_settings) {
    		startActivity(new Intent(this, SettingsActivity.class));
    	}
    	
    	if (item.getItemId() == R.id.menu_camera){
    		Intent intent = new Intent(GuideActivity.this, MediaCaptureActivity.class);
        	intent.putExtra("MEDIA_TYPE", 1);
        	intent.putExtra("method","yes");
        	//MainActivity.imgAdapt.setRunning(false);	// lets the gallery know that images have to be reloaded
        	startActivity(intent);
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
    	
    	if(item.getItemId() == android.R.id.home) {
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
    	}
    	
        return true;
        
    }

    static void displayVideo(){
    	
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
            Fragment fragment = new GuideFragment();
            Bundle args = new Bundle();
            args.putInt(GuideFragment.ARG_SECTION_NUMBER, i);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return getString(R.string.guide_title_section1).toUpperCase();
                case 1: return getString(R.string.guide_title_section2).toUpperCase();
                case 2: return getString(R.string.guide_title_section3).toUpperCase();
                case 3: return getString(R.string.guide_title_section4).toUpperCase();
                case 4: return getString(R.string.guide_title_section5).toUpperCase();
                case 5: return getString(R.string.guide_title_section6).toUpperCase();
            }
            return null;
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class GuideFragment extends Fragment {
        
    	public TextView tv_strongLiftsGuide_p1;
    	public TextView tv_strongLiftsGuide_p2;
    	public TextView tv_strongLiftsGuide_p3;
    	public TextView tv_strongLiftsGuide_p4;
    	public TextView tv_strongLiftsGuide_p5;
    	public TextView tv_strongLiftsGuide_p6;
    	public TextView tv_squatsGuide;
    	public TextView tv_benchPressGuide;
    	public TextView tv_deadLiftGuide;
    	public TextView tv_ohpGuide;
    	public TextView tv_rowingGuide;
    	
    	public GuideFragment() {
        }

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                				 Bundle savedInstanceState) 
        {
        	super.onCreate(savedInstanceState);
        	Bundle args = getArguments();
        	int position = args.getInt(ARG_SECTION_NUMBER);
        	int tabLayout = 1;

        	switch(position) {
        	case 0:
        		tabLayout = R.layout.guide_tab1;
        		break;
        	case 1:
        		tabLayout = R.layout.guide_tab2;
        		break;
        	case 2:
        		tabLayout = R.layout.guide_tab3;
        		break;
        	case 3:
        		tabLayout = R.layout.guide_tab4;
        		break;
        	case 4:
        		tabLayout = R.layout.guide_tab5;
        		break;
        	case 5:
        		tabLayout = R.layout.guide_tab6;
        		break;
        		
        	}
        	
        	View view = inflater.inflate(tabLayout, container, false);
        	
            return view;
            
        }
        
        
        @Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
			
        	Bundle args = getArguments();
        	int position = args.getInt(ARG_SECTION_NUMBER);

        	switch(position) {
        	case 0:
        		initTab1();
        		break;
        	case 1:
        		initTab2();
        		break;
        	case 2:
        		initTab3();
        		break;
        	case 3:
        		initTab4();
        		break;
        	case 4:
        		initTab5();
        		break;
        	case 5:
        		initTab6();
        		break;
        		
        	}
		}



		private void initTab1()
        {
        	tv_strongLiftsGuide_p1 = (TextView) getActivity().findViewById(R.id.tv_strongLiftsGuide_p1);
        	tv_strongLiftsGuide_p1.setText(Html.fromHtml(getString(R.string.guide_tv_p1)));
        	
        	tv_strongLiftsGuide_p2 = (TextView) getActivity().findViewById(R.id.tv_strongLiftsGuide_p2);
        	tv_strongLiftsGuide_p2.setText(Html.fromHtml(getString(R.string.guide_tv_p2)));
        	
        	tv_strongLiftsGuide_p3 = (TextView) getActivity().findViewById(R.id.tv_strongLiftsGuide_p3);
        	tv_strongLiftsGuide_p3.setText(Html.fromHtml(getString(R.string.guide_tv_p3)));
        	
        	tv_strongLiftsGuide_p4 = (TextView) getActivity().findViewById(R.id.tv_strongLiftsGuide_p4);
        	tv_strongLiftsGuide_p4.setText(Html.fromHtml(getString(R.string.guide_tv_p4)));
        	
        	tv_strongLiftsGuide_p5 = (TextView) getActivity().findViewById(R.id.tv_strongLiftsGuide_p5);
        	tv_strongLiftsGuide_p5.setText(Html.fromHtml(getString(R.string.guide_tv_p5)));
        	
        	tv_strongLiftsGuide_p6 = (TextView) getActivity().findViewById(R.id.tv_strongLiftsGuide_p6);
        	tv_strongLiftsGuide_p6.setText(Html.fromHtml(getString(R.string.guide_tv_p6)));
        	
        	tv_strongLiftsGuide_p6.setMovementMethod(LinkMovementMethod.getInstance());
        }
		
		private void initTab2()
		{
			tv_squatsGuide = (TextView) getActivity().findViewById(R.id.tv_squatsGuide);
			tv_squatsGuide.setText(Html.fromHtml(getString(R.string.guide_tv_squats)));
			
			tv_squatsGuide.setMovementMethod(LinkMovementMethod.getInstance());
		}
		
		private void initTab3()
		{
			tv_benchPressGuide = (TextView) getActivity().findViewById(R.id.tv_benchPressGuide);
			tv_benchPressGuide.setText(Html.fromHtml(getString(R.string.guide_tv_bench)));
			
			tv_benchPressGuide.setMovementMethod(LinkMovementMethod.getInstance());
		}
		
		private void initTab4()
		{
			tv_deadLiftGuide = (TextView) getActivity().findViewById(R.id.tv_deadliftGuide);
			tv_deadLiftGuide.setText(Html.fromHtml(getString(R.string.guide_tv_deadlift)));
			
			tv_deadLiftGuide.setMovementMethod(LinkMovementMethod.getInstance());
		}
		
		private void initTab5()
		{
			tv_ohpGuide = (TextView) getActivity().findViewById(R.id.tv_ohpGuide);
			tv_ohpGuide.setText(Html.fromHtml(getString(R.string.guide_tv_ohp)));
			
			tv_ohpGuide.setMovementMethod(LinkMovementMethod.getInstance());
		}
		
		private void initTab6()
		{
			tv_rowingGuide = (TextView) getActivity().findViewById(R.id.tv_rowingGuide);
			tv_rowingGuide.setText(Html.fromHtml(getString(R.string.guide_tv_rowing)));
			
			tv_rowingGuide.setMovementMethod(LinkMovementMethod.getInstance());
		}
        
        @Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			
			Bundle args = getArguments();
			int position = args.getInt(ARG_SECTION_NUMBER);

    
			switch(position) {
			case 0:
				// Tab 1 - 
				break;
			default:
				System.out.println("position is: " + position);
//	        	if (position != 0) {
//	        		initVideo(view, position, getActivity());
//		        	video.setOnPreparedListener(new OnPreparedListener() {
//		    			public void onPrepared(MediaPlayer arg0) {
//		    				pd.dismiss();
//		    				//video.start();
//		    			}
//		    		});
//	        	}
			}
		}
    }
    
//    protected static void initVideo(View view, int tab, Context c){
//    	//Uri uri = Uri.parse("http://hum.re/getfit/squat.mp4");
//    	if (networkAvailable(c)){
//			// Select the videoview from the xml
//	    	switch (tab){
//		    case 1:
//				video = (VideoView) view.findViewById(R.id.vi_guidetab2);
//				break;
//		    case 2:
//				video = (VideoView) view.findViewById(R.id.vi_guidetab3);
//				break;
//		    case 3:
//				video = (VideoView) view.findViewById(R.id.vi_guidetab4);
//				break;
//		    case 4:
//				video = (VideoView) view.findViewById(R.id.vi_guidetab5);
//				break;
//		    case 5:
//				video = (VideoView) view.findViewById(R.id.vi_guidetab6);
//				break;
//		    }
//	    	Uri uri = Uri.parse(videos[tab - 1]);
//			video.setVideoURI(uri);
//	
//			// Shows a progressdialog until the video is fully loaded to reduce interface lag
//			//  and to make it less confusing for the user
//			pd = ProgressDialog.show(c, "Video", "Loading...");
//	
//			mc = new MediaController(c);
//			mc.setMediaPlayer(video);
//			video.setMediaController(mc);
//    	} else {
//    		// no fucking network
//    	}
//    }
    
    protected static boolean networkAvailable(Context c) {
    	final ConnectivityManager conMgr = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
              return true;
        } else {
              System.out.println("Internet Connection Not Present");
            return false;
        }
    }
}
