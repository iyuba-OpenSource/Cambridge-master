package com.iyuba.camstory.utils;
import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
public class Mychart extends AbstractDemoChart {

	public double[] rightArr;
	public double[] totalArr;
	public List<String> time;

	public Mychart(double[] right, double[] total, List<String> times) {
		super();
		rightArr = right;
		totalArr = total;
		if (times != null && times.size() > 0) {
			time = times;
		} else {
			time = new ArrayList<String>();
		}
	}

	@Override
	public String getName() {
		return "题目信息";
	}

	@Override
	public String getDesc() {
		return "题目";
	}

	@Override
	public View execute(Context context) {
		String[] titles = new String[] { "答题题数", "正确题数" };
		List<double[]> values = new ArrayList<double[]>();
		values.add(totalArr);
		values.add(rightArr);
		int bigPosi = 0;
		for (int i = 0; i < totalArr.length; i++) {
			if (totalArr[i] > totalArr[bigPosi]) {
				bigPosi = i;
			}
		}
		int[] colors = new int[] { 0xFF2981B3, 0XFFE9D477 };// 两个bar的颜色
		XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
		renderer.setOrientation(Orientation.HORIZONTAL);
		setChartSettings(renderer, "题目详情", "", "", 0, 4, 0,
				(int) (totalArr[bigPosi] * 1.2) + 2, Color.BLACK, Color.BLACK);
		renderer.setXLabels(0);// 不显示默认数字label
		// renderer.setYLabels(10);
		for (int i = 0; i < time.size(); i++) {
			String ss = time.get(i);
			renderer.addXTextLabel(i + 1, ss);
		}
		renderer.setMarginsColor(0xFFE3E4E6);// 设置图表周边颜色
		renderer.setZoomButtonsVisible(true);// 是否显示缩放按钮
		renderer.setXLabelsColor(Color.BLACK);// x标签颜色
		renderer.setYLabelsColor(0, Color.BLACK);// y标签颜色
		renderer.setYLabelsAlign(Paint.Align.RIGHT);// 标签字偏向左、右、中
		renderer.setBarWidth(40);// 设置柱的宽度
		renderer.setPanLimits(new double[] { 0, 8, 0, 0 });// 设置可以拖放的位置，分别为x的区间，y的区间
		int length = renderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			SimpleSeriesRenderer seriesRenderer = renderer.getSeriesRendererAt(i);
			seriesRenderer.setDisplayChartValues(true);
		}
		return ChartFactory.getBarChartView(context,
				buildBarDataset(titles, values), renderer, Type.DEFAULT);
	}

}

