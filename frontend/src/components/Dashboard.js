import React, {useEffect} from 'react';
import Stomp from 'stompjs';




const Dashboard = () => {

    useEffect(() => {
        var socket = new WebSocket('ws://localhost:8080/connect');
        var stompClient = Stomp.over(socket);
        stompClient.connect({}, function(frame){
            console.log("Connected");
            stompClient.subscribe('/games', function(payload){
                console.log(payload);
            });
            stompClient.send('/ws/start', {})

        })
    }, [])
    return (
        <div className="container">
        </div>
    )
}

export default Dashboard;