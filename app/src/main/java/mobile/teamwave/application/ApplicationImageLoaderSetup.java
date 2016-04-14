package mobile.teamwave.application;

import android.app.Application;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class ApplicationImageLoaderSetup extends Application {
	public static ApplicationImageLoaderSetup instance;
	public void onCreate() {
		super.onCreate();
		instance = this;
		// UNIVERSAL IMAGE LOADER SETUP
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheOnDisc(false).cacheInMemory(false)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.resetViewBeforeLoading()
				.displayer(new FadeInBitmapDisplayer(300)).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				instance).memoryCache(new WeakMemoryCache())
				.threadPriority(1)
				.memoryCacheSize(1048576 * 10)
				.defaultDisplayImageOptions(defaultOptions)
				.diskCacheExtraOptions(480, 320, null).build();

		ImageLoader.getInstance().init(config);
		// END - UNIVERSAL IMAGE LOADER SETUP
	}
}
