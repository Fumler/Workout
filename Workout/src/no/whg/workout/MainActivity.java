package no.whg.workout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;
import java.lang.Runnable;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	public static boolean resetPressed;

	public static StrongLiftsCalculator SLCalc;
	String SLCalcFILENAME = "SLCALCOBJECT";
	
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
    
	//variable for selection intent
	private final static int PICKER = 1;
	//variable to store the currently selected image
	private static int currentPic = 0;
	//adapter for gallery view
	private static PicAdapter imgAdapt;
	//gallery object
	private static Gallery picGallery;
	//image view for larger display
	private static ImageView picView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        FileInputStream fis = null;
        
		// Checking to see if the file with the object exists.
		try {
			fis = getApplicationContext().openFileInput(SLCalcFILENAME);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Inits SLCalc based on existence of file
		if (fis != null) {
			loadSLCalc(fis);
		} else{
			SLCalc = new StrongLiftsCalculator();
		}
        
        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);
        imgAdapt = new PicAdapter(getApplicationContext());
        updateGallery();
        
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        getActionBar().setDisplayShowTitleEnabled(false);
        return true;
    }
    
    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		saveSLCalc();
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
    	
    	if (item.getItemId() == R.id.menu_camera){
    		Intent intent = new Intent(MainActivity.this, MediaCaptureActivity.class);
        	intent.putExtra("MEDIA_TYPE", 1);
        	intent.putExtra("method","yes");
        	startActivity(intent);
        	updateGallery();
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
    
	// Loading the SLCalc object
	public void loadSLCalc(FileInputStream fis) {
		if (fis != null) {
			ObjectInputStream is = null;
			try {
				is = new ObjectInputStream(fis);
			} catch (StreamCorruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			StrongLiftsCalculator tempSL = null;
			try {
				tempSL = (StrongLiftsCalculator) is.readObject();
			} catch (OptionalDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SLCalc = tempSL;
		}
	}

	// Saving the SLCalc object
	public void saveSLCalc() {
		FileOutputStream fos = null;
		try {
			// fos sets up a file that is private, which means only this
			// application
			// can access it.
			fos = getApplicationContext().openFileOutput(SLCalcFILENAME, Context.MODE_PRIVATE);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectOutputStream os = null;
		try {
			os = new ObjectOutputStream(fos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			os.writeObject(SLCalc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    public void videoCapture(int i){
    	Exercise exercise = SLCalc.getBothSessions().get(i);
    	String lift = "SL_VID_";
    	Intent intent = new Intent(this, MediaCaptureActivity.class);
    	intent.putExtra("MEDIA_TYPE", 2);
    	
    	lift += exercise.getShortName();
		
    	intent.putExtra("lift", lift);

		this.startActivity(intent);
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
        
        // STATS RELATED TEXTVIEWS
        public TextView tab3_tv_squats;
        public TextView tab3_tv_squats_deloads;
        public TextView tab3_tv_squats_fails;
        public TextView tab3_tv_benchPress;
        public TextView tab3_tv_benchPress_deloads;
        public TextView tab3_tv_benchPress_fails;
        public TextView tab3_tv_rowing;
        public TextView tab3_tv_rowing_deloads;
        public TextView tab3_tv_rowing_fails;
        public TextView tab3_tv_deadlift;
        public TextView tab3_tv_deadlift_deloads;
        public TextView tab3_tv_deadlift_fails;
        public TextView tab3_tv_OHP;
        public TextView tab3_tv_OHP_deloads;
        public TextView tab3_tv_OHP_fails;
        public LineGraphView graphView;
        public GraphViewSeries weightDataSeries;
        public LinearLayout layout;
        
        // LOG WORKOUT RELATED XML STUFF
		public LinearLayout tab1_ll_squats;
		public LinearLayout tab1_ll_benchpress;
		public LinearLayout tab1_ll_rowing;
		public LinearLayout tab1_ll_ohp;
		public LinearLayout tab1_ll_deadlift;
		
		public TextView tab1_tv_squatsTitle;
        public TextView tab1_tv_benchPressTitle;
        public TextView tab1_tv_rowingTitle;
        public TextView tab1_tv_deadliftTitle;
        public TextView tab1_tv_OHPTitle;
		
        public TextView tab1_tv_squats;
        public TextView tab1_tv_benchPress;
        public TextView tab1_tv_rowing;
        public TextView tab1_tv_deadlift;
        public TextView tab1_tv_OHP;
        
        List<ThreeStateCheckbox> tab1_squats = new ArrayList<ThreeStateCheckbox>(5);
        List<ThreeStateCheckbox> tab1_benchpress = new ArrayList<ThreeStateCheckbox>(5);
        List<ThreeStateCheckbox> tab1_rowing = new ArrayList<ThreeStateCheckbox>(5);
        List<ThreeStateCheckbox> tab1_ohp = new ArrayList<ThreeStateCheckbox>(5);
        List<ThreeStateCheckbox> tab1_deadlift = new ArrayList<ThreeStateCheckbox>(5);
        
        public Button tab1_b_log;
        public String weightUnit;
        
        
        
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
        }

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
			
        	Bundle args = getArguments();
        	int position = args.getInt(ARG_SECTION_NUMBER);

        	switch(position) {
        	case 0:
        		// Tab 1 - Log Workout
    			initTab1();
    			refreshTab1();
        		break;
        	case 1:
        		// Tab 2 - Home
        		break;
        	case 2:
        		// Tab 3 - Stats
        		// Initializing the TextViews.
        		initTab3();
        		refreshTab3();
        		break;
        	case 3:
        		// Tab 4 - Gallery
        		initTab4();
        		
        		currentPic = 0;
                //set long click listener for each gallery thumbnail item
          		picGallery.setOnItemLongClickListener(new OnItemLongClickListener() {
          			//handle long clicks
          			public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
          				//update the currently selected position so that we assign the imported bitmap to correct item
          				currentPic = position;
          				//take the user to their chosen image selection app (gallery or file manager)
          				Intent pickIntent = new Intent();
          				pickIntent.setType("image/*");
          				pickIntent.setAction(Intent.ACTION_GET_CONTENT);
          				//handle the returned data in onActivityResult
          				startActivityForResult(Intent.createChooser(pickIntent, "Select Picture"), PICKER);
          				return true;
          			}
          		});

          		//set the click listener for each item in the thumbnail gallery
          		picGallery.setOnItemClickListener(new OnItemClickListener() {
          			//handle clicks
          			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
          				//set the larger image view to display the chosen bitmap calling method of adapter class
          				Matrix matrix = new Matrix();
    					matrix.setRotate(90);
    					picView.setImageBitmap(Bitmap.createBitmap(imgAdapt.getPic(position), 0, 0, imgAdapt.getPic(position).getWidth(), imgAdapt.getPic(position).getHeight(), matrix, false));
          				currentPic = position;
          			}
          		});
    	        break;
        	}
		}
		
		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			
			Bundle args = getArguments();
			int position = args.getInt(ARG_SECTION_NUMBER);
			
			switch(position) {
			case 0:
				// Tab 1 - Log Workout
				break;
			case 1:
				// Tab 2 - Home
				break;
			case 2:
				// Tab 3 - Stats
				refreshTab3();
				break;
			case 3:
				// Tab 4 - Gallery
				initTab4();
				//refreshGallery();
				break;
			}
		}
		
		@Override
		public void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			
			Bundle args = getArguments();
			int position = args.getInt(ARG_SECTION_NUMBER);
			
			switch(position) {
			case 0:
				// Tab 1 - Log Workout
				break;
			case 1:
				// Tab 2 - Home
				break;
			case 2:
				// Tab 3 - Stats
				break;
			case 3:
				// Tab 4 - Gallery
				break;
			}
		}
		
		@Override
		public void onStop() {
			// TODO Auto-generated method stub
			super.onStop();
			
			Bundle args = getArguments();
			int position = args.getInt(ARG_SECTION_NUMBER);
			
			switch(position) {
			case 0:
				// Tab 1 - Log Workout
				break;
			case 1:
				// Tab 2 - Home
				break;
			case 2:
				// Tab 3 - Stats
				break;
			case 3:
				// Tab 4 - Gallery
				break;
			}
		}
		
		//Initializes tab 1
		public void initTab1() {
			tab1_tv_squats 			= (TextView) getActivity().findViewById(R.id.log_squatsDetailed);
			tab1_tv_benchPress 		= (TextView) getActivity().findViewById(R.id.log_benchPressDetailed);
			tab1_tv_rowing 			= (TextView) getActivity().findViewById(R.id.log_rowingDetailed);
			tab1_tv_deadlift 		= (TextView) getActivity().findViewById(R.id.log_deadliftDetailed);
			tab1_tv_OHP 			= (TextView) getActivity().findViewById(R.id.log_ohpDetailed);
			
			tab1_ll_deadlift 		= (LinearLayout) getActivity().findViewById(R.id.log_linearFour);
			tab1_ll_benchpress 		= (LinearLayout) getActivity().findViewById(R.id.log_linearTwo);
			tab1_ll_rowing 			= (LinearLayout) getActivity().findViewById(R.id.log_linearThree);
			tab1_ll_ohp 			= (LinearLayout) getActivity().findViewById(R.id.log_linearFive);

			tab1_tv_squatsTitle 	= (TextView) getActivity().findViewById(R.id.log_squats);
			tab1_tv_benchPressTitle = (TextView) getActivity().findViewById(R.id.log_benchPress);
			tab1_tv_rowingTitle		= (TextView) getActivity().findViewById(R.id.log_rowing);
			tab1_tv_deadliftTitle	= (TextView) getActivity().findViewById(R.id.log_deadlift);
			tab1_tv_OHPTitle		= (TextView) getActivity().findViewById(R.id.log_ohp);
			
			tab1_b_log				= (Button) getActivity().findViewById(R.id.log_button);
			

			for(int i = 0; i < 5; i++) { // counting from 1, fml..
				String squatsId = "log_squats_cb"+i;
				int squatsIdInt = getResources().getIdentifier(squatsId, "id", "no.whg.workout");
				tab1_squats.add(i, (ThreeStateCheckbox) getActivity().findViewById(squatsIdInt));
				
				String benchpressId = "log_benchpress_cb"+i;
				int benchpressIdInt = getResources().getIdentifier(benchpressId, "id", "no.whg.workout");
				tab1_benchpress.add(i, (ThreeStateCheckbox) getActivity().findViewById(benchpressIdInt));
				
				String rowingId = "log_rowing_cb"+i;
				int rowingIdInt = getResources().getIdentifier(rowingId, "id", "no.whg.workout");
				tab1_rowing.add(i,(ThreeStateCheckbox) getActivity().findViewById(rowingIdInt));
				
				String ohpId = "log_OHP_cb"+i;
				int ohpIdInt = getResources().getIdentifier(ohpId, "id", "no.whg.workout");
				tab1_ohp.add(i,(ThreeStateCheckbox) getActivity().findViewById(ohpIdInt));
				
			}
		}
		
		public void refreshTab1() {
			List<Exercise> currentSession = SLCalc.getCurrentSession();
			boolean isA = SLCalc.getSessionTypeA();
			@SuppressWarnings("deprecation")
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
			        ViewGroup.LayoutParams.WRAP_CONTENT);
			
			p.setMargins(15, 15, 15, 15); // left, top, right, bottom
			
			
			// Number in list -> exercise:
			// 0 - Squats
			// 1 - Benchpress
			// 2 - Rowing
			// 3 - OHP
			// 4 - Deadlift
	        
	        setWeightString();

			if(isA) {
				tab1_tv_squats.setText(String.valueOf(currentSession.get(0).getCurrentWeight()) + weightUnit);
				tab1_tv_benchPress.setText(String.valueOf(currentSession.get(1).getCurrentWeight()) + weightUnit);
				tab1_tv_rowing.setText(String.valueOf(currentSession.get(2).getCurrentWeight()) + weightUnit);				
				
				tab1_ll_deadlift.setVisibility(View.GONE);
				tab1_tv_deadliftTitle.setVisibility(View.GONE);
				tab1_ll_ohp.setVisibility(View.GONE);
				tab1_tv_OHPTitle.setVisibility(View.GONE);
				
				p.addRule(RelativeLayout.BELOW, R.id.log_linearThree);
								
				tab1_b_log.setLayoutParams(p);
				
				for(int i = 0; i <= 4; i++) {
			        tab1_squats.get(i).setOnClickListener(new View.OnClickListener() {
			        	public void onClick(View v) {
			        		for(int j = 0; j <= 4; j++) {
				        		int state = tab1_squats.get(j).getState();
				        		
				        		switch(state) {
				        		case 0: // do stuff if unchecked
				        			tab1_squats.get(j).setBackgroundResource(R.drawable.unchecked);
				        			break;
				        		case 1: // do stuff if checked
				        			tab1_squats.get(j).setBackgroundResource(R.drawable.checked);

				        			break;
				        		case 2: // do stuff if crossed
				        			tab1_squats.get(j).setBackgroundResource(R.drawable.crossed);

				        			break;
				        			default: break;
				        		}
			        		}

			        	}
			        });
					
				}

			} else {
				
				tab1_tv_squats.setText(String.valueOf(currentSession.get(0).getCurrentWeight()) + weightUnit);
				tab1_tv_deadlift.setText(String.valueOf(currentSession.get(5).getCurrentWeight()) + weightUnit);
				tab1_tv_OHP.setText(String.valueOf(currentSession.get(4).getCurrentWeight()) + weightUnit);	
				
				tab1_ll_benchpress.setVisibility(View.GONE);
				tab1_tv_benchPressTitle.setVisibility(View.GONE);
				tab1_ll_rowing.setVisibility(View.GONE);
				tab1_tv_rowingTitle.setVisibility(View.GONE);
				
				p.addRule(RelativeLayout.BELOW, R.id.log_linearFive);
				
				tab1_b_log.setLayoutParams(p);
				}

		}
		
		//Initializes tab 3
		public void initTab3(){
//			layout 						= (LinearLayout) getActivity().findViewById(R.id.stats_graphViewLayout); 
			tab3_tv_squats 				= (TextView) getActivity().findViewById(R.id.stats_squatsDetailed);
			tab3_tv_squats_deloads 		= (TextView) getActivity().findViewById(R.id.stats_squatsDetailed2);
			tab3_tv_squats_fails 		= (TextView) getActivity().findViewById(R.id.stats_squatsDetailed3);
			tab3_tv_benchPress 			= (TextView) getActivity().findViewById(R.id.stats_benchPressDetailed);
			tab3_tv_benchPress_deloads 	= (TextView) getActivity().findViewById(R.id.stats_benchPressDetailed2);
			tab3_tv_benchPress_fails 	= (TextView) getActivity().findViewById(R.id.stats_benchPressDetailed3);
			tab3_tv_rowing 				= (TextView) getActivity().findViewById(R.id.stats_rowingDetailed);
			tab3_tv_rowing_deloads 		= (TextView) getActivity().findViewById(R.id.stats_rowingDetailed2);
			tab3_tv_rowing_fails 		= (TextView) getActivity().findViewById(R.id.stats_rowingDetailed3);
			tab3_tv_deadlift 			= (TextView) getActivity().findViewById(R.id.stats_deadliftDetailed);
			tab3_tv_deadlift_deloads 	= (TextView) getActivity().findViewById(R.id.stats_deadliftDetailed2);
			tab3_tv_deadlift_fails 		= (TextView) getActivity().findViewById(R.id.stats_deadliftDetailed3);
			tab3_tv_OHP 				= (TextView) getActivity().findViewById(R.id.stats_ohpDetailed);
			tab3_tv_OHP_deloads 		= (TextView) getActivity().findViewById(R.id.stats_ohpDetailed2);
			tab3_tv_OHP_fails 			= (TextView) getActivity().findViewById(R.id.stats_ohpDetailed3);
//			graphView					= new LineGraphView(getActivity().getApplicationContext(), "Squats graph");
//			layout.addView(graphView);
		}
		
		public void refreshTab3(){
			List<Exercise> 	exercises;
			exercises = SLCalc.getBothSessions();
			
			setWeightString();
			
			// Number in list -> exercise:
			// 0 - Squats
			// 1 - Benchpress
			// 2 - Rowing
			// 3 - OHP
			// 4 - Deadlift

			tab3_tv_squats.setText(String.valueOf(exercises.get(0).getCurrentWeight()) + weightUnit);
			tab3_tv_squats_deloads.setText("Deloads: " + String.valueOf(exercises.get(0).getNumberOfDeloads()));
			tab3_tv_squats_fails.setText("Fails: " + String.valueOf(exercises.get(0).getNumberOfFails()));
			tab3_tv_benchPress.setText(String.valueOf(exercises.get(1).getCurrentWeight()) + weightUnit);
			tab3_tv_benchPress_deloads.setText("Deloads: " + String.valueOf(exercises.get(1).getNumberOfDeloads()));
			tab3_tv_benchPress_fails.setText("Fails: " + String.valueOf(exercises.get(1).getNumberOfFails()));
			tab3_tv_rowing.setText(String.valueOf(exercises.get(2).getCurrentWeight()) + weightUnit);
			tab3_tv_rowing_deloads.setText("Deloads: " + String.valueOf(exercises.get(2).getNumberOfDeloads()));
			tab3_tv_rowing_fails.setText("Fails: " + String.valueOf(exercises.get(2).getNumberOfFails()));
			tab3_tv_deadlift.setText(String.valueOf(exercises.get(4).getCurrentWeight()) + weightUnit);
			tab3_tv_deadlift_deloads.setText("Deloads: " + String.valueOf(exercises.get(4).getNumberOfDeloads()));
			tab3_tv_deadlift_fails.setText("Fails: " + String.valueOf(exercises.get(4).getNumberOfFails()));
			tab3_tv_OHP.setText(String.valueOf(exercises.get(3).getCurrentWeight()) + weightUnit);
			tab3_tv_OHP_deloads.setText("Deloads: " + String.valueOf(exercises.get(3).getNumberOfDeloads()));
			tab3_tv_OHP_fails.setText("Fails: " + String.valueOf(exercises.get(3).getNumberOfFails()));
			
//			populateGraph();
		}
		
//		@SuppressWarnings("deprecation")
//		public void populateGraph(){
//			List<Double> weightData;
//			weightData = SLCalc.getBothSessions().get(0).getProgressList(); 
//			TextView tv_noData = (TextView) getActivity().findViewById(R.id.stats_tvNoData);
//			GraphViewData[] graphViewData;
//			
//			// Only populates the graph if the progresslist has data in it
//			if (!weightData.isEmpty()) {
//				
//				tv_noData.setVisibility(View.GONE);
//				graphView.setVisibility(View.VISIBLE);
//
////				graphViewData = new GraphViewData[weightData.size()];
//				
//				GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {  
//					      new GraphViewData(1, 2.0d)  
//					      , new GraphViewData(2, 1.5d)  
//					      , new GraphViewData(3, 2.5d)  
//					      , new GraphViewData(4, 1.0d)  
//					});  
//				
////				for (int i = 0; i < weightData.size(); i++) {
////					graphViewData[i] = new GraphViewData(i, (double)weightData.get(i));
////					System.out.println(i);
////				}
//				
//				// Inits and resets the weightDataSeries
////				weightDataSeries = new GraphViewSeries(graphViewData);
//				
//				graphView.addSeries(exampleSeries);
//			} else {
//				tv_noData.setVisibility(View.VISIBLE);
//				graphView.setVisibility(View.GONE);
//				tv_noData.setText("No data to display");
//			    tv_noData.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
//			}
//		}
//		
		//Initializes tab 4
		public void initTab4(){
			//get the large image view
      		picView = (ImageView) getActivity().findViewById(R.id.tab4_picture);
      		//get the gallery view
      		picGallery = (Gallery) getActivity().findViewById(R.id.tab4_gallery);
      		//set the imgadapter for picgallery
            picGallery.setAdapter(imgAdapt);
            //redraw the gallery thumbnails to reflect the new addition
			picGallery.setAdapter(imgAdapt);
			//display the newly selected image at larger size
			Matrix matrix = new Matrix();
			matrix.setRotate(90);
			Bitmap pic = imgAdapt.getPic(currentPic);
			picView.setImageBitmap(Bitmap.createBitmap(pic, 0, 0, pic.getWidth(), pic.getHeight(), matrix, false));
			picView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		}
		
		public void setWeightString(){
			if (SLCalc.getWeightUnitKilograms())
				weightUnit  = " KG";
			else
				weightUnit = " Lbs";
		}
    }
    
    /**
	 * Base Adapter subclass creates Gallery view
	 * - provides method for adding new images from user selection
	 * - provides method to return bitmaps from array
	 *
	 */
	public class PicAdapter extends BaseAdapter {

		//use the default gallery background image
		int defaultItemBackground;
		//gallery context
		private Context galleryContext;

		//array to store bitmaps to display
		private Bitmap[] imageBitmaps;
		//placeholder bitmap for empty spaces in gallery
		Bitmap placeholder;

		//constructor
		public PicAdapter(Context c) {
			//instantiate context
			galleryContext = getApplicationContext();
				
			//create bitmap array
			imageBitmaps = new Bitmap[10];
			//decode the placeholder image
			placeholder = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
			Matrix matrix = new Matrix();
			matrix.setRotate(-90);
			placeholder = Bitmap.createBitmap(placeholder, 0, 0, placeholder.getWidth(), placeholder.getHeight(), matrix, false);

			//set placeholder as all thumbnail images in the gallery initially
			for(int i=0; i<imageBitmaps.length; i++)
				imageBitmaps[i]=placeholder;

			//get the styling attributes - use default Andorid system resources
			TypedArray styleAttrs = galleryContext.obtainStyledAttributes(R.styleable.PicGallery);
			//get the background resource
			defaultItemBackground = styleAttrs.getResourceId(R.styleable.PicGallery_android_galleryItemBackground, 0);
			//defaultItemBackground = R.drawable.splash;
			//recycle attributes
			styleAttrs.recycle();
		}

		//BaseAdapter methods

		//return number of data items i.e. bitmap images
		public int getCount() {
			return imageBitmaps.length;
		}

		//return item at specified position
		public Object getItem(int position) {
			return position;
		}

		//return item ID at specified position
		public long getItemId(int position) {
			return position;
		}

		//get view specifies layout and display options for each thumbnail in the gallery
		public View getView(int position, View convertView, ViewGroup parent) {

			//create the view
			ImageView imageView = new ImageView(galleryContext);
			//specify the bitmap at this position in the array
			Matrix matrix = new Matrix();
			matrix.setRotate(90);
			imageView.setImageBitmap(Bitmap.createBitmap(imageBitmaps[position], 0, 0, imageBitmaps[position].getWidth(), imageBitmaps[position].getHeight(), matrix, false));
			//set layout options
			imageView.setLayoutParams(new Gallery.LayoutParams(230, 300));
			//scale type within view area
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			//set default gallery item background
			imageView.setBackgroundResource(defaultItemBackground);
			//return the view
			return imageView;
		}

		//custom methods for this app

		//helper method to add a bitmap to the gallery when the user chooses one
		public void addPic(Bitmap newPic)
		{
			//set at currently selected index
			imageBitmaps[currentPic] = newPic;
		}

		//helper method to add a bitmap to the gallery programmatically
		public void addPic (Bitmap newPic, int i)
		{
			//set at requested index
			imageBitmaps[i] = newPic;
		}
		
		public void addNewPic (Bitmap newPic)
		{
			//set at requested index
			//Bitmap[] tempBitmaps = imageBitmaps;
			for (int i = imageBitmaps.length - 1; i > 1; i--){
				imageBitmaps[i] = imageBitmaps[i-1];
			}
			imageBitmaps[0] = newPic;
		}

		//return bitmap at specified position for larger display
		public Bitmap getPic(int pos)
		{
			//return bitmap at pos index
			return imageBitmaps[pos];
		}
		
		public void resetBitmapArray(){
			//recycle all thumbnail images in the gallery
			for(int i=0; i<imageBitmaps.length; i++)
				imageBitmaps[i].recycle();
		}
	}
	
	protected void updateGallery(){
		File dir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
						"StrongLifts"); // set destination folder
		
		if (dir.exists()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < 10; i++){
				//the returned picture URI
				Uri imgUri = Uri.fromFile(files[files.length - i - 1]);
				if(imgUri!=null) {
					String imgPath = imgUri.getPath();
				    class MyThread implements Runnable {
						String imgPath;
						int pos;
						public MyThread (String s, int pos) {
							this.imgPath = s;
							this.pos = pos;
						}
						public void run (){
							Bitmap bitmap = decodeSampledBitmapFromPath(imgPath);
							imgAdapt.addPic(bitmap, pos);
							invalidator();
						}
					}
					Runnable r = new MyThread(imgPath, i);
					new Thread(r).start();
				}
			}
		}
	}
	
	/**
	 * Handle returning from gallery or file manager image selection
	 * - import the image bitmap
	 */
	@SuppressWarnings("deprecation")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (resultCode == RESULT_OK) {
			//the returned picture URI
			Uri imgUri = data.getData();
			
			//declare the path string
			String imgPath = "";

			//retrieve the string using media data
			String[] medData = { MediaStore.Images.Media.DATA };
			//query the data
			Cursor picCursor = managedQuery(imgUri, medData, null, null, null);
			if(picCursor!=null)
			{
				//get the path string
				int index = picCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				picCursor.moveToFirst();
				imgPath = picCursor.getString(index);
			}
			else
				imgPath = imgUri.getPath();
			
			View vg = (View)findViewById (R.id.tab4);
			class MyThread implements Runnable {
				String imgPath;
				public MyThread (String imgPath) {
					this.imgPath = imgPath;
				}
				public void run (){
					Bitmap bitmap = decodeSampledBitmapFromPath(imgPath);
					imgAdapt.addPic(bitmap, currentPic);
					invalidator();
				}
			}
			Runnable r = new MyThread(imgPath);
			new Thread(r).start();
			vg.invalidate();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void invalidator(){
//		picView = (ImageView) findViewById(R.id.tab4_picture);
//  		picGallery = (Gallery) findViewById(R.id.tab4_gallery);
//  		picView.invalidate();
//  		picGallery.invalidate();
//		ViewGroup vg = (ViewGroup) findViewById(R.id.tab4);
//		vg.invalidate();
		System.out.println("invalidate");
		//invalid = true;
	}
	
	public static Bitmap decodeSampledBitmapFromPath(String imgPath) {
		//set the width and height we want to use as maximum display
		int targetWidth = 600;
		int targetHeight = 400;

		//sample the incoming image to save on memory resources

		//create bitmap options to calculate and use sample size
		BitmapFactory.Options bmpOptions = new BitmapFactory.Options();

		//first decode image dimensions only - not the image bitmap itself
		bmpOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imgPath, bmpOptions);

		//work out what the sample size should be

		//image width and height before sampling
		int currHeight = bmpOptions.outHeight;
		int currWidth = bmpOptions.outWidth;

		//variable to store new sample size
		int sampleSize = 1;

		//calculate the sample size if the existing size is larger than target size
		if (currHeight>targetHeight || currWidth>targetWidth) 
		{
			//use either width or height
			if (currWidth>currHeight)
				sampleSize = Math.round((float)currHeight/(float)targetHeight);
			else 
				sampleSize = Math.round((float)currWidth/(float)targetWidth);
		}
		//use the new sample size
		bmpOptions.inSampleSize = sampleSize;

		//now decode the bitmap using sample options
		bmpOptions.inJustDecodeBounds = false;
		
		//get the file as a bitmap
		return BitmapFactory.decodeFile(imgPath, bmpOptions);
	}

	public static boolean isResetPressed() {
		return resetPressed;
	}

	public static void setResetPressed(boolean resetPressed) {
		MainActivity.resetPressed = resetPressed;
	}
}
