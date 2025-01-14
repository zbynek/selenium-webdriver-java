/*
 * (C) Copyright 2021 Boni Garcia (https://bonigarcia.github.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.github.bonigarcia.webdriver.jupiter.ch5.cdp;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.slf4j.Logger;

import io.github.bonigarcia.wdm.WebDriverManager;

class ConsoleListenerJupiterTest {

    static final Logger log = getLogger(lookup().lookupClass());

    WebDriver driver;

    DevTools devTools;

    @BeforeEach
    void setup() {
        driver = WebDriverManager.chromedriver().create();
        devTools = ((ChromeDriver) driver).getDevTools();
        devTools.createSession();
    }

    @AfterEach
    void teardown() throws InterruptedException {
        // FIXME: pause for manual browser inspection
        Thread.sleep(Duration.ofSeconds(30).toMillis());

        devTools.close();
        driver.quit();
    }

    @Test
    void testConsoleListener() {
        devTools.getDomains().events().addConsoleListener(consoleEvent -> {
            log.debug("{} {}: {}", consoleEvent.getTimestamp(),
                    consoleEvent.getType(), consoleEvent.getMessages());
        });

        devTools.getDomains().events()
                .addJavascriptExceptionListener(jsException -> {
                    log.debug("JavascriptException: {} {}",
                            jsException.getMessage(),
                            jsException.getSystemInformation());
                });

        driver.get(
                "https://bonigarcia.dev/selenium-webdriver-java/console-logs.html");
    }
}
