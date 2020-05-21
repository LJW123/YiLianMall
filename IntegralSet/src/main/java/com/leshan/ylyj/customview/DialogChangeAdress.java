package com.leshan.ylyj.customview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.bean.CityBean;
import com.yiguo.adressselectorlib.AddressSelector;
import com.yiguo.adressselectorlib.CityInterface;
import com.yiguo.adressselectorlib.OnItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/25 0025.
 */

public class DialogChangeAdress {

    //编辑dialog
    public static AlertDialog dialog;
    public static Window window;

    //选择城市dialog
//    private AlertDialog dialog;
//    private Window window;
    //城市
    private static ArrayList<CityBean> cities1 = new ArrayList<>();
    private static ArrayList<CityBean> cities2 = new ArrayList<>();
    private static ArrayList<CityBean> cities3 = new ArrayList<>();
    private static ArrayList<CityBean> cities4 = new ArrayList<>();
    private static String editprovince, editcity, editcounty, editstreet;

    /**
     * 编辑dialog  编辑，删除，取消
     */
    public static void EditDialog(Context context) {
        dialog = new AlertDialog.Builder(context, R.style.transverse_bespread_dialog).create();
        window = dialog.getWindow();
        dialog.show();
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.dialog_check_correct_car_license, null, false);
        TextView check_car_edit = (TextView) view.findViewById(R.id.check_car_edit);//编辑
        TextView check_car_delete = (TextView) view.findViewById(R.id.check_car_delete);//删除
        TextView check_car_cancel = (TextView) view.findViewById(R.id.check_car_cancel);//取消
        check_car_edit.setOnClickListener((View.OnClickListener) context);
        check_car_delete.setOnClickListener((View.OnClickListener) context);
        check_car_cancel.setOnClickListener((View.OnClickListener) context);
//        AddressSelector address_edit_as=view.findViewById(R.id.address_edit_as);
        window.setContentView(view);
        //这句设置我们dialog在底部
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        //这句就是设置dialog横向满屏了。
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = 1200;
        window.setAttributes(lp);
    }

    /**
     * 删除，取消
     */
    public static void TwoEditDialog(Context context) {
        dialog = new AlertDialog.Builder(context, R.style.transverse_bespread_dialog).create();
        window = dialog.getWindow();
        dialog.show();
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.dialog_check_correct_car_license, null, false);
        TextView check_car_edit = (TextView) view.findViewById(R.id.check_car_edit);
        TextView check_car_delete = (TextView) view.findViewById(R.id.check_car_delete);
        TextView check_car_cancel = (TextView) view.findViewById(R.id.check_car_cancel);
        check_car_edit.setVisibility(View.GONE);
        check_car_edit.setOnClickListener((View.OnClickListener) context);
        check_car_delete.setOnClickListener((View.OnClickListener) context);
        check_car_cancel.setOnClickListener((View.OnClickListener) context);
//        AddressSelector address_edit_as=view.findViewById(R.id.address_edit_as);
        window.setContentView(view);
        //这句设置我们dialog在底部
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        //这句就是设置dialog横向满屏了。
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = 1200;
        window.setAttributes(lp);
    }


    /**
     * 城市列表dialog
     */
    public static void showAuthenticationStateDialog(Context context) {
        dialog = new AlertDialog.Builder(context, R.style.transverse_bespread_dialog).create();
        window = dialog.getWindow();
        dialog.show();
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.adress_edit_address_selector, null, false);
        ImageView adress_edit_close = (ImageView) view.findViewById(R.id.adress_edit_close);
        CityChoice(view, context);//获取城市列表
//        AddressSelector address_edit_as=view.findViewById(R.id.address_edit_as);
        window.setContentView(view);
        //这句设置我们dialog在底部
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        //这句就是设置dialog横向满屏了。
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = 1200;
        window.setAttributes(lp);
    }


    /**
     * 获取城市列表
     */
    public static void CityChoice(View view, Context context) {
//拿到本地JSON 并转成String
        try {
            JSONArray jsonArray = new JSONArray(context.getString(R.string.cities1));
            for (int i = 0; i < jsonArray.length(); i++) {
                cities1.add(new Gson().fromJson(jsonArray.get(i).toString(), CityBean.class));
            }
            JSONArray jsonArray2 = new JSONArray(context.getString(R.string.cities2));
            for (int i = 0; i < jsonArray2.length(); i++) {
                cities2.add(new Gson().fromJson(jsonArray2.get(i).toString(), CityBean.class));
            }
            JSONArray jsonArray3 = new JSONArray(context.getString(R.string.cities3));
            for (int i = 0; i < jsonArray3.length(); i++) {
                cities3.add(new Gson().fromJson(jsonArray3.get(i).toString(), CityBean.class));
            }
            JSONArray jsonArray4 = new JSONArray(context.getString(R.string.cities4));
            for (int i = 0; i < jsonArray4.length(); i++) {
                cities3.add(new Gson().fromJson(jsonArray4.get(i).toString(), CityBean.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AddressSelector addressSelector = (AddressSelector) view.findViewById(R.id.address_edit_as);
        addressSelector.setTabAmount(4);
        addressSelector.setCities(cities1);
        addressSelector.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void itemClick(AddressSelector addressSelector, CityInterface city, int tabPosition) {
                switch (tabPosition) {
                    case 0:
                        addressSelector.setCities(cities2);
                        editprovince = city.getCityName();
                        break;
                    case 1:
                        addressSelector.setCities(cities3);
                        editcity = city.getCityName();
                        break;
                    case 2:
                        editcounty = city.getCityName();
                        addressSelector.setCities(cities4);
                        break;
                    case 3:
                        editstreet = city.getCityName();
//                        city_choice_tv.setText(editprovince + editcity + editcounty + editstreet);//获得数据
                        dialog.dismiss();
                        break;
                }
            }
        });
        addressSelector.setOnTabSelectedListener(new AddressSelector.OnTabSelectedListener() {
            @Override
            public void onTabSelected(AddressSelector addressSelector, AddressSelector.Tab tab) {
                switch (tab.getIndex()) {
                    case 0:
                        addressSelector.setCities(cities1);
                        break;
                    case 1:
                        addressSelector.setCities(cities2);
                        break;
                    case 2:
                        addressSelector.setCities(cities3);
                        break;
                    case 3:
                        addressSelector.setCities(cities4);
                        break;
                }
            }

            @Override
            public void onTabReselected(AddressSelector addressSelector, AddressSelector.Tab tab) {

            }
        });
    }

}
