//import { useEffect, useState } from "react";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import React, {useEffect, useState} from 'react';
//import axios from 'axios';
import ReactDOM from 'react-dom/client';
import Cart from "./routes/Cart";
import Order from "./routes/Order";
import Test from "./routes/Test";


function App() {
    return (
          <Router>
            <Switch>
              <Route path="/cart">
                <Cart />
              </Route>
              <Route path="/Order">
                <Order />
              </Route>
              <Route path="/">
                <Test />
              </Route>

            </Switch>
          </Router>
        );
}


//function App() {
//   const [hello, setHello] = useState('')
//
//    useEffect(() => {
//        axios.get('/api/hello')
//        .then(response => setHello(response.data))
//        .catch(error => console.log(error))
//    }, []);
//
//    return (
//        <div>
//            백엔드에서 가져온 데이터입니다 : {hello}
//        </div>
//    );
//}

export default App;
