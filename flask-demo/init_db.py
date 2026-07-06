import sqlite3

conn=sqlite3.connect("users.db")
cursor=conn.cursor()
cursor.execute("DROP TABLE IF EXISTS users")
cursor.execute("""
    CREATE TABLE users (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        email TEXT NOT NULL,
        password TEXT NOT NULL,
        full_name TEXT NOT NULL
    )
""")
test_kullanicilar = [
    ("ahmet@test.local", "1234", "Ahmet Yilmaz"),
    ("ayse@test.local", "sifre123", "Ayse Kaya"),
    ("admin@test.local", "admin999", "Sistem Yoneticisi"),
]

cursor.executemany(
    "INSERT INTO users (email, password, full_name) VALUES (?, ?, ?)",
    test_kullanicilar
)

conn.commit()
conn.close()

print("Veritabani olusturuldu ve test kullanicilari eklendi")