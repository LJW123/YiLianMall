package com.yilian.mall.entity;

/**
 * Created by Kent on 2014/12/15.
 */
public class NearDataFiliale extends NearDataBase{

    private FilialeList filialeList = null;


    public NearDataFiliale(String alpha,int item_type, FilialeList filialeList) {
        super(item_type);
        this.filialeList = filialeList;
    }
    

	public FilialeList getFilialeList() {
		return filialeList;
	}

	public void setFilialeList(FilialeList filialeList) {
		this.filialeList = filialeList;
	}
    

    public int getItemType(){
        return super.getItem_type();
    }

    public void setItemType(int itemType){
        super.setItem_type(itemType);
    }


}
