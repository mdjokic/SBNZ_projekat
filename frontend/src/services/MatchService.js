import axios from "axios"

export const MatchService ={
    getLatest
}

function getLatest(){
    return axios.get(`${process.env.REACT_APP_API_URL}/matches`)
}