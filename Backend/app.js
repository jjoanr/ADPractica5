const express = require('express'); 
const multer = require('multer'); // Handle file uploads
const database = require('./database'); // Database functions file
const path = require('path');
const fs = require('fs');

  
const app = express(); 
const PORT = 3000;

app.use(express.urlencoded({ extended: true }));
app.use(express.json());

const uploadDir = '/home/joanr/project/ADPractica5/images/';

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
    console.log(username, password)
    try {
        // Check login credentials against the database
        database.connectToDatabase();
        const isLoggedIn = await database.login(username, password);
        console.log(isLoggedIn)
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

//Modify existing image
app.post('/modify', async (req, res) => {
    try {
        const { title, oldtitle, description, keywords, author, creator, filename, extension } = req.body;

        // Check if the title is already in use
        database.connectToDatabase();
        const titleUsed = await database.titleIsUsed(title, creator);
        database.disconnectFromDatabase();

        if (title !== oldtitle && titleUsed) {
            return res.status(409);
        }

        // Rename the file
        const oldFilePath = `/home/joanr/project/ADPractica5/images/` + filename;
        const newFilePath = `/home/joanr/project/ADPractica5/images/${title}-${creator}${extension}`;
        fs.renameSync(oldFilePath, newFilePath);

        newfilename = title + '-' + creator + extension;

        // Update the image in the database
        database.connectToDatabase();
        await database.modifyImage(title, oldtitle, description, keywords, author, creator, newfilename);
        database.disconnectFromDatabase();

        return res.status(200).json({ message: 'Image modified successfully' });
    } catch (error) {
        return res.status(500).json({ error: 'An error occurred while modifying the image' });
    }
});

// Delete image
app.post('/delete', async (req, res) => {
    try {
        const { id } = req.body;

        //Delete from local storage
        const filePath = `/home/joanr/project/ADPractica5/images/${id}`;
        fs.unlinkSync(filePath);

        //Delete information from database
        database.connectToDatabase();
        await database.deleteImage(id);
        database.disconnectFromDatabase();

        res.status(200).json({ message: 'Image deleted successfully' });
    } catch (err) {
        res.status(500).json({ error: 'An error occurred while deleting the image' });
    }
});

//Get all the images
app.get('/list', async (req, res) => {
    try {
        // Retrieve images from the database
        database.connectToDatabase();
        const images = await database.getImages();
        database.disconnectFromDatabase();

        // Process the images
        let imagesArray = [];
        for (let image of images) {
            let imageBase64 = await encodeImageToBase64('/home/joanr/project/ADPractica5/images/' + image.filename);

            imagesArray.push({
                title: image.title,
                description: image.description,
                keywords: image.keywords,
                author: image.author,
                creator: image.creator,
                creationDate: image.capture_date,
                introductionDate: image.storage_date,
                filename: image.filename,
                imageData: imageBase64
            });
        }

        // Send the formatted JSON response
        return res.status(200).json(imagesArray);
    } catch (error) {
        return res.status(500).send('Error retrieving images: ' + error.message);
    }
});

// Search image endpoint
app.post('/search', async (req, res) => {
    try {
        const { searchWord } = req.body;
        
        // Search image
        database.connectToDatabase();
        const images = await database.searchImages(searchWord);
        database.disconnectFromDatabase();

        // Check if there are no images
        if (images.length === 0) {
            return res.status(404).send('No images found');
        }

        // Process the images
        let imagesArray = [];
        for (let image of images) {
            let imageBase64 = await encodeImageToBase64('/home/joanr/project/ADPractica5/images/' + image.filename);

            imagesArray.push({
                title: image.title,
                description: image.description,
                keywords: image.keywords,
                author: image.author,
                creator: image.creator,
                creationDate: image.capture_date,
                introductionDate: image.storage_date,
                filename: image.filename,
                imageData: imageBase64
            });
        }

        // Send the formatted JSON response
        return res.status(200).json(imagesArray);

    } catch (err) {
        return res.status(400);
    }
});

//Encode in base 64
function encodeImageToBase64(filename) {
    return new Promise((resolve, reject) => {
        fs.readFile(filename, (err, data) => {
            if (err) {
                reject(err);
            } else {
                resolve(data.toString('base64'));
            }
        });
    });
}

app.listen(PORT, (error) =>{ 
    if(!error) 
        console.log("Server is Successfully Running, and App is listening on port "+ PORT) 
    else 
        console.log("Error occurred, server can't start", error); 
    } 
); 
