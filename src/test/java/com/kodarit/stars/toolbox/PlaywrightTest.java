package com.kodarit.stars.toolbox;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class PlaywrightTest {
    private Playwright playwright;
    private Browser browser;
    protected Page page;

    @LocalServerPort
    protected int port;

    @BeforeAll
    void globalSetup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    @AfterAll
    void globalTeardown() {
        browser.close();
        playwright.close();
    }

    @BeforeEach
    void setupPage() {
        page = browser.newPage();
    }

    @AfterEach
    void closePage() {
        page.close();
    }
}
