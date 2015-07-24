package ru.peaksystems.varm.loyalty.tb.pageobjects;

import org.openqa.selenium.WebDriver;

import ru.peaksystems.varm.loyalty.view.reports.ReportsView;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.WindowElement;

public class TBConfirmDialog extends TestBenchTestCase {

    private final WindowElement scope;

    public TBConfirmDialog(WebDriver driver) {
        setDriver(driver);
        scope = $(WindowElement.class).id(ReportsView.CONFIRM_DIALOG_ID);
    }

    public void discard() {
        $(ButtonElement.class).caption("Discard Changes").first().click();
    }

}