package me.jessyan.armscomponent.app.mvp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.armscomponent.app.mvp.model.entity.MainEntity;
import me.jessyan.armscomponent.app.mvp.ui.Fragment.MainFragment;

/**
 * author: 小川
 * Date: 2019/6/26
 * Description:
 */
public class VerticalPagerAdapter extends FragmentStatePagerAdapter {

    private List<MainEntity.DatasBean> dataList = new ArrayList<>();

    public VerticalPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return MainFragment.instance(dataList.get(i));
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    public void setDataList(List<MainEntity.DatasBean> data) {
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    public String getLastItemId(){
        if (dataList.size()==0){
            return "0";
        }
        MainEntity.DatasBean item = dataList.get(dataList.size()-1);
        return item.getId();
    }

    public String getLastItemCreateTime(){
        if (dataList.size()==0){
            return "0";
        }
        MainEntity.DatasBean item = dataList.get(dataList.size()-1);
        return item.getCreate_time();
    }
}
