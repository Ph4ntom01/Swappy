package swappy;

import java.awt.Color;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;
import javax.swing.event.HyperlinkEvent;

import org.apache.commons.validator.routines.InetAddressValidator;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;

public class Model {

    private static final Logger LOG = Logger.getLogger(Model.class.getName());

    protected Model() {}

    protected void changeTheme(View view) {
        if (view.getItemDarkMode().isSelected()) {
            try {
                UIManager.setLookAndFeel(new FlatDarculaLaf());
            } catch (UnsupportedLookAndFeelException e) {
                LOG.log(Level.WARNING, "An error occurred while changing the look and feel !");
            }
            Swappy.getConfig().updateValue(EProperties.DARK_MODE.getKey(), "true");
            view.getWindowsDns().setBorder(new LineBorder(Color.decode(EConstant.FRAME_BORDER_DARK.getConstant()), Integer.parseInt(EConstant.BORDER_THICKNESS.getConstant())));
            SwingUtilities.updateComponentTreeUI(view.getJFrame());
        } else {
            try {
                UIManager.setLookAndFeel(new FlatIntelliJLaf());
            } catch (UnsupportedLookAndFeelException e) {
                LOG.log(Level.WARNING, "An error occurred while changing the look and feel !");
            }
            Swappy.getConfig().updateValue(EProperties.DARK_MODE.getKey(), "false");
            view.getWindowsDns().setBorder(new LineBorder(Color.decode(EConstant.FRAME_BORDER_LIGHT.getConstant()), Integer.parseInt(EConstant.BORDER_THICKNESS.getConstant())));
            SwingUtilities.updateComponentTreeUI(view.getJFrame());
        }
    }

    protected void changeMinimize(View view) {
        if (view.getItemMinimize().isSelected()) {
            Swappy.getConfig().updateValue(EProperties.MINIMIZE.getKey(), "true");
        } else {
            Swappy.getConfig().updateValue(EProperties.MINIMIZE.getKey(), "false");
        }
    }

    protected void about(View view) {
        // @formatter:off
        JEditorPane editorPane = new JEditorPane(
                "text/html",
                "<html><body>Swappy Version " + EConstant.SWAPPY_VERSION.getConstant() +
                "<br>Designed by <a href=\"https://github.com/Ph4ntom01\">Ph4ntom01</a></body></html>"
                );
        // @formatter:on
        editorPane.addHyperlinkListener(event -> {
            if (event.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                try {
                    Desktop.getDesktop().browse(event.getURL().toURI());
                } catch (IOException | URISyntaxException err) {
                    LOG.log(Level.WARNING, "An error occurred while opening the link !");
                }
            }
        });
        editorPane.setEditable(false);
        JOptionPane.showMessageDialog(view.getJFrame(), editorPane, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Swaps DNS between the windows and alternative DNS.
     * 
     * @param view The current view.
     */
    protected void swapDns(View view) {
        String windowsDns = view.getWindowsDns().getText();
        String alternativeDns = view.getAlternativeDns().getText();
        String setWindowsDns = view.getSetWindowsDns().getText();
        String setAlternativeDns = view.getSetAlternativeDns().getText();
        Swappy.getConfig().updateValue(EProperties.DNS_WINDOWS.getKey(), alternativeDns);
        Swappy.getConfig().updateValue(EProperties.DNS_ALTERNATIVE.getKey(), windowsDns);
        view.getWindowsDns().setText(alternativeDns);
        view.getAlternativeDns().setText(windowsDns);
        view.getSetWindowsDns().setText(setAlternativeDns);
        view.getSetAlternativeDns().setText(setWindowsDns);
        changeWindowsDNS(view);
    }

    /**
     * Submits a new DNS value.
     * 
     * @param view The current view.
     */
    protected void submitDns(View view) {
        String setWindowsDns = view.getSetWindowsDns().getText();
        String setAlternativeDns = view.getSetAlternativeDns().getText();
        String outputValidation = verifyInetAddress(setWindowsDns, setAlternativeDns);
        if (outputValidation.equals("valid")) {
            Swappy.getConfig().updateValue(EProperties.DNS_WINDOWS.getKey(), setWindowsDns);
            Swappy.getConfig().updateValue(EProperties.DNS_ALTERNATIVE.getKey(), setAlternativeDns);
            view.getWindowsDns().setText(setWindowsDns);
            view.getAlternativeDns().setText(setAlternativeDns);
            changeWindowsDNS(view);
        } else {
            JOptionPane.showMessageDialog(view.getJFrame(), "<html><body>Invalid IPv4 address !<b>" + outputValidation + "</b></body></html>", "Alert", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Changes the Windows DNS.
     * 
     * @param windowsDns Chosen DNS value.
     */
    protected void changeWindowsDNS(View view) {
        try {
            flushWindowsDNS(view);
            Runtime.getRuntime().exec("netsh interface ip set dns " + Swappy.getConfig().readValue(EProperties.NET_ADAPTER) + " static " + view.getWindowsDns().getText());
        } catch (IOException e) {
            LOG.log(Level.WARNING, "An error occurred while changing the DNS !");
            JOptionPane.showMessageDialog(view.getJFrame(), "An error occurred while changing the DNS !", "DNS Change Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Flushes the DNS resolver cache.
     */
    protected void flushWindowsDNS(View view) {
        try {
            Runtime.getRuntime().exec("cmd /c ipconfig /flushdns");
        } catch (IOException e) {
            LOG.log(Level.WARNING, "An error occurred while flushing the DNS records !");
            JOptionPane.showMessageDialog(view.getJFrame(), "An error occurred while flushing the DNS records !", "DNS Flush Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Validates an IPv4 address.
     * 
     * @param ipv4 The array containing both the windows and alternative DNS.
     * 
     * @return The output validation.
     */
    private String verifyInetAddress(String inet1, String inet2) {
        InetAddressValidator validator = InetAddressValidator.getInstance();
        String outputValidation = "valid";
        String newline = "<br>- ";
        if (!validator.isValidInet4Address(inet1)) {
            outputValidation = newline + inet1;
        }
        if (!validator.isValidInet4Address(inet2)) {
            outputValidation = newline + inet2;
        }
        if (!validator.isValidInet4Address(inet1) && !validator.isValidInet4Address(inet2)) {
            outputValidation = newline + inet1 + newline + inet2;
        }
        return outputValidation;
    }

    /**
     * Gets a specific value from the file.
     * 
     * @param prop The ENUM properties key.
     * 
     * @return A String containing the value.
     */
    protected Object getValueFromFile(EProperties prop) {
        Object value = "";
        switch (prop) {
        case DARK_MODE:
            value = Boolean.parseBoolean(Swappy.getConfig().readValue(EProperties.DARK_MODE));
            break;
        case MINIMIZE:
            value = Boolean.parseBoolean(Swappy.getConfig().readValue(EProperties.MINIMIZE));
            break;
        case DNS_WINDOWS:
            value = Swappy.getConfig().readValue(EProperties.DNS_WINDOWS);
            break;
        case DNS_ALTERNATIVE:
            value = Swappy.getConfig().readValue(EProperties.DNS_ALTERNATIVE);
            break;
        case NET_ADAPTER:
            value = Swappy.getConfig().readValue(EProperties.NET_ADAPTER);
            break;
        }
        return value;
    }

}
