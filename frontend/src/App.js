import React from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import Login from './components/Login';
import Admin from './components/Admin';

function App() {
  return (
    <Router>
      <Switch>
        <Route path={'(|/login)'} exact component={Login}/>
        <Route path={'/admin'} exact component={Admin}/>
      </Switch>
    </Router>
  );
}

export default App;
