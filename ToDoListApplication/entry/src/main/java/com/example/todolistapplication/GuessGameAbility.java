package com.example.todolistapplication;

import com.example.todolistapplication.slice.GuessGameAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class GuessGameAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(GuessGameAbilitySlice.class.getName());
    }
}
