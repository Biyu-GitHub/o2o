package com.biyu.o2o.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ImageUtil {
    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss"); // 时间格式化的格式
    private static final Random r = new Random();
    private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    /**
     * 将CommonsMultipartFile转换成File
     * @param cFile
     * @return
     */
    public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile) {
        File newFile = new File(cFile.getOriginalFilename());
        try {
            cFile.transferTo(newFile);
        } catch (IOException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }

        return newFile;
    }

    /**
     * 处理缩略图，并返回新生成的图片的相对值路径
     *
     * @param thumbnail
     * @param targetAddr
     * @return
     */
    public static String generateThumbnail(File thumbnail, String targetAddr) {
        // 生成随机的新的名字，因为用户上传的图片可能重名
        String realFileName = getRandomFileName();
        // 获取文件的扩展名
        String extension = getFileExtension(thumbnail);
        // 创建文件保存路径
        makeDirPath(targetAddr);
        // 相对路径
        String relativeAddr = targetAddr + realFileName + extension;
        // 绝对路径
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);

        logger.debug("当前相对路径：" + relativeAddr);
        logger.debug("当前绝对路径：" + PathUtil.getImgBasePath() + relativeAddr);

        try {
            Thumbnails.of(thumbnail)
                    .size(200, 200)
                    .outputQuality(0.8f).toFile(dest);
        } catch (IOException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return relativeAddr;
    }

    /**
     * 创建目标路径涉及的目录
     *
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
        File dirPAth = new File(realFileParentPath);
        if (!dirPAth.exists()) {
            dirPAth.mkdirs();
        }
    }

    /**
     * 获取输入文件流的扩展名
     *
     * @param cFile
     * @return
     */
    private static String getFileExtension(File cFile) {
        String originFileName = cFile.getName();

        return originFileName.substring(originFileName.lastIndexOf("."));
    }

    /**
     * 生成随机文件名：当前年月日时分秒+五位随机数
     *
     * @return
     */
    private static String getRandomFileName() {
        int rannum = r.nextInt(89999) + 10000;
        String nowTimeStr = sDateFormat.format(new Date());

        return nowTimeStr + rannum;
    }


    public static void main(String[] args) throws IOException {

        System.out.println(basePath);
        Thumbnails.of(new File("C:\\Users\\BiYu\\Pictures\\frame0000.jpg"))
                .size(303, 303)
                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.png")), 0.25f)
                .outputQuality(0.8f)
                .toFile("C:\\Users\\BiYu\\Pictures\\frame000000.jpg");
    }
}
