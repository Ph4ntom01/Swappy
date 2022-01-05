package swappy;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;

public class Swappy {

    private static final Logger LOG = Logger.getLogger(Swappy.class.getName());
    private static final Configuration CONFIG = new Configuration();

    public static void main(String[] args) {
        try {
            UIManager.put("Button.arc", 999);
            UIManager.put("Component.focusWidth", 1);
            if (Boolean.parseBoolean(CONFIG.readValue(EProperties.DARK_MODE)))
                UIManager.setLookAndFeel(new FlatDarculaLaf());
            else
                UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException e) {
            LOG.log(Level.WARNING, "Look and feel not present !");
        }
        SwingUtilities.invokeLater(Swappy::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        Model model = new Model();
        View view = new View(model);
        Controller controller = new Controller(model, view);
        view.getJFrame().addWindowStateListener(controller);
        view.getItemDarkMode().addActionListener(controller);
        view.getItemMinimize().addActionListener(controller);
        view.getItemFlush().addActionListener(controller);
        view.getItemClose().addActionListener(controller);
        view.getItemAbout().addActionListener(controller);
        view.getSetWindowsDns().addActionListener(controller);
        view.getSetAlternativeDns().addActionListener(controller);
        view.getBtnSwap().addActionListener(controller);
        view.getBtnSubmit().addActionListener(controller);
        view.getTrayIcon().addMouseListener(controller);
        view.getTrayIcon().addMouseMotionListener(controller);
        view.getSwapItem().addActionListener(controller);
        view.getFlushItem().addActionListener(controller);
        view.getOpenItem().addActionListener(controller);
        view.getCloseItem().addActionListener(controller);
        if (Boolean.parseBoolean(CONFIG.readValue(EProperties.MINIMIZE))) {
            view.iconifyGUI();
        } else {
            view.getJFrame().setVisible(true);
        }
    }

    protected static Configuration getConfig() {
        return CONFIG;
    }

}
