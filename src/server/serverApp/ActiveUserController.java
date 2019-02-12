package server.serverApp;

public class ActiveUserController {
    private static ActiveUserController ourInstance = new ActiveUserController();

    public static ActiveUserController getInstance() {
        return ourInstance;
    }

    private ActiveUserController() {
    }
}
