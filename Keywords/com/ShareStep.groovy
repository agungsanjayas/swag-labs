package com

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import internal.GlobalVariable

import com.kms.katalon.core.webui.driver.DriverFactory
import org.openqa.selenium.WebElement
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By as By
import org.openqa.selenium.JavascriptExecutor

public class ShareStep {
	WebDriver driver = DriverFactory.getWebDriver()
	
	/**
	 * Check item chart
	 * @param productPriceMap has been added to chart
	 */
	@Keyword
	def checkItemChart(Map<String, String> productPriceMap) {
		// Mendapatkan daftar produk dari Map
		List<String> products = productPriceMap.keySet().toList()
	
		for (int i = 0; i < products.size(); i++) {
			int indexItem = i + 1
	
			// Pengecekan nama item
			WebElement elementItem = driver.findElement(By.xpath("//div[@class='cart_list']/div[@class='cart_item'][${indexItem}]//div[@class='inventory_item_name']"))
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementItem)
			String itemName = elementItem.getText()
	
			if (!itemName.equals(products[i])) {
				throw new AssertionError("Nama item di keranjang tidak sesuai dengan harga yang diharapkan")
			}
	
			// Pengecekan harga
			WebElement elementPrice = driver.findElement(By.xpath("//div[@class='cart_list']/div[@class='cart_item'][${indexItem}]//div[@class='inventory_item_price']"))
			String priceText = elementPrice.getText()
	
			// Mendapatkan harga dari Map menggunakan nama produk sebagai kunci
			String expectedPrice = productPriceMap.get(products[i])
	
			if (!priceText.equals(expectedPrice)) {
				throw new AssertionError("Harga item di keranjang tidak sesuai dengan harga yang diharapkan")
			}
			WebUI.takeScreenshot()
		}	
	}
}
