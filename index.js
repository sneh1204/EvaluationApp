const express = require('express');
const jwt = require("jsonwebtoken");
const bcrypt = require('bcrypt');
const { auth } = require('express-oauth2-jwt-bearer');
const {MongoClient, ObjectId} = require('mongodb');

const app = express();
const port = process.env.PORT || 3000;
const jwtSecret = "secretsecretsecret";

app.use(express.urlencoded({ extended: true }));

const uri = "mongodb+srv://root:vT8TuRnVNN4hFCPh@auth0cluster.7a4lj.mongodb.net/survey?retryWrites=true&w=majority";
const client = new MongoClient(uri, { useNewUrlParser: true, useUnifiedTopology: true });

let auth_collection;
let examiner_collection;
let teams_collection;
let admin_collection;

client.connect(err => {
  auth_collection = client.db("survey").collection("auth");
  examiner_collection = client.db("survey").collection("examiner");
  teams_collection = client.db("survey").collection("teams");
  admin_collection = client.db("survey").collection("admin");
});

const checkJwt = auth({
  audience: 'YQIIAQIYXOJ8t7aXeR362GZdMMnIfkzI',
  issuerBaseURL: 'https://dev-9glxzlwm.us.auth0.com/',
})

const fetchToken = (email, id) => {
  return jwt.sign(
      {email: email, id: id, exp: Math.floor(Date.now() / 1000) + 60 * 60 },
      jwtSecret
  );
};

function validateCredentials(res, email, pass){
  if(!validateEmail(email)){
    res.status(401).send({message: "Invalid email provided!"});
    return false;
  }
  else if(!validatePass(pass)){
    res.status(401).send({message: "Invalid pass provided!"});
    return false;
  }
  return true;
}

function validatePass(pass){
  return pass.length >= 7;
}

function validateEmail(email) {
  const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  return re.test(String(email).toLowerCase());
}

async function profileCheck(res, req){
  let decoded = req.decodedToken;
  const id = decoded["id"]

  if(!id) {
    res.status(401).send({message: "User id is required"});
    return false;
  }

  const cursor = examiner_collection.find({_id: ObjectId(id)});
  const result = await cursor.toArray();

  if(result.length < 1){
    res.status(400).send({message: "User not found"});
    return false;
  }

  return result;
}

const authMiddleWare = async (req, res, next) => {

  if(!("email" in req.body) || !("pass" in req.body)){
    res.status(401).send({message: "Email/Pass is required to auth!"});
    return;
  }

  if(!validateCredentials(res, req.body['email'], req.body['pass'])) return;

  const cursor = auth_collection.find({email: req.body["email"]});
  const result = await cursor.toArray();

  if(result.length < 1){
    res.status(401).send({message: "Email not found in database!"});
    return;
  }

  const pass = result[0]["pass"]

  if(!bcrypt.compareSync(req.body["pass"], pass)){
    res.status(401).send({message: "Incorrect password!"});
    return;
  }

  req.body["uid"] = result[0]["_id"];

  if("customerId" in result[0]) {
    req.body["cId"] = result[0]["customerId"];
  }

  next();

};

const jwtVerificationMiddleware = async (req, res, next) => {
  let token = req.header("x-jwt-token");
  if (token) {
    try {
      req.decodedToken = jwt.verify(token, jwtSecret);
      next();
    } catch (err) {
      res.status(401).send({message: "Invalid token", fullError: err});
    }
  } else {
    res.status(400).send({message: "x-jwt-token header is required"});
  }
};


  app.post("/auth/login", (req, res, next) => {

    const postData = req.body;

    const email = postData.email;
    const pass = postData.pass;


    //Check exists email
    auth_collection.find({"email": email}).count((error, number) =>{
        if(error){
            res.status(404).json({error: error})
        }else{
            if(number == 0){
                console.log("Email not exists");
                 res.status(401).json({error: "Email not exists"});
            }else{

              auth_collection.findOne({"email": email}, (error, user) => {
      
                    if(bcrypt.compareSync(pass, user.pass)){
                        console.log("Login success");
                        
                        //create JWT token
                        let token = jwt.sign({userId:user._id, email:user.email}, jwtSecret, {expiresIn: '1d'});
                        //res.status(200).json({token: token})
                        examiner_collection.findOne({email: req.body["email"]}, (error, user) => {
                          if(error){
                              res.status(404).json({error: "Examiner not found"});
                      
                          }else{
                            console.log(user);
                              const userJson = {
                                  uid: user._id,
                                  fullname: user.fullname,
                                  address: user.address,
                                  email: user.email,
                      
                              }
                              //res.status(200).send(userJson);
                              res.status(200).send({status: "ok", uid: user._id, token: token, fullname: user.fullname, address: user.address, email: user.email});
                                    
                          }
                      })
                        //res.status(200).send({status: "ok", uid: user._id, token: token, email: req.body["email"]});
                        
                    }else{
                        console.log("Wrong Password");
                         res.status(401).json({message: "Wrong Passord"});
                    }
                })

            }
        }
        
    })

});

app.post("/auth/signup", checkJwt, async (req, res, next) => {

  if(!("email" in req.body) || !("pass" in req.body) || !("fullname") in req.body || !("address") in req.body || !("phone") in req.body ){
    res.status(401).send({message: "Email/Pass/Fullname/Address/Phone is required to sign up!"});
    return;
  }

  if(!validateCredentials(res, req.body['email'], req.body['pass'])) return;

  const salt = bcrypt.genSaltSync(10); // hashing
  const hash = bcrypt.hashSync(req.body["pass"], salt);

  await auth_collection.insertOne({email: req.body["email"], pass: hash}, async function(err, sign_result){
    if(err !== undefined && err.code === 11000){
      res.status(400).send({message: "Email already registered!"});
      return;
    }
    await examiner_collection.insertOne({_id: sign_result.insertedId, email: req.body["email"], fullname: req.body["fullname"], address: req.body["address"], phone: req.body["phone"]});
    res.status(200).send({status: "ok", uid: sign_result.insertedId, email: req.body["email"]});


  });

});

app.get("/examiners/getAll", checkJwt, async (req, res, next) => {

  const cursor = examiner_collection.find({});
  await cursor.toArray( (error, result) =>{
    //console.log(result);
    if (!error) {
      console.log("Operation completed successfully");
      if(result.length < 1){
      res.status(400).send({message: "No Examiner found"});
      return false;
    }

    console.log(result);
    res.status(200).send(result);
    } else {
      console.log(`An error occurred: ${error}`);
    }
  });

  // if(result.length < 1){
  //   res.status(400).send({message: "No Examiner found"});
  //   return false;
  // }

  // console.log(result);
  // res.status(200).send(result);

});

app.post("/register/team", checkJwt, async (req, res, next) => {

  if(!("teamname" in req.body) || !("city" in req.body) || !("participants") in req.body ){
    res.status(401).send({message: "Teamname/City/Participants is required to sign up!"});
    return;
  }
  teams_collection.find({"teamname": req.body["teamname"]}).count((error, number) =>{
    if(error){
        res.status(404).json({error: error})
    }else{
        if(number == 0){
            console.log("Teamname not exists");
            teams_collection.insertOne({teamname: req.body["teamname"], city: req.body["city"], participants: req.body["participants"]});
            res.status(200).send({status: "ok"});
        }else{
          res.status(401).send({message: "Team name already exists"});
        }
    }
    
})

});

app.get("/teams/getAll", async (req, res, next) => {

  const cursor = teams_collection.find({});
  const result = await cursor.toArray();

  if(result.length < 1){
    res.status(400).send({message: "No teams found"});
    return false;
  }

  console.log(result);
  res.status(200).send(result);

});

app.get("/profile/view", jwtVerificationMiddleware, async (req, res, next) => {
  const profile = await profileCheck(res, req);
  if(profile === false) return;

  const info = profile[0];
  info["uid"] = info["_id"]

  res.status(200).send(info);

  //get the user data 
  console.log(req.body["email"]);
  examiner_collection.findOne({email: req.body["email"]}, (error, user) => {
    if(error){
        res.status(404).json({error: "Examiner not found"});

    }else{
      console.log(user);
        const userJson = {
            uid: user._id,
            fullname: user.fullname,
            address: user.address,
            email: user.email,

        }
        res.status(200).send(userJson);
        //res.status(200).json({user: userJson});
              
    }
})
});

app.post("/update/teamscore", async(req, res, next) => {

  const fullname = req.body["fullname"];
  const score = req.body["score"];
  const team = req.body["team"];
  const avgscore = req.body["avgscore"];

  await teams_collection.updateOne({teamname: team}, {$push: { scores: {examinername: fullname , score:score} }});
  await teams_collection.updateOne({teamname: team}, {$set: { avgscore: avgscore }});

  res.status(200).send({status:"ok"});

});


app.post("/auth/adminsignup", async (req, res, next) => {

  if(!("email" in req.body) || !("pass" in req.body) || !("fullname") in req.body ){
    res.status(401).send({message: "Email/Pass/Fullname is required to sign up!"});
    return;
  }

  if(!validateCredentials(res, req.body['email'], req.body['pass'])) return;

  const salt = bcrypt.genSaltSync(10); // hashing
  const hash = bcrypt.hashSync(req.body["pass"], salt);

  await auth_collection.insertOne({email: req.body["email"], pass: hash}, async function(err, sign_result){
    if(err !== undefined && err.code === 11000){
      res.status(400).send({message: "Email already registered!"});
      return;
    }
    await admin_collection.insertOne({_id: sign_result.insertedId, email: req.body["email"], fullname: req.body["fullname"]});
    res.status(200).send({status: "ok", uid: sign_result.insertedId, token: fetchToken(req.body["email"], sign_result.insertedId), email: req.body["email"]});


  });

});


app.listen(port, () => {
    console.log(`Server running on port ${port}`);  
  });