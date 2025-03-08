package MyLibs;

public class Session {
    private static Account currentAccount;
    
    public static void setCurrentAccount(Account account) {
        currentAccount = account;
        System.out.println("Session updated: " + (account != null ? account.getUserId() : "null"));
    }
    
    public static Account getCurrentAccount() {
        return currentAccount;
    }
}
