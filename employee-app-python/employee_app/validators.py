import re
from datetime import date, datetime
from decimal import Decimal, InvalidOperation


def validate_amount(value):
    """VR-01: Amount must be greater than 0, but less than or equal to 10,000 and contain no more than 2 decimal places."""

    value = str(value).strip()

    try:
        amount = Decimal(value)
    except InvalidOperation:
        raise ValueError("Please enter a valid numeric amount.")

    if amount <= 0:
        raise ValueError("Amount must be greater than 0.")

    if amount > 10000:
        raise ValueError("Amount cannot exceed 10000.")

    if not re.fullmatch(r"\d+(\.\d{1,2})?", value):
        raise ValueError("Amount must contain no more than 2 decimal places.")

    return amount


def validate_description(value):
    """VR-02: Description must contain 1 to 200 characters after trimming."""

    description = value.strip()

    if not 1 <= len(description) <= 200:
        raise ValueError("Description must be between 1 and 200 characters.")

    return description


def validate_category(value):
    """VR-03: Category must be a whole number from 1 to 6"""

    try:
        category_id = int(value)
    except (ValueError, TypeError):
        raise ValueError("Category must be a whole number between 1 to 6.")

    if str(value).strip() != str(category_id) or not 1 <= category_id <= 6:
        raise ValueError("Please select a category from 1 to 6.")

    return category_id


def validate_expense_date(value):
    """VR-04: Date must be valid, not in the future, and no more than 90 days old."""

    value = value.strip()

    if not re.fullmatch(r"\d{4}-\d{2}-\d{2}", value):
        raise ValueError("Date must use YYYY-MM-DD format.")

    try:
        expense_date = datetime.strptime(value, "%Y-%m-%d").date()
    except ValueError:
        raise ValueError("Please enter a valid calendar date.")

    today = date.today()
    earliest_date = date.fromordinal(today.toordinal() - 90)

    if expense_date > today:
        raise ValueError("Expense date cannot be in the future.")

    if expense_date < earliest_date:
        raise ValueError("Expense date cannot be more than 90 days old.")

    return expense_date.isoformat()


#amount = validate_amount(input("Enter amount: "))
#category_id = validate_category(input("Select category: "))
#description = validate_description(input("Enter description: "))
#expense_date = validate_expense_date(input("Enter date (YYYY-MM-DD): "))