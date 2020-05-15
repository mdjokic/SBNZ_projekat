import axios from "axios"
import jwt_decode from 'jwt-decode'

export const AuthenticationService = {
    authenticate
}

function authenticate(userCredentials){
    return axios.post(`${process.env.REACT_APP_API_URL}/auth`, userCredentials)
        .then(response => {
            updateStorageToken(response)
        })
}

function updateStorageToken(response){
    let decodedToken = jwt_decode(response.data);
    let sessionInfo = {
        token: response.data,
        role: decodedToken.role
    }
    localStorage.setItem("session", JSON.stringify(sessionInfo))
}