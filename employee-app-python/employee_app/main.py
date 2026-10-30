
import os
import sys
from testsql import get_connection
from testsql import getRoot


if __name__ == "__main__":
    failed = 0
    HERE = getRoot()
    if not HERE:
        print("cannot find root")
        sys.exit(1)
        
    SCHEMA = os.path.join(HERE, "sql", "schema.sql")
    SEED = os.path.join(HERE, "sql", "seed.sql")
    with get_connection() as conn:
        with open(SCHEMA, encoding="utf-8") as f:
            conn.executescript(f.read())
        with open(SEED, encoding="utf-8") as f:
            conn.executescript(f.read())
            
        while True:
            user = input("Please enter your username: ")
            pasw = input("Please enter your password: ")
            if not user.strip() or not pasw.strip():
                print("Username or Password is empty. Please try again")
                continue
            
            cursor = conn.execute(
    f"Select username, password, role, full_name from users where username = '{user.strip()}' and password = '{pasw.strip()}'")
            rows = cursor.fetchall()
            if not rows:
                failed +=1
                print("Invalid username or password")
            elif rows[0][2] != "employee":
                print("This account does not have access to this app")
                sys.exit(1)
            else:
                failed = 0
                print(f"Welcome, {rows[0][3]}")
            if failed == 3:
                print("Too many failed attempts")
                sys.exit(1)
