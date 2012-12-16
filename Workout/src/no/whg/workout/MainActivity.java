package no.whg.workout;

import java.io.File;
import java.util.List;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

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
        
    public void videoCapture(int i){	//Commented out until it gets put in use
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
        
        // STATS RELATED TEXTVIEWS
        public TextView tab3_tv_squats;
        public TextView tab3_tv_benchPress;
        public TextView tab3_tv_rowing;
        public TextView tab3_tv_deadlift;
        public TextView tab3_tv_OHP;
        
        public TextView tab1_tv_squats;
        public TextView tab1_tv_benchPress;
        public TextView tab1_tv_rowing;
        public TextView tab1_tv_deadlift;
        public TextView tab1_tv_OHP;
        
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
          				//we will handle the returned data in onActivityResult
          				startActivityForResult(Intent.createChooser(pickIntent, "Select Picture"), PICKER);
          				return true;
          			}
          		});

          		//set the click listener for each item in the thumbnail gallery
          		picGallery.setOnItemClickListener(new OnItemClickListener() {
          			//handle clicks
          			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
          				//set the larger image view to display the chosen bitmap calling method of adapter class
          				picView.setImageBitmap(imgAdapt.getPic(position));
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
			tab1_tv_squats 		= (TextView) getActivity().findViewById(R.id.log_squatsDetailed);
			tab1_tv_benchPress 	= (TextView) getActivity().findViewById(R.id.log_benchPressDetailed);
			tab1_tv_rowing 		= (TextView) getActivity().findViewById(R.id.log_rowingDetailed);
			tab1_tv_deadlift 	= (TextView) getActivity().findViewById(R.id.log_deadliftDetailed);
			tab1_tv_OHP 		= (TextView) getActivity().findViewById(R.id.log_ohpDetailed);
		}
		
		public void refreshTab1() {
			List<Exercise> 	exercises;
			exercises = SLCalc.getBothSessions();
			
			// Number in list -> exercise:
			// 0 - Squats
			// 1 - Benchpress
			// 2 - Rowing
			// 3 - Squats (not used)
			// 4 - OHP
			// 5 - Deadlift
			
			tab1_tv_squats.setText(String.valueOf(exercises.get(0).getCurrentWeight()) + " KG");
			tab1_tv_benchPress.setText(String.valueOf(exercises.get(1).getCurrentWeight()) + " KG");
			tab1_tv_rowing.setText(String.valueOf(exercises.get(2).getCurrentWeight()) + " KG");
			tab1_tv_deadlift.setText(String.valueOf(exercises.get(5).getCurrentWeight()) + " KG");
			tab1_tv_OHP.setText(String.valueOf(exercises.get(4).getCurrentWeight()) + " KG");
			
		}
		
		//Initializes tab 3
		public void initTab3(){
			tab3_tv_squats 		= (TextView) getActivity().findViewById(R.id.stats_squatsDetailed);
			tab3_tv_benchPress 	= (TextView) getActivity().findViewById(R.id.stats_benchPressDetailed);
			tab3_tv_rowing 		= (TextView) getActivity().findViewById(R.id.stats_rowingDetailed);
			tab3_tv_deadlift 	= (TextView) getActivity().findViewById(R.id.stats_deadliftDetailed);
			tab3_tv_OHP 		= (TextView) getActivity().findViewById(R.id.stats_ohpDetailed);
		}
		
		public void refreshTab3(){
			List<Exercise> 	exercises;
			exercises = SLCalc.getBothSessions();
			
			// Number in list -> exercise:
			// 0 - Squats
			// 1 - Benchpress
			// 2 - Rowing
			// 3 - Squats (not used)
			// 4 - OHP
			// 5 - Deadlift
			
			tab3_tv_squats.setText(String.valueOf(exercises.get(0).getCurrentWeight()) + " KG");
			tab3_tv_benchPress.setText(String.valueOf(exercises.get(1).getCurrentWeight()) + " KG");
			tab3_tv_rowing.setText(String.valueOf(exercises.get(2).getCurrentWeight()) + " KG");
			tab3_tv_deadlift.setText(String.valueOf(exercises.get(5).getCurrentWeight()) + " KG");
			tab3_tv_OHP.setText(String.valueOf(exercises.get(4).getCurrentWeight()) + " KG");
		}
		
		//Initializes tab 4
		public void initTab4(){
			//get the large image view
      		picView = (ImageView) getActivity().findViewById(R.id.tab4_picture);
      		//get the gallery view
      		picGallery = (Gallery) getActivity().findViewById(R.id.tab4_gallery);
      		//set the imgadapter for picgallery
            picGallery.setAdapter(imgAdapt);
            //initialize the gallery
	        initGallery();
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
		boolean running = false;
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
			imageBitmaps  = new Bitmap[10];
			//decode the placeholder image
			placeholder = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

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
			imageView.setImageBitmap(imageBitmaps[position]);
			//set layout options
			imageView.setLayoutParams(new Gallery.LayoutParams(300, 200));
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
		
		public boolean getRunning(){
			return running;
		}
		
		public void setRunning(){
			running = true;
		}
	}
	
	/**
	 * Load up images at startup
	 * - import the image bitmap
	 */
	protected static void initGallery(){
		if (!imgAdapt.getRunning()){
			imgAdapt.setRunning();
			File dir = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
							"StrongLifts"); // set destination folder
			
			//reset stored array in imgAdapt to prevent rampant growth of heap
			//imgAdapt.resetBitmapArray();
			
			if (dir.exists()) {
	
				//declare the bitmap
				Bitmap pic = null;
				//declare the path string
				String imgPath = "";
				//File[] files = dir.listFiles();
				int counter = 0;
				for (File file : dir.listFiles()){
					//the returned picture URI
					Uri pickedUri = Uri.fromFile(file);
					
					imgPath = pickedUri.getPath();
					
					if(pickedUri!=null) {
	
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
						pic = BitmapFactory.decodeFile(imgPath, bmpOptions);
	
						//pass bitmap to ImageAdapter to add to array
						imgAdapt.addPic(pic, counter);
					}
					counter++;
					if (counter >= 10)
							break;
				}
	
				
			} else {
				// error message
			}
			
		}
		//redraw the gallery thumbnails to reflect the new addition
		picGallery.setAdapter(imgAdapt);
		//display the newly selected image at larger size
		picView.setImageBitmap(imgAdapt.getPic(currentPic));
		picView.setScaleType(ImageView.ScaleType.FIT_CENTER);
	}

	/**
	 * Handle returning from gallery or file manager image selection
	 * - import the image bitmap
	 */
	@SuppressWarnings("deprecation")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (resultCode == RESULT_OK) {
			
			//check if we are returning from picture selection
			//if (requestCode == PICKER) {
				//the returned picture URI
				Uri pickedUri = data.getData();

				//declare the bitmap
				Bitmap pic = null;
				//declare the path string
				String imgPath = "";

				//retrieve the string using media data
				String[] medData = { MediaStore.Images.Media.DATA };
				//query the data
				Cursor picCursor = managedQuery(pickedUri, medData, null, null, null);
				if(picCursor!=null)
				{
					//get the path string
					int index = picCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					picCursor.moveToFirst();
					imgPath = picCursor.getString(index);
				}
				else
					imgPath = pickedUri.getPath();

				//if and else handle both choosing from gallery and from file manager

				//if we have a new URI attempt to decode the image bitmap
				if(pickedUri!=null) {

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
					pic = BitmapFactory.decodeFile(imgPath, bmpOptions);

					//pass bitmap to ImageAdapter to add to array
					imgAdapt.addPic(pic);
					//redraw the gallery thumbnails to reflect the new addition
					picGallery.setAdapter(imgAdapt);

					//display the newly selected image at larger size
					picView.setImageBitmap(pic);
					//scale options
					picView.setScaleType(ImageView.ScaleType.FIT_CENTER);
				}
			//}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
