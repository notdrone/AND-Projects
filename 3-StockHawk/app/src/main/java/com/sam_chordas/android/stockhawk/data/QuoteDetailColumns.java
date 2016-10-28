package com.sam_chordas.android.stockhawk.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by drone on 18/05/16.
 */
public class QuoteDetailColumns {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    public static final String _ID =
            "_id";
    @DataType(DataType.Type.INTEGER)
    public static final String SYMBOL = "symbol";
    @DataType(DataType.Type.REAL)
    public static final String BID = "bid";
    @DataType(DataType.Type.TEXT)
    public static final String DATE = "date";
}
