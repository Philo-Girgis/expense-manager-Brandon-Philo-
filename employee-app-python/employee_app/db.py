import sqlite3
from pathlib import Path


def get_root():
    """Find the project root directory."""

    current_path = Path(__file__).resolve()

    for parent in current_path.parents:
        if parent.name.lower() == "employee-app-python":
            return parent.parent

    raise FileNotFoundError("Cannot find project root.")


# One shared in-memory database connection.
_connection = None


def initialize_database():
    """Create the in-memory database and load schema and seed data."""

    global _connection

    HERE = get_root()

    SCHEMA = HERE / "sql" / "schema.sql"
    SEED = HERE / "sql" / "seed.sql"

    _connection = sqlite3.connect(":memory:", timeout=5)
    _connection.row_factory = sqlite3.Row

    _connection.execute("PRAGMA foreign_keys = ON")

    with open(SCHEMA, encoding="utf-8") as file:
        _connection.executescript(file.read())

    with open(SEED, encoding="utf-8") as file:
        _connection.executescript(file.read())

    return _connection


def get_connection():
    """Return the existing shared database connection."""

    if _connection is None:
        raise RuntimeError("Database has not been initialized.")

    return _connection


def get_categories():
    """Retrieve all available expense categories."""

    connection = get_connection()

    categories = connection.execute(
        "SELECT id, name FROM categories ORDER BY id"
    ).fetchall()

    return categories
