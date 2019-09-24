CREATE TABLE IF NOT EXISTS  ACCOUNT_USER(id bigint primary key auto_increment,
            user_name varchar(255) not null,
            first_name varchar(255),
            last_name varchar(255),
            email_address varchar(255),
            dob date,
            address varchar(255),
            unique(user_name)
            );

CREATE TABLE IF NOT EXISTS  ACCOUNT(id bigint primary key auto_increment,
                        user_id bigint,
                        account_name varchar(30) not null,
                        branch_code varchar(30),
                        currency_code varchar(3),
                        balance decimal,
                        account_type varchar(10),
                        FOREIGN KEY(user_id) REFERENCES ACCOUNT_USER,
                        unique(account_name)
                        );