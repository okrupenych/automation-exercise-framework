package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Download;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Stream;

public class OrderPlacedPage extends ConfirmationPage {

    private static final Logger log = LoggerFactory.getLogger(OrderPlacedPage.class);

    private static final String URL = BASE_URL + "/payment_done/";

    private final Locator btnDownloadInvoice;

    public OrderPlacedPage(Page page) {
        super(page);

        btnDownloadInvoice = page.locator("div.col-sm-9 a.check_out");
    }


//----Public interface----

    @Override
    public String getTargetUrl() {
        return URL;
    }

    @Override
    public List<Locator> getRequiredElements() {
        return Stream.concat(
                super.getRequiredElements().stream(),
                Stream.of(btnDownloadInvoice)
        ).toList();
    }

    public void clickDownloadInvoice() {
        log.info("Clicking \"Download invoice\" button");
        btnDownloadInvoice.click();
    }

    public Download downloadInvoice() {
        log.info("Downloading invoice");
        return page.waitForDownload(this::clickDownloadInvoice);
    }

}
