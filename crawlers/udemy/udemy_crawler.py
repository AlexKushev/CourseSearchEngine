from selenium import webdriver
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

from bs4 import BeautifulSoup
from time import sleep


BASE_URL = 'https://www.udemy.com'

FILE_PATH = 'categories/%s.txt'

CATEGORIES = [   
    #'development', 'business', 'it-and-software', 'office-productivity', 'personal-development',
    # 'design', 'marketing', 'lifestyle', 'photography', 'health-and-fitness',
    # 'teacher-training', 'music', 'academics', 'language', 'test-prep'
]

class UdemyCourseScraper():
    def __init__(self):
        self.driver = webdriver.Chrome('/Users/Marina/Downloads/chromedriver')
        self.driver.maximize_window()

        self.start_url = BASE_URL + '/courses/%s/all-courses/?p=%s'

    def scrape(self):
        for category in CATEGORIES:
            self.scrape_category_courses(category)
    
    def scrape_category_courses(self, category):
        courses_links = self.scrape_courses_links(category)

        f_name = FILE_PATH % category
        with open(f_name, 'a') as f:
            for course_link in courses_links:
                f.write(BASE_URL + course_link + '\n')
                #print(course_link)
            print(len(courses_links))

    def scrape_courses_links(self, category):
        page_number = 1
        max_page_number = 70

        self.driver.get(self.start_url % (category, page_number))

        wait = WebDriverWait(self.driver, 10)
        wait.until(EC.visibility_of_element_located((By.CLASS_NAME, "card--search")))

        courses_links = []

        while True:
            soup = BeautifulSoup(self.driver.page_source)
            anchors = soup.select('li.card--search div.search-course-card--card__head--2X4bl > a')

            links = [anchor['href'] for anchor in anchors]
            courses_links.extend(links)

            pages = soup.select('ul.pagination li.num > a')
            pages_numbers = {int(page.get_text()) for page in pages}
            next_page_number = page_number + 1
               
            if next_page_number in pages_numbers:
                page_number += 1
                self.driver.get(self.start_url % (category, page_number))
                try:
                    wait = WebDriverWait(self.driver, 10)
                    wait.until(EC.visibility_of_element_located((By.CLASS_NAME, "card--search")))
                except TimeoutException as ex:
                    print(category + ': ' + str(next_page_number))
                    break
            else:
                break
        
        return courses_links

        self.driver.quit()


if __name__ == '__main__':
    scraper = UdemyCourseScraper()
    scraper.scrape()