package com.biyu.o2o.util;

public class PathUtil {
    private static String separator = System.getProperty("file.separator");

    /**
     * @return 项目图片的根路径
     */
    public static String getImgBasePath() {
        String os = System.getProperty("os.name");
        String basePath = "";

        if (os.toLowerCase().startsWith("win")) {
            basePath = "C:/Users/BiYu/projects/o2oimages";
        } else {
            basePath = "/home/by/images/";
        }
        basePath = basePath.replace("/", separator);

        return basePath;
    }

    /**
     * @return 项目图片的子路径
     */
    public static String getShopImagePath(long shopId) {
        String imagePath = "/upload/item/shop/" + shopId + "/";
        return imagePath.replace("/", separator);
    }
}
