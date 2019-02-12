package client.clientApp;

import java.net.Socket;

public class Reciever extends Thread{
    private Socket socket;

    public Reciever(Socket socket){
    this.socket=socket;
    }

    @Override
    public void run() {

    }
}
