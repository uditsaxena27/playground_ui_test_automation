# Playground UI Test Framework

End-to-end UI test automation for the TensorFlow Playground–style web app, built with **Java + Selenium 4.35.0 + TestNG + Maven**.

---

## 📋 Prerequisites

1. **Java JDK 24** (17 recommended)
   
2. **Apache Maven**
     Apache Maven 3.9.1

3. **Web Browsers**
    - Google Chrome (latest stable)
    - Firefox (optional)
    - Selenium Manager will auto-provision the correct driver.

4. **Source Code**  
     Download and unzip Playground.zip

5. **Execution command**  : mvn clean test -Denvironment=production -Dheadless=false

6. Playground can be run (PROD or STAGE URL)

7. **Configuration Files**  
   In src/test/resources:
   env.properties → framework defaults (browser, timeouts, headless, env).
   testdata.properties → per-environment data (dataset, noise, learning rate, etc.).

8. **Maven POM Setup**
   Dependencies for:
   selenium-java
   testng