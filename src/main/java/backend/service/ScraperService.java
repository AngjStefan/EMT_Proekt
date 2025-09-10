package backend.service;

import backend.data.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This is a scraper service to populate the database with data from the site ceni.mk
 */

@Service
public class ScraperService {

    private final ProductService productService;
    private final AiHelperService aiHelper;

    public ScraperService(ProductService productService, AiHelperService aiHelperService) {
        this.productService = productService;
        this.aiHelper = aiHelperService;
    }

    public Boolean scrapeProducts() {
        try{
            WebDriver driver = new ChromeDriver();
            driver.manage().window().maximize();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            driver.get("https://ceni.mk/");
            WebElement button1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/ul[1]/li[1]/a")));
            button1.click();
            List<WebElement> listOfCategories = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div.category-nav > div.category-pill")));
            for(int i = 0; i < listOfCategories.size(); i++){
                listOfCategories = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div.category-nav > div.category-pill")));
                WebElement category = listOfCategories.get(i);
                category.click();

                List<WebElement> listOfSubCategories = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div.category-nav > div.category-pill")));
                for(int j = 0; j < listOfSubCategories.size(); j++){
                    listOfSubCategories = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div.category-pill")));
                    WebElement subCategory = listOfSubCategories.get(j);
                    subCategory.click();

                    List<WebElement> listOfProducts = new ArrayList<>();
                    try{
                        listOfProducts = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div.col-md-4.col-6.mb-0")));
                    }
                    catch (Exception e){
                        System.out.println("No products for this subcategory.");
                    }
                    for(WebElement product: listOfProducts){
                        String name = product.findElement(By.cssSelector("h5.card-title.mb-0")).getText();
                        List<WebElement> listOfMarketsAndPrices = product.findElements(By.className("store-price-row"));
                        for(WebElement marketAndPrice : listOfMarketsAndPrices){
                            String market = marketAndPrice.findElement(By.className("store-name")).getText().split("\\s+")[0];
                            Integer price_in_mkd = Integer.parseInt(marketAndPrice.findElement(By.className("price-value")).getText().split("\\s+")[0]);
                            Optional<Product> product1 = productService.findByNameAndMarket(name, market);
                            if(product1.isEmpty()){
                                productService.save(new Product(name, price_in_mkd, market));
                                aiHelper.embedProduct(name);
                            }
                            else{
                                product1.get().setPriceInMkd(price_in_mkd);
                                productService.update(product1.get().getId(), product1.get());
                            }
                        }
                    }
                    WebElement backToAllSubCategoriesPageButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div[1]/div[4]/a")));
                    backToAllSubCategoriesPageButton.click();
                }
                WebElement backToAllCategoriesPageButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div[1]/div[3]/a")));
                backToAllCategoriesPageButton.click();
            }
            return true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
}
