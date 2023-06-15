package puzzles.battleship.rules;


import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import puzzles.lightup.rules.*;

public class TestRunner {
    public static void main(String[] args) {
        // Battleship Tests
        Result result1 = JUnitCore.runClasses(AdjacentShipsContradictionRuleTest.class);
        printTestResults(result1);
        Result result2 = JUnitCore.runClasses(FinishWithShipsDirectRuleTests.class);
        printTestResults(result2);

        // Lightup Tests
        Result result3 = JUnitCore.runClasses(BulbsInPathContradictionRuleTest.class);
        printTestResults(result3);
        Result result4 = JUnitCore.runClasses(CannotLightACellContradictionRuleTest.class);
        printTestResults(result4);
        Result result5 = JUnitCore.runClasses(EmptyCellinLightDirectRuleTest.class);
        printTestResults(result5);
        Result result6 = JUnitCore.runClasses(EmptyCornersDirectRuleTest.class);
        printTestResults(result6);
        Result result7 = JUnitCore.runClasses(FinishWithBulbsDirectRuleTest.class);
        printTestResults(result7);
        Result result8 = JUnitCore.runClasses(LightOrEmptyCaseRuleTest.class);
        printTestResults(result8);
        Result result9 = JUnitCore.runClasses(MustLightDirectRuleTest.class);
        printTestResults(result9);
        Result result10 = JUnitCore.runClasses(SatisfyNumberCaseRuleTest.class);
        printTestResults(result10);
        Result result11 = JUnitCore.runClasses(TooFewBulbsContradictionRuleTest.class);
        printTestResults(result11);
        Result result12 = JUnitCore.runClasses(TooManyBulbsContradictionRuleTest.class);
        printTestResults(result12);

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