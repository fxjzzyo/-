package fxjzzyo.com.sspkudormselection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fxjzzyo.com.sspkudormselection.Constant.SelectPerson;
import fxjzzyo.com.sspkudormselection.R;

/**
 * Created by fxjzzyo on 2017/11/22.
 */

public class MyAdapter extends BaseAdapter {
private List<SelectPerson> datas;
    private Context context;
    private LayoutInflater layoutInflater;

    public MyAdapter(Context context, List<SelectPerson> datas) {
        this.context = context;
        this.datas = datas;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_item_layout, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        SelectPerson selectPerson = datas.get(i);
        viewHolder.tvStudid.setText(selectPerson.getStudId());
        viewHolder.tvCode.setText(selectPerson.getVcode());

        return view;
    }
    /**
     * viewHolder类，用来持有view，提升界面刷新效率
     */
    class ViewHolder {
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
        @BindView(R.id.tv_stuid)
        TextView tvStudid;
        @BindView(R.id.tv_vcode)
        TextView tvCode;

    }
}
