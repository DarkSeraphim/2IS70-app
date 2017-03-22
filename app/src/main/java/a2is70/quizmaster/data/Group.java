package a2is70.quizmaster.data;
import java.util.List;

/**Object to represent a group of users (students and teachers combined).*/
public class Group {
    /**Name identifying this group.*/
    private String name;

    /**ID identifying this group (for database).*/
    private final int ID;

    /**List of all members that are part of this group.*/
    private List<Account> members;

    /**Access code for new users to join this group.*/
    private final String accessCode;

    public Group(String name, int ID, String accessCode){
        this.name = name;
        this.ID = ID;
        this.accessCode = accessCode;
    }

    public void setName(String in){
        name = in;
    }

    public String getName(){
        return name;
    }

    public String getAccessCode(){
        return accessCode;
    }
}
