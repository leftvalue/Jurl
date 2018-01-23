package tools;

import org.fusesource.jansi.Ansi;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

/**
 * @author linxi
 * www.leftvalue.top
 * tools
 * Date 23/01/2018 9:12 PM
 */
public class SystemClipboardTools {
    public static Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();

    /**
     * get string from system clipboard
     *
     * @return
     */
    public static String get() {
        Transferable clipTf = sysClip.getContents(null);
        if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                String line = (String) clipTf.getTransferData(DataFlavor.stringFlavor);
                return line;
            } catch (Exception e) {
                JansiService.print("Fail to read system Clipboard", Ansi.Color.RED);
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * write string into system clipboard
     *
     * @param str
     * @return
     */
    public static boolean write(String str) {
        try {
            Transferable tText = new StringSelection(str.toString());
            sysClip.setContents(tText, null);
            JansiService.print("The result has successfully copy to your clipboard :)", Ansi.Color.BLUE);
            return true;
        } catch (Exception e) {
            System.err.println("something was wrong when write into system slipboard");
            return false;
        }
    }
}
