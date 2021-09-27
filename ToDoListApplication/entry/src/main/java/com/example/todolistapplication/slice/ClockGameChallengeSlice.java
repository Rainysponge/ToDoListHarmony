package com.example.todolistapplication.slice;

import com.example.todolistapplication.Items.curUser;
import com.example.todolistapplication.ResourceTable;
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

import java.text.SimpleDateFormat;
import java.util.Date;

public class ClockGameChallengeSlice extends AbilitySlice implements Component.ClickedListener {
    ProgressBar ClockGameChallengeProgressBar;
    Button ClockGameChallengeStartButton;
    Button ClockGameChallengeQuitButton;

    int count = 0;
    long startTime = 0;
    int playNum = 0;
    private DataAbilityHelper dataAbilityHelper;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_clock_game_challenge);

        dataAbilityHelper = DataAbilityHelper.creator(this);

        ClockGameChallengeProgressBar = (ProgressBar)findComponentById(ResourceTable.Id_ClockGameChallengeProgressBar);
        ClockGameChallengeStartButton = (Button)findComponentById(ResourceTable.Id_ClockGameChallengeStartButton);
        ClockGameChallengeQuitButton = (Button)findComponentById(ResourceTable.Id_ClockGameChallengeQuitButton);
//        ClockGameChallengeTickTimer = (TickTimer)findComponentById(ResourceTable.Id_ClockGameChallengeTickTimer);

        ClockGameChallengeProgressBar.setProgressValue(0);
        ClockGameChallengeQuitButton.setClickedListener(component -> {
            Intent i = new Intent();

            Operation operation =  new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName("com.example.todolistapplication")
                    .withAbilityName("com.example.todolistapplication.ClockGameAbility").build();

            i.setOperation(operation);

            startAbility(i);
        });
        ClockGameChallengeStartButton.setClickedListener(this);
    }

    @Override
    public void onClick(Component component) {
        if(playNum == 1){
            count++;
            ClockGameChallengeProgressBar.setProgressValue(count * 2);
            if(count >= 50){
                playNum = 2; // disable to click
                ClockGameChallengeStartButton.setText("重来!");
                long endTime = new Date().getTime();

                if(curUser.userId != -1){
                    /**
                     * 非游客用户更新数据
                     */
                    String userName = curUser.userName;
                    Uri uriZen = Uri.parse("dataability:///com.example.todolistapplication.ClockGameChallengeDataAbility" +
                            "/clockGameChallenge");

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
                            valuesBucket.putFloat("score", (float) ((endTime-startTime)/1000.0));
                            valuesBucket.putString("recordTime", dateFormat.format(date));
                            valuesBucket.putString("userName", userName);
                            dataAbilityHelper.insert(uriZen, valuesBucket);
                        }else{
                            // update
                            resultSetZen.goToFirstRow();
                            float record = resultSetZen.getFloat(0);

                            if((float)(endTime-startTime)/1000.0 < record){
                                // update
                                valuesBucket.putFloat("score", (float) ((endTime-startTime)/1000.0));
                                valuesBucket.putString("recordTime", dateFormat.format(date));
                            }
                            dataAbilityHelper.update(uriZen, valuesBucket, dataAbilityPredicates);
                        }


                    } catch (DataAbilityRemoteException e) {
                        e.printStackTrace();
                    }
                }

                CommonDialog cd = new CommonDialog(this);
                cd.setCornerRadius(15);

                // load xml
                DirectionalLayout dl = (DirectionalLayout) LayoutScatter.getInstance(this).parse(ResourceTable.Layout_messagedialog, null, false);


                // 获取Dl中的东西
                Text text = (Text) dl.findComponentById(ResourceTable.Id_message);
                Button submit = (Button) dl.findComponentById(ResourceTable.Id_submit);
                Button cancel = (Button) dl.findComponentById(ResourceTable.Id_cancel);
                String msg = String.format("共计%.3fs!", (float)(endTime-startTime)/1000);

                text.setText(msg);

                submit.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        playNum = 0; // able to click
                        cd.destroy();
                    }
                });

                cancel.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        playNum = 0;
                        cd.destroy();
                    }
                });

                cd.setContentCustomComponent(dl);
                cd.show();
            }
        }else if(playNum == 0){
            ClockGameChallengeStartButton.setText("Click Here!");
            playNum = 1;
            ClockGameChallengeProgressBar.setProgressValue(0);
            startTime = new Date().getTime();
            count = 0;
        }
    }


}
