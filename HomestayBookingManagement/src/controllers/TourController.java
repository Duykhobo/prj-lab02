/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.util.List;

import middlewares.MiddlewareChain;
import models.Tour;
import services.HomestayService;
import services.TourService;
import utilities.Acceptable;
import view.TourView;

/**
 *
 * @author ThanhDuy
 */
public class TourController {

    private TourService tourService;
    private HomestayService homestayService;
    private TourView view;

    public TourController(TourService tourService, HomestayService homestayService) {
        this.tourService = tourService;
        this.homestayService = homestayService;
        this.view = new TourView();
    }

    public void addTour() {
        view.displayMessage("----- ADD NEW TOUR -----");
        do {
            MiddlewareChain.perform(() -> { // Middleware Wrapper
                String id = view.inputIdForAdd("Enter Tour ID (Txxxxx):", Acceptable.TOUR_ID_VALID,
                        checkId -> tourService.getById(checkId) != null, "This tour already exists.");
                Tour newTour = view.inputTourDetails(id, homeId -> homestayService.getById(homeId) != null);

                // Delegate to Service
                if (tourService.addTourWithValidation(newTour)) {
                    view.displayMessage("Tour added successfully!");
                    tourService.saveToFile();
                    view.displayMessage("Tour data saved successfully!");
                } else {
                    view.displayError("Failed to add tour. Check errors above (Capacity/Overlap).");
                }
            });
        } while (view.confirmContinue());
    }

    void updateTour() {
        // phương thức cập nhật
        view.displayMessage("----- UPDATE TOUR -----");
        do {
            MiddlewareChain.perform(() -> { // Middleware Wrapper
                String id = view.inputIdForAction("Enter Tour ID (Txxxxx):", Acceptable.TOUR_ID_VALID,
                        checkId -> tourService.getById(checkId) != null, "This tour does not exist.");
                Tour oldTour = tourService.getById(id);
                if (oldTour == null) { // Double check inside handle though predicate handles it primarily
                    // Predicate negation handles "this tour does not exist" message.
                    // But logic flow keeps going.
                    // Wait, inputIdForAction throws if valid? No.
                    // If inputIdForAction returns, ID exists in map.
                }

                Tour newTour = view.inputUpdateDetails(oldTour);
                if (newTour != null) {
                    // Delegate to Service
                    if (tourService.updateTourWithValidation(newTour, oldTour)) {
                        view.displayMessage("Tour updated successfully!");
                        tourService.saveToFile();
                        view.displayMessage("Tour data saved successfully!");
                    } else {
                        view.displayError("Failed to update tour. Check validation errors.");
                    }
                }
            });
        } while (view.confirmContinue());
    }

    void listLaterThanToday() {
        // phương thức liệt kê
        view.displayMessage("----- LIST LATER THAN TODAY -----");
        try {
            List<Tour> tours = tourService.getLaterThanToday();
            if (tours.isEmpty()) {
                view.displayMessage("No tours found with departure date later than today.");
            } else {
                // Sort by total amount descending
                tours.sort((t1, t2) -> Double.compare(t2.getTotalAmount(), t1.getTotalAmount()));
                view.displayList(tours);
            }
        } catch (Exception e) {
            view.displayError(e.getMessage());
        }
    }

    void showStatistics() {
        // phương thức hiển thị thống kê
        view.displayMessage("----- SHOW STATISTICS -----");
        try {
            // Triển khai logic thống kê ở đây
            Object[][] stats = tourService.getStatistics();
            if (stats == null || stats.length == 0) {
                view.displayMessage("No statistics data available.");
            } else {
                view.displayStatistics(stats);
            }
        } catch (Exception e) {
            view.displayError(e.getMessage());
        }
    }

    void listEarlierThanToday() {
        view.displayMessage("----- LIST EARLIER THAN TODAY -----");
        try {
            List<Tour> tours = tourService.getAll().stream()
                    .filter(t -> t.getDepartureDate().isBefore(java.time.LocalDate.now()))
                    .collect(java.util.stream.Collectors.toList());
            if (tours.isEmpty()) {
                view.displayMessage("No tours found with departure date earlier than today.");
            } else {
                view.displayList(tours);
            }
        } catch (Exception e) {
            view.displayError(e.getMessage());
        }
    }
}
