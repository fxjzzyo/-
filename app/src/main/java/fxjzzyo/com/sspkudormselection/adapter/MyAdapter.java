package fxjzzyo.com.sspkudormselection.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by fxjzzyo on 2017/11/22.
 */

public class MyAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
    /**
     * viewHolder类，用来持有view，提升界面刷新效率
     */
    class ViewHolder {
        TextView tvCity;
        TextView tvCode;

    }
}
