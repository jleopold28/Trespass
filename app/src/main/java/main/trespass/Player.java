package main.trespass;

/**
 * Created by Liu on 6/19/2015.
 * This class is a class for Player. It includes the username, avatar number and the secret number of the player.
 * the username is the name for the player.
 * the avatar number represent one of the four avatars, range from 0 to 3.
 * the secret the number is the number player uses to play the game.
 */
public class Player {
    private String username;
    private int avatar;
    private int secretNum;

    public Player() {
        this.username = "Username";
        this.avatar = 0;
        this.secretNum = 0;
    }
    public Player(String aUsername, int aAvatar) {
        this.username = aUsername;
        this.avatar = aAvatar;
    }

    /*
    setter and getter for username
     */
    public void setUsername(String aUsername) {
        this.username = aUsername;
    }
    public String getUsername() {
        return this.username;
    }

    /*
    setter and getter for avatar number
    */
    public void setAvatar(int aAvatar) {
        this.avatar = aAvatar;
    }
    public int getAvatar() {
        return avatar;
    }

    /*
    setter and getter for secret number
    */
    public void setSecretNum(int aSecretNum) {
        this.secretNum = aSecretNum;
    }
    public int getSecretNum() {
        return secretNum;
    }
}
