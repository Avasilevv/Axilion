import org.openqa.selenium.WebDriver;
public class planPricingMethods {
    final static double weightLength = 0.01;
    final static double weightJunctions = 0.02;
    final static double xwuRef = 5.625;

    //Starter Tier Range
    final static int tMinStarter = 1;
    final static int tMaxStarter = 28;

    //Standard Tier Range
    final static int tMinStandard = 29;
    final static int tMaxStandard = 56;

    //Pro Tier Range
    final static int tMinPro = 57;
    final static int tMaxPro = 1000;

    final static int xWayStarterUnitsIncluded = 14;
    final static int xWayStandardUnitsIncluded = 22;
    final static int xWayProUnitsIncluded = 50;

    private static int xwuFactor(int roadLength, int numberOfSignalizedIntersections) {
        final double trafficComplexity = weightLength * roadLength + weightJunctions * numberOfSignalizedIntersections;
        return (int) Math.ceil(xwuRef * trafficComplexity);
    }
    private static int saasPrice(int xwu, int flatRate, int unitRate, int unitsIncluded) {
        if (xwu <= unitsIncluded) {
            return flatRate;
        } else {
            return flatRate + (xwu - unitsIncluded) * unitRate;
        }
    }
    private static void planDetails(String selectedPlan, int saasPrice, int xwu, int unitsIncluded) {
        int additionalUnits = Math.max(0, xwu - unitsIncluded);
        String additionalUnitsString = additionalUnits > 0 ? " + " + additionalUnits + " X Way Units" : "";
        System.out.println(selectedPlan + additionalUnitsString);
        System.out.println("SaaS Price: $" + saasPrice + " per month");
        System.out.println("------------------------------------");
    }
    private static void setPlanDetails(int xwu, String xWayPulsePlan) {
        int flatRate, unitRate, unitsIncluded;
        String selectedPlan;

        if (xwu >= planPricingMethods.tMinStarter && xwu <= planPricingMethods.tMaxStarter) {
            selectedPlan = xWayPulsePlan + " Starter";
            flatRate = (xWayPulsePlan.contains("Pulse")) ? 980 : ((xWayPulsePlan.contains("Pulse + Twin")) ? 1260 : 1680);
            unitRate = (xWayPulsePlan.contains("Pulse")) ? 70 : ((xWayPulsePlan.contains("Pulse + Twin")) ? 90 : 120);
            unitsIncluded = planPricingMethods.xWayStarterUnitsIncluded;
        } else if (xwu >= planPricingMethods.tMinStandard && xwu <= planPricingMethods.tMaxStandard) {
            selectedPlan = xWayPulsePlan + " Standard";
            flatRate = (xWayPulsePlan.contains("Pulse")) ? 1430 : ((xWayPulsePlan.contains("Pulse + Twin")) ? 1760 : 2530);
            unitRate = (xWayPulsePlan.contains("Pulse")) ? 65 : ((xWayPulsePlan.contains("Pulse + Twin")) ? 80 : 115);
            unitsIncluded = planPricingMethods.xWayStandardUnitsIncluded;
        } else if (xwu >= planPricingMethods.tMinPro && xwu <= planPricingMethods.tMaxPro) {
            selectedPlan = xWayPulsePlan + " Pro";
            flatRate = (xWayPulsePlan.contains("Pulse")) ? 3000 : ((xWayPulsePlan.contains("Pulse + Twin")) ? 3750 : 5250);
            unitRate = (xWayPulsePlan.contains("Pulse")) ? 60 : ((xWayPulsePlan.contains("Pulse + Twin")) ? 75 : 105);
            unitsIncluded = planPricingMethods.xWayProUnitsIncluded;
        } else {
            System.out.println("Out of range");
            return;
        }

        int saasPrice = saasPrice(xwu, flatRate, unitRate, unitsIncluded);
        planDetails(selectedPlan, saasPrice, xwu, unitsIncluded);
    }
    public static void getXWayPulsePlan(WebDriver driver, int roadLengthValue, int signalizedIntersectionsValue) {
        final int roadLength = webPageObjects.moveRoadLengthSliderMetric(driver, roadLengthValue);
        final int numberOfSignalizedIntersections = webPageObjects.moveSignalizedIntersectionsSlider(driver, signalizedIntersectionsValue);
        final int xwu = xwuFactor(roadLength, numberOfSignalizedIntersections);
        String xWayPulsePlan = webPageObjects.getXwayPulse(driver);
        setPlanDetails(xwu, xWayPulsePlan);
    }
    public static void getXWayPulseTwinPlan(WebDriver driver, int roadLengthValue, int signalizedIntersectionsValue) {
        final int roadLength = webPageObjects.moveRoadLengthSliderMetric(driver, roadLengthValue);
        final int numberOfSignalizedIntersections = webPageObjects.moveSignalizedIntersectionsSlider(driver, signalizedIntersectionsValue);
        final int xwu = xwuFactor(roadLength, numberOfSignalizedIntersections);
        String xWayPulseTwinPlan = webPageObjects.getXwayPulseTwin(driver);
        setPlanDetails(xwu, xWayPulseTwinPlan);
    }
    public static void getXWayPulseTwinNeuralPlan(WebDriver driver, int roadLengthValue, int signalizedIntersectionsValue) {
        final int roadLength = webPageObjects.moveRoadLengthSliderMetric(driver, roadLengthValue);
        final int numberOfSignalizedIntersections = webPageObjects.moveSignalizedIntersectionsSlider(driver, signalizedIntersectionsValue);
        final int xwu = xwuFactor(roadLength, numberOfSignalizedIntersections);
        String xWayPulseTwinNeuralPlan = webPageObjects.getXwayPulseTwinNeural(driver);
        setPlanDetails(xwu, xWayPulseTwinNeuralPlan);
    }
}