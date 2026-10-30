insert into users(id, username, password, role, full_name) values
(1, 'manager1', 'm1password123', 'manager', 'Alice Caldwell'),
(2, 'manager2', 'm2password123', 'manager', 'David Smith'),
(3, 'employee1', 'e1password123', 'employee', 'Philip Daniels'),
(4, 'employee2', 'e2password123', 'employee', 'Jane Doe'),
(5, 'employee3', 'e3password123', 'employee', 'Michael Johnson'),
(6, 'employee4', 'e4password123', 'employee', 'Charlotte Sinclair');

insert into categories(id, name) values
(1, 'Travel'),
(2, 'Meals'),
(3, 'Lodging'),
(4, 'Office Supplies'),
(5, 'Training'),
(6, 'Other');

insert into expenses(id, user_id, category_id, amount, description, expense_date, created_at) values
(1,  3, 1, 125.50, 'Taxi fare to a client meeting', '2026-10-20', '2026-10-20 09:15:00'),
(2,  4, 2,  18.75, 'Lunch with project teammates', '2026-10-21', '2026-10-21 12:30:00'),
(3,  5, 4,   0.01, 'Small office supply purchase', '2026-10-22', '2026-10-22 10:00:00'),
(4,  6, 5, 250.00, 'Software testing training course', '2026-10-23', '2026-10-23 14:20:00'),
(5,  3, 3, 180.00, 'Hotel accommodation for business trip', '2026-10-24', '2026-10-24 16:45:00'),
(6,  4, 6,  35.25, 'Business meeting parking fee', '2026-10-25', '2026-10-25 11:10:00'),
(7,  5, 4,  42.99, 'Printer paper and office supplies', '2026-10-26', '2026-10-26 13:05:00'),

(8,  6, 1, 320.00, 'Train tickets for a work conference', '2026-10-10', '2026-10-10 08:30:00'),
(9,  3, 2,  56.40, 'Client lunch meeting', '2026-10-11', '2026-10-11 12:00:00'),
(10, 4, 3, 450.00, 'Hotel stay for training event', '2026-10-12', '2026-10-12 15:30:00'),
(11, 5, 5, 199.99, 'Professional development workshop', '2026-10-13', '2026-10-13 09:45:00'),
(12, 6, 4,  85.60, 'Keyboard and mouse for office use', '2026-10-14', '2026-10-14 10:15:00'),
(13, 3, 1,  75.00, 'Rideshare to business appointment', '2026-10-15', '2026-10-15 07:50:00'),
(14, 4, 6,  24.50, 'Parking for work event', '2026-10-16', '2026-10-16 11:25:00'),
(15, 5, 2,  32.80, 'Meal during business travel', '2026-10-17', '2026-10-17 13:40:00'),
(16, 6, 3, 620.00, 'Accommodation during regional visit', '2026-10-18', '2026-10-18 17:00:00'),
(17, 3, 5, 150.00, 'Technical certification examination', '2026-10-19', '2026-10-19 09:00:00'),

(18, 4, 1, 210.00, 'Flight change fee for business trip', '2026-10-01', '2026-10-01 08:15:00'),
(19, 5, 2,  95.00, 'Restaurant bill for team dinner', '2026-10-02', '2026-10-02 19:30:00'),
(20, 6, 4,  67.25, 'Office supplies for team project', '2026-10-03', '2026-10-03 10:20:00'),
(21, 3, 3, 780.00, 'Hotel booking for a work event', '2026-10-04', '2026-10-04 16:00:00'),
(22, 4, 5, 120.00, 'Training materials and course fee', '2026-10-05', '2026-10-05 11:45:00'),
(23, 5, 6,  15.00, 'Miscellaneous business expense', '2026-10-06', '2026-10-06 14:10:00'),
(24, 6, 1, 10000.00, 'Maximum permitted expense boundary test', '2026-10-07', '2026-10-07 09:30:00'),
(25, 3, 4,  45.00,
     substr(
         'Office supplies for quarterly team training and project documentation. '
         || 'Office supplies for quarterly team training and project documentation. '
         || 'Office supplies for quarterly team training and project documentation.',
         1, 200
     ),
     '2026-10-08', '2026-10-08 10:30:00');

insert into approvals(id, expense_id, status, reviewer_id, comment, review_date) values
-- Pending: reviewer and review date must be NULL
(1,  1, 'pending',  NULL, NULL, NULL),
(2,  2, 'pending',  NULL, NULL, NULL),
(3,  3, 'pending',  NULL, NULL, NULL),
(4,  4, 'pending',  NULL, NULL, NULL),
(5,  5, 'pending',  NULL, NULL, NULL),
(6,  6, 'pending',  NULL, NULL, NULL),
(7,  7, 'pending',  NULL, NULL, NULL),

-- Approved: reviewer and review date are required
(8,  8, 'approved', 1, 'Approved for business travel.', '2026-10-11'),
(9,  9, 'approved', 2, 'Approved.', '2026-10-12'),
(10, 10, 'approved', 1, NULL, '2026-10-13'),
(11, 11, 'approved', 2, 'Training expense approved.', '2026-10-14'),
(12, 12, 'approved', 1, NULL, '2026-10-15'),
(13, 13, 'approved', 2, 'Approved for work travel.', '2026-10-16'),
(14, 14, 'approved', 1, 'Parking expense approved.', '2026-10-17'),
(15, 15, 'approved', 2, NULL, '2026-10-18'),
(16, 16, 'approved', 1, 'Approved accommodation expense.', '2026-10-19'),
(17, 17, 'approved', 2, 'Certification expense approved.', '2026-10-20'),

-- Denied: reviewer, review date, and a meaningful comment
(18, 18, 'denied', 1, 'Please provide the required travel authorization.', '2026-10-02'),
(19, 19, 'denied', 2, 'This meal expense exceeds the permitted limit.', '2026-10-03'),
(20, 20, 'denied', 1, 'Please provide an itemized purchase receipt.', '2026-10-04'),
(21, 21, 'denied', 2, 'The hotel booking lacks prior approval.', '2026-10-05'),
(22, 22, 'denied', 1, 'This training course was not pre-approved.', '2026-10-06'),
(23, 23, 'denied', 2, 'Please clarify the business purpose.', '2026-10-07'),
(24, 24, 'denied', 1, 'The amount requires additional supporting documentation.', '2026-10-08'),
(25, 25, 'denied', 2, 'Please provide an itemized office supply receipt.', '2026-10-09');
