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
import ohos.agp.components.element.ShapeElement;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.rdb.ValuesBucket;
import ohos.data.resultset.ResultSet;
import ohos.utils.net.Uri;

public class ForgetPasswordAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    TextField ForgetPasswordUserNameTextField;
    TextField ForgetPasswordPasswordTextField;
    TextField ForgetPasswordPasswordAgainTextField;
    TextField ForgetPasswordUserPhoneTextField;

    Text ForgetPasswordUserNameError;
    Text ForgetPasswordUserPhoneError;
    Text ForgetPasswordPasswordError;
    Text ForgetPasswordPasswordAgainError;

    Button ForgetPasswordSubmitButton;

    private DataAbilityHelper dataAbilityHelper;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_login_forget_password);

        dataAbilityHelper = DataAbilityHelper.creator(this);

        ForgetPasswordUserNameTextField = (TextField)findComponentById(ResourceTable.Id_ForgetPasswordUserNameTextField);
        ForgetPasswordPasswordTextField = (TextField)findComponentById(ResourceTable.Id_ForgetPasswordPasswordTextField);
        ForgetPasswordPasswordAgainTextField = (TextField)findComponentById(ResourceTable.Id_ForgetPasswordPasswordAgainTextField);
        ForgetPasswordUserPhoneTextField = (TextField)findComponentById(ResourceTable.Id_ForgetPasswordUserPhoneTextField);

        ForgetPasswordUserNameError = (Text)findComponentById(ResourceTable.Id_ForgetPasswordUserNameError);
        ForgetPasswordUserPhoneError = (Text)findComponentById(ResourceTable.Id_ForgetPasswordUserPhoneError);
        ForgetPasswordPasswordError = (Text)findComponentById(ResourceTable.Id_ForgetPasswordPasswordError);
        ForgetPasswordPasswordAgainError = (Text)findComponentById(ResourceTable.Id_ForgetPasswordPasswordAgainError);

        ForgetPasswordSubmitButton = (Button)findComponentById(ResourceTable.Id_ForgetPasswordSubmitButton);

        // 清空内容
        ForgetPasswordUserNameTextField.setText("");
        ForgetPasswordPasswordTextField.setText("");
        ForgetPasswordPasswordAgainTextField.setText("");
        ForgetPasswordUserPhoneTextField.setText("");


        // 设为初始样式
        ForgetPasswordUserNameError.setVisibility(Component.HIDE);
        ForgetPasswordUserPhoneError.setVisibility(Component.HIDE);
        ForgetPasswordPasswordError.setVisibility(Component.HIDE);
        ForgetPasswordPasswordAgainError.setVisibility(Component.HIDE);
        ShapeElement normalElement = new ShapeElement(this, ResourceTable.Graphic_register_textfield);
        ForgetPasswordUserNameTextField.setBackground(normalElement);
        ForgetPasswordPasswordTextField.setBackground(normalElement);
        ForgetPasswordPasswordAgainTextField.setBackground(normalElement);
        ForgetPasswordUserPhoneTextField.setBackground(normalElement);


        ForgetPasswordSubmitButton.setClickedListener(this);



    }

    @Override
    public void onClick(Component component) {
        String userName = ForgetPasswordUserNameTextField.getText();
        String userPhone = ForgetPasswordUserPhoneTextField.getText();
        String password = ForgetPasswordPasswordTextField.getText();
        String passAgain = ForgetPasswordPasswordAgainTextField.getText();

        if(!password.equals(passAgain)){
            ForgetPasswordPasswordAgainError.setVisibility(Component.VISIBLE);
            ToastUtils.show(this, "密码重复有误!", 2000);
            WarningUtil.setWarning(this, ForgetPasswordPasswordAgainTextField);
            return;
        }

        String[] columns = {"userId"};
        Uri uri = Uri.parse("dataability:///com.example.todolistapplication.UsersDataAbility/users");
        DataAbilityPredicates dataAbilityPredicates = new DataAbilityPredicates()
                .equalTo("userName", userName).equalTo("userPhone", userPhone);
        try {
            ResultSet resultSet = dataAbilityHelper.query(uri, columns, dataAbilityPredicates);

            if(resultSet.getRowCount() == 0){
                ForgetPasswordUserNameError.setVisibility(Component.VISIBLE);
                ForgetPasswordUserPhoneError.setVisibility(Component.VISIBLE);
                WarningUtil.setWarning(this, ForgetPasswordUserNameTextField);
                WarningUtil.setWarning(this, ForgetPasswordUserPhoneTextField);
                ToastUtils.show(this, "用户名不存在或电话有误!", 2000);
            }else if(resultSet.getRowCount() == 1){
                // check password
                if(!SecurityUtil.checkPasswordLength(password)){
                    ForgetPasswordPasswordError.setVisibility(Component.VISIBLE);
                    WarningUtil.setWarning(this, ForgetPasswordPasswordTextField);
                    ToastUtils.show(this, "密码长度不足或过长!", 2000);
                    return;
                }

                if(!SecurityUtil.checkPasswordComplexity(password)){
                    ForgetPasswordPasswordError.setVisibility(Component.VISIBLE);
                    WarningUtil.setWarning(this, ForgetPasswordPasswordTextField);
                    ToastUtils.show(this, "密码过于简单!", 2000);
                    return;
                }

                resultSet.goToFirstRow();
                ValuesBucket valuesBucket = new ValuesBucket();
                valuesBucket.putString("userName", userName);
//            System.out.println(SecurityUtil.getSHA256StrJava(password));
                password = SecurityUtil.getSHA256StrJava(password);
                valuesBucket.putString("password", password);
//            valuesBucket.putString("passwordAgain", passwordAgain);
                valuesBucket.putString("userPhone", userPhone);

                int result = dataAbilityHelper.update(uri, valuesBucket, dataAbilityPredicates);

                ToastUtils.show(this, "密码修改成功,别忘了哦~!", 2000);
                curUser.userId = resultSet.getInt(0);

                curUser.userName = userName;
                Intent i = new Intent();

                Operation operation =  new Intent.OperationBuilder()
                        .withDeviceId("")
                        .withBundleName("com.example.todolistapplication")
                        .withAbilityName("com.example.todolistapplication.ToDoMainAbility").build();

                i.setOperation(operation);

                startAbility(i);


            }else{
                ToastUtils.show(this, "系统有误!", 2000);

            }

        } catch (DataAbilityRemoteException e) {
            e.printStackTrace();
        }

    }
}
