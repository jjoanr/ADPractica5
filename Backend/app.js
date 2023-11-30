const express = require('express'); 
const multer = require('multer'); // Handle file uploads
const database = require('./database'); // Database functions file
const path = require('path');
const fs = require('fs');

  
const app = express(); 
const PORT = 3000; 

app.use(express.urlencoded({ extended: true }));

const uploadDir = '/home/joanr/Practica5-AD/images/';

// Multer configuration for file upload
const storage = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, uploadDir);
    },
    filename: function (req, file, cb) {
        const imageDetails = JSON.parse(req.body.jsonInput);
        const { filename } = imageDetails;
        cb(null, filename || file.originalname); // Use 'filename' attribute if available
    }
});

const upload = multer({ storage: storage });

// Login endpoint
app.post('/login', async (req, res) => {
    const { username, password } = req.body;
    try {
        // Check login credentials against the database
        database.connectToDatabase();
        const isLoggedIn = await database.login(username, password);
        database.disconnectFromDatabase();

        if (isLoggedIn) {
            return res.status(200).send(); // Successful login
        } else {
            return res.status(401).send(); // Unauthorized
        }
    } catch (error) {
        return res.status(500).send(error.message);
    }
});
   
// Register user endpoint
app.post('/registerUser', async (req, res) => {
    const { username, password } = req.body;
    try {
        // Register the user in the database
        database.connectToDatabase();
        const isRegistered = await database.registerUser(username, password);
        database.disconnectFromDatabase();

        if (isRegistered) {
            return res.status(200).send(); // Successful registration
        } else {
            return res.status(401).send(); // Unauthorized
        }
    } catch (error) {
        return res.status(500).send(error.message);
    }
});


// Register image endpoint
app.post('/register', upload.single('file'), async (req, res) => {
    const { jsonInput } = req.body;
    try {
        const imageDetails = JSON.parse(jsonInput);
    
        const { title, description, keywords, author, creator, creationDate, introductionDate, filename } = imageDetails;
    
        // Register image
        database.connectToDatabase();
        const saved = database.registerImage(title, description, keywords, author,
                                             creator, creationDate, introductionDate, filename)
        database.disconnectFromDatabase();

        if (!saved) {
        return res.status(500).send('Error registering image');
        }

        return res.status(200).send('Image registration successful');
    } catch (err) {
    return res.status(400).send('Invalid JSON input');
    }
});


/*
// Modify image endpoint
app.post('/modify', upload.none(), async (req, res) => {
    const { jsonInput } = req.body;

    try {
        // Parse JSON input
        const { title, oldTitle, description, keywords, author, creator, filename, extension } = JSON.parse(jsonInput);

        // Process modifications and handle file operations if necessary (e.g., rename file)

        // Update image details in the database
        await database.modifyImage(title, oldTitle, description, keywords, author, creator, filename);

        return res.status(200).send(); // Successful image modification
    } catch (error) {
        return res.status(500).send(error.message); // Internal server error
    }
});

app.post('/delete', (req, res)=>{ 
    res.status(200); 
    res.send("Welcome to root URL of Server"); 
}); 

app.get('/list', (req, res)=>{ 
    res.status(200); 
    res.send("Welcome to root URL of Server"); 
}); 

app.post('/search', (req, res)=>{ 
    res.status(200); 
    res.send("Welcome to root URL of Server"); 
}); 

*/



app.listen(PORT, (error) =>{ 
    if(!error) 
        console.log("Server is Successfully Running, and App is listening on port "+ PORT) 
    else 
        console.log("Error occurred, server can't start", error); 
    } 
); 
