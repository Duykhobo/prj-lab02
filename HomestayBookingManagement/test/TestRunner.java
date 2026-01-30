

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Test Runner for Homestay Booking Management System
 * Executes all unit tests and provides summary
 */
public class TestRunner {

    // Java 8 compatible repeat method
    private static String repeat(String s, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(s);
        }
        return sb.toString();
    }

    public static void main(String[] args) {

        System.out.println(repeat("=", 80));
        System.out.println("HOMESTAY BOOKING MANAGEMENT SYSTEM - UNIT TEST EXECUTION");
        System.out.println(repeat("=", 80));

        // Array of all test classes
        Class<?>[] testClasses = {
                BookingTest.class,
                TourTest.class,
                HomestayTest.class,
                TourStatusTest.class,
                DateValidatorTest.class,
                TimeValidatorTest.class,
                ValidationTest.class,
                BookingServiceTest.class,
                TourServiceTest.class,
                HomestayServiceTest.class
        };

        int totalTests = 0;
        int totalFailures = 0;
        long totalTime = 0;

        // Run each test class
        for (Class<?> testClass : testClasses) {
            System.out.println("\n" + repeat("-", 60));
            System.out.println("Running: " + testClass.getSimpleName());
            System.out.println(repeat("-", 60));

            Result result = JUnitCore.runClasses(testClass);

            totalTests += result.getRunCount();
            totalFailures += result.getFailureCount();
            totalTime += result.getRunTime();

            System.out.printf(
                    "Tests run: %d, Failures: %d, Time: %.3f sec%n",
                    result.getRunCount(),
                    result.getFailureCount(),
                    result.getRunTime() / 1000.0);

            // Print failure details
            if (result.getFailureCount() > 0) {
                System.out.println("\nFAILURES:");
                for (Failure failure : result.getFailures()) {
                    System.out.println("  - " + failure.getTestHeader());
                    System.out.println("    " + failure.getMessage());
                }
            } else {
                System.out.println("✓ All tests passed!");
            }
        }

        // Overall summary
        System.out.println("\n" + repeat("=", 80));
        System.out.println("OVERALL TEST SUMMARY");
        System.out.println(repeat("=", 80));
        System.out.printf("Total Test Classes: %d%n", testClasses.length);
        System.out.printf("Total Tests: %d%n", totalTests);
        System.out.printf("Total Failures: %d%n", totalFailures);

        if (totalTests > 0) {
            System.out.printf(
                    "Success Rate: %.1f%%%n",
                    ((totalTests - totalFailures) * 100.0 / totalTests));
        }

        System.out.printf("Total Execution Time: %.3f seconds%n", totalTime / 1000.0);

        if (totalFailures == 0) {
            System.out.println("\nALL TESTS PASSED! The system is ready for deployment.");
        } else {
            System.out.println("\nSome tests failed. Please review and fix the issues.");
        }

        System.out.println(repeat("=", 80));
    }
}
