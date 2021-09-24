package com.example.todolistapplication;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.data.DatabaseHelper;
import ohos.data.dataability.DataAbilityUtils;
import ohos.data.rdb.*;
import ohos.data.resultset.ResultSet;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.net.Uri;
import ohos.utils.PacMap;

import java.io.FileDescriptor;

public class ToDoDataAbility extends Ability {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "Demo");

    RdbStore rdbStore;
    private StoreConfig config = StoreConfig.newDefaultConfig("ToDoList.db");
    private RdbOpenCallback callback = new RdbOpenCallback() {
        @Override
        public void onCreate(RdbStore rdbStore) {
            // toDoThingId integer primary key autoincrement
            // userId integer key
            // toDoThingName text not,
            // startTime text,  2021-09-23-12-04-23
            // endTime text,2021-09-24-12-04-23
            // context text not null
            // isActive
            // isComplete
            rdbStore.executeSql("create table if not exists toDoList(" +
                    "toDoThingId integer primary key autoincrement," +
                    "userId integer," +
                    "toDoThingName text not null, " +
                    "context text not null, " +
                    "startTime text not null," +
                    "endTime text not null, " +
                    "isActive boolean default true, " +
                    "isComplete boolean default false)");


        }

        @Override
        public void onUpgrade(RdbStore rdbStore, int i, int i1) {

        }
    };
    public void onStart(Intent intent) {
        super.onStart(intent);
        HiLog.info(LABEL_LOG, "ToDoDataAbility onStart");
        DatabaseHelper helper = new DatabaseHelper(this);
        rdbStore = helper.getRdbStore(config, 1, callback);
    }

    @Override
    public ResultSet query(Uri uri, String[] columns, DataAbilityPredicates predicates) {
        RdbPredicates rdbPredicates =  DataAbilityUtils.createRdbPredicates(predicates,
                "toDoList");
        ResultSet res = rdbStore.query(rdbPredicates, columns);

        return res;
    }

    @Override
    public int insert(Uri uri, ValuesBucket value) {
        HiLog.info(LABEL_LOG, "ToDoDataAbility insert");
        int i = -1;

//        ValuesBucket valuesBucket = new ValuesBucket();


        String path = uri.getLastPath();
        if("toDoList".equalsIgnoreCase(path)){
            i = (int)rdbStore.insert("toDoList", value);
        }
        return i;
    }

    @Override
    public int delete(Uri uri, DataAbilityPredicates predicates) {
        RdbPredicates rdbPredicates = DataAbilityUtils.createRdbPredicates(predicates,
                "toDoList");
        int res = rdbStore.delete(rdbPredicates);

        return res;
    }

    @Override
    public int update(Uri uri, ValuesBucket value, DataAbilityPredicates predicates) {
        RdbPredicates rdbPredicates = DataAbilityUtils.createRdbPredicates(predicates,
                "toDoList");
        return rdbStore.update(value, rdbPredicates);
    }

    @Override
    public FileDescriptor openFile(Uri uri, String mode) {
        return null;
    }

    @Override
    public String[] getFileTypes(Uri uri, String mimeTypeFilter) {
        return new String[0];
    }

    @Override
    public PacMap call(String method, String arg, PacMap extras) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}