package com.william.util;

import java.io.*;
import java.util.*;

/**
 * 此类作为一个工具类，用来进行文件操作，可以进行读取文件，写入文件，缓存文件等操作。
 *
 * @author WilliamLi
 * @version 1.0
 * @date 2021/9/30 9:50
 */
public class FileHelper {

    private File file = null;
    private FileOutputStream fout = null;
    private BufferedReader br = null;

    /**
     * 有参数构造方法，通过文件路径创建FileHelper所操作的文件对象。
     *
     * @param path 所操作的文件路径
     */
    public FileHelper(String path) {
        if (path == null)
            throw new NullPointerException();
        try {
            this.file = new File(path);
            this.checkFile();
            this.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 有参数构造方法，通过传入文件对象构建FileHelper类。
     *
     * @param file 所操作的文件对象。
     */
    public FileHelper(File file) {
        if (file == null)
            throw new NullPointerException();
        try {
            this.file = file;
            this.checkFile();
            this.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 此方法用于确保文件存在，如果文件不存在，则在相应目录下创建文件。
     *
     * @throws IOException 创建过程中可能产生IO异常
     */
    private void checkFile() throws IOException {
        if (!this.file.exists()) {
            this.file.createNewFile();
        }
    }

    /**
     * 此方法用于初始化文件读取数据流。
     *
     * @throws IOException 创建流对象的时候可能产生IO异常
     */
    private void init() throws IOException {
        this.br = new BufferedReader(new InputStreamReader(new FileInputStream(this.file)));
    }

    /**
     * 此方法用于通过cha数组读取文件内容，使用默认1024长度char类型数据实现。
     *
     * @return 读取到的1024字符的char数组。
     */
    public char[] read() {
        return read(1024);
    }

    /**
     * 此方法用于通过char数组读取文件内容。数组长度可自定义。
     *
     * @param length 单次读取长度。
     * @return 读取到的char数组。
     */
    public char[] read(int length) {
        char[] c = new char[length];
        try {
            this.br.read(c);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return c;
    }

    /**
     * 此方法用于读取一行数据。
     *
     * @return 读取到的一行数据。
     */
    public String readLn() {
        try {
            return this.br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 此方法用于将字符串写入文件，并且不换行。此方法可以选择是否开启附加模式。
     *
     * @param val    字符串数据。
     * @param append 写入模式，传入true表示在文件结尾附加信息，传入false代表覆盖元数据。
     * @return 写入的内容。
     */
    public String write(String val, boolean append) {
        try {
            this.fout = new FileOutputStream(this.file, append);
            this.fout.write(val.getBytes());
            this.fout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                this.fout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return val;
    }

    /**
     * 此方法用于写入一行字符串数据，并在结尾处添加回车换行符。此方法可以选择是否开启附加模式。
     *
     * @param val    字符串数据。
     * @param append 写入模式，传入true表示在文件结尾附加信息，传入false代表覆盖元数据。
     * @return 写入的内容。
     */
    public String writeLn(String val, boolean append) {
        this.write(val + "\r\n", append);
        return val;
    }

    /**
     * 此方法用于通过文件路径更换读取的文件。
     *
     * @param path 新文件路径。
     * @return 是否更换成功。
     */
    public boolean changeFile(final String path) {
        return this.changeFile(new File(path));
    }

    /**
     * 此方法用于通过新文件对象更换读取的文件。
     *
     * @param file 新文件对象。
     * @return 是否更换成功。
     */
    public boolean changeFile(final File file) {
        try {
            this.closeInput();
            this.file = file;
            this.checkFile();
            this.init();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 此方法用于将文件中的所有内容缓存进List中。
     *
     * @return 缓存之后的List对象，通过ArrayList实现。
     * @throws IOException 创建流对象过程中可能产生IO异常。
     */
    public List<String> bufferFileList() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(this.file)));
        List<String> resultList = new ArrayList<>();
        String value = bufferedReader.readLine();
        while (value != null) {
            resultList.add(value);
            value = bufferedReader.readLine();
        }
        bufferedReader.close();
        return resultList;
    }

    /**
     * 此方法用于将文件中的所有内容拆分并且缓存进Map中。
     *
     * @param sign 拆分标志。
     * @return 缓存之后的Map对象，通过LinkedHashMap实现。
     * @throws IOException 创建流对象过程中可能产生IO异常。
     */
    public Map<String, String> bufferFileStringMap(final String sign) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(this.file)));
        Map<String, String> resultMap = new LinkedHashMap<>();
        String value = bufferedReader.readLine();
        while (value != null) {
            String[] values = value.split(sign);
            resultMap.put(values[0], values[1]);
            value = bufferedReader.readLine();
        }
        bufferedReader.close();
        return resultMap;
    }

    /**
     * 此方法用于将文件中的所有内容拆分并且缓存进Map中，同时拆分完之后第一部分将会被转换成Long类型包装类对象。
     *
     * @param sign 拆分标志。
     * @return 缓存之后的Map对象，通过LinkedHashMap实现。
     * @throws IOException 创建流对象过程中可能产生IO异常。
     */
    public Map<Long, String> bufferFileIdStringMap(final String sign) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(this.file)));
        Map<Long, String> resultMap = new LinkedHashMap<>();
        String value = bufferedReader.readLine();
        while (value != null) {
            String[] values = value.split(sign);
            resultMap.put(Long.parseLong(values[0]), values[1]);
            value = bufferedReader.readLine();
        }
        bufferedReader.close();
        return resultMap;
    }

    /**
     * 此方法用于将一个存放字符串的List写入文件中。
     *
     * @param list   将要写入的List。
     * @param append 是否为附加信息。
     * @return 是否写入成功。
     */
    public boolean writeList(final List<String> list, final boolean append) {
        if (list.size() == 0)
            return false;
        if (!append) {
            this.write("", false);
        }
        this.myWriteList(list);
        return true;
    }

    /**
     * 此方法用于将一个存储字符串键值对的Map写入文件中，并且用sign作为分隔符分开。
     *
     * @param map    将要存储的map。
     * @param sign   分隔符。
     * @param append 是否为附加信息。
     * @return 是否写入成功。
     */
    public boolean writeMap(final Map<String, String> map, String sign, final boolean append) {
        if (map.size() == 0)
            return false;
        if (!append) {
            this.write("", false);
        }
        this.myWriteMap(map, sign);
        return true;
    }

    /**
     * 此方法用于关闭所有文件读取流。
     *
     * @throws IOException 关闭流过程中可能抛出IO异常。
     */
    public void closeInput() throws IOException {
        if (br != null) {
            br.close();
            br = null;
        }
    }

    /**
     * 此方法用于获取详见正在读取的文件对象。<br>不建议使用此方法。
     *
     * @return 当前正在读取的文件对象。
     */
    @Deprecated
    public File getFile() {
        return file;
    }

    /**
     * 私有的方法，用于辅助将整个list中的数据写入文件，同时每次写入之后换行。
     *
     * @param list 将要写入的内容。
     */
    private void myWriteList(final List<String> list) {
        for (String v : list)
            this.writeLn(v, true);
    }

    /**
     * 私有的方法，用于辅助将整个map中的数据写入文件，key和value之间用分隔符分开，同时每次写入之后换行。
     *
     * @param map  将要写入的内容。
     * @param sign key和value之间的分隔符。
     */
    private void myWriteMap(final Map<String, String> map, String sign) {
        for (Map.Entry<String, String> entry : map.entrySet())
            this.writeLn(entry.getKey() + sign + entry.getValue(), true);
    }
}
