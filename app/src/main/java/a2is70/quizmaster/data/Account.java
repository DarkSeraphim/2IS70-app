package a2is70.quizmaster.data;

/* Object to store data related to a user's account.
 */
public class Account {

    /* Unique ID given by database. */
    private final int ID;

    /* Full name of user. */
    private String name;

    /* Password hash (in long format(?)).*/
    private long pw_hash;

    /* Account type of user. See enum AccountType*/
    private final AccountType accType;

    /* Email adress of user. Used as login identifier.*/
    private String email;

    /* School associated with user.*/
    private String school;

    /** Default constructor.
     */
    public Account(int ID, String name, AccountType type, String email, String school){
        this.ID = ID;
        this.name = name;
        this.accType = type;
        this.email = email;
        this.school = school;
    }

    /**Method to delete this account from the database.
     */
    public void delete(){

    }

    /**Method to change name of Account.
     * Updates GUI(?)
     * @param in
     */
    public void setName(String in){
        name = in;
    }

    public String getName(){
        return name;
    }

    /**Method to initiate a password reset.
     * Probably opens a window to enter new credentials.
     * Updates database accordingly.
     */
    public void resetPassword(){

    }

    /**Method to change school affiliated with Account.
     * Updates GUI(?)
     * @param in
     */
    public void setSchool(String in){
        school = in;
    }

    public String getSchool(){
        return school;
    }

    public long getHash(){
        return pw_hash;
    }



}
