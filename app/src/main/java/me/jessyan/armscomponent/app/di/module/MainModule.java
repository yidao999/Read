package me.jessyan.armscomponent.app.di.module;

import dagger.Binds;
import dagger.Module;
import me.jessyan.armscomponent.app.mvp.contract.MainContract;
import me.jessyan.armscomponent.app.mvp.contract.SplashContract;
import me.jessyan.armscomponent.app.mvp.model.MainModel;
import me.jessyan.armscomponent.app.mvp.model.SplashModel;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/26/2019 11:47
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
public abstract class MainModule {

    @Binds
    abstract MainContract.Model bindMainModel(MainModel model);
}