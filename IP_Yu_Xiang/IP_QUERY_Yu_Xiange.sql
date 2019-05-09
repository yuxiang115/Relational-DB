CREATE TABLE employee(
    name            VARCHAR(255) NOT NULL PRIMARY KEY,
    address         VARCHAR(255) NOT NULL
);

CREATE TABLE quality_controller(
    name            VARCHAR(255) NOT NULL PRIMARY KEY REFERENCES Employee(name),
    product_type    VARCHAR(50) NOT NULL,

    CONSTRAINT      check_type CHECK(product_type in ('product 1', 'product 2', 'product 3'))
);

CREATE TABLE technical(
    name            VARCHAR(255) NOT NULL PRIMARY KEY REFERENCES Employee(name),
    degree          VARCHAR(10)  NOT NULL,
    position        VARCHAR(50)  NOT NULL,

    CONSTRAINT      check_degree CHECK(degree in ('BS', 'MS', 'Ph.D'))
);


CREATE TABLE worker(
    name            VARCHAR(255) NOT NULL PRIMARY KEY REFERENCES Employee(name),
    max_mun         INT NOT NULL
);

CREATE TABLE account(
    accnum          INT NOT NULL PRIMARY KEY,
    accdate         DATE NOT NULL,
    acctype         VARCHAR(50) NOT NULL,

    CONSTRAINT      check_acctype CHECK(acctype in ('product1-account', 'product2-account','product3-account'))
);

CREATE TABLE product(
    pid             INT NOT NULL PRIMARY KEY,
    pdate           DATE NOT NULL,
    duration        INT NOT NULL,
    producer        VARCHAR(255) NOT NULL FOREIGN KEY REFERENCES worker(name),
    tester          VARCHAR(255) NOT NULL FOREIGN KEY REFERENCES quality_controller(name),
    size            VARCHAR(50) NOT NULL,
    accnum          INT NOT NULL FOREIGN KEY REFERENCES account (accnum),
    cost            INT NOT NULL,
);


CREATE TABLE product1(
    pid             INT NOT NULL PRIMARY KEY REFERENCES product(pid),
    software        VARCHAR(255)
);


CREATE TABLE product2(
    pid             INT NOT NULL PRIMARY KEY REFERENCES product(pid),
    color           VARCHAR(50) NOT NULL
);


CREATE TABLE product3(
    pid             INT NOT NULL PRIMARY KEY REFERENCES product(pid),
    weight          VARCHAR(50) NOT NULL
);


CREATE TABLE fix(
    pid             INT NOT NULL PRIMARY KEY REFERENCES product(pid),
    name            VARCHAR(255) NOT NULL FOREIGN KEY REFERENCES technical(name),
    fdate           DATE NOT NULL,
    requested       VARCHAR(50) NOT NULL,

    CONSTRAINT      check_request CHECK(requested in ('complaint', 'controller'))
);


CREATE TABLE customer(
    cname           VARCHAR(255) NOT NULL PRIMARY KEY,
    address         VARCHAR(255) NOT NULL
);


CREATE TABLE purchased(
    cname             VARCHAR(255) NOT NULL PRIMARY KEY REFERENCES customer(cname),
    pid               INT NOT NULL UNIQUE FOREIGN KEY REFERENCES product(pid)
);


CREATE TABLE complaint(
    cid               INT NOT NULL PRIMARY KEY,
    cdate             DATE NOT NULL,
    description       VARCHAR(255) NOT NULL,
    treatment         VARCHAR(50) NOT NULL,

    CONSTRAINT        check_treatment CHECK(treatment in ('refund', 'exchange'))
);


CREATE TABLE make_complaint(
    cid               INT NOT NULL PRIMARY KEY REFERENCES complaint(cid),
    cname             VARCHAR(255) NOT NULL FOREIGN KEY REFERENCES purchased(cname),
    pid               INT NOT NULL FOREIGN KEY REFERENCES purchased(pid)
);


CREATE TABLE accident(
    accidentnum       INT NOT NULL PRIMARY KEY,
    accidentdate      DATE NOT NULL,
    lostday           INT NOT NULL
);


CREATE TABLE record_fixacc(
    accidentnum       INT NOT NULL PRIMARY KEY REFERENCES accident(accidentnum),
    pid               INT NOT NULL FOREIGN KEY REFERENCES product(pid),
    name            VARCHAR(255) NOT NULL FOREIGN KEY REFERENCES technical(name)
);


CREATE TABLE record_pacci(
    accidentnum       INT NOT NULL PRIMARY KEY REFERENCES accident(accidentnum),
    pid               INT NOT NULL FOREIGN KEY REFERENCES product(pid),
    name            VARCHAR(255) NOT NULL FOREIGN KEY REFERENCES worker(name)
);






CREATE INDEX FixrequestedIndex 
ON Fix (requested)

CREATE INDEX AccidentDateIndex 
ON Accident (accidentdate)

CREATE INDEX ProductColorIndex 
ON Product2 (color)

CREATE INDEX ProductDateIndex 
ON Product (pdate)



--1) Enter a new employee  (2/month)
-------------------QUERY 1----------------------------------
--add Technical--
CREATE PROCEDURE QUERY1_1
@name       VARCHAR(255),
@address     VARCHAR(255),
@degree     VARCHAR(10),
@position   VARCHAR(50)
AS
BEGIN
INSERT INTO [dbo].[employee](name, address)                 VALUES(@name, @address);
INSERT INTO [dbo].[technical](name, degree, position)       VALUES(@name, @degree, @position);
END
GO

--add Quality Controller
CREATE PROCEDURE QUERY1_2
@name           VARCHAR(255),
@address        VARCHAR(255),
@product_type   VARCHAR(50)
AS
BEGIN
INSERT INTO [dbo].[employee](name, address)                 VALUES(@name, @address);
INSERT INTO [dbo].[quality_controller](name, product_type)  VALUES(@name, @product_type);
END
GO

--add worker
CREATE PROCEDURE QUERY1_3
@name           VARCHAR(255),
@address        VARCHAR(255),
@max_num   VARCHAR(50)
AS
BEGIN
INSERT INTO [dbo].[employee](name, address)                 VALUES(@name, @address);
INSERT INTO [dbo].[worker](name, max_mun)  VALUES(@name, @max_num);
END
GO
-------------------QUERY 1 end----------------------------------

--Enter a new product associated with the person 
--who made the product, repaired the product if it is repaired, or checked the product (400/day). 
-------------------QUERY2----------------------------------
--add product1
CREATE PROCEDURE QUERY2_1
@pid            INT,
@pdate          DATE,
@duration       INT,
@producer       VARCHAR(255),
@tester         VARCHAR(255),
@size           VARCHAR(255),
@accnum         INT,
@cost           INT,
@software       VARCHAR(255)
AS
BEGIN
INSERT INTO [dbo].[product](pid, pdate, duration, producer, tester, size, accnum, cost) VALUES (@pid, @pdate, @duration, @producer, @tester, @size, @accnum, @cost);
INSERT INTO [dbo].[product1](pid, software) VALUES(@pid, @software)
END
GO

--add product2
CREATE PROCEDURE QUERY2_2
@pid            INT,
@pdate          DATE,
@duration       INT,
@producer       VARCHAR(255),
@tester         VARCHAR(255),
@size           VARCHAR(255),
@accnum         INT,
@cost           INT,
@color          VARCHAR(50)
AS
BEGIN
INSERT INTO [dbo].[product](pid, pdate, duration, producer, tester, size, accnum, cost) VALUES (@pid, @pdate, @duration, @producer, @tester, @size, @accnum, @cost);
INSERT INTO [dbo].[product2](pid, color) VALUES(@pid, @color)
END
GO


--add Product 3
CREATE PROCEDURE QUERY2_3
@pid            INT,
@pdate          DATE,
@duration       INT,
@producer       VARCHAR(255),
@tester         VARCHAR(255),
@size           VARCHAR(255),
@accnum         INT,
@cost           INT,
@weight         VARCHAR(50)
AS
BEGIN
INSERT INTO [dbo].[product](pid, pdate, duration, producer, tester, size, accnum, cost) VALUES (@pid, @pdate, @duration, @producer, @tester, @size, @accnum, @cost);
INSERT INTO [dbo].[product3](pid, weight) VALUES(@pid, @weight)
END
GO

--add fixer
CREATE PROCEDURE QUERY2_4
@pid            INT,
@name           VARCHAR(255),
@fdate          DATE,
@requested      VARCHAR(50)
AS
BEGIN
INSERT INTO [dbo].[fix](pid, name, fdate, requested) VALUES(@pid, @name, @fdate, @requested)
END
GO
-------------------------------------------------query2 end-----------------------------------------

--Enter a customer  associated with some products (50/day). 
---------------Query 3-----------------------
CREATE PROCEDURE QUERY3
@cname          VARCHAR(255),
@address        VARCHAR(255),
@pid            INT
AS
BEGIN
INSERT INTO [dbo].[customer](cname, address) VALUES(@cname, @address);
INSERT INTO [dbo].[purchased](cname, pid) VALUES(@cname, @pid);
END
GO

--4)Create a new account associated with a product (40/day). 
-------------------Query4--------------
--product 1 without fixed. account associated
CREATE PROCEDURE QUERY4_1
@accnum         INT,
@accdate        DATE,
@acctype        VARCHAR(50),
@pid            INT,
@pdate          DATE,
@duration       INT,
@producer       VARCHAR(255),
@tester         VARCHAR(255),
@size           VARCHAR(255),
@software       VARCHAR(255),
@cost           INT
AS
BEGIN
INSERT INTO [dbo].[account](accnum, accdate, acctype) VALUES(@accnum, @accdate, @acctype);
EXEC QUERY2_1  @pid = @pid, @pdate = @pdate, @duration = @duration, @producer = @producer, @tester = @tester, @size = size, @accnum = @accnum, @cost = @cost ,@software = @software;
END
GO

--product 2 without fixed account associated
CREATE PROCEDURE QUERY4_2
@accnum         INT,
@accdate        DATE,
@acctype        VARCHAR(50),
@pid            INT,
@pdate          DATE,
@duration       INT,
@producer       VARCHAR(255),
@tester         VARCHAR(255),
@size           VARCHAR(255),
@color          VARCHAR(50),
@cost           INT

AS
BEGIN
INSERT INTO [dbo].[account](accnum, accdate, acctype) VALUES(@accnum, @accdate, @acctype);
EXEC QUERY2_2  @pid = @pid, @pdate = @pdate, @duration = @duration, @producer = @producer, @tester = @tester, @size = size, @accnum = @accnum, @cost = @cost, @color = @color;
END
GO

--product 3 without fixed account associated
CREATE PROCEDURE QUERY4_3
@accnum         INT,
@accdate        DATE,
@acctype        VARCHAR(50),
@pid            INT,
@pdate          DATE,
@duration       INT,
@producer       VARCHAR(255),
@tester         VARCHAR(255),
@size           VARCHAR(255),
@weight         VARCHAR(50),
@cost           INT

AS
BEGIN
INSERT INTO [dbo].[account](accnum, accdate, acctype) VALUES(@accnum, @accdate, @acctype);
EXEC QUERY2_3  @pid = @pid, @pdate = @pdate, @duration = @duration, @producer = @producer, @tester = @tester, @size = size, @accnum = @accnum, @cost = @cost, @weitht = @weight;
END
GO


--5) Enter a complaint associated with a customer and product (30/day). 
---------------Query5-------------
CREATE PROCEDURE QUERY5
@cid            INT,
@cdate          DATE,
@description    VARCHAR(255),
@treatment      VARCHAR(50),
@cname          VARCHAR(255),
@pid            INT
AS
BEGIN
INSERT INTO [dbo].[complaint](cid, cdate, [description], treatment) VALUES(@cid, @cdate, @description, @treatment);
INSERT INTO [dbo].[make_complaint](cid, cname, pid) VALUES(@cid, @cname, @pid);
END
GO

--6) Enter  an accident associated with appropriate employee and product (1/week). 
---------------------Query 6--------------
----fixed accident
CREATE PROCEDURE QUERY6_1
@accidentnum      INT,
@accidentdate     DATE,
@lostday          INT,
@pid              INT,
@name             VARCHAR(255)
AS
BEGIN
INSERT INTO [dbo].[accident](accidentnum, accidentdate, lostday) VALUES(@accidentnum, @accidentdate, @lostday);
INSERT INTO [dbo].[record_fixacc](accidentnum, pid, name) VALUES(@accidentnum, @pid, @name);
END
GO

----produce accident
CREATE PROCEDURE QUERY6_2
@accidentnum      INT,
@accidentdate     DATE,
@lostday          INT,
@pid              INT,
@name             VARCHAR(255)
AS
BEGIN
INSERT INTO [dbo].[accident](accidentnum, accidentdate, lostday) VALUES(@accidentnum, @accidentdate, @lostday);
INSERT INTO [dbo].[record_pacci](accidentnum, pid, name) VALUES(@accidentnum, @pid, @name);
END
GO
-----------------------------Query6 end---------------------------


--7) Retrieve the date produced and time spent to produce a particular product (100/day). 
-----------------------------Query7-------------------------------
CREATE PROCEDURE QUERY7
@pid            INT,
@pdate          DATE OUTPUT,
@duration       INT OUTPUT
AS
BEGIN
SELECT @pdate = pdate, @duration = duration FROM [dbo].[product] WHERE pid = @pid;
END
GO


--8) Retrieve all products  made by  a  particular worker (2000/day).
-----------------------Query 8---------------------
CREATE PROCEDURE QUERY8
@name           VARCHAR(255)
AS
BEGIN
SELECT  *FROM [dbo].[Product]
WHERE   producer = @name;   
END
GO


--9)Retrieve the total number of errors a particular quality controller made.  
--This is the total number of  products certified by this controller and got some complaints (400/day). 
-------------------------QUERY 9-------------------------------
CREATE PROCEDURE QUERY9
@name           VARCHAR(255),
@count          INT OUTPUT
AS
BEGIN
SELECT  @count = COUNT(pid) FROM [dbo].[product]
WHERE   producer = @name AND pid IN (SELECT pid FROM [dbo].[make_complaint]);
END
GO


-- Retrieve the total costs  of the products in the product3 category  
--which were repaired at the request of  a particular quality controller (40/day). 
-------------------------------QUERY 10---------------------------------
CREATE PROCEDURE QUERY10
@name           VARCHAR(255),
@total_cost     INT OUTPUT
AS
BEGIN
SELECT  @total_cost = SUM(cost) FROM [dbo].[product] 
WHERE   tester = @name  AND pid IN (SELECT pid FROM [dbo].[fix] WHERE requested = 'controller') 
                        AND pid IN (SELECT pid FROM [dbo].[product3])
END
GO

--11) Retrieve all customers who purchased all products of a particular color (5/month)
----------------------------------------QUERY 11---------------------------
CREATE PROCEDURE QUERY11
@color          VARCHAR(50)
AS
BEGIN
SELECT * FROM [dbo].[customer] WHERE cname IN (SELECT cname FROM [dbo].[purchased] WHERE COUNT(pid) = (SELECT COUNT(pid) FROM [dbo].[product2] WHERE color = ""))
END
GO





--12)Retrieve  the total number of work days lost due to accidents in repairing  the products which got complaints (1/month).
------------------------------------------Query 12-------------------------------
CREATE PROCEDURE QUERY12
@totaldays          INT OUTPUT
AS
BEGIN
SELECT @totaldays = SUM(lostday) FROM [dbo].[accident] WHERE accidentnum IN (SELECT accidentnum FROM [dbo].[record_fixacc] WHERE pid IN (SELECT pid FROM [dbo].[make_complaint]))
END
GO



--13)Retrieve all customers who are also workers (10/month)
----------------------------QUERY 13------------------
CREATE PROCEDURE QUERY13
AS
BEGIN
SELECT * FROM [dbo].[customer] WHERE cname IN (SELECT name FROM [dbo].[worker])
END
GO


--14)Retrieve all the customers who have purchased the products made or certified or repaired by themselves (5/day). 
------------------------------Query14-----------------------------
CREATE PROCEDURE QUERY14
AS
BEGIN
SELECT  cname 
FROM    [dbo].[purchased] 
WHERE   cname IN    (SELECT producer FROM [dbo].[product] WHERE [dbo].[product].pid = [dbo].[purchased].pid) 
OR      cname IN    (SELECT tester   FROM [dbo].[product] WHERE [dbo].[product].pid = [dbo].[purchased].pid)
OR      cname IN    (SELECT name     FROM [dbo].[fix]     WHERE [dbo].[fix].pid = [dbo].[purchased].pid);
END
GO

