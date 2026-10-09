package framework.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class ProductFilterBar {

    private final Locator rootAccordionCategory;
    private final Locator rootBlockBrands;

    public ProductFilterBar(Page page) {
        Locator rootLeftSideBar = page.locator("div.left-sidebar");
        rootAccordionCategory = rootLeftSideBar.locator("div#accordian");
        rootBlockBrands = rootLeftSideBar.locator("div.brands_products div.brands-name");
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

    public void clickCategoryHeader(String categoryName) {
        categoryHeader(categoryName).click();
    }

    public void openCategoryLink(String categoryName, String linkName) {
        Locator content = categoryContent(categoryName);

        boolean isOpen = Boolean.TRUE.equals(
            content.evaluate("el => el.classList.contains('in')")
        );

        if (!isOpen) {
            clickCategoryHeader(categoryName);
        }

        content.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName(linkName)).click();
    }

    public void openBrandLink(String brandName) {
        rootBlockBrands.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName(brandName)).click();
    }

}
