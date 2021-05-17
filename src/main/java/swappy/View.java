package swappy;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class View {

    private static final Logger LOG = Logger.getLogger(View.class.getName());
    private static final String FRAME_TITLE = "Swappy";

    private JFrame frame;
    private JPanel contentPane;
    private JCheckBoxMenuItem itemDarkMode;
    private JCheckBoxMenuItem itemMinimize;
    private JMenuItem itemFlush;
    private JMenuItem itemClose;
    private JMenuItem itemAbout;
    private JTextField tfWindowsDns;
    private JTextField tfAlternativeDns;
    private JTextField tfSetWindowsDns;
    private JTextField tfSetAlternativeDns;
    private JButton btnSwap;
    private JButton btnSubmit;
    private TrayIcon trayIcon;
    private MenuItem swapItem;
    private MenuItem flushItem;
    private MenuItem openItem;
    private MenuItem closeItem;

    protected View(Model model) {
        boolean isDarkModeSelected = (boolean) model.getValueFromFile(EProperties.DARK_MODE);
        frame = new JFrame();
        frame.setTitle(FRAME_TITLE);
        frame.setBounds(100, 100, 360, 140);
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        File iconImage = new File("./img/icon32.png");
        if (iconImage.exists()) {
            frame.setIconImage(Toolkit.getDefaultToolkit().getImage(iconImage.getPath()));
        }

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        JMenu menuApp = new JMenu("Application");
        menuBar.add(menuApp);
        itemFlush = new JMenuItem("Flush DNS");
        menuApp.add(itemFlush);
        itemFlush.setActionCommand("flush_item");
        itemAbout = new JMenuItem("About");
        menuApp.add(itemAbout);
        itemAbout.setActionCommand("about");
        menuApp.addSeparator();
        itemClose = new JMenuItem("Exit");
        menuApp.add(itemClose);
        itemClose.setActionCommand("exit_item");
        JMenu menuSettings = new JMenu("Settings");
        menuBar.add(menuSettings);
        itemDarkMode = new JCheckBoxMenuItem("Dark Theme");
        menuSettings.add(itemDarkMode);
        itemDarkMode.setSelected(isDarkModeSelected);
        itemDarkMode.setActionCommand("dark_mode");
        itemMinimize = new JCheckBoxMenuItem("Start Minimized");
        menuSettings.add(itemMinimize);
        itemMinimize.setSelected((boolean) model.getValueFromFile(EProperties.MINIMIZE));
        itemMinimize.setActionCommand("minimize");

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        frame.setContentPane(contentPane);
        GridBagLayout gblContentPane = new GridBagLayout();
        gblContentPane.columnWidths = new int[] { 0, 0, 0, 0 };
        gblContentPane.rowHeights = new int[] { 0, 29, 0, 0, 0, 0 };
        gblContentPane.columnWeights = new double[] { 1.0, 0.0, 1.0, Double.MIN_VALUE };
        gblContentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        contentPane.setLayout(gblContentPane);

        JLabel lblWindowsDNS = new JLabel("Windows DNS");
        GridBagConstraints gbcLblWindowsDns = new GridBagConstraints();
        gbcLblWindowsDns.insets = new Insets(0, 0, 5, 5);
        gbcLblWindowsDns.gridx = 0;
        gbcLblWindowsDns.gridy = 1;
        contentPane.add(lblWindowsDNS, gbcLblWindowsDns);

        JLabel lblAlternativeDNS = new JLabel("Alternative DNS");
        GridBagConstraints gbcLblAlternativeDNS = new GridBagConstraints();
        gbcLblAlternativeDNS.insets = new Insets(0, 0, 5, 0);
        gbcLblAlternativeDNS.gridx = 2;
        gbcLblAlternativeDNS.gridy = 1;
        contentPane.add(lblAlternativeDNS, gbcLblAlternativeDNS);

        tfWindowsDns = new JTextField((String) model.getValueFromFile(EProperties.DNS_WINDOWS));
        tfWindowsDns.setHorizontalAlignment(SwingConstants.CENTER);
        tfWindowsDns.setEnabled(false);
        tfWindowsDns.setEditable(false);
        GridBagConstraints gbcTfWindowsDns = new GridBagConstraints();
        gbcTfWindowsDns.insets = new Insets(0, 0, 5, 5);
        gbcTfWindowsDns.fill = GridBagConstraints.HORIZONTAL;
        gbcTfWindowsDns.gridx = 0;
        gbcTfWindowsDns.gridy = 2;
        contentPane.add(tfWindowsDns, gbcTfWindowsDns);
        tfWindowsDns.setColumns(10);
        if (isDarkModeSelected) {
            tfWindowsDns.setBorder(new LineBorder(Color.decode(EConstant.FRAME_BORDER_DARK.getConstant()), Integer.parseInt(EConstant.BORDER_THICKNESS.getConstant())));
        } else {
            tfWindowsDns.setBorder(new LineBorder(Color.decode(EConstant.FRAME_BORDER_LIGHT.getConstant()), Integer.parseInt(EConstant.BORDER_THICKNESS.getConstant())));
        }

        btnSwap = new JButton("Swap");
        GridBagConstraints gbcBtnSwap = new GridBagConstraints();
        gbcBtnSwap.insets = new Insets(0, 0, 5, 5);
        gbcBtnSwap.fill = GridBagConstraints.HORIZONTAL;
        gbcBtnSwap.gridx = 1;
        gbcBtnSwap.gridy = 2;
        contentPane.add(btnSwap, gbcBtnSwap);
        btnSwap.setActionCommand("swap");

        tfAlternativeDns = new JTextField((String) model.getValueFromFile(EProperties.DNS_ALTERNATIVE));
        tfAlternativeDns.setHorizontalAlignment(SwingConstants.CENTER);
        tfAlternativeDns.setEnabled(false);
        tfAlternativeDns.setEditable(false);
        GridBagConstraints gbcTfAlternativeDns = new GridBagConstraints();
        gbcTfAlternativeDns.insets = new Insets(0, 0, 5, 0);
        gbcTfAlternativeDns.fill = GridBagConstraints.HORIZONTAL;
        gbcTfAlternativeDns.gridx = 2;
        gbcTfAlternativeDns.gridy = 2;
        contentPane.add(tfAlternativeDns, gbcTfAlternativeDns);
        tfAlternativeDns.setColumns(10);

        tfSetWindowsDns = new JTextField((String) model.getValueFromFile(EProperties.DNS_WINDOWS));
        tfSetWindowsDns.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbcTfSetWindowsDns = new GridBagConstraints();
        gbcTfSetWindowsDns.insets = new Insets(0, 0, 5, 5);
        gbcTfSetWindowsDns.fill = GridBagConstraints.HORIZONTAL;
        gbcTfSetWindowsDns.gridx = 0;
        gbcTfSetWindowsDns.gridy = 3;
        contentPane.add(tfSetWindowsDns, gbcTfSetWindowsDns);
        tfSetWindowsDns.setColumns(10);
        tfSetWindowsDns.setActionCommand("set_windows_dns");

        btnSubmit = new JButton("Submit");
        GridBagConstraints gbcBtnSubmit = new GridBagConstraints();
        gbcBtnSubmit.insets = new Insets(0, 0, 5, 5);
        gbcBtnSubmit.fill = GridBagConstraints.HORIZONTAL;
        gbcBtnSubmit.gridx = 1;
        gbcBtnSubmit.gridy = 3;
        contentPane.add(btnSubmit, gbcBtnSubmit);
        btnSubmit.setActionCommand("submit");

        tfSetAlternativeDns = new JTextField((String) model.getValueFromFile(EProperties.DNS_ALTERNATIVE));
        tfSetAlternativeDns.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbcTfSetAlternativeDns = new GridBagConstraints();
        gbcTfSetAlternativeDns.insets = new Insets(0, 0, 5, 0);
        gbcTfSetAlternativeDns.fill = GridBagConstraints.HORIZONTAL;
        gbcTfSetAlternativeDns.gridx = 2;
        gbcTfSetAlternativeDns.gridy = 3;
        contentPane.add(tfSetAlternativeDns, gbcTfSetAlternativeDns);
        tfSetAlternativeDns.setColumns(10);
        tfSetAlternativeDns.setActionCommand("set_alternative_dns");

        defaultPopupTray();
    }

    protected void defaultPopupTray() {
        if (!SystemTray.isSupported()) { return; }
        PopupMenu defaultPopup = new PopupMenu();
        openItem = new MenuItem("Open");
        swapItem = new MenuItem("Swap");
        flushItem = new MenuItem("Flush");
        closeItem = new MenuItem("Exit");
        swapItem.setActionCommand("swap_item");
        flushItem.setActionCommand("flush_item");
        openItem.setActionCommand("open_item");
        closeItem.setActionCommand("exit_item");
        defaultPopup.add(swapItem);
        defaultPopup.add(flushItem);
        defaultPopup.add(openItem);
        defaultPopup.addSeparator();
        defaultPopup.add(closeItem);
        Image image = Toolkit.getDefaultToolkit().getImage("./img/icon16.png");
        trayIcon = new TrayIcon(image, FRAME_TITLE);
        trayIcon.setImageAutoSize(true);
        trayIcon.setPopupMenu(defaultPopup);
    }

    protected void infoPopupTray() {
        trayIcon.setToolTip("Current DNS : " + tfWindowsDns.getText());
    }

    protected void iconifyGUI() {
        frame.setVisible(false);
        try {
            SystemTray.getSystemTray().add(trayIcon);
        } catch (AWTException e) {
            LOG.log(Level.WARNING, "An error occurred while iconifying the GUI !");
        }
    }

    protected void showGUI() {
        frame.setVisible(true);
        frame.setExtendedState(java.awt.Frame.NORMAL);
        SystemTray.getSystemTray().remove(trayIcon);
    }

    protected JFrame getJFrame() {
        return frame;
    }

    protected JCheckBoxMenuItem getItemDarkMode() {
        return itemDarkMode;
    }

    protected JCheckBoxMenuItem getItemMinimize() {
        return itemMinimize;
    }

    protected JMenuItem getItemFlush() {
        return itemFlush;
    }

    protected JMenuItem getItemClose() {
        return itemClose;
    }

    protected JMenuItem getItemAbout() {
        return itemAbout;
    }

    protected JTextField getWindowsDns() {
        return tfWindowsDns;
    }

    protected JTextField getAlternativeDns() {
        return tfAlternativeDns;
    }

    protected JTextField getSetWindowsDns() {
        return tfSetWindowsDns;
    }

    protected JTextField getSetAlternativeDns() {
        return tfSetAlternativeDns;
    }

    protected JButton getBtnSwap() {
        return btnSwap;
    }

    protected JButton getBtnSubmit() {
        return btnSubmit;
    }

    protected TrayIcon getTrayIcon() {
        return trayIcon;
    }

    protected MenuItem getSwapItem() {
        return swapItem;
    }

    protected MenuItem getFlushItem() {
        return flushItem;
    }

    protected MenuItem getOpenItem() {
        return openItem;
    }

    protected MenuItem getCloseItem() {
        return closeItem;
    }

}
