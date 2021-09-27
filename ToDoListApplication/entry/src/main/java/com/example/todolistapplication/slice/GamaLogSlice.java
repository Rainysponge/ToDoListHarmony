package com.example.todolistapplication.slice;

import com.example.todolistapplication.ResourceTable;
import com.example.todolistapplication.Items.curUser;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.resultset.ResultSet;
import ohos.utils.net.Uri;

public class GamaLogSlice extends AbilitySlice implements Component.ClickedListener {
    Text GameLogText;
    Button GameLogClockGame;
    Button GameLogGuess;
    Button GameLogMyRecord;
    Button GameLogQuit;

    private DataAbilityHelper dataAbilityHelper;
    Uri uriChallenge;
    Uri uriZen;
    Uri uriGuess;
//                    Uri uri = Uri.parse("dataability:///com.example.todolistapplication.UsersDataAbility/users");

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_game_log);

        dataAbilityHelper = DataAbilityHelper.creator(this);

        // 组件初始化
        GameLogText = (Text)findComponentById(ResourceTable.Id_GameLogText);
        GameLogClockGame = (Button)findComponentById(ResourceTable.Id_GameLogClockGame);
        GameLogGuess = (Button)findComponentById(ResourceTable.Id_GameLogGuess);
        GameLogMyRecord = (Button)findComponentById(ResourceTable.Id_GameLogMyRecord);
        GameLogQuit = (Button)findComponentById(ResourceTable.Id_GameLogQuit);

        // Uri初始化
        GameLogText.setText("Thank you for your playing");

        uriChallenge = Uri.parse("dataability:///com.example.todolistapplication.ClockGameChallengeDataAbility/clockGameChallenge");
        uriZen = Uri.parse("dataability:///com.example.todolistapplication.ClockGameZenDataAbility/clockGameZen");
        uriGuess = Uri.parse("dataability:///com.example.todolistapplication.GuessGameDataAbility/guessGame");

        GameLogClockGame.setClickedListener(this);
        GameLogGuess.setClickedListener(this);
        GameLogMyRecord.setClickedListener(this);

        GameLogQuit.setClickedListener(component -> {
            Intent i = new Intent();
            i.setParam("curIndex", 2);
            Operation operation =  new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName("com.example.todolistapplication")
                    .withAbilityName("com.example.todolistapplication.ToDoMainAbility").build();

            i.setOperation(operation);

            startAbility(i);
        });

    }

    @Override
    public void onClick(Component component) {
        if(component == GameLogClockGame){
            StringBuilder sb = new StringBuilder();
            /**
             * 需要展示的信息
             * 模式名: 挑战
             * 最短时间用户: xxx
             * 时长: {%.2f}s 创造时间 yyyy-MM-dd hh:mm:ss
             * \n
             * 模式名: 禅
             * 最短时间用户: xxx
             * 得分: {%.2f}圈 创造时间 yyyy-MM-dd hh:mm:ss
             */
            String[] columns = {"userName", "score", "recordTime"};
            DataAbilityPredicates dataAbilityPredicatesChallenge = new DataAbilityPredicates()
                    .orderByAsc("score");  // 时间越短越好 故用升序

            DataAbilityPredicates dataAbilityPredicatesZen = new DataAbilityPredicates()
                    .orderByDesc("score");  // 圈数越多越号 故降序


            try {
                ResultSet resultSetChallenge = dataAbilityHelper.query(uriChallenge, columns, dataAbilityPredicatesChallenge);
                ResultSet resultSetZen = dataAbilityHelper.query(uriZen, columns, dataAbilityPredicatesZen);
                if(resultSetChallenge.getRowCount() == 0){
//                    msg += "挑战模式等待一位挑战者的到来\n\n";
                    sb.append("挑战模式等待一位挑战者的到来\n\n");
                }else{
                    resultSetChallenge.goToFirstRow();
                    String userName = resultSetChallenge.getString(0);
                    float score = resultSetChallenge.getFloat(1);
                    String recordTime = resultSetChallenge.getString(2);
                    //* 模式名: 挑战
                    //             * 最短时间用户: xxx
                    //             * 时长: {%.2f}s 创造时间 yyyy-MM-dd hh:mm:ss
                    String tmp = String.format("模式名: 挑战\n纪录保持者: %s\n时长: %.2fs 创造时间: %s\n",
                            userName, score, recordTime);
                    sb.append(tmp);

                }

                if(resultSetZen.getRowCount() == 0){
                    sb.append("禅模式等待一位挑战者的到来\n\n");
                }else{
                    resultSetZen.goToFirstRow();
                    String userName = resultSetZen.getString(0);
                    float score = resultSetZen.getFloat(1);
                    String recordTime = resultSetZen.getString(2);
                    //* 模式名: 挑战
                    //             * 最短时间用户: xxx
                    //             * 时长: {%.2f}s 创造时间 yyyy-MM-dd hh:mm:ss
                    String tmp = String.format("模式名: 禅\n纪录保持者: %s\n圈数: %.2f圈 创造时间: %s",
                            userName, score, recordTime);
                    sb.append(tmp);
                }

                GameLogText.setText(sb.toString());


            } catch (DataAbilityRemoteException e) {
                e.printStackTrace();
            }


        }else if(component == GameLogGuess){

            String[] columns = {"userName", "score", "recordTime"};
            DataAbilityPredicates dataAbilityPredicates = new DataAbilityPredicates()
                    .orderByAsc("score");  // 越小越好
            try {
                ResultSet resultSet = dataAbilityHelper.query(uriGuess, columns, dataAbilityPredicates);
                StringBuilder sb = new StringBuilder();
                if(resultSet.getRowCount() == 0){
                    sb.append("该游戏还没有最高纪录哦~");
                }else{
                    resultSet.goToFirstRow();
                    String userName = resultSet.getString(0);
                    int score = resultSet.getInt(1);
                    String recordTime = resultSet.getString(2);

                    String tmp = String.format("模式名: 普通\n纪录保持者: %s\n偏离: %d%% 创造时间: %s",
                            userName, score, recordTime);
                    sb.append(tmp);
                }

                GameLogText.setText(sb.toString());


            } catch (DataAbilityRemoteException e) {
                e.printStackTrace();
            }


        }else if(component == GameLogMyRecord){
            if(curUser.userId == -1){
                GameLogText.setText("游客用户不记录游戏成绩哦~");
            }else{
                String userName = curUser.userName;
                String[] columns = {"score", "recordTime", "lastPlayTime"};
                DataAbilityPredicates dataAbilityPredicates = new DataAbilityPredicates()
                        .equalTo("userName", userName);  // 查用户即可


                try {
                    ResultSet resultSetChallenge = dataAbilityHelper.query(uriChallenge, columns, dataAbilityPredicates);
                    ResultSet resultSetZen = dataAbilityHelper.query(uriZen, columns, dataAbilityPredicates);
                    ResultSet resultSetGuess = dataAbilityHelper.query(uriGuess, columns, dataAbilityPredicates);

                    StringBuilder sb = new StringBuilder();
                    if(resultSetChallenge.getRowCount() == 0){
                        sb.append("您还没体验过生死时速的挑战模式哦~\n\n");
                    }else{
                        resultSetChallenge.goToFirstRow();
                        //* 游戏名: 生死时速
                        //  模式: xxx 上次游玩时间: yyyy-MM-dd hh:mm:ss
                        //  最佳成绩: {%.2f}s, 创造时间 yyyy-MM-dd hh:mm:ss
                        String tmp = String.format("游戏名: 生死时速\n模式: 挑战模式  上次游玩时间: %s\n最佳成绩: {%.2f}s" +
                                ", 创造时间: %s\n", resultSetChallenge.getString(2), resultSetChallenge.getFloat(0),
                                resultSetChallenge.getString(1));
                        sb.append(tmp);

                    }

                    if(resultSetZen.getRowCount() == 0){
                        sb.append("您还没体验过生死时速的禅模式哦~\n\n");
                    }else{
                        resultSetZen.goToFirstRow();
                        String tmp = String.format("游戏名: 生死时速\n模式: 禅模式  上次游玩时间: %s\n最佳成绩: %.2f圈" +
                                        ", 创造时间: %s\n", resultSetZen.getString(2), resultSetZen.getFloat(0),
                                resultSetZen.getString(1));
                        sb.append(tmp);

                    }

                    if(resultSetGuess.getRowCount() == 0){
                        sb.append("您还没体验过拔河的普通模式哦~\n\n");
                    }else{
                        resultSetGuess.goToFirstRow();
                        String tmp = String.format("游戏名: 拔河\n模式: 普通模式  上次游玩时间: %s\n最佳成绩: %d%%" +
                                        ", 创造时间: %s\n", resultSetGuess.getString(2), resultSetGuess.getInt(0),
                                resultSetGuess.getString(1));
                        sb.append(tmp);

                    }
                    GameLogText.setText(sb.toString());


                } catch (DataAbilityRemoteException e) {
                    e.printStackTrace();
                }


            }
        }
    }





}
