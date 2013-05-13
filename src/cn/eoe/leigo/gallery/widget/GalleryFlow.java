package cn.eoe.leigo.gallery.widget;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryFlow extends Gallery {

	// 创建Camera对象
	private Camera mCamera = new Camera();
	// 最大旋转角度
	private int mMaxRotateAngle = 60;
	// 最大缩放值
	private int mMaxZoom = -120;
	private int mCoverflowCenter;

	public GalleryFlow(Context context) {
		super(context);
		setStaticTransformationsEnabled(true);
	}

	public GalleryFlow(Context context, AttributeSet attrs) {
		super(context, attrs);
		setStaticTransformationsEnabled(true);
	}

	public GalleryFlow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setStaticTransformationsEnabled(true);
	}

	// 获得gallery展示图片的中心点
	public int getCenterOfCoverflow() {
		return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2
				+ getPaddingLeft();
	}

	// 获得图片中心点
	public int getCenterOfView(View view) {
		return view.getLeft() + view.getWidth() / 2;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mCoverflowCenter = getCenterOfCoverflow();
		super.onSizeChanged(w, h, oldw, oldh);
	}

	// 控制gallery中每个图片的旋转
	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t) {
		// 得到图片的重心点
		final int childCenter = getCenterOfView(child);
		final int width = child.getLayoutParams().width;
		// 旋转角度
		int rotationAngle = 0;
		//重置转换状态
		t.clear();
		 //设置转换类型
		t.setTransformationType(Transformation.TYPE_MATRIX);
		//如果图片位于中心位置不需要进行旋转
		if (childCenter == mCoverflowCenter) {
			transformImageBitmap((ImageView) child, t, 0);
		} else {
			//根据图片在gallery中的位置来计算图片的旋转角度
			rotationAngle = (int) ((float) (mCoverflowCenter - childCenter)
					/ width * mMaxRotateAngle);
			//如果旋转角度绝对值大于最大旋转角度返回（-mMaxRotationAngle或mMaxRotationAngle;）
			if (Math.abs(rotationAngle) > mMaxRotateAngle) {
				rotationAngle = rotationAngle < 0 ? -mMaxRotateAngle
						: mMaxRotateAngle;
			}
			transformImageBitmap((ImageView) child, t, rotationAngle);

		}
		return true;
	}

	// 图片变形
	private void transformImageBitmap(ImageView child, Transformation t,
			int rotateAngle) {
		// 保存图像变换的效果
		mCamera.save();

		final Matrix matrix = t.getMatrix();
		// 图片高度
		final int imageHeight = child.getLayoutParams().height;
		// 图片宽度
		final int imageWidth = child.getLayoutParams().width;
		// 返回旋转角度的绝对值
		final int rotation = Math.abs(rotateAngle);

		// 在Z轴上正向移动camera的视角，实际效果为放大图片。
		// 如果在Y轴上移动，则图片上下移动；X轴上对应图片左右移动。
		mCamera.translate(0.0f, 0.0f, 100.0f);

		if (rotation < mMaxRotateAngle) {
			float zoom = (float) ((rotation * 1.5) + mMaxZoom);
			mCamera.translate(0.0f, 0.0f, zoom);
			child.setAlpha((int) (255 - rotation * 2.5));
		}

		/// 在Y轴上旋转，对应图片竖向向里翻转。
        // 如果在X轴上旋转，则对应图片横向向里翻转。
		mCamera.rotateY(rotateAngle);
		mCamera.getMatrix(matrix);
		matrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
		matrix.postTranslate((imageWidth / 2), (imageHeight / 2));

		// 还原
		mCamera.restore();

	}

}
