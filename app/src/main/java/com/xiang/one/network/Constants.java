package com.xiang.one.network;
import com.xiang.one.App;
import com.xiang.one.network.model.UserModel;
import com.xiang.one.network.model.UserModelDB;

import io.objectbox.Box;

/**
 * 09/08/2017  11:20 AM
 * Created by Zhang.
 */

public class Constants {

    public static final String RESPONSE_SUCCESS ="RESPONSE_OK";
    public static final String RESPONSE_FAIL ="RESPONSE_FAIL";
    public static final int MAX_FAILURE_COUNT= 1;

    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    public static final int MAX_ALLOWED_UPLOAD_IMAGES = 4;
    public static final int IMAGE_ITEM_ADD= -1;
    public static final String SHARE_LAST_SEND_SMS = "LAST_SEND_SMS";
    public static final int INVALIDATE_RESOURCE = -1;
    public static final int PASS = 0;
    public static final int FAIL = 1;
    public static final int WEB= 0;
    public static final int APP= 1;




    /*计分方式*/
    public static final String SCORE_TYPE_ENTIRE = "1";
    public static final String SCORE_TYPE_SEPARATE= "2";

    /*1我未关注 2我已关注*/
    public static final String FOLLOWED = "2";
    public static final String NOT_FOLLOWED = "1";

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_FOOT= 2;
    public static final int TYPE_COMMENT =  3;
    public static final int TYPE_ADD=  4;

    private static UserModel mCurUser = null;

    /*EventBus Signal*/
    public static final String EVENT_BUS_NOTIFY_TABLE_ITEM_RECYCLE_ADAPTER= "EVENT_BUS_NOTIFY_TABLE_ITEM_RECYCLE_ADAPTER";
    public static final String EVENT_BUS_NOTIFY_CHECKLIST_ID_FINISHED = "EVENT_BUS_NOTIFY_CHECKLIST_ID_FINISHED";
    public static final String EVENT_BUS_NOTIFY_SAVE_ARRANGE_FINISHED = "EVENT_BUS_NOTIFY_SAVE_ARRANGE_FINISHED";
    public static final String EVENT_BUS_UPDATE_ARRANGE_LIST= "EVENT_BUS_UPDATE_ARRANGE_LIST";
    public static final String EVENT_BUS_UPDATE_GAI_SHAN_TASK= "EVENT_BUS_UPDATE_GAI_SHAN_TASK";
    public static final String EVENT_BUS_UPDATE_USER_SELECT= "EVENT_BUS_UPDATE_USER_SELECT";
    public static final String EVENT_BUS_CALENDAR_PERSON_SELECT = "EVENT_BUS_CALENDAR_PERSON_SELECT";
    public static final String EVENT_BUS_LOG_OUT= "EVENT_BUS_LOG_OUT";


    public static void setLoginUser(UserModel u) {
        Constants.mCurUser = u;
        if(u != null){
            Constants.mCurUser.setId(u.asDBModel().saveToDb());
        }

    }

    public static UserModel getUser() {
        if(mCurUser == null){
            Box<UserModelDB> userModelBox = App.mBoxStore.boxFor(UserModelDB.class);
            try {
                UserModelDB userInDB = userModelBox.query().build().findUnique();
                if(userInDB == null){
                    return  null;
                }else{
                    mCurUser = userInDB.asUserModel();
                }


            }catch (io.objectbox.exception.DbException  ex){
                mCurUser = null;
                userModelBox.removeAll();
            }

        }
        return mCurUser;
    }
}
