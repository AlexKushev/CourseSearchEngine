from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities


dcap = dict(DesiredCapabilities.PHANTOMJS)
dcap["phantomjs.page.settings.userAgent"] = (
    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/53 "
    "(KHTML, like Gecko) Chrome/15.0.87"
)
driver = webdriver.PhantomJS(desired_capabilities=dcap)

# driver = webdriver.Chrome("/Users/Marina/Downloads/chromedriver")
# driver.get("http://www.sreality.cz/hledani/prodej/domy?region=jemnice")
links = []
page = 1
# driver.get("https://www.udemy.com/courses/development/all-courses/")

base_url = "https://www.udemy.com/courses/personal-development/all-courses/?courseLabel=6680&p=%s"
driver.get(base_url % page )
driver.maximize_window()

wait = WebDriverWait(driver, 20)
wait.until(EC.visibility_of_element_located((By.CLASS_NAME, "card--search")))

current_page_links = [link.get_attribute("href") for link in driver.find_elements_by_css_selector("li.card--search div.search-course-card--card__head--2X4bl > a")]
links.extend(current_page_links)
for l in links:
    print(l)

print(len(links))

page = 2
while True:
    driver.get(base_url % page)
    driver.maximize_window()
    wait = WebDriverWait(driver, 20)
    wait.until(EC.visibility_of_element_located((By.CLASS_NAME, "card--search")))
    current_page_links = [link.get_attribute("href") for link in driver.find_elements_by_css_selector("li.card--search div.search-course-card--card__head--2X4bl > a")]
    links.extend(current_page_links)

    for l in current_page_links:
        print(l)

    page += 1
    print(page + ': ' + len(links))

driver.close()