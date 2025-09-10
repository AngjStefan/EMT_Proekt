package backend.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private final ScraperService scraperService;

    public ScheduledTasks(ScraperService scraperService) {
    this.scraperService = scraperService;
    }

    @Scheduled(cron = "0 0 4 * * *")
    public void scrapeProducts() {
        if(this.scraperService.scrapeProducts()){
            System.out.println("Product scraping finished successfully");
        }
        else{
            System.out.println("Product scraping was terminated because of some error. Probably not all products were scraped.");
        }
    }
}
