package mobile.teamwave.pm_activity;

import teamwave.android.mobile.teamwave.R;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FileImageViewer extends ActionBarActivity{
	android.support.v7.app.ActionBar actionBar;
	DisplayImageOptions options;
	ImageLoader imageLoader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_image_viewer);
		
		ImageView imgFile=(ImageView)findViewById(R.id.imgFile);
		
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).resetViewBeforeLoading(true)
				.showImageForEmptyUri(R.drawable.noimage)
				.showImageOnFail(R.drawable.noimage)
				.showImageOnLoading(R.drawable.noimage).build();
		
		actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#F9F9FA")));
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
		
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String imgUrl = (String) bundle.get("imgUrl");
		actionBar.setTitle(Html
				.fromHtml("<font color='#000000'>"+(String) bundle.get("imgTitle")+"</font>"));
		
		imageLoader.displayImage(imgUrl, imgFile, options);
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			onBackPressed();
			overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_out_right);
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
