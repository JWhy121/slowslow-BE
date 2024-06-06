import { useEffect, useState } from "react";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import Cart from "./routes/Cart";


function App() {
  return (
    <Router>
          <Switch>
            <Route path="/cart">
              <Cart />
            </Route>
            <Route path="/">

            </Route>
          </Switch>
    </Router>
  );
}

export default App;
