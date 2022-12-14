package cn.magicalsheep.client.ui;

import cn.magicalsheep.client.ClientMain;
import cn.magicalsheep.client.core.Mode2Worker;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import cn.magicalsheep.client.core.Mode1Worker;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Locale;

public class MainFrame {
    public JPanel root;
    public JButton mode1Button;
    public JButton mode2Button;

    public MainFrame() {
        mode1Button.addActionListener(e -> {
            JFrame frame = new JFrame("功能1：根据中文补齐英文");
            Mode1Frame layout = new Mode1Frame();
            frame.setContentPane(layout.root);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setSize(500, 500);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            ClientMain.mainFrame.setVisible(false);
            new Thread(new Mode1Worker(frame, layout)).start();
        });
        mode2Button.addActionListener(e -> {
            JFrame frame = new JFrame("功能2：根据英文选择中文");
            Mode2Frame layout = new Mode2Frame();
            frame.setContentPane(layout.root);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setSize(500, 500);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            ClientMain.mainFrame.setVisible(false);
            new Thread(new Mode2Worker(frame, layout)).start();
        });
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        root = new JPanel();
        root.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:283px:grow,left:4dlu:noGrow,fill:max(d;4px):grow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):grow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:max(d;4px):grow,top:5dlu:noGrow,center:max(d;4px):noGrow"));
        mode1Button = new JButton();
        Font mode1ButtonFont = this.$$$getFont$$$(null, -1, 20, mode1Button.getFont());
        if (mode1ButtonFont != null) mode1Button.setFont(mode1ButtonFont);
        mode1Button.setText("功能1：根据中文补齐英文");
        CellConstraints cc = new CellConstraints();
        root.add(mode1Button, cc.xy(3, 5));
        mode2Button = new JButton();
        Font mode2ButtonFont = this.$$$getFont$$$(null, -1, 20, mode2Button.getFont());
        if (mode2ButtonFont != null) mode2Button.setFont(mode2ButtonFont);
        mode2Button.setText("功能2：根据英文选择中文");
        root.add(mode2Button, cc.xy(3, 7));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, 28, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setHorizontalAlignment(0);
        label1.setText("六级单词程序");
        root.add(label1, cc.xy(3, 3));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return root;
    }

}
