package com.iyuba.camstory.utils;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;

import com.iyuba.camstory.sqlite.mode.Voa;
import com.iyuba.camstory.sqlite.op.VoaOp;
import com.iyuba.voa.frame.components.ConfigManagerVOA;

/**
 * 下一曲
 * 
 * @author chentong
 * 
 */
public class NextVideo {
	private ArrayList<Integer> AEID = new ArrayList<Integer>();
	private ArrayList<Integer> LOCALID = new ArrayList<Integer>();
	private int position;
	private int size;
	private final static int local = 100;
	private final static int all = 0;
	private ArrayList<Voa> voaList = new ArrayList<Voa>();
	private boolean justlocal = false;

	public NextVideo(int voaid, int mode, Context mContext) {
		VoaOp vo = new VoaOp();
		justlocal = ConfigManagerVOA.Instance(mContext).loadBoolean("play_local_only");
		if (mode == all) {
			voaList = (ArrayList<Voa>) vo.findDataByAll();
			size = voaList.size();
			for (int i = 0; i < size; i++) {
				AEID.add(voaList.get(i).voaid);
			}
		} else if (mode == local) {
			voaList = (ArrayList<Voa>) vo.findDataByAll();
			size = voaList.size();
			for (int i = 0; i < size; i++) {
				AEID.add(voaList.get(i).voaid);
			}
		} else {
			if (mode > 0 && mode < 11) {
				voaList = (ArrayList<Voa>) vo.findDataByCategory(String.valueOf(mode));
				size = voaList.size();
				for (int i = size - 1; i >= 0; i--) {
					AEID.add(voaList.get(i).voaid);
				}
			} else {
				AEID.add(voaid);
			}
		}
		voaList = (ArrayList<Voa>) vo.findDataByDownload();

		if (voaList != null) {
			for (int i = 0; i < voaList.size(); i++) {
				LOCALID.add(voaList.get(i).voaid);
			}
		}

		if (justlocal && LOCALID != null) {
			position = LOCALID.indexOf(voaid);
		} else {
			position = AEID.indexOf(voaid);
		}

	}

	public int following() {
		if (justlocal) {
			if (position + 1 < voaList.size()) {
				return LOCALID.get(position + 1);
			} else {
				return LOCALID.get(0);
			}
		} else {
			if (position + 1 < size) {
				return AEID.get(position + 1);
			} else {
				return AEID.get(0);
			}
		}

	}

	public int nextVideo() {
		if (justlocal) {
			Random rnd = new Random();
			int nextAEID = rnd.nextInt(LOCALID.size() * 10) / 10;
			while (nextAEID == position && LOCALID.size() != 1) {
				nextAEID = rnd.nextInt(LOCALID.size() * 10) / 10;
			}
			return LOCALID.get(nextAEID);
		} else {
			Random rnd = new Random();
			int nextAEID = rnd.nextInt(AEID.size() * 10) / 10;
			while (nextAEID == position && AEID.size() != 1) {
				nextAEID = rnd.nextInt(AEID.size() * 10) / 10;
			}
			return AEID.get(nextAEID);
		}

	}
}
