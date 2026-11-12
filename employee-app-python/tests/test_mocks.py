import pytest
from employee_app.main import main
from employee_app.menu import employee_menu
from unittest.mock import Mock, patch
#python -m pytest

#old tests that mock the connection


def test_invalid_login_old(capsys):
    with patch("employee_app.main.get_connection") as connection:
        # Arrange
        mock_conn = Mock()     
        connection.return_value.__enter__.return_value = mock_conn      
        mock_conn.execute.return_value.fetchone.return_value = None

        inputs = iter(['a', 'a'])
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
        assert "Invalid username or password" in captured.out

def test_empty_username_old(capsys):
    with patch("employee_app.main.get_connection") as connection:
        # Arrange
        mock_conn = Mock()     
        connection.return_value.__enter__.return_value = mock_conn      
        mock_conn.execute.return_value.fetchone.return_value = None

        inputs = iter([' ', 'a'])
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
        assert "Username or password is empty. Please try again" in captured.out

def test_empty_password_old(capsys):
    with patch("employee_app.main.get_connection") as connection:
        # Arrange
        mock_conn = Mock()     
        connection.return_value.__enter__.return_value = mock_conn      
        mock_conn.execute.return_value.fetchone.return_value = None

        inputs = iter(['a', ' '])
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
        assert "Username or password is empty. Please try again" in captured.out

def test_empty_password_and_empty_username_old(capsys):
    with patch("employee_app.main.get_connection") as connection:
        # Arrange
        mock_conn = Mock()     
        connection.return_value.__enter__.return_value = mock_conn      
        mock_conn.execute.return_value.fetchone.return_value = None

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
        assert "Username or password is empty. Please try again" in captured.out

def test_manager_in_employee_login_old(capsys):
    with patch("employee_app.main.get_connection") as connection:
        # Arrange
        mock_conn = Mock()   
        connection.return_value.__enter__.return_value = mock_conn    
        mock_conn.execute.return_value.fetchone.return_value = {
            "id": 1,
            "username": "user",
            "password": "pass",
            "role": "manager",
            "full_name": "jo"
        }

        inputs = iter(['user', 'pass'])
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
        assert "This account does not have access to this app" in captured.out


def test_valid_employee_login_old(capsys):
    with patch("employee_app.main.get_connection") as connection:
        # Arrange
        mock_conn = Mock()   
        connection.return_value.__enter__.return_value = mock_conn    
        mock_conn.execute.return_value.fetchone.return_value = {
            "id": 1,
            "username": "employee1",
            "password": "e1password123",
            "role": "employee",
            "full_name": "Philip Daniels"
        }

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

def test_three_failed_attempts_old(capsys):
    with patch("employee_app.main.get_connection") as connection:
        # Arrange
        mock_conn = Mock()     
        connection.return_value.__enter__.return_value = mock_conn      
        mock_conn.execute.return_value.fetchone.return_value = None

        inputs = iter(['a','a','b','b','c','c'])
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
        assert "Too many failed attempts" in captured.out

def test_three_empty_attempts_old(capsys):
    with patch("employee_app.main.get_connection") as connection:
        # Arrange
        mock_conn = Mock()     
        connection.return_value.__enter__.return_value = mock_conn      
        mock_conn.execute.return_value.fetchone.return_value = None

        inputs = iter([' ',' ',' ',' ',' ',' '])
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
        assert "Too many failed attempts" not in captured.out

def test_exit_menu_old(capsys):

    #arrange
    #gonna have to always exit at the end of my inputs when testing menu
    inputs = iter(['0'])
    def fake_input(prompt):
        print(prompt)
        return next(inputs)

    #act
    with patch('builtins.input', side_effect=fake_input):
        employee_menu(1)
    captured = capsys.readouterr()

    #assert
    assert "Exiting employee app. Goodbye!" in captured.out

def test_submit_expense_old(capsys):
    with patch("employee_app.menu.submit_expense") as expenseMock:
        #arrange
        mockcat = patch('employee_app.menu.get_categories').start()
        mockcat.return_value = {}
        expenseMock.return_value = 1
        #gonna have to always exit at the end of my inputs when testing menu
        inputs = iter(['1','a','b','c','d','0'])
        def fake_input(prompt):
            print(prompt)
            return next(inputs)

        #act
        with patch('builtins.input', side_effect=fake_input):
            employee_menu(1)
        captured = capsys.readouterr()

        #assert
        assert "Expense submitted successfully!" in captured.out

def test_submit_Invalid_expense_old(capsys):
    with patch("employee_app.menu.submit_expense", side_effect=ValueError("bad data")):
        #arrange
        mockcat = patch('employee_app.menu.get_categories').start()
        mockcat.return_value = {}
        #gonna have to always exit at the end of my inputs when testing menu
        inputs = iter(['1','a','b','c','d','0'])
        def fake_input(prompt):
            print(prompt)
            return next(inputs)

        #act
        with patch('builtins.input', side_effect=fake_input):
            employee_menu(1)
        captured = capsys.readouterr()

        #assert
        assert "Expense submitted successfully!" not in captured.out
        assert "Invalid input: bad data" in captured.out