DROP TABLE IF EXISTS job_posting;
DROP TABLE IF EXISTS job_posting_location;
DROP TABLE IF EXISTS job_posting_tag;

CREATE TABLE job_posting
(
    id                 BIGINT NOT NULL AUTO_INCREMENT,
    position_name      VARCHAR(255),
    job_page_url       VARCHAR(255),
    organization_url   VARCHAR(255),
    logo_url           VARCHAR(255),
    organization_title VARCHAR(255),
    labor_function     VARCHAR(255),
    posted_date        DATE,
    description_html   LONGTEXT,
    PRIMARY KEY (id)
);

CREATE TABLE job_posting_location
(
    job_posting_id BIGINT NOT NULL,
    location       VARCHAR(1000),
    FOREIGN KEY (job_posting_id) REFERENCES job_posting (id) ON DELETE CASCADE
);

CREATE TABLE job_posting_tag
(
    job_posting_id BIGINT NOT NULL,
    tag            VARCHAR(1000),
    FOREIGN KEY (job_posting_id) REFERENCES job_posting (id) ON DELETE CASCADE
);

INSERT INTO job_posting (id, position_name, job_page_url, organization_url, logo_url, organization_title,
                         labor_function, posted_date, description_html)
VALUES (1, 'Software Engineer', 'https://jobs.techstars.com/job/1', 'https://techstars.com',
        'https://logo.com/techstars.png', 'Techstars', 'Engineering', '2025-06-16',
        '<p>Join our growing team to build scalable services...</p>');

INSERT INTO job_posting_location (job_posting_id, location)
VALUES (1, 'Remote'),
       (1, 'New York, USA');

INSERT INTO job_posting_tag (job_posting_id, tag)
VALUES (1, 'Java'),
       (1, 'Spring Boot'),
       (1, 'AWS');
