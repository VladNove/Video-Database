package repo.Actions;

import fileio.ActionInputData;
import repo.Repository;

import java.io.IOException;


/**
 * Abstract class for objects that can execute actions
 */
public abstract class ActionExecutor {
    protected final Repository repository;

    ActionExecutor(final Repository repository) {
        this.repository = repository;
    }

    /** executes action */
    public abstract void executeAction(ActionInputData actionInputData) throws IOException;
}
