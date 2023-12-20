// database.js
const sqlite3 = require('sqlite3');

let db = null;

//Connect to database
function connectToDatabase(databasePath) {
    return new Promise((resolve, reject) => {
        db = new sqlite3.Database('/home/joanr/project/ADPractica5/database.db', sqlite3.OPEN_READWRITE, (err) => {
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
                } else {
                    // Check if a user was found in the database
                    if (row) {
                        resolve(true); // User and password match
                    } else {
                        resolve(false); // User not found or password doesn't match
                    }                
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
                    resolve(true);
                }
            }
        );
    });
}

//List images
function getImages() {
    return new Promise((resolve, reject) => {
        db.all('SELECT * FROM IMAGE', (error, results) => {
            if (error) {
                reject(error);
            } else {
                resolve(results);
            }
        });
    });
}

// Check if a title is already used by the creator
function titleIsUsed(title, creator) {
    return new Promise((resolve, reject) => {
        const query = 'SELECT * FROM IMAGE WHERE TITLE = ? AND CREATOR = ?';
        db.get(query, [title, creator], (error, row) => {
            if (error) {
                reject(error);
            } else {
                resolve(row ? true : false);
            }
        });
    });
}

// Modify an existing image
function modifyImage(title, oldtitle, description, keywords, author, creator, filename) {
    return new Promise((resolve, reject) => {
        const query = 'UPDATE IMAGE SET TITLE = ?, DESCRIPTION = ?, KEYWORDS = ?, AUTHOR = ?, FILENAME = ? WHERE TITLE = ? AND CREATOR = ?';
        db.run(query, [title, description, keywords, author, filename, oldtitle, creator], function(error) {
            if (error) {
                reject(error);
            } else {
                resolve(this.changes);
            }
        });
    });
}

function deleteImage(filename) {
    return new Promise((resolve, reject) => {
        const query = 'DELETE FROM IMAGE WHERE FILENAME = ?';
        db.run(query, [filename], function(error) {
            if (error) {
                reject(error);
            } else {
                resolve(this.changes);
            }
        });
    });
}

function searchImages(searchWord) {
    return new Promise((resolve, reject) => {
        const query = "SELECT * FROM image WHERE TITLE LIKE ? OR KEYWORDS LIKE ? OR CREATOR LIKE ? OR AUTHOR LIKE ?";;
        db.all(query, [`%${searchWord}%`, `%${searchWord}%`, `%${searchWord}%`, `%${searchWord}%`], function(error, rows) {
            if (error) {
                reject(error);
            } else {
                resolve(rows);
            }
        });
    });
}

module.exports = {
    connectToDatabase,
    disconnectFromDatabase,
    login,
    registerUser,
    registerImage,
    getImages,
    titleIsUsed,
    modifyImage,
    deleteImage,
    searchImages,
};