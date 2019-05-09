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
