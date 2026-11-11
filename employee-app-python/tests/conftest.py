from pathlib import Path
from employee_app.testsql import reset
import pytest

@pytest.fixture(autouse=True)
def setup_test_database(monkeypatch):
    conftest_dir = Path(__file__).resolve().parent
    test_db_path = conftest_dir / "test_em.db"
    monkeypatch.setenv("EM_DB_PATH", str(test_db_path))
    reset()
    
    yield