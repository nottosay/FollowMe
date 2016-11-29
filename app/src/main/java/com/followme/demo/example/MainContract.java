package com.followme.demo.example;

import com.followme.demo.base.BasePresenter;
import com.followme.demo.base.BaseView;

/**
 * Created by wally.yan on 2016/11/23.
 */

public interface MainContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

    }
}
