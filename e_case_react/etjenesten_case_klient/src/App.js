import React, { useState, useEffect, useRef } from 'react';
import logo from './logo.svg';
import './App.css';
import axios from 'axios';
import firebase from 'firebase';

function App() {

  const userList = useRef();
  const [getFile, setFile] = useState(null);
  const [getContents, setContents] = useState(null)
  const [activeItems, setActiveItems] = useState(null);
  const [getResult, setResult] = useState(null);
  const [getSearchWord, setSearchWord] = useState(null);
  const [mostUsers, setMostUsers] = useState({state: null, number: null});
  const [secondMostUsers, setSecondMostUsers] = useState({state: null, number: null});
  const [fewestUsers, setFewestUsers] = useState({state: null, number: null});
  const [secondFewestUsers, setSecondFewestUsers] = useState({state: null, number: null});

  const onFileChange = async(e) => {
    const file = e.target.files[0];
    readFile(file);
    setFile(file);
    const response = await fetch('http://localhost:8080', {
      method: 'post',
      mode: 'no-cors',
      body: file
    });
    const result = await response.json();
    console.log(result);
  }

  const handleScroll = () => {
    const element = userList.current;
    if(element.scrollHeight - element.scrollTop === element.clientHeight) {
      const lastIndex = activeItems.length - 1;
      if(getResult === null) {
        setActiveItems(getContents.slice(0, lastIndex + 5));
      } else {
        setActiveItems(getResult.slice(0, lastIndex + 5));
      }
    }
  }
  
  useEffect(() => {
    if(getContents !== null) {
      statesWithMostUsers();
      statesWithFewestUsers();
    }
  }, [getContents]);

  const readFile = (file) => {
    const reader = new FileReader();
    reader.onload = (event) => {
      const contents = event.target.result;
      let usersList = [];
      let rows = contents.split("\r\n");
      rows.forEach(row => {
        const fields = row.split(",");
        const userObject = {
          seq: fields[0],
          fname: fields[1],
          lname: fields[2],
          age: fields[3],
          street: fields[4],
          city: fields[5],
          state: fields[6],
          lat: fields[7],
          lng: fields[8],
          ccnumber: fields[9]
        };
        if(userObject.fname !== "name/first") {
          usersList.push(userObject);
        }
      });
      setContents(usersList);
      setActiveItems(usersList.slice(0, 20));
    }

    reader.readAsText(file)
  }

  const getUsers = async() => {
    const response = await fetch('http://localhost:8080', {
      method: 'get',
      mode: 'cors',
      headers: {
        'Content-Type': 'x-www-form-urlencoded'
      }
    });
    const result = await response.json();
    console.log(response);
  }

  const onSearchChange = (event) => {
    setSearchWord(event.target.value);
    if(event.target.value === "") {
      setActiveItems(getContents.slice(0, 20));
    }
  }

  const onEnterPress = (e) => {
    if(e.key === "Enter") {
      onClickSearch();
    }
  }

  const onClickSearch = () => {
      setActiveItems(getContents.map((user) => {
        if(user.fname !== undefined) {
          if(user.fname.includes(getSearchWord) ||
          user.lname.includes(getSearchWord) ||
          user.city.includes(getSearchWord) ||
          user.street.includes(getSearchWord) ||
          user.state.includes(getSearchWord)) {
            return user;
          }
        }
      }));
  }

  const ageStats = () => {
    let largest = 0;
    let smallest = getContents[0].age;
    for(let i = 0; i <= largest; i++) {
      if(getContents[i].age > largest) {
        largest = getContents[i].age;
      }
      if(getContents[i].age < smallest) {
        smallest = getContents[i].age;
      }
    }
    let rangeYoungest = [];
    let rangeMid = [];
    let rangeOlder = [];
    for(let user of getContents) {
      if(user.age >= smallest && user.age < 30) {
        rangeYoungest.push(user);
      } else if(user.age >= 30 && user.age < 45) {
        rangeMid.push(user);
      } else if(user.age >= 45) {
        rangeOlder.push(user)
      }
    }
    const percentageYoungest = Math.round((rangeYoungest.length / getContents.length) * 100);
    const percentageMid = Math.round((rangeMid.length / getContents.length) * 100);
    const percentageOlder = Math.round((rangeOlder.length / getContents.length) * 100);
    return [percentageYoungest, percentageMid, percentageOlder];
  }

  const duplicates = () => {
    let counts = getContents.reduce((map, val) => {map[val.state] = (map[val.state] || 0)+1; return map}, {} );
    let numOfNonDuplicates = [];
    getContents.forEach((user) => {
      if(!Object.keys(counts).includes(user.state)) {
        Object.keys(counts);
        numOfNonDuplicates.push(user);
      }
    });
    return counts;
  }

  const statesWithMostUsers = () => {
    let duplicatesObj = duplicates();
    let mostUsers = Object.keys(duplicatesObj).reduce((a, b) => duplicatesObj[a] > duplicatesObj[b] ? a : b);
    setMostUsers({state: mostUsers, number: duplicatesObj[mostUsers]});
    delete duplicatesObj[mostUsers];
    let secondMostUsersNum;
    let secondMostUsers = Object.keys(duplicatesObj).reduce((a, b) => duplicatesObj[a] > duplicatesObj[b] ? a : b);
    setSecondMostUsers({state: secondMostUsers, number: duplicatesObj[secondMostUsers]});
  }

  const statesWithFewestUsers = () => {
    let duplicatesObj = duplicates();
    delete duplicatesObj[undefined];
    let fewestUsers = Object.keys(duplicatesObj).reduce((a, b) => duplicatesObj[a] < duplicatesObj[b] ? a : b);
    setFewestUsers({state: fewestUsers, number: duplicatesObj[fewestUsers]});
    delete duplicatesObj[fewestUsers];
    let secondFewest = Object.keys(duplicatesObj).reduce((a, b) => duplicatesObj[a] < duplicatesObj[b] ? a : b);
    setSecondFewestUsers({state: secondFewest, number: duplicatesObj[secondFewest]});
  }

  return (
    <div className="App">
      <h1>Brutus</h1>
      <input type="file" onChange={onFileChange} />
      <div className="parentStatDiv">
        <p>Alderstatistikk :</p>
        <div className="statDiv">
          <p className="ageStat">18 - 30 år: {getContents !== null ? ageStats()[0] : null}%</p>
          <p className="ageStat">30 - 45 år: {getContents !== null ? ageStats()[1] : null}%</p>
          <p className="ageStat">45 år +: {getContents !== null ? ageStats()[2] : null}%</p>
        </div>
      </div>
      <div className="parentStatDiv">
        <p>Statene med flest brukere :</p>
        <div className="statDiv">
          <p className="ageStat">{getContents !== null ? `${mostUsers.state}, brukere: ${mostUsers.number}` : null}</p>
          <p className="ageStat">{getContents !== null ? `${secondMostUsers.state}, brukere: ${secondMostUsers.number}` : null}</p>
        </div>
      </div>
      <div className="parentStatDiv">
        <p>Statene med færrest brukere :</p>
        <div className="statDiv">
          <p className="ageStat">{getContents !== null ? `${fewestUsers.state}, brukere: ${fewestUsers.number}` : null}</p>
          <p className="ageStat">{getContents !== null ? `${secondFewestUsers.state}, brukere: ${secondFewestUsers.number}` : null}</p>
        </div>
      </div>
      <div className="parentStatDiv">
        <p>Totalt antall brukere: {getContents !== null ? getContents.length - 1 : null}</p>
      </div>
      <div>
        <input onKeyDown={onEnterPress} onChange={onSearchChange} />
        <button onClick={() => onClickSearch()}>Søk</button>
      </div>
      <div className="userList" ref={userList} onScroll={getSearchWord === null || getSearchWord === "" ? handleScroll : null}>
        {
          activeItems !== null ?
          activeItems.map((user) => (
            user !== undefined ?
            <div className="userItem">{`
              ${user.fname !== undefined ? user.fname : null} 
              ${user.lname !== undefined ? user.lname : null}, 
              alder: ${user.age !== undefined ? user.age : null}, 
              gate: ${user.street !== undefined ? user.street : null}, 
              by: ${user.city !== undefined ? user.city : null}, 
              stat: ${user.state !== undefined ? user.state : null}
            `}
            </div> : null
          )) : null
        }
      </div>
    </div>
  );
}

export default App;
