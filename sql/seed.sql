insert into users(id, username, password, role, full_name)
values(1, "pg123", "123abc", "employee", "philip daniels");

insert into categories(id, name)
values(1, "Travel");

insert into expenses(id, user_id, category_id, amount, description, expense_date, created_at)
values(1, 1, 1, 100.00, "description", "2001-09-23", "2001-09-23 02:02:02");

insert into approvals(id, expense_id, status, reviewer_id, comment, review_date)
values(1, 1, "denied", 1, "comment", "2001-09-23");
