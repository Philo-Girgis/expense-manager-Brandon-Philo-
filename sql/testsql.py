# employee_app/db.py
import os
import sqlite3
from contextlib import contextmanager
from tabulate import tabulate

HERE = os.path.dirname(os.path.abspath(__file__))
SCHEMA = os.path.join(HERE, "schema.sql")
SEED = os.path.join(HERE, "seed.sql")
TEST = os.path.join(HERE, "validation", "test.sql")

@contextmanager
def get_connection():
    conn = sqlite3.connect(":memory:", timeout=5)
    conn.row_factory = sqlite3.Row
    conn.execute("PRAGMA foreign_keys = ON")
    try:
        yield conn
        conn.commit()
    except Exception:
        conn.rollback()
        raise
    finally:
        conn.close()

if __name__ == "__main__":
    
    with get_connection() as conn:
        with open(SCHEMA, encoding="utf-8") as f:
            conn.executescript(f.read())
        with open(SEED, encoding="utf-8") as f:
            conn.executescript(f.read())
        with open(TEST, encoding="utf-8") as f:
            queries = f.read().split(";")
            for query in queries:
                if query.strip():
                    cursor = conn.execute(query)
                    rows = cursor.fetchall()
                    if rows:
                        headers = [col[0] for col in cursor.description]
                        table_data = [list(row) for row in rows]
                        
                        print(tabulate(table_data, headers=headers, tablefmt="pretty"))
                    else:
                        print("No records found.")