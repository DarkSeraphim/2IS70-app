package a2is70.quizmaster.database;

/**Callback interface for database.*/
public interface DBCallback<T> {

    /**
     * Callback method.
     * @param data requested data from database. null if not applicable.*/
    public void execute(T data);
}
