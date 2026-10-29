create table users (
    id integer primary key,
    username text not null unique,
    password text not null,
    role text not null check (role in ('employee', 'manager')),
    full_name text not null
);

create table categories (
    id integer primary key,
    name text not null unique
);

create table expenses (
    id integer primary key,
    user_id integer not null,
    category_id integer not null,
    amount real not null check (amount > 0 and amount <= 10000 and amount = round(amount, 2)),
    description text not null check (length(trim(description)) between 1 and 200),
    expense_date text not null,
    created_at text not null,

    foreign key (user_id) references users(id),
    foreign key (category_id) references categories(id)
);

create table approvals (
    id integer primary key,
    expense_id integer not null unique,
    status text not null default 'pending' check (status in ('pending', 'approved', 'denied')),
    reviewer_id integer null,
    comment text null,
    review_date text null,

    foreign key (expense_id) references expenses(id),
    foreign key (reviewer_id) references users(id),

    check (
        (status = 'pending' and reviewer_id is null and review_date is null)
        or
        (status in ('approved', 'denied') and reviewer_id is not null and review_date is not null)
    )
);