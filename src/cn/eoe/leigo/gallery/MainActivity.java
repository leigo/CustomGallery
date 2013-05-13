package cn.eoe.leigo.gallery;

import android.app.Activity;
import android.os.Bundle;
import cn.eoe.leigo.gallery.adapter.ImageAdapter;
import cn.eoe.leigo.gallery.widget.GalleryFlow;

public class MainActivity extends Activity {
	private GalleryFlow mGalleryFlow;

	private int[] imageIds = new int[] { R.drawable.baiyang, R.drawable.chunv,
			R.drawable.jinniu, R.drawable.juxie, R.drawable.mojie,
			R.drawable.sheshou, R.drawable.shizi, R.drawable.shuangyu,
			R.drawable.shuangzi, R.drawable.shuiping, R.drawable.tianping,
			R.drawable.tianxie };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViews();
		initViews();
	}

	private void findViews() {
		mGalleryFlow = (GalleryFlow) findViewById(R.id.gf);
	}

	private void initViews() {
		ImageAdapter adapter = new ImageAdapter(this, imageIds);
		// 生成带有倒影效果的图片
		adapter.createReflectedImages();
		mGalleryFlow.setAdapter(adapter);
	}

}
