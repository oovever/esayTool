package com.Oovever.esayTool.io;

import com.Oovever.esayTool.util.ArrayUtil;
import com.Oovever.esayTool.util.CommonUtil;
import com.Oovever.esayTool.util.StringUtil;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 文件操作工具类
 * @author OovEver
 * 2018/6/8 15:25
 */
public class FileUtil {
    /** Windows路径分隔符 */
    private static final char    WINDOWS_SEPARATOR             = StringUtil.C_BACKSLASH;
    /** Windows下文件名中的无效字符 */
    private static       Pattern FILE_NAME_INVALID_PATTERN_WIN = Pattern.compile("[\\\\/:*?\"<>|]");
    /** Class文件扩展名 */
    public static final String CLASS_EXT = ".class";
    /** Jar文件扩展名 */
    public static final String JAR_FILE_EXT = ".jar";
    /** 在Jar中的路径jar的扩展名形式 */
    public static final String JAR_PATH_EXT = ".jar!";
    /** 当Path为文件形式时, path会加入一个表示文件的前缀 */
    public static final String PATH_FILE_PRE = "file:";


    /**
     * 文件是否为空<br>
     * 目录：里面没有文件时为空 文件：文件大小为0时为空
     *
     * @param file 文件
     * @return 是否为空，当提供非目录时，返回false
     */
    public static boolean isEmpty(File file) {
        if (null == file) {
            return true;
        }
        if (file.isDirectory()) {
            String[] subFiles = file.list();
            if (ArrayUtil.isEmpty(subFiles)) {
                return true;
            } else if (file.isFile()) {
                return file.length() <= 0;
            }
        }
        return false;
    }
    /**
     * 目录是否为空
     *
     * @param file 目录
     * @return 是否为空，当提供非目录时，返回false
     */
    public static boolean isNotEmpty(File file) {
        return false == isEmpty(file);
    }
    /**
     * 目录是否为空
     *
     * @param dirPath 目录
     * @return 是否为空
     * @exception IORuntimeException IOException
     */
    public static boolean isDirEmpty(Path dirPath) {
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(dirPath)) {
            return false == dirStream.iterator().hasNext();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
    /**
     * 目录是否为空
     *
     * @param dir 目录
     * @return 是否为空
     */
    public static boolean isDirEmpty(File dir) {
        return isDirEmpty(dir.toPath());
    }
    /**
     * 列出目录文件<br>
     * 给定的绝对路径不能是压缩包中的路径
     *
     * @param path 目录绝对路径或者相对路径
     * @return 文件列表（包含目录）
     */
    public static File[] ls(String path) {
        if (path == null) {
            return null;
        }
//        TODO 列出目录所有文件
        File file = file(path);
        if (file.isDirectory()) {
            return file.listFiles();
        } else {
            throw new IORuntimeException(path+" is not a directory");
        }
    }
    /**
     * 获取标准的绝对路径
     *
     * @param file 文件
     * @return 绝对路径
     */
    public static String getAbsolutePath(File file) {
        if (file == null) {
            return null;
        }

        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            return file.getAbsolutePath();
        }
    }
    /**
     * 创建File对象，自动识别相对或绝对路径，相对路径将自动从ClassPath下寻找
     *
     * @param path 文件路径
     * @return File
     */
    public static File file(String path) {
        if (StringUtil.isBlank(path)) {
            throw new NullPointerException("File path is blank!");
        }
        return new File(getAbsolutePath(path));
    }
    /**
     * 创建File对象
     *
     * @param parent 父目录
     * @param path 文件路径
     * @return File
     */
    public static File file(String parent, String path) {
        if (StringUtil.isBlank(path)) {
            throw new NullPointerException("File path is blank!");
        }
        return new File(parent, path);
    }
    /**
     * 创建File对象
     *
     * @param parent 父文件对象
     * @param path 文件路径
     * @return File
     */
    public static File file(File parent, String path) {
        if (StringUtil.isBlank(path)) {
            throw new NullPointerException("File path is blank!");
        }
        return new File(parent, path);
    }
    /**
     * 创建File对象
     *
     * @param uri 文件URI
     * @return File
     */
    public static File file(URI uri) {
        if (uri == null) {
            throw new NullPointerException("File uri is null!");
        }
        return new File(uri);
    }
    /**
     * 获取绝对路径，相对于ClassPath的目录<br>
     * 如果给定就是绝对路径，则返回原路径，原路径把所有\替换为/<br>
     *
     * @param path 相对路径
     * @return 绝对路径
     */
    public static String getAbsolutePath(String path) {
//        Todo 获取绝对路径
        return null;
    }
    /**
     * 递归遍历目录以及子目录中的所有文件<br>
     * 如果提供file为文件，直接返回过滤结果
     *
     * @param path 当前遍历文件或目录的路径
     * @param fileFilter 文件过滤规则对象，选择要保留的文件，只对文件有效，不过滤目录
     * @return 文件列表
     */
    public static List<File> loopFiles(String path, FileFilter fileFilter) {
        return loopFiles(file(path), fileFilter);
    }
    /**
     * 递归遍历目录以及子目录中的所有文件<br>
     * 如果提供file为文件，直接返回过滤结果
     *
     * @param file 当前遍历文件或目录
     * @param fileFilter 文件过滤规则对象，选择要保留的文件，只对文件有效，不过滤目录
     * @return 文件列表
     */
    public static List<File> loopFiles(File file, FileFilter fileFilter) {
        List<File> fileList = new ArrayList<File>();
        if (null == file) {
            return fileList;
        } else if (false == file.exists()) {
            return fileList;
        }

        if (file.isDirectory()) {
            for (File tmp : file.listFiles()) {
                fileList.addAll(loopFiles(tmp, fileFilter));
            }
        } else {
            if (null == fileFilter || fileFilter.accept(file)) {
                fileList.add(file);
            }
        }

        return fileList;
    }
    /**
     * 递归遍历目录以及子目录中的所有文件
     *
     * @param path 当前遍历文件或目录的路径
     * @return 文件列表
     */
    public static List<File> loopFiles(String path) {
        return loopFiles(file(path));
    }
    /**
     * 递归遍历目录以及子目录中的所有文件
     *
     * @param file 当前遍历文件
     * @return 文件列表
     */
    public static List<File> loopFiles(File file) {
        return loopFiles(file, null);
    }
    /**
     * 判断文件是否存在，如果path为null，则返回false
     *
     * @param path 文件路径
     * @return 如果存在返回true
     */
    public static boolean exist(String path) {
        return (path == null) ? false : file(path).exists();
    }
    /**
     * 判断文件是否存在，如果file为null，则返回false
     *
     * @param file 文件
     * @return 如果存在返回true
     */
    public static boolean exist(File file) {
        return (file == null) ? false : file.exists();
    }
    /**
     * 是否存在匹配文件
     *
     * @param directory 文件夹路径
     * @param regexp 文件夹中所包含文件名的正则表达式
     * @return 如果存在匹配文件返回true
     */
    public static boolean exist(String directory, String regexp) {
        File file = new File(directory);
        if (!file.exists()) {
            return false;
        }
//       列出目录下所有文件
        String[]fileList = file.list();
        if (fileList == null) {
            return false;
        }
        for (String fileName : fileList) {
            if (fileName.matches(regexp)) {
                return true;
            }
        }
        return false;
    }
    /**
     * 指定文件最后修改时间
     *
     * @param file 文件
     * @return 最后修改时间
     */
    public static Date lastModifiedTime(File file) {
        if (!exist(file)) {
            return null;
        }
        return new Date(file.lastModified());
    }
    /**
     * 指定路径文件最后修改时间
     *
     * @param path 绝对路径
     * @return 最后修改时间
     */
    public static Date lastModifiedTime(String path) {
        return lastModifiedTime(new File(path));
    }
    /**
     * 计算目录或文件的总大小<br>
     * 当给定对象为目录时，遍历目录下的所有文件和目录，递归计算其大小，求和返回
     *
     * @param file 目录或文件
     * @return 总大小，bytes长度
     */
    public static long size(File file) {
        CommonUtil.notNull(file, "file不能为空");
        if (false == file.exists()) {
            throw new IllegalArgumentException(file.getAbsolutePath()+"路径不存在文件");
        }
        if (file.isDirectory()) {
            long size = 0L;
            File[] subFiles = file.listFiles();
            if (ArrayUtil.isEmpty(subFiles)) {
                return 0L;// empty directory
            }
            for (int i = 0; i < subFiles.length; i++) {
                size += size(subFiles[i]);
            }
            return size;
        } else {
            return file.length();
        }

    }
    /**
     * 创建文件及其父目录，如果这个文件存在，直接返回这个文件<br>
     * 此方法不对File对象类型做判断，如果File不存在，无法判断其类型
     *
     * @param fullFilePath 文件的全路径
     * @return 文件，若路径为null，返回null
     * @throws IORuntimeException IO异常
     */
    public static File touch(String fullFilePath) throws IORuntimeException {
        if (fullFilePath == null) {
            return null;
        }
        return touch(file(fullFilePath));
    }
    /**
     * 创建文件及其父目录，如果这个文件存在，直接返回这个文件<br>
     * 此方法不对File对象类型做判断，如果File不存在，无法判断其类型
     *
     * @param file 文件对象
     * @return 文件，若路径为null，返回null
     * @throws IORuntimeException IO异常
     */
    public static File touch(File file) throws IORuntimeException {
        if (null == file) {
            return null;
        }
        if (false == file.exists()) {
            mkParentDirs(file);
            try {
                file.createNewFile();
            } catch (Exception e) {
                throw new IORuntimeException(e);
            }
        }
        return file;
    }
    /**
     * 创建文件及其父目录，如果这个文件存在，直接返回这个文件<br>
     * 此方法不对File对象类型做判断，如果File不存在，无法判断其类型
     *
     * @param parent 父文件对象
     * @param path 文件路径
     * @return File
     * @throws IORuntimeException IO异常
     */
    public static File touch(File parent, String path) throws IORuntimeException {
        return touch(file(parent, path));
    }
    /**
     * 创建文件及其父目录，如果这个文件存在，直接返回这个文件<br>
     * 此方法不对File对象类型做判断，如果File不存在，无法判断其类型
     *
     * @param parent 父文件对象
     * @param path 文件路径
     * @return File
     * @throws IORuntimeException IO异常
     */
    public static File touch(String parent, String path) throws IORuntimeException {
        return touch(file(parent, path));
    }
    /**
     * 创建所给文件或目录的父目录
     *
     * @param file 文件或目录
     * @return 父目录
     */
    public static File mkParentDirs(File file) {
        final File parentFile = file.getParentFile();
        if (null != parentFile && false == parentFile.exists()) {
            parentFile.mkdirs();
        }
        return parentFile;
    }
    /**
     * 创建父文件夹，如果存在直接返回此文件夹
     *
     * @param path 文件夹路径，使用POSIX格式，无论哪个平台
     * @return 创建的目录
     */
    public static File mkParentDirs(String path) {
        if (path == null) {
            return null;
        }
        return mkParentDirs(file(path));
    }
}
