package com.iac.shipwar.models.dataclass;

import javax.swing.JPanel;

public record PanelCharacteristic(
        int width,
        int height,
        int padding,
        int transparency,
        int rounded,
        int gap,
        int Xposition,
        int Yposition,
        String colorHex,
        JPanel container) {

}
