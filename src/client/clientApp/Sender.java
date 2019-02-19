package client.clientApp;

import models.Message;
import models.Sendable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class Sender extends Thread {
    private Socket socket;
    private static ObjectOutputStream objectOutputStream;
    private LinkedBlockingDeque<Sendable> outbox;

    public Sender(Socket socket) {
        this.socket = socket;
        outbox = new LinkedBlockingDeque<>();
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("failed to create stream");
            Client.getInstance().isRunning = false;
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

    public boolean hasMessagesTosend(){
        return outbox.size() > 0;
    }

    @Override
    public void run() {
        System.out.println("Sender is running");
        while (Client.getInstance().isRunning) {
            while (hasMessagesTosend()){
                Sendable m = outbox.getFirst();
                try{
                    objectOutputStream.writeObject(m);  // Try to send first sendable
                    outbox.removeFirst();               // Remove if sent
                } catch (Exception e){

                }
            }
            try{
                Thread.sleep(1);
            }catch (Exception e){

            }
        }

    }

}
