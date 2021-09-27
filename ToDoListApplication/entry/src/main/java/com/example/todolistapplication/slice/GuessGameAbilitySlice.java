package com.example.todolistapplication.slice;

import com.example.todolistapplication.Items.curUser;
import com.example.todolistapplication.ResourceTable;
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
import ohos.data.resultset.ResultSet;
import ohos.utils.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GuessGameAbilitySlice extends AbilitySlice
        implements Component.ClickedListener, Slider.ValueChangedListener {
    int playNum = 0; // 记录游戏进程
    Button GuessGamePlayButton;
    Button GuessGameQuitButton;
    Slider GuessGameSlider;
    Text GuessGameTargetText;

    int target = 0;
    int delta = 0;
    int stateNum = 0;

    private DataAbilityHelper dataAbilityHelper;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_guess_game);

        dataAbilityHelper = DataAbilityHelper.creator(this);

        GuessGamePlayButton = (Button)findComponentById(ResourceTable.Id_GuessGamePlayButton);
        GuessGameQuitButton = (Button)findComponentById(ResourceTable.Id_GuessGameQuitButton);
        GuessGameSlider = (Slider)findComponentById(ResourceTable.Id_GuessGameSlider);
        GuessGameTargetText = (Text)findComponentById(ResourceTable.Id_GuessGameTargetText);

        GuessGameSlider.setProgressValue(50);
        GuessGameSlider.setEnabled(false);
        GuessGameSlider.setValueChangedListener(this);
        GuessGamePlayButton.setClickedListener(this);
        GuessGameQuitButton.setClickedListener(component -> {
            Intent i = new Intent();
            i.setParam("curIndex", 1);
            Operation operation =  new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName("com.example.todolistapplication")
                    .withAbilityName("com.example.todolistapplication.ToDoMainAbility").build();
            i.setOperation(operation);
            startAbility(i);
        });
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
        if(playNum == 0){
            ToastUtils.show(this, "拖动!", 2000);
            GuessGamePlayButton.setText("确定");
            playNum++;
            target = (int)(Math.random()*100);
            GuessGameTargetText.setText(target + "%");
            GuessGameSlider.setProgressValue(0);  // init
            GuessGameSlider.setEnabled(true);
        }else if(1 <= playNum && playNum < 3){
            System.out.println( playNum+" "+stateNum + " " + target);
            playNum++;
            delta += Math.abs(stateNum - target) ;
            target = (int)(Math.random()*100);
            GuessGameTargetText.setText(target + "%");
            GuessGameSlider.setProgressValue(0);  // init
        }else{
            playNum++;
            delta += Math.abs(stateNum - target) ;

            if(curUser.userId != -1){
                /**
                 * 非游客用户更新数据
                 */
                String userName = curUser.userName;
                Uri uriZen = Uri.parse("dataability:///com.example.todolistapplication.GuessGameDataAbility" +
                        "/guessGame");

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
                        valuesBucket.putInteger("score", delta);
                        valuesBucket.putString("recordTime", dateFormat.format(date));
                        valuesBucket.putString("userName", userName);
                        dataAbilityHelper.insert(uriZen, valuesBucket);
                    }else{
                        // update
                        resultSetZen.goToFirstRow();
                        int record = resultSetZen.getInt(0);

                        if(delta < record){
                            // update
                            valuesBucket.putInteger("score", delta);
                            valuesBucket.putString("recordTime", dateFormat.format(date));
                        }
                        dataAbilityHelper.update(uriZen, valuesBucket, dataAbilityPredicates);
                    }


                } catch (DataAbilityRemoteException e) {
                    e.printStackTrace();
                }
            }

            String msg = String.format("共计偏离%d%%", delta);
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
                    GuessGamePlayButton.setClickable(true);
                    playNum = 0;
                    cd.destroy();
                }
            });

            cancel.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    GuessGamePlayButton.setClickable(true);
                    playNum = 0;
                    cd.destroy();
                }
            });
            cd.setContentCustomComponent(dl);
            cd.show();
            GuessGamePlayButton.setText("重来!");
            GuessGameSlider.setEnabled(false);
        }
    }

    @Override
    public void onProgressUpdated(Slider slider, int i, boolean b) {
        stateNum = i;
    }

    @Override
    public void onTouchStart(Slider slider) {

    }

    @Override
    public void onTouchEnd(Slider slider) {

    }
}
