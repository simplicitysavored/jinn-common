package xyz.yuanjin.project.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.yuanjin.project.common.enums.VideoEnum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemException;
import java.util.Objects;

/**
 * @author yuanjin
 * @version v1.0 on 2019/9/10 21:55
 */
public final class FileUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    public static boolean exists(String filePath) {
        File file = new File(filePath);
        return exists(file);
    }

    public static boolean notExists(String filePath) {
        return !exists(filePath);
    }

    public static boolean notExists(File file) {
        return !exists(file);
    }

    public static boolean exists(File file) {
        return file.exists();
    }

    public static boolean existsAll(String[] filePathArray) {
        for (String path : filePathArray) {
            if (notExists(path)) {
                return false;
            }
        }
        return true;
    }

    public static File mkdirs(String directoryPath) {
        File file = new File(directoryPath);
        if (!file.exists()) {
            if (file.mkdirs()) {
                return file;
            } else {
                file = null;
            }
        }
        return file;
    }

    /**
     * 如果不存在则创建文件
     *
     * @param filePath 文件绝对路径
     * @return 创建的文件
     */
    public static File createFileIfNotExists(String filePath) {
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                File parentFile = file.getParentFile();
                if (parentFile.mkdirs() || parentFile.exists()) {
                    if (file.createNewFile()) {
                        return file;
                    } else {
                        file = null;
                    }
                } else {
                    file = null;
                }
            }
            return file;
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public static File createFolderIfNotExists(String folderPath) {
        File file = new File(folderPath);
        if (!file.exists()) {
            if (file.mkdirs()) {
                return file;
            }
        }
        return null;
    }

    public static boolean isFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.isFile();
        }
        return Boolean.FALSE;
    }

    public static boolean isDirectory(String filePath) {
        File file = new File(filePath);
        return isDirectory(file);
    }

    public static boolean isDirectory(File file) {
        if (file.exists()) {
            return file.isDirectory();
        }
        return Boolean.FALSE;
    }

    public static boolean isFileOrEmptyDirectory(File file) {
        return null != file && file.exists() && (file.isFile() || (file.isDirectory() && (file.list() == null || Objects.requireNonNull(file.list()).length == 0)));
    }

    /**
     * 写入字符串
     *
     * @param content  内容
     * @param charset  编码
     * @param filePath 保存文件的绝对路径
     * @return 成功返回 File, 否则 null
     */
    public static File write(String filePath, final String charset, String content) {
        File file = createFileIfNotExists(filePath);
        FileOutputStream fos = null;
        try {
            if (file != null) {
                fos = new FileOutputStream(file);
                fos.write(content.getBytes(charset));
                fos.flush();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            file = null;
        } finally {
            close(fos);
        }

        return file;
    }

    /**
     * 写入字符串
     *
     * @param content  内容
     * @param filePath 保存文件的绝对路径
     * @return 成功返回 File, 否则 null
     */
    public static File write(String filePath, String content) {
        return write(filePath, StandardCharsets.UTF_8.name(), content);
    }

    public static String read(InputStream inputStream, final String charset) {
        String result = null;
        if (null != inputStream) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                byte[] bytes = new byte[1024];
                int length;
                while ((length = inputStream.read(bytes)) != -1) {
                    bos.write(bytes, 0, length);
                }
                result = bos.toString(charset);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            } finally {
                close(bos);
            }
        }
        return result;
    }

    public static File write(String filePath, byte[] bytes, int offset, int length, boolean append) {
        FileOutputStream fos = null;
        try {
            File file = new File(filePath);
            fos = new FileOutputStream(file, append);
            fos.write(bytes, offset, length);
            fos.flush();
            return file;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        } finally {
            close(fos);
        }
    }

    public static String read(InputStream inputStream) {
        return read(inputStream, StandardCharsets.UTF_8.name());
    }

    public static String read(File file, final String charset) {
        try {
            if (isFile(file.getAbsolutePath())) {
                FileInputStream fis = new FileInputStream(file);
                return read(fis, charset);
            }
            return null;
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public static String read(File file) {
        return read(file, StandardCharsets.UTF_8.name());
    }

    public static void close(InputStream... inputStreams) {
        if (inputStreams != null) {
            for (InputStream stream : inputStreams) {
                try {
                    if (stream != null) {
                        stream.close();
                    }
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }

    public static void close(OutputStream... outputStreams) {
        if (outputStreams != null) {
            for (OutputStream stream : outputStreams) {
                try {
                    if (stream != null) {
                        stream.close();
                    }
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }


    public static void copy(InputStream sourceInputStream, File destFile) throws IOException {
        if (destFile.exists()) {
            throw new FileAlreadyExistsException("file already exists | " + destFile.getAbsolutePath());
        }
        if (!destFile.getParentFile().exists()) {
            LOGGER.info("创建路径: {} - {}", destFile.getParentFile().mkdirs(), destFile.getParentFile().getAbsolutePath());
        }
        FileOutputStream fos = new FileOutputStream(destFile);
        int length;
        byte[] bytes = new byte[10240];
        while ((length = sourceInputStream.read(bytes)) != -1) {
            fos.write(bytes, 0, length);
            fos.flush();
        }
        fos.close();
    }

    public static boolean delete(String path) {
        return delete(new File(path));
    }

    public static boolean delete(File file) {
        boolean success = false;
        if (notExists(file)) {
            return false;
        }
        if (isFileOrEmptyDirectory(file)) {
            return file.delete();
        } else {
            File[] files = file.listFiles();
            if (null != files) {
                for (File tmp : files) {
                    success = delete(tmp);
                    if (!success) {
                        return false;
                    }
                }
            }
        }

        return delete(file);
    }

    public static boolean isVideo(File file) {
        for (VideoEnum video : VideoEnum.values()) {
            if (video.getPattern().matcher(file.getAbsolutePath()).matches()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 从 classpath 中获取获取文件内容
     *
     * @param name 文件名
     * @return 文件内容
     * @throws IOException 异常
     */
    public static String readFromClasspath(String name) throws IOException {
        InputStream is = null;
        try {
            is = FileUtil.class.getClassLoader().getResourceAsStream(name);

            if (null != is) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                try {

                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = is.read(bytes)) != -1) {
                        bos.write(bytes, 0, length);
                    }
                    return bos.toString();
                } finally {
                    close(bos);
                }
            }
        } finally {
            close(is);
        }
        return null;
    }

    @Deprecated
    public static void rename_old(File sourFile, File destFile) throws FileSystemException {
        boolean flag = sourFile.renameTo(destFile);
        if (!flag) {
            throw new FileSystemException(sourFile.getAbsolutePath() + " rename to " + destFile.getAbsolutePath() + " failed!");
        }
    }

    public static void rename(File sourFile, File destFile) throws IOException {
        if (destFile.exists()) {
            throw new FileAlreadyExistsException("file already exists: " + destFile.getAbsolutePath());
        }
        LOGGER.info("target file: {}", destFile.getAbsoluteFile());
        File parentFile = destFile.getParentFile();
        if (!parentFile.exists()) {
            boolean flag = parentFile.mkdirs();
            LOGGER.info("create folder {} | {}", (flag ? "success" : "failed"), parentFile.getAbsoluteFile());
            if (!flag) {
                throw new FileSystemException("create folder exception " + parentFile.getAbsolutePath());
            }
        }
        try (FileInputStream fis = new FileInputStream(sourFile)) {
            FileOutputStream fos = new FileOutputStream(destFile);
            int length;
            byte[] bytes = new byte[1024];
            while ((length = fis.read(bytes)) != -1) {
                fos.write(bytes, 0, length);
                fos.flush();
            }
            fos.flush();
            fos.close();
            LOGGER.info("write file success | {}", destFile.getAbsoluteFile());
        }

        boolean delete = sourFile.delete();
        LOGGER.info("delete file {}", delete ? "success" : "failed");

    }
}
