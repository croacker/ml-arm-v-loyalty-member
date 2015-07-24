package ru.peaksystems.varm.loyalty.tb;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.peaksystems.varm.loyalty.tb.pageobjects.TBLoginView;
import ru.peaksystems.varm.loyalty.tb.pageobjects.TBMainView;
import ru.peaksystems.varm.loyalty.tb.pageobjects.TBProfileWindow;
import com.vaadin.testbench.TestBenchTestCase;

public class MainViewIT extends TestBenchTestCase {

    private TBLoginView loginView;
    private TBMainView mainView;

    @Before
    public void setUp() {
        loginView = TBUtils.openInitialView();
        mainView = loginView.login();
    }

    @Test
    public void testEditProfile() {
        TBProfileWindow profile = mainView.openProfileWindow();
        profile.setName("Test", "User");
        profile.commit();
        Assert.assertEquals("Test User", mainView.getUserFullName());
    }

    @After
    public void tearDown() {
        loginView.getDriver().quit();
    }
}
