package com.example.todolistapplication.slice;

import com.example.todolistapplication.ResourceTable;
import com.example.todolistapplication.Items.curUser;
import com.example.todolistapplication.Utils.DialogUtil;
import com.example.todolistapplication.Utils.SecurityUtil;
import com.example.todolistapplication.Utils.TimeUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import ohos.agp.window.dialog.CommonDialog;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.rdb.ValuesBucket;
import ohos.data.resultset.ResultSet;
import ohos.utils.net.Uri;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClockGameZenAbilitySlice extends AbilitySlice implements Component.ClickedListener
        , TickTimer.TickListener {
    RoundProgressBar clockGameZenRoundProgressBar;
    TickTimer clockGameZenTickTimer;
    Button clockGameZenStartButton;
    Button clockGameZenQuitButton;
    Text clockGameZenReMainTimeText;
    int playNum = 0;
    int count = 0;
    long startTime = 0;

    long timeLimit = 15000;

    private DataAbilityHelper dataAbilityHelper;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_clock_game_zen);

        dataAbilityHelper = DataAbilityHelper.creator(this);

        clockGameZenRoundProgressBar = (RoundProgressBar)findComponentById(ResourceTable.Id_ClockGameZenRoundProgressBar);
        clockGameZenTickTimer = (TickTimer)findComponentById(ResourceTable.Id_ClockGameZenTickTimer);
        clockGameZenStartButton = (Button)findComponentById(ResourceTable.Id_ClockGameZenStartButton);
        clockGameZenQuitButton = (Button)findComponentById(ResourceTable.Id_ClockGameZenQuitButton);
        clockGameZenReMainTimeText = (Text)findComponentById(ResourceTable.Id_ClockGameZenReMainTimeText);
        clockGameZenTickTimer.setCountDown(false);
        clockGameZenTickTimer.setFormat("yyyy-MM-dd-hh-mm:ss");



        clockGameZenTickTimer.setTickListener(this);
        clockGameZenQuitButton.setClickedListener(component -> {
            Intent i = new Intent();

            Operation operation =  new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName("com.example.todolistapplication")
                    .withAbilityName("com.example.todolistapplication.ClockGameAbility").build();

            i.setOperation(operation);

            startAbility(i);
        });
        clockGameZenStartButton.setClickedListener(this);
    }

    @Override
    public void onClick(Component component) {
        if(playNum == 1){
            count++;

            clockGameZenRoundProgressBar.setProgressValue((count*3)%100);

        }else if(playNum == 0){
            playNum = 1;

            clockGameZenStartButton.setText("Click Here!");
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm:ss");
            startTime = new Date().getTime();

            count = 0;
            clockGameZenRoundProgressBar.setProgressValue(0);
            clockGameZenReMainTimeText.setText(15 + "s");
            clockGameZenTickTimer.start();

        }
    }

    @Override
    public void onTickTimerUpdate(TickTimer tickTimer) {
        long now = new Date().getTime();

        long remainTime = now - startTime;
        if(remainTime >= timeLimit - 1500){
            playNum = 2;
            tickTimer.stop();
            clockGameZenStartButton.setClickable(false);
            clockGameZenReMainTimeText.setText(0 + "s");
            clockGameZenStartButton.setText("重来!");

            if(curUser.userId != -1){
                /**
                 * 非游客用户更新数据
                 */
                String userName = curUser.userName;
                Uri uriZen = Uri.parse("dataability:///com.example.todolistapplication.ClockGameZenDataAbility/clockGameZen");

                DataAbilityPredicates dataAbilityPredicates = new DataAbilityPredicates()
                        .equalTo("userName", userName);

                String[] columns = {"score", "recordTime", "lastPlayTime"};
                ValuesBucket valuesBucket = new ValuesBucket();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
                valuesBucket.putString("lastPlayTime", dateFormat.format(date));
                try {
                    ResultSet resultSetZen = dataAbilityHelper.query(uriZen, columns, dataAbilityPredicates);
                    if(resultSetZen.getRowCount()==0){
                        // insert
                        valuesBucket.putFloat("score", (float)(count*3.0 / 100.0));
                        valuesBucket.putString("recordTime", dateFormat.format(date));
                        valuesBucket.putString("userName", userName);
                        dataAbilityHelper.insert(uriZen, valuesBucket);
                    }else{
                        // update
                        resultSetZen.goToFirstRow();
                        float record = resultSetZen.getFloat(0);

                        if((float)(count*3.0 / 100.0) > record){
                            // update
                            valuesBucket.putFloat("score", (float)(count*3.0 / 100.0));
                            valuesBucket.putString("recordTime", dateFormat.format(date));
                        }
                        dataAbilityHelper.update(uriZen, valuesBucket, dataAbilityPredicates);
                    }


                } catch (DataAbilityRemoteException e) {
                    e.printStackTrace();
                }
            }



            String msg = String.format("完成了%.2f圈", (float)count*3.0 / 100.0);
            CommonDialog cd = new CommonDialog(this);
            cd.setCornerRadius(15);

            // load xml
            DirectionalLayout dl = (DirectionalLayout) LayoutScatter.getInstance(this).parse(ResourceTable.Layout_messagedialog, null, false);


            // 获取Dl中的东西
            Text text = (Text) dl.findComponentById(ResourceTable.Id_message);
            Button submit = (Button) dl.findComponentById(ResourceTable.Id_submit);
            Button cancel = (Button) dl.findComponentById(ResourceTable.Id_cancel);
            text.setText(msg);

            submit.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    clockGameZenStartButton.setClickable(true);
                    playNum = 0;
                    cd.destroy();
                }
            });

            cancel.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
//                    isPlaying = false;
                    clockGameZenStartButton.setClickable(true);
                    playNum = 0;
                    cd.destroy();
                }
            });

            cd.setContentCustomComponent(dl);
            cd.show();

        }else{
            int remainTimeSecond = (int)(timeLimit - remainTime) / 1000;
            clockGameZenReMainTimeText.setText(remainTimeSecond + "s");


        }

    }
}
