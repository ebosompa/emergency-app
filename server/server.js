const express = require("express");


const app = express();


class Location {
    constructor(id, longitude, latitude, timestamp, sound) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.timestamp = timestamp;
        this.userAudios = new Sounds(id, sound);
    }

    
}

class Sounds {
    constructor(id,audio) {
        this.id = id;
        this.audio;
        this.addAudio(audio);
    }

    addAudio(sound) {
        if(this.audio == null) {
            this.audio = [sound];
        }
        else {
            this.audio.push(sound);
        }
    }
}

const addLocation =  (locationItems)=> {
   
    if(AllLocations[locationItems.identifier] == null) {
        tempLocation = new Location(locationItems.identifier, locationItems.longitude, locationItems.latitude, locationItems.timestamp, locationItems.sound);
        AllLocations[locationItems.identifier] = tempLocation;
    }
    else {
        AllLocations[locationItems.identifier].longitude = locationItems.longitude;
        AllLocations[locationItems.identifier].latitude = locationItems.latitude;
        AllLocations[locationItems.identifier].timestamp = locationItems.timestamp;
        AllLocations[locationItems.identifier].userAudios.addAudio(locationItems.audio);
    }

    console.log("gotten location:")
    console.table(AllLocations[locationItems.identifier]);
}

const AllLocations = {};


app.use(express.urlencoded({
    extended: true
  }))



app.get('/', (req, res) => {
    console.log("In hereeee");
    res.sendFile("/Users/mike/emergency-app/server/form.html");
})


app.get('/emergencyLoc', (req, res) =>{
    //grab the list of all Location class and send in res
    console.log("Sending emergency locations");
    res.send(JSON.stringify(AllLocations));
})

app.post('/sendLocation', (req,res) => {
   
    console.log("Body is");

    console.table(req.body);
    addLocation(req.body);
    console.log(AllLocations);

    res.send("Gotten data tenk yew!!!")
})

app.listen(3000, ()=> {
    console.log("Listening on port 3000");
})