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
package io.github.bonigarcia.webdriver.junit4.ch3.keyboard;

import static java.lang.invoke.MethodHandles.lookup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;

import io.github.bonigarcia.wdm.WebDriverManager;

public class UploadFileJUnit4Test {

    static final Logger log = getLogger(lookup().lookupClass());

    WebDriver driver;

    @Before
    public void setup() {
        driver = WebDriverManager.chromedriver().create();
    }

    @After
    public void teardown() {
        driver.quit();
    }

    @Test
    public void testUploadFile() throws IOException {
        String initUrl = "https://bonigarcia.dev/selenium-webdriver-java/web-form.html";
        driver.get(initUrl);

        WebElement inputFile = driver.findElement(By.name("my-file"));

        Path tempFile = Files.createTempFile("tempfiles", ".tmp");
        String filename = tempFile.toAbsolutePath().toString();
        log.debug("Using temporal file {} in file uploading", filename);
        inputFile.sendKeys(filename);

        driver.findElement(By.tagName("form")).submit();
        assertThat(driver.getCurrentUrl()).isNotEqualTo(initUrl);
    }

}
