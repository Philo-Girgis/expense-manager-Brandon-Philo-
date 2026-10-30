select * from users;
Select * from categories;
select * from expenses;
select * from approvals;


SELECT role, COUNT(*) AS total
FROM users
GROUP BY role
order by role desc;

SELECT COUNT(*) AS total_categories
FROM categories;

SELECT COUNT(*) AS total_expenses
FROM expenses;

SELECT status, COUNT(*) AS total
FROM approvals
GROUP BY status;

SELECT id, length(description) AS description_length
FROM expenses
WHERE length(description) = 200;

SELECT
    id,
    user_id,
    category_id,
    printf('%.2f', amount) AS amount,
    description,
    expense_date,
    created_at
FROM expenses;

-- Validation queries
-- Expenses with no matching user.
SELECT e.id AS expense_id, e.user_id
FROM expenses e
LEFT JOIN users u ON e.user_id = u.id
WHERE u.id IS NULL;

-- Expenses with no approval row.
SELECT e.id AS expense_id
FROM expenses e
LEFT JOIN approvals a ON e.id = a.expense_id
WHERE a.id IS NULL;

-- pending approvals that have a reviewer or review date.
SELECT id AS approval_id, expense_id, reviewer_id, review_date
FROM approvals
WHERE status = 'pending' AND (reviewer_id IS NOT NULL OR review_date IS NOT NULL);

-- approved/denied approvals missing a reviewer or review date, or reviewed by a non-manager.
SELECT a.id AS approval_id, a.expense_id, a.status, a.reviewer_id, a.review_date
FROM approvals a
LEFT JOIN users u ON a.reviewer_id = u.id
WHERE a.status IN ('approved', 'denied') AND (a.reviewer_id IS NULL OR a.review_date IS NULL OR u.id IS NULL OR u.role <> 'manager');

-- Approvals whose status or review_date breaks the shared data contract (8.3).
SELECT id AS approval_id, status, review_date
FROM approvals
WHERE status IS NULL OR status NOT IN ('pending', 'approved', 'denied') OR (status IN ('approved', 'denied') AND review_date IS NULL)
    OR (review_date IS NOT NULL AND (LENGTH(review_date) <> 10 OR review_date NOT GLOB '[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]'));