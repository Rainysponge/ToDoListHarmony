package com.example.todolistapplication.Utils;

import com.example.todolistapplication.Items.ToDoThingItem;
import com.example.todolistapplication.Items.curUser;
import com.example.todolistapplication.ResourceTable;
import com.example.todolistapplication.slice.ToDoMainAbilitySlice;
import com.example.todolistapplication.slice.ToDoThingDetailAbilitySlice;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import ohos.agp.components.element.Element;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.rdb.ValuesBucket;
import ohos.utils.net.Uri;

import java.text.ParseException;
import java.util.ArrayList;

public class ToDoThingItemProvider extends BaseItemProvider {
    private ArrayList<ToDoThingItem> list;
    private AbilitySlice abilitySlice;

    private DataAbilityHelper dataAbilityHelper;

    public ToDoThingItemProvider(ArrayList<ToDoThingItem> list, AbilitySlice abilitySlice) {
        this.list = list;
        this.abilitySlice = abilitySlice;
        this.dataAbilityHelper = DataAbilityHelper.creator(abilitySlice.getContext());
    }

    public ArrayList<ToDoThingItem> getList() {
        return list;
    }

    public void setList(ArrayList<ToDoThingItem> list) {
        this.list = list;
    }

    public AbilitySlice getAbilitySlice() {
        return abilitySlice;
    }

    public void setAbilitySlice(AbilitySlice abilitySlice) {
        this.abilitySlice = abilitySlice;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        if(list != null && i>=0&& i< list.size()){
            return list.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * 返回item中要加载的布局对象
     * @param i 当前要加载item的索引
     * @param component
     * @param componentContainer
     * @return
     */

    @Override
    public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
        DirectionalLayout directionalLayout = (DirectionalLayout) LayoutScatter.getInstance(abilitySlice).parse(ResourceTable.Layout_to_do_thing_item, null, false);
//
        // data come from the list
        ToDoThingItem item = list.get(i);
        Text tittle = (Text) directionalLayout.findComponentById(ResourceTable.Id_ToDoListItemTittle);
        tittle.setText(item.getThingTittle());
        Text endTime = (Text) directionalLayout.findComponentById(ResourceTable.Id_ToDoListItemEndTime);
        endTime.setText("结束时间: " + item.getEndTime());

        Button toDoThingDetailButton = (Button)directionalLayout.findComponentById(ResourceTable.Id_ToDoThingDetailButton);
        toDoThingDetailButton.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                System.out.println("toDoThingDetailButton is clicked! " + item.getToDoThingId());
//                present(new ToDoMainAbilitySlice(), new Intent());
                Intent i = new Intent();

                Operation operation =  new Intent.OperationBuilder()
                        .withDeviceId("")
                        .withBundleName("com.example.todolistapplication")
                        .withAbilityName("com.example.todolistapplication.ToDoThingDetailAbility").build();




                i.setOperation(operation);

                i.setParam("toDoThingId", item.getToDoThingId());
                i.setParam("thingTittle", item.getThingTittle());
                i.setParam("startTime", item.getStartTime());
                i.setParam("endTime", item.getEndTime());
                i.setParam("context", item.getContext());
                i.setParam("isActive", item.isActive());
                i.setParam("isComplete", item.isComplete());
                System.out.println(item.getStartTime() + "-----" + item.getEndTime());

                abilitySlice.startAbility(i);

            }
        });

//        StackLayout stackLayout = (StackLayout)directionalLayout.findComponentById(ResourceTable.Id_ToDoThingItemBackground);
        try {
            if(!item.isComplete() && TimeUtil.getRemainTime(item.getEndTime()) < 1000 * 60 * 60){
//                System.out.println(TimeUtil.getRemainTime(item.getEndTime()));
                Text ToDoThingWarning = (Text)directionalLayout.findComponentById(ResourceTable.Id_ToDoThingWarning);
                ToDoThingWarning.setVisibility(Component.VISIBLE);
            }



        } catch (ParseException e) {
            e.printStackTrace();
        }
//        ToDoThingWarning.setVisibility(true);


        Switch isComplete = (Switch) directionalLayout.findComponentById(ResourceTable.Id_ToDoListItemIsCompleteSwitch);
        isComplete.setChecked(!item.isComplete());

        isComplete.setCheckedStateChangedListener(new AbsButton.CheckedStateChangedListener() {
            @Override
            public void onCheckedChanged(AbsButton absButton, boolean b) {
                if(b){
//                    System.out.println("开启");


                    try {
                        if(TimeUtil.getRemainTime(item.getEndTime()) < 1000 * 60 * 60){
//                            System.out.println(TimeUtil.getRemainTime(item.getEndTime()));
                            Text ToDoThingWarning = (Text)directionalLayout.findComponentById(ResourceTable.Id_ToDoThingWarning);
                            ToDoThingWarning.setVisibility(Component.VISIBLE);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }else{
//                    System.out.println("关闭");
                    Text ToDoThingWarning = (Text)directionalLayout.findComponentById(ResourceTable.Id_ToDoThingWarning);
                    ToDoThingWarning.setVisibility(Component.HIDE);
                }
                Uri uri = Uri.parse("dataability:///com.example.todolistapplication.ToDoDataAbility/toDoList");

                DataAbilityPredicates dataAbilityPredicates = new DataAbilityPredicates()
                        .equalTo("toDoThingId", item.getToDoThingId());

                ValuesBucket valuesBucket = new ValuesBucket();
                valuesBucket.putInteger("userId", curUser.userId);
                valuesBucket.putString("startTime", item.getStartTime());
                valuesBucket.putBoolean("isActive", true);
                valuesBucket.putBoolean("isComplete", !b);


                // update data here
                valuesBucket.putString("toDoThingName", item.getThingTittle());
                valuesBucket.putString("endTime", item.getEndTime());
                valuesBucket.putString("context", item.getContext());

                try {
                    int res = dataAbilityHelper.update(uri, valuesBucket, dataAbilityPredicates);
                } catch (DataAbilityRemoteException e) {
                    e.printStackTrace();
                }

            }
        });


        return directionalLayout;
    }
}
