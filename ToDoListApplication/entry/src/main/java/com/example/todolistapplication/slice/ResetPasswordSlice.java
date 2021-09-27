package com.example.todolistapplication.slice;

import com.example.todolistapplication.Items.curUser;
import com.example.todolistapplication.ResourceTable;
import com.example.todolistapplication.Utils.SecurityUtil;
import com.example.todolistapplication.Utils.ToastUtils;
import com.example.todolistapplication.Utils.WarningUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.rdb.ValuesBucket;
import ohos.data.resultset.ResultSet;
import ohos.utils.net.Uri;

public class ResetPasswordSlice extends AbilitySlice implements Component.ClickedListener {
    Button ResetSubmitButton;
    Button ResetQuitButton;
    TextField ResetPasswordNewAgainTextField;
    TextField ResetPasswordNewTextField;
    TextField ResetPasswordOriginTextField;

    Text ResetPasswordOriginError;
    Text ResetPasswordNewError;
    Text ResetPasswordNewAgainError;

    private DataAbilityHelper dataAbilityHelper;
    int userId = curUser.userId;
//    String userName = curUser.userName;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_reset_password);

        dataAbilityHelper = DataAbilityHelper.creator(this);

        ResetQuitButton = (Button)findComponentById(ResourceTable.Id_ResetQuitButton);
        ResetSubmitButton = (Button)findComponentById(ResourceTable.Id_ResetSubmitButton);
        ResetPasswordNewAgainTextField = (TextField)findComponentById(ResourceTable.Id_ResetPasswordNewAgainTextField);
        ResetPasswordNewTextField = (TextField)findComponentById(ResourceTable.Id_ResetPasswordNewTextField);
        ResetPasswordOriginTextField = (TextField)findComponentById(ResourceTable.Id_ResetPasswordOriginTextField);
        ResetPasswordOriginError = (Text)findComponentById(ResourceTable.Id_ResetPasswordOriginError);
        ResetPasswordNewError = (Text)findComponentById(ResourceTable.Id_ResetPasswordNewError);
        ResetPasswordNewAgainError = (Text)findComponentById(ResourceTable.Id_ResetPasswordNewAgainError);

        // 初始化
        ResetPasswordNewAgainTextField.setText("");
        ResetPasswordNewTextField.setText("");
        ResetPasswordOriginTextField.setText("");

        ResetPasswordOriginError.setVisibility(Component.HIDE);
        ResetPasswordNewError.setVisibility(Component.HIDE);
        ResetPasswordNewAgainError.setVisibility(Component.HIDE);

        // 设置监听器
        ResetQuitButton.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Intent i = new Intent();
                i.setParam("curIndex", 2);
                Operation operation =  new Intent.OperationBuilder()
                        .withDeviceId("")
                        .withBundleName("com.example.todolistapplication")
                        .withAbilityName("com.example.todolistapplication.ToDoMainAbility").build();

                i.setOperation(operation);

                startAbility(i);
            }
        });
        ResetSubmitButton.setClickedListener(this);

    }

    @Override
    public void onClick(Component component) {
        String originPassword = ResetPasswordOriginTextField.getText();
        String newPassword = ResetPasswordNewTextField.getText();
        String newPasswordAgain = ResetPasswordNewAgainTextField.getText();

        if(!newPassword.equals(newPasswordAgain)){
            System.out.println(newPassword + " " + newPasswordAgain);
            ResetPasswordNewAgainError.setVisibility(Component.VISIBLE);
            WarningUtil.setWarning(this, ResetPasswordNewAgainTextField);
            ToastUtils.show(this, "重复密码错误", 2000);
            return;
        }

        Uri uri = Uri.parse("dataability:///com.example.todolistapplication.UsersDataAbility/users");
        DataAbilityPredicates dataAbilityPredicates = new DataAbilityPredicates()
                .equalTo("userId", userId);
        String[] columns = {"password"};
        try {
            ResultSet resultSet = dataAbilityHelper.query(uri, columns, dataAbilityPredicates);
            if(resultSet.getRowCount() == 0){
                ToastUtils.show(this, "系统错误", 2000);

            }else{
                resultSet.goToFirstRow();

                originPassword = SecurityUtil.getSHA256StrJava(originPassword);
                if(!originPassword.equals(resultSet.getString(0))){
                    ResetPasswordOriginError.setVisibility(Component.VISIBLE);
                    WarningUtil.setWarning(this, ResetPasswordOriginError);
                    ToastUtils.show(this, "原始密码错误", 2000);
                    return;
                }

                // update database

                ValuesBucket valuesBucket = new ValuesBucket();

//            System.out.println(SecurityUtil.getSHA256StrJava(password));
                newPassword = SecurityUtil.getSHA256StrJava(newPassword);
                valuesBucket.putString("password", newPassword);
                int result = dataAbilityHelper.update(uri, valuesBucket, dataAbilityPredicates);
                if(result == 1){
                    ToastUtils.show(this, "修改成功!", 2000);
                    Intent i = new Intent();
                    i.setParam("curIndex", 2);
                    Operation operation =  new Intent.OperationBuilder()
                            .withDeviceId("")
                            .withBundleName("com.example.todolistapplication")
                            .withAbilityName("com.example.todolistapplication.ToDoMainAbility").build();

                    i.setOperation(operation);

                    startAbility(i);
                }

            }
        } catch (DataAbilityRemoteException e) {
            e.printStackTrace();
        }

    }
}
