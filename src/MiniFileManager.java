import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class MiniFileManager {

    // Constantes - Formato
    private final String FRMT_DATE = "dd/MM/yyyy HH:mm:ss";
    private final String FRMT_INPUT = "%s ...: ";
    private final String FRMT_LIST = "%-50s %-2d bytes %s";
    private final String FRMT_HELP = "%-19s: %s";

    // Constantes - Valores
    protected final String VALU_PWD = "pwd";
    protected final String VALU_CD = "cd";
    protected final String VALU_LS = "ls";
    protected final String VALU_LL = "ll";
    protected final String VALU_MKDIR = "mkdir";
    protected final String VALU_RM = "rm";
    protected final String VALU_MV = "mv";
    protected final String VALU_HELP_TEXT = "help";
    protected final String VALU_HELP_CHAR = "?";
    protected final String VALU_EXIT = "exit";

    // Constantes - Descripcion valores
    private final String DESC_PWD = "Muestra la carpeta actual";
    private final String DESC_CD_PARENT = "Cambia la carpeta actual a <dir>";
    private final String DESC_CD_PATH = "Cambia a la carpeta superior";
    private final String DESC_LS = "Muestra la lista de directorios y archivos de la carpeta actual";
    private final String DESC_LL = "Muestra la lista de directorios y archivos de la carpeta actual con el tamaño y la fecha de modificación";
    private final String DESC_MKDIR = "Crea el directorio <dir> en la carpeta actual";
    private final String DESC_RM = "Elimina <file>. Si es una carpeta, eliminará todos los archivos de esta";
    private final String DESC_MV = "Mueve o cambia el nombre de <file1> a <file2>";
    private final String DESC_HELP_BASIC = "Muestra la ayuda de todos los comandos de la Terminal Simple";
    private final String DESC_HELP_ADVANCED = "Muestra la ayuda de los comandos especificados de la Terminal Simple";
    private final String DESC_EXIT = "Salir de la Terminal Simple";

    // Constantes - MSG error
    private final String ERRO_DIR_NOT_FOUND = "(!) Directorio no encontrado";
    private final String ERRO_DIR_NOT_EXIST = "(!) Directorio existente";
    private final String ERRO_MOVE_DIR = "(!) Error al mover el directorio: ";
    private final String ERRO_DELETE_DIR = "(!) Error al eliminar el directorio";

    private File currentPath = new File(System.getProperty("user.dir") + "\\src");

    protected void getPWD() {
        System.out.println(getAbsoluteCurrentPath());
    }

    protected void changeDir(String ent) {
        String dir = separateEnt(ent, 2, 1);

        if (dir.equals("..") && currentPath.getParentFile().exists()) {
            currentPath = currentPath.getParentFile();
        } else {
            changeNewPath(dir);
        }
    }

    private String separateEnt(String ent, int size, int position) {
        String dir = inputArr(ent, size)[position];
        return dir;
    }

    private void changeNewPath(String path) {
        File newCurrentPath = new File("\\" + path);

        if (newCurrentPath.exists() && newCurrentPath.isDirectory()) {
            currentPath = newCurrentPath;
        } else {
            System.out.println(ERRO_DIR_NOT_FOUND);
        }
    }

    protected void printList(String ent) {
        if (ent.equals(VALU_LL)) {
            listMoreInfo();
        } else {
            listBasicInfo();
        }
    }

    private void listBasicInfo() {
        for (String infoDir : currentPath.list()) {
            System.out.println(infoDir);
        }
    }

    private void listMoreInfo() {
        String fch = getLastModifiedDate();

        for (String infoDir : currentPath.list()) {
            System.out.println(String.format(FRMT_LIST, infoDir, infoDir.length(), fch));
        }
    }

    private String getLastModifiedDate() {
        long fecMod = currentPath.lastModified();
        return changeToDate(fecMod);
    }

    private String changeToDate(long fecMod) {
        Date d = new Date(fecMod);
        SimpleDateFormat fecModDate = new SimpleDateFormat(FRMT_DATE);
        String fch = fecModDate.format(d);
        return fch;
    }

    protected void createDir(String ent) {
        String dir = separateEnt(ent, 2, 1);
        File newDirectoryPath = new File(getAbsoluteCurrentPath() + '\\' + dir);

        if (!newDirectoryPath.exists()) {
            newDirectoryPath.mkdir();
        } else {
            System.out.println(ERRO_DIR_NOT_EXIST);
        }
    }

    protected void deleteDir(String ent) {
        String dir = separateEnt(ent, 2, 1);
        File newDirectoryPath = new File(getAbsoluteCurrentPath() + '\\' + dir);

        if (newDirectoryPath.exists()) {

            if (newDirectoryPath.listFiles() == null) {
                deleteDirectory(newDirectoryPath);
            } else {
                deleteDirectoryFiles(newDirectoryPath);
                deleteDirectory(newDirectoryPath);
            }

        } else {
            System.out.println(ERRO_DIR_NOT_FOUND);
        }
    }

    private static void deleteDirectory(File newDirectoryPath) {
        newDirectoryPath.delete();
    }

    private static void deleteDirectoryFiles(File newDirectoryPath) {
        for (File fl : newDirectoryPath.listFiles()) {
            deleteDirectory(fl);
        }
    }

    private static boolean searchDirectory(File newDirectoryPath) {
        boolean isDirectory = false;
        for (File fl : newDirectoryPath.listFiles()) {
            if (fl.isDirectory()) {
                isDirectory = true;
            }
        }
        return isDirectory;
    }

    protected void moveDir(String ent) {
        String actualDir = separateEnt(ent, 3, 1);
        String newDir = separateEnt(ent, 3, 2);

        File currentPathWithActualDir = new File(getAbsoluteCurrentPath() + '\\' + actualDir);
        File newCurrentPathWithNewDir = new File('\\' + newDir);

        moveToNewDir(currentPathWithActualDir, newCurrentPathWithNewDir);
    }

    private void moveToNewDir(File currentPathWithActualDir, File newCurrentPathWithNewDir) {
        if (currentPathWithActualDir.exists()) {
            try {
                Files.move(currentPathWithActualDir.toPath(), newCurrentPathWithNewDir.toPath());
            } catch (IOException ex) {
                System.out.println(ERRO_MOVE_DIR + ex.getMessage());
            }
        } else {
            System.out.println(ERRO_DIR_NOT_FOUND);
        }
    }

    protected void basicCmdHelp() {
        System.out.println(String.format(FRMT_HELP, VALU_PWD, DESC_PWD) + "\n" +
                String.format(FRMT_HELP, VALU_CD, "\s<dir>", DESC_CD_PARENT) + "\n" +
                String.format(FRMT_HELP, VALU_CD, "\s..", DESC_CD_PATH) + "\n" +
                String.format(FRMT_HELP, VALU_LS, DESC_LS) + "\n" +
                String.format(FRMT_HELP, VALU_LL, DESC_LL) + "\n" +
                String.format(FRMT_HELP, VALU_MKDIR + "\s<dir>", DESC_MKDIR) + "\n" +
                String.format(FRMT_HELP, VALU_RM + "\s<file>", DESC_RM) + "\n" +
                String.format(FRMT_HELP, VALU_MV + "\s<file1>\s<file2>", DESC_MV) + "\n" +
                String.format(FRMT_HELP, VALU_HELP_TEXT + '\\' + VALU_HELP_CHAR, DESC_HELP_BASIC) + "\n" +
                String.format(FRMT_HELP, VALU_HELP_TEXT + "\s<cmd>", DESC_HELP_ADVANCED) + "\n" +
                String.format(FRMT_HELP, VALU_EXIT, DESC_EXIT));
    }

    protected void advancedCmdHelp(String ent) {
        String cmd = separateEnt(ent, 2, 1);

        if (cmd.equals(VALU_PWD)) {
            System.out.println(String.format(FRMT_HELP, VALU_PWD, DESC_PWD));

        } else if (cmd.equals(VALU_CD)) {
            System.out.println(String.format(FRMT_HELP, VALU_CD + "\s<dir>", DESC_CD_PARENT));
            System.out.println(String.format(FRMT_HELP, VALU_CD + "\s..", DESC_CD_PATH));

        } else if (cmd.equals(VALU_LS)) {
            System.out.println(String.format(FRMT_HELP, VALU_LS, DESC_LS));

        } else if (cmd.equals(VALU_LL)) {
            System.out.println(String.format(FRMT_HELP, VALU_LL, DESC_LL));

        } else if (cmd.equals(VALU_MKDIR)) {
            System.out.println(String.format(FRMT_HELP, VALU_MKDIR + "\s<dir>", DESC_MKDIR));

        } else if (cmd.equals(VALU_RM)) {
            System.out.println(String.format(FRMT_HELP, VALU_RM + "\s<file>", DESC_RM));

        } else if (cmd.equals(VALU_MV)) {
            System.out.println(String.format(FRMT_HELP, VALU_MV + "\s<file1>\s<file2>", DESC_MV));

        } else if (cmd.equals(VALU_HELP_TEXT)) {
            System.out.println(String.format(FRMT_HELP, VALU_HELP_TEXT + '\\' + VALU_HELP_CHAR, DESC_HELP_BASIC));
            System.out.println(String.format(FRMT_HELP, VALU_HELP_TEXT + "\s<cmd>", DESC_HELP_ADVANCED));

        } else if (cmd.equals(VALU_EXIT)) {
            System.out.println(String.format(FRMT_HELP, VALU_EXIT, DESC_EXIT));
        }
    }

    protected String readText() {
        Scanner SCN = new Scanner(System.in);

        System.out.print(String.format(FRMT_INPUT, getAbsoluteCurrentPath()));
        return SCN.nextLine();
    }

    private String[] inputArr(String ent, int lmt) {
        return ent.split("\\s", lmt);
    }

    private String getAbsoluteCurrentPath() {
        return currentPath.getAbsolutePath();
    }
}
