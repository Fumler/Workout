package no.whg.workout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.lang.Runnable;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;

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
import android.view.ViewGroup.LayoutParams;
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
        public TextView tab3_tv_benchPress;
        public TextView tab3_tv_rowing;
        public TextView tab3_tv_deadlift;
        public TextView tab3_tv_OHP;
        public GraphView graphView;
        public GraphViewSeries weightDataSeries;
        
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
			tab3_tv_squats 		= (TextView) getActivity().findViewById(R.id.stats_squatsDetailed);
			tab3_tv_benchPress 	= (TextView) getActivity().findViewById(R.id.stats_benchPressDetailed);
			tab3_tv_rowing 		= (TextView) getActivity().findViewById(R.id.stats_rowingDetailed);
			tab3_tv_deadlift 	= (TextView) getActivity().findViewById(R.id.stats_deadliftDetailed);
			tab3_tv_OHP 		= (TextView) getActivity().findViewById(R.id.stats_ohpDetailed);
			graphView			= new BarGraphView(getActivity().getApplicationContext(), "Squats graph");
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
			tab3_tv_benchPress.setText(String.valueOf(exercises.get(1).getCurrentWeight()) + weightUnit);
			tab3_tv_rowing.setText(String.valueOf(exercises.get(2).getCurrentWeight()) + weightUnit);
			tab3_tv_deadlift.setText(String.valueOf(exercises.get(4).getCurrentWeight()) + weightUnit);
			tab3_tv_OHP.setText(String.valueOf(exercises.get(3).getCurrentWeight()) + weightUnit);
			
			populateGraph();
		}
		
		@SuppressWarnings("deprecation")
		public void populateGraph(){
			List weightData;
			weightData = SLCalc.getBothSessions().get(0).getProgressList();
			LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.stats_graphView);  
			TextView tv_noData = (TextView) getActivity().findViewById(R.id.stats_tvNoData);
			
			// Inits and resets the weightDataSeries
			weightDataSeries = new GraphViewSeries(null);
			
			// Only populates the graph if the progresslist has data in it
			if (!weightData.isEmpty()) {
				tv_noData.setVisibility(View.GONE);
				for (int i = 0; i <= weightData.size(); i++) {
					weightDataSeries.appendData(new GraphViewData(i+1, (Double) weightData.get(0)), false);
				}
				
				graphView.addSeries(weightDataSeries);
				layout.addView(graphView);
				
				System.out.println("yes");
			}
			else {
				tv_noData.setVisibility(View.VISIBLE);
				tv_noData.setText("No data to display");
			    tv_noData.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
			    System.out.println("no");
			}
		}
		
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
