package client.clientApp.network;

import client.clientApp.Client;
import models.Message;
import models.Sendable;
import models.Encryption;

import javax.crypto.SealedObject;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;

public class Sender extends Thread {
    private Socket socket;
    private static ObjectOutputStream objectOutputStream;
    private LinkedBlockingDeque<Sendable> outbox;
    private Encryption encrypt;


    public Sender(Socket socket) {
        this.socket = socket;
        this.encrypt = new Encryption();
        outbox = new LinkedBlockingDeque<>();
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Failed to create stream");
            Client.getInstance().setIsRunning(false);
            // todo kolla om server är död, prova återanslutning
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToServer(Sendable o) {
        if (o instanceof Message) {
            outbox.add(o);
        }
    }

    private boolean hasMessagesTosend() {
        return outbox.size() > 0;
    }

    @Override
    public void run() {
        while (Client.getInstance().isRunning()) {
            while (hasMessagesTosend()) {
                Sendable m = outbox.getFirst();
                SealedObject encryptedObject = encrypt.encryptObject(m);
                try {
                    objectOutputStream.reset();
                    objectOutputStream.writeObject(encryptedObject);  // Try to send first sendable
                    outbox.removeFirst();               // Remove if sent
                } catch (Exception e) {
                  e.printStackTrace();
                }
            }
            try {
                Thread.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}