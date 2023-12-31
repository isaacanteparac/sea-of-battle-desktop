package com.iac.shipwar.UI.widgets;

import javax.swing.JButton;

import com.iac.shipwar.models.enums.Ui;

import java.awt.*;

public class Button_ {
    protected JButton button;
    protected String text;
    protected String colorHex = Ui.DEFAULT_BUTTON_COLOR.getStringValue();
    protected int width = Ui.BUTTON_WIDTH_DEFAULT.getIntValue();
    protected int height = Ui.BUTTON_HEIGHT_DEFAULT.getIntValue();
    protected int rounded = 45;

    public Button_() {
        this.button = new JButton(this.text) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.decode(colorHex));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), rounded, rounded);
                g2d.dispose();
            }
        };
        size();
    }

    private void size() {
        this.button.setOpaque(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFocusable(false);
        button.setContentAreaFilled(false);
        this.button.setPreferredSize(new Dimension(width, height));
    }

    public void setColorBg(String hex) {
        this.colorHex = hex;
        this.button.setBackground(Color.decode(hex));

    }

    public void setWidth(int w) {
        this.width = w;
        this.button.setPreferredSize(new Dimension(width, height));

    }

    public void setHeight(int h) {
        this.height = h;
        this.button.setPreferredSize(new Dimension(width, height));

    }

    public void setRounded(int r) {
        this.rounded = r;
    }

    public void add(Component component) {
        this.button.setLayout(new GridBagLayout());
        this.button.add(component);
    }

    public JButton geButton() {
        return this.button;
    }
}
