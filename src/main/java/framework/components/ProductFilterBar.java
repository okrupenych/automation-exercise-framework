package framework.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProductFilterBar {

    private static final Logger log = LoggerFactory.getLogger(ProductFilterBar.class);

    private final Locator rootAccordionCategory;
    private final Locator rootBlockBrands;
    private final Locator lblHeaderCategory;
    private final Locator lblHeaderBrands;

    public ProductFilterBar(Page page) {
        Locator rootLeftSideBar = page.locator("div.left-sidebar");
        rootAccordionCategory = rootLeftSideBar.locator("div#accordian");
        rootBlockBrands = rootLeftSideBar.locator("div.brands_products div.brands-name");
        lblHeaderCategory = rootLeftSideBar.locator("h2:text-is('Category')");
        lblHeaderBrands = rootLeftSideBar.locator("div.brands_products h2:text-is('Brands')");
    }


//----Internal implementation----

    private Locator categoryPanel(String categoryName) {
        return rootAccordionCategory.locator("div.panel:has(div#" + categoryName + ")");
    }

    private Locator categoryHeader(String categoryName) {
        return categoryPanel(categoryName).locator("a[href='#" + categoryName + "']");
    }

    private Locator categoryContent(String categoryName) {
        return categoryPanel(categoryName).locator("div#" + categoryName);
    }


//----Public interface----

    public List<Locator> getRequiredElements() {
        return List.of(
                rootAccordionCategory,
                rootBlockBrands,
                lblHeaderBrands,
                lblHeaderCategory,
                categoryHeader("Women"),
                categoryHeader("Men"),
                categoryHeader("Kids")
        );
    }

    public void clickCategoryHeader(String categoryName) {
        log.info("Clicking category header \"{}\"", categoryName);
        categoryHeader(categoryName).click();
    }

    public void openCategoryLink(String categoryName, String linkName) {
        log.info("Opening category \"{}/{}\"", categoryName, linkName);
        Locator content = categoryContent(categoryName);

        boolean isOpen = Boolean.TRUE.equals(
            content.evaluate("el => el.classList.contains('in')")
        );
        log.trace("Category content expanded: {}", isOpen);

        if (!isOpen) {
            clickCategoryHeader(categoryName);
        }

        content.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName(linkName)).click();
    }

    public void openBrandLink(String brandName) {
        log.info("Opening brand link \"{}\"", brandName);
        rootBlockBrands.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName(brandName)).click();
    }

}
