//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class MiniTerminal {

    public static void main(String[] args) {
        iniTerminal();
    }

    private static void iniTerminal() {
        System.out.println("""
                \nTERMINAL SENCILLA
                =================""");

        MiniFileManager miniFileManager = new MiniFileManager();
        String ent;
        do {
            ent = miniFileManager.readText();
            commandDecision(ent, miniFileManager);
        } while (!ent.equals(miniFileManager.VALU_EXIT));
    }

    private static void commandDecision(String ent, MiniFileManager mfm) {
        if (ent.equals(mfm.VALU_PWD)) {
            mfm.getPWD();
        } else if (ent.startsWith(mfm.VALU_CD)) {
            mfm.changeDir(ent);
        } else if (ent.equals(mfm.VALU_LS) || ent.equals(mfm.VALU_LL)) {
            mfm.printList(ent);
        } else if (ent.startsWith(mfm.VALU_MKDIR)) {
            mfm.createDir(ent);
        } else if (ent.startsWith(mfm.VALU_RM)) {
            mfm.deleteDir(ent);
        } else if (ent.startsWith(mfm.VALU_MV)) {
            mfm.moveDir(ent);
        } else if (ent.equals(mfm.VALU_HELP_TEXT) || ent.equals(mfm.VALU_HELP_CHAR)) {
            mfm.basicCmdHelp();
        } else if (ent.startsWith(mfm.VALU_HELP_TEXT)) {
            mfm.advancedCmdHelp(ent);
        }
    }
}