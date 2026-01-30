package utilities;

import java.util.ArrayList;

public class Menu<E> {

    private String title;
    private ArrayList<String> options;

    public Menu(String title) {
        this.title = title;
        this.options = new ArrayList<>();
    }

    public void addNewOption(String newOption) {
        this.options.add(newOption);
    }

    // Hàm in menu (không đổi)
    public void print() {
        System.out.println("\n" + title);
        System.out.println("-----------------------------------------------");
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }
        System.out.println("-----------------------------------------------");
    }

    // [CẬP NHẬT] Sử dụng wrapRetry để bắt lỗi nhập liệu
    public int getChoice() {
        return Inputter.wrapRetry(() -> Inputter.getAnInteger(
                "Enter your choice: ",
                "Your choice must be between 1 and " + options.size(),
                1, options.size()
        ));
    }

    // [CẬP NHẬT] Sử dụng wrapRetry để bắt lỗi nhập liệu
    public E getChoiceFromObjectList(ArrayList<E> list) {
        if (list.isEmpty()) {
            System.out.println("The list is empty. No items to choose from.");
            return null;
        }

        // In danh sách object ra trước
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + ". " + list.get(i).toString());
        }

        // Dùng wrapRetry để yêu cầu nhập số thứ tự
        int choice = Inputter.wrapRetry(() -> Inputter.getAnInteger(
                "Enter your choice: ",
                "Your choice must be between 1 and " + list.size(),
                1, list.size()
        ));

        return list.get(choice - 1);
    }
}
