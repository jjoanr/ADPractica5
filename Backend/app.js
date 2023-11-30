const express = require('express'); 
//const multer = require('multer'); // Handle file uploads
const database = require('./database'); // Database functions file
  
const app = express(); 
const PORT = 3000; 

app.use(express.urlencoded({ extended: true }));

const uploadDir = '/home/joanr/Practica5-AD/images/';

//const upload = multer({ dest: uploadDir });

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

/*
// Register image endpoint
app.post('/register', upload.single('file'), async (req, res) => {
    const { jsonInput } = req.body;
    const file = req.file;

    try {
        // Parse JSON input
        const { title, description, keywords, author, creator, creationDate, introductionDate, filename } = JSON.parse(jsonInput);

        // Process the file and save it to the specified directory
        // Handle file operations here (saving the uploaded file to a directory)

        // Save image details to the database
        await database.registerImage(title, description, keywords, author, creator, creationDate, introductionDate, filename);

        return res.status(200).send(); // Successful image registration
    } catch (error) {
        return res.status(500).send(error.message); // Internal server error
    }
});

function writeImage(file_name, fileinputStream) {
    
}

function makeDirIfNotExists() {

}

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
