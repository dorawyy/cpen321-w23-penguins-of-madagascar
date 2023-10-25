const express          = require("express");
const { OAuth2Client } = require('google-auth-library');

const oauthClient = new OAuth2Client();
export const user = {
    get: getUser,
    // post: makeUser,
    // put: updateUser,
    // delete: removeUser
}

const getUser = async (req, res) => {
    try{
        console.log(req.body);
        // const token = req.body.userToken;
        // const tokenResp = await oauthClient.verifyIdToken({
        //     idToken: token,
        //     audience: process.env.CLIENT_ID
        // });
        
        // check database for user
        // return to client userinfo for user page
        res.status(200).send({
            body: req.body
        })

    }catch(err){
        console.log(err);
        
        res.status(400).send(err);
    }
}

// const makeUser = async (req, res) => {

// }

// const updateUser = async (req, res) => {

// }

// const removeUser = async (req, res) => {

// }
