package framework.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class Accordion {

    private final Locator rootAccordionItems;

    public Accordion(Page page) {
        rootAccordionItems = page.locator("section#form div.container div.panel-group");
    }


//----Internal implementation----

    private Locator itemByIndex(int index) {
        return rootAccordionItems.nth(index);
    }

    private Locator itemByTitle(String title) {
        return rootAccordionItems.filter(
                new Locator.FilterOptions()
                        .setHasText(title)
        );
    }

    private Locator itemHeader(Locator item) {
        return item.locator("div.panel-heading a");
    }

    private Locator itemContent(Locator item) {
        return item.locator("div.panel-collapse");
    }


//----Public interface----

    public Locator getItemContent(int index) {
        return itemContent(itemByIndex(index));
    }

    public Locator getItemContent(String title) {
        return itemContent(itemByTitle(title));
    }


    public void openAll() {
        for (int i = 0; i < rootAccordionItems.count(); i++) {
            openItem(i);
        }
    }

    public void closeAll() {
        for (int i = 0; i < rootAccordionItems.count(); i++) {
            closeItem(i);
        }
    }

    public void openItem(int index) {
        Locator targetItem = itemByIndex(index);
        Locator content = itemContent(targetItem);

        boolean isOpen = Boolean.TRUE.equals(
                content.evaluate("el => el.classList.contains('in')")
        );

        if (!isOpen) {
            itemHeader(targetItem).click();
        }
    }

    public void closeItem(int index) {
        Locator targetItem = itemByIndex(index);
        Locator content = itemContent(targetItem);

        boolean isOpen = Boolean.TRUE.equals(
                content.evaluate("el => el.classList.contains('in')")
        );

        if (isOpen) {
            itemHeader(targetItem).click();
        }
    }

    public void openItem(String title) {
        Locator targetItem = itemByTitle(title);
        Locator content = itemContent(targetItem);

        boolean isOpen = Boolean.TRUE.equals(
                content.evaluate("el => el.classList.contains('in')")
        );

        if (!isOpen) {
            itemHeader(targetItem).click();
        }
    }

    public void closeItem(String title) {
        Locator targetItem = itemByTitle(title);
        Locator content = itemContent(targetItem);

        boolean isOpen = Boolean.TRUE.equals(
                content.evaluate("el => el.classList.contains('in')")
        );

        if (isOpen) {
            itemHeader(targetItem).click();
        }
    }

}
