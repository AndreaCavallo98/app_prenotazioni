package dao;

public class AuthResponse {

    private int authId;

    private String nameUsername;
    private String authUsername;

    private String email;
    private String jwtToken;
    private String authImageName;
    private String authError;

    public AuthResponse(int authId, String nameUsername, String authUsername, String email, String jwtToken, String authImageName, String authError) {
        this.authId = authId;
        this.nameUsername = nameUsername;
        this.authUsername = authUsername;
        this.email = email;
        this.jwtToken = jwtToken;
        this.authImageName = authImageName;
        this.authError = authError;
    }
}
