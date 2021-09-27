package com.example.todolistapplication.Utils;

import com.example.todolistapplication.ResourceTable;
import ohos.agp.components.Component;
import ohos.agp.components.element.ShapeElement;
import ohos.app.Context;

public class WarningUtil {

    public static void setWarning(Context context, Component component){
        ShapeElement errorElement = new ShapeElement(context, ResourceTable.Graphic_background_text_field_error);
        //
        component.setBackground(errorElement);

        // TextField失去焦点
        component.clearFocus();
    }

}
