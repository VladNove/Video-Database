package repo.Actions;

import fileio.ActionInputData;
import repo.Repository;

import java.io.IOException;

public abstract class ActionExecutor {
    protected Repository repository;
    ActionExecutor(Repository repository)
    {
        this.repository = repository;
    }
     public abstract void executeAction(ActionInputData actionInputData) throws IOException;
}
