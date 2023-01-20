package gitlet;
import java.io.File;
import java.io.Serializable;
import java.util.*;

import static gitlet.Utils.readObject;
import static gitlet.Utils.writeObject;


public class Register implements Serializable {
    private TreeMap<File, String> addRegister;
    private LinkedList<File> removeRegister;

    static final File REGISTER_PATH = Repository.INDEX_FILE;

    Register(){
        addRegister = new TreeMap<>();
        removeRegister = new LinkedList<>();
    }

    void registerSave(){
        writeObject(REGISTER_PATH, this);
    }

    TreeMap<File, String> getAddRegister() {
        return addRegister;
    }

    LinkedList<File> getRemoveRegister() {
        return removeRegister;
    }

    /**返回暂存区对象*/
    static Register fromFile(){
        return readObject(REGISTER_PATH, Register.class);
    }
    /**向暂存区中添加文件*/
    void addFile(File file, String blobName){
        addRegister.put(file, blobName);
    }
    /**向暂存区中删除文件*/
    void rmFile(File file) {
        if (addRegister.containsKey(file)) {
            //删除blob
            Blob.deletBlob(addRegister.get(file));
            addRegister.remove(file);
        } else {
            removeRegister.add(file);
        }
    }

    /**返回目标文件在暂存区中的哈希值*/
    String blobHashcode(File file) {
        return addRegister.get(file);
    }

    /**判断暂存区是否为空*/
    boolean isEmpty() {
        return addRegister.isEmpty() && removeRegister.isEmpty();
    }

    /**清空暂存区*/
    void clean() {
        addRegister.clear();
        removeRegister.clear();
    }

    /** 返回添加区文件 */
    List<String> stagedFiles() {
        if (addRegister.isEmpty()) {
            return null;
        } else {
            List<String> ans = new ArrayList<>();
            for (File file : addRegister.keySet()) {
                ans.add(file.getName());
            }
            return ans;
        }
    }

    /** 返回删除区文件 */
    List<String> removedFiles() {
        if (removeRegister.isEmpty()) {
            return null;
        } else {
            List<String> ans = new ArrayList<>();
            for (File file : removeRegister) {
                ans.add(file.getName());
            }
            return ans;
        }
    }

    /** 文件是否在暂存区 */
    boolean isStaged(File file) {
        return addRegister.containsKey(file);
    }

}
