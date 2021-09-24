package com.example.todolistapplication.slice;

import com.example.todolistapplication.ResourceTable;
import com.example.todolistapplication.Items.curUser;
import com.example.todolistapplication.Utils.ToastUtils;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.data.rdb.ValuesBucket;
import ohos.utils.net.Uri;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ToToThingAddAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    DatePicker AddToDoThingDatePicker;
    TimePicker AddToDoThingTimePicker;
    TextField AddToDoThingTittleTextField;
    TextField AddToDoThingContentTextField;
    Button AddToDoThingAddButton;

    private DataAbilityHelper dataAbilityHelper;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_to_to_thing_add);

        dataAbilityHelper = DataAbilityHelper.creator(this);

        AddToDoThingDatePicker = (DatePicker)findComponentById(ResourceTable.Id_AddToDoThingDatePicker);
        AddToDoThingTimePicker = (TimePicker)findComponentById(ResourceTable.Id_AddToDoThingTimePicker);

        AddToDoThingTittleTextField = (TextField)findComponentById(ResourceTable.Id_AddToDoThingTittleTextField);
        AddToDoThingContentTextField = (TextField)findComponentById(ResourceTable.Id_AddToDoThingContentTextField);

        AddToDoThingAddButton = (Button) findComponentById(ResourceTable.Id_AddToDoThingAddButton);

        AddToDoThingAddButton.setClickedListener(this);
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
        if(component == AddToDoThingAddButton){
            int endYear = AddToDoThingDatePicker.getYear();
            int endMonth = AddToDoThingDatePicker.getMonth();
            int endDay = AddToDoThingDatePicker.getDayOfMonth();
            int endHour = AddToDoThingTimePicker.getHour();
            int endMinute = AddToDoThingTimePicker.getMinute();
            int endSecond = AddToDoThingTimePicker.getSecond();

            String toDoThingName = AddToDoThingTittleTextField.getText();
            String context = AddToDoThingContentTextField.getText();

            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
            String startTime = dateFormat.format(date);
            String endTime = endYear + "-" + endMonth + "-" + endDay + "-" + endHour + "-" + endMinute + "-" + endSecond;
            //endTimeDate.compareTo(date) > 0 endTime在startTime之后
            try {
                Date endTimeDate = dateFormat.parse(endTime); //
//                System.out.println(endTimeDate.compareTo(date));
                if(endTimeDate.compareTo(date) > 0){
                    ValuesBucket valuesBucket = new ValuesBucket();
                    valuesBucket.putInteger("userId", curUser.userId);
                    valuesBucket.putString("toDoThingName", toDoThingName);
                    valuesBucket.putString("startTime", startTime);
                    valuesBucket.putString("endTime", endTime);
                    valuesBucket.putString("context", context);
                    valuesBucket.putBoolean("isActive", true);
                    valuesBucket.putBoolean("isComplete", false);

                    int res = dataAbilityHelper.insert(
                            Uri.parse("dataability:///com.example.todolistapplication.ToDoDataAbility/toDoList")
                            , valuesBucket
                    );
                    
                    if(res < 0){
                        ToastUtils.show(this, "操作失败", 2000);
                    }else {
                        ToastUtils.show(this, "添加成功", 1500);
                        Intent i = new Intent();
                        i.setParam("curIndex", 1);
                        present(new ToDoMainAbilitySlice(), i);
                    }

                }else{
                    ToastUtils.show(this, "不要住在回忆里不出来哦~", 2000);
                }

            } catch (ParseException | DataAbilityRemoteException e) {
                e.printStackTrace();
            }


//            if(endYear < )


            // toDoThingId integer primary key autoincrement
            // userId integer key
            // toDoThingName text not,
            // startTime text,  2021-09-23-12-04-23
            // endTime text,2021-09-24-12-04-23
            // context text not null
            // isActive
            // isComplete


//            System.out.println("Start Time is " + dateFormat.format(date));
//            System.out.println("End Time is " + endYear + "-" + endMonth + "-" + endDay + endHour + "-" + endMinute + "-" + endSecond);

//            System.out.println();
//            System.out.println();

        }
    }
}
