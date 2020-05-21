package com.yilian.luckypurchase.umeng;

import java.util.ArrayList;
import java.util.List;

public class UmengGloble {
	List<IconModel> models = new ArrayList<IconModel>();

	public List<IconModel> getAllIconModels() {
		IconModel model0 = new IconModel();
		model0.setType(0);
		model0.setTitle("微信好友");

		IconModel model1 = new IconModel();
		model1.setType(1);
		model1.setTitle("微信朋友圈");
		/*IconModel model2 = new IconModel();
		model2.setType(2);
		model2.setTitle("新浪微博");*/

		IconModel model4 = new IconModel();
		model4.setType(4);
		model4.setTitle("QQ好友");
		
		IconModel model3 = new IconModel();
		model3.setType(3);
		model3.setTitle("QQ空间");

		/*IconModel model5 = new IconModel();
		model5.setType(5);
		model5.setTitle("腾讯微博");*/

		// IconModel model6=new IconModel();
		// model6.setType(6);
		// model6.setTitle("人人网");
		//
		// IconModel model7=new IconModel();
		// model7.setType(7);
		// model7.setTitle("短信");

		models.add(model0);
		models.add(model1);
		//models.add(model2);
		// models.add(model7);
		models.add(model3);
		models.add(model4);
		//models.add(model5);
		// models.add(model6);
		return models;
	}
	public List<IconModel> getWeChatCircleIconModels() {

		IconModel model1 = new IconModel();
		model1.setType(1);
		model1.setTitle("微信朋友圈");
		models.add(model1);
		return models;
	}

	public List<IconModel> getWeChatCricleAndQQZoneIconModels() {

		IconModel model1 = new IconModel();
		model1.setType(1);
		model1.setTitle("微信朋友圈");
		IconModel model3 = new IconModel();
		model3.setType(3);
		model3.setTitle("QQ空间");
		models.add(model1);
		models.add(model3);
		return models;
	}

	// public List<IconModel> getSomeModels(){
	// IconModel model0=new IconModel();
	// model0.setType(0);
	// model0.setTitle("新浪微博");
	//
	// IconModel model1=new IconModel();
	// model1.setType(1);
	// model1.setTitle("腾讯微博");
	//
	// IconModel model2=new IconModel();
	// model2.setType(2);
	// model2.setTitle("QQ空间");
	//
	//
	// IconModel model5=new IconModel();
	// model5.setType(5);
	// model5.setTitle("朋友圈");
	//
	//
	// models.add(model0);
	// models.add(model1);
	// models.add(model2);
	// models.add(model5);
	// return models;
	// }
}
