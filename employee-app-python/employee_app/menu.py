from employee_app.db import get_categories
from employee_app.services import submit_expense, get_expenses


def display_menu():
    """Display the employee menu."""

    print("\n========== EMPLOYEE EXPENSE APP ==========")
    print("1. Submit expense")
    print("2. View my expenses")
    print("3. Edit pending expense")
    print("4. Delete pending expense")
    print("5. Review history")
    print("0. Exit")
    print("===========================================")


def submit_expense_menu(user_id):
    """Collect expense details and submit an expense."""

    print("\n========== SUBMIT AN EXPENSE ==========")

    try:
        categories = get_categories()

        print("\nAvailable Categories:")
        for category in categories:
            print(f"{category['id']}. {category['name']}")

        amount = input("\nEnter expense amount: ")
        category_id = input("Select category (1-6): ")
        description = input("Enter description: ")
        expense_date = input("Enter expense date (YYYY-MM-DD): ")

        expense_id = submit_expense(user_id, amount, category_id, description, expense_date)

        print("\nExpense submitted successfully!")
        print(f"Expense ID: {expense_id}")
        print("Status: Pending approval")

    except ValueError as error:
        print(f"\nInvalid input: {error}")

    except Exception as error:
        print(f"\nUnable to submit expense: {error}")


def view_expenses_menu(user_id):
    """Display all expenses submitted by the logged-in employee."""

    print("\n========== MY EXPENSES ==========")

    try:
        expenses = get_expenses(user_id)

        if not expenses:
            print("\nYou have not submitted any expenses yet.")
            return

        print(
            f"\n{'ID':<6}"
            f"{'Amount':<12}"
            f"{'Category':<20}"
            f"{'Description':<82}"
            f"{'Date':<14}"
            f"{'Status':<12}"
        )

        print("-" * 146)

        for expense in expenses:
            print(
                f"{expense['id']:<6}"
                f"${expense['amount']:<11.2f}"
                f"{expense['category']:<20}"
                f"{expense['description'][:80]:<82}"
                f"{expense['expense_date']:<14}"
                f"{(expense['status'] or 'Unknown'):<12}"
            )

        print("-" * 146)
        print(f"Total expenses: {len(expenses)}")

    except Exception as error:
        print(f"\nUnable to retrieve expenses: {error}")



def employee_menu(user_id):
    """Display and handle employee menu selections."""

    while True:
        display_menu()

        choice = input("Enter your choice: ").strip()

        if choice == "1":
            submit_expense_menu(user_id)

        elif choice == "2":
            view_expenses_menu(user_id)

        elif choice == "3":
            print("\nEdit pending expenses - Not implemented yet.")

        elif choice == "4":
            print("\nDelete pending expenses - Not implemented yet.")

        elif choice == "5":
            print("\nReview history - Not implemented yet.")

        elif choice == "0":
            print("\nExiting employee app. Goodbye!")
            break

        else:
            print("\nInvalid choice. Please select an option from 0 to 5.")
