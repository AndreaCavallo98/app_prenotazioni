package dao;

public class AuthResponse {

    private int authId;
    private String authUsername;
    private String jwtToken;
    private String authImageName;
    private String authError;

    public AuthResponse(int authId, String authUsername, String jwtToken, String authImageName, String authError) {
        this.authId = authId;
        this.authUsername = authUsername;
        this.jwtToken = jwtToken;
        this.authImageName = authImageName;
        this.authError = authError;
    }
}
