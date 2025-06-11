package com.iyuba.camstory;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.utils.SaveImage;
import com.iyuba.camstory.utils.SelectPicUtils;
import com.iyuba.camstory.utils.UtilPostUpic;
import com.iyuba.camstory.widget.CustomDialog;
import com.iyuba.camstory.widget.CustomToast;
import com.iyuba.http.LogUtils;
import com.iyuba.voa.activity.setting.Constant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class UpLoadImageActivity extends Activity implements OnClickListener {
    private static final String TAG = UpLoadImageActivity.class.getSimpleName();

    public static final int PHOTOHRAPH = 1;// 拍照
    public static final int PHOTOZOOM = 2; // 缩放
    public static final int PHOTORESULT = 3;// 结果
    public static final int NONE = 0;

    private Context mContext;
    private View backView;
    private ImageView userImage;
    private Button upLoad, photo, local, skip, back;
    private CustomDialog waitingDialog;
    /*
     * 变量声明 newName：上传后在服务器上的文件名称 uploadFile：要上传的文件路径 actionUrl：服务器对应的程序路径
     */
    private String actionUrl = "http://api." + Constant.IYBHttpHead2 + "/v2/avatar?uid=";

    private DisplayImageOptions option = new DisplayImageOptions.Builder().cacheInMemory(false)
            .cacheOnDisk(false).displayer(new FadeInBitmapDisplayer(800)).delayBeforeLoading(400).build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_uploadimage);
        backView = findViewById(R.id.backlayout);
        backView.setBackgroundColor(Constant.getBackgroundColor());
        mContext = this;
        waitingDialog = waitingDialog();
        initWidget();
        UpLoadImageActivityPermissionsDispatcher.openCarmerWithPermissionCheck(this);
    }

    private void initWidget() {
        userImage = findViewById(R.id.userImage);
        back = findViewById(R.id.upload_back_btn);
        upLoad = findViewById(R.id.upLoad);
        photo = findViewById(R.id.photo);
        local = findViewById(R.id.local);
        skip = findViewById(R.id.skip);
        back.setOnClickListener(this);
        photo.setOnClickListener(this);
        local.setOnClickListener(this);
        upLoad.setOnClickListener(this);
        skip.setOnClickListener(this);
        ImageLoader.getInstance().displayImage(
                Constant.getUserimage() + AccountManager.getInstance().userId + "&size=big", userImage,
                option);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == NONE)
            return;
        // 拍照
        if (requestCode == PHOTOHRAPH) {
            SelectPicUtils.cropPicture(this, getUri(), PHOTORESULT, "header.jpg");
        }
        // 读取相册缩放图片
        if (requestCode == PHOTOZOOM && data != null) {

            String path = SelectPicUtils.getPath(this, data.getData());
            Uri uri = FileProvider.getUriForFile(this, getPackageName() , new File(path));
            SelectPicUtils.cropPicture(this, uri, PHOTORESULT, "header.jpg");// Uri.fromFile(new File(path)),

        }
        // 处理结果
        if (requestCode == PHOTORESULT && data != null) {
            File cropFile = new File(Environment.getExternalStorageDirectory() + "/header.jpg");
            Bitmap bitmap = BitmapFactory.decodeFile(cropFile.getAbsolutePath(), getBitmapOption(2));

            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(getUri()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                bitmap = BitmapFactory.decodeFile(getUri().getPath());
            }
            userImage.setImageBitmap(bitmap);
            ImageLoader.getInstance().cancelDisplayTask(userImage);
            SaveImage.saveImage(Environment.getExternalStorageDirectory() + "/header.jpg", bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private BitmapFactory.Options getBitmapOption(int inSampleSize) {
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }

    private void showDialog(String mess) {
        new AlertDialog.Builder(UpLoadImageActivity.this).setTitle("").setMessage(mess)
                .setNegativeButton(R.string.alert_btn_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    waitingDialog.show();
                    break;
                case 2:
                    waitingDialog.dismiss();
                    break;
                case 5:
                    // 保存修改的头像
                    break;
                case 6:
                    handler.sendEmptyMessage(2);
                    upLoad.setClickable(true);
                    Toast.makeText(getApplicationContext(), R.string.uploadimage_success, Toast.LENGTH_SHORT)
                            .show();
                    // 将本地缓存旧图片删除
                    /*
                     * File f = new File("/sdcard/com.iyuba.iyubaclient/image/" + uid +
                     * ".jpeg"); f.delete();
                     */
                    // 下载新头像
                    // DownLoadUserImg();
                    handler.sendEmptyMessage(9);
                    break;
                case 7:
                    handler.sendEmptyMessage(2);
                    showDialog(getResources().getString(R.string.uploadimage_failupload));
                    break;
                case 8:
                    handler.sendEmptyMessage(2);
                    showDialog(getResources().getString(R.string.uploadimage_failmodify));
                    break;
                case 9:
//                    Intent intent = new Intent(mContext, VipCenterActivity.class);
//                    intent.putExtra("isregister", true);
//                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    public CustomDialog waitingDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.wetting, null);
        CustomDialog.Builder customBuilder = new CustomDialog.Builder(this);
        CustomDialog cDialog = customBuilder.setContentView(layout).create();
        return cDialog;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.upload_back_btn) {
            finish();
        } else if (id == R.id.photo) {
            // UpLoadImageActivityPermissionsDispatcher.openCarmerWithPermissionCheck(this);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT >= 24) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getUri());
            startActivityForResult(intent, PHOTOHRAPH);
        } else if (id == R.id.local) {
            // 手机相册中选择
            Intent intent;
            intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, PHOTOZOOM);
//			SelectPicUtils.selectPicture(UpLoadImageActivity.this, PHOTOZOOM);
        } else if (id == R.id.skip) {
            Intent intent = new Intent(mContext, VipCenterGoldActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.upLoad) {
            final File tempHead = new File(Environment.getExternalStorageDirectory() + "/header.jpg");//temp
            if (!tempHead.exists()) {
                finish();
                return;
            }
            handler.sendEmptyMessage(0);
            CustomToast.showToast(mContext, R.string.uploadimage_uploading, Toast.LENGTH_SHORT);
            Thread upload = new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        String success = UtilPostUpic.post(actionUrl + AccountManager.getInstance().userId,
                                tempHead);
                        LogUtils.e(TAG, success);
                        if (success == null) {
                            return;
                        }
                        JSONObject jsonObject = new JSONObject(success.substring(success.indexOf("{"),
                                success.lastIndexOf("}") + 1));
                        System.out.println("cc=====" + jsonObject.getString("status"));
                        if (jsonObject.getString("status").equals("0")) {// status 为0则修改成功
                            handler.sendEmptyMessage(6);
                        } else {
                            handler.sendEmptyMessage(8);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            upload.start();
        }
    }

    private Uri getUri() {
        File file = new File(Environment.getExternalStorageDirectory(), "header.jpg");//temp
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(this, getPackageName(), file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void openCarmer() {
        //获取权限后操作
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showDeniedForCamera() {
        Toast.makeText(this, getString(R.string.permission_deny), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        UpLoadImageActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
