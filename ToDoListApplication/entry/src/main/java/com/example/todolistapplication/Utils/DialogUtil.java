package com.example.todolistapplication.Utils;

import com.example.todolistapplication.ResourceTable;
import ohos.agp.components.*;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;

public class DialogUtil {
    public static void show(Context context, String message){
        CommonDialog cd = new CommonDialog(context);
        cd.setCornerRadius(15);
        cd.setAutoClosable(true);
        // load xml
        DirectionalLayout dl = (DirectionalLayout) LayoutScatter.getInstance(context).parse(ResourceTable.Layout_messagedialog, null, false);


        // 获取Dl中的东西
        Text text = (Text) dl.findComponentById(ResourceTable.Id_message);
        Button submit = (Button) dl.findComponentById(ResourceTable.Id_submit);
        Button cancel = (Button) dl.findComponentById(ResourceTable.Id_cancel);
        text.setText(message);

        submit.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {

            }
        });

        cancel.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                cd.destroy();
            }
        });

        cd.setContentCustomComponent(dl);
        cd.show();
    }
}
