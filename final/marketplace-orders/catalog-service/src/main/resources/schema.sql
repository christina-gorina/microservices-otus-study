drop table if exists product_item;
drop table if exists orders_reserve;

create table product_item(id int, count int, name varchar);
create table orders_reserve(id int, uuid varchar);