package com.chryl.nsid.snowflake.test;

import com.chryl.nsid.snowflake.NS_Snowflake;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class NS_SnowflakeTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testNextIdAndParse() throws Exception {
        long beginTimeStamp = System.currentTimeMillis();
        NS_Snowflake NSSnowflake = new NS_Snowflake(3, 16);

        // gen id and parse it
        long id = NSSnowflake.nextId();
        long[] arr = NSSnowflake.parseId(id);
        System.out.println(NSSnowflake.formatId(id));

        Assert.assertTrue(arr[0] >= beginTimeStamp);
        Assert.assertEquals(3, arr[1]); // datacenterId
        Assert.assertEquals(16, arr[2]); // workerId
        Assert.assertEquals(0, arr[3]); // sequenceId

        // gen two ids in different MS
        long id2 = NSSnowflake.nextId();
        Assert.assertFalse(id == id2);
        System.out.println(NSSnowflake.formatId(id2));

        Thread.sleep(1); // wait one ms
        long id3 = NSSnowflake.nextId();
        long[] arr3 = NSSnowflake.parseId(id3);
        System.out.println(NSSnowflake.formatId(id3));
        Assert.assertTrue(arr3[0] > arr[0]);

        // gen two ids in the same MS
        long[] ids = new long[2];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = NSSnowflake.nextId();
        }
        System.out.println(NSSnowflake.formatId(ids[0]));
        System.out.println(NSSnowflake.formatId(ids[1]));
        Assert.assertFalse(ids[0] == ids[1]);
        long[] arr_ids0 = NSSnowflake.parseId(ids[0]);
        long[] arr_ids1 = NSSnowflake.parseId(ids[1]);
        Assert.assertEquals(arr_ids0[0], arr_ids1[0]);
        Assert.assertEquals(arr_ids0[3], arr_ids1[3] - 1);
    }

}
