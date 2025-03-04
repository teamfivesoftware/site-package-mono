import logo from './logo.svg';
import './App.css';

function App() {
  return (
    <div className="App">
      <p> Version {process.env.REACT_APP_VERSION} </p> 
    </div>
  );
}

export default App;
