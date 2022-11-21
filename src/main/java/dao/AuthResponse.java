package dao;

public class AuthResponse {

    private int authId;
    private String authUsername;
    private String authSessionToken;
    private String authImageName;
    private String authError;

    public AuthResponse(int authId, String authUsername, String authSessionToken, String authImageName, String authError) {
        this.authId = authId;
        this.authUsername = authUsername;
        this.authSessionToken = authSessionToken;
        this.authImageName = authImageName;
        this.authError = authError;
    }
}
