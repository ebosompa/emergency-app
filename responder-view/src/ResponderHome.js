import React from 'react';
import ReactDOM from 'react-dom';
import './ResponderHome.css';
import { Button, Card, Form} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

function Request({ request, index, markAsComplete, deleteRequest }) {
    return (
        <div className="request">
            <span style={{ textDecoration: request.isComplete ? "line-through" : ""}}>
                {request.longitude}
            </span>
            <span style={{ textDecoration: request.isComplete ? "line-through" : ""}}>
                {request.latitude}
            </span>
            <span style={{ textDecoration: request.isComplete ? "line-through" : ""}}>
                <a href={
                    "https://www.google.com/maps/search/?api=1&query=" +
                    request.latitude + "%2C" + request.longitude
                    } target="_blank">Get Location Through Google Maps</a> 
            </span>
            <div>
                <Button variant="outline-success" onClick={() =>
                markAsComplete(index)}>✓</Button>{' '}
                <Button variant="outline-danger" onClick={() =>
                deleteRequest(index)}>✕</Button>{' '}
            </div>
        </div>
    );
}

function ResponderHome(){
    const [requests, setRequests] = React.useState([{"":"", "":""}]);

    React.useEffect(() => {
        fetch("http://localhost:5000/emergencyLoc",
        {
            method: "GET",
            headers: {
                "Content-Type": "application/json", 
                "Access-Control-Allow-Origin":"*"
            },
        })
          .then(results => results.json())
          .then(data => setRequests(Object.values(data)));
      }, []);

    const markAsComplete = index => {
        const newRequests = [...requests];
        newRequests[index].isComplete = true;
        setRequests(newRequests);
    };

    const deleteRequest = index => {
        const newRequests = [...requests];
        newRequests.splice(index, 1);
        setRequests(newRequests);
    };
    return (
        <div className="home">
            <div className="container">
                <h1 className="text-center mb-4">See All Emergencies</h1>
                <div>
                    {requests.map((request,index) => (
                        <Card>
                            <Card.Body>
                                <Request
                                key={index}
                                index={index}
                                request={request}
                                markAsComplete={markAsComplete}
                                deleteRequest={deleteRequest}
                                />
                            </Card.Body>
                        </Card>
                    ))}
                </div>
            </div>
        </div>
    );
}

export default ResponderHome;