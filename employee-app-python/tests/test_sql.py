import os, pytest
from employee_app.testsql import get_connection, getRoot

@pytest.mark.disable_autouse #check the actual database and not the temp
def test_sql_validation():
    HERE = getRoot()
    if not HERE:
        assert False, "cannot find root"
    TEST = os.path.join(HERE, "sql", "validation", "test.sql")
    with get_connection() as conn:
        with open(TEST, encoding="utf-8") as f:
            queries = f.read().split(";")
            total_queries = 0
            for query in queries:
                if query.strip():
                    total_queries += 1
                    cursor = conn.execute(query)
                    rows = cursor.fetchall()
                    assert rows == [], "row is not empty"
            if total_queries == 5:
                return
            assert False, "wrong number of queries"
        assert False, "no file opened"
    assert False, "no connection established"
