package AdvantageOnlineShop;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AOS_SetUp {

	public static void main(String[] args) throws InterruptedException { 
	
		WebDriver webBrowser = new FirefoxDriver();
		webBrowser.manage().window().maximize();
		//webBrowser.manage().timeouts().implicitlyWait( 10 , TimeUnit.SECONDS );
		
		//navigate to landing page
		String baseUri = "http://demo.proficom.de:8020/#/";
		webBrowser.get( baseUri );
		waitForPageLoad( webBrowser );
			
		//get landing page title
        String actualTitle;
        actualTitle = webBrowser.getTitle();
        System.out.println( "Homepage title is " + actualTitle );
        
        //find element link login
        WebElement webElement_link_login;
        webElement_link_login = findWait( webBrowser, By.id( "menuUser" ) );
        webElement_link_login.click();
        
        //find element input field username
        WebElement webElement_input_username;
        webElement_input_username = findWait( webBrowser, By.name( "username" ) );
        String username = "loadtest1";
        webElement_input_username.sendKeys( username );
        
        //find element input field password
        WebElement webElement_input_password;
        webElement_input_password = findWait( webBrowser, By.name( "password" ) );      
        String password = "TtHWILzgyUh1";
        webElement_input_password.sendKeys( password );
        
        waitForPageLoad( webBrowser );
        Thread.sleep(5000);
        //find element button login
        WebElement webElement_button_login;
        webElement_button_login = findWait( webBrowser, By.id( "sign_in_btnundefined" ) );
        webElement_button_login.click();
        
        //find login verification element
        WebElement webElement_label_loggedin;
        webElement_label_loggedin = findWait( webBrowser, By.cssSelector( "span.hi-user:nth-child(1)" ) );
        
        String loggedin_user;
        loggedin_user = webElement_label_loggedin.getText();
        
        if ( loggedin_user == username )
        	System.out.println( "Logged in as  " + loggedin_user );
        System.out.println( "Login was not successful. |" + loggedin_user + "|" + username + "|" );
        
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
			public Boolean apply( WebDriver webBrowser ) {
				return (( JavascriptExecutor ) webBrowser).executeScript( "return document.readyState" ).toString()
						.equals( "complete" );
			}
		};
		// wait for AngularJS to load
		ExpectedCondition<Boolean> angularLoad = new ExpectedCondition<Boolean>() {
			public Boolean apply( WebDriver webBrowser ) {
				return Boolean.valueOf((( JavascriptExecutor ) webBrowser).executeScript(
						"return (window.angular !== undefined) && (angular.element(document).injector() !== undefined) && (angular.element(document).injector().get('$http').pendingRequests.length === 0)")
						.toString());
			}
		};
		return wait.until( jQueryLoad ) && wait.until( jsLoad ) && wait.until( angularLoad );
	}
}
