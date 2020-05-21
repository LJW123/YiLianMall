package com.yilian.mall.widgets;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.widget.EditText;

public class PayKeyBoard extends KeyboardView{
	 public PayKeyBoard(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	private Context ctx;  
     private Activity act;  
     private KeyboardView keyboardView;  
     private Keyboard k1;// 字母键盘  

     private EditText ed;  


}
