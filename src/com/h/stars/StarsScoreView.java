package com.h.stars;

import com.cs.stars.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class StarsScoreView extends View {
	// 五角星半径
	private float mRadiusOut = 20;
	// 内部五边形半径
	private float mRadiusIn;
	// 每个五角星之间的间距
	private float mSpace;
	// 一个空的五角星的轮廓边距
	private float mStroke = 2;

	private Paint mPaintFull;
	private Paint mPaintStroke;

	private int mMaxStar = 1; // 星星数量
	private int maxProgress; // 最大进度
	private float mProgress; // 当前进度

	private int color = 0xFF0000;
	private final static float DEGREE = 36; // 五角星角度
	// 36度的弧度值
	private float radian;

	public StarsScoreView(Context context) {
		super(context);
	}

	public StarsScoreView(Context context, AttributeSet attrs) {
		super(context, attrs);
		try {
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.StarsScoreView);
			this.color = a.getColor(R.styleable.StarsScoreView_color, color);
			this.mRadiusOut = a.getDimensionPixelSize(
					R.styleable.StarsScoreView_radius, (int) mRadiusOut);
			this.mStroke = a.getDimensionPixelSize(
					R.styleable.StarsScoreView_stroke, (int) mStroke);
			this.mSpace = a.getDimensionPixelSize(
					R.styleable.StarsScoreView_space, (int) mSpace);
			this.mMaxStar = a.getInt(R.styleable.StarsScoreView_maxStar,
					mMaxStar);
			this.maxProgress = a.getInt(R.styleable.StarsScoreView_maxProgress,
					maxProgress);
			if (mMaxStar < 1)
				mMaxStar = 1;
			a.recycle();
		} catch (Exception e) {
		}
		init();
	}

	private void init() {
		mPaintFull = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaintFull.setStyle(Style.FILL_AND_STROKE);
		mPaintFull.setColor(color);
		mPaintFull.setStrokeJoin(Join.ROUND);
		mPaintFull.setStrokeCap(Cap.ROUND);

		mPaintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaintStroke.setStyle(Style.STROKE);
		mPaintStroke.setStrokeWidth(mStroke);
		mPaintStroke.setColor(color);
		mPaintStroke.setStrokeJoin(Join.ROUND);
		mPaintStroke.setStrokeCap(Cap.ROUND);
		radian = degree2Radian(DEGREE);
		mRadiusIn = (float) (mRadiusOut * Math.sin(radian / 2) / Math
				.cos(radian)); // 中间五边形的半径
	}

	private float degree2Radian(float degree) {
		return (float) (Math.PI * degree / 180);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int width;
		int height;
		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else {
			width = (int) (getPaddingLeft() + mRadiusOut * Math.cos(radian / 2)
					* 2 * mMaxStar + (mMaxStar - 1) * mSpace + getPaddingRight());
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
			height = (int) (getPaddingTop() + mRadiusOut + mRadiusOut
					* Math.cos(radian) + getPaddingBottom());
		}
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (maxProgress == 0)
			return;

		float itemProgress = maxProgress / mMaxStar; // 每个星星多少分
		int num = (int) (mProgress / itemProgress); // 需要完整星星多少个
		// 需要多少个完整的星星
		for (int i = 0; i < num; i++) {
			canvas.save();
			canvas.translate(mSpace * i, 0);
			canvas.translate(
					(float) (i * mRadiusOut * Math.cos(radian / 2) * 2), 0);
			drawFullStar(canvas);
			canvas.restore();
		}

		// 有内有半个星星的出现
		int halfNum = 0;
		if ((mProgress - ((int) (mProgress / itemProgress) * itemProgress)) >= itemProgress / 2) {
			halfNum = 1;
			canvas.save();
			canvas.translate(
					(float) (num * mRadiusOut * Math.cos(radian / 2) * 2), 0);
			canvas.translate(mSpace * num, 0);
			drawHalfStar(canvas);
			canvas.restore();
		}

		// 画完最后几个空星星
		for (int i = 0; i < mMaxStar - num - halfNum; i++) {
			canvas.save();
			canvas.translate(mSpace * (i + num + halfNum), 0);
			canvas.translate(
					(float) ((i + num + halfNum) * mRadiusOut
							* Math.cos(radian / 2) * 2), 0);
			drawEmptyStar(canvas);
			canvas.restore();
		}

	}

	private void drawFullStar(Canvas canvas) {

		Path path = new Path();
		float x_cos_out = (float) (mRadiusOut * Math.cos(radian / 2));

		path.moveTo(x_cos_out, 0);
		path.lineTo((float) (x_cos_out + mRadiusIn * Math.sin(radian)),
				(float) (mRadiusOut - mRadiusOut * Math.sin(radian / 2)));

		path.lineTo((float) (x_cos_out * 2), (float) (mRadiusOut - mRadiusOut
				* Math.sin(radian / 2)));
		path.lineTo((float) (x_cos_out + mRadiusIn * Math.cos(radian / 2)),
				(float) (mRadiusOut + mRadiusIn * Math.sin(radian / 2)));
		path.lineTo((float) (x_cos_out + mRadiusOut * Math.sin(radian)),
				(float) (mRadiusOut + mRadiusOut * Math.cos(radian)));
		path.lineTo(x_cos_out, (float) (mRadiusOut + mRadiusIn));
		path.lineTo((float) (x_cos_out - mRadiusOut * Math.sin(radian)),
				(float) (mRadiusOut + mRadiusOut * Math.cos(radian)));
		path.lineTo((float) (x_cos_out - mRadiusIn * Math.cos(radian / 2)),
				(float) (mRadiusOut + mRadiusIn * Math.sin(radian / 2)));
		path.lineTo(0, (float) (mRadiusOut - mRadiusOut * Math.sin(radian / 2)));
		path.lineTo((float) (x_cos_out - mRadiusIn * Math.sin(radian)),
				(float) (mRadiusOut - mRadiusOut * Math.sin(radian / 2)));
		path.close();
		canvas.drawPath(path, mPaintFull);

	}

	private void drawHalfStar(Canvas canvas) {
		float x_cos_out = (float) (mRadiusOut * Math.cos(radian / 2));
		Path leftPath = new Path();
		leftPath.moveTo(x_cos_out, 0);
		leftPath.lineTo(x_cos_out, mRadiusOut + mRadiusIn);
		leftPath.lineTo((float) (x_cos_out - mRadiusOut * Math.sin(radian)),
				(float) (mRadiusOut + mRadiusOut * Math.cos(radian)));
		leftPath.lineTo((float) (x_cos_out - mRadiusIn * Math.cos(radian / 2)),
				(float) (mRadiusOut + mRadiusIn * Math.sin(radian / 2)));
		leftPath.lineTo(0,
				(float) (mRadiusOut - mRadiusOut * Math.sin(radian / 2)));
		leftPath.lineTo((float) (x_cos_out - mRadiusIn * Math.sin(radian)),
				(float) (mRadiusOut - mRadiusOut * Math.sin(radian / 2)));
		leftPath.close();
		canvas.drawPath(leftPath, mPaintFull);
		drawEmptyStar(canvas);
	}

	private void drawEmptyStar(Canvas canvas) {
		float x_cos_out = (float) (mRadiusOut * Math.cos(radian / 2));
		Path path = new Path();
		// 1
		path.moveTo(x_cos_out, mStroke);
		// 2
		path.lineTo(
				(float) (x_cos_out + mRadiusIn * Math.sin(radian) - mStroke
						* Math.sin(radian)),
				(float) (mRadiusOut - mRadiusOut * Math.sin(radian / 2) + mStroke
						* Math.cos(radian)));
		// 3
		path.lineTo(
				(float) (x_cos_out * 2 - mStroke * Math.cos(radian / 2)),
				(float) (mRadiusOut - mRadiusOut * Math.sin(radian / 2) + mStroke
						* Math.sin(radian / 2)));
		// 4
		path.lineTo(
				(float) (x_cos_out + (mRadiusIn - mStroke)
						* Math.cos(radian / 2)),
				(float) (mRadiusOut + (mRadiusIn - mStroke)
						* Math.sin(radian / 2)));
		// 5
		path.lineTo(
				(float) (x_cos_out + (mRadiusOut - mStroke) * Math.sin(radian)),
				(float) (mRadiusOut + (mRadiusOut - mStroke) * Math.cos(radian)));
		// 6
		path.lineTo(x_cos_out, (float) (mRadiusOut + mRadiusIn - mStroke));
		// 7
		path.lineTo(
				(float) (x_cos_out - (mRadiusOut - mStroke) * Math.sin(radian)),
				(float) (mRadiusOut + (mRadiusOut - mStroke) * Math.cos(radian)));
		// 8
		path.lineTo(
				(float) (x_cos_out - (mRadiusIn - mStroke)
						* Math.cos(radian / 2)),
				(float) (mRadiusOut + (mRadiusIn - mStroke)
						* Math.sin(radian / 2)));
		// 9
		path.lineTo(
				(float) (mStroke * Math.cos(radian / 2)),
				(float) (mRadiusOut - (mRadiusOut - mStroke)
						* Math.sin(radian / 2)));
		// 10
		path.lineTo(
				(float) (x_cos_out - (mRadiusIn - mStroke) * Math.sin(radian)),
				(float) (mRadiusOut - mRadiusOut * Math.sin(radian / 2) + mStroke
						* Math.cos(radian)));
		path.close();
		canvas.drawPath(path, mPaintStroke);
	}

	public void setProgress(float progress) {
		this.mProgress = progress;
		if (mProgress >= maxProgress) {
			mProgress = maxProgress;
		}

		if (mProgress < 0) {
			mProgress = 0;
		}
		invalidate();
	}
	
	public float getProgress(){
		return mProgress;
	}
}
