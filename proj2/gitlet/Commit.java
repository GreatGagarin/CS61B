package gitlet;

// TODO: any imports you need here

//import net.sf.saxon.trans.SymbolicName;
//import org.checkerframework.checker.units.qual.C;

import static gitlet.Utils.*;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
//import java.util.Collection;

//TODO: 使用Collection更改map和List结构

/** Commit类，用来存储每次Commit时的数据，包括暂存区文件，时间，message
 *
 *  @author Great Gagain
 */

//TODO: 修改parent
public class Commit implements Serializable {

    /** Commit存储路径 */
    protected static final File COMMIT_DIR = join(Repository.GITLET_DIR, "commit");
    /** message */
    private final String message;
    /** The name of last commit*/
    private final LinkedList<String> parents;
    /** The time when commit*/
    private final String time;

    private final String hashcode;

    /**该Commit内包括的文件*/
    private TreeMap<File, String> blobs;

    public Commit(String message) {
        this(message, "initial commit");
    }
    public Commit(String message, String parent) {
        this.message = message;
        this.parents = new LinkedList<>();
        this.parents.add(parent);
        time = MyUtils.currentTime();
        blobs = new TreeMap<>();
        hashcode = sha1(time, message, parents.toString());
    }


    public void commitSave() {
        File currentFile = join(COMMIT_DIR, hashcode);
        writeObject(currentFile, this);
    }

    public String getTime() {
        return time;
    }

    public TreeMap<File, String> getBlobs() {
        return blobs;
    }

    public String getMessage(){
        return message;
    }


    public String getHashcode() {
        return hashcode;
    }

    /**通过Commit哈希值返回Commit对象
     *
     * @param hashcode 目标Commit的哈希值
     * */
    static Commit fromFile(String hashcode) {
        File commitflie = join(COMMIT_DIR, hashcode);
        return readObject(commitflie, Commit.class);
    }

    /**返回该Commit的父Commit
     * 如果该Commit为初始化Commit，返回null
     * 如果该Commit有多个父Commit，只返回第一个父Commit
     * */
    Commit parentCommit() {
        if (parents.get(0).equals("initial commit")) {
            return null;
        } else {
            return fromFile(parents.get(0));
        }
    }

    /**返回HEAD所指向的Commit*/
    static Commit headCommit() {
        File currentBranch = new File(readContentsAsString(Repository.HEAD_FILE));
        return fromFile(readContentsAsString(currentBranch));
    }

    String blobHashcode(File file) {
        return blobs.get(file);
    }

    /**将另一个Commit中的blobs复制到该Commit*/
    void cloneFromCommit(Commit other) {
        for (File file : other.blobs.keySet()) {
            this.blobs.put(file, other.blobs.get(file));
        }
    }

    /**通过暂存区修改该Commit中的blobs*/
    void changeFromRegister(Register register) {
        //处理addRegister
        TreeMap<File, String> addReg = register.getAddRegister();
        for (File file : addReg.keySet()) {
            this.blobs.put(file, addReg.get(file));
        }
        //处理removeRegister
        LinkedList<File> rmReg = register.getRemoveRegister();
        for (File file : rmReg) {
            this.blobs.remove(file);
        }
    }

    /** 根据Commit内容修改工作区 */
    void replaceWorkDir() {
        if (blobs != null) {
            //删除不存在文件
            List<File> files = MyUtils.plainFilesIn(Repository.CWD);
            if (files != null) {
                for (File file : files) {
                    if (! Repository.isTracked(file)) {
                        MyUtils.exitWithError("There is an untracked file in the way; " +
                                "delete it, or add and commit it first");
                    }
                }
            }
            if (files != null) {
                for (File file : files) {
                    if (Repository.isTracked(file) && ! hasFile(file)) {
                        file.delete();
                    }
                }
            }
            //修改存在文件
            for (String hashcode : blobs.values()) {
                Blob blob = Blob.fromHash(hashcode);
                blob.replaceWorkDir();
            }
        }
    }

    /** 将工作区中的目标文件改为该Commit中保存的内容 */
    void replaceFile(File file) {
        Blob blob = Blob.fromHash(blobs.get(file));
        blob.replaceWorkDir();
    }
    /** 将所有的Commit以列表的形式返回 */
    static List<Commit> eachCommit() {
        List<String> names = plainFilenamesIn(COMMIT_DIR);
        List<Commit> ans = new ArrayList<>();
        assert names != null;
        for (String name : names) {
            Commit commit = Commit.fromFile(name);
            ans.add(commit);
        }
        return ans;
    }

    /** 判断文件是否被提交 */
    static boolean isCommit(File file) {
        List<Commit> commits = eachCommit();
        for (Commit commit : commits) {
            if (commit.blobs.containsKey(file)) {
                return true;
            }
        }
        return false;
    }

    /** 判断文件是否在该Commit */
    boolean hasFile(File file) {
        return blobs.containsKey(file);
    }
}
