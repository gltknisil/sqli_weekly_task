import sqlite3

conn=sqlite3.connect("users.db")
cursor=conn.cursor()

cursor.execute("SELECT * FROM users")
sonuclar=cursor.fetchall()

for i in sonuclar:
    print(i)

conn.close()    