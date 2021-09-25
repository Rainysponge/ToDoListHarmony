package com.example.todolistapplication;

import com.example.todolistapplication.slice.ClockGameAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class ClockGameAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(ClockGameAbilitySlice.class.getName());
    }
}
