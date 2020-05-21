package com.yilian.mall;

/**
 * @author Created by  on 2017/11/22 0022.
 */

public class BaseDialogActivity extends BaseAppCompatActivity {
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
