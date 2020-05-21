package com.yilian.mall.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.FqaActivityArrayList;
import com.yilian.mall.entity.FqaActivityList;
import com.yilian.mall.http.ServiceNetRequest;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
/**
 * 常见问题
 */
public class FAQActivity extends BaseActivity implements OnItemClickListener {
	private ListView fqa_list;

	@ViewInject(R.id.tv_back)
	private TextView tvBack;


	FqaActivityArrayList list;
	ArrayList<FqaActivityList> listinfor;

	private ServiceNetRequest mServiceNetRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fqa);
		ViewUtils.inject(this);
		tvBack.setText("常见问题");
		listinfor = new ArrayList<FqaActivityList>();
		fqa_list = (ListView) findViewById(R.id.fqa_list);
		fqa_list.setOverScrollMode(View.OVER_SCROLL_NEVER);//禁止listView下拉
		fqa_list.setOnItemClickListener(this);
		setActivityList();
	}

	private void setActivityList() {
		if (mServiceNetRequest == null) {
			mServiceNetRequest = new ServiceNetRequest(mContext);
		}

		mServiceNetRequest.getFQA(FqaActivityArrayList.class, new RequestCallBack<FqaActivityArrayList>() {

			@Override
			public void onStart() {
				super.onStart();
				startMyDialog();
			}

			@Override
			public void onSuccess(ResponseInfo<FqaActivityArrayList> arg0) {
				stopMyDialog();
				list = arg0.result;
				if (list == null) {
					return;
				}
				switch (list.code) {
				case 1:
					if (list.infor.size() > 0) {
						for (int i = 0; i < list.infor.size(); i++) {
							listinfor.add(list.infor.get(i));
						}
						activityAdapter activityAdapter = new activityAdapter(FAQActivity.this, listinfor);
						fqa_list.setAdapter(activityAdapter);
						activityAdapter.notifyDataSetChanged();

					}

					break;

				default:
					break;
				}

			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				stopMyDialog();
			}
		});

	}

	class activityAdapter extends BaseAdapter {
		private ArrayList<FqaActivityList> fqaActivityLists;
		private Context context;

		public activityAdapter(Context context, ArrayList<FqaActivityList> mList) {

			this.context = context;
			this.fqaActivityLists = mList;
		}

		@Override
		public int getCount() {
			return fqaActivityLists.size();
		}

		@Override
		public Object getItem(int position) {
			return fqaActivityLists.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.listview_main_fqa, null);
				holder.activity_fqa_title = (TextView) convertView.findViewById(R.id.activity_fqa_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.activity_fqa_title.setText(listinfor.get(position).title);
			return convertView;
		}
	}

	private static class ViewHolder {
		TextView activity_fqa_title;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if ("".equals(listinfor.get(position).url)) {
			return;
		}
		Intent intent = new Intent(FAQActivity.this, WebViewActivity.class);
		intent.putExtra("url", listinfor.get(position).url);
		intent.putExtra("title_name",getString(R.string.problem_details));
		startActivity(intent);

	}

}
