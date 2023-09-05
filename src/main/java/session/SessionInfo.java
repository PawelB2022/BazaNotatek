package session;

public class SessionInfo
{
    private static SessionInfo instance;
    private int userID;
    private String username;
    private boolean noteEditedBool = false;
    private int editedNoteID = -1;

    private SessionInfo()
    {
        // Private constructor to enforce singleton pattern
    }

    public static SessionInfo getInstance()
    {
        if (instance == null)
        {
            instance = new SessionInfo();
        }
        return instance;
    }

    public int getUserID() { return userID; }

    public void setUserID(int userID) { this.userID = userID; }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public boolean isNoteEditedBool() { return noteEditedBool; }

    public void setNoteEditedBool(boolean noteEditedBool) { this.noteEditedBool = noteEditedBool; }

    public int getEditedNoteID() { return editedNoteID; }

    public void setEditedNoteID(int editedNoteID) { this.editedNoteID = editedNoteID; }

    //Czyszczenie sesji
    public void clearSession() { instance = null; }
}
