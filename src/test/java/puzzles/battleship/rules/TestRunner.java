package puzzles.battleship.rules;


import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
    public static void main(String[] args) {
        // Run the tests
        Result result1 = JUnitCore.runClasses(AdjacentShipsContradictionRuleTest.class);
        printTestResults(result1);
        Result result2 = JUnitCore.runClasses(FinishWithShipsDirectRuleTests.class);
        printTestResults(result2);
    }

    private static void printTestResults(Result result) {
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println("Tests run: " + result.getRunCount());
        System.out.println("Tests failed: " + result.getFailureCount());
        System.out.println("All tests passed: " + result.wasSuccessful());
        System.out.println();
    }


}