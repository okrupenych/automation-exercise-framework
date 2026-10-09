package framework.pages;

import com.microsoft.playwright.Page;
import framework.components.Accordion;

public class ApiTestingPage extends BasePage {

    private static final String URL = BASE_URL + "/api_list";

    private final Accordion cmpAccordion;

    public ApiTestingPage(Page page) {
        super(page);
        cmpAccordion = new Accordion(page);
    }


//----Public interface----

    @Override
    public String getTargetUrl() {
        return URL;
    }


    // Built-in components
    public Accordion accordion() {
        return cmpAccordion;
    }

}
