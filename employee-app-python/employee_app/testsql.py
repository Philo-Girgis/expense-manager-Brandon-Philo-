# employee_app/db.py
import os
import sys
import sqlite3
from contextlib import contextmanager
from tabulate import tabulate
from pathlib import Path

def getRoot():
    # Get the absolute path of the current file or current working directory
    # Use Path(__file__).resolve() if running from a script file
    current_path = Path(__file__).resolve()
    temp = False
    
    # Loop through all parent directories upward
    for parent in current_path.parents:
        if temp:
            return parent
        if parent.name.lower() == "employee-app-python".lower():
            temp = True
            
    return temp

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
    HERE = getRoot()
    if not HERE:
        print("cannot find root")
        sys.exit(1)

    SCHEMA = os.path.join(HERE, "sql", "schema.sql")
    SEED = os.path.join(HERE, "sql", "seed.sql")
    TEST = os.path.join(HERE, "sql", "validation", "test.sql")
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