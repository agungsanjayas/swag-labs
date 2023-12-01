import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.ShareStep
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import org.openqa.selenium.By as By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

WebDriver driver = DriverFactory.getWebDriver()

String firstName = firstName
String lastName = lastName
String postalCode = postalCode
double tax = 3.20

//List<String> products = products
//List<String> prices = prices

Map<String,String> shoppingItem = GlobalVariable.MapShoppingItem

WebUI.scrollToElement(findTestObject('Section Cart/btn_Checkout'), 3)

WebUI.click(findTestObject('Section Cart/btn_Checkout'))

WebUI.setText(findTestObject('Section Checkout/Checkout Information/field_FirstName'), firstName)

WebUI.setText(findTestObject('Section Checkout/Checkout Information/field_LastName'), lastName)

WebUI.setText(findTestObject('Section Checkout/Checkout Information/field_PostalCode'), postalCode)

WebUI.takeScreenshot()

WebUI.click(findTestObject('Section Checkout/Checkout Information/btn_Continue'))

WebUI.takeScreenshot()

CustomKeywords.'com.ShareStep.checkItemChart'(shoppingItem)

WebElement elementPriceTotal = driver.findElement(By.xpath("//div[@class='summary_subtotal_label']"))

double expected_PriceTotal = calculatePrice(shoppingItem)
double actual_PriceTotal = parsePrice(elementPriceTotal.getText())

if (expected_PriceTotal != actual_PriceTotal) {
	throw new AssertionError("Result sum of Price Total is not accurate! Expected: ${expected_PriceTotal} -> Actual: ${actual_PriceTotal}")
}

WebElement elementTotal = driver.findElement(By.xpath("//div[@class='summary_info_label summary_total_label']"))

double expected_Total = expected_PriceTotal + tax
double actual_Total = parsePrice(elementTotal.getText())

if (expected_Total != actual_Total) {
	throw new AssertionError("Result sum of Total is not accurate! Expected: ${expected_Total} -> Actual: ${actual_Total}")
}

WebUI.takeScreenshot()

WebUI.click(findTestObject('Section Checkout/btn_Finish'))

WebUI.scrollToElement(findTestObject('Section Catalog/header_container'), 3)

WebUI.verifyElementPresent(findTestObject('Section Catalog/container_CheckoutComplete'), 3)

WebUI.takeScreenshot()

double calculatePrice(Map<String,String> productPriceMap) {
	List<String> products = productPriceMap.keySet().toList()
	
	double total = 0.0;

	for (int i = 0; i < products.size(); i++) {
		double numericPrice = parsePrice(productPriceMap.get(products[i]));
		total += numericPrice;
	}

	return total;
}

double parsePrice(String price) {
	// Hapus karakter selain angka dan titik desimal, kemudian konversi ke tipe double
	return Double.parseDouble(price.replaceAll("[^\\d.]", ""));
}