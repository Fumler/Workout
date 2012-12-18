package no.whg.workout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

public class MediaCaptureActivity extends Activity {
	private static final int IMAGE_REQUEST_CODE = 100;
	private static final int VIDEO_REQUEST_CODE = 200;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	public static final int MEDIA_TYPE_PLAY = 3;
	private Uri fileUri;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediacapture);
        
        Intent intent = getIntent();

        if(intent.getStringExtra("method").equals("yes")){
	        int i = intent.getIntExtra("MEDIA_TYPE", 0);
	        // onCreate() runs twice, this prevents it from running the intent's request twice
        	intent.putExtra("method","no");
        	
        	if (i == MEDIA_TYPE_PLAY){
        		System.out.println("i == MEDIA_TYPE_PLAY");
        		String lift = intent.getStringExtra("lift");
        		Uri liftUri = videoExists(lift);
        		if (liftUri != null){
	        		Intent watch = new Intent(Intent.ACTION_VIEW);
	        		//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        		watch.setDataAndType(liftUri, "video/*");
	        		startActivity(watch);
	        		finish();
        		} else {
        			//no such video
        			finish();
        		}
        	} else {
        		captureMedia(intent, i);
        	}
        } else {
        	finish();
        }
	    
	}
	

	/* *
	 * Determines the type of media to capture, and redirects to the correct one. Runs finish() if it fails.
	 * 
	 * @param intent The intent received from the function that created the activity.
	 * @param i	The media type.
	 * @see MainActivity#videoCapture
	 * @see Mainactivity#onOptionsItemSelected
	 */
	private void captureMedia(Intent intent, int i) {
		if (i == MEDIA_TYPE_IMAGE){
			captureImage();
		} else if (i == MEDIA_TYPE_VIDEO) {
			captureVideo(intent);
		} else {
			// invalid intent
			finish();
		}
	}
	

	/* *
	 * Starts Camera for result, sending a request code defining how to handle the result
	 * 
	 */
	private void captureImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = getMediaFileUri(MEDIA_TYPE_IMAGE, "SL_IMG_");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		startActivityForResult(intent, IMAGE_REQUEST_CODE);
	}
	

	/* *
	 * Starts Camera for result, sending a request code defining how to handle the result
	 * 
	 */
	private void captureVideo(Intent i) {
		String lift = i.getStringExtra("lift");
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		
		
		fileUri = getMediaFileUri(MEDIA_TYPE_VIDEO, lift);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		startActivityForResult(intent, VIDEO_REQUEST_CODE);
	}
	

	/* *
	 * This does nothing except call finish().
	 * 
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == IMAGE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// saved
			} else if (resultCode == RESULT_CANCELED) {
				// cancelled
			} else {
				// something went wrong
			}
		} else if (requestCode == VIDEO_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// saved
			} else if (resultCode == RESULT_CANCELED) {
				// cancelled
			} else {
				// something went wrong
			}
		}
		finish();
	}
	

	/* *
	 * Gets an Uri from a string.
	 * 
	 * @param type	The type of media.
	 * @param lift	The name of the media.
	 * @return Uri	The requested Uri.
	 */
	private Uri getMediaFileUri(int type, String lift) {
		return Uri.fromFile(getMediaFile(type, lift));
	}
	

	/* *
	 * Returns a File from a String.
	 * 
	 * @param type	The type of media.
	 * @param lift	The name of the media.
	 * @return File	Returns the requested file. 
	 */
	private File getMediaFile(int type, String lift) {
		File dir;
		if (type == MEDIA_TYPE_IMAGE) {
			dir = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					"StrongLifts"); // set destination folder
		} else {
			dir = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
					"StrongLifts"); // set destination folder
		}
		if (!dir.exists()) { // if dir does not exist
			dir.mkdirs();
		}

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			// mediaFile = new File(dir.getPath() + File.separator + lift +
			// timeStamp + ".jpg");
			mediaFile = new File(dir.getPath() + File.separator + lift
					+ timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			// mediaFile = new File(dir.getPath() + File.separator + lift +
			// timeStamp + ".mp4");
			mediaFile = new File(dir.getPath() + File.separator + lift + ".mp4");
		} else {
			return null; // something went wrong
		}
		return mediaFile;
	}
	

	/* *
	 * Checks whether a video exists, and returns an Uri to its location.
	 * 
	 * @param text	The name of the exercise we are looking for.
	 * @return Uri	The Uri of the discovered video.
	 * @return null	null is returned if no video is found.
	 */
	private Uri videoExists(String text){
		File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "StrongLifts/"); //set destination folder
		File[] vids = dir.listFiles();
		String vid = "SL_VID_" + text + ".mp4";
		for (int i = 0; i < vids.length; i++){
			if (vids[i].getName().equals(vid)){
				return Uri.fromFile(vids[i]);
			}
		}
		return null;
	}
}
