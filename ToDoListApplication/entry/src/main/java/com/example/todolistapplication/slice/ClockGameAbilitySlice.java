package com.example.todolistapplication.slice;

import com.example.todolistapplication.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class ClockGameAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    Button ClockGameZenModeButton;
    Button ClockGameChallengeModeButton;
    Button ClockGameQuit;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_clock_game);

        ClockGameZenModeButton = (Button)findComponentById(ResourceTable.Id_ClockGameZenModeButton);
        ClockGameChallengeModeButton = (Button)findComponentById(ResourceTable.Id_ClockGameChallengeModeButton);
        ClockGameQuit = (Button)findComponentById(ResourceTable.Id_ClockGameQuit);
        ClockGameZenModeButton.setClickedListener(this);
        ClockGameChallengeModeButton.setClickedListener(this);
        ClockGameQuit.setClickedListener(this);

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
        else if(component == ClockGameChallengeModeButton){
            Intent i = new Intent();  // 放记录数据

            present(new ClockGameChallengeSlice(), i);
        }
        else if(component == ClockGameQuit){
            Intent i = new Intent();
            i.setParam("curIndex", 1);
            Operation operation =  new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName("com.example.todolistapplication")
                    .withAbilityName("com.example.todolistapplication.ToDoMainAbility").build();

            i.setOperation(operation);

            startAbility(i);

        }

    }
}
