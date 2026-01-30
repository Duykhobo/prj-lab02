package utilities;

import java.util.List;
import java.util.function.Predicate;

public abstract class GeneralView<T> {

    public void displayMessage(String msg) {
        System.out.println(msg);
    }

    public void displaySuccess(String msg) {
        System.out.println(">> [SUCCESS]: " + msg);
    }

    public void displayError(String err) {
        System.out.println(">> [ERROR]: " + err);
    }

    public boolean confirmContinue() {
        return confirmContinue("Do you want to continue (Y/N)? ");
    }

    public boolean confirmContinue(String message) {
        System.out.println("-----------------------------------------------");
        String choice = Inputter.wrapRetry(() -> Inputter.getString(
                message,
                "Please enter Y or N!",
                Acceptable.YES_NO_VALID));
        return choice.equalsIgnoreCase("Y");
    }

    public String inputCode(String msg, String regex, Predicate<String> checker, String errorMessage,
            boolean negate) {
        return Inputter.wrapRetry(() -> {
            String id = Inputter.getString(msg, "Invalid format!", regex);

            if (negate ? checker.test(id) : !checker.test(id)) {
                throw new Exception(errorMessage);
            }
            return id;
        });
    }

    public String truncate(String value, int width) {
        if (value == null)
            return "";
        return value.length() > width ? value.substring(0, width - 3) + "..." : value;
    }

    public String inputIdForAdd(String msg, String regex, Predicate<String> isDuplicate, String errorMsg) {
        return inputCode(msg, regex, isDuplicate, errorMsg, true);
    }

    public String inputIdForAction(String msg, String regex, Predicate<String> isExist, String errorMsg) {
        return inputCode(msg, regex, isExist, errorMsg, false);
    }

    public abstract void displayList(List<T> list);
}
