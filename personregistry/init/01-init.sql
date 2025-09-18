USE master;
GO

IF NOT EXISTS (SELECT * FROM sys.sql_logins WHERE name = 'newuser')
BEGIN
    CREATE LOGIN [newuser] WITH PASSWORD = 'Password00', CHECK_POLICY = OFF;
    ALTER SERVER ROLE [sysadmin] ADD MEMBER [newuser];
END
GO

IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'RegistryDB')
CREATE DATABASE RegistryDB;
GO

USE RegistryDB;
GO

CREATE TABLE Person (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    first_name NVARCHAR(50) NOT NULL,
    last_name NVARCHAR(50) NOT NULL,
    birth_date DATE,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE()
);
GO

CREATE TABLE Address (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    person_id BIGINT NOT NULL,
    address_type NVARCHAR(20) NOT NULL CHECK (address_type IN ('PERMANENT', 'TEMPORARY')),
    country NVARCHAR(100) NOT NULL,
    city NVARCHAR(100) NOT NULL,
    zip_code NVARCHAR(20) NOT NULL,
    street NVARCHAR(200) NOT NULL,
    house_number NVARCHAR(20),
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (person_id) REFERENCES Person(id) ON DELETE CASCADE,
    CONSTRAINT UK_Person_AddressType UNIQUE (person_id, address_type)
);
GO

CREATE TABLE Contact (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    address_id BIGINT NOT NULL,
    contact_type NVARCHAR(20) NOT NULL CHECK (contact_type IN ('EMAIL', 'PHONE', 'MOBILE', 'FAX')),
    contact_value NVARCHAR(200) NOT NULL,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (address_id) REFERENCES Address(id) ON DELETE CASCADE
);
GO

CREATE INDEX IX_Address_PersonId ON Address(person_id);
CREATE INDEX IX_Contact_AddressId ON Contact(address_id);
CREATE INDEX IX_Person_Name ON Person(last_name, first_name);
GO