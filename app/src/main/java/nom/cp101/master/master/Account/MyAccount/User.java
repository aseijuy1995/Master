package nom.cp101.master.master.Account.MyAccount;


public class User {
    private String userId, userName, userPassword, userAddress, userTel, userProfile,
            userPortraitBase64, userBackgroundBase64, userProfession;
    private int userGender, userAccess;

    public User() {
    }

    public User(String userName, String userPortraitBase64) {
        this.userName = userName;
        this.userPortraitBase64 = userPortraitBase64;
    }

    public User(String userId, String userName, int type) {
        this.userId = userId;
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getUserPortraitBase64() {
        return userPortraitBase64;
    }

    public void setUserPortraitBase64(String userPortraitBase64) {
        this.userPortraitBase64 = userPortraitBase64;
    }

    public String getUserBackgroundBase64() {
        return userBackgroundBase64;
    }

    public void setUserBackgroundBase64(String userBackgroundBase64) {
        this.userBackgroundBase64 = userBackgroundBase64;
    }

    public int getUserGender() {
        return userGender;
    }

    public void setUserGender(int userGender) {
        this.userGender = userGender;
    }

    public int getUserAccess() {
        return userAccess;
    }

    public void setUserAccess(int userAccess) {
        this.userAccess = userAccess;
    }

    public String getUserProfession() {
        return userProfession;
    }

    public void setUserProfession(String userProfession) {
        this.userProfession = userProfession;
    }
}
