# Assignment – Software Engineer
### Application
Create a JVM based backend application using REST. That contains the following endpoints:
The initial list of stocks should be created in-memory on application start-up. The stock object contains at least the following fields:
- id (Number);
- name (String);
- currentPrice (Amount);
- lastUpdate (Timestamp).

### Features
- Each stock must be protected from abusive price updates, thus it must be locked for a minimum of 5 minutes before it can be updated or deleted. Each stock must be automatically unlocked at the time when its lock expires (a state desynchronization of maximum of 5 seconds after expiration is allowed).
- Each endpoint must be compliant with the HTTP/1.1 and REST standards. Use any frameworks that you see fit to build and test this application.

### Nice to have (Optional)
We would also like you to create a front-end which shows the stock list.

*NOTE:* Please do not use a generator (like yeoman), because then it is very difficult for us to determine what you have written yourself and what parts are generated.
### Implementation
Treat this application as a real MVP that should go to production.
All main use cases must be covered by at least one unit or(and) integration test.
- `GET /api/stocks (get a list of stocks)`
- `POST /api/stocks (create a stock)`
- `GET /api/stocks/1 (get one stock from the list)`
- `PUT /api/stocks/1 (update the price of a single stock)`
- `DELETE/api/stocks/1 (delete a single stock)`

### Submission
- Create a private git repo (the repository can be hosted on Github or on any similar platform)
that contains the project codebase. When done, contact your Payconiq recruiter, who will send you the email of your reviewer (you will have to grant them access).
- Important! Please remove the project once it’s been reviewed. Good luck!