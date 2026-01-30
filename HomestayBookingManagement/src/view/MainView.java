package view;

import utilities.Menu;

public class MainView {

    private Menu<String> mainMenu;

    public MainView() {
        mainMenu = new Menu<>("HOMESTAY BOOKING MANAGEMENT");

        mainMenu.addNewOption("Add a new Tour");

        mainMenu.addNewOption("Update a Tour by ID");

        mainMenu.addNewOption("List Tours (Departure < Today)");

        mainMenu.addNewOption("List Tours (Departure > Today) [Sorted by Amount]");

        mainMenu.addNewOption("Add a new Booking");

        mainMenu.addNewOption("Remove a Booking by ID");

        mainMenu.addNewOption("Update a Booking by ID");

        mainMenu.addNewOption("Search Booking by customer name");

        mainMenu.addNewOption("Statistics (Tourists per Homestay)");

        mainMenu.addNewOption("Quit");
    }

    // Hàm hiển thị và lấy lựa chọn Menu chính
    public int getMainMenuChoice() {
        mainMenu.print();
        return mainMenu.getChoice();
    }
}
