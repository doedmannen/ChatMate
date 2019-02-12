package models;

import java.io.Serializable;

public class User implements Serializable, Comparable<User> {

    @Override
    public int compareTo(User o) {
        return 0;
    }
}
