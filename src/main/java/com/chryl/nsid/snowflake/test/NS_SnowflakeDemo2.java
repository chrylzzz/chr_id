package com.chryl.nsid.snowflake.test;

import com.chryl.nsid.snowflake.NS_Snowflake;

import java.util.ArrayList;
import java.util.List;


public class NS_SnowflakeDemo2 {

	/*
	 * 快速生成1千个ID，基本在1毫秒内就能
	 * 20180909090909111
	 * */
	public static void main(String[] args) {
		NS_Snowflake NSSnowflake = new NS_Snowflake(1, 1);
		final int idAmout = 1000;
		List<Long> idPool = new ArrayList<Long>(idAmout);
		for (int i = 0; i < idAmout; i++) {
			long id = NSSnowflake.nextId();
			idPool.add(id);
		}

		for (Long id : idPool) {
			/*System.out.println(String.format("%s => id: %d, hex: %s, bin: %s", NSSnowflake.formatId(id), id,
					BinHexUtil.hex(id), BinHexUtil.bin(id)));*/
			System.out.println(id);
		}

	}

}
