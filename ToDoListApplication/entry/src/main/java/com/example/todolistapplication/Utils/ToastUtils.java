package com.example.todolistapplication.Utils;

import com.example.todolistapplication.ResourceTable;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;

public class ToastUtils {
    public static void show(Context context, String message, int duration){
        DirectionalLayout directionalLayout = (DirectionalLayout) LayoutScatter.getInstance(context).parse(ResourceTable.Layout_toast, null, false) ;

        Text msg = (Text) directionalLayout.findComponentById(ResourceTable.Id_msg_toast);
        msg.setText(message);

        ToastDialog td = new ToastDialog(context);

        td.setSize(DirectionalLayout.LayoutConfig.MATCH_CONTENT, DirectionalLayout.LayoutConfig.MATCH_CONTENT);
        td.setDuration(duration);
        // 对齐方式
//        td.setAlignment(LayoutAlignment.BOTTOM);
        td.setOffset(0, 200);


        td.setContentCustomComponent(directionalLayout);
        td.show();



    }
}
