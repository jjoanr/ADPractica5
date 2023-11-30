// database.js
const sqlite3 = require('sqlite3');

let db = null;

//Connect to database
function connectToDatabase(databasePath) {
    return new Promise((resolve, reject) => {
        db = new sqlite3.Database('/home/joanr/Practica5-AD/database.db', sqlite3.OPEN_READWRITE, (err) => {
            if (err) {
                reject(err);
                return;
            }
            console.log('Connected to the database');
            resolve();
        });
    });
}

//Disconect from database
function disconnectFromDatabase() {
    return new Promise((resolve, reject) => {
        if (db) {
            db.close((err) => {
                if (err) {
                    reject(err);
                    return;
                }
                console.log('Disconnected from the database');
                resolve();
            });
        } else {
            resolve(); // Resolve if already disconnected
        }
    });
}

//Login function
function login(username, password) {
    return new Promise((resolve, reject) => {
        db.get(
            'SELECT id_usuario, password FROM usuarios WHERE id_usuario = ? AND password = ?',
            [username, password],
            (err, row) => {
                if (err) {
                    reject(err);
                    resolve(false);
                } else {
                    resolve(true);
                }
            }
        );
    });
}

//Register user function
function registerUser(username, password) {
    return new Promise((resolve, reject) => {
        db.run(
            'INSERT INTO usuarios (id_usuario, password) VALUES (?, ?)',
            [username, password],
            function (err) {
                if (err) {
                    reject(err);
                    resolve(false);
                } else {
                    // Success - Return the ID of the inserted user
                    resolve(true);
                }
            }
        );
    });
}

//Register image
function registerImage(title, description, keywords, author, creator, creationDate, introductionDate, filename) {
      return new Promise((resolve, reject) => {
        db.run(
            'INSERT INTO IMAGE (TITLE, DESCRIPTION, KEYWORDS, AUTHOR, CREATOR, CAPTURE_DATE, STORAGE_DATE, FILENAME) VALUES (?, ?, ?, ?, ?, ?, ?, ?)', 
            [title, description, keywords, author, creator, creationDate, introductionDate, filename],
            function (err) {
                if (err) {
                    reject(err);
                    resolve(false);
                } else {
                    // Success - Return the ID of the inserted user
                    resolve(true);
                }
            }
        );
    });
}



module.exports = {
    connectToDatabase,
    disconnectFromDatabase,
    login,
    registerUser,
    registerImage
};