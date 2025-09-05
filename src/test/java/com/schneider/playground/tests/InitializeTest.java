package com.schneider.playground.tests;

import com.playground.config.FrameworkConfiguration;
import core.DriverSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class InitializeTest {
    protected DriverSession context;
    protected FrameworkConfiguration framework;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @BeforeClass(alwaysRun = true)
    public void setUpBase() throws Exception {
        framework = FrameworkConfiguration.load();
        context = DriverSession.create(framework);
        log.info("Driver session created.");
    }

    @AfterClass(alwaysRun = true)
    public void tearDownBase() {
        if (context != null) {
            context.close();
            log.info("Driver session closed.");
        }
    }
}