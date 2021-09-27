package com.example.todolistapplication.slice;

import com.example.todolistapplication.Items.curUser;
import com.example.todolistapplication.ResourceTable;
import com.example.todolistapplication.Utils.TimeUtil;
import com.example.todolistapplication.Utils.ToastUtils;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import ohos.agp.window.dialog.CommonDialog;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.rdb.ValuesBucket;
import ohos.utils.net.Uri;

import java.text.ParseException;

public class ToDoThingDetailAbilitySlice extends AbilitySlice implements Component.ClickedListener {

     int toDoThingId;  // 主键
     String thingTittle;
     String startTime;
     String endTime;
     String context;
     boolean isActive;
     boolean isComplete;

    DatePicker DetailToDoThingDatePicker;
    TimePicker DetailToDoThingTimePicker;
    TextField DetailToDoThingTittleTextField;
    TextField DetailToDoThingContentTextField;

    Button DetailToDoThingDeleteButton;
    Button DetailToDoThingSaveButton;

    private DataAbilityHelper dataAbilityHelper;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_to_do_thing_detail);

        dataAbilityHelper = DataAbilityHelper.creator(this);

        DetailToDoThingDatePicker = (DatePicker)findComponentById(ResourceTable.Id_DetailToDoThingDatePicker);
        DetailToDoThingTimePicker = (TimePicker)findComponentById(ResourceTable.Id_DetailToDoThingTimePicker);
        DetailToDoThingTittleTextField =
                (TextField)findComponentById(ResourceTable.Id_DetailToDoThingTittleTextField);
        DetailToDoThingContentTextField =
                (TextField)findComponentById(ResourceTable.Id_DetailToDoThingContentTextField);

        DetailToDoThingDeleteButton = (Button)findComponentById(ResourceTable.Id_DetailToDoThingDeleteButton);
        DetailToDoThingSaveButton = (Button)findComponentById(ResourceTable.Id_DetailToDoThingSaveButton);

        toDoThingId = intent.getIntParam("toDoThingId", 0);
        thingTittle = intent.getStringParam("thingTittle");
        startTime = intent.getStringParam("startTime");
        endTime = intent.getStringParam("endTime");
        context = intent.getStringParam("context");
        isActive = intent.getBooleanParam("isActive", true);
        isComplete = intent.getBooleanParam("isComplete", false);

        int[] startTimeList = TimeUtil.parse(startTime);
        int[] endTimeList = TimeUtil.parse(endTime);


        DetailToDoThingDatePicker.updateDate(endTimeList[TimeUtil.Year],
                endTimeList[TimeUtil.Month],
                endTimeList[TimeUtil.Day]);

        DetailToDoThingTimePicker.setHour(endTimeList[TimeUtil.Hour]);
        DetailToDoThingTimePicker.setMinute(endTimeList[TimeUtil.Minute]);
        DetailToDoThingTimePicker.setSecond(endTimeList[TimeUtil.Second]);

        DetailToDoThingTittleTextField.setText(thingTittle);
        DetailToDoThingContentTextField.setText(context);

        DetailToDoThingDeleteButton.setClickedListener(this);

        DetailToDoThingSaveButton.setClickedListener(this);

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

        Uri uri = Uri.parse("dataability:///com.example.todolistapplication.ToDoDataAbility/toDoList");
        String[] columns = {"toDoThingId", "userId", "toDoThingName", "context",
                "startTime", "endTime", "isActive", "isComplete"};

        DataAbilityPredicates dataAbilityPredicates = new DataAbilityPredicates()
                .equalTo("toDoThingId", toDoThingId);


        if(component == DetailToDoThingDeleteButton){
            // delete
            int res = 0;
            CommonDialog cd = new CommonDialog(this);
            cd.setCornerRadius(15);
            cd.setAutoClosable(true);
            // load xml
            DirectionalLayout dl = (DirectionalLayout) LayoutScatter.getInstance(this).parse(ResourceTable.Layout_messagedialog, null, false);


            // 获取Dl中的东西
            Text text = (Text) dl.findComponentById(ResourceTable.Id_message);
            Button submit = (Button) dl.findComponentById(ResourceTable.Id_submit);
            Button cancel = (Button) dl.findComponentById(ResourceTable.Id_cancel);
            text.setText("确定要删除" + thingTittle + "?");

            submit.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    try {
                        int res = dataAbilityHelper.delete(uri, dataAbilityPredicates);
//                        if(res > 0){
//                            ToastUtils.show(ToDoThingDetailAbilitySlice.this, "成功删除" + thingTittle, 2000);
//                        }
                        ToastUtils.show(ToDoThingDetailAbilitySlice.this,
                                "成功删除" + thingTittle, 2000);
                        cd.destroy();
                        Intent i = new Intent();
                        i.setParam("curIndex", 0);
                        Operation operation =  new Intent.OperationBuilder()
                                .withDeviceId("")
                                .withBundleName("com.example.todolistapplication")
                                .withAbilityName("com.example.todolistapplication.ToDoMainAbility").build();

                        i.setOperation(operation);

                        startAbility(i);
                    } catch (DataAbilityRemoteException e) {
                        e.printStackTrace();
                    }

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

        }else if(component == DetailToDoThingSaveButton){
            // save
            String thingTittle;
            String endTime;
            String context;

            thingTittle = DetailToDoThingTittleTextField.getText();
            endTime = TimeUtil.getTimeFromPicker(DetailToDoThingDatePicker, DetailToDoThingTimePicker);
            context = DetailToDoThingContentTextField.getText();

            if(!thingTittle.equals(this.thingTittle) || !endTime.equals(this.endTime) || !context.equals(this.context)){
                ValuesBucket valuesBucket = new ValuesBucket();
                valuesBucket.putInteger("userId", curUser.userId);
                valuesBucket.putString("startTime", startTime);
                valuesBucket.putBoolean("isActive", true);
                valuesBucket.putBoolean("isComplete", false);

                // update data here
                valuesBucket.putString("toDoThingName", thingTittle);
                valuesBucket.putString("endTime", endTime);
                valuesBucket.putString("context", context);
                try {
                    int res = dataAbilityHelper.update(uri, valuesBucket, dataAbilityPredicates);
                    if(res > 0){
                        ToastUtils.show(ToDoThingDetailAbilitySlice.this, "保存成功", 2000);
                        Intent i = new Intent();
                        i.setParam("curIndex", 0);

                        present(new ToDoMainAbilitySlice(), i);
                    }else{
                        ToastUtils.show(ToDoThingDetailAbilitySlice.this, "发生了一些错误", 2000);
                    }
                } catch (DataAbilityRemoteException e) {
                    e.printStackTrace();
                }
            }



        }
    }
}
