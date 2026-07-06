from flask import Flask, request, jsonify
import sqlite3

app=Flask(__name__)

@app.route("/")
def anasayfa():
    return """
    <html>
    <body>
        <h2>Giris Yap</h2>
        <form action="/login" method="POST">
            Email: <input type="text" name="email"><br><br>
            Sifre: <input type="password" name="password"><br><br>
            <input type="submit" value="Giris Yap">
        </form>
    </body>
    </html>
    """


def get_connection():
    return sqlite3.connect("users.db")

@app.route("/login",methods=["POST"])
def login():
    email=request.form.get("email","")
    password=request.form.get("password","")

    conn = get_connection()
    cursor = conn.cursor()

    #query = "SELECT * FROM users WHERE email = '" + email + "' AND password = '" + password + "'"
    #cursor.execute(query)
    # GUVENLI VERSIYON - Parametreli sorgu (SQL Injection'a kapali)
    query = "SELECT * FROM users WHERE email = ? AND password = ?"
    cursor.execute(query, (email, password))

    result = cursor.fetchall()
    print("Calistirilan sorgu:", query)

    if result:
        return jsonify({"basarili": True, "kullanicilar": result})
    else:
        return jsonify({"basarili": False, "mesaj": "Gecersiz email veya sifre"}), 401

    conn.close()

if __name__=="__main__":
    app.run(debug=True, port=5000)
