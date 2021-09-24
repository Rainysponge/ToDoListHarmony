package com.example.todolistapplication.Utils;

import com.example.todolistapplication.Items.TestItem;
import com.example.todolistapplication.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.*;

import java.util.ArrayList;

public class TestItemProvider extends BaseItemProvider {
    ArrayList<TestItem> list; // data
    private AbilitySlice abilitySlice;


    public TestItemProvider(ArrayList<TestItem> list, AbilitySlice abilitySlice) {
        this.list = list;
        this.abilitySlice = abilitySlice;
    }

    public TestItemProvider(ArrayList<TestItem> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
//        return null;
        if(list != null && i>=0&& i< list.size()){
            return list.get(i);
        }
        return null;
    }

    // 返回某一项id
    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
//        DirectionalLayout directionalLayout = (DirectionalLayout) LayoutScatter.getInstance(abilitySlice).parse(ResourceTable.Layout_to_do_thing_item, null, false);
//
//        // data come from the list
//        TestItem item = list.get(i);
//        Text text = (Text) directionalLayout.findComponentById(ResourceTable.Id_TestItem);
//        text.setText(item.getText());

        return null;


    }
}
