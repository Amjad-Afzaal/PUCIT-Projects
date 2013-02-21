/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package documentcomparer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 *
 * @author amjad
 */
public class ChooseFile extends JFrame
{
    private JFileChooser chooser;
    private String fileName;

    ChooseFile()
    {
        chooser = new JFileChooser();
        chooser.updateUI();
        int r = chooser.showOpenDialog(new JFrame());
        if (r == JFileChooser.APPROVE_OPTION)
        {
            fileName = chooser.getSelectedFile().getPath();
        }
    }

    public JFileChooser getChooser() {
        return chooser;
    }

    public void setChooser(JFileChooser chooser) {
        this.chooser = chooser;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    
}

