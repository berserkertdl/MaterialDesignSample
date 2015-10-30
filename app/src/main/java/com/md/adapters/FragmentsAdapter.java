package com.md.adapters;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;

import java.util.List;

/**
 * Created by Administrator on 2015/10/29 0029.
 */
public class FragmentsAdapter extends FragmentPagerAdapter {

    public List<View> views;

    private List<Fragment> fragments;

    private List<String> titles;

    public FragmentsAdapter (FragmentManager fm,List<Fragment> fragments,List<String> titles) {

        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        if(fragments==null){
            return 0;
        }
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        if(fragments==null){
            return null;
        }
        return fragments.get(position);
    }

    /**

     * 此方法是给tablayout中的tab赋值的，就是显示名称

     * @param position

     * @return

     */
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position % titles.size());


        //显示 图片 + 文字
       /* Drawable dImage = context.getResources().getDrawable(tabImg[position]);
        dImage.setBounds(0, 0, dImage.getIntrinsicWidth(), dImage.getIntrinsicHeight());
        //这里前面加的空格就是为图片显示
        SpannableString sp = new SpannableString("  "+ list_Title.get(position));
        ImageSpan imageSpan = new ImageSpan(dImage, ImageSpan.ALIGN_BOTTOM);
        sp.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return  sp;*/

    }

}
