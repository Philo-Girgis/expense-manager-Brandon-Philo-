from employee_app.testsql import get_connection

def get_categories():
    """Retrieve all available expense categories."""

    with get_connection() as connection:
        categories = connection.execute(
            "SELECT id, name FROM categories ORDER BY id"
        ).fetchall()

        return categories
