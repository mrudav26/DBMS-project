CREATE DATABASE Pro;
USE Pro;
CREATE TABLE Client (
    ClientID INT AUTO_INCREMENT PRIMARY KEY,
    FirstName VARCHAR(50),
    LastName VARCHAR(50),
    Password VARCHAR(50),
    Address VARCHAR(200),
    CreditCardInfo VARCHAR(10),
    PhoneNumber VARCHAR(10),
    Email VARCHAR(50) UNIQUE,
    UNIQUE (ClientID)
)


CREATE TABLE Administrator
(
    AdminID  INT AUTO_INCREMENT PRIMARY KEY,
    Email  VARCHAR(50) ,
    Username VARCHAR(50),
    Password VARCHAR(50)
);


CREATE TABLE Contractor
(
    ContractorID  INT PRIMARY KEY DEFAULT 1,       
    Username VARCHAR(50),
    Password VARCHAR(50),
    Email  VARCHAR(50)
);

 
CREATE TABLE Revenue
(
    RevenueID INT AUTO_INCREMENT PRIMARY KEY,
    Date      DATE,
    Amount    FLOAT
);

CREATE TABLE TreeCuttingRequest
(
    RequestID   VARCHAR(10) PRIMARY KEY,
    Client_ID   VARCHAR(10) REFERENCES Client (Client_ID),
    RequestDate DATE,
    Status      VARCHAR(100),
    Note        VARCHAR(200)
);
 
CREATE TABLE TreeInfo
(
    TreeID           VARCHAR(10) PRIMARY KEY,
    RequestID        VARCHAR(10) REFERENCES TreeCuttingRequest (RequestID),
    Size             VARCHAR(10),
    Height           DECIMAL(2, 1),
    Location         VARCHAR(100),
    ProximityToHouse FLOAT
); 
CREATE TABLE Quote
(
    QuoteID        VARCHAR(10) PRIMARY KEY,
    RequestID      VARCHAR(10) REFERENCES TreeCuttingRequest (RequestID),
    Price          DECIMAL(2, 2),
    Fromdate       DATE,
    Duedate        Date,
    ResponseDate   Date,
    Note           VARCHAR(200)
);

CREATE TABLE QuoteRequest
(
    RequestID INT AUTO_INCREMENT PRIMARY KEY,
    ClientID INT,   
    RequestDate DATE,
    Status VARCHAR(100),
    Note VARCHAR(200),
    Size VARCHAR(10), 
    Height DECIMAL(3, 1), 
    Location VARCHAR(100),
    ProximityToHouse FLOAT (20),
    UNIQUE (RequestID),
    FOREIGN KEY (ClientID) REFERENCES Client (ClientID)    
);

CREATE TABLE QuoteResponse
(
    ResponseID INT AUTO_INCREMENT PRIMARY KEY,
    RequestID INT,
    ClientID INT,
    ResponseDate DATE,
    Price DOUBLE,
    WorkPeriodFrom DATE,
    WorkPeriodTo DATE,     
    Note VARCHAR(200),
     UNIQUE (ResponseID),
    FOREIGN KEY (RequestID) REFERENCES QuoteRequest (RequestID),
	FOREIGN KEY (ClientID) REFERENCES QuoteRequest (ClientID)
);

CREATE TABLE ResponseToQuote
(
    ResponseToQuoteID VARCHAR(10) PRIMARY KEY,
    QuoteID VARCHAR(10) REFERENCES Quote (QuoteID),
    Client_ID VARCHAR(10) REFERENCES Client (Client_ID),
    ResponseDate DATE,
    Note VARCHAR(200)
);

CREATE TABLE ClientRespondToQuoteResponse (
    ClientResponseID INT AUTO_INCREMENT PRIMARY KEY,
    ContractorID INT,
    ResponseID INT,
    ResponseDate DATE,
    Status ENUM('RequestAgain','Rejected', 'Pending','Accepted'),
    Note VARCHAR(200),
     UNIQUE (ClientResponseID),
    FOREIGN KEY (ContractorID) REFERENCES Contractor(ContractorID),
    FOREIGN KEY (ResponseID) REFERENCES QuoteResponse(ResponseID)
);

CREATE TABLE ContractorRespondToClientResponse (
    ContractorResponseID INT AUTO_INCREMENT PRIMARY KEY,
    ContractorID INT,
    ClientResponseID INT,
    ResponseDate DATE,
    Status ENUM('RequestAgain','Rejected', 'Pending','Accepted'),
    Note VARCHAR(200),
    ModifiedPrice DOUBLE,
    ModifiedWorkPeriodFrom DATE,
    ModifiedWorkPeriodTo DATE,
    FOREIGN KEY (ContractorID) REFERENCES Contractor(ContractorID),
    FOREIGN KEY (ClientResponseID) REFERENCES ClientRespondToQuoteResponse(ClientResponseID)
);

CREATE TABLE OrderOfWork
(
    OrderID   INT AUTO_INCREMENT PRIMARY KEY,
    RequestID INT,   
    StartDate DATE,
    EndDate   DATE,
    Status    VARCHAR(50),
    FOREIGN KEY (RequestID) REFERENCES QuoteRequest (RequestID)
    
);

CREATE TABLE Bill
(
    BillID  INT AUTO_INCREMENT PRIMARY KEY,
    OrderID INT REFERENCES OrderOfWork (OrderID),
    Amount  FLOAT,
    DueDate DATE,
    Status  VARCHAR(50)
);

CREATE TABLE ResponseToBill
(
    ResponseToBillID INT AUTO_INCREMENT PRIMARY KEY,
    BillID           INT REFERENCES Bill (BillID),
    Client_ID        INT REFERENCES Client (Client_ID),
    ResponseDate     DATE,
    Note             VARCHAR(50)
);

