from pathlib import Path
from employee_app.testsql import reset
import pytest

@pytest.fixture(autouse=True)
def setup_test_database(monkeypatch, request):
    if request.node.get_closest_marker("disable_autouse"):
        yield  # Skip the setup/teardown logic
        return
    conftest_dir = Path(__file__).resolve().parent
    test_db_path = conftest_dir / "test_em.db"
    monkeypatch.setenv("EM_DB_PATH", str(test_db_path))
    reset()
    
    yield