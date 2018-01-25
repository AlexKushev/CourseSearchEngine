from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities


# dcap = dict(DesiredCapabilities.PHANTOMJS)
# dcap["phantomjs.page.settings.userAgent"] = (
#     "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/53 "
#     "(KHTML, like Gecko) Chrome/15.0.87"
# )

# driver = webdriver.PhantomJS(desired_capabilities=dcap)
driver = webdriver.PhantomJS()
url = "https://www.udemy.com/courses/personal-development/all-courses/?courseLabel=6680&p=1"
driver.get(url)

# waiting for presence of an element
driver.implicitly_wait(10)
#wait = WebDriverWait(driver, 10)
#wait.until(EC.visibility_of_element_located((By.CLASS_NAME, "card--search")))

print(driver.page_source, file=open('output.html','w'))

driver.close()