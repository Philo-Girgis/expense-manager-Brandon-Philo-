import pytest
from employee_app.main import main
from unittest.mock import Mock, patch
#python -m pytest

def test_invalid_login(capsys):
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
        assert "Invalid" in captured.out

def test_empty_username(capsys):
    with patch("employee_app.main.get_connection") as connection:
        # Arrange
        mock_conn = Mock()     
        connection.return_value.__enter__.return_value = mock_conn      
        mock_conn.execute.return_value.fetchone.return_value = None

        inputs = iter(['', 'a'])
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
        assert "empty" in captured.out

def test_empty_password(capsys):
    with patch("employee_app.main.get_connection") as connection:
        # Arrange
        mock_conn = Mock()     
        connection.return_value.__enter__.return_value = mock_conn      
        mock_conn.execute.return_value.fetchone.return_value = None

        inputs = iter(['a', ''])
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
        assert "empty" in captured.out

def test_valid_employee_login(capsys):
    with patch("employee_app.main.get_connection") as connection:
        # Arrange
        mock_conn = Mock()   
        connection.return_value.__enter__.return_value = mock_conn    
        mock_conn.execute.return_value.fetchone.return_value = {
            "id": 1,
            "username": "user",
            "password": "pass",
            "role": "employee",
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
        assert "Welcome" in captured.out