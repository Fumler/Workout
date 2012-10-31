package no.whg.workout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import no.whg.getfit.Exercise;
import no.whg.getfit.Main;
import no.whg.getfit.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.net.Uri;

public class MediaCapture {
	private static final int IMAGE_REQUEST_CODE = 100;
	private static final int VIDEO_REQUEST_CODE = 200;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private Uri fileUri;
	
	public void takePicture() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = getMediaFileUri(MEDIA_TYPE_IMAGE, "SL_IMG_");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		startActivityForResult(intent, IMAGE_REQUEST_CODE);
	}
	
	public void takeVideo(View view) {
		String lift = "SL_VID_";
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		Exercise[] exercises = MainActivity.SLCalc.getABworkouts();

		ImageButton ib = (ImageButton) view;
		if (ib.equals(findViewById(R.id.btn_video0))) {
			lift += exercises[0].getName();
		} else if (ib.equals(findViewById(R.id.btn_video1))) {
			lift += exercises[1].getName();
		} else if (ib.equals(findViewById(R.id.btn_video2))) {
			lift += exercises[2].getName();
		}

		fileUri = getMediaFileUri(MEDIA_TYPE_VIDEO, lift);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		startActivityForResult(intent, IMAGE_REQUEST_CODE);
	}
	
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
	}

	private Uri getMediaFileUri(int type, String lift) {
		return Uri.fromFile(getMediaFile(type, lift));
	}

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
			if (!dir.mkdirs()) { // create dir
				// something went horribly wrong
				return null;
			}
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
}
