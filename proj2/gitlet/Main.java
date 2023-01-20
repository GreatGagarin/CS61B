package gitlet;

import java.io.IOException;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Great_Gagarin
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            MyUtils.exitWithError("please enter a command.");
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "init":
                validateNumArgs("init", args, 1);
                Repository.initRepo();
                break;
            case "add":
                validateNumArgs("add", args, 2);
                Repository.add(args[1]);
                break;
            case "commit":
                if (args.length == 1) {
                    MyUtils.exitWithError("Please enter a commit message.");
                }
                validateNumArgs("commit", args, 2);
                Repository.commit(args[1]);
                break;
            case "rm":
                validateNumArgs("rm", args, 2);
                Repository.rm(args[1]);
                break;
            case "log":
                validateNumArgs("log", args, 1);
                Repository.log();
                break;
            case "global-log":
                validateNumArgs("global-log", args, 1);
                Repository.global_log();
                break;
            case "find":
                validateNumArgs("find", args, 2);
                Repository.find(args[1]);
                break;
            case "status":
                validateNumArgs("status", args, 1);
                Repository.status();
                break;
            case "checkout":
                validateNumAroundArgs("checkout", args, 2, 3);
                Repository.checkout(args);
                break;
            case "branch":
                validateNumArgs("branch", args, 2);
                Repository.branch(args[1]);
                break;
            default:
                MyUtils.exitWithError("No command with that name exists.");
        }
    }

    private static void validateNumArgs(String cmd, String[] args, int num) {
        if (args.length != num) {
            throw new RuntimeException(
                    String.format("Invalid number of arguments for %s", cmd));
        }
    }

    private static void validateNumAroundArgs(String cmd, String[] args, int min, int max) {
        if (args.length < min || args.length > max) {
            throw new RuntimeException(
                    String.format("Invalid number of arguments for %s", cmd));
        }
    }
}
