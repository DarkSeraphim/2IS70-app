package a2is70.quizmaster.database;

/**Callback interface for database.*/
public interface DBCallback<T> {

    /**
     * Callback method with status code.
     * @param data requested data from database. null if not applicable.
     * @param isOK true iff database request was successful.
     */
    public void execute(T data, boolean isOK);
}
