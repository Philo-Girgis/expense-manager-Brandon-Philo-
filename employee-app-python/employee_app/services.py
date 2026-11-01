from datetime import datetime
from employee_app.db import get_connection
from employee_app.validators import validate_amount, validate_category, validate_description, validate_expense_date


def submit_expense(user_id, amount, category_id, description, expense_date):
    """
    Submit an employee expense.

    Validates the expense details, inserts the expense, and creates an initial pending approval record.
    """

    # Validate all input values.
    amount = validate_amount(amount)
    category_id = validate_category(category_id)
    description = validate_description(description)
    expense_date = validate_expense_date(expense_date)

    # Generate the creation timestamp.
    created_at = datetime.now().strftime("%Y-%m-%d %H:%M:%S")

    # Insert the expense and its pending approval.
    connection = get_connection()

    cursor = connection.execute(
        """
        INSERT INTO expenses (
            user_id,
            category_id,
            amount,
            description,
            expense_date,
            created_at
        )
        VALUES (?, ?, ?, ?, ?, ?)
        """,
        (
            user_id,
            category_id,
            float(amount),
            description,
            expense_date,
            created_at,
        ),
    )

    expense_id = cursor.lastrowid

    connection.execute(
        """
        INSERT INTO approvals (expense_id, status)
        VALUES (?, 'pending')
        """,
        (expense_id,),
    )

    connection.commit()

    return expense_id

