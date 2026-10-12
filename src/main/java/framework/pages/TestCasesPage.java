package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import framework.components.Accordion;

import java.util.ArrayList;
import java.util.List;

public class TestCasesPage extends BasePage {

    private static final String URL = BASE_URL + "/test_cases";

    private final Accordion cmpAccordion;

    public TestCasesPage(Page page) {
        super(page);
        cmpAccordion = new Accordion(page);
    }


//----Public interface----

    @Override
    public List<Locator> getRequiredElements() {
        return new ArrayList<>(
                cmpAccordion.getRequiredElements()
        );
    }

    @Override
    public String getTargetUrl() {
        return URL;
    }


    // Built-in components
    public Accordion accordion() {
        return cmpAccordion;
    }

}
