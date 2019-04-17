package AdvantageOnlineShop;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AOS_Selenium_TestNG {

	public WebDriver webBrowser;
	public String baseUri = "http://demo.proficom.de:8020/#/";
	public String actualTitle;
	public String username = "loadtest1";
	
	@BeforeClass
	public void setUp() throws MalformedURLException {
		
		//webBrowser = new FirefoxDriver();
		//webBrowser.manage().window().maximize();
		//webBrowser.manage().timeouts().implicitlyWait( 10 , TimeUnit.SECONDS );
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
    	webBrowser = new RemoteWebDriver( new URL("http://172.16.40.45:4444/wd/hub"), capabilities );
		
	}
	
	@Test
	public void step_01_CallWebsite() {
		
		//navigate to landing page
		webBrowser.get( baseUri );
		waitForPageLoad( webBrowser );
		
		//get landing page title
	    actualTitle = webBrowser
	    				.getTitle()
	    				.trim();
	    //Assert.assertEquals( actualTitle, "Advantage Shopping" );
	    System.out.println( "Homepage title is " + actualTitle );
		
	}
	
	@Test(dependsOnMethods = { "step_01_CallWebsite" })
	public void step_02_SelectLogin() {
	
		//find element link login
	    WebElement webElement_link_login;
	    webElement_link_login = findWait( webBrowser, By.id( "menuUser" ) );
	    webElement_link_login.click();
	    
	}
	
	@Test(dependsOnMethods = { "step_02_SelectLogin" })
	public void step_03_PerformLogin() {
	    
	    //find element input field username
	    WebElement webElement_input_username;
	    webElement_input_username = findWait( webBrowser, By.name( "username" ) );
	    webElement_input_username.sendKeys( username );
	    
	    //find element input field password
	    WebElement webElement_input_password;
	    webElement_input_password = findWait( webBrowser, By.name( "password" ) );      
	    String password = "TtHWILzgyUh1";
	    webElement_input_password.sendKeys( password );
	    
	    waitForPageLoad( webBrowser );
	    //find element button login
	    WebElement webElement_button_login;
	    webElement_button_login = findWait( webBrowser, By.id( "sign_in_btnundefined" ) );
	    waitForClickable( webElement_button_login, 15 );
	    webElement_button_login.click();
	    
	}
    
	@Test(dependsOnMethods = { "step_03_PerformLogin" })
	public void step_04_VerifyLogon() {
	    
		//find login verification element
		String loggedin_user;
	    WebElement webElement_label_loggedin;
	    
	    webElement_label_loggedin = findWait( webBrowser, By.cssSelector( "span.hi-user:nth-child(1)" ) );
	    loggedin_user = webElement_label_loggedin
	    					.getText()
	    					.trim();
	    
	    Assert.assertEquals( loggedin_user ,  username );
	    
	}
	
    @AfterClass
    public void tearDown() {
    	
    	//close browser
        webBrowser.close();
    	
    }

	public static WebElement findWait( WebDriver webBrowser, By locator ) { 
    	Wait<WebDriver> wait = new FluentWait<WebDriver>( webBrowser )
	            .withTimeout( 30, TimeUnit.SECONDS )
	            .pollingEvery( 10, TimeUnit.SECONDS )
	            .ignoring( NoSuchElementException.class );
	    WebElement foo = wait.until( new Function<WebDriver, WebElement>() {
	        public WebElement apply( WebDriver webBrowser ) {
	            return webBrowser.findElement( locator );
	        }
	    });
	    return  foo;
    }
	
	public static boolean waitForPageLoad( WebDriver webBrowser ) {
		WebDriverWait wait = new WebDriverWait( webBrowser, 15 );
		// wait for jQuery to load
		ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply( WebDriver driver ) {
				try {
					return ((Long) (( JavascriptExecutor ) webBrowser).executeScript( "return jQuery.active" ) == 0);
				} catch ( Exception e ) {
					// no jQuery present
					return true;
				}
			}
		};
		// wait for Javascript to load
		ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply( WebDriver webBrowser ) {
				return (( JavascriptExecutor ) webBrowser).executeScript( "return document.readyState" ).toString()
						.equals( "complete" );
			}
		};
		// wait for AngularJS to load
		ExpectedCondition<Boolean> angularLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply( WebDriver webBrowser ) {
				return Boolean.valueOf((( JavascriptExecutor ) webBrowser).executeScript(
						"return (window.angular !== undefined) && (angular.element(document).injector() !== undefined) && (angular.element(document).injector().get('$http').pendingRequests.length === 0)")
						.toString());
			}
		};
		return wait.until( jQueryLoad ) && wait.until( jsLoad ) && wait.until( angularLoad );
	}
	
	public void waitForClickable( WebElement webElement, int timer ) {
		WebDriverWait exists = new WebDriverWait( webBrowser, timer );
		exists.until( ExpectedConditions.refreshed( ExpectedConditions.elementToBeClickable( webElement ) ) );
	}
	
}
