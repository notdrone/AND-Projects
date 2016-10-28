package me.droan.netsu.del;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

/**
 * Created by Drone on 20/10/16.
 */

@SimpleSQLTable(table = "UserData", provider = "UserDataProvider")
public class UserData {
    @SimpleSQLColumn("_id")
    public String id;

    @SimpleSQLColumn("COL_name")
    public String name;

    @SimpleSQLColumn("col_emailId")
    public String emailId;
}
