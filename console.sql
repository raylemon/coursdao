CREATE TABLE IF NOT EXISTS societies
(
	soc_id IDENTITY PRIMARY KEY,
	name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS languages
(
    lang_id IDENTITY PRIMARY KEY,
    name VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS employees
(
    emp_id IDENTITY PRIMARY KEY,
    firstname VARCHAR(50),
    lastname VARCHAR(50),
    birthday DATE,
    fk_society BIGINT,
    FOREIGN KEY (fk_society) REFERENCES societies (soc_id)
);

CREATE TABLE IF NOT EXISTS j_emp_lang
(
    jel_id IDENTITY PRIMARY KEY,
    fk_employee BIGINT,
    fk_language BIGINT,
    FOREIGN KEY (fk_employee) REFERENCES employees (emp_id) ON DELETE CASCADE,
    FOREIGN KEY (fk_language) REFERENCES languages (lang_id)
);