package ru.xmn.myapplication;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;

/**
 * Created by USER on 16.02.2017.
 */

public class RecyclerViewMeasureHelper {

    RecyclerView recyclerView;

    public int getMeasuredHeightCustom(RecyclerView recyclerView, int widthSpec, int heightSpec) {

        this.recyclerView = recyclerView;

        int[] mMeasuredDimension = new int[2];
        int height = 0;

        Class c = recyclerView.getClass();
        java.lang.reflect.Field protectedField = null;
        RecyclerView.Recycler recycler = null;
        try {
            protectedField = getField(c, "mRecycler");
            protectedField.setAccessible(true);
            recycler = (RecyclerView.Recycler) protectedField.get(recyclerView);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < recyclerView.getLayoutManager().getItemCount(); i++) {
            measureScrapChild(recycler, i,
                    View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                    mMeasuredDimension);
            height = height + mMeasuredDimension[1];
        }
        return height;
    }

    private void measureScrapChild(RecyclerView.Recycler rec, int position, int widthSpec,
                                   int heightSpec, int[] measuredDimension) {
        View view = rec.getViewForPosition(position);
        if (view != null) {
            RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
            int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec,
                    recyclerView.getPaddingLeft() + recyclerView.getPaddingRight(), p.width);
            int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
                    recyclerView.getPaddingTop() + recyclerView.getPaddingBottom(), p.height);
            view.measure(childWidthSpec, childHeightSpec);
            measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
            measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
            rec.recycleView(view);
        }
    }

    private static Field getField(Class clazz, String fieldName)
            throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) {
                throw e;
            } else {
                return getField(superClass, fieldName);
            }
        }
    }
}
