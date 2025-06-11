package com.iyuba.camstory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.iyuba.camstory.adpater.SchoolListAdapter;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.listener.AppUpdateCallBack;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.camstory.listener.RequestEditUserInfo;
import com.iyuba.camstory.listener.RequestUserDetailInfo;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.sqlite.mode.EditUserInfo;
import com.iyuba.camstory.sqlite.mode.School;
import com.iyuba.camstory.sqlite.op.SchoolOp;
import com.iyuba.camstory.utils.GitHubImageLoader;
import com.iyuba.camstory.utils.JudgeZodicaAndConstellation;
import com.iyuba.camstory.utils.LogUtils;
import com.iyuba.camstory.widget.CustomDialog;
import com.iyuba.camstory.widget.CustomToast;
import com.iyuba.camstory.widget.WaittingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

public class EditUserInfoActivity extends Activity  implements OnWheelChangedListener {
	private static final String TAG = EditUserInfoActivity.class.getSimpleName();
	private Context mContext;

	private TextView gender, birthday, zodiac, constellation, school, location;
	private LinearLayout changeImageLayout;
	private ImageView userImage;
	private Button back, save;
	private final static int GENDER_DIALOG = 1;// 性别选择
	private final static int DATE_DIALOG = 2;// 日期选择
	private final static int SCHOOL_DIALOG = 3;// 学校选择
	private final static int LOCATION_DIALOG = 4;// 位置选择
	/**
	 * 省的WheelView控件
	 */
	private WheelView mProvince;
	/**
	 * 市的WheelView控件
	 */
	private WheelView mCity;
	/*** 区的WheelView控件*/
	private WheelView mArea;
	private Button btnShowChoose;
	private Calendar calendar = null;
	private EditUserInfo editUserInfo = new EditUserInfo();
	private CustomDialog waitingDialog;
	private String cityName;
	// school
	private View schoolDialog;
	private View locationDialog;
	private EditText searchText;
	private Button sure;
	private View clear;
	private ListView schoolList;
	private ArrayList<School> schools = new ArrayList<School>();
	private SchoolListAdapter schoolListAdapter;
	private StringBuffer tempSchool;
	/**
	 * 所有省
	 */
	private String[] mProvinceDatas;
	/**
	 * key - 省 value - 市s
	 */
	private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/*** key - 市 values - 区s*/
	private Map<String, String[]> mAreaDatasMap = new HashMap<String, String[]>();

	/*** 当前省的名称*/
	private String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	private String mCurrentCityName;
	/*** 当前区的名称*/
	private String mCurrentAreaName = "";



	/**
	 * 把全国的省市区的信息以json的格式保存，解析完成后赋值为null
	 */
	private JSONObject mJsonObj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edituserinfo);
		mContext = this;
		waitingDialog = new WaittingDialog().wettingDialog(mContext);
		//初始化控件
		initWidget();
		//获取intent传递过来的数据
		getIntentData();
		//加载位置信息
		//LoadInfo();
		//设置监听⌚️
		setListener();
	}
	
	public void getIntentData() {
		Intent intent = getIntent();
		editUserInfo.setEdGender(intent.getStringExtra("gender"));
		editUserInfo.setBirthday(intent.getStringExtra("birthday"));
		editUserInfo.setEdZodiac(intent.getStringExtra("zodiac"));
		editUserInfo.setEdConstellation(intent.getStringExtra("constellation"));
		editUserInfo.setEdResideCity(intent.getStringExtra("location"));
		editUserInfo.setUniversity(intent.getStringExtra("school"));
		if(!"".equals(editUserInfo.getBirthday())&&editUserInfo.getBirthday()!=null){
		String []year_splite = editUserInfo.getBirthday().split("-");
		int year = Integer.parseInt(year_splite[0]);
		int month = Integer.parseInt(year_splite[1]);
		int day = Integer.parseInt(year_splite[2]);
		Log.e("year-month-day",year+"---"+month+"---"+day);
		editUserInfo.setEdBirthYear(year);
		editUserInfo.setEdBirthMonth(month);
		editUserInfo.setEdBirthDay(day);
		}
		handler.sendEmptyMessage(5);

	}
	private void initWidget() {
		userImage = findViewById(R.id.iveditPortrait);
		gender = findViewById(R.id.editGender);
		birthday = findViewById(R.id.editBirthday);
		location = findViewById(R.id.editResideLocation);
		zodiac = findViewById(R.id.editZodiac);
		constellation = findViewById(R.id.editConstellation);
		changeImageLayout = findViewById(R.id.editPortrait);
		back = findViewById(R.id.button_back);
		save = findViewById(R.id.editinfo_save_btn);
		school = findViewById(R.id.editSchool);
	}

	private void setText() {
		if (editUserInfo.getEdGender() != null) {
			if (editUserInfo.getEdGender().equals("1")) {
				gender.setText(getResources().getStringArray(R.array.gender)[0]);
			} else if (editUserInfo.getEdGender().equals("2")) {
				gender.setText(getResources().getStringArray(R.array.gender)[1]);
			}
		} else {
			gender.setText(getResources().getStringArray(R.array.gender)[0]);
		}
		birthday.setText(editUserInfo.getBirthday());
		zodiac.setText(editUserInfo.getEdZodiac());
		constellation.setText(editUserInfo.getEdConstellation());
		location.setText(editUserInfo.getEdResideCity());
		school.setText(editUserInfo.getUniversity());
		GitHubImageLoader.Instace(mContext).setCirclePic(
				AccountManager.getInstance().userId + "", userImage);
	}
	
//	public void setGenderWithNoLocate() {
//		if (editUserInfo.getEdGender() != null) {
//			if (editUserInfo.getEdGender().equals("1")) {
//				gender.setText(getResources().getStringArray(R.array.gender)[0]);
//			} else if (editUserInfo.getEdGender().equals("2")) {
//				gender.setText(getResources().getStringArray(R.array.gender)[1]);
//			}
//		} else {
//			gender.setText(getResources().getStringArray(R.array.gender)[0]);
//		}
//		birthday.setText(editUserInfo.getBirthday());
//		zodiac.setText(editUserInfo.getEdZodiac());
//		constellation.setText(editUserInfo.getEdConstellation());
//		school.setText(editUserInfo.getUniversity());
//		GitHubImageLoader.Instance(mContext).setCirclePic(
//				AccountManager.Instance().userId + "", userImage);
//	}
	
	private void LoadInfo() {
		// TODO Auto-generated method stub
		handler.sendEmptyMessage(0);
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				waitingDialog.show();
				GetAddr();
//				handler.sendEmptyMessage(1);
				break;
			case 1:
				RequestUserDetailInfo request = new RequestUserDetailInfo(AccountManager.getInstance().userId
						, new RequestCallBack() {
					@Override
					public void requestResult(Request result) {
						// TODO 自动生成的方法存根
						RequestUserDetailInfo requestUserDetailInfo = (RequestUserDetailInfo) result;
						if (requestUserDetailInfo.result.equals("211")) {
							editUserInfo = requestUserDetailInfo.editUserInfo;
						}
						handler.sendEmptyMessage(2);
					}
				});
				break;
			case 2:
				waitingDialog.dismiss();
				setText();
				break;
			case 3:
				save.setClickable(true);
				CustomToast.showToast(mContext, R.string.person_info_success, 1000);
				finish();
				break;
			case 4:
				save.setClickable(true);
				CustomToast.showToast(mContext, R.string.person_info_fail, 1000);
				break;
			case 5:
				setText();
				break;
			case 6:
				CustomToast.showToast(mContext, "请检查网络", 1000);
				break;
			case 7:
				CustomToast.showToast(mContext, R.string.check_gps, 1000);
				break;
			case 8:
				schools = new SchoolOp().findDataByFuzzy(tempSchool
						.toString());
				Log.e("schools-size",schools.size()+"");
				schoolListAdapter = new SchoolListAdapter(mContext, schools);
				schoolList.setAdapter(schoolListAdapter);
				schoolListAdapter.notifyDataSetChanged();
				break;
			case 9:
				schools = new SchoolOp().findDataByFuzzy(tempSchool
						.toString());
				Log.e("schools",schools.toString()+schools.size());
				schoolListAdapter.setData(schools);
				schoolListAdapter.notifyDataSetChanged();
				break;
			case 10:
				location.setText(cityName);
				handler.sendEmptyMessage(2);
				break;
			default:
				break;
			}
		}
	};

	private void setListener() {
		// TODO Auto-generated method stub
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});

		location.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createDialog(LOCATION_DIALOG);
			}
		});
		changeImageLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(AccountManager.getInstance().islinshi){
					showNormalDialog();
				}
				else {
				Intent intent = new Intent(mContext, UpLoadImageActivity.class);
				startActivity(intent);
				}
			}
		});

		gender.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				createDialog(GENDER_DIALOG);
			}
		});
		birthday.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				createDialog(DATE_DIALOG);
			}
		});
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				save.setClickable(false);
				String city = location.getText().toString();
				city = city.trim();
				String value = "", key = "";
				StringBuffer sb = new StringBuffer("");
				int i;
				if (city.contains(" ")) {
					String[] area = city.split(" ");
					sb.append(editUserInfo.getEdGender()).append(",");
					sb.append(editUserInfo.getEdBirthYear()).append(",");
					sb.append(editUserInfo.getEdBirthMonth()).append(",");
					sb.append(editUserInfo.getEdBirthDay()).append(",");
					sb.append(editUserInfo.getEdConstellation()).append(",");
					sb.append(editUserInfo.getEdZodiac()).append(",");
					sb.append(school.getText()).append(",");
					for (i = 0; i < area.length; i++) {

						sb.append(area[i]).append(",");

					}
					sb.deleteCharAt(sb.length() - 1);
					value = sb.toString();
					if (i == 3) {
						key = "gender,birthyear,birthmonth,birthday,constellation,zodiac,graduateschool,resideprovince,residecity,residedist";
					} else {
						key = "gender,birthyear,birthmonth,birthday,constellation,zodiac,graduateschool,resideprovince,residecity";
					}
				} else {
					sb.append(editUserInfo.getEdGender()).append(",");
					sb.append(editUserInfo.getEdBirthYear()).append(",");
					sb.append(editUserInfo.getEdBirthMonth()).append(",");
					sb.append(editUserInfo.getEdBirthDay()).append(",");
					sb.append(editUserInfo.getEdConstellation()).append(",");
					sb.append(editUserInfo.getEdZodiac()).append(",");
					sb.append(school.getText()).append(",");
					sb.append(city);
					value = sb.toString();
					key = "gender,birthyear,birthmonth,birthday,constellation,zodiac,graduateschool,residecity";
				}
				RequestEditUserInfo request = new RequestEditUserInfo(AccountManager.getInstance().userId
						,key, value, new RequestCallBack() {
					@Override
					public void requestResult(Request result) {
						// TODO 自动生成的方法存根
						RequestEditUserInfo requestEditUserInfo = (RequestEditUserInfo) result;
						if (requestEditUserInfo.result.equals("221")) {
							handler.sendEmptyMessage(3);
						} else {
							handler.sendEmptyMessage(4);
						}
					}
				});
				CrashApplication.getInstance().getQueue().add(request);
			}
		});
		school.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				createDialog(SCHOOL_DIALOG);

			}
		});
	}


	private void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
		final android.app.AlertDialog.Builder normalDialog =
				new android.app.AlertDialog.Builder(mContext);
		normalDialog.setIcon(R.drawable.iyubi_icon);
		normalDialog.setTitle("提示");
		normalDialog.setMessage("临时用户无法修改头像！");
		normalDialog.setPositiveButton("登录",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//...To-do
						Intent intent = new Intent();
						intent.setClass(mContext, LoginActivity.class);
						startActivity(intent);
						EditUserInfoActivity.this.finish();

					}
				});
		normalDialog.setNegativeButton("确定",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//...To-do

					}
				});
		// 显示
		normalDialog.show();
	}
	private void initSchoolDialog(final Dialog dialog) {

		searchText = schoolDialog.findViewById(R.id.search_text);
		sure = schoolDialog.findViewById(R.id.search);
		clear = schoolDialog.findViewById(R.id.clear);
		schoolList = schoolDialog.findViewById(R.id.school_list);
		searchText.requestFocus();
		searchText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				tempSchool = new StringBuffer("");
				int size = arg0.length();
				for (int i = 0; i < size; i++) {
					tempSchool.append(arg0.charAt(i));
					tempSchool.append('%');
				}
				handler.sendEmptyMessage(9);
			}
		});
		clear.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				searchText.setText("");
				schools.clear();
				tempSchool = new StringBuffer("");
				handler.sendEmptyMessage(8);
			}
		});
		sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String university = searchText.getText().toString();
				school.setText(university);
				editUserInfo.setUniversity(university);
				dialog.dismiss();
			}
		});
		schoolList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String university = schools.get(arg2).school_name;
				school.setText(university);
				editUserInfo.setUniversity(university);
				dialog.dismiss();
			}
		});
		tempSchool = new StringBuffer("");
		handler.sendEmptyMessage(8);
	}

	private void GetAddr() {
		//String locationPos[] = GetLocation.getInstance(mContext).getLocation();
		//String latitude = locationPos[0];
		//String longitude = locationPos[1];

		String latitude;
		String longitude;

		//这里取消设置的地理位置数据，在实际使用中都没有请求权限，感觉没必要
//		Log.e("地理位置权限", PermissionUtils.hasSelfPermissions(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) + "");
//		if (PermissionUtils.hasSelfPermissions(mContext, Manifest.permission.ACCESS_FINE_LOCATION)) {
//			Pair<Double, Double> location = GetLocation.getLocation(mContext);
//			latitude = String.valueOf(location.first);
//			longitude = String.valueOf(location.second);
//		} else {
			latitude = "0.0";
			longitude = "0.0";
			LogUtils.e("地理位置信息获取失败");
//		}
		if (latitude.equals("0.0") && longitude.equals("0.0")) {
			handler.sendEmptyMessage(7);
		}
		AppUpdateCallBack.RequestLocation request = new AppUpdateCallBack.RequestLocation(latitude, longitude
				, new RequestCallBack() {
			@Override
			public void requestResult(Request result) {
				// TODO 自动生成的方法存根
				AppUpdateCallBack.RequestLocation lcr = (AppUpdateCallBack.RequestLocation) result;
				StringBuffer builder = new StringBuffer();
				builder.append(lcr.province).append(" ");
				builder.append(lcr.locality).append(" ");
				builder.append(lcr.subLocality);
				cityName = builder.toString();
				editUserInfo.setEdResideCity(cityName);
				handler.sendEmptyMessage(10);
			}
		});
		CrashApplication.getInstance().getQueue().add(request);
	}

	private void createDialog(int id) {
		Dialog dialog = null;
		Builder builder = new Builder(mContext);
		switch (id) {
		case GENDER_DIALOG:
			builder.setTitle(R.string.person_info_gender);
			builder.setSingleChoiceItems(R.array.gender, 0,
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stud
							if (which == 1) {
								editUserInfo.setEdGender("2");
							} else if (which == 0) {
								editUserInfo.setEdGender("1");
							}
							handler.sendEmptyMessage(5);
							dialog.dismiss();
						}
					});
			builder.setNegativeButton(R.string.alert_btn_cancel,
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
			dialog = builder.create();
			break;
		case DATE_DIALOG:
			calendar = Calendar.getInstance();
			dialog = new DatePickerDialog(this,
					new DatePickerDialog.OnDateSetListener() {
						public void onDateSet(DatePicker dp, int year,
								int month, int dayOfMonth) {
							editUserInfo.setEdBirthDay(dayOfMonth);
							editUserInfo.setEdBirthYear(year);
							editUserInfo.setEdBirthMonth(month + 1);
							Calendar cal = new GregorianCalendar(year, month,
									dayOfMonth);
							String constellation = JudgeZodicaAndConstellation
									.date2Constellation(cal);
							String zodiac = JudgeZodicaAndConstellation
									.date2Zodica(cal);
							editUserInfo.setEdZodiac(zodiac);
							editUserInfo.setEdConstellation(constellation);
							editUserInfo.setBirthday(year + "-" + (month + 1)
									+ "-" + dayOfMonth);
							handler.sendEmptyMessage(5);
						}
					}, calendar.get(Calendar.YEAR), // 传入年份
					calendar.get(Calendar.MONTH), // 传入月份
					calendar.get(Calendar.DAY_OF_MONTH) // 传入天数
			);
			dialog.setTitle(R.string.person_info_birth);
			break;
		case SCHOOL_DIALOG:
			builder.setTitle(R.string.person_info_school);
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			schoolDialog = vi.inflate(R.layout.school_dialog, null);
			builder.setView(schoolDialog);
			builder.setNegativeButton(R.string.alert_btn_cancel,
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
			dialog = builder.create();
			dialog.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface arg0) {
					// TODO Auto-generated method stub
					schoolListAdapter = null;
				}
			});
			initSchoolDialog(dialog);
			break;

			case LOCATION_DIALOG:
				builder.setTitle(R.string.person_info_local_now);
				LayoutInflater vInflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				locationDialog = vInflater.inflate(R.layout.location_dialog, null);
				builder.setView(locationDialog);
				builder.setNegativeButton(R.string.alert_btn_cancel,
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {


							}
						});
				dialog = builder.create();
				dialog.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface arg0) {


					}
				});
				initLocationDialog(dialog);
				break;
		default:
			break;
		}
		dialog.show();
	}

	private void initLocationDialog(final Dialog dialog) {
		initJsonData();

		mProvince = locationDialog.findViewById(R.id.id_province);
		mCity = locationDialog.findViewById(R.id.id_city);
		mArea = locationDialog.findViewById(R.id.id_area);
		btnShowChoose = locationDialog.findViewById(R.id.btn_showchoose);
		btnShowChoose.setVisibility(View.VISIBLE);
		initDatas();

		mProvince.setViewAdapter(new ArrayWheelAdapter<String>(this, mProvinceDatas));
		// 添加change事件
		mProvince.addChangingListener(this);
		// 添加change事件
		mCity.addChangingListener(this);
		// 添加change事件
		mArea.addChangingListener(this);

		btnShowChoose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

//				Toast.makeText(mContext, mCurrentProviceName + mCurrentCityName + mCurrentAreaName, 1).show();

				location.setText(mCurrentProviceName + " " + mCurrentCityName + " " + mCurrentAreaName);
				dialog.dismiss();
			}
		});

		mProvince.setVisibleItems(5);
		mCity.setVisibleItems(5);
		mArea.setVisibleItems(5);
		updateCities();
		updateAreas();
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mAreaDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[]{""};
			mCurrentAreaName = "";
		} else {
			mCurrentAreaName = mAreaDatasMap.get(mCurrentCityName)[0];
		}
		mArea.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mArea.setCurrentItem(0);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[]{""};
		}
		mCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mCity.setCurrentItem(0);
		updateAreas();
	}

	/**
	 * 解析整个Json对象，完成后释放Json对象的内存
	 */
	private void initDatas() {
		try {
			JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
			mProvinceDatas = new String[jsonArray.length()];
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonP = jsonArray.getJSONObject(i);// 每个省的json对象
				String province = jsonP.getString("p");// 省名字

				mProvinceDatas[i] = province;

				JSONArray jsonCs = null;
				try {
					/**
					 * Throws JSONException if the mapping doesn't exist or is
					 * not a JSONArray.
					 */
					jsonCs = jsonP.getJSONArray("c");
				} catch (Exception e1) {
					continue;
				}
				String[] mCitiesDatas = new String[jsonCs.length()];
				for (int j = 0; j < jsonCs.length(); j++) {
					JSONObject jsonCity = jsonCs.getJSONObject(j);
					String city = jsonCity.getString("n");// 市名字
					mCitiesDatas[j] = city;
					JSONArray jsonAreas = null;
					try {
						/**
						 * Throws JSONException if the mapping doesn't exist or
						 * is not a JSONArray.
						 */
						jsonAreas = jsonCity.getJSONArray("a");
					} catch (Exception e) {
						continue;
					}

					String[] mAreasDatas = new String[jsonAreas.length()];// 当前市的所有区
					for (int k = 0; k < jsonAreas.length(); k++) {
						String area = jsonAreas.getJSONObject(k).getString("s");// 区域的名称
						mAreasDatas[k] = area;
					}
					mAreaDatasMap.put(city, mAreasDatas);
				}

				mCitisDatasMap.put(province, mCitiesDatas);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		mJsonObj = null;
	}
	/**
	 * 从assert文件夹中读取省市区的json文件，然后转化为json对象
	 */
	private void initJsonData() {
		try {
			StringBuffer sb = new StringBuffer();
			InputStream is = getAssets().open("city.json");
			int len = -1;
			byte[] buf = new byte[1024];
			while ((len = is.read(buf)) != -1) {
				sb.append(new String(buf, 0, len, "gbk"));
			}
			is.close();
			mJsonObj = new JSONObject(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onResume() {
		super.onResume();
	}
	
	public void finish() {
		super.finish();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == mProvince) {
			updateCities();
		} else if (wheel == mCity) {
			updateAreas();
		} else if (wheel == mArea) {
			mCurrentAreaName = mAreaDatasMap.get(mCurrentCityName)[newValue];
		}
	}
}
