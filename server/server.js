const express = require("express");


const app = express();


app.use(express.urlencoded({
    extended: true
  }))

  
app.get('/', (req, res) => {
    console.log("In hereeee");
    res.sendFile("/Users/mike/emergency-app/server/form.html");
})

app.post('/mike', (req,res) => {
   
    console.log("Body is");

    console.table(req.body);
    res.send(req.body);
})



app.listen(3000, ()=> {
    console.log("Listening on port 3000");
})