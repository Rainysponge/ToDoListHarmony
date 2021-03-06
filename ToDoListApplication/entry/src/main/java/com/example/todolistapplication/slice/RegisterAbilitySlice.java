package com.example.todolistapplication.slice;

import com.example.todolistapplication.ResourceTable;
import com.example.todolistapplication.Utils.SecurityUtil;
import com.example.todolistapplication.Utils.ToastUtils;
import com.example.todolistapplication.Items.curUser;
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

import java.util.Arrays;

public class RegisterAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    TextField RegisterUserNameTextField;
    TextField RegisterPasswordTextField;
    TextField RegisterPasswordAgainTextField;
    TextField RegisterUserPhoneTextField;
    Text RegisterUserNameError;
    Text RegisterPasswordError;
    Text RegisterPasswordAgainError;
    Text RegisterUserPhoneError;
    Button RegisterSubmitButton;

    private DataAbilityHelper dataAbilityHelper;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_login_register);


        dataAbilityHelper = DataAbilityHelper.creator(this);

        RegisterUserNameTextField = (TextField)findComponentById(ResourceTable.Id_RegisterUserNameTextField);
        RegisterPasswordTextField = (TextField)findComponentById(ResourceTable.Id_RegisterPasswordTextField);
        RegisterPasswordAgainTextField = (TextField)findComponentById(ResourceTable.Id_RegisterPasswordAgainTextField);
        RegisterUserPhoneTextField = (TextField)findComponentById(ResourceTable.Id_RegisterUserPhoneTextField);
        RegisterUserNameError = (Text)findComponentById(ResourceTable.Id_RegisterUserNameError);
        RegisterPasswordAgainError = (Text)findComponentById(ResourceTable.Id_RegisterPasswordAgainError);
        RegisterPasswordError = (Text)findComponentById(ResourceTable.Id_RegisterPasswordError);
        RegisterUserPhoneError = (Text)findComponentById(ResourceTable.Id_RegisterUserPhoneError);

        // ????????????
        RegisterUserPhoneTextField.setText("");
        RegisterUserNameTextField.setText("");
        RegisterPasswordTextField.setText("");
        RegisterPasswordAgainTextField.setText("");


        RegisterSubmitButton = (Button)findComponentById(ResourceTable.Id_RegisterSubmitButton);

        // ??????????????????
        RegisterUserNameError.setVisibility(Component.HIDE);
        RegisterPasswordAgainError.setVisibility(Component.HIDE);
        RegisterPasswordError.setVisibility(Component.HIDE);
        RegisterUserPhoneError.setVisibility(Component.HIDE);
        ShapeElement normalElement = new ShapeElement(this, ResourceTable.Graphic_register_textfield);
        RegisterUserNameTextField.setBackground(normalElement);
        RegisterPasswordTextField.setBackground(normalElement);
        RegisterPasswordAgainTextField.setBackground(normalElement);
        RegisterUserPhoneTextField.setBackground(normalElement);


        RegisterSubmitButton.setClickedListener(this);



    }

    @Override
    public void onClick(Component component) {
        // ????????????
        if(component == RegisterSubmitButton){
            // ??????

//            Text text = (Text) findComponentById(ResourceTable.Id_error_tip_text);
//            text.setVisibility(Component.VISIBLE);
//
//            // ??????TextField????????????????????????
//            ShapeElement errorElement = new ShapeElement(this, ResourceTable.Graphic_background_text_field_error);
//
//            RegisterUserNameTextField.setBackground(errorElement);
//
//            // TextField????????????
//            RegisterUserNameTextField.clearFocus();

            String username = RegisterUserNameTextField.getText();
            String password = RegisterPasswordTextField.getText();
            String passwordAgain = RegisterPasswordAgainTextField.getText();
            String userPhone = RegisterUserPhoneTextField.getText();


            // ????????????
            RegisterUserNameError.setVisibility(Component.HIDE);
            RegisterPasswordAgainError.setVisibility(Component.HIDE);
            RegisterPasswordError.setVisibility(Component.HIDE);
            RegisterUserPhoneError.setVisibility(Component.HIDE);
            ShapeElement normalElement = new ShapeElement(this, ResourceTable.Graphic_register_textfield);
            RegisterUserNameTextField.setBackground(normalElement);
            RegisterPasswordTextField.setBackground(normalElement);
            RegisterPasswordAgainTextField.setBackground(normalElement);
            RegisterUserPhoneTextField.setBackground(normalElement);


            if(username.length()<4 || username.length() > 16){
                RegisterUserNameError.setVisibility(Component.VISIBLE);
                ToastUtils.show(this, "?????????????????????4?????????16", 2000);
                // ??????TextField????????????????????????
                ShapeElement errorElement = new ShapeElement(this, ResourceTable.Graphic_background_text_field_error);

                RegisterUserNameTextField.setBackground(errorElement);

                // TextField????????????
                RegisterUserNameTextField.clearFocus();
                return;
            }
            if(!SecurityUtil.checkPasswordLength(password)){
//                Text text = (Text) findComponentById(ResourceTable.Id_RegisterPasswordError);
                RegisterPasswordError.setVisibility(Component.VISIBLE);
                ToastUtils.show(this, "??????????????????6?????????16", 2000);
                // ??????TextField????????????????????????
                WarningUtil.setWarning(this, RegisterPasswordTextField);
//                ShapeElement errorElement = new ShapeElement(this, ResourceTable.Graphic_background_text_field_error);
//
//                RegisterPasswordTextField.setBackground(errorElement);
//
//                // TextField????????????
//                RegisterPasswordTextField.clearFocus();
                return;
            }else{
                if(!SecurityUtil.checkPasswordComplexity(password)){
//                    Text text = (Text) findComponentById(ResourceTable.Id_RegisterPasswordError);
                    RegisterPasswordError.setVisibility(Component.VISIBLE);
                    ToastUtils.show(this, "??????????????????", 2000);
                    WarningUtil.setWarning(this, RegisterPasswordTextField);
                    // ??????TextField????????????????????????
//                    ShapeElement errorElement = new ShapeElement(this, ResourceTable.Graphic_background_text_field_error);
//
//                    RegisterPasswordTextField.setBackground(errorElement);
//
//                    // TextField????????????
//                    RegisterPasswordTextField.clearFocus();
                    return;
                }
            }


            if(!password.equals(passwordAgain)){
//                Text text = (Text) findComponentById(ResourceTable.Id_RegisterPasswordAgainError);
                RegisterPasswordAgainError.setVisibility(Component.VISIBLE);
                ToastUtils.show(this, "??????????????????", 2000);
                // ??????TextField????????????????????????
                ShapeElement errorElement = new ShapeElement(this, ResourceTable.Graphic_background_text_field_error);

                RegisterPasswordAgainTextField.setBackground(errorElement);

                // TextField????????????
                RegisterPasswordAgainTextField.clearFocus();
                return;
            }
            // ????????????
            if(userPhone.length() != 11){
//                Text text = (Text) findComponentById(ResourceTable.Id_RegisterUserPhoneError);
                RegisterUserPhoneError.setVisibility(Component.VISIBLE);
                ToastUtils.show(this, "??????????????????", 2000);
                // ??????TextField????????????????????????
                ShapeElement errorElement = new ShapeElement(this, ResourceTable.Graphic_background_text_field_error);

                RegisterUserPhoneTextField.setBackground(errorElement);

                // TextField????????????
                RegisterUserPhoneTextField.clearFocus();
                return;
            }else{
                int i;
                for(i=0; i<11; i++){
                    if(!Character.isDigit(userPhone.charAt(i))){
                        break;
                    }
                }
                if(i < 11){
//                    Text text = (Text) findComponentById(ResourceTable.Id_RegisterUserPhoneError);
                    RegisterUserPhoneError.setVisibility(Component.VISIBLE);
                    ToastUtils.show(this, "??????????????????", 2000);
                    // ??????TextField????????????????????????
                    ShapeElement errorElement = new ShapeElement(this, ResourceTable.Graphic_background_text_field_error);

                    RegisterUserPhoneTextField.setBackground(errorElement);

                    // TextField????????????
                    RegisterUserPhoneTextField.clearFocus();
                    return;
                }
            }
            /**
             * ?????? userName, userPhone
             */
            String[] columns = {"userId"};
            Uri uri = Uri.parse("dataability:///com.example.todolistapplication.UsersDataAbility/users");

            DataAbilityPredicates dataAbilityPredicatesUserName = new DataAbilityPredicates()
                    .equalTo("userName", username);
            DataAbilityPredicates dataAbilityPredicatesUserPhone = new DataAbilityPredicates()
                    .equalTo("userPhone", userPhone);
            try {
                ResultSet resultSetUserName = dataAbilityHelper.query(uri, columns, dataAbilityPredicatesUserName);
                ResultSet resultSetUserPhone = dataAbilityHelper.query(uri, columns, dataAbilityPredicatesUserPhone);
                if(resultSetUserName.getRowCount() > 0){
                    RegisterUserNameError.setVisibility(Component.VISIBLE);
                    ToastUtils.show(this, "??????????????????!", 2000);
                    // ??????TextField????????????????????????
                    ShapeElement errorElement = new ShapeElement(this, ResourceTable.Graphic_background_text_field_error);

                    RegisterUserNameTextField.setBackground(errorElement);

                    // TextField????????????
                    RegisterUserNameTextField.clearFocus();
                    return;
                }
                if(resultSetUserPhone.getRowCount()>0){
                    RegisterUserPhoneError.setVisibility(Component.VISIBLE);
                    ToastUtils.show(this, "??????????????????!", 2000);
                    // ??????TextField????????????????????????
                    ShapeElement errorElement = new ShapeElement(this, ResourceTable.Graphic_background_text_field_error);

                    RegisterUserNameTextField.setBackground(errorElement);

                    // TextField????????????
                    RegisterUserNameTextField.clearFocus();
                    return;
                }


            } catch (DataAbilityRemoteException e) {
                e.printStackTrace();
            }

            // ???????????????  ???????????????

            ValuesBucket valuesBucket = new ValuesBucket();
            valuesBucket.putString("userName", username);
//            System.out.println(SecurityUtil.getSHA256StrJava(password));
            password = SecurityUtil.getSHA256StrJava(password);
            valuesBucket.putString("password", password);
//            valuesBucket.putString("passwordAgain", passwordAgain);
            valuesBucket.putString("userPhone", userPhone);

            try {
                int res = dataAbilityHelper.insert(
                        Uri.parse("dataability:///com.example.todolistapplication.UsersDataAbility/users")
                        , valuesBucket
                );
                //dataability://com.example.todolistapplication.UsersDataAbility
//                System.out.println(res);
                if(res == 1){
                    ToastUtils.show(this, username + "????????????!", 2000);

//                    Uri uri = Uri.parse("dataability:///com.example.todolistapplication.UsersDataAbility/users");
//                    String[] columns = {"userId"};

                    DataAbilityPredicates dataAbilityPredicates = new DataAbilityPredicates()
                            .equalTo("userName", username);
                    ResultSet resultSet = dataAbilityHelper.query(uri, columns, dataAbilityPredicates);
                    if(resultSet.getRowCount() == 0){
                        System.out.println("???????????????");
                    }else{
                        resultSet.goToFirstRow();
                        curUser.userId = resultSet.getInt(0);

                        curUser.userName = username;
                    }



                    Intent i = new Intent();

                    Operation operation =  new Intent.OperationBuilder()
                            .withDeviceId("")
                            .withBundleName("com.example.todolistapplication")
                            .withAbilityName("com.example.todolistapplication.ToDoMainAbility").build();

                    i.setOperation(operation);

                    startAbility(i);

                }else{
                    RegisterUserPhoneError.setVisibility(Component.VISIBLE);
                    RegisterUserNameError.setVisibility(Component.VISIBLE);
                    ToastUtils.show(this, "?????????????????????????????????", 2000);
                    // ??????TextField????????????????????????
                    ShapeElement errorElement = new ShapeElement(this, ResourceTable.Graphic_background_text_field_error);

                    RegisterUserPhoneTextField.setBackground(errorElement);
                    RegisterUserNameTextField.setBackground(errorElement);


                }

            } catch (DataAbilityRemoteException e) {
                e.printStackTrace();
            }

        }
    }
}
