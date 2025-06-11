package tv.lycam.gift.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tv.lycam.gift.Info;
import tv.lycam.gift.R;
import tv.lycam.gift.consts.GEventCode;
import tv.lycam.gift.core.AndroidEventManager;
import tv.lycam.gift.entity.mqtt.Chat;
import tv.lycam.gift.entity.mqtt.Gift;
import tv.lycam.gift.entity.mqtt.Like;
import tv.lycam.gift.entity.mqtt.MetaInfo;
import tv.lycam.gift.entity.mqtt.UserJoin;
import tv.lycam.gift.entity.mqtt.UserLeave;
import tv.lycam.gift.util.DisplayUtil;
import tv.lycam.gift.widget.span.SimplifySpanBuild;
import tv.lycam.gift.widget.span.other.SpecialGravityEnum;
import tv.lycam.gift.widget.span.unit.SpecialLabelUnit;
import tv.lycam.gift.widget.span.unit.SpecialTextUnit;

/**
 * Created by su on 16/5/19.
 */
public class PublicMessageAdapter extends BaseAdapter {
    protected final int LABEL_TEXTSIZE = 14;
    protected final int CONTENT_TEXTSIZE;
    protected final int COLOR_CONTENT = 0xFF4A4A4A;
    protected Context context;
    protected List<Object> models;
    protected List<String> users;
    private final ObjectMapper mRoot;
    private final int COLOR_NAME = 0xFFFFB90F;
    private final String USER_LEVEL = " LV.%d ";
    private long lastTimeStamp = 100;

    public interface OnMessageDataChanged {
        void onChanged();
    }

    public PublicMessageAdapter(Context context, List<Object> models) {
        this.context = context;
        this.models = models;
        mRoot = new ObjectMapper();
        if (models == null) {
            this.models = new ArrayList<>();
        }
        users = new ArrayList<>();
        CONTENT_TEXTSIZE = DisplayUtil.sp2px(context, 4);
    }

    public synchronized void addString(String msg) {
        models.add(msg);
        notifyDataSetChanged();
    }

    public synchronized void addData(final String model, final OnMessageDataChanged mDataChanged) {
        handleMessage(model, mDataChanged);
    }

    private void handleMessage(String model, OnMessageDataChanged mDataChanged) {
        try {
            JSONObject obj = new JSONObject(model);
            String type = obj.getString("type");
            String content;
            String username;
            Chat chat;
            switch (type) {
                case "barrage":
                    chat = mRoot.readValue(model, Chat.class);//createtory()fromJson(model, Chat.class);
//                    MetaInfo cMetaInfo = chat.getMetaInfo();
//                    AndroidEventManager.getInstance().runUiEvent(GEventCode.Local_Barrage, cMetaInfo.getAvatarUrl(), cMetaInfo.getDisplayName(), chat.getContent(), chat);
                    break;
                case "message":
                    chat = mRoot.readValue(model, Chat.class);
                    if(chat != null){
                        models.add(chat);
                        notifyDataSetChanged();
                        if (mDataChanged != null) {
                            mDataChanged.onChanged();
                        }
                    }
                    break;
                case "gift":
                    Gift gift = mRoot.readValue(model, Gift.class);
                    content = obj.optString("content");
                    MetaInfo gMetaInfo = gift.getMetaInfo();
                    AndroidEventManager.getInstance().runUiEvent(GEventCode.Local_Gift, gMetaInfo.getUserId(), gMetaInfo.getAvatarUrl(), gMetaInfo.getDisplayName(), content, gift);
                    break;
                case "like":
                    Like like = mRoot.readValue(model, Like.class);
                    MetaInfo lMetaInfo = like.getMetaInfo();
                    username = lMetaInfo.getUsername();
                    if (!Info.Username().equals(username)) {
                        AndroidEventManager.getInstance().runUiEvent(GEventCode.Local_Like, lMetaInfo.getDisplayName(), like.getContent(), like);
                    }
                    break;
                //推流的开始、暂停和结束
                case "stream.status":
                    JSONObject j1 = obj.getJSONObject("content");
                    if (j1.has("status") && j1.getString("status").equals("stream.unpublish")) {
                        addString("直播已结束,感谢您的观看!");
                        AndroidEventManager.getInstance().runUiEvent(GEventCode.Local_UnPublish, j1.getString("stream"));
                    }else if(j1.has("status") && j1.getString("status").equals("stream.pause")){
                        addString("老师暂时离开,请稍后……");
                    }
                    break;
                case "user.status":
                    if (obj.has("metaInfo")) {
                        JSONObject jo = obj.getJSONObject("metaInfo");
                        if (jo.has("popular") && jo.get("popular") != null) {
                            int popular = jo.getInt("popular");
                            AndroidEventManager.getInstance().runUiEvent(GEventCode.Local_UserStatus, popular);
                        }
                    }
                    break;
                case "user.join":
                    UserJoin join = mRoot.readValue(model, UserJoin.class);
                    //models.add(join);
                    //notifyDataSetChanged();
                    MetaInfo uiMetaInfo = join.getMetaInfo();
                    if (uiMetaInfo != null)
                        AndroidEventManager.getInstance().runUiEvent(GEventCode.Local_UserJoin, uiMetaInfo);
                    break;
                case "user.leave":
                    UserLeave leave = mRoot.readValue(model, UserLeave.class);
                    //models.add(leave);
                    //notifyDataSetChanged();
                    MetaInfo ulMetaInfo = leave.getMetaInfo();
                    if (ulMetaInfo != null)
                        AndroidEventManager.getInstance().runUiEvent(GEventCode.Local_UserLeave, ulMetaInfo);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getDisplayName(int position) {
        Object o = models.get(position);
        if (o instanceof Chat) {
            String displayName = ((Chat) o).getMetaInfo().getNickname();
            return displayName;
        }
        return "";
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_public_message, null);
            holder = new BaseViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (BaseViewHolder) convertView.getTag();
        }
        Object obj = models.get(position);
        TextView view = holder.getView(R.id.item_chat_msg);
        SimplifySpanBuild span = new SimplifySpanBuild(context, view);
        if (obj instanceof Chat) {
            span = chat((Chat) obj, span);
        } else if (obj instanceof String) {
            span = system(String.valueOf(obj), span);
        }/*else if(obj instanceof UserJoin){
            span = userJoin((UserJoin) obj,span);
        }else if(obj instanceof UserLeave){
            span = userLeave((UserLeave) obj,span);
        }*/
        if (span != null) {
            view.setText(span.build());
        }
        return convertView;
    }

    protected SimplifySpanBuild userJoin(UserJoin obj, SimplifySpanBuild span) {
        String username = obj.getMetaInfo().getDisplayName();
        MetaInfo metaInfo = obj.getMetaInfo();
        span.appendSpecialUnit(new SpecialLabelUnit(String.format(USER_LEVEL, metaInfo.getLevel()), Color.WHITE, 12, 0xFF00BFFF)
                .setLabelBgRadius(5)
                .setGravity(SpecialGravityEnum.CENTER))
                .appendSpecialUnit(new SpecialLabelUnit(username + ": ", COLOR_NAME, LABEL_TEXTSIZE, 0x00ffffff).setPadding(DisplayUtil.dp2px(context, 4)).setGravity(SpecialGravityEnum.CENTER).useTextBold())
                .appendSpecialUnit(new SpecialTextUnit("进入了房间", COLOR_CONTENT, LABEL_TEXTSIZE, SpecialGravityEnum.CENTER).useTextBold());
        return span;
    }

    protected SimplifySpanBuild userLeave(UserLeave obj, SimplifySpanBuild span) {
        String username = obj.getMetaInfo().getDisplayName();
        int level = 1;
        //span.appendSpecialUnit(new SpecialLabelUnit("LV." + level, 0xffe7e7e7, LABEL_TEXTSIZE, 0x8846A4E9).setPadding(DisplayUtil.dp2px(context, 4)).setGravity(SpecialGravityEnum.CENTER))
        MetaInfo metaInfo = obj.getMetaInfo();
        span.appendSpecialUnit(new SpecialLabelUnit(String.format(USER_LEVEL, metaInfo.getLevel()), Color.WHITE, 12, 0xFF00BFFF)
                .setLabelBgRadius(5)
                .setGravity(SpecialGravityEnum.CENTER))
                .appendSpecialUnit(new SpecialLabelUnit(username + ": ", COLOR_NAME, LABEL_TEXTSIZE, 0x00ffffff).setPadding(DisplayUtil.dp2px(context, 4)).setGravity(SpecialGravityEnum.CENTER).useTextBold())
                .appendSpecialUnit(new SpecialTextUnit("离开了房间", COLOR_CONTENT, LABEL_TEXTSIZE, SpecialGravityEnum.CENTER).useTextBold());
        return span;
    }

    protected SimplifySpanBuild chat(Chat chat, SimplifySpanBuild span) {
        Chat.MetaInfoBean metaInfo = chat.getMetaInfo();
        String displayName = " " + metaInfo.getNickname();
        String avatarUrl = metaInfo.getAvatarUrl();
        String content = metaInfo.getContent();
        if ("system".equals(displayName)) {
            //username = "系统消息";
            //span.appendSpecialUnit(new SpecialLabelUnit("LV.1", 0xffe7e7e7, LABEL_TEXTSIZE, 0x8846A4E9).setPadding(DisplayUtil.dp2px(context, 4)).setGravity(SpecialGravityEnum.CENTER));

//            span.appendSpecialUnit(new SpecialLabelUnit(String.format(USER_LEVEL, metaInfo.getLevel()), Color.WHITE, 12, 0xFF00BFFF).setGravity(SpecialGravityEnum.CENTER));

            String nickname = content.substring(0, content.indexOf(" ") + 1);
            content = content.substring(content.indexOf(" ") + 1, content.length());

            span.appendSpecialUnit(new SpecialTextUnit(nickname, COLOR_NAME, LABEL_TEXTSIZE, SpecialGravityEnum.CENTER).useTextBold())
                    .appendSpecialUnit(new SpecialTextUnit(String.valueOf(content), 0xff969BAB, LABEL_TEXTSIZE, SpecialGravityEnum.CENTER).useTextBold());
        } else {
//            span.appendSpecialUnit(new SpecialLabelUnit(String.format(USER_LEVEL, metaInfo.getLevel()), Color.WHITE, 12, 0xFF00BFFF)
//                    .setLabelBgRadius(5)
//                    .setGravity(SpecialGravityEnum.CENTER));
            /*span.appendSpecialUnit(new SpecialTextUnit(displayName + ": ", 0xff00BF7E, CONTENT_TEXTSIZE, SpecialGravityEnum.CENTER))
                    .appendSpecialUnit(new SpecialTextUnit(String.valueOf(content), 0xff969BAB, CONTENT_TEXTSIZE, SpecialGravityEnum.CENTER)
                            .setShowMask(true));*/
            span.appendSpecialUnit(new SpecialTextUnit(displayName + ": ", COLOR_NAME, LABEL_TEXTSIZE, SpecialGravityEnum.CENTER)
                    .setShowMask(false).useTextBold())
                    .appendSpecialUnit(new SpecialTextUnit(String.valueOf(content), COLOR_CONTENT, LABEL_TEXTSIZE, SpecialGravityEnum.CENTER)
                            .setShowMask(false).useTextBold());
        }
        return span;
    }

    protected SimplifySpanBuild system(String msg, SimplifySpanBuild span) {
        span.appendSpecialUnit(new SpecialTextUnit(String.valueOf(msg), 0xff00cd00, LABEL_TEXTSIZE, SpecialGravityEnum.CENTER).useTextBold());
        return span;
    }

}
