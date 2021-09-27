package com.example.todolistapplication.slice;

import com.example.todolistapplication.ResourceTable;
import com.example.todolistapplication.Utils.SecurityUtil;
import com.example.todolistapplication.Utils.ToastUtils;
import com.example.todolistapplication.Items.curUser;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.TextField;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.resultset.ResultSet;
import ohos.utils.net.Uri;

public class MainAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    TextField LoginUserNameTextField;
    TextField LoginPasswordTextField;
    Button ForgetPasswordButton;
    Button LoginButton;
    Button RegisterButton;
    Button TestButton;

    private DataAbilityHelper dataAbilityHelper;


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        dataAbilityHelper = DataAbilityHelper.creator(this);

        LoginUserNameTextField = (TextField)findComponentById(ResourceTable.Id_LoginUserNameTextField);
        LoginPasswordTextField = (TextField)findComponentById(ResourceTable.Id_LoginPasswordTextField);
        ForgetPasswordButton = (Button)findComponentById(ResourceTable.Id_ForgetPasswordButton);
        LoginButton = (Button)findComponentById(ResourceTable.Id_LoginButton);
        RegisterButton = (Button)findComponentById(ResourceTable.Id_RegisterButton);
        TestButton = (Button)findComponentById(ResourceTable.Id_TestButton);

        // 清空内容
        LoginUserNameTextField.setText("");
        LoginPasswordTextField.setText("");


        LoginButton.setClickedListener(this);
        ForgetPasswordButton.setClickedListener(component -> present(new ForgetPasswordAbilitySlice(), new Intent()));
        RegisterButton.setClickedListener(component -> present(new RegisterAbilitySlice(), new Intent()));
        TestButton.setClickedListener(component -> present(new ToDoMainAbilitySlice(), new Intent()));
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
        if(component == LoginButton){
            // 如果点击登录按钮
            String userName = LoginUserNameTextField.getText();
            String password = LoginPasswordTextField.getText();

            if(userName==null || password==null || userName.length() == 0 || password.length() == 0){
                ToastUtils.show(this, "输入不得为空!", 2000);
                return;
            }

            Uri uri = Uri.parse("dataability:///com.example.todolistapplication.UsersDataAbility/users");
            String[] columns = {"userId", "userName", "password"};

            DataAbilityPredicates dataAbilityPredicates = new DataAbilityPredicates()
                    .equalTo("userName", userName);

            try {
                ResultSet resultSet = dataAbilityHelper.query(uri, columns, dataAbilityPredicates);
//                ToastUtils.show(this, resultSet.getRowCount() + "", 2100);
                if(resultSet.getRowCount() < 1){
                    ToastUtils.show(this, "用户不存在", 2100);
                }else{
                    resultSet.goToFirstRow();

                    password = SecurityUtil.getSHA256StrJava(password);
                    if(password.equals(resultSet.getString(2))){
                        curUser.userId = resultSet.getInt(0);
                        curUser.userName = resultSet.getString(1);
                        ToastUtils.show(this, "欢迎" + userName + "!", 2000);
                        Intent i = new Intent();

                        Operation operation =  new Intent.OperationBuilder()
                                .withDeviceId("")
                                .withBundleName("com.example.todolistapplication")
                                .withAbilityName("com.example.todolistapplication.ToDoMainAbility").build();

                        i.setOperation(operation);

                        startAbility(i);
                    }else{
                        ToastUtils.show(this, "密码有误", 2000);
                    }
                }

            } catch (DataAbilityRemoteException e) {
                e.printStackTrace();
            }

        }

    }
}
