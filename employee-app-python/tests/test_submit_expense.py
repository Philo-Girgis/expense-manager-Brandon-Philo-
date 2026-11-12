import pytest
from employee_app.main import main
from unittest.mock import patch
from datetime import date, timedelta
#python -m pytest


def test_submit_valid_expense(capsys):
    inputs = iter(["employee1", "e1password123",
                "1", "100", "1", "Uber cost", "2026-11-06"])
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
    assert "Expense submitted successfully!" in captured.out
    assert "Expense ID: 26" in captured.out #25 expense ids in seed, new one is 26
    assert "Status: Pending approval" in captured.out


def test_submit_negative_amount(capsys):
    inputs = iter(["employee1", "e1password123",
                "1", "-1", "1", "Uber cost", "2026-11-06"])
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
    assert "Amount must be greater than 0." in captured.out


def test_submit_maximum_amount(capsys):
    inputs = iter(["employee1", "e1password123",
                "1", "10000.01", "1", "Uber cost", "2026-11-06"])
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
    assert "Amount cannot exceed 10000." in captured.out

def test_submit_more_than_2_decimals(capsys):
    inputs = iter(["employee1", "e1password123",
                "1", "0.001", "1", "Uber cost", "2026-11-06"])
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
    assert "Amount must contain no more than 2 decimal places." in captured.out

def test_submit_letter_amount(capsys):
    inputs = iter(["employee1", "e1password123",
                "1", "abc", "1", "Uber cost", "2026-11-06"])
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
    assert "Please enter a valid numeric amount." in captured.out

def test_submit_min_amount(capsys):
    inputs = iter(["employee1", "e1password123",
                "1", "0.01", "1", "Uber cost", "2026-11-06"])
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
    assert "Expense submitted successfully!" in captured.out
    assert "Expense ID: 26" in captured.out #25 expense ids in seed, new one is 26
    assert "Status: Pending approval" in captured.out

def test_submit_max_amount(capsys):
    inputs = iter(["employee1", "e1password123",
                "1", "10000", "1", "Uber cost", "2026-11-06"])
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
    assert "Expense submitted successfully!" in captured.out
    assert "Expense ID: 26" in captured.out #25 expense ids in seed, new one is 26
    assert "Status: Pending approval" in captured.out

def test_submit_below_permitted_category(capsys):
    inputs = iter(["employee1", "e1password123",
                "1", "100", "0", "Uber cost", "2026-11-06"])
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
    assert "Please select a category from 1 to 6." in captured.out

def test_submit_above_permitted_category(capsys):
    inputs = iter(["employee1", "e1password123",
                "1", "100", "7", "Uber cost", "2026-11-06"])
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
    assert "Please select a category from 1 to 6." in captured.out


def test_submit_empty_description(capsys):
    inputs = iter(["employee1", "e1password123",
                "1", "100", "3", "", "2026-11-06"])
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
    assert "Description must be between 1 and 200 characters." in captured.out


def test_submit_whitespace_description(capsys):
    inputs = iter(["employee1", "e1password123",
                "1", "100", "3", "     ", "2026-11-06"])
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
    assert "Description must be between 1 and 200 characters." in captured.out

def test_submit_over_max_description(capsys):
    desc = ""
    for i in range(201):
        desc+="a"
    inputs = iter(["employee1", "e1password123",
                "1", "100", "3", desc, "2026-11-06"])
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
    assert "Description must be between 1 and 200 characters." in captured.out

def test_submit_max_description(capsys):
    desc = ""
    for i in range(200):
        desc+="a"
    inputs = iter(["employee1", "e1password123",
                "1", "100", "3", desc, "2026-11-06"])
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
    assert "Expense submitted successfully!" in captured.out
    assert "Expense ID: 26" in captured.out #25 expense ids in seed, new one is 26
    assert "Status: Pending approval" in captured.out

def test_submit_min_description(capsys):
    inputs = iter(["employee1", "e1password123",
                "1", "100", "3", "a", "2026-11-06"])
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
    assert "Expense submitted successfully!" in captured.out
    assert "Expense ID: 26" in captured.out #25 expense ids in seed, new one is 26
    assert "Status: Pending approval" in captured.out

def test_submit_future_date(capsys):
    future = date.today()+timedelta(days = 1)
    inputs = iter(["employee1", "e1password123",
                "1", "100", "3", "Uber eats", future.isoformat()])
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
    assert "Expense date cannot be in the future." in captured.out

def test_submit_max_date(capsys):
    past = date.today()+timedelta(days = -90)
    inputs = iter(["employee1", "e1password123",
                "1", "100", "3", "Uber eats", past.isoformat()])
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
    assert "Expense submitted successfully!" in captured.out
    assert "Expense ID: 26" in captured.out #25 expense ids in seed, new one is 26
    assert "Status: Pending approval" in captured.out

def test_submit_past_max_date(capsys):
    past = date.today()+timedelta(days = -91)
    inputs = iter(["employee1", "e1password123",
                "1", "100", "3", "Uber eats", past.isoformat()])
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
    assert "Expense date cannot be more than 90 days old." in captured.out

