USE RegistryDB;
GO

IF NOT EXISTS (SELECT * FROM sys.sql_logins WHERE name = 'newuser')
BEGIN
    CREATE LOGIN [newuser] WITH PASSWORD = 'Password00', CHECK_POLICY = OFF;
    ALTER SERVER ROLE [sysadmin] ADD MEMBER [newuser];
END
GO

INSERT INTO Person (first_name, last_name, birth_date) VALUES
('Kovács', 'István', '1985-05-15'),
('Nagy', 'Erika', '1990-08-22'),
('Szabó', 'Gábor', '1978-12-03');
GO

INSERT INTO Address (person_id, address_type, country, city, zip_code, street, house_number) VALUES
(1, 'PERMANENT', 'Magyarország', 'Budapest', '1011', 'Petőfi Sándor utca', '12/A'),
(1, 'TEMPORARY', 'Magyarország', 'Debrecen', '4025', 'Kossuth Lajos utca', '45'),
(2, 'PERMANENT', 'Magyarország', 'Szeged', '6720', 'Rákóczi út', '78'),
(3, 'PERMANENT', 'Magyarország', 'Pécs', '7621', 'Zrínyi utca', '33');
GO

INSERT INTO Contact (address_id, contact_type, contact_value) VALUES
(1, 'EMAIL', 'kovacs.istvan@email.com'),
(1, 'PHONE', '+36-1-234-5678'),
(1, 'MOBILE', '+36-30-123-4567'),
(2, 'EMAIL', 'kovacs.istvan.debrecen@email.com'),
(2, 'MOBILE', '+36-70-765-4321'),
(3, 'EMAIL', 'nagy.erika@email.com'),
(3, 'PHONE', '+36-62-345-6789'),
(3, 'MOBILE', '+36-20-987-6543'),
(4, 'EMAIL', 'szabo.gabor@email.com'),
(4, 'PHONE', '+36-72-456-7890');
GO