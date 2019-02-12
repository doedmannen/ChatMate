package server.serverApp;

public class ActiveChannelController {
    private static ActiveChannelController ourInstance = new ActiveChannelController();

    public static ActiveChannelController getInstance() {
        return ourInstance;
    }

    private ActiveChannelController() {
    }
}
