INSERT INTO agent (agent_name, contact_info, licence_number, email, password,role, agency_agency_id)
VALUES
    ('Kingsley', '+32465787555', '3A3-NWA', 'kingley.ahams@student.kdg.be','$2a$12$t5espvkhpONF8NliPHq29egltDiWvEPuzz6gMyaqLF6528riN2bTO','ADMIN', (SELECT agency_id FROM real_estate_agency WHERE agency_name = 'Premium Realty')),
    ('Lesley', '+32843992200', 'AAA-ZE5', 'Lesley.Tianga@student.kdg.be', '$2a$12$/89kMbx4HWjcH9P/8Qq/gOhS4tpORtBt7JANUsu7g.gGdnfMnKSgO','USER', (SELECT agency_id FROM real_estate_agency WHERE agency_name = 'Budget Homes')),
    ('Fortune', '+3274399307', 'YUB-456', 'Fortune@gmail.com', '$2a$12$GjS302S9uO6wo8ef6lzEceu5xhibj0ce566BeQ4jTmmDzzCnIQo66', 'USER',(SELECT agency_id FROM real_estate_agency WHERE agency_name = 'City plus')),
    ('Aisosa', '+3245676788', 'ZES-2RE', 'Aisosa@gmail.com', '$2a$12$hy/ZS6/nhnU/L9pwBLoXsOmTYlh1XaovJzBlUAVzWay/ZkHE5iKXy','USER', (SELECT agency_id FROM real_estate_agency WHERE agency_name = 'Immovlan')),
    ('Maimuna', '+32466789704', 'RXE-BE1', 'Maimuna@gmail.com', '$2a$12$ecJ0S5vomp.UTNZ28PFNQuASo/iKMfckywFPq5tVN.mt.Kn//10JK', 'USER',(SELECT agency_id FROM real_estate_agency WHERE agency_name = 'Immobob')),
    ('Alex', '+3245676788', 'NIG-IS6', 'Alex@gmail.com', '$2a$12$ufgh/VE7RX1Z7bFZVb0WzOcW9iTJrNQQc3XCyRQ9KFR2Odj7ZsuZS','USER' ,(SELECT agency_id FROM real_estate_agency WHERE agency_name = 'Premium Realty')),
    ('Abir', '+32499876543', 'LOP-11X', 'abir@student.kdg.be', '$2a$12$PGWsVJpaVpy5gtGeBBkGdeHqOEqvmwY6qXiErb5NkJN4SszoTAWVy', 'USER',(SELECT agency_id FROM real_estate_agency WHERE agency_name = 'Premium Realty')),
    ('Lilit', '+32456712345', 'MNO-45Z', 'lilit@gmail.com', '$2a$12$pI9r.TVmrhTiNxgqCJ342u7s.VYooYWmJR3Ji.tVLk4mxS7NM1rWu', 'USER',(SELECT agency_id FROM real_estate_agency WHERE agency_name = 'Budget Homes')),
    ('Gloria', '+32465873901', 'QRS-88W', 'gloria@gmail.com', '$2a$12$lZ9ya.rr/6CWH3X1t6XR5OQJetSuEHKUg7lxJe2aCE.HqPqrvSM2i','USER' ,(SELECT agency_id FROM real_estate_agency WHERE agency_name = 'City plus')),
    ('Ethan', '+32467238945', 'TUV-34R', 'Ethan@student.kdg.be', '$2a$12$GWW8LAvyxjGpY0h.wXJ5meHT1QZ/NTJyCkxOd3ElLS0z4iu4N582K', 'USER',(SELECT agency_id FROM real_estate_agency WHERE agency_name = 'Immovlan')),
    ('Liam', '+32489123456', 'WXY-67P', 'Liam@gmail.com', '$2a$12$CvCe.fLzAcQ1B6kRJXoOPuCLvBkxrlj0/TS.7J/bY/PxI3UXKzS9u', 'USER',(SELECT agency_id FROM real_estate_agency WHERE agency_name = 'Immobob')),
    ('Charlotte', '+32456789012', 'ZAB-12Y', 'Charlotte@student.kdg.be', '', 'USER',(SELECT agency_id FROM real_estate_agency WHERE agency_name = 'Premium Realty')),
    ('Amara', '+32467812349', 'DEF-56T', 'Amara@gmail.com', '', 'USER',(SELECT agency_id FROM real_estate_agency WHERE agency_name = 'Budget Homes')),
    ('Nathan', '+32456987654', 'GHI-78M', 'Nathan@student.kdg.be', '', 'USER',(SELECT agency_id FROM real_estate_agency WHERE agency_name = 'City plus')),
    ('Emma', '+32468754321', 'JKL-99N', 'Emma@gmail.com', '', 'USER',(SELECT agency_id FROM real_estate_agency WHERE agency_name = 'Immovlan'));


INSERT INTO property(property_name, image, address, price, type, size, status, number_of_rooms, date_listed)
VALUES ('Modern Haven', '/images/properties/Modern Haven.jpg', 'Nationalstraat 1 box 2', 1400000.0, 'RESIDENTIAL', 3000.0, 'SOLD', 18, '2024-04-19'),
       ('Cityview Residence', '/images/properties/Cityview Residence.jpg', 'Steynstraat 1 box 2', 3000000.0, 'RESIDENTIAL', 7000.0, 'AVAILABLE', 10, '2025-01-01'),
       ('Business Plaza', '/images/properties/Business Plaza.jpg', 'Bolivarplaats 1 box 2', 15000000.0, 'COMMERCIAL', 8000.0, 'RENTED', 25, '2024-06-28'),
       ('Suburban Retreat', '/images/properties/Suburban Retreat.jpeg', 'Boukunststraat 1 box 2', 1400000.0, 'RESIDENTIAL', 2000.0, 'LEASED', 8, '2024-07-12'),
       ('Weekend Escape', '/images/properties/Weekend Escape.jpeg', 'Ruggeveld 1 box 2', 1400000.0, 'RECREATIONAL', 2000.0, 'AVAILABLE', 8, '2025-01-04'),
       ('Greenfield Factory', '/images/properties/Greenfield Factory.jpg', 'Groenplats 3 box 5', 6000000.0, 'INDUSTRIAL', 12000.0, 'AVAILABLE', 40, '2022-01-01'),
       ('Vacation Cabin', '/images/properties/Cabin.jpg', 'flyhigh 3 block 12', 450000.0, 'RESIDENTIAL', 4000.0, 'AVAILABLE', 4, '2022-01-01'),
       ('The View', '/images/properties/Penthouse.jpg', 'No 19 Stevens lane', 10000000.0, 'COMMERCIAL', 20000.0, 'AVAILABLE', 30, '2024-12-01'),
       ('Relaxation Haven', '/images/properties/Suite.jpg', 'Groenplats 10 box 9', 2500000.0, 'RESIDENTIAL', 7000.0, 'AVAILABLE', 7, '2024-11-11'),
       ( 'Prime Villa', '/images/properties/Apartment.jpg', 'NO 12 Main street', 5000000.0, 'RECREATIONAL', 12000.0, 'AVAILABLE', 8, '2024-12-25'),
       ('Sunny Haven', '/images/properties/Bungalow.jpg', 'N0 3 third street', 120000.0, 'RECREATIONAL', 1500, 'AVAILABLE', 2, '2024-12-27'),
       ('Aqua Oasis', '/images/properties/Aqua Oasis.jpg', 'No 35 Main Street', 3500000.0, 'INDUSTRIAL', 4500.0, 'AVAILABLE', 5, '2024-01-01'),
       ('Forest Edge Retreat', '/images/properties/Forest Retreat.jpg', 'No 3 Cresent close off road', 2000000.0, 'RECREATIONAL', 4000.0, 'AVAILABLE', 4, '2024-01-01'),
       ('Lake Villa', '/images/properties/Lake Villa.jpg', '12 Handel Second Street', 3000000.0, 'COMMERCIAL', 6000.0, 'AVAILABLE', 5, '2024-01-01'),
       ('Apartment Residence', '/images/properties/Apartment2.jpg', 'Groenplats 3 box 3', 200000.0, 'RESIDENTIAL', 1000.0, 'AVAILABLE', 3, '2024-11-01');


INSERT INTO real_estate_agency(agency_name, address, contact_info, city,image)
VALUES ('Premium Realty', '789 High Street', 'premium@realty.com', 'Atlanta', '/images/agencies/Premium Realty.jpg'),
       ('Budget Homes', '555 Low Street', 'budget@homes.com', 'New York', '/images/agencies/Budget Homes.jpg'),
       ('City plus', '777 Middle Street', 'cityplus@plus.com', 'London', '/images/agencies/City Plus.jpg'),
       ('Immovlan', '888 Main Street', 'immovlan@web.com', 'Brussels', '/images/agencies/Immovlan.jpg'),
       ('Immobob', '999 Nationalstraat', 'immobob@web.com', 'Antwerp', '/images/agencies/Land And Home.jpg');

INSERT INTO agent_properties(agent_id, property_id)
VALUES ((SELECT agent_id FROM Agent WHERE agent_name = 'Kingsley'), (SELECT property_id FROM Property WHERE property_name = 'Modern Haven')),
       ((SELECT agent_id FROM Agent WHERE agent_name = 'Lesley'), (SELECT property_id FROM Property WHERE property_name = 'Cityview Residence')),
       ((SELECT agent_id FROM Agent WHERE agent_name = 'Fortune'), (SELECT property_id FROM Property WHERE property_name = 'Business Plaza')),
       ((SELECT agent_id FROM Agent WHERE agent_name = 'Aisosa'), (SELECT property_id FROM Property WHERE property_name = 'Suburban Retreat')),
       ((SELECT agent_id FROM Agent WHERE agent_name = 'Maimuna'), (SELECT property_id FROM Property WHERE property_name = 'Weekend Escape')),
       ((SELECT agent_id FROM Agent WHERE agent_name = 'Alex'), (SELECT property_id FROM Property WHERE property_name = 'Greenfield Factory')),
       ((SELECT agent_id FROM Agent WHERE agent_name = 'Abir'), (SELECT property_id FROM Property WHERE property_name = 'Vacation Cabin')),
       ((SELECT agent_id FROM Agent WHERE agent_name = 'Lilit'), (SELECT property_id FROM Property WHERE property_name = 'The View')),
       ((SELECT agent_id FROM Agent WHERE agent_name = 'Gloria'), (SELECT property_id FROM Property WHERE property_name = 'Relaxation Haven')),
       ((SELECT agent_id FROM Agent WHERE agent_name = 'Ethan'), (SELECT property_id FROM Property WHERE property_name = 'Prime Villa')),
       ((SELECT agent_id FROM Agent WHERE agent_name = 'Liam'), (SELECT property_id FROM Property WHERE property_name = 'Sunny Haven')),
       ((SELECT agent_id FROM Agent WHERE agent_name = 'Charlotte'), (SELECT property_id FROM Property WHERE property_name = 'Modern Haven')),
       ((SELECT agent_id FROM Agent WHERE agent_name = 'Amara'), (SELECT property_id FROM Property WHERE property_name = 'Cityview Residence')),
       ((SELECT agent_id FROM Agent WHERE agent_name = 'Nathan'), (SELECT property_id FROM Property WHERE property_name = 'Business Plaza')),
       ((SELECT agent_id FROM Agent WHERE agent_name = 'Emma'), (SELECT property_id FROM Property WHERE property_name = 'Suburban Retreat'));