package com.example.todolistapplication;

import com.example.todolistapplication.slice.ToDoThingDetailAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class ToDoThingDetailAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(ToDoThingDetailAbilitySlice.class.getName());
    }
}
