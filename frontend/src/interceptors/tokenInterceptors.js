import axios from 'axios'

export const setupInterceptors = () => {
    axios.interceptors.request.use((config) =>{
        if(config.url !== `${process.env.REACT_APP_API_URL}/auth`){
            config.headers["X-Auth-Token"] = JSON.parse(localStorage.getItem('session')).token
        }
        return config
    })

}