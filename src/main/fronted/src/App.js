import { useEffect, useState } from "react";
//import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import Cart from "./routes/Cart";
import axios from "axios";


function App() {
    const [hello, setHello] = useState('');

    useEffect(() => {
        axios.get('/api/test')
            .then((res) => {
                setHello(res.data);
            })
    }, []);
    return (
        <div className="App">
            백엔드 데이터 : {hello}
        </div>
    );
}

export default App;
