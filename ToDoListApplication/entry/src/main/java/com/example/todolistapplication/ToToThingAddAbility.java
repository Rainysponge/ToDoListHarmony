package com.example.todolistapplication;

import com.example.todolistapplication.slice.ToToThingAddAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class ToToThingAddAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(ToToThingAddAbilitySlice.class.getName());




    }
}
