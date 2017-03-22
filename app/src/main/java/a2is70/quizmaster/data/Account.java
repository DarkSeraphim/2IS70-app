package a2is70.quizmaster.data;

import a2is70.quizmaster.database.DBInterface;

/* Object to store data related to a user's account.
 */
public class Account {

    /* Unique ID given by database. */
    private final int ID;

    /* Full name of user. */
    private String name;

    /* Account type of user. See enum AccountType*/
    private final AccountType accType;

    /* Email adress of user. Used as login identifier.*/
    private String email;

    /* School associated with user.*/
    private String school;

    /*DataBase API object.*/
    DBInterface dbi;

    /** Default constructor.
     */
    public Account(int ID, String name, AccountType type, String email, String school){
        this.ID = ID;
        this.name = name;
        this.accType = type;
        this.email = email;
        this.school = school;
    }

    /**Method to add this account to the database (if it does not already exist).
     */
    public void create(String password){
        dbi.addAccount(email, password);
    }

    /**Method to delete this account from the database.
     */
    public void delete(){
        dbi.deleteAccount(this);
    }

    /**Method to change name of Account.
     * Updates GUI(?)
     * @param in
     */
    public void setName(String in){
        name = in;
        dbi.editAccount(this);
    }

    public String getName(){
        return name;
    }

    /**Method to initiate a password reset.
     * Probably opens a window to enter new credentials.
     * Updates database accordingly.
     */
    public void resetPassword(){
        //TODO
    }

    /**Method to change school affiliated with Account.
     * Updates GUI(?)
     * @param in
     */
    public void setSchool(String in){
        school = in;
        dbi.editAccount(this);
    }

    public String getSchool(){
        return school;
    }

    public AccountType getAccType(){
        return accType;
    }
}
