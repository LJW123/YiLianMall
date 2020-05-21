package com.yilian.mall.widgets;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mall.R;

public class VersionDialog extends Dialog {

	public VersionDialog(Context context) {  
		super(context);  
	}  

	public VersionDialog(Context context, int theme) {  
		super(context, theme);  
	}  
	

	public static class Builder {  
		private Context context;  
		private String title;  
		private CharSequence message;
		private String detailMessage;
		private String positiveButtonText;  
		private String negativeButtonText; 
		private int gravity = Gravity.CENTER;
		private int icon ;
		private boolean cancelable=true;

		private View contentView;  
		private DialogInterface.OnClickListener positiveButtonClickListener;  
		private DialogInterface.OnClickListener negativeButtonClickListener;  

		public Builder(Context context) {  
			this.context = context;  
		}  
		
		public void setCancelable(boolean cancelable) {
			this.cancelable=cancelable;
		}

		public Builder setMessage(String message) {  
			this.message = message;  
			return this;  
		} 

		public Builder setMessage(String message,int icon) {  
			this.icon = icon;
			this.message = message;  
			return this;  
		}  

		public Builder setMessage(CharSequence message,int icon,int gravity) {
			this.icon = icon;
			this.gravity = gravity;
			this.message = message;  
			return this;  
		} 

		/** 
		 * Set the Dialog message from resource
		 * @return
		 */  
		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);  
			return this;  
		}  

		public Builder setMessage(int message,int icon) {  
			this.icon = icon;
			this.message = (String) context.getText(message);  
			return this;  
		}  

		public Builder setMessage(int message,int icon,int gravity) {  
			this.icon = icon;
			this.gravity = gravity;
			this.message = (String) context.getText(message);  
			return this;  
		}  
		
		public Builder setDetailMessage(int detailMessage) {
			this.detailMessage = (String) context.getText(detailMessage);  
			return this;  
		} 
		
		
		public Builder setDetailMessage(String detailMessage) {  
			this.detailMessage = detailMessage;  
			return this;  
		} 
		

		/** 
		 * Set the Dialog title from resource 
		 *  
		 * @param title 
		 * @return 
		 */  
		public Builder setTitle(int title) {  
			this.title = (String) context.getText(title);
			return this;  
		}  

		/** 
		 * Set the Dialog title from String 
		 *  
		 * @param title 
		 * @return 
		 */  

		public Builder setTitle(String title) {  
			this.title = title;  
			return this;  
		}  

		public Builder setContentView(View v) {  
			this.contentView = v;  
			return this;  
		}  

		/** 
		 * Set the positive button resource and it's listener 
		 *  
		 * @param positiveButtonText 
		 * @return 
		 */  
		public Builder setPositiveButton(int positiveButtonText,  
				DialogInterface.OnClickListener listener) {  
			this.positiveButtonText = (String) context  
					.getText(positiveButtonText);  
			this.positiveButtonClickListener = listener;  
			return this;  
		}  

		public Builder setPositiveButton(String positiveButtonText,  
				DialogInterface.OnClickListener listener) {  
			this.positiveButtonText = positiveButtonText;  
			this.positiveButtonClickListener = listener;  
			return this;  
		}  

		public Builder setNegativeButton(int negativeButtonText,  
				DialogInterface.OnClickListener listener) {  
			this.negativeButtonText = (String) context  
					.getText(negativeButtonText);  
			this.negativeButtonClickListener = listener;  
			return this;  
		}  

		public Builder setNegativeButton(String negativeButtonText,  
				DialogInterface.OnClickListener listener) {  
			this.negativeButtonText = negativeButtonText;  
			this.negativeButtonClickListener = listener;  
			return this;  
		}  

		public VersionDialog create() {
			LayoutInflater inflater = (LayoutInflater) context  
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
			// instantiate the dialog with the custom Theme  
			final VersionDialog dialog = new VersionDialog(context,R.style.Dialog);  
			View layout = inflater.inflate(R.layout.dialog_new_version, null);  
			dialog.addContentView(layout, new LayoutParams(  
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));  
			// set the dialog title  

			if (title != null) {
				((TextView)layout.findViewById(R.id.title)).setText(title);  
			}else{
				((TextView)layout.findViewById(R.id.title)).setVisibility(View.GONE);
				layout.findViewById(R.id.top_line).setVisibility(View.GONE);
			}

			// set the confirm button  
			if (positiveButtonText != null) { 
				((Button) layout.findViewById(R.id.on))  
				.setText(positiveButtonText);  
				if (positiveButtonClickListener != null) {  
					((Button) layout.findViewById(R.id.on))  
					.setOnClickListener(new View.OnClickListener() {  
						public void onClick(View v) {  
							positiveButtonClickListener.onClick(dialog,  
									DialogInterface.BUTTON_POSITIVE);  
						}  
					});  
				}  
			} else {  
				// if no confirm button just set the visibility to GONE  
				layout.findViewById(R.id.on).setVisibility(  
						View.GONE);  
				layout.findViewById(R.id.bouttom_line).setVisibility(View.GONE);
			}  
			// set the cancel button  
			if (negativeButtonText != null) {  
				((Button) layout.findViewById(R.id.off))  
				.setText(negativeButtonText);  
				if (negativeButtonClickListener != null) {  
					((Button) layout.findViewById(R.id.off))  
					.setOnClickListener(new View.OnClickListener() {  
						public void onClick(View v) {  
							negativeButtonClickListener.onClick(dialog,  
									DialogInterface.BUTTON_NEGATIVE);  
						}  
					});  
				}  
			} else {  
				// if no confirm button just set the visibility to GONE  
				layout.findViewById(R.id.off).setVisibility(  
						View.GONE);  
				layout.findViewById(R.id.bouttom_line).setVisibility(View.GONE);
			}  
			// set the content message  
			if (message != null) {
				((TextView) layout.findViewById(R.id.message)).setText(message);  
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);  
				//此处相当于布局文件中的Android:layout_gravity属性  
				lp.gravity = gravity;
				((TextView) layout.findViewById(R.id.message)).setLayoutParams(lp);  
				
			} 

			if (icon != 0) {
				/// 这一步必须要做,否则不会显示.  
//				((ImageView) layout.findViewById(R.id.iv_icon)).setImageResource(icon);
//				((ImageView) layout.findViewById(R.id.iv_icon)).setVisibility(View.VISIBLE);
				Drawable drawable= context.getResources().getDrawable(icon);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());  
				((TextView) layout.findViewById(R.id.message)).setCompoundDrawables(drawable,null,null,null);
				
				
			}
			
			Log.e("================", "**********************"+icon);
			
			if (detailMessage != null) {
				((TextView) layout.findViewById(R.id.detail_message)).setText(detailMessage);
				((TextView) layout.findViewById(R.id.detail_message)).setVisibility(View.VISIBLE);
				
			}
			dialog.setContentView(layout);
			dialog.setCancelable(cancelable);
			dialog.setCanceledOnTouchOutside(cancelable);
			return dialog;  
		}  
	}  
}
