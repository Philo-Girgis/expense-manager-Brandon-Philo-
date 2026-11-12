import pytest
from employee_app.main import main
from unittest.mock import patch
from datetime import date
#python -m pytest


def test_view_expenses(capsys):
    inputs = iter(["employee1", "e1password123",
                "2"])
    def fake_input(prompt):
        print(prompt)
        try:
            return next(inputs)
        except StopIteration:
            raise SystemExit(0)

    #act
    with patch('builtins.input', side_effect=fake_input):
        with pytest.raises(SystemExit):
            main()
    captured = capsys.readouterr()

    #assert
    assert "5     $180.00     Lodging" in captured.out
    assert "1     $125.50     Travel" in captured.out
    assert "17    $150.00     Training" in captured.out
    assert "13    $75.00      Travel" in captured.out
    assert "9     $56.40      Meals" in captured.out
    assert "25    $45.00      Office Supplies" in captured.out
    assert "21    $780.00     Lodging" in captured.out
    assert "Total expenses: 7" in captured.out

def test_edit_pending_expense(capsys):
    inputs = iter(["employee1", "e1password123",
                "3", "5", "150", "2", "Dinner with client", date.today().isoformat(), "2"])
    def fake_input(prompt):
        print(prompt)
        try:
            return next(inputs)
        except StopIteration:
            raise SystemExit(0)

    #act
    with patch('builtins.input', side_effect=fake_input):
        with pytest.raises(SystemExit):
            main()
    captured = capsys.readouterr()

    #assert
    assert "Expense updated successfully!" in captured.out
    assert "Expense ID: 5" in captured.out
    assert "Status: Pending approval" in captured.out
    assert "5     $150.00     Meals" in captured.out

def test_edit_approved_expense(capsys):
    inputs = iter(["employee1", "e1password123",
                "2", "3", "25", "150", "2", "Dinner with client", date.today().isoformat()])
    def fake_input(prompt):
        print(prompt)
        try:
            return next(inputs)
        except StopIteration:
            raise SystemExit(0)

    #act
    with patch('builtins.input', side_effect=fake_input):
        with pytest.raises(SystemExit):
            main()
    captured = capsys.readouterr()

    #assert
    assert "25    $45.00      Office Supplies" in captured.out
    assert "Expense not found, or it is not eligible for editing." in captured.out

def test_edit_another_employees_expense(capsys):
    inputs = iter(["employee1", "e1password123",
                "3", "2", "150", "2", "Dinner with client", date.today().isoformat()])
    def fake_input(prompt):
        print(prompt)
        try:
            return next(inputs)
        except StopIteration:
            raise SystemExit(0)

    #act
    with patch('builtins.input', side_effect=fake_input):
        with pytest.raises(SystemExit):
            main()
    captured = capsys.readouterr()

    #assert
    assert "Expense not found, or it is not eligible for editing." in captured.out

def test_delete_pending_expense(capsys):
    inputs = iter(["employee1", "e1password123",
                "4", "5", "2"])
    def fake_input(prompt):
        print(prompt)
        try:
            return next(inputs)
        except StopIteration:
            raise SystemExit(0)

    #act
    with patch('builtins.input', side_effect=fake_input):
        with pytest.raises(SystemExit):
            main()
    captured = capsys.readouterr()

    #assert
    assert "Expense deleted successfully!" in captured.out
    assert "Deleted expense ID: 5" in captured.out
    assert "5     $180.00     Lodging" not in captured.out
    assert "Total expenses: 6" in captured.out


def test_delete_denied_expense(capsys):
    inputs = iter(["employee1", "e1password123",
                "4", "21", "2"])
    def fake_input(prompt):
        print(prompt)
        try:
            return next(inputs)
        except StopIteration:
            raise SystemExit(0)

    #act
    with patch('builtins.input', side_effect=fake_input):
        with pytest.raises(SystemExit):
            main()
    captured = capsys.readouterr()

    #assert
    assert "Unable to delete expense: Expense not found, or it is not eligible for deletion." in captured.out
    assert "21    $780.00     Lodging" in captured.out
    assert "Total expenses: 7" in captured.out

def test_existing_review_history(capsys):
    inputs = iter(["employee1", "e1password123", "5"])
    def fake_input(prompt):
        print(prompt)
        try:
            return next(inputs)
        except StopIteration:
            raise SystemExit(0)

    #act
    with patch('builtins.input', side_effect=fake_input):
        with pytest.raises(SystemExit):
            main()
    captured = capsys.readouterr()

    #assert
    assert "Review Date:        2026-10-20" in captured.out
    assert "Review Date:        2026-10-16" in captured.out
    assert "Review Date:        2026-10-12" in captured.out
    assert "Review Date:        2026-10-09" in captured.out
    assert "Review Date:        2026-10-05" in captured.out
    assert "Total reviewed expenses: 5" in captured.out

def test_review_history_with_no_expenses(capsys):
    inputs = iter(["employee5", "e5password123", "5"])
    def fake_input(prompt):
        print(prompt)
        try:
            return next(inputs)
        except StopIteration:
            raise SystemExit(0)

    #act
    with patch('builtins.input', side_effect=fake_input):
        with pytest.raises(SystemExit):
            main()
    captured = capsys.readouterr()

    #assert
    assert "You have no reviewed expenses yet." in captured.out

def test_menu_exit(capsys):
    inputs = iter(["employee5", "e5password123", "0"])
    def fake_input(prompt):
        print(prompt)
        try:
            return next(inputs)
        except StopIteration:
            raise SystemExit(0)

    #act
    with patch('builtins.input', side_effect=fake_input):
        with pytest.raises(SystemExit):
            main()
    captured = capsys.readouterr()

    #assert
    assert "Exiting employee app. Goodbye!" in captured.out
