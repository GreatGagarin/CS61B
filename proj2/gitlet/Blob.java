package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.*;

public class Blob implements Serializable {

    static File BLOB_DIR = join(Repository.GITLET_DIR, "object");
    String hashcode;
    /** blob所存储文件的路径 */
    File pathFile;
    /** blob所存储文件的具体内容 */
    String content;

    Blob(File file, String content) {
        this.pathFile = file;
        this.content = content;
        this.hashcode = sha1(pathFile.toString(), content);
    }

    static Blob fromHash(String hashcode) {
        File file = join(BLOB_DIR, hashcode);
        return readObject(file, Blob.class);
    }

    public static void deletBlob(String hashcode) {
        File blob = join(BLOB_DIR, hashcode);
        blob.delete();
    }

    void blobSave() {
        writeObject(blobPath(), this);
    }

    File blobPath() {
        return join(BLOB_DIR, hashcode);
    }

    String getHashcode() {
        return hashcode;
    }

    void replaceWorkDir() {
        writeContents(pathFile, content);
    }
}
