import axios from "axios"

export const MatchService ={
    getLatest,
    reset,
}

function getLatest(){
    return axios.get(`${process.env.REACT_APP_API_URL}/matches`)
}

function reset(){
    return axios.get(`${process.env.REACT_APP_API_URL}/matches/reset`)
}