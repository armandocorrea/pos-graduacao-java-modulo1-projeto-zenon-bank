create table CUSTOMER (
    id INT UNSIGNED AUTO_INCREMENT not null primary key,
    name varchar(150) not null,
    oldBalance numeric(15,2),
    newBalance numeric(15,2));

create table TRANSACTION (
    id INT UNSIGNED AUTO_INCREMENT not null primary key,
    step integer not null,
    type varchar(20) not null,
    amount numeric(15,2) not null,
    id_origin INT UNSIGNED not null,
    id_recipient INT UNSIGNED not null,
    isFraud boolean,
    isflaggedFraud boolean);

alter table TRANSACTION
    add constraint fk_origin_customer foreign key (id_origin) references CUSTOMER(id),
    add constraint fk_recipient_customer foreign key (id_recipient) references CUSTOMER(id);

alter table CUSTOMER
    add constraint uk_origin_name unique key (name);