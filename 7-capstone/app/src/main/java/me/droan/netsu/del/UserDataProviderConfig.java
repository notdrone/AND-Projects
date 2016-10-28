package me.droan.netsu.del;

import ckm.simple.sql_provider.UpgradeScript;
import ckm.simple.sql_provider.annotation.ProviderConfig;
import ckm.simple.sql_provider.annotation.SimpleSQLConfig;

/**
 * Created by Drone on 20/10/16.
 */

@SimpleSQLConfig(
        name = "UserDataProvider",
        authority = "me.droan.netsu",
        database = "user.db",
        version = 1)
public class UserDataProviderConfig implements ProviderConfig {
    @Override
    public UpgradeScript[] getUpdateScripts() {
        return new UpgradeScript[0];
    }
}