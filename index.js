const express = require('express');

//var jwt = require('express-jwt');
//var jwks = require('jwks-rsa');
const jwt = require("jsonwebtoken");
const bcrypt = require('bcrypt');
//const { auth } = require('express-oauth2-jwt-bearer');
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

client.connect(err => {
  auth_collection = client.db("survey").collection("auth");
  examiner_collection = client.db("survey").collection("examiner");
  teams_collection = client.db("survey").collection("teams");
});


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

app.post("/auth/login", authMiddleWare, (req, res, next) => {
  if("cId" in req.body){
    res.status(200).send({status: "ok", uid: req.body["uid"], token: fetchToken(req.body["email"], req.body["uid"]), cId: req.body["cId"], email: req.body["email"]});
  }else{
    res.status(200).send({status: "ok", uid: req.body["uid"], token: fetchToken(req.body["email"], req.body["uid"]), email: req.body["email"]});
  }
});

app.post("/auth/signup", async (req, res, next) => {

  if(!("email" in req.body) || !("pass" in req.body) || !("fullname") in req.body || !("address") in req.body){
    res.status(401).send({message: "Email/Pass/Fullname/Address is required to sign up!"});
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
    await examiner_collection.insertOne({_id: sign_result.insertedId, email: req.body["email"], fullname: req.body["fullname"], address: req.body["address"]});
    res.status(200).send({status: "ok", uid: sign_result.insertedId, token: fetchToken(req.body["email"], sign_result.insertedId), email: req.body["email"]});


  });

});

app.get("/teams/getAll", jwtVerificationMiddleware, async (req, res, next) => {

  const cursor = teams_collection.find({});
  const result = await cursor.toArray();

  if(result.length < 1){
    res.status(400).send({message: "No teams found"});
    return false;
  }

  res.status(200).send(result);

});

app.get("/profile/view", jwtVerificationMiddleware, async (req, res, next) => {
  const profile = await profileCheck(res, req);
  if(profile === false) return;

  const info = profile[0];
  info["uid"] = info["_id"]

  res.status(200).send(info);

});




// // Authorization middleware. When used, the Access Token must
// // exist and be verified against the Auth0 JSON Web Key Set.
// const checkJwt = auth({
//     audience: 'https://mysterious-beach-05426.herokuapp.com/auth/login',
//     issuerBaseURL: `https://dev-9glxzlwm.us.auth0.com/`,
//   });


//   app.get('/api/public', function(req, res) {
//     console.log("hello");
//     res.json({
//       message: 'Hello from a public endpoint! You don\'t need to be authenticated to see this.'
//     });
//   });

//   app.get('/api/private', checkJwt, function(req, res) {
//     res.json({
//       message: 'Hello from a private endpoint! You need to be authenticated to see this.'
//     });
//   });

app.listen(port, () => {
    console.log(`Server running on port ${port}`);  
  });