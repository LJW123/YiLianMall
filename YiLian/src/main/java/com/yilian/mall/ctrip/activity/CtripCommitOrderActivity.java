package com.yilian.mall.ctrip.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.fragment.order.CtripCommitOrderSelectArrivalTimeDialogFragment;
import com.yilian.mall.ctrip.popupwindow.CtripCommitOrderPriceParticularsPopwindow;
import com.yilian.mall.ctrip.popupwindow.CtripCommitOrderWriteExplainPopwindow;
import com.yilian.mall.ctrip.popupwindow.CtripOrderCancelPolicyPopwindow;
import com.yilian.mall.ctrip.util.JumpCtripActivityUtils;
import com.yilian.mall.entity.BookRoomInfo;
import com.yilian.mall.ui.AddressEditActivity;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.RegExUtils;
import com.yilian.networkingmodule.entity.ctrip.CtripBookRoomOrderEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripCheckBookable;
import com.yilian.networkingmodule.entity.ctrip.CtripCommitOrderEntity;
import com.yilian.networkingmodule.entity.ctrip.ctripEventBusModel.CtripOrderListUpdateModel;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.yilian.mylibrary.Constants.CTRIP_CONTACT_SERVICE;
import static com.yilian.networkingmodule.entity.ctrip.CtripCheckBookable.BOOKABLE;

public class CtripCommitOrderActivity extends BaseAppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_CODE = 99;//请求获取联系人权限请求码
    public static final int CHOOSE_CONTACT = 1;
    public static final String TAG_ROOM_INFO = "tag_room_info";
    String bookInfoUnCompleteInfo = "";
    private TextView tvLeft;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private View viewLine;
    private LinearLayout llTitle;
    private TextView tvRoomType;
    private TextView tvLiveDate;
    private TextView tvRoomLabels;
    private TextView tvRoomCount;
    private TextView tvSelectRoomCount;
    private RecyclerView recyclerViewRoomCount;
    private LinearLayout llEnterPeople;
    private EditText etLinkPerson;
    private EditText etLinkPhone;
    private LinearLayout llContacts;
    private TextView tvEnterTime;
    private TextView tvHotelPhone;
    private LinearLayout llInvoice;
    private TextView tvInvoice;
    private TextView tvDocuments;
    private TextView tvPrice;
    private TextView tvReturnBean;
    private TextView tvDetail;
    private TextView tvCommit;
    private BookRoomInfo mBookRoomInfo;
    private ArrayList<RoomCount> mRoomCount;
    private EnterPeopleCountAdapter enterPeopleCountAdapter;
    private LinearLayout llExplain;
    private CtripCommitOrderWriteExplainPopwindow explainPop;
    /**
     * 明细
     */
    private CtripCommitOrderPriceParticularsPopwindow priceParticularsPop;
    /**
     * 选择的间数
     */
    private int mSelectRoomCount = 1;
    private ArrayList<EditText> editTextsEnterPeopleName = new ArrayList<>();
    /**
     * 入住人数
     */
    private int mPeopleCount = 1;
    /**
     * 最晚到店时间
     */
    private String mLateArrivalTime;
    //    private CtripHotelDetailEntity.DataBean.ArrivalOperationsBean mArrivalTime;
    private CtripCommitOrderSelectArrivalTimeDialogFragment instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctrip_commit_order);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        tvLeft = (TextView) findViewById(R.id.tv_left);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        viewLine = (View) findViewById(R.id.view_line);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        tvRoomType = (TextView) findViewById(R.id.tv_room_type);
        tvLiveDate = (TextView) findViewById(R.id.tv_live_date);
        tvRoomLabels = (TextView) findViewById(R.id.tv_room_label);
        tvRoomCount = (TextView) findViewById(R.id.tv_room_count);
        tvSelectRoomCount = (TextView) findViewById(R.id.tv_select_room_count);
        recyclerViewRoomCount = (RecyclerView) findViewById(R.id.recycler_view_room_count);
        recyclerViewRoomCount.setLayoutManager(new GridLayoutManager(mContext, 5));
        llEnterPeople = (LinearLayout) findViewById(R.id.ll_enter_people);
        etLinkPerson = (EditText) findViewById(R.id.et_link_person);
        etLinkPhone = (EditText) findViewById(R.id.et_link_phone);
        llContacts = (LinearLayout) findViewById(R.id.ll_contacts);
        tvEnterTime = (TextView) findViewById(R.id.tv_enter_time);
        tvHotelPhone = (TextView) findViewById(R.id.tv_hotel_phone);
        llInvoice = (LinearLayout) findViewById(R.id.ll_invoice);
        tvInvoice = (TextView) findViewById(R.id.tv_invoice);
        tvDocuments = (TextView) findViewById(R.id.tv_documents);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvReturnBean = (TextView) findViewById(R.id.tv_return_bean);
        tvDetail = (TextView) findViewById(R.id.tv_detail);
        tvCommit = (TextView) findViewById(R.id.tv_commit);
        llExplain = findViewById(R.id.ll_explain);


    }

    private void initData() {
        mBookRoomInfo = (BookRoomInfo) getIntent().getSerializableExtra(TAG_ROOM_INFO);
        tvSelectRoomCount.setText(String.format("每间最多住%s人", mBookRoomInfo.maxPeoplePerRoom));
        v3Title.setText(mBookRoomInfo.hotelName);
        tvRoomType.setText(mBookRoomInfo.roomType);
        tvLiveDate.setText(String.format("入住%s  离店%s  %s晚", mBookRoomInfo.startDate, mBookRoomInfo.endDate, mBookRoomInfo.dateArea));
        // 0 无wifi  1、2 全部房间WIFI  3、4 部分房间WIFI
        String netMsg = "无WI-FI";
        if (mBookRoomInfo.wirelessBroadnet == 1 || mBookRoomInfo.wirelessBroadnet == 2) {
            netMsg = "全部房间WI-FI";
        } else if (mBookRoomInfo.wirelessBroadnet == 3 || mBookRoomInfo.wirelessBroadnet == 4) {
            netMsg = "部分房间WI-FI";
        }
        String breakfast = "无早餐";
        if (mBookRoomInfo.breakfast == 1) {
            breakfast = "单份早餐";
        } else if (mBookRoomInfo.breakfast == 2) {
            breakfast = "双份早餐";
        }
        tvRoomLabels.setText(String.format("%s %s %s", mBookRoomInfo.bedName, netMsg, breakfast));
        mLateArrivalTime = mBookRoomInfo.latestTime;
        tvEnterTime.setText(mBookRoomInfo.latestTime + (mBookRoomInfo.isMustBe.equals("T") ? "(必须）" : ""));
        //特殊要求
        String tips = String.format("请联系酒店：%s", mBookRoomInfo.hotelPhone);
        SpannableString spannableString = new SpannableString(tips);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + mBookRoomInfo.hotelPhone);
                intent.setData(data);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#4289FF"));
                ds.setUnderlineText(false);
            }
        }, tips.length() - mBookRoomInfo.hotelPhone.length(), tips.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvHotelPhone.setMovementMethod(LinkMovementMethod.getInstance());//设置可点击状态
        tvHotelPhone.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明
        tvHotelPhone.setText(spannableString);

        //说明
        String explain = String.format("扣款说明：该订单确认后不可被取消修改，" +
                "若未入住将收取您首日房费RMB%s，如提前离店将扣除提前离店当天的费用。" +
                "益联益家会根据您的付款方式进行预授权或扣除房费，如订单不确认将解除预授权或全额退款至您的付款账户。" +
                "附加服务费用将与房费同时扣除或返还。", mBookRoomInfo.roomDailyPrice);
        SpannableString explainStr = new SpannableString(explain);
        explainStr.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tvDocuments.setText(explainStr);

        mRoomCount = new ArrayList<RoomCount>();
        for (int i = 0; i < mBookRoomInfo.maxRoomCount; i++) {
            RoomCount roomCount = new RoomCount(String.valueOf(i + 1));
            if (i == 0) {
                roomCount.isChecked = true;
                tvRoomCount.setText("1间");
                llEnterPeople.removeAllViews();
                View view1 = View.inflate(mContext, R.layout.activity_ctrip_commit_order_edittext, null);
                EditText editText = view1.findViewById(R.id.et_name);
                editTextsEnterPeopleName.add(editText);
                if (i == mSelectRoomCount - 1) {
                    View line = view1.findViewById(R.id.view_line);
                    line.setVisibility(View.GONE);
                }
                llEnterPeople.addView(view1);

                mPeopleCount = mSelectRoomCount * mBookRoomInfo.maxPeoplePerRoom;
            }
            mRoomCount.add(roomCount);
        }
        enterPeopleCountAdapter = new EnterPeopleCountAdapter(mRoomCount);
        recyclerViewRoomCount.setAdapter(enterPeopleCountAdapter);
        setPrice();
    }

    private void initListener() {
        tvDetail.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        tvCommit.setOnClickListener(this);
        llExplain.setOnClickListener(this);
        llInvoice.setOnClickListener(this);
        llContacts.setOnClickListener(this);
//        tvEnterTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if (instance == null) {
//                    instance = CtripCommitOrderSelectArrivalTimeDialogFragment.getInstance(
//                                    mBookRoomInfo.arrivalOperationsBeans,
//                                    new CtripCommitOrderSelectArrivalTimeDialogFragment.ArraivalTimeSelected() {
//                                        @Override
//                                        public void onArraivalTimeSelected(
//                                                CtripHotelDetailEntity.DataBean.ArrivalOperationsBean arrivalOperationsBean) {
//                                            mArrivalTime = arrivalOperationsBean;
//                                            tvEnterTime.setText(mArrivalTime.name);
//                                            mLateArrivalTime = mArrivalTime.timeStr;
//                                        }
//                                    }, mArrivalTime == null ? "" : (TextUtils.isEmpty(mArrivalTime.name) ? "" : mArrivalTime.name));
////                }
//                instance.show(getSupportFragmentManager(), CtripCommitOrderSelectArrivalTimeDialogFragment.TAG);
//            }
//        });
        tvSelectRoomCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerViewRoomCount.getVisibility() == View.VISIBLE) {
                    recyclerViewRoomCount.setVisibility(View.GONE);
                    tvSelectRoomCount.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            ContextCompat.getDrawable(mContext, R.mipmap.arrows_down_gray), null);
                } else {
                    recyclerViewRoomCount.setVisibility(View.VISIBLE);
                    tvSelectRoomCount.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            ContextCompat.getDrawable(mContext, R.mipmap.arrows_up_gray), null);
                }
                setPrice();
            }
        });
        enterPeopleCountAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ArrayList<RoomCount> adapterDatas = (ArrayList<RoomCount>) adapter.getData();
                for (int i = 0; i < adapterDatas.size(); i++) {
                    RoomCount roomCount = adapterDatas.get(i);
                    if (i == position) {
                        roomCount.isChecked = true;
                    } else {
                        roomCount.isChecked = false;
                    }
                }
                enterPeopleCountAdapter.notifyDataSetChanged();
                RoomCount item = (RoomCount) adapter.getItem(position);
                tvRoomCount.setText(String.format("%s间", item.count));
                mSelectRoomCount = NumberFormat.convertToInt(item.count, 0);
                mPeopleCount = mSelectRoomCount * mBookRoomInfo.maxPeoplePerRoom;
                editTextsEnterPeopleName.clear();
                llEnterPeople.removeAllViews();
                for (int i = 0; i < mSelectRoomCount; i++) {
                    View view1 = View.inflate(mContext, R.layout.activity_ctrip_commit_order_edittext, null);
                    EditText editText = view1.findViewById(R.id.et_name);
                    editTextsEnterPeopleName.add(editText);
                    if (i == mSelectRoomCount - 1) {
                        View line = view1.findViewById(R.id.view_line);
                        line.setVisibility(View.GONE);
                    }
                    llEnterPeople.addView(view1);
                }
                setPrice();
            }
        });
    }


    void setPrice() {
        tvPrice.setText(getOrderTotalPrice());
        tvReturnBean.setText(String.format("+%s", getOrderTotalReturnBean()));
    }

    //TODO 测试输入
//    private void setTestData(EditText editText) {
//        editText.setText("测试人员");
//    }
//    private void setTestData(){
//        etLinkPhone.setText("18203660536");
//        etLinkPerson.setText("测试人员");
//    }

    public String getLiveTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR) + "年" + calendar.get(Calendar.MONTH) + "月";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_CONTACT:
                if (resultCode == RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor cursor = managedQuery(contactData, null, null, null, null);
                    namePhone(cursor);
                }
                break;
            default:
                break;
        }
    }

    public void namePhone(Cursor cursor) {

        cursor.moveToFirst();
        int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);

        int phoneNum = 0;
        try {//部分手机在都区联系人权限为询问状态时，不会提示开启权限，参考魅族M3，会直接报异常，所以在此处添加tryCatch处理
            phoneNum = cursor.getInt(phoneColumn);
        } catch (Exception e) {
            showToast("请开启读取联系人权限!");
            e.printStackTrace();
            return;
        }
        String result = "";
        if (phoneNum > 0) {
            // 获得联系人的ID号
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);
            // 获得联系人电话的cursor
            Cursor phone = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
            if (phone.moveToFirst()) {
                for (; !phone.isAfterLast(); phone.moveToNext()) {
                    int index = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int typeindex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    String phoneName = phone.getString(typeindex);
                    String string = phone.getString(index);
                    String phoneNumber;
                    if (string.contains(" ")) {
                        phoneNumber = string.replace(" ", "");//处理部分机型拿到的号码是由空格分隔的情况
                    } else {
                        phoneNumber = string;
                    }
                    etLinkPhone.setText(phoneNumber);
                }
                if (!phone.isClosed()) {
                    phone.close();
                }
            }
        }
    }

    class EnterPeopleCountAdapter extends BaseQuickAdapter<RoomCount, BaseViewHolder> {

        public EnterPeopleCountAdapter(@Nullable List<RoomCount> data) {
            super(R.layout.item_ctrip_book_room_count, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, RoomCount item) {
            helper.setText(R.id.tv_room_counts, item.count);
            if (item.isChecked) {
                helper.setBackgroundRes(R.id.tv_room_counts, R.drawable.bg_blue_stroke_5);
                helper.setTextColor(R.id.tv_room_counts, Color.parseColor("#4289FF"));
            } else {
                helper.setBackgroundRes(R.id.tv_room_counts, R.drawable.bg_white_stroke_5);
                helper.setTextColor(R.id.tv_room_counts, ContextCompat.getColor(mContext, R.color.color_333));
            }
        }
    }

    class RoomCount {
        public String count;
        public boolean isChecked;

        public RoomCount(String count) {
            this.count = count;
        }
    }    /**
     * 获取订单总价
     *
     * @return
     */
    String getOrderTotalPrice() {
//        单价*房间数*天数
        return new BigDecimal(mBookRoomInfo.roomDailyPrice).multiply(new BigDecimal(String.valueOf(mSelectRoomCount))).multiply(new BigDecimal(
                String.valueOf(mBookRoomInfo.dateArea)
        )).toString();
    }



    /**
     * 获取订单赠送益豆数量
     *
     * @return
     */
    String getOrderTotalReturnBean() {
//        单价*房间数*天数
        return new BigDecimal(mBookRoomInfo.returnDailyBean).multiply(new BigDecimal(String.valueOf(mSelectRoomCount))).multiply(new BigDecimal(
                String.valueOf(mBookRoomInfo.dateArea)
        )).toString();
    }


    /**
     * 预定房间下订单
     *
     * @param inclusiveAmount
     */
    @SuppressWarnings("unchecked")
    private void bookRoom(String inclusiveAmount) {
        //为跟 ios请求一直 '总共入住人数' 改为 总共入住人数=预定房间数量
        mPeopleCount = mSelectRoomCount;
        RetrofitUtils3.getRetrofitService(mContext)
                .commitBookRoomOrder("xc_order/xc_make_order", mSelectRoomCount, mBookRoomInfo.roomId, mBookRoomInfo.hotelCode,
                        getEnterNames(), getContactName(), getContactPhone(), mLateArrivalTime, mLateArrivalTime,
                        mBookRoomInfo.startDate, mBookRoomInfo.endDate, mPeopleCount, inclusiveAmount,
                        inclusiveAmount, mBookRoomInfo.getIsHourlyRoom())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CtripBookRoomOrderEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(CtripBookRoomOrderEntity ctripBookRoomOrderEntity) {
                        /**
                         * {@link com.yilian.mall.ctrip.fragment.order.CtripOrderCommonFragment}
                         */
                        EventBus.getDefault().post(new CtripOrderListUpdateModel(CtripOrderListUpdateModel.HandleType_add, ctripBookRoomOrderEntity.resIDValue));
//                        showToast("预定成功,跳转支付");
                        CtripCommitOrderEntity entity = new CtripCommitOrderEntity();
                        entity.hotelId = mBookRoomInfo.hotelId;
                        entity.roomId = mBookRoomInfo.roomId;
                        entity.orderIndex = ctripBookRoomOrderEntity.id;
                        entity.orderPrice = ctripBookRoomOrderEntity.amount;
                        entity.returnBean = ctripBookRoomOrderEntity.returnBean;
                        entity.ResID_Value = ctripBookRoomOrderEntity.resIDValue;
                        entity.hotelName = mBookRoomInfo.hotelName;
                        entity.roomType = mBookRoomInfo.roomType;
                        entity.bedName = mBookRoomInfo.bedName;
                        // 0 无wifi  1、2 全部房间WIFI  3、4 部分房间WIFI
                        String netMsg = "无WI-FI";
                        if (mBookRoomInfo.wirelessBroadnet == 1 || mBookRoomInfo.wirelessBroadnet == 2) {
                            netMsg = "全部房间WI-FI";
                        } else if (mBookRoomInfo.wirelessBroadnet == 3 || mBookRoomInfo.wirelessBroadnet == 4) {
                            netMsg = "部分房间WI-FI";
                        }
                        entity.netMsg = netMsg;
                        entity.breakfast = mBookRoomInfo.breakfast;
                        entity.checkIn = mBookRoomInfo.startDate;
                        entity.checkOut = mBookRoomInfo.endDate;
                        entity.duration = String.valueOf(mBookRoomInfo.dateArea);
                        entity.number = String.valueOf(mSelectRoomCount);
                        entity.checkInPerson = getEnterNames();
                        entity.linkman = getContactName();
                        entity.phone = getContactPhone();

                        stopMyDialog();
                        JumpCtripActivityUtils.toCtripOrderPayment(mContext, entity);
                        finish();
                    }
                });
    }


    String getEnterNames() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < editTextsEnterPeopleName.size(); i++) {
            EditText editText = editTextsEnterPeopleName.get(i);
            String inputName = editText.getText().toString().trim();
            if (!TextUtils.isEmpty(inputName)) {
                if (i < editTextsEnterPeopleName.size() - 1) {
                    stringBuilder.append(inputName).append(",");
                } else {
                    stringBuilder.append(inputName);
                }
            } else {
                return "";
            }

        }
        return stringBuilder.toString();
    }

    String getContactName() {
        return etLinkPerson.getText().toString().trim();
    }

    String getContactPhone() {
        return etLinkPhone.getText().toString().trim();
    }

    /**
     * 预定信息是否完整
     *
     * @return
     */
    boolean bookInfoUnComplete() {
        if (mSelectRoomCount < 1) {
            bookInfoUnCompleteInfo = "最少选择一间";
            return false;
        }
        if (TextUtils.isEmpty(getEnterNames())) {
            bookInfoUnCompleteInfo = "每间需填写1人姓名";
            return false;
        }
        if (TextUtils.isEmpty(getContactName())) {
            bookInfoUnCompleteInfo = "请填写联系人";
            etLinkPerson.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(getContactPhone())) {
            bookInfoUnCompleteInfo = "请填写联系人电话号码";
            etLinkPhone.requestFocus();
            return false;
        }
        if (!RegExUtils.isPhone(getContactPhone())) {
            showToast("请输入正确手机号");
            return false;
        }

        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v3Back:
                finish();
                break;
            case R.id.ll_explain:
                if (explainPop == null) {
                    explainPop = new CtripCommitOrderWriteExplainPopwindow(mContext);
                }
                explainPop.showPop(llExplain);
                break;
            case R.id.ll_invoice:
                JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext, CTRIP_CONTACT_SERVICE, false);
                break;
            case R.id.ll_contacts:
                //选择联系人
                if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, CHOOSE_CONTACT);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS}, REQUEST_CODE);
                }
                break;
            case R.id.tv_detail:
                if (priceParticularsPop == null) {
                    priceParticularsPop = new CtripCommitOrderPriceParticularsPopwindow(mContext);
                    priceParticularsPop.setCommit(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            commit();
                        }
                    });
                }
//                if (priceParticularsPop.setContent(mBookRoomInfo.startDate, mBookRoomInfo.dateArea, mSelectRoomCount, getOrderTotalPrice(), getOrderTotalReturnBean(), mBookRoomInfo.returnDailyBean)) {
                priceParticularsPop.setContent(mBookRoomInfo.startDate, mBookRoomInfo.dateArea, mSelectRoomCount, getOrderTotalPrice(), getOrderTotalReturnBean(), mBookRoomInfo.roomDailyPrice);
                priceParticularsPop.showPop(tvDetail);
//                }
                break;
            case R.id.tv_commit:
                commit();
                break;
            default:
                break;
        }
    }

    /**
     * 提交订单
     */
    private void commit() {
        checkRoomBookable();
    }


    /**
     * 检查房间是否可以预定
     */
    @SuppressWarnings("unchecked")
    private void checkRoomBookable() {
        if (!bookInfoUnComplete()) {
            showToast(bookInfoUnCompleteInfo);
            return;
        }
        //判断是否登录
        if (!isLogin()) {
            JumpToOtherPageUtil.getInstance().jumpToLeFenPhoneLoginActivity(mContext);
            return;
        }

        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getRoomBookableStatus("xc_order/xc_check_order", mBookRoomInfo.hotelCode,
                        mBookRoomInfo.roomId, mBookRoomInfo.startDate, mBookRoomInfo.endDate, String.valueOf(mPeopleCount),
                        String.valueOf(mSelectRoomCount), mLateArrivalTime
                )
//                .getRoomBookableStatus("xc_order/xc_check_order", "776692",
//                        "96866517", "2018-10-30", "2018-10-31", "1",
//                        "1", "2018-10-30T14:00:00"
//                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CtripCheckBookable>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(CtripCheckBookable ctripCheckBookable) {
                        if (ctripCheckBookable.availabilityStatus == BOOKABLE) {
//                            可预定
                            bookRoom(ctripCheckBookable.inclusiveAmount);
                        } else {
                            showToast(ctripCheckBookable.msg);
                            stopMyDialog();
                        }
                    }
                });
        addSubscription(subscription);
    }


}
