/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timer;

import java.awt.Toolkit;

/**
 *
 * @author Lutz
 */
public class Beep {
    private static Beep singleton = null;
    private Toolkit defaultToolkit;
    
    public static Beep getInstance() {
        if (singleton == null) {
            singleton = new Beep();
        }
        return singleton;
    }
    
    private Beep() {
        this.defaultToolkit = Toolkit.getDefaultToolkit();
    }
    
    public void beep() {
        this.defaultToolkit.beep();
    }
    
    
}
