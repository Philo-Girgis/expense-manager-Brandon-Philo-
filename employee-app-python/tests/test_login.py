import pytest
from employee_app.main import main
from unittest.mock import patch
#python -m pytest


def test_valid_employee_login(capsys):
    inputs = iter(['employee1', 'e1password123'])
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
    assert "Welcome, Philip Daniels" in captured.out

def test_valid_user_invalid_pass(capsys):
    inputs = iter(['employee1', 'password1'])
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
    assert "Invalid username or password." in captured.out

def test_valid_pass_invalid_user(capsys):
    inputs = iter(['emp1', 'e1password123'])
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
    assert "Invalid username or password." in captured.out

def test_invalid_login(capsys):
    inputs = iter(['emp1', 'emp1password'])
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
    assert "Invalid username or password." in captured.out

def test_empty_password_and_empty_username(capsys):
    inputs = iter(['', ''])
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
    assert "Username or password is empty. Please try again." in captured.out

def test_three_failed_attempts(capsys):
    inputs = iter(['emp1', 'emp1password', 'emp1', 'emp1password','emp1', 'emp1password'])
    def fake_input(prompt):
        print(prompt)
        return next(inputs)

    #act
    with patch('builtins.input', side_effect=fake_input):
        with pytest.raises(SystemExit):
            #must raise system exist by itself
            main()
    captured = capsys.readouterr()

    #assert
    assert "Too many failed attempts." in captured.out

def test_manager_in_employee_login(capsys):
    inputs = iter(['manager1', 'm1password123'])
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
    assert "This account does not have access to this app." in captured.out

