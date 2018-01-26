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
    'development', 'business', 'it-and-software', 'office-productivity', 'personal-development',
    'design', 'marketing', 'lifestyle', 'photography', 'health-and-fitness',
    'teacher-training', 'music', 'academics', 'language', 'test-prep'
]

class UdemyCourseScraper():
    def __init__(self):
        self.driver = webdriver.Chrome('/Users/alexkushev/Downloads/chromedriver')
        self.driver.maximize_window()

        self.start_url = BASE_URL + '/courses/%s/all-courses/?p=%s'
    
    def scrape_course_data(self, course_link, category, i):
        self.driver.get(course_link)
        self.driver.implicitly_wait(10)

        soup = BeautifulSoup(self.driver.page_source)

        desc_items = soup.select('div.js-simple-collapse-inner > p')
        additional_description = ''
        for desc_item in desc_items:
            additional_description += desc_item.get_text(strip=True) + ' '

        num_lectures = 0
        if soup.find('div', {'class': 'num-lectures'}) is not None:
            num_lectures = soup.find('div', {'class': 'num-lectures'}).get_text(strip=True)

        length = 0
        if soup.find('span', {'class': 'curriculum-header-length'}) is not None:
            length = soup.find('span', {'class': 'curriculum-header-length'}).get_text(strip=True)
        
        r = soup.select('div.rate-count span.tooltip--rate-count-container > span')
        rating = 0 if not r else r[0].get_text(strip=True)

        d = soup.find('meta', property='og:description')
        description = "None" if d is None else d['content']

        course = {
            'url': course_link,
            'title': soup.select('h1.clp-lead__title')[0].get_text(strip=True),
            'description': description,
            'additionalDescription': additional_description,
            'image': soup.find('meta', property='og:image')['content'],
            'price': soup.find('meta', property='udemy_com:price')['content'],
            'rating': str(rating),
            'language': soup.find('div', {'class': 'clp-lead__locale'}).get_text(strip=True),
            # 'subtitles': soup.select('div.clp-lead__caption span.caption')[0].get_text(strip=True),
            'duration': str(num_lectures) + ', ' + str(length),
            'hoursPerWeek': 'NA',
            'level': 'For all',
            'certificate': 'Certificate of Completion',
            'category': soup.find('meta', property='udemy_com:category')['content'],
            'provider': 'Udemy',
            'university': 'NA',
            'instructor': soup.find('a', {'class': 'instructor-links__link'}).get_text(strip=True),
        }

        # print(course)

        with open(category + '/' + category + '-course-' + str(i) + '.html', 'w') as f:
            f.write(str(soup))

        with open(category + '/udemy-' + category + '-course-' + str(i) + '.txt', 'w') as f:
            f.write('Provider' + ': ' + course['provider'] + '\n')
            f.write('Title' + ': ' + course['title'] + '\n')
            f.write('Price' + ': ' + course['price'] + '\n')
            f.write('Instructor' + ': ' + course['instructor'] + '\n')
            f.write('Duration' + ': ' + course['duration'] + '\n')
            f.write('Rating' + ': ' + course['rating'] + '\n')
            f.write('Url' + ': ' + course['url'] + '\n')
            f.write('Language' + ': ' + course['language'] + '\n')
            # f.write('Subtitles' + ': ' + course['subtitles'] + '\n')
            f.write('Hours per week' + ': ' + course['hoursPerWeek'] + '\n')
            f.write('Level' + ': ' + course['level'] + '\n')
            f.write('Certificate' + ': ' + course['certificate'] + '\n')
            f.write('Category' + ': ' + course['category'] + '\n')
            f.write('University' + ': ' + course['university'] + '\n')
            f.write('Image' + ': ' + course['image'] + '\n')
            f.write('Description' + ': ' + course['description'] + '\n')
            f.write('Additional description' + ': ' + course['additionalDescription'] + '\n')
        
        # self.driver.close()


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
    # scraper.scrape()

    with open('categories/it-and-software.txt', 'r') as f:
        for i in range(3751):
            f.readline()
        line = f.readline()
        cnt = 3752
        while line:
            # try:
            #     scraper.scrape_course_data(line, 'test-prep')
            # except Exception as ex:
            #     print(cnt)
            # finally:
            #     line = f.readline()
            #     cnt += 1
            scraper.scrape_course_data(line, 'it-and-software', cnt)
            line = f.readline()
            cnt +=1

    # scraper.scrape_course_data('https://www.udemy.com/understand-nodejs/', 'development')
