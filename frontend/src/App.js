import './App.css';
import axios from 'axios';
import React from "react";

class App extends React.Component {
    state = {
        text: ""
    }

    constructor(props) {
        super(props);
        axios.get(`http://localhost:8080/test`)
            .then(response => {
                this.setState({ text: response.data });
            })
    }

  render() {
      return (
          <div className="App">
              <h1>Social network</h1>
              <p>{ this.state.text }</p>
          </div>
      );
  }
}

export default App;
