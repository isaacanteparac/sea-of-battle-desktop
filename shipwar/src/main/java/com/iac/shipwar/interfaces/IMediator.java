package com.iac.shipwar.interfaces;

import java.util.Map;

import com.iac.shipwar.UI.widgets.Panel_;

public interface IMediator {
    void send(Map<String, Panel_> windowsPanels);
    void receive(String informacion);
}
