package fxjzzyo.com.sspkudormselection.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import fxjzzyo.com.sspkudormselection.R;

/**
 * Created by fxjzzyo on 2017/12/9.
 */

public class DialogUtil {
    private Context context;
    private TextView textView;//选中后设置选中的楼号到textview
    private String building[] = new String[]{"5号楼", "13号楼", "14号楼", "8号楼", "9号楼"};
    private int select;//选择项
    private static DialogUtil mInstance;

    public DialogUtil(Context context) {
        this.context = context;
    }
    /**
     * 获取选中的项
     * @return
     */
    public int getSelect() {
        return select;
    }

    /**
     * 产生单选楼号的对话框
     * @param select
     * @param textView
     */
    public void showDialog(int select,TextView textView) {
        this.select = select;
        this.textView = textView;
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog_style);
        builder.setTitle("请选择楼号");
        //默认选中当前选中的楼号selectBuilding
        builder.setSingleChoiceItems(building, select, new DialogOnClick(context,textView));
        builder.setPositiveButton("确定", new DialogOnClick(context,textView));
        builder.setNegativeButton("取消", new DialogOnClick(context,textView));
        builder.create().show();
    }
    /**
     * 对话框点击监听
     */
    private  class DialogOnClick implements DialogInterface.OnClickListener {
        private Context context;
        private TextView tvTargetBuilding;//选中后设置选中的楼号到textview

        public DialogOnClick(Context context, TextView textView) {
            this.context = context;
            this.tvTargetBuilding = textView;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            // which表示单击的按钮索引，所有的选项索引都是大于0，按钮索引都是小于0的。
            Log.i("tag", "which: " + which);
            if (which >= 0) {
                //如果单击的是列表项，将当前列表项的索引保存在index中。
                //如果想单击列表项后关闭对话框，可在此处调用dialog.cancel()
                //或是用dialog.dismiss()方法。
                select = which;//当前选中的宿舍号
            } else {
                //用户单击的是【确定】按钮
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    tvTargetBuilding.setText(building[select]);//设置选择的楼号

                }
                //用户单击的是【取消】按钮
                else if (which == DialogInterface.BUTTON_NEGATIVE) {
                    Toast.makeText(context, "你没有选择任何东西",
                            Toast.LENGTH_LONG);
                }
            }
        }
    }
}
