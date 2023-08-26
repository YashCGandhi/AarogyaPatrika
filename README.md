# ASHA Health Survey Application

## Overview

In rural India, Accredited Social Health Activists (ASHAs) play a crucial role in collecting health information from families in their assigned areas. The gathered data, including family composition, disease history, and other health-related details, is essential for providing targeted healthcare support. This project aimed to create a user-friendly Survey Application to collect medical information from families in rural India, addressing the limitations of basic smartphones and low connectivity.

This project was a collaborative effort involving a team of three members.

## Features

The ASHA Health Survey Application consists of two main components:

### Android Application

Developed using Android Studio, this app offers an intuitive interface for ASHA workers. They can log in, add, edit, or delete data easily. Android was chosen due to its affordability, user-friendly programming environment, and reliable data connectivity. The app connects to the backend through a GraphQL API.

### Web Dashboard - Admin Panel

The dashboard was built using HTML, CSS, Vanilla JS, and Google Charts for data visualization. It serves as an admin panel for managing and analyzing the collected data. The dashboard interacts with the backend using Node.js middleware to fetch information from MongoDB.

## Android Application

The Android App offers the following features:

- User-friendly interface for ASHA workers
- Secure login system
- Data entry, editing, and deletion
- GraphQL API integration for backend communication
- Compatibility with basic smartphones and low connectivity

## Web Dashboard - Admin Panel

The Web Dashboard includes:

- Analytics page with various charts (e.g., pie charts) for disease distribution, family planning methods, water supply types, and more
- Data visualization using Google Charts
- Table-based display of specific data according to user-selected filters
- Server-side pagination for large datasets
- Real-time data updates from MongoDB
- Responsive design for different screen sizes

## Backend and Database

- **Backend**: The Android app communicates with the backend using a GraphQL API, providing efficient data retrieval and reducing data load times.

- **Database**: MongoDB was chosen as the database due to the unstructured nature of the data. It allows flexibility in storing diverse health-related information.

## Deployment

The ASHA Health Survey Application was deployed on Heroku, providing a robust and free hosting solution. Heroku's ease of deployment and scalability make it a suitable choice for this project.

## Challenges and Solutions

One significant challenge was optimizing data retrieval for ASHA workers with limited bandwidth. Initially, a REST API was implemented, but it led to slow performance due to multiple endpoints for data retrieval. To overcome this, a GraphQL API was adopted, allowing efficient querying of multiple data points from a single endpoint.

## Conclusion

The ASHA Health Survey Application empowers ASHA workers to collect and manage health-related data efficiently. The Android app and web dashboard provide a comprehensive solution for data collection, analysis, and visualization in rural India, addressing the unique challenges posed by basic smartphones and low connectivity.


![AarogyaPatrikappt-1](https://github.com/YashCGandhi/AarogyaPatrika/assets/35528420/94a36f61-1ebd-4283-9afe-85b39ea8cfb9)
![AarogyaPatrikappt-2](https://github.com/YashCGandhi/AarogyaPatrika/assets/35528420/d069c54c-bee9-460b-9d1f-f55f977e399d)
![AarogyaPatrikappt-3](https://github.com/YashCGandhi/AarogyaPatrika/assets/35528420/15109301-2ca3-4d6f-abf0-1e7678db4e2c)
![AarogyaPatrikappt-4](https://github.com/YashCGandhi/AarogyaPatrika/assets/35528420/494aa22d-af3f-4e28-969f-882aa20dc4b7)
![AarogyaPatrikappt-5](https://github.com/YashCGandhi/AarogyaPatrika/assets/35528420/20782dbe-3b23-405c-8bac-f583c6cb6318)
![AarogyaPatrikappt-6](https://github.com/YashCGandhi/AarogyaPatrika/assets/35528420/51e9d70c-d2df-410a-9383-b35c405b9460)
![AarogyaPatrikappt-7](https://github.com/YashCGandhi/AarogyaPatrika/assets/35528420/a0ede6f1-3aa9-4da9-8e5d-78a33b6652e5)
![AarogyaPatrikappt-8](https://github.com/YashCGandhi/AarogyaPatrika/assets/35528420/22c7f655-d154-461d-bb1c-f8a695f92ed4)


