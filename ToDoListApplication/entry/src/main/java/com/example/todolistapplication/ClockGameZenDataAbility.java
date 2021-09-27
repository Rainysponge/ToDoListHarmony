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

public class ClockGameZenDataAbility extends Ability {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "Demo");

    RdbStore rdbStore;
    private StoreConfig config = StoreConfig.newDefaultConfig("ToDoList.db");

    private RdbOpenCallback callback = new RdbOpenCallback() {
        @Override
        public void onCreate(RdbStore rdbStore) {
            // userName foreignKey unique
            // score float  仅最高分
            // lastPlayTime text yyyy-MM-dd-hh-mm-ss
            // recordTime text yyyy-MM-dd-hh-mm-ss

            rdbStore.executeSql("create table if not exists clockGameZen(userName text unique," +
                    "score float not null, lastPlayTime text not null, recordTime text not null)");


        }

        @Override
        public void onUpgrade(RdbStore rdbStore, int i, int i1) {

        }
    };

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        HiLog.info(LABEL_LOG, "ClockGameZenDataAbility onStart");
        DatabaseHelper helper = new DatabaseHelper(this);
        rdbStore = helper.getRdbStore(config, 1, callback);
    }

    @Override
    public ResultSet query(Uri uri, String[] columns, DataAbilityPredicates predicates) {
        RdbPredicates rdbPredicates =  DataAbilityUtils.createRdbPredicates(predicates,
                "clockGameZen");
        ResultSet res =  rdbStore.query(rdbPredicates, columns);

        return res;
    }

    @Override
    public int insert(Uri uri, ValuesBucket value) {
        HiLog.info(LABEL_LOG, "ClockGameZenDataAbility insert");
        int i = -1;

//        ValuesBucket valuesBucket = new ValuesBucket();


        String path = uri.getLastPath();
        if("clockGameZen".equalsIgnoreCase(path)){
            i = (int)rdbStore.insert("clockGameZen", value);
        }
        return i;
    }

    @Override
    public int delete(Uri uri, DataAbilityPredicates predicates) {
        RdbPredicates rdbPredicates = DataAbilityUtils.createRdbPredicates(predicates,
                "clockGameZen");
        int res = rdbStore.delete(rdbPredicates);

        return res;
    }

    @Override
    public int update(Uri uri, ValuesBucket value, DataAbilityPredicates predicates) {
        RdbPredicates rdbPredicates = DataAbilityUtils.createRdbPredicates(predicates, "clockGameZen");
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