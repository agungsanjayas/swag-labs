import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
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

def Number parsePrice(String priceText) {
	return Double.parseDouble(priceText.replaceAll("[^\\d.]", ""));
}

WebDriver driver = DriverFactory.getWebDriver()
List<String> products = products
List<String> prices = []

if (products.size() == 0) {
	KeywordUtil.markWarning("There's no item in cart")
} else {
	
	Map<String, String> productPriceMap = [:]
	
	for (product in products) {
		TestObject btn_atcProductObj = new TestObject()
		String suffixId = "add-to-cart-"
		btn_atcProductObj.addProperty("id", ConditionType.EQUALS, suffixId + product.replaceAll(" ", "-").toLowerCase())
		
		WebUI.click(btn_atcProductObj)
		
		WebElement elementPrice = driver.findElement(By.xpath("//div[text()='${product}']/ancestor::div[3]//div[@class='inventory_item_price']"))
		String priceText = elementPrice.getText()
		prices.add(priceText)
		
		productPriceMap.put(product, priceText)
	}

	String getTotalCart = WebUI.getText(findTestObject('Section Catalog/icon_Cart'))
	int totalCart = getTotalCart.toInteger()
	
	if (totalCart != products.size()) {
		throw new AssertionError("There's items that aren't added to cart")
	} else {
		GlobalVariable.MapShoppingItem = productPriceMap
	}
}

WebUI.takeScreenshot()

