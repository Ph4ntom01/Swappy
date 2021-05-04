package swappy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

public class Controller implements ActionListener, MouseListener, MouseMotionListener, WindowStateListener {

    private Model model;
    private View view;

    protected Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("flush_item")) {
            model.flushWindowsDNS(view);
        } else if (e.getActionCommand().equals("dark_mode")) {
            model.changeTheme(view);
        } else if (e.getActionCommand().equals("minimize")) {
            model.changeMinimize(view);
        } else if (e.getActionCommand().equals("about")) {
            model.about(view);
        } else if (e.getActionCommand().equals("swap") || e.getActionCommand().equals("swap_item")) {
            model.swapDns(view);
        } else if (e.getActionCommand().equals("set_windows_dns") || e.getActionCommand().equals("set_alternative_dns") || e.getActionCommand().equals("submit")) {
            model.submitDns(view);
        } else if (e.getActionCommand().equals("open_item")) {
            view.showGUI();
        } else if (e.getActionCommand().equals("exit_item")) {
            System.exit(0);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        view.infoPopupTray();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            view.showGUI();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void windowStateChanged(WindowEvent e) {
        if (e.getNewState() == java.awt.Frame.ICONIFIED) {
            view.iconifyGUI();
        } else if (e.getNewState() == java.awt.Frame.NORMAL) {
            view.showGUI();
        }
    }

}
