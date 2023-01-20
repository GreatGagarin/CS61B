package gitlet;

//import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.util.List;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** Repository工作区目录 */
    static final File CWD = new File(System.getProperty("user.dir"));
    /** gitlet文件夹目录 */
    static final File GITLET_DIR = join(CWD, ".gitlet");
    /** 暂存区文件路径 */
    static final File INDEX_FILE = join(GITLET_DIR, "index");
    /** 头指针文件路径 */
    static final File HEAD_FILE = join(GITLET_DIR, "HEAD");
    /** 分区文件夹路径 */
    static final File BRANCH_DIR = join(GITLET_DIR, "branches");

    /**
     * TODO: stage
     * TODO: blob
     * TODO: branch master
     * TODO: HEAD
     * TODO: index
     *
     *
     * 
     */
    /**初始化*/
    static void initRepo() {
        //判断是否已经初始化
        if(GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in current directory");
            return;
        }
        GITLET_DIR.mkdir();
        //初始化Commit
        Commit.COMMIT_DIR.mkdir();
        Commit initCommit = new Commit("Initial Commit");
        initCommit.commitSave();
        //初始化HEAD指针与分支
        BRANCH_DIR.mkdir();
        File masterFile = join(BRANCH_DIR, "master");
        writeContents(masterFile, initCommit.getHashcode());
        writeContents(HEAD_FILE, masterFile.toString());
        //初始化暂存区
        Blob.BLOB_DIR.mkdir();
        Register initRegister = new Register();
        initRegister.registerSave();
        System.out.println(initCommit.getTime());
    }

    /**检查工作目录下是否存在fileName文件*/
    static void checkWorkDir(File file) {
        if(!file.exists()) {
            MyUtils.exitWithError("File does not exist.");
        }
    }

    static void add(String fileName) {
        File targetFile = join(CWD,fileName);
        //判断所添加文件是否存在
        checkWorkDir(targetFile);
        //判断所添加文件与是否存在于当前Commit
        String addHashcode = sha1(targetFile.toString(), readContentsAsString(targetFile));
        String headCommitFileHashcode = Commit.headCommit().blobHashcode(targetFile);
        if (addHashcode.equals(headCommitFileHashcode)) {
            MyUtils.exit();
        }
        //判断文件是否存在于当前暂存区
        Register register = Register.fromFile();
        String registerHashcode = register.blobHashcode(targetFile);
        if (addHashcode.equals(registerHashcode)) {
            MyUtils.exit();
        }
        //将文件存在blob中
        Blob addBlob = new Blob(targetFile, readContentsAsString(targetFile));
        addBlob.blobSave();
        //更改暂存区
        register.addFile(targetFile, addBlob.getHashcode());
        register.registerSave();
    }

    static void commit(String message) {
        //判断暂存区是否为空
        Register register = Register.fromFile();
        if (register.isEmpty()) {
            MyUtils.exitWithError("No changes added to the commit.");
        }
        //创建新Commit，并复制当前Commit
        Commit currentCommit = Commit.headCommit();
        Commit newCommit = new Commit(message, currentCommit.getHashcode());
        newCommit.cloneFromCommit(currentCommit);
        //通过暂存区修改Commit
        newCommit.changeFromRegister(register);
        newCommit.commitSave();
        //修改HEAD
        File currentBranch = new File(readContentsAsString(HEAD_FILE));
        writeContents(currentBranch, newCommit.getHashcode());
        //删除暂存区
        register.clean();
        register.registerSave();
    }

    static void rm(String fileName) {
        File targetFile = join(CWD, fileName);
        //判断所删除文件是否存在
        checkWorkDir(targetFile);
        //修改工作区
        targetFile.delete();
        //修改暂存区
        Register register = Register.fromFile();
        register.rmFile(targetFile);
        register.registerSave();
    }
    
    /**以日志的形式将Commit的信息输出*/
    private static void printCommit(Commit commit) {
        System.out.println("===");
        System.out.println("commit " + commit.getHashcode());
        //TODO: 如果有多个parent，修改
        System.out.println("Date: " + commit.getTime());
        System.out.println(commit.getMessage());
        System.out.println();
    }

    static void log() {
        Commit currentCommit = Commit.headCommit();
        Commit parentCommit = currentCommit.parentCommit();
        while (parentCommit != null) {
            printCommit(parentCommit);
            parentCommit = parentCommit.parentCommit();
        }
    }



    public static void global_log() {
        List<Commit> commits = Commit.eachCommit();
        for (Commit commit : commits) {
            printCommit(commit);
        }
    }

    public static void find(String msg) {
        List<Commit> commits = Commit.eachCommit();
        boolean isContain = false;
        for (Commit commit : commits) {
            if (commit.getMessage().equals(msg)) {
                printCommit(commit);
                isContain = true;
            }
        }
        if (!isContain) {
            MyUtils.exitWithError("Found no commit with that message.");
        }
    }

    /** 判断文件是否被跟踪 */
    static boolean isTracked(File file) {
        Register register = Register.fromFile();
        return register.isStaged(file) || Commit.isCommit(file);
    }



    public static void status() {
        //打印分支
        String headBranch = new File(readContentsAsString(HEAD_FILE)).getName();
        System.out.println("=== branches ===");
        System.out.println("*" + headBranch);
        List<String> branches = plainFilenamesIn(BRANCH_DIR);
        for (String branch : branches) {
            if (branch.equals(headBranch)) {
                continue;
            } else {
                System.out.println(branch);
            }
        }
        System.out.println();

        //打印暂存区
        Register register = Register.fromFile();
        System.out.println("=== Staged Files ===");
        MyUtils.printStringList(register.stagedFiles());
        System.out.println();
        System.out.println("=== Removed Files ===");
        MyUtils.printStringList(register.removedFiles());
        System.out.println();

        //TODO: 打印修改却没有同步于暂存区或Commit

        //打印未跟踪文件
        System.out.println("=== Untracked Files ===");
        List<File> files = MyUtils.plainFilesIn(CWD);
        if (files != null) {
            for (File file : files) {
                if (!Repository.isTracked(file)) {
                    System.out.println(file.getName());
                }
            }
        }
        System.out.println();
    }

    public static void checkout(String[] args) {
        switch (args.length) {
            case 2: //分支
                //判断分支是否存在
                List<String> branches = Utils.plainFilenamesIn(BRANCH_DIR);
                assert branches != null;
                if (! branches.contains(args[1])) {
                    MyUtils.exitWithError("No such branch exists");
                }
                //修改HEAD指针
                File branchFile = join(BRANCH_DIR, args[1]);
                writeContents(HEAD_FILE, branchFile.toString());
                //修改工作区
                Commit commit = Commit.headCommit();
                commit.replaceWorkDir();
                break;
            case 3: //HEAD Commit中的文件
                File targetFile = join(CWD, args[2]);
                //判断文件是否在HEAD Commit
                Commit HeadCommit = Commit.headCommit();
                if (! HeadCommit.hasFile(targetFile)) {
                    MyUtils.exitWithError("File does not exist in that commit.");
                }
                //修改工作区
                HeadCommit.replaceFile(targetFile);
                break;
            case 4:
                //判断Commit是否存在
                List<String> commitNames = Utils.plainFilenamesIn(Commit.COMMIT_DIR);
                assert commitNames != null;
                if (!commitNames.contains(args[1])) {
                    MyUtils.exitWithError("No commit with that id exists.");
                }
                File file = join(CWD, args[3]);
                //判断文件是否在目标Commit中
                Commit targetCommit = Commit.fromFile(args[1]);
                if (! targetCommit.hasFile(file)) {
                    MyUtils.exitWithError("File does not exist in that commit.");
                }
                //修改工作区
                targetCommit.replaceFile(file);
        }
    }

    public static void branch(String name) {
        //判断该分支名是否存在
        List<String> branches = Utils.plainFilenamesIn(BRANCH_DIR);
        assert branches != null;
        if (branches.contains(name)) {
            MyUtils.exitWithError("A branch with that name already exists.");
        }
        //添加新branch文件
        File newBranch = join(BRANCH_DIR, name);
        File headBranch = new File(Utils.readContentsAsString(HEAD_FILE));
        writeContents(newBranch, Utils.readContentsAsString(headBranch));
        //修改head指针
        writeContents(HEAD_FILE, newBranch.toString());
    }
}
