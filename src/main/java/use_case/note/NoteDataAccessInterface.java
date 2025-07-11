package use_case.note;

import entity.User;

/**
 * Interface for the NoteDAO. It consists of methods for
 * both loading and saving a note.
 */
public interface NoteDataAccessInterface {

    /**
     * Saves a note for a given user. This will replace any existing note.
     * <p>The password of the user must match that of the user saved in the system.</p>
     * @param user the user information associated with the note
     * @param note the note to be saved
     * @return the contents of the note
     * @throws DataAccessException if the user's note can not be saved for any reason
     */
    String saveNote(User user, String note) throws DataAccessException;

    /**
     * Returns the note associated with the user. The password
     * is not checked, so anyone can read the information.
     * @param user the user information associated with the note
     * @return the contents of the note
     * @throws DataAccessException if the user's note can not be loaded for any reason
     */
    String loadNote(User user) throws DataAccessException;

    //TODO
    // this method overlaps with the one in the LoginUserDataAccessInterface but this is just an example
    // so ill keep it for now and we can fix it later
    /**
     * Returns the user with the given username.
     * @param username the username to look up
     * @return the user with the given username
     */
    User get(String username);
}
