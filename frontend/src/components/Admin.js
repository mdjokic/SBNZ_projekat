import React, {useEffect} from 'react';
import axios from 'axios';

const Admin = () => {
    useEffect(() => {
        axios.get(`${process.env.REACT_APP_API_URL}/test`)
            .then(response => console.log("jee"))
    }, [])
    return (
        <div>
            Admin
        </div>
    )
}

export default Admin;