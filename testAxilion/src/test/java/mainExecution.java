import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Random;

public class mainExecution {

    private WebDriver driver;
    private final Dimension screenSize = new Dimension(1280, 800);
    private static final Random random = new Random();

    private int randomInt() {
        return random.nextInt(1000) + 1;
    }

    interface TestPlanFunction {
        void apply(WebDriver driver, int roadLength, int intersections);
    }

    private void setSliderMetricValues(int roadLength, int intersections) {
        webPageObjects.moveRoadLengthSliderMetric(driver, roadLength);
        webPageObjects.moveSignalizedIntersectionsSlider(driver, intersections);
    }

    private void setRoadLengthImperialValues(int roadLength, int intersections) {
        webPageObjects.moveRoadLengthSliderImperial(driver, roadLength);
        webPageObjects.moveSignalizedIntersectionsSlider(driver, intersections);
    }

    private void testPlanWithValues(TestPlanFunction testPlan, int roadLength, int intersections) {
        testPlan.apply(driver, roadLength, intersections);
    }

    @BeforeTest
    public void setUp() {
        driver = new ChromeDriver();
        driver.get("https://axilion.z6.web.core.windows.net/#/");
        driver.manage().window().setSize(screenSize);
        System.out.println("------------Test results------------");
    }

    //Initial sliders test

    @Test (priority = 1)
    public void testSliders() {
        setSliderMetricValues(1000, 1000);
        webPageObjects.setSliderMinValue(driver);
        setSliderMetricValues(randomInt(), randomInt());
        webPageObjects.setSliderMinValue(driver);
    }

    //Initial Dropdown menu test

    @Test (priority = 2)
    public void testDropdown() {
        webPageObjects.getXwayPulse(driver);
        webPageObjects.getXwayPulseTwin(driver);
        webPageObjects.getXwayPulseTwinNeural(driver);
    }

    @Test (priority = 3)
    public void testImperialUnit() {
        webPageObjects.setImperialUnit(driver);
        setRoadLengthImperialValues(randomInt(), randomInt());
        webPageObjects.setSliderMinValue(driver);
        setRoadLengthImperialValues(randomInt(), randomInt());
        webPageObjects.setSliderMinValue(driver);
        webPageObjects.setMetricUnit(driver);
    }

    //Verify and validate Suggested plan and SaaS price calculations

    //Test with minimal values

    @Test(priority = 4)
    public void testPlansWithMinimalValues() {
        testPlanWithValues(planPricingMethods::getXWayPulsePlan, 1, 1);
        testPlanWithValues(planPricingMethods::getXWayPulseTwinPlan, 1, 1);
        testPlanWithValues(planPricingMethods::getXWayPulseTwinNeuralPlan, 1, 1);
        webPageObjects.setSliderMinValue(driver);
    }

    //Test with maximum values

    @Test(priority = 5)
    public void testPlansWithMaximumValues() {
        testPlanWithValues(planPricingMethods::getXWayPulsePlan, 1000, 1000);
        testPlanWithValues(planPricingMethods::getXWayPulseTwinPlan, 1000, 1000);
        testPlanWithValues(planPricingMethods::getXWayPulseTwinNeuralPlan, 1000, 1000);
        webPageObjects.setSliderMinValue(driver);
    }

    //Random Slider Value test

    @Test(priority = 6)
    public void plansWithRandomSliderValues() {
        testPlanWithValues(planPricingMethods::getXWayPulsePlan, randomInt(), randomInt());
        webPageObjects.setSliderMinValue(driver);
        testPlanWithValues(planPricingMethods::getXWayPulseTwinPlan, randomInt(), randomInt());
        webPageObjects.setSliderMinValue(driver);
        testPlanWithValues(planPricingMethods::getXWayPulseTwinNeuralPlan, randomInt(), randomInt());
        webPageObjects.setSliderMinValue(driver);
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }
}