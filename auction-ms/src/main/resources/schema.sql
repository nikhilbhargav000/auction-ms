

CREATE TABLE users (
	user_id VARCHAR(250) PRIMARY KEY NOT NULL,
	email VARCHAR(250) UNIQUE,
	name VARCHAR(250), 
	password VARCHAR(250),
	role INTEGER
);

INSERT INTO users (
	user_id,
	name,
	email,
	password,
	role
) VALUES 
( 'e8cebd44-47ca-4bb4-a23e-b0c816cc9d5e', 'Nikhil Bhargav', 'nikhil.bhargav01@cars.com','Abcd#123', '0' ), 
( 'ed850e28-03fa-4f40-86ea-96f73d393ad2', 'Aman Gahir', 'aman.gahir01@cars.com', 'Abcd#123', '0' );


INSERT INTO users (
	user_id,
	name,
	email,
	password,
	role
) VALUES 
( 'd47740c7-ca62-44ff-a6cf-722c50ba6b55', 'Alice', 'alice@cars.com','Abcd#123', '1' ), 
( 'e253f4fd-e66a-46e4-b6d2-6204fb855b47', 'Bob', 'bob@cars.com', 'Abcd#123', '1' );

	
CREATE TABLE cars (
	id VARCHAR(255) NOT NULL AUTO_INCREMENT, 
	auction_status INTEGER, 
	current_auction_status INTEGER,
	color VARCHAR(255), 
	description VARCHAR(255), 
	highest_bid_price INTEGER, 
	km_driven INTEGER, 
	make VARCHAR(255), 
	model VARCHAR(255), 
	sale_price VARCHAR(255), 
	year VARCHAR(255), 
	current_auction_id VARCHAR(255), 
	highest_bid_id VARCHAR(255),
	seller_id VARCHAR(255) REFERENCES users(user_id),
	date TIMESTAMP, 
	version INTEGER,
	PRIMARY KEY(seller_id, id)
);
	
CREATE TABLE auctions (
	id VARCHAR(255) NOT NULL PRIMARY KEY AUTO_INCREMENT,  
	seller_id varchar(255),
	car_id varchar(255) REFERENCES cars(id)
);


CREATE TABLE bids (
	id varchar(255) NOT NULL, 
	price INTEGER, 
	auction_id VARCHAR(255) REFERENCES auctions(id), 
	car_id VARCHAR(255), 
	seller_id varchar(255),
	user_id varchar(255) REFERENCES users(user_id),
	PRIMARY KEY (car_id, id)
);

CREATE SEQUENCE car_id_seq START WITH 1001 INCREMENT BY 1;
CREATE SEQUENCE auction_id_seq START WITH 1001 INCREMENT BY 1;
CREATE SEQUENCE bid_id_seq START WITH 1001 INCREMENT BY 1;

CREATE INDEX idx_date ON cars (date);
CREATE INDEX idx_seller_id ON cars (seller_id);
CREATE INDEX idx_current_auction_status ON cars (current_auction_status);

CREATE INDEX idx_user_id ON bids (user_id);
