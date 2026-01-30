package middlewares;

import java.util.ArrayList;
import java.util.List;
import utilities.ErrorHandler;

/**
 * Middleware Chain - Coordinates execution flow and handled errors.
 * Supports both static one-off execution and instance-based reusable chains.
 */
public class MiddlewareChain {

    private final List<Action> actions = new ArrayList<>();

    // --- Builder Pattern for Reusability ---

    /**
     * Adds an action to the chain.
     * 
     * @param action The action to execute.
     * @return The current chain instance for method chaining.
     */
    public MiddlewareChain add(Action action) {
        this.actions.add(action);
        return this;
    }

    /**
     * Executes the built chain of actions sequentially.
     */
    public void execute() {
        perform(() -> {
            for (Action action : actions) {
                action.execute();
            }
        });
    }

    // --- Static Methods for One-off Execution ---

    /**
     * Executes a chain of actions sequentially (Static wrapper).
     * 
     * @param actions List of actions to execute
     */
    public static void run(Action... actions) {
        perform(() -> {
            for (Action action : actions) {
                action.execute();
            }
        });
    }

    /**
     * Execute a single action with error handling.
     * 
     * @param action Action to execute
     */
    public static void perform(Action action) {
        try {
            action.execute();
        } catch (IllegalArgumentException e) {
            System.out.println(">> [VALIDATION ERROR]: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println(">> [NULL POINTER ERROR]: Unexpected null data!");
            e.printStackTrace();
        } catch (Exception e) {
            // Log other errors via ErrorHandler utility
            ErrorHandler.logError(e);
        }
    }
}
