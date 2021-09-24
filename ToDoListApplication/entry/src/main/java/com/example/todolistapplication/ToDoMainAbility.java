package com.example.todolistapplication;

import com.example.todolistapplication.slice.ToDoMainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class ToDoMainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(ToDoMainAbilitySlice.class.getName());
    }
}
