package com.example.todolistapplication.slice;

import com.example.todolistapplication.Items.TestItem;
import com.example.todolistapplication.Items.ToDoThingItem;
import com.example.todolistapplication.ResourceTable;
import com.example.todolistapplication.Utils.DialogUtil;
import com.example.todolistapplication.Utils.TabPageSliderProvider;
import com.example.todolistapplication.Items.curUser;
import com.example.todolistapplication.Utils.TestItemProvider;
import com.example.todolistapplication.Utils.ToDoThingItemProvider;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import ohos.agp.window.dialog.CommonDialog;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.resultset.ResultSet;
import ohos.utils.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 主页
 */
public class ToDoMainAbilitySlice extends AbilitySlice implements TabList.TabSelectedListener{
    TabList MainTabList;
    PageSlider ToDoMainPageSlider;
    boolean isVisitor = false;

    private DataAbilityHelper dataAbilityHelper;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_to_do_main);

        dataAbilityHelper = DataAbilityHelper.creator(this);

        if(curUser.userId == -1){
            isVisitor = true;
            StringBuffer sb = new StringBuffer();
            sb.append("游客");
            Random ra = new Random();
            for(int i=0; i<4; i++){
                sb.append(ra.nextInt(10));
            }

            curUser.userName = sb.toString();
        }

        System.out.println(curUser.userId);

        MainTabList = (TabList)findComponentById(ResourceTable.Id_MainTabList);
        String[] tabTexts = {"首页", "记事本", "小游戏", "我的"};
        MainTabList.setFixedMode(true);
        for(int i=0; i<tabTexts.length; i++){
            TabList.Tab tab = MainTabList.new Tab(this);
            tab.setText(tabTexts[i]);
            MainTabList.addTab(tab);
        }
        // initial pageSlider
        List<Integer> layoutFileIds = new ArrayList<>();
        layoutFileIds.add(ResourceTable.Layout_ability_to_do_index);
        layoutFileIds.add(ResourceTable.Layout_ability_to_do_list);
        layoutFileIds.add(ResourceTable.Layout_ability_to_do_tools);
        layoutFileIds.add(ResourceTable.Layout_ability_to_do_center);

        ToDoMainPageSlider = (PageSlider)findComponentById(ResourceTable.Id_ToDoMainPageSlider);
        ToDoMainPageSlider.setProvider(new TabPageSliderProvider(layoutFileIds, this));

        ToDoMainPageSlider.addPageChangedListener(new PageSlider.PageChangedListener() {
            @Override
            public void onPageSliding(int i, float v, int i1) {

            }

            @Override
            public void onPageSlideStateChanged(int i) {

            }

            @Override
            public void onPageChosen(int i) {
                if (MainTabList.getSelectedTabIndex() != i) {
                    MainTabList.selectTabAt(i);
                }
            }
        });
        int curIndex = intent.getIntParam("curIndex", 0);
        MainTabList.addTabSelectedListener(this);
        MainTabList.selectTabAt(curIndex);





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
    public void onSelected(TabList.Tab tab) {
        int index = tab.getPosition(); // 获取点击菜单索引
        // 设置pageSlider和餐单索引一致
        ToDoMainPageSlider.setCurrentPage(index);
        if(index == 0){
            // 首页
        }else if(index == 1){

//                    StackLayout stackLayout = (StackLayout)LayoutScatter.getInstance().parse(ResourceTable.Layout_toast, null, false) ;
            DirectionalLayout directionalLayout = (DirectionalLayout) LayoutScatter.getInstance(this)
                    .parse(ResourceTable.Layout_to_do_thing_item, null, false);
            // 找到listContainer
            ListContainer listContainer = (ListContainer)findComponentById(ResourceTable.Id_ToDoThingListContainer);
//
//            // 创建数据集合
            ArrayList<ToDoThingItem> dataList = getData();


//
            ToDoThingItemProvider testItemProvider = new ToDoThingItemProvider(dataList, this);
            listContainer.setItemProvider(testItemProvider);





            // 记事本
            Button MainAddTodoThingButton = (Button)findComponentById(ResourceTable.Id_MainAddTodoThingButton);
            MainAddTodoThingButton.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {

                    Intent i = new Intent();

                    Operation operation =  new Intent.OperationBuilder()
                            .withDeviceId("")
                            .withBundleName("com.example.todolistapplication")
                            .withAbilityName("com.example.todolistapplication.ToToThingAddAbility").build();

                    i.setOperation(operation);

                    startAbility(i);

                }
            });


        }else if(index == 2){
            // games and tools

            Button ClockGameButton = (Button)findComponentById(ResourceTable.Id_ClockGameButton);
            Button GuessGameButton = (Button)findComponentById(ResourceTable.Id_GuessGameButton);

            ClockGameButton.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    Intent i = new Intent();

                    Operation operation =  new Intent.OperationBuilder()
                            .withDeviceId("")
                            .withBundleName("com.example.todolistapplication")
                            .withAbilityName("com.example.todolistapplication.ClockGameAbility").build();

                    i.setOperation(operation);

                    startAbility(i);
                }
            });


        }else{
            // user center

            Button userCenterLogOut = (Button)findComponentById(ResourceTable.Id_UserCenterLogOut);
            Text userName = (Text)findComponentById(ResourceTable.Id_UserCenterUserName);

            userName.setText(curUser.userName);

            userCenterLogOut.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
//                    DialogUtil.show(ToDoMainAbilitySlice.this, userName + ",确定要退出吗?");

//                    curUser.userName = "";
//                    curUser.userId = -1;
//                    Intent i = new Intent();
//
//                    Operation operation =  new Intent.OperationBuilder()
//                            .withDeviceId("")
//                            .withBundleName("com.example.todolistapplication")
//                            .withAbilityName("com.example.todolistapplication.MainAbility").build();
//
//                    i.setOperation(operation);
//
//                    startAbility(i);
                    CommonDialog cd = new CommonDialog(ToDoMainAbilitySlice.this);
                    cd.setCornerRadius(15);
                    cd.setAutoClosable(true);
                    // load xml
                    DirectionalLayout dl = (DirectionalLayout) LayoutScatter
                            .getInstance(ToDoMainAbilitySlice.this)
                            .parse(ResourceTable.Layout_messagedialog, null, false);


                    // 获取Dl中的东西
                    Text text = (Text) dl.findComponentById(ResourceTable.Id_message);
                    Button submit = (Button) dl.findComponentById(ResourceTable.Id_submit);
                    Button cancel = (Button) dl.findComponentById(ResourceTable.Id_cancel);
                    text.setText(curUser.userName + ",确认要退出吗?");

                    submit.setClickedListener(new Component.ClickedListener() {
                        @Override
                        public void onClick(Component component) {
                            curUser.userName = "";
                            curUser.userId = -1;
                            Intent i = new Intent();

                            Operation operation =  new Intent.OperationBuilder()
                                    .withDeviceId("")
                                    .withBundleName("com.example.todolistapplication")
                                    .withAbilityName("com.example.todolistapplication.MainAbility").build();

                            i.setOperation(operation);

                            startAbility(i);
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


                }
            });



        }

    }

    @Override
    public void onUnselected(TabList.Tab tab) {

    }

    @Override
    public void onReselected(TabList.Tab tab) {

    }

    public ArrayList<ToDoThingItem> getData() {
        ArrayList<ToDoThingItem> list = new ArrayList<>();

        Uri uri = Uri.parse("dataability:///com.example.todolistapplication.ToDoDataAbility/toDoList");
        String[] columns = {"toDoThingId", "userId", "toDoThingName", "context",
                "startTime", "endTime", "isActive", "isComplete"};

        DataAbilityPredicates dataAbilityPredicates = new DataAbilityPredicates()
                .equalTo("userId", curUser.userId).orderByAsc("endTime").orderByAsc("isComplete");
        try{
            ResultSet resultSet = dataAbilityHelper.query(uri, columns, dataAbilityPredicates);
            if(resultSet.getRowCount() > 0){
                System.out.println(resultSet.getRowCount());
                resultSet.goToFirstRow();
                do{

                    ToDoThingItem item = new ToDoThingItem(resultSet.getInt(0),
                            resultSet.getString(2),
                            resultSet.getString(4),
                            resultSet.getString(5),
                            resultSet.getString(3),
                            resultSet.getString(6).equals("1"),
                            resultSet.getString(7).equals("1")
                    );

                    list.add(item);
                }while(resultSet.goToNextRow());



            }

        }catch (DataAbilityRemoteException e){
            e.printStackTrace();
        }

//        for(int i=0; i<11; i++){
//            list.add(new ToDoThingItem(i, "Tittle " + i, "2021-9-24-05-09-08",
//                    "2021-9-25-05-09-08", "123", true, false));
//        }

        return list;
    }
}
