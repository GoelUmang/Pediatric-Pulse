package application;

public class UserSession
{
    private static UserSession instance;
    private String username;
    private String role;

    private UserSession(String username, String role) {
        this.username = username;
        this.role = role;
    }

    // Overloaded method for instance initialization
    public static UserSession getInstance(String username, String role) {
        if (instance == null) {
            instance = new UserSession(username, role);
        }
        return instance;
    }

    // Method to get the current instance without new parameters
    public static UserSession getInstance() {
        if (instance == null) {
            throw new IllegalStateException("UserSession not initialized");
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}
