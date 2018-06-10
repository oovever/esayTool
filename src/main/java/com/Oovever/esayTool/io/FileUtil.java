package com.Oovever.esayTool.io;

import com.Oovever.esayTool.io.file.FileReader;
import com.Oovever.esayTool.io.file.FileWriter;
import com.Oovever.esayTool.util.ArrayUtil;
import com.Oovever.esayTool.util.CharsetUtil;
import com.Oovever.esayTool.util.CommonUtil;
import com.Oovever.esayTool.util.StringUtil;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.*;
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
        return new File(path);
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
    /**
     * 删除文件或者文件夹<br>
     * 路径如果为相对路径，会转换为ClassPath路径！ 递归删除子文件夹
     * 某个文件删除失败会终止删除操作
     *
     * @param fullFileOrDirPath 文件或者目录的路径
     * @return 成功与否
     * @throws IORuntimeException IO异常
     */
    public static boolean del(String fullFileOrDirPath) throws IORuntimeException {
        return del(file(fullFileOrDirPath));
    }
    /**
     * 删除文件或者文件夹<br>
     * 注意：删除文件夹时不会判断文件夹是否为空，如果不空则递归删除子文件或文件夹<br>
     * 某个文件删除失败会终止删除操作
     *
     * @param file 文件对象
     * @return 成功与否
     * @throws IORuntimeException IO异常
     */
    public static boolean del(File file) throws IORuntimeException {
        if (file == null || false == file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            clean(file);
        }
        try {
            Files.delete(file.toPath());
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        return true;
    }
    /**
     * 清空文件夹<br>
     * 注意：清空文件夹时不会判断文件夹是否为空，如果不空则递归删除子文件或文件夹<br>
     * 某个文件删除失败会终止删除操作
     *
     * @param directory 文件夹
     * @return 成功与否
     * @throws IORuntimeException IO异常
     */
    public static boolean clean(File directory) throws IORuntimeException {
        if (directory == null || directory.exists() == false || false == directory.isDirectory()) {
            return true;
        }
//        列出所有文件（文件夹）绝对路径
        final File[] files = directory.listFiles();
        for (File childFile : files) {
            boolean isOk = del(childFile);
            if (isOk == false) {
                // 删除一个出错则本次删除任务失败
                return false;
            }
        }
        return true;
    }
    /**
     * 创建文件夹，如果存在直接返回此文件夹<br>
     * 此方法不对File对象类型做判断，如果File不存在，无法判断其类型
     *
     * @param dirPath 文件夹路径
     * @return 创建的目录
     */
    public static File mkdir(String dirPath) {
        if (dirPath == null) {
            return null;
        }
        final File dir = file(dirPath);
        return mkdir(dir);
    }

    /**
     * 创建文件夹，会递归自动创建其不存在的父文件夹，如果存在直接返回此文件夹<br>
     * 此方法不对File对象类型做判断，如果File不存在，无法判断其类型
     *
     * @param dir 目录
     * @return 创建的目录
     */
    public static File mkdir(File dir) {
        if (dir == null) {
            return null;
        }
        if (false == dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 检查两个文件是否是同一个文件<br>
     * 所谓文件相同，是指File对象是否指向同一个文件或文件夹
     *
     * @param file1 文件1
     * @param file2 文件2
     * @return 是否相同
     * @throws IORuntimeException IO异常
     * @see Files#isSameFile(Path, Path)
     */
    public static boolean equals(File file1, File file2) throws IORuntimeException {
        CommonUtil.notNull(file1,"文件1不能为空");
        CommonUtil.notNull(file2,"文件2不能为空");
        if (false == file1.exists() || false == file2.exists()) {
            // 两个文件都不存在判断其路径是否相同
            if (false == file1.exists() && false == file2.exists() && pathEquals(file1, file2)) {
                return true;
            }
            // 对于一个存在一个不存在的情况，一定不相同
            return false;
        }
        try {
            return Files.isSameFile(file1.toPath(), file2.toPath());
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
    /**
     * 文件路径是否相同<br>
     * 取两个文件的绝对路径比较，在Windows下忽略大小写，在Linux下不忽略。
     *
     * @param file1 文件1
     * @param file2 文件2
     * @return 文件路径是否相同
     */
    public static boolean pathEquals(File file1, File file2) {
// Windows环境
        try {
            if (StringUtil.equalsIgnoreCase(file1.getCanonicalPath(), file2.getCanonicalPath())) {
                return true;
            }
        } catch (Exception e) {
            if (StringUtil.equalsIgnoreCase(file1.getAbsolutePath(), file2.getAbsolutePath())) {
                return true;
            }
        }
        return false;
    }
    /**
     * 拷贝文件
     *
     * @param src 源文件路径
     * @param dest 目标文件或目录路径，如果为目录使用与源文件相同的文件名
     * @param options {@link StandardCopyOption}
     * @return File
     * @throws IORuntimeException IO异常
     */
    public static File copyFile(String src, String dest, StandardCopyOption... options) throws IORuntimeException {
        CommonUtil.notNull(src, "Source File path is blank !");
        CommonUtil.notNull(src, "Destination File path is null !");
        return copyFile(Paths.get(src), Paths.get(dest), options).toFile();
    }
    /**
     * 拷贝文件
     *
     * @param src 源文件
     * @param dest 目标文件或目录，如果为目录使用与源文件相同的文件名
     * @param options   复制一个文件同样非常简单，Files的copy方法就可以实现。在复制文件时，我们可以对复制操作加一个参数来指明具体如何进行复制。java.lang.enum.StandardCopyOption这个枚举可以用作参数传递给copy方法
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static File copyFile(File src, File dest, StandardCopyOption... options) throws IORuntimeException {
        // check
        CommonUtil.notNull(src, "Source File is null !");
        if (false == src.exists()) {
            throw new IORuntimeException("File not exist: " + src);
        }
        CommonUtil.notNull(dest, "Destination File or directiory is null !");
        if (equals(src, dest)) {
            throw new IORuntimeException("Files are equal");
        }
        return copyFile(src.toPath(), dest.toPath(), options).toFile();
    }
    /**
     * 拷贝文件
     *
     * @param src 源文件路径
     * @param dest 目标文件或目录，如果为目录使用与源文件相同的文件名
     * @param options {@link StandardCopyOption}
     * @return Path
     * @throws IORuntimeException IO异常
     */
    public static Path copyFile(Path src, Path dest, StandardCopyOption... options) throws IORuntimeException {
        CommonUtil.notNull(src, "Source File is null !");
        CommonUtil.notNull(dest, "Destination File or directiory is null !");
//        Path接口中resolve方法的作用相当于把当前路径当成父目录，而把参数中的路径当成子目录或是其中的文件，进行解析之后得到一个新路径；
        Path destPath = dest.toFile().isDirectory() ? dest.resolve(src.getFileName()) : dest;
        try {
            return Files.copy(src, destPath, options);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
    /**
     * 修改文件或目录的文件名，不变更路径，只是简单修改文件名<br>
     * 重命名有两种模式：<br>
     * 1、isRetainExt为true时，保留原扩展名：
     * 2、isRetainExt为false时，不保留原扩展名，需要在newName中指定
     * @param file 被修改的文件
     * @param newName 新的文件名，包括扩展名
     * @param isRetainExt 是否保留原文件的扩展名，如果保留，则newName不需要加扩展名
     * @param isOverride 是否覆盖目标文件
     * @return 目标文件
     */
    public static File rename(File file, String newName, boolean isRetainExt, boolean isOverride) {
        if (isRetainExt) {
            newName = newName.concat(".").concat(FileUtil.extName(file));
        }
        final Path path = file.toPath();
        final CopyOption[] options = isOverride ? new CopyOption[] { StandardCopyOption.REPLACE_EXISTING } : new CopyOption[] {};
        try {
            return Files.move(path, path.resolveSibling(newName), options).toFile();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
    /**
     * 给定路径已经是绝对路径<br>
     * 此方法并没有针对路径做标准化，建议先执行{@link #normalize(String)}方法标准化路径后判断
     *
     * @param path 需要检查的Path
     * @return 是否已经是绝对路径
     */
    public static boolean isAbsolutePath(String path) {
        if (StringUtil.isEmpty(path)) {
            return false;
        }

        if (StringUtil.C_SLASH == path.charAt(0) || path.matches("^[a-zA-Z]:/.*")) {
            // 给定的路径已经是绝对路径了
            return true;
        }
        return false;
    }

    /**
     * 获取文件扩展名，扩展名不带“.”
     *
     * @param file 文件
     * @return 扩展名
     */
    public static String extName(File file) {
        if (null == file) {
            return null;
        }
        if (file.isDirectory()) {
            return null;
        }
        return extName(file.getName());
    }
    /**
     * 获得文件的扩展名，扩展名不带“.”
     *
     * @param fileName 文件名
     * @return 扩展名
     */
    public static String extName(String fileName) {
        if (fileName == null) {
            return null;
        }
        int index = fileName.lastIndexOf(StringUtil.DOT);
        if (index == -1) {
            return StringUtil.EMPTY;
        } else {
            String ext = fileName.substring(index + 1);
            // 扩展名中不能包含路径相关的符号
            return ( ext.contains(String.valueOf(WINDOWS_SEPARATOR))) ? StringUtil.EMPTY : ext;
        }
    }
    /**
     * 修复路径<br>
     * 如果原路径尾部有分隔符，则保留为标准分隔符（/），否则不保留
     * <ol>
     * <li>1. 统一用 /</li>
     * <li>2. 多个 / 转换为一个 /</li>
     * <li>3. 去除两边空格</li>
     * <li>4. .. 和 . 转换为绝对路径，当..多于已有路径时，直接返回根路径</li>
     * </ol>
     *
     * @param path 原路径
     * @return 修复后的路径
     */
    public static String normalize(String path) {
        if (path == null) {
            return null;
        }

        //去除前缀，不区分大小写
        String pathToUse = StringUtil.removePrefixIgnoreCase(path, "classpath:");
        // 去除file:前缀
        pathToUse = StringUtil.removePrefixIgnoreCase(pathToUse, "file:");
        // 统一使用斜杠
        pathToUse = pathToUse.replaceAll("[/\\\\]{1,}", "/").trim();

        int prefixIndex = pathToUse.indexOf(StringUtil.COLON);
        String prefix = "";
        if (prefixIndex > -1) {
            // 可能Windows风格路径
            prefix = pathToUse.substring(0, prefixIndex + 1);
            if (prefix.startsWith("/")) {
                // 去除类似于/C:这类路径开头的斜杠
                prefix = prefix.substring(1);
            }
            if (false == prefix.contains("/")) {
                pathToUse = pathToUse.substring(prefixIndex + 1);
            }
        }
        if (pathToUse.startsWith(StringUtil.SLASH)) {
            prefix += StringUtil.SLASH;
            pathToUse = pathToUse.substring(1);
        }

        List<String> pathList = StringUtil.split(pathToUse, StringUtil.SLASH);
        List<String> pathElements = new LinkedList<String>();
        int tops = 0;

        String element;
        for (int i = pathList.size() - 1; i >= 0; i--) {
            element = pathList.get(i);
            if (StringUtil.DOT.equals(element)) {
                // 当前目录，丢弃
            } else if (StringUtil.DOUBLE_DOT.equals(element)) {
                tops++;
            } else {
                if (tops > 0) {
                    // 有上级目录标记时按照个数依次跳过
                    tops--;
                } else {
                    // Normal path element found.
                    pathElements.add(0, element);
                }
            }
        }

        return prefix + StringUtil.join(pathElements, StringUtil.SLASH);
    }
    /**
     * 判断是否为目录，如果path为null，则返回false
     *
     * @param path 文件路径
     * @return 如果为目录true
     */
    public static boolean isDirectory(String path) {
        return (path == null) ? false : file(path).isDirectory();
    }
    /**
     * 判断是否为目录，如果file为null，则返回false
     *
     * @param file 文件
     * @return 如果为目录true
     */
    public static boolean isDirectory(File file) {
        return (file == null) ? false : file.isDirectory();
    }
    /**
     * 判断是否为目录，如果file为null，则返回false
     *
     * @param path  路径
     * @param isFollowLinks 是否追踪到软链对应的真实地址
     * @return 如果为目录true
     */
    public static boolean isDirectory(Path path, boolean isFollowLinks) {
        if (null == path) {
            return false;
        }
        final LinkOption[] options = isFollowLinks ? new LinkOption[0] : new LinkOption[] { LinkOption.NOFOLLOW_LINKS };
        return Files.isDirectory(path, options);
    }
    /**
     * 判断是否为文件，如果path为null，则返回false
     *
     * @param path 文件路径
     * @return 如果为文件true
     */
    public static boolean isFile(String path) {
        return (path == null) ? false : file(path).isFile();
    }
    /**
     * 判断是否为文件，如果file为null，则返回false
     *
     * @param file 文件
     * @return 如果为文件true
     */
    public static boolean isFile(File file) {
        return (file == null) ? false : file.isFile();
    }
    /**
     * 判断是否为文件，如果file为null，则返回false
     *
     * @param path 文件
     * @param isFollowLinks 是否跟踪软链（快捷方式）
     * @return 如果为文件true
     */
    public static boolean isFile(Path path, boolean isFollowLinks) {
        if (null == path) {
            return false;
        }
        final LinkOption[] options = isFollowLinks ? new LinkOption[0] : new LinkOption[] { LinkOption.NOFOLLOW_LINKS };
        return Files.isRegularFile(path, options);
    }
    /**
     * 判断文件是否被改动<br>
     * 如果文件对象为 null 或者文件不存在，被视为改动
     *
     * @param file 文件对象
     * @param lastModifyTime 上次的改动时间
     * @return 是否被改动
     */
    public static boolean isModifed(File file, long lastModifyTime) {
        if (null == file || false == file.exists()) {
            return true;
        }
        return file.lastModified() != lastModifyTime;
    }
    /**
     * 返回主文件名
     *
     * @param file 文件
     * @return 主文件名
     */
    public static String mainName(File file) {
        if (file.isDirectory()) {
            return file.getName();
        }
        return mainName(file.getName());
    }
    /**
     * 返回主文件名
     *
     * @param fileName 完整文件名
     * @return 主文件名
     */
    public static String mainName(String fileName) {
        if (StringUtil.isBlank(fileName) || false == fileName.contains(StringUtil.DOT)) {
            return fileName;
        }
        return fileName.substring(fileName.lastIndexOf(StringUtil.DOT));
    }
    /**
     * 判断文件路径是否有指定后缀，忽略大小写<br>
     * 常用语判断扩展名
     *
     * @param file 文件或目录
     * @param suffix 后缀
     * @return 是否有指定后缀
     */
    public static boolean pathEndsWith(File file, String suffix) {
        return file.getPath().toLowerCase().endsWith(suffix);
    }
    /**
     * 获取文件属性
     *
     * @param path 文件路径
     * @param isFollowLinks 是否跟踪到软链对应的真实路径
     * @return  文件属性
     * @throws IORuntimeException IO异常
     * @since 3.1.0
     */
    public static BasicFileAttributes getAttributes(Path path, boolean isFollowLinks) throws IORuntimeException {
        if (null == path) {
            return null;
        }

        final LinkOption[] options = isFollowLinks ? new LinkOption[0] : new LinkOption[] { LinkOption.NOFOLLOW_LINKS };
        try {
            return Files.readAttributes(path, BasicFileAttributes.class, options);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
    /**
     * 获得输入流
     *
     * @param file 文件
     * @return 输入流
     * @throws IORuntimeException 文件未找到
     */
    public static BufferedInputStream getInputStream(File file) throws IORuntimeException {
        try {
            return new BufferedInputStream(new FileInputStream(file));
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
    /**
     * 获得输入流
     *
     * @param path 文件路径
     * @return 输入流
     * @throws IORuntimeException 文件未找到
     */
    public static BufferedInputStream getInputStream(String path) throws IORuntimeException {
        return getInputStream(file(path));
    }
    /**
     * 获得一个文件读取器
     *
     * @param file 文件
     * @return BufferedReader对象
     * @throws IORuntimeException IO异常
     */
    public static BufferedReader getUtf8Reader(File file) throws IORuntimeException {
        return getReader(file, CharsetUtil.CHARSET_UTF_8);
    }
    /**
     * 获得一个文件读取器
     *
     * @param path 文件路径
     * @return BufferedReader对象
     * @throws IORuntimeException IO异常
     */
    public static BufferedReader getUtf8Reader(String path) throws IORuntimeException {
        return getReader(path, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 获得一个文件读取器
     *
     * @param file 文件
     * @param charsetName 字符集
     * @return BufferedReader对象
     * @throws IORuntimeException IO异常
     */
    public static BufferedReader getReader(File file, String charsetName) throws IORuntimeException {
        return IoUtil.getReader(getInputStream(file), charsetName);
    }

    /**
     * 获得一个文件读取器
     *
     * @param file 文件
     * @param charset 字符集
     * @return BufferedReader对象
     * @throws IORuntimeException IO异常
     */
    public static BufferedReader getReader(File file, Charset charset) throws IORuntimeException {
        return IoUtil.getReader(getInputStream(file), charset);
    }
    /**
     * 获得一个文件读取器
     *
     * @param path 绝对路径
     * @param charsetName 字符集
     * @return BufferedReader对象
     * @throws IORuntimeException IO异常
     */
    public static BufferedReader getReader(String path, String charsetName) throws IORuntimeException {
        return getReader(file(path), charsetName);
    }
    /**
     * 获得一个文件读取器
     *
     * @param path 绝对路径
     * @param charset 字符集
     * @return BufferedReader对象
     * @throws IORuntimeException IO异常
     */
    public static BufferedReader getReader(String path, Charset charset) throws IORuntimeException {
        return getReader(file(path), charset);
    }
    /**
     * 读取文件所有数据<br>
     * 文件的长度不能超过Integer.MAX_VALUE
     *
     * @param file 文件
     * @return 字节码
     * @throws IORuntimeException IO异常
     */
    public static byte[] readBytes(File file) throws IORuntimeException {
        return FileReader.create(file).readBytes();
    }
    /**
     * 读取文件所有数据<br>
     * 文件的长度不能超过Integer.MAX_VALUE
     *
     * @param filePath 文件路径
     * @return 字节码
     * @throws IORuntimeException IO异常
     * @since 3.2.0
     */
    public static byte[] readBytes(String filePath) throws IORuntimeException {
        return readBytes(file(filePath));
    }
    /**
     * 读取文件内容
     *
     * @param file 文件
     * @return 内容
     * @throws IORuntimeException IO异常
     */
    public static String readUtf8String(File file) throws IORuntimeException {
        return readString(file, CharsetUtil.CHARSET_UTF_8);
    }
    /**
     * 读取文件内容
     *
     * @param path 文件路径
     * @return 内容
     * @throws IORuntimeException IO异常
     */
    public static String readUtf8String(String path) throws IORuntimeException {
        return readString(path, CharsetUtil.CHARSET_UTF_8);
    }
    /**
     * 读取文件内容
     *
     * @param file 文件
     * @param charsetName 字符集
     * @return 内容
     * @throws IORuntimeException IO异常
     */
    public static String readString(File file, String charsetName) throws IORuntimeException {
        return readString(file, CharsetUtil.charset(charsetName));
    }
    /**
     * 读取文件内容
     *
     * @param file 文件
     * @param charset 字符集
     * @return 内容
     * @throws IORuntimeException IO异常
     */
    public static String readString(File file, Charset charset) throws IORuntimeException {
        return FileReader.create(file, charset).readString();
    }
    /**
     * 读取文件内容
     *
     * @param path 文件路径
     * @param charsetName 字符集
     * @return 内容
     * @throws IORuntimeException IO异常
     */
    public static String readString(String path, String charsetName) throws IORuntimeException {
        return readString(file(path), charsetName);
    }
    /**
     * 读取文件内容
     *
     * @param path 文件路径
     * @param charset 字符集
     * @return 内容
     * @throws IORuntimeException IO异常
     */
    public static String readString(String path, Charset charset) throws IORuntimeException {
        return readString(file(path), charset);
    }

    /**
     * 读取文件内容
     *
     * @param url 文件URL
     * @param charset 字符集
     * @return 内容
     * @throws IORuntimeException IO异常
     */
    public static String readString(URL url, String charset) throws IORuntimeException {
        if (url == null) {
            throw new NullPointerException("Empty url provided!");
        }

        InputStream in = null;
        try {
            in = url.openStream();
            return IoUtil.read(in, charset);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            IoUtil.close(in);
        }
    }
    /**
     * 从文件中读取每一行的UTF-8编码数据
     *
     * @param <T> 集合类型
     * @param path 文件路径
     * @param collection 集合
     * @return 文件中的每行内容的集合
     * @throws IORuntimeException IO异常
     * @since 3.1.1
     */
    public static <T extends Collection<String>> T readUtf8Lines(String path, T collection) throws IORuntimeException {
        return readLines(path, CharsetUtil.CHARSET_UTF_8, collection);
    }
    /**
     * 从文件中读取每一行数据
     *
     * @param <T> 集合类型
     * @param path 文件路径
     * @param charset 字符集
     * @param collection 集合
     * @return 文件中的每行内容的集合
     * @throws IORuntimeException IO异常
     */
    public static <T extends Collection<String>> T readLines(String path, String charset, T collection) throws IORuntimeException {
        return readLines(file(path), charset, collection);
    }
    /**
     * 从文件中读取每一行数据
     *
     * @param <T> 集合类型
     * @param path 文件路径
     * @param charset 字符集
     * @param collection 集合
     * @return 文件中的每行内容的集合
     * @throws IORuntimeException IO异常
     */
    public static <T extends Collection<String>> T readLines(String path, Charset charset, T collection) throws IORuntimeException {
        return readLines(file(path), charset, collection);
    }
    /**
     * 从文件中读取每一行数据，数据编码为UTF-8
     *
     * @param <T> 集合类型
     * @param file 文件路径
     * @param collection 集合
     * @return 文件中的每行内容的集合
     * @throws IORuntimeException IO异常
     * @since 3.1.1
     */
    public static <T extends Collection<String>> T readUtf8Lines(File file, T collection) throws IORuntimeException {
        return readLines(file, CharsetUtil.CHARSET_UTF_8, collection);
    }
    /**
     * 从文件中读取每一行数据
     *
     * @param <T> 集合类型
     * @param file 文件路径
     * @param charset 字符集
     * @param collection 集合
     * @return 文件中的每行内容的集合
     * @throws IORuntimeException IO异常
     */
    public static <T extends Collection<String>> T readLines(File file, String charset, T collection) throws IORuntimeException {
        return FileReader.create(file, CharsetUtil.charset(charset)).readLines(collection);
    }
    /**
     * 从文件中读取每一行数据
     *
     * @param <T> 集合类型
     * @param file 文件路径
     * @param charset 字符集
     * @param collection 集合
     * @return 文件中的每行内容的集合
     * @throws IORuntimeException IO异常
     */
    public static <T extends Collection<String>> T readLines(File file, Charset charset, T collection) throws IORuntimeException {
        return FileReader.create(file, charset).readLines(collection);
    }

    /**
     * 从文件中读取每一行数据，编码为UTF-8
     *
     * @param <T> 集合类型
     * @param url 文件的URL
     * @param collection 集合
     * @return 文件中的每行内容的集合
     * @throws IORuntimeException IO异常
     */
    public static <T extends Collection<String>> T readUtf8Lines(URL url, T collection) throws IORuntimeException {
        return readLines(url, CharsetUtil.CHARSET_UTF_8, collection);
    }
    /**
     * 从文件中读取每一行数据
     *
     * @param <T> 集合类型
     * @param url 文件的URL
     * @param charsetName 字符集
     * @param collection 集合
     * @return 文件中的每行内容的集合
     * @throws IORuntimeException IO异常
     */
    public static <T extends Collection<String>> T readLines(URL url, String charsetName, T collection) throws IORuntimeException {
        return readLines(url, CharsetUtil.charset(charsetName), collection);
    }
    /**
     * 从文件中读取每一行数据
     *
     * @param <T> 集合类型
     * @param url 文件的URL
     * @param charset 字符集
     * @param collection 集合
     * @return 文件中的每行内容的集合
     * @throws IORuntimeException IO异常
     * @since 3.1.1
     */
    public static <T extends Collection<String>> T readLines(URL url, Charset charset, T collection) throws IORuntimeException {
        InputStream in = null;
        try {
            in = url.openStream();
            return IoUtil.readLines(in, charset, collection);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            IoUtil.close(in);
        }
    }
    /**
     * 从文件中读取每一行数据
     *
     * @param url 文件的URL
     * @return 文件中的每行内容的集合List
     * @throws IORuntimeException IO异常
     */
    public static List<String> readUtf8Lines(URL url) throws IORuntimeException {
        return readLines(url, CharsetUtil.CHARSET_UTF_8);
    }
    /**
     * 从文件中读取每一行数据
     *
     * @param url 文件的URL
     * @param charset 字符集
     * @return 文件中的每行内容的集合List
     * @throws IORuntimeException IO异常
     */
    public static List<String> readLines(URL url, String charset) throws IORuntimeException {
        return readLines(url, charset, new ArrayList<String>());
    }
    /**
     * 从文件中读取每一行数据
     *
     * @param url 文件的URL
     * @param charset 字符集
     * @return 文件中的每行内容的集合List
     * @throws IORuntimeException IO异常
     */
    public static List<String> readLines(URL url, Charset charset) throws IORuntimeException {
        return readLines(url, charset, new ArrayList<String>());
    }
    /**
     * 从文件中读取每一行数据，编码为UTF-8
     *
     * @param path 文件路径
     * @return 文件中的每行内容的集合List
     * @throws IORuntimeException IO异常
     * @since 3.1.1
     */
    public static List<String> readUtf8Lines(String path) throws IORuntimeException {
        return readLines(path, CharsetUtil.CHARSET_UTF_8);
    }
    /**
     * 从文件中读取每一行数据
     *
     * @param path 文件路径
     * @param charset 字符集
     * @return 文件中的每行内容的集合List
     * @throws IORuntimeException IO异常
     */
    public static List<String> readLines(String path, String charset) throws IORuntimeException {
        return readLines(path, charset, new ArrayList<String>());
    }
    /**
     * 从文件中读取每一行数据
     *
     * @param path 文件路径
     * @param charset 字符集
     * @return 文件中的每行内容的集合List
     * @throws IORuntimeException IO异常
     */
    public static List<String> readLines(String path, Charset charset) throws IORuntimeException {
        return readLines(path, charset, new ArrayList<String>());
    }
    /**
     * 从文件中读取每一行数据
     *
     * @param file 文件
     * @return 文件中的每行内容的集合List
     * @throws IORuntimeException IO异常
     */
    public static List<String> readUtf8Lines(File file) throws IORuntimeException {
        return readLines(file, CharsetUtil.CHARSET_UTF_8);
    }
    /**
     * 从文件中读取每一行数据
     *
     * @param file 文件
     * @param charset 字符集
     * @return 文件中的每行内容的集合List
     * @throws IORuntimeException IO异常
     */
    public static List<String> readLines(File file, String charset) throws IORuntimeException {
        return readLines(file, charset, new ArrayList<String>());
    }
    /**
     * 从文件中读取每一行数据
     *
     * @param file 文件
     * @param charset 字符集
     * @return 文件中的每行内容的集合List
     * @throws IORuntimeException IO异常
     */
    public static List<String> readLines(File file, Charset charset) throws IORuntimeException {
        return readLines(file, charset, new ArrayList<String>());
    }
    /**
     * 按行处理文件内容，编码为UTF-8
     *
     * @param file 文件
     * @param lineHandler {@link LineHandler}行处理器
     * @throws IORuntimeException IO异常
     */
    public static void readUtf8Lines(File file, LineHandler lineHandler) throws IORuntimeException {
        readLines(file, CharsetUtil.CHARSET_UTF_8, lineHandler);
    }
    /**
     * 按行处理文件内容
     *
     * @param file 文件
     * @param charset 编码
     * @param lineHandler {@link LineHandler}行处理器
     * @throws IORuntimeException IO异常
     */
    public static void readLines(File file, Charset charset, LineHandler lineHandler) throws IORuntimeException {
        FileReader.create(file, charset).readLines(lineHandler);
    }
    /**
     * 获得一个输出流对象
     *
     * @param file 文件
     * @return 输出流对象
     * @throws IORuntimeException IO异常
     */
    public static BufferedOutputStream getOutputStream(File file) throws IORuntimeException {
        try {
            return new BufferedOutputStream(new FileOutputStream(touch(file)));
        } catch (Exception e) {
            throw new IORuntimeException(e);
        }
    }
    /**
     * 获得一个输出流对象
     *
     * @param path 输出到的文件路径，绝对路径
     * @return 输出流对象
     * @throws IORuntimeException IO异常
     */
    public static BufferedOutputStream getOutputStream(String path) throws IORuntimeException {
        return getOutputStream(touch(path));
    }
    /**
     * 获得一个带缓存的写入对象
     *
     * @param path 输出路径，绝对路径
     * @param charsetName 字符集
     * @param isAppend 是否追加
     * @return BufferedReader对象
     * @throws IORuntimeException IO异常
     */
    public static BufferedWriter getWriter(String path, String charsetName, boolean isAppend) throws IORuntimeException {
        return getWriter(touch(path), Charset.forName(charsetName), isAppend);
    }
    /**
     * 获得一个带缓存的写入对象
     *
     * @param path 输出路径，绝对路径
     * @param charset 字符集
     * @param isAppend 是否追加
     * @return BufferedReader对象
     * @throws IORuntimeException IO异常
     */
    public static BufferedWriter getWriter(String path, Charset charset, boolean isAppend) throws IORuntimeException {
        return getWriter(touch(path), charset, isAppend);
    }
    /**
     * 获得一个带缓存的写入对象
     *
     * @param file 输出文件
     * @param charsetName 字符集
     * @param isAppend 是否追加
     * @return BufferedReader对象
     * @throws IORuntimeException IO异常
     */
    public static BufferedWriter getWriter(File file, String charsetName, boolean isAppend) throws IORuntimeException {
        return getWriter(file, Charset.forName(charsetName), isAppend);
    }
    /**
     * 获得一个带缓存的写入对象
     *
     * @param file 输出文件
     * @param charset 字符集
     * @param isAppend 是否追加
     * @return BufferedReader对象
     * @throws IORuntimeException IO异常
     */
    public static BufferedWriter getWriter(File file, Charset charset, boolean isAppend) throws IORuntimeException {
        return FileWriter.create(file, charset).getWriter(isAppend);
    }

    /**
     * 获得一个打印写入对象，可以有print
     *
     * @param path 输出路径，绝对路径
     * @param charset 字符集
     * @param isAppend 是否追加
     * @return 打印对象
     * @throws IORuntimeException IO异常
     */
    public static PrintWriter getPrintWriter(String path, String charset, boolean isAppend) throws IORuntimeException {
        return new PrintWriter(getWriter(path, charset, isAppend));
    }
    /**
     * 获得一个打印写入对象，可以有print
     *
     * @param file 文件
     * @param charset 字符集
     * @param isAppend 是否追加
     * @return 打印对象
     * @throws IORuntimeException IO异常
     */
    public static PrintWriter getPrintWriter(File file, String charset, boolean isAppend) throws IORuntimeException {
        return new PrintWriter(getWriter(file, charset, isAppend));
    }
    /**
     * 将String写入文件，覆盖模式，字符集为UTF-8
     *
     * @param content 写入的内容
     * @param path 文件路径
     * @return 写入的文件
     * @throws IORuntimeException IO异常
     */
    public static File writeUtf8String(String content, String path) throws IORuntimeException {
        return writeString(content, path, CharsetUtil.CHARSET_UTF_8);
    }
    /**
     * 将String写入文件，覆盖模式，字符集为UTF-8
     *
     * @param content 写入的内容
     * @param file 文件
     * @return 写入的文件
     * @throws IORuntimeException IO异常
     */
    public static File writeUtf8String(String content, File file) throws IORuntimeException {
        return writeString(content, file, CharsetUtil.CHARSET_UTF_8);
    }
    /**
     * 将String写入文件，覆盖模式
     *
     * @param content 写入的内容
     * @param path 文件路径
     * @param charset 字符集
     * @return 写入的文件
     * @throws IORuntimeException IO异常
     */
    public static File writeString(String content, String path, String charset) throws IORuntimeException {
        return writeString(content, touch(path), charset);
    }
    /**
     * 将String写入文件，覆盖模式
     *
     * @param content 写入的内容
     * @param path 文件路径
     * @param charset 字符集
     * @return 写入的文件
     * @throws IORuntimeException IO异常
     */
    public static File writeString(String content, String path, Charset charset) throws IORuntimeException {
        return writeString(content, touch(path), charset);
    }
    /**
     * 将String写入文件，覆盖模式
     *
     *
     * @param content 写入的内容
     * @param file 文件
     * @param charset 字符集
     * @return 被写入的文件
     * @throws IORuntimeException IO异常
     */
    public static File writeString(String content, File file, String charset) throws IORuntimeException {
        return FileWriter.create(file, CharsetUtil.charset(charset)).write(content);
    }
    /**
     * 将String写入文件，覆盖模式
     *
     *
     * @param content 写入的内容
     * @param file 文件
     * @param charset 字符集
     * @return 被写入的文件
     * @throws IORuntimeException IO异常
     */
    public static File writeString(String content, File file, Charset charset) throws IORuntimeException {
        return FileWriter.create(file, charset).write(content);
    }
    /**
     * 将String写入文件，UTF-8编码追加模式
     *
     * @param content 写入的内容
     * @param path 文件路径
     * @return 写入的文件
     * @throws IORuntimeException IO异常
     */
    public static File appendUtf8String(String content, String path) throws IORuntimeException {
        return appendString(content, path, CharsetUtil.CHARSET_UTF_8);
    }
    /**
     * 将String写入文件，追加模式
     *
     * @param content 写入的内容
     * @param path 文件路径
     * @param charset 字符集
     * @return 写入的文件
     * @throws IORuntimeException IO异常
     */
    public static File appendString(String content, String path, String charset) throws IORuntimeException {
        return appendString(content, touch(path), charset);
    }
    /**
     * 将String写入文件，追加模式
     *
     * @param content 写入的内容
     * @param path 文件路径
     * @param charset 字符集
     * @return 写入的文件
     * @throws IORuntimeException IO异常
     */
    public static File appendString(String content, String path, Charset charset) throws IORuntimeException {
        return appendString(content, touch(path), charset);
    }
    /**
     * 将String写入文件，UTF-8编码追加模式
     *
     * @param content 写入的内容
     * @param file 文件
     * @return 写入的文件
     * @throws IORuntimeException IO异常
     */
    public static File appendUtf8String(String content, File file) throws IORuntimeException {
        return appendString(content, file, CharsetUtil.CHARSET_UTF_8);
    }
    /**
     * 将String写入文件，追加模式
     *
     * @param content 写入的内容
     * @param file 文件
     * @param charset 字符集
     * @return 写入的文件
     * @throws IORuntimeException IO异常
     */
    public static File appendString(String content, File file, String charset) throws IORuntimeException {
        return FileWriter.create(file, CharsetUtil.charset(charset)).append(content);
    }
    /**
     * 将String写入文件，追加模式
     *
     * @param content 写入的内容
     * @param file 文件
     * @param charset 字符集
     * @return 写入的文件
     * @throws IORuntimeException IO异常
     */
    public static File appendString(String content, File file, Charset charset) throws IORuntimeException {
        return FileWriter.create(file, charset).append(content);
    }
    /**
     * 将列表写入文件，覆盖模式，编码为UTF-8
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param path 绝对路径
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static <T> File writeUtf8Lines(Collection<T> list, String path) throws IORuntimeException {
        return writeLines(list, path, CharsetUtil.CHARSET_UTF_8);
    }
    /**
     * 将列表写入文件，覆盖模式，编码为UTF-8
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param file 绝对路径
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static <T> File writeUtf8Lines(Collection<T> list, File file) throws IORuntimeException {
        return writeLines(list, file, CharsetUtil.CHARSET_UTF_8);
    }
    /**
     * 将列表写入文件，覆盖模式
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param path 绝对路径
     * @param charset 字符集
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static <T> File writeLines(Collection<T> list, String path, String charset) throws IORuntimeException {
        return writeLines(list, path, charset, false);
    }
    /**
     * 将列表写入文件，覆盖模式
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param path 绝对路径
     * @param charset 字符集
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static <T> File writeLines(Collection<T> list, String path, Charset charset) throws IORuntimeException {
        return writeLines(list, path, charset, false);
    }
    /**
     * 将列表写入文件，覆盖模式
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param file 文件
     * @param charset 字符集
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static <T> File writeLines(Collection<T> list, File file, String charset) throws IORuntimeException {
        return writeLines(list, file, charset, false);
    }
    /**
     * 将列表写入文件，覆盖模式
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param file 文件
     * @param charset 字符集
     * @return 目标文件
     * @throws IORuntimeException IO异常
     * @since 4.2.0
     */
    public static <T> File writeLines(Collection<T> list, File file, Charset charset) throws IORuntimeException {
        return writeLines(list, file, charset, false);
    }
    /**
     * 将列表写入文件，追加模式
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param path 文件路径
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static <T> File appendUtf8Lines(Collection<T> list, String path) throws IORuntimeException {
        return appendLines(list, path, CharsetUtil.CHARSET_UTF_8);
    }
    /**
     * 将列表写入文件，追加模式
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param path 绝对路径
     * @param charset 字符集
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static <T> File appendLines(Collection<T> list, String path, String charset) throws IORuntimeException {
        return writeLines(list, path, charset, true);
    }
    /**
     * 将列表写入文件，追加模式
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param file 文件
     * @param charset 字符集
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static <T> File appendLines(Collection<T> list, File file, String charset) throws IORuntimeException {
        return writeLines(list, file, charset, true);
    }
    /**
     * 将列表写入文件，追加模式
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param path 绝对路径
     * @param charset 字符集
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static <T> File appendLines(Collection<T> list, String path, Charset charset) throws IORuntimeException {
        return writeLines(list, path, charset, true);
    }
    /**
     * 将列表写入文件，追加模式
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param file 文件
     * @param charset 字符集
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static <T> File appendLines(Collection<T> list, File file, Charset charset) throws IORuntimeException {
        return writeLines(list, file, charset, true);
    }
    /**
     * 将列表写入文件
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param path 文件路径
     * @param charset 字符集
     * @param isAppend 是否追加
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static <T> File writeLines(Collection<T> list, String path, String charset, boolean isAppend) throws IORuntimeException {
        return writeLines(list, file(path), charset, isAppend);
    }
    /**
     * 将列表写入文件
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param path 文件路径
     * @param charset 字符集
     * @param isAppend 是否追加
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static <T> File writeLines(Collection<T> list, String path, Charset charset, boolean isAppend) throws IORuntimeException {
        return writeLines(list, file(path), charset, isAppend);
    }
    /**
     * 将列表写入文件
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param file 文件
     * @param charset 字符集
     * @param isAppend 是否追加
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static <T> File writeLines(Collection<T> list, File file, String charset, boolean isAppend) throws IORuntimeException {
        return FileWriter.create(file, CharsetUtil.charset(charset)).writeLines(list, isAppend);
    }
    /**
     * 将列表写入文件
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param file 文件
     * @param charset 字符集
     * @param isAppend 是否追加
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static <T> File writeLines(Collection<T> list, File file, Charset charset, boolean isAppend) throws IORuntimeException {
        return FileWriter.create(file, charset).writeLines(list, isAppend);
    }

    /**
     * 写数据到文件中
     *
     * @param data 数据
     * @param path 目标文件
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static File writeBytes(byte[] data, String path) throws IORuntimeException {
        return writeBytes(data, touch(path));
    }
    /**
     * 写数据到文件中
     *
     * @param dest 目标文件
     * @param data 数据
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static File writeBytes(byte[] data, File dest) throws IORuntimeException {
        return writeBytes(data, dest, 0, data.length, false);
    }
    /**
     * 写入数据到文件
     *
     * @param data 数据
     * @param dest 目标文件
     * @param off 数据开始位置
     * @param len 数据长度
     * @param isAppend 是否追加模式
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static File writeBytes(byte[] data, File dest, int off, int len, boolean isAppend) throws IORuntimeException {
        return FileWriter.create(dest).write(data, off, len, isAppend);
    }
    /**
     * 将流的内容写入文件<br>
     *
     * @param dest 目标文件
     * @param in 输入流
     * @return dest
     * @throws IORuntimeException IO异常
     */
    public static File writeFromStream(InputStream in, File dest) throws IORuntimeException {
        return FileWriter.create(dest).writeFromStream(in);
    }
    /**
     * 将文件写入流中
     *
     * @param file 文件
     * @param out 流
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static File writeToStream(File file, OutputStream out) throws IORuntimeException {
        return FileReader.create(file).writeToStream(out);
    }
    /**
     * 将流的内容写入文件<br>
     *
     * @param fullFilePath 文件绝对路径
     * @param out 输出流
     * @throws IORuntimeException IO异常
     */
    public static void writeToStream(String fullFilePath, OutputStream out) throws IORuntimeException {
        writeToStream(touch(fullFilePath), out);
    }
    /**
     * 可读的文件大小<br>
     * 参考 http://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc
     *
     * @param size Long类型大小
     * @return 大小
     */
    public static String readableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB", "EB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }





}
