import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class webPageObjects {

    public static String selectDropdownOption(WebDriver driver, String optionValue) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        WebElement dropdownMenu = driver.findElement(By.cssSelector(".PlanPricing_packageSelect__3LovE .MuiSelect-root.MuiSelect-selectMenu"));
        wait.until(ExpectedConditions.visibilityOf(dropdownMenu));
        dropdownMenu.click();

        WebElement option = driver.findElement(By.cssSelector("li[data-value='" + optionValue + "']"));
        option.click();

        return optionValue;
    }

    public static WebElement[] roadLength(WebDriver driver) {
        WebElement roadLengthSlider = driver.findElement(By.cssSelector(".PlanPricing_sliders-slider__1d1Cq[data-testid='slider-distance'] .MuiSlider-thumb"));
        WebElement roadLengthRail = driver.findElement(By.cssSelector(".PlanPricing_sliders-slider__1d1Cq[data-testid='slider-distance'] .MuiSlider-rail"));
        return new WebElement[]{roadLengthSlider, roadLengthRail};
    }

    public static WebElement[] intersections(WebDriver driver) {
        WebElement intersectionsSlider = driver.findElement(By.cssSelector("[data-testid='slider-intersections'] .MuiSlider-thumb"));
        WebElement intersectionsRail = driver.findElement(By.cssSelector("[data-testid='slider-intersections'] .MuiSlider-rail"));
        return new WebElement[]{intersectionsSlider, intersectionsRail};
    }

    public static int moveSlider(WebDriver driver, WebElement slider, WebElement rail, int desiredValue) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        wait.until(ExpectedConditions.elementToBeClickable(slider));

        int minValue = Integer.parseInt(slider.getAttribute("aria-valuemin"));
        int maxValue = Integer.parseInt(slider.getAttribute("aria-valuemax"));

        int sliderWidth = rail.getSize().getWidth();
        int distanceToMove = sliderWidth * (desiredValue - minValue) / (maxValue - minValue);

        Actions actions = new Actions(driver);
        actions.clickAndHold(slider).moveByOffset(distanceToMove, 0).release().perform();

        String updatedValue = slider.getAttribute("aria-valuenow");
        int updatedValueInt = Integer.parseInt(updatedValue);

        while (updatedValueInt != desiredValue) {

            if (updatedValueInt > desiredValue) {
                actions.sendKeys(Keys.ARROW_LEFT).perform();
            } else {
                actions.sendKeys(Keys.ARROW_RIGHT).perform();
            }
            updatedValue = slider.getAttribute("aria-valuenow");
            updatedValueInt = Integer.parseInt(updatedValue);
        }

        wait.until(ExpectedConditions.attributeToBe(slider, "aria-valuenow", Integer.toString(desiredValue)));

        Assert.assertEquals(updatedValueInt, desiredValue);
        return updatedValueInt;
    }

    public static int moveRoadLengthSliderMetric(WebDriver driver, int desiredValue) {

        WebElement[] roadLengthElements = roadLength(driver);
        return moveSlider(driver, roadLengthElements[0], roadLengthElements[1], desiredValue);
    }

    public static void moveRoadLengthSliderImperial(WebDriver driver, int desiredValue) {

        WebElement imperialElement = driver.findElement(By.cssSelector("button.switch>div:nth-child(2)"));
        imperialElement.click();

        WebElement[] roadLengthElements = roadLength(driver);
        moveSlider(driver, roadLengthElements[0], roadLengthElements[1], desiredValue);
    }

    public static int moveSignalizedIntersectionsSlider(WebDriver driver, int desiredValue) {

        WebElement[] intersections = intersections(driver);
        return moveSlider(driver, intersections[0], intersections[1], desiredValue);
    }

    public static void setMetricUnit(WebDriver driver) {

        WebElement metricElement = driver.findElement(By.xpath("//button[@class='switch']//div[text()='METRIC']"));
        metricElement.click();
    }

    public static void setImperialUnit(WebDriver driver) {

        WebElement imperialElement = driver.findElement(By.xpath("//button[@class='switch']//div[text()='IMPERIAL']"));
        imperialElement.click();
    }

    public static String getXwayPulse(WebDriver driver) {
        String selectedOption = selectDropdownOption(driver, "X Way Pulse");
        assertSelection(driver, selectedOption);
        return selectedOption;
    }

    public static String getXwayPulseTwin(WebDriver driver) {
        String selectedOption = selectDropdownOption(driver, "X Way (Pulse + Twin)");
        assertSelection(driver, selectedOption);
        return selectedOption;
    }

    public static String getXwayPulseTwinNeural(WebDriver driver) {
        String selectedOption = selectDropdownOption(driver, "X Way (Pulse + Twin + Neural)");
        assertSelection(driver, selectedOption);
        return selectedOption;
    }

    public static void assertSelection(WebDriver driver, String expectedOption) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        WebElement selectedElement = driver.findElement(By.xpath("//div[contains(text(), '" + expectedOption + "')]"));
        wait.until(ExpectedConditions.visibilityOf(selectedElement));

        String actualOption = selectedElement.getText();
        Assert.assertEquals(actualOption, expectedOption);
    }

    public static void setSliderMinValue(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        WebElement[] roadLengthElements = roadLength(driver);
        WebElement roadLengthSlider = roadLengthElements[0];
        WebElement roadLengthRail = roadLengthElements[1];

        WebElement[] intersectionsElements = intersections(driver);
        WebElement intersectionsSlider = intersectionsElements[0];
        WebElement intersectionsRail = intersectionsElements[1];

        wait.until(ExpectedConditions.elementToBeClickable(roadLengthSlider));
        wait.until(ExpectedConditions.elementToBeClickable(intersectionsSlider));

        int roadMinValue = Integer.parseInt(roadLengthSlider.getAttribute("aria-valuemin"));
        int roadMaxValue = Integer.parseInt(roadLengthSlider.getAttribute("aria-valuemax"));
        int intersectionsMinValue = Integer.parseInt(intersectionsSlider.getAttribute("aria-valuemin"));
        int intersectionsMaxValue = Integer.parseInt(intersectionsSlider.getAttribute("aria-valuemax"));

        int roadSliderRailWidth = roadLengthRail.getSize().getWidth();
        int roadCurrentValue = Integer.parseInt(roadLengthSlider.getAttribute("aria-valuenow"));
        int roadDistanceToMove = (roadSliderRailWidth * (roadCurrentValue - roadMinValue) / (roadMaxValue - roadMinValue)) + 2;

        int intersectionsSliderRailWidth = intersectionsRail.getSize().getWidth();
        int intersectionsCurrentValue = Integer.parseInt(intersectionsSlider.getAttribute("aria-valuenow"));
        int intersectionsDistanceToMove = (intersectionsSliderRailWidth * (intersectionsCurrentValue - intersectionsMinValue) / (intersectionsMaxValue - intersectionsMinValue)) + 2;

        Actions actions = new Actions(driver);
        actions.clickAndHold(roadLengthSlider).moveByOffset(-roadDistanceToMove, 0).release().perform();
        actions.clickAndHold(intersectionsSlider).moveByOffset(-intersectionsDistanceToMove, 0).release().perform();
    }
}