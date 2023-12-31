package com.iac.shipwar.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;

import com.iac.shipwar.UI.layout.UiDashboard;
import com.iac.shipwar.controllers.ShipDeployed;
import com.iac.shipwar.controllers.Singleton;
import com.iac.shipwar.interfaces.IGame;
import com.iac.shipwar.models.enums.Dashboard;

//sender
public class JoinGame implements IGame {
    private int port;
    private String host = "127.0.0.1";
    private final int bufferSize = 10 * 1024 * 1024;// 10mb
    private DatagramSocket dtSocket;
    private byte[] buffer;
    private DatagramPacket dtPacket;
    private Boolean serverListening;
    private Boolean attackComponent = true;
    private UiDashboard dashboard;

    protected final Singleton singleton = Singleton.getInstance();

    public JoinGame(String http, UiDashboard dashboard) throws Exception {
        this.dashboard = dashboard;
        dtSocket = new DatagramSocket();
        System.out.println("- - - join game - - -");
        if (verifyUrl(http) || verifyPort(http)) {
            System.out.println("- - - connected - - -");
            start();
            startListening();
        } else {
            end();
            System.out.println("- - - not found - - -");
        }

    }

    private boolean verifyPort(String http) {
        try {
            if (String.valueOf(http).length() == 4) {
                this.port = Integer.parseInt(http);
                System.out.println("port: " + this.port);
                return true;
            } else {
                System.out.println("El puerto debe tener exactamente cuatro dígitos.");
                return false;
            }
        } catch (NumberFormatException e) {
            this.port = 0;
            System.out.println("La cadena no representa un número válido.");
            return false;
        }
    }

    private boolean verifyUrl(String urlString) {
        try {
            if (!urlString.startsWith("http://") && !urlString.startsWith("https://")) {
                urlString = "http://" + urlString;
            }
            URI uri = new URI(urlString);
            if (uri.getScheme() != null && uri.getHost() != null && uri.getPort() != -1) {
                this.host = uri.getHost();
                this.port = uri.getPort();
                System.out.println("\nLa URL es válida.");
                System.out.println("Scheme: " + uri.getScheme());
                System.out.println("Host: " + uri.getHost());
                System.out.println("Port: " + uri.getPort());
                return true;
            } else {
                return false;
            }
        } catch (URISyntaxException e) {
            System.out.println("La URL no es válida.");
            e.printStackTrace();
            return false;
        }
    }

    public void startListening() {
        Thread listenerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (serverListening) {
                    receiveData();

                }
            }
        });
        listenerThread.start();
    }

    @Override
    public ShipDeployed receiveData() {
        try {
            this.buffer = new byte[this.bufferSize];
            this.dtPacket = new DatagramPacket(this.buffer, this.bufferSize);
            this.dtSocket.receive(this.dtPacket);
            ByteArrayInputStream bis = new ByteArrayInputStream(this.buffer);
            ObjectInputStream in = new ObjectInputStream(bis);
            ShipDeployed receivedData = (ShipDeployed) in.readObject();
            this.singleton.receiveAttack(receivedData);
            this.attackComponent = true;
            String text = "Enemigo: " + receivedData.getRow().name() + " " + receivedData.getColumn().name();
            addText(text, dashboard.getBox(Dashboard.SHOOTINGLOG), "#Fc045b");
            return receivedData;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public DatagramPacket sendData(ShipDeployed content) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(content);
            byte[] data = bos.toByteArray();
            this.dtPacket = new DatagramPacket(data, data.length,
                    InetAddress.getByName(this.host),
                    this.port);
            content.printDetails("MIO");
            dtSocket.send(dtPacket);
            dtSocket.send(dtPacket);
            this.attackComponent = false;
            String text = "Yo: " + content.getRow().name() + " " + content.getColumn().name();
            addText(text, dashboard.getBox(Dashboard.SHOOTINGLOG), "#045bfc");
            return dtPacket;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public boolean getAttackComponent() {
        return attackComponent;
    }

    public void setServerListening(boolean open) {
        this.serverListening = open;
    }

    @Override
    public String getPort() {
        return "" + this.host + ":" + this.port;
    }

    @Override
    public void start() {
        this.serverListening = true;
    }

    @Override
    public void end() {
        this.serverListening = false;

    }

    @Override
    public boolean getServerListening() {
        return this.serverListening;
    }

}
