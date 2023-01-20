package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class MyUtils {

    static String currentTime(){
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");
        Date date = new Date();
        return sdf.format(date);
    }

    static void exitWithError(String error) {
        if (error != null && error != "") {
            System.out.println(error);
        }
        System.exit(0);
    }

    static void exit() {
        System.exit(0);
    }

    /** 分行打印字符串列表里的所有元素 */
    static void printStringList(List<String> list) {
        if (list != null) {
            for (String string : list) {
                System.out.println(string);
            }
        }
    }

    /**以列表的形式（元素为文件）返回目标文件夹内所有的plain file,
     * 如果目标文件夹内没有plain file，返回null
     *
     * @param dir 目标文件夹
     * */
    static List<File> plainFilesIn(File dir) {
        List<String> names = Utils.plainFilenamesIn(dir);
        List<File> files = new ArrayList<>();
        if (names != null) {
            for (String name : names) {
                files.add(Utils.join(dir, name));
            }
        } else {
            return null;
        }
        return files;
    }

}
