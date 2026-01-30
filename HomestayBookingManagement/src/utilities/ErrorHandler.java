package utilities;

import middlewares.Action;

public class ErrorHandler {

    // --- Centralized Logging ---

    public static void logError(Exception e) {
        System.out.println(">> [ERROR]: " + e.getMessage());
    }

    public static void logError(String message) {
        System.out.println(">> [ERROR]: " + message);
    }

    // --- Action Handlers ---

    public static void handle(Action action) {
        try {
            action.execute();
        } catch (IllegalArgumentException e) {
            System.out.println(">> [VALIDATION ERROR]: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println(">> [NULL POINTER ERROR]: Unexpected null data!");
            e.printStackTrace();
        } catch (Exception e) {
            logError(e);
        }
    }

    // --- Middleware/Chain Pattern ---

    // Express-style: run(validator, controller)
    public static void run(Action... actions) {
        handle(() -> {
            for (Action action : actions) {
                action.execute();
            }
        });
    }

    public static <T> T handleWithReturn(java.util.concurrent.Callable<T> callable, T defaultValue) {
        try {
            return callable.call();
        } catch (Exception e) {
            logError(e);
            return defaultValue;
        }
    }
}
