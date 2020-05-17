import React, { useState, useEffect} from 'react';
import {MatchService} from '../services/MatchService'
import moment from 'moment';
import Stomp from 'stompjs';
import Navbar from '../shared/Navbar';

var stompClient = null;

const Dashboard = () => {
    const[latestMatches, setLatestMatches] = useState([]);
    const[started, setStarted] = useState(false);
    useEffect(() => {
        window.addEventListener("unload", () => handleStop());
        MatchService.getLatest()
            .then((response) => setLatestMatches(response.data));
        return () => handleStop();
    }, [])

    const handleStart = () =>{
        var socket = new WebSocket('ws://localhost:8080/connect');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function(frame) {
            stompClient.subscribe('/topic/messages', function(messageOutput){
                updateMatches(messageOutput);
            });
            stompClient.send("/ws/start");
            setStarted(true);
        });
    }
    const handleStop = () =>{
        if(stompClient === null){
            return;
        }
        stompClient.send("/ws/stop")
        setStarted(false);
    }

    const updateMatches = (message) =>{
        console.log(message);
        var latestMatch = JSON.parse(message.body);
        if(latestMatch.body === null){
            return;
        }
        setLatestMatches(prevState => {
            var newArray = [];
            newArray = Array.from(prevState);
                if(prevState.length === 10){
                    newArray.pop();
                    newArray.unshift(latestMatch);
                }else{
                    newArray.unshift(latestMatch);
                }
            return [...newArray]});
        }

 
    
    return (
        <div>
        <Navbar/>
        <div className="hero is-info is-bold is-fullheight">
            <div className="hero-head">
            </div>
            <div className="hero-body">
                <div className="container ">
                    <div className="box" style={{minHeight: "800px"}}>
                        <div className="columns">
                            <h3 className="subtitle is-3" style={{color: "black"}}>Latest matches</h3>
                        </div>
                        <div className="columns is-vcentered">
                            <div className="column">
                                <table className="table is-striped">
                                    <thead>
                                        <tr>
                                            <th>Id</th>
                                            <th>Username</th>
                                            <th>Date</th>
                                            <th>Finished</th>
                                            <th>Report</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {latestMatches.map((match) => 
                                            <tr key={match.id}>
                                                <td>{match.id}</td>
                                                <td>{match.userId}</td>
                                                <td>{moment(match.timestamp).format('DD.MM.YYYY HH:mm:ss')}</td>
                                                <td>{String(match.finished)}</td>
                                                <td>{match.report}</td>
                                            </tr>
                                            )}
                                    </tbody>
                                </table>
                            </div>
                            <div style={{textAlign:"center"}}className="column is-centered">
                                {
                                    started ? <button className="button is-info is-medium " onClick={handleStop}>Stop</button> : <button className="button is-info is-medium" onClick={handleStart}>Start simulation</button>
                                }
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        </div>
    )
}

export default Dashboard;