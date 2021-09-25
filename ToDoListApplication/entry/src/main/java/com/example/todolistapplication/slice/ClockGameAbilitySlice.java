package com.example.todolistapplication.slice;

import com.example.todolistapplication.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class ClockGameAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    Button ClockGameZenModeButton;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_clock_game);

        ClockGameZenModeButton = (Button)findComponentById(ResourceTable.Id_ClockGameZenModeButton);

        ClockGameZenModeButton.setClickedListener(this);

    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    public void onClick(Component component) {
        if(component == ClockGameZenModeButton){
            Intent i = new Intent();  // 放记录数据

            present(new ClockGameZenAbilitySlice(), i);


        }
    }
}
