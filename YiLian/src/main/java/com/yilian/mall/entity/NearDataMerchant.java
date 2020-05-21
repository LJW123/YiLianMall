package com.yilian.mall.entity;

/**
 * Created by Kent on 2014/12/15.
 */
public class NearDataMerchant extends NearDataBase{

    private MerchantList merchantList = null;
    private String alpha;

    public NearDataMerchant(String alpha, int item_Type, MerchantList merchantList) {
        super(item_Type);
        this.merchantList = merchantList;
        this.alpha = alpha;
    }

	public MerchantList getMerchantList() {
		return merchantList;
	}

	public void setMerchantList(MerchantList merchantList) {
		this.merchantList = merchantList;
	}

    public int getItemType(){
        return super.getItem_type();
    }

    public void setItemType(int itemType){
        super.setItem_type(itemType);
    }

	public String getAlpha() {
		return alpha;
	}

	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}

}
