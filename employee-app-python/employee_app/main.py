import sys
from employee_app.testsql import get_connection
from employee_app.menu import employee_menu


def main():

    failed = 0

    with get_connection() as connection:

        # Employee login.
        while True:

            user = input("Please enter your username: ")
            password = input("Please enter your password: ")

            if not user.strip() or not password.strip():
                print("Username or password is empty. Please try again.")
                continue

            employee = connection.execute(
                """
                SELECT id, username, password, role, full_name
                FROM users
                WHERE username = ? AND password = ?
                """,
                (user.strip(), password.strip())
            ).fetchone()

            if employee is None:
                failed += 1
                print("Invalid username or password.")

            elif employee["role"] != "employee":
                print("This account does not have access to this app.")
                sys.exit(1)

            else:
                failed = 0
                print(f"\nWelcome, {employee['full_name']}!")

                # Open the employee menu after successful login.
                employee_menu(employee["id"])

            if failed == 3:
                print("Too many failed attempts.")
                sys.exit(1)


if __name__ == "__main__":
    main()