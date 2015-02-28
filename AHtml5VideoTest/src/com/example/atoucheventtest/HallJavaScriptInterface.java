package com.example.atoucheventtest; 

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/** 
 * @description : js回调类,点击图片 ,触发clickOnAndroid方法, 该方法不能混淆,需在混淆文件中声明
 * @package com.unitepower.mcd.noproguard
 * @title:JsScript.java
 * @author : email:xiangyanhui@unitepower.net
 * @date :2012-5-24 下午05:24:48 
 * @version : v1.0
 */
 
public class HallJavaScriptInterface { 
		private Handler hand;
		private int what;
		
		public HallJavaScriptInterface(Handler hand, int what) {
			this.hand = hand;
			this.what = what;
		} 
		
		public void clickOnAndroid( final String str) {   
			Message msg = new Message();
			Bundle bund = new Bundle();
			bund.putString("picurl", str);
			msg.setData(bund);
			msg.what = what;
			hand.sendMessage(msg); 
		}   
	}   
 
