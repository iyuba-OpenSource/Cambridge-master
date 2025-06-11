package com.iyuba.camstory;
import com.android.volley.Request;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.camstory.listener.RequestUserDetailInfo;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.sqlite.mode.EditUserInfo;
import com.iyuba.camstory.widget.CustomDialog;
import com.iyuba.camstory.widget.WaittingDialog;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
/**
 * 详细信息
 * 
 * @author ct
 * @version 1.0
 * @para "currentuid" "currentname"
 */
public class UserDetailInfo extends Activity {
	private TextView tvUserName, tvGender, tvResideLocation, tvBirthday,
			tvConstellation, tvZodiac, tvGraduatesSchool, tvCompany,
			tvAffectivestatus, tvLookingfor, tvIntro, tvInterest;
	private RequestUserDetailInfo userDetailInfo;
	private EditUserInfo editUserInfo = new EditUserInfo();
	private Button back;
	private Button modifyBtn;
	private String currentuid, currentname;
	private CustomDialog waitting;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userdetailinfo);
		mContext = this;
		waitting = new WaittingDialog().wettingDialog(mContext);
		Intent intent = getIntent();
		currentuid = intent.getStringExtra("currentuid");
		currentname = intent.getStringExtra("currentname");
		initWidget();

	}

	@Override
	protected void onResume() {
		super.onResume();
		handler.sendEmptyMessage(0);
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				waitting.show();
				handler.sendEmptyMessage(1);
				break;
			case 1:
				if(AccountManager.getInstance().userId!=0){
					if(AccountManager.getInstance().userId!=Integer.valueOf(currentuid)){
						currentuid = AccountManager.getInstance().userId+"";
					}
				}
				RequestUserDetailInfo request = new RequestUserDetailInfo(Integer.valueOf(currentuid), new RequestCallBack() {
					@Override
					public void requestResult(Request result) {
						RequestUserDetailInfo requestUserDetailInfo = (RequestUserDetailInfo) result;
						if (requestUserDetailInfo.result.equals("211")) {
							userDetailInfo = requestUserDetailInfo;
							editUserInfo = userDetailInfo.editUserInfo;
							currentname = AccountManager.getInstance().userName;
						}
						handler.sendEmptyMessage(2);
					}
				});
				CrashApplication.getInstance().getQueue().add(request);
				break;
			case 2:
				waitting.dismiss();
				setText();
			default:
				break;
			}
		}
	};

	private void setText() {
		tvUserName.setText(currentname);
		if (userDetailInfo.gender.equals("1")) {
			tvGender.setText("男");
		} else if (userDetailInfo.gender.equals("2")) {
			tvGender.setText("女");
		} else if (userDetailInfo.gender.equals("0")) {
			tvGender.setText("保密");
		}
		tvResideLocation.setText(userDetailInfo.resideLocation);
		tvBirthday.setText(userDetailInfo.birthday);
		tvConstellation.setText(userDetailInfo.constellation);
		tvZodiac.setText(userDetailInfo.zodiac);
		tvGraduatesSchool.setText(userDetailInfo.graduateschool);
		tvCompany.setText(userDetailInfo.company);
		tvAffectivestatus.setText(userDetailInfo.affectivestatus);
		tvLookingfor.setText(userDetailInfo.lookingfor);
		tvIntro.setText(userDetailInfo.bio);
		tvInterest.setText(userDetailInfo.interest);
	}

	private void initWidget() {
		tvUserName = findViewById(R.id.tvUserName);
		tvGender = findViewById(R.id.tvGender);
		tvResideLocation = findViewById(R.id.tvResideLocation);
		tvBirthday = findViewById(R.id.tvBirthday);
		tvConstellation = findViewById(R.id.tvConstellation);
		tvZodiac = findViewById(R.id.tvZodiac);
		tvGraduatesSchool = findViewById(R.id.tvGraduatesSchool);
		tvCompany = findViewById(R.id.tvCompany);
		tvAffectivestatus = findViewById(R.id.tvAffectivestatus);
		tvLookingfor = findViewById(R.id.tvLookingfor);
		tvIntro = findViewById(R.id.tvBio);
		tvInterest = findViewById(R.id.tvInterest);
		back = findViewById(R.id.button_back);
		modifyBtn = findViewById(R.id.editinfo_btn);
		modifyBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, EditUserInfoActivity.class);
				intent.putExtra("gender", editUserInfo.getEdGender());
				intent.putExtra("birthday", editUserInfo.getBirthday());
				intent.putExtra("zodiac", editUserInfo.getEdZodiac());
				intent.putExtra("constellation", editUserInfo.getEdConstellation());
				intent.putExtra("location", editUserInfo.getEdResideCity());
				intent.putExtra("school", editUserInfo.getUniversity());
				startActivity(intent);
			}
		});
		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
}
