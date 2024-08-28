DROP TABLE IF EXISTS coupon;
 
CREATE TABLE coupon (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  code  VARCHAR(250) NOT NULL,
  discount NUMBER(10,2) NOT NULL,
  min_basket_value NUMBER(10,2) DEFAULT NULL,
  expiration_date DATE
);

INSERT INTO coupon (code, discount, min_basket_value, expiration_date) VALUES
  ('TEST1', 10.00, 50.00, '2024-01-31'),
  ('TEST2', 15.00, 100.00, '2025-01-31'),
  ('TEST3', 20.00, 200.00, '2025-06-30');