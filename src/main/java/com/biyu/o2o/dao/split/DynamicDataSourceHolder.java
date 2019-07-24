package com.biyu.o2o.dao.split;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicDataSourceHolder {
    private static Logger logger = LoggerFactory.getLogger(DynamicDataSourceHolder.class);
    private static ThreadLocal<String> contexHolder = new ThreadLocal<>();

    public static final String DB_MASTER = "master";
    public static final String DB_SLAVE = "slave";


    public static String getDbType() {
        String db = contexHolder.get();

        if (db == null) {
            db = DB_MASTER;
        }
        return db;
    }

    /**
     * 设置DB Type
     *
     * @param str
     */
    public static void setDbType(String str) {
        logger.debug("使用的数据源是:" + str);
        contexHolder.set(str);
    }

    /**
     * 清理连接类型
     */
    public static void clearDBType() {
        contexHolder.remove();
    }
}
