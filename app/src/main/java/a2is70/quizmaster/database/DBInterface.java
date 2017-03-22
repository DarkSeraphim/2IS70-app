package a2is70.quizmaster.database;
import a2is70.quizmaster.data.*;

/**Interface to facilitate communication with database.*/
public interface DBInterface {

    /**Method to retrieve account data for authentication.*/
    public void loadAccount(Account a, DBCallback b);

    /**Method to add new account to database.*/
    public void addAccount(Account a, DBCallback b);

    /**Method to edit existing account in database.*/
    public void editAccount(Account a, DBCallback b);

    /**Method to delete an existing account from database.*/
    public void deleteAccount(Account a, DBCallback b);

    /**Method to retrieve group data for one account (to list in groupActivity).*/
    public void loadGroups(Account a, DBCallback b);

    /**Method to retrieve Quiz data for one account (to list in overviewActivity).*/
    public void loadQuizzes(Account a, DBCallback b);

    /**Method to join group data.*/
    public void joinGroup(Group g, DBCallback b);

    /**Method to edit group.*/
    public void editGroup(Group g, DBCallback b);

    /**Method to leave group.*/
    public void leaveGroup(Group g, DBCallback b);

    /**Method to create a new Quiz.*/
    public void addQuiz(Quiz q, DBCallback b);

    /**Method to edit an existing Quiz.*/
    public void editQuiz(Quiz q, DBCallback b);

    /**Method to delete an existing Quiz.*/
    public void deleteQuiz(Quiz q, DBCallback b);
}
