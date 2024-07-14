# SkyStar

SkyStar is an Android application developed to provide users with real-time updates about flights at Ben Gurion Airport. The application allows users to track specific flights, perform advanced searches, and filter flights by various criteria such as airline, flight number, origin/destination, and date/time range.

## Features

- Real-time updates about inbound and outbound flights at Ben Gurion Airport.
- Track specific flights of interest.
- Advanced search and filtering options by airline, flight number, origin/destination, and date/time range.
- Regularly updated flight information including current status and actual departure/arrival times.

## Installation

To install the SkyStar application, download the APK file from the link below and install it on your Android device:

[Download SkyStar APK](https://drive.google.com/file/d/1ISr7tbnN1mf0IYIGNI_cyOTVngCUXiBG/view?usp=drive_link)

## Screenshots

<table>
  <tr>
    <td><img src="https://drive.google.com/uc?export=view&id=1qfIctEL-lpK-TJDm3ssRNc8WHUTTS6Qh" alt="Home Screen" width="200"></td>
    <td><img src="https://drive.google.com/uc?export=view&id=1qmfn6wTEMpTLDhNnH38mWJ3kWJ340Uhc" alt="Departures" width="200"></td>
    <td><img src="https://drive.google.com/uc?export=view&id=1qwCmz-JGi8_oIefysTE9zlmGlHZApYe5" alt="Arrivals" width="200"></td>
  </tr>
  <tr>
    <td><img src="https://drive.google.com/uc?export=view&id=1rCtVRT-dETdecdiJ0A1A_7JDBcU7WtN7" alt="Favorites" width="200"></td>
    <td><img src="https://drive.google.com/uc?export=view&id=1qy-kWhg53M7jixulMOpB4a4-opALLXK7" alt="Search" width="200"></td>
    <td><img src="https://drive.google.com/uc?export=view&id=1rEITTNwwLcNxjfeS4U3xBlCfb6_AGiAs" alt="Search Results" width="200"></td>
  </tr>
  <tr>
    <td><img src="https://drive.google.com/uc?export=view&id=1r-UjsfYezA256ZvuO8SY1XCaESZxweCN" alt="Flight Details" width="200"></td>
  </tr>
</table>

## Video Demonstration

Check out the video demonstration of our application:

[![SkyStar Video](https://img.youtube.com/vi/yENMu5VoE-Q/0.jpg)](https://youtu.be/yENMu5VoE-Q)

## Documentation

For detailed information about the project, refer to the following documents:

### Project Presentation

Click to view the full project presentation:
[![View Full Project Presentation](https://drive.google.com/uc?export=view&id=1J4TLQQ_hJB9y9c1Q7wNZqNaUgU_r9wtq)](https://drive.google.com/file/d/142MpyxJF3-iEkyB_lc-gU7wR06LAviVb/view?usp=drive_link)

## Application Architecture

The SkyStar application uses various libraries and technologies to fetch, arrange, and display flight information:

- **Retrofit2**: For sending `GET` requests to the `gov.il` server.
- **Room**: For creating and managing local databases on the device.
- **Dagger-Hilt**: For dependency injection.
- **Glide**: For loading images from the internet.
- **MVVM Architecture**: For organizing the application’s code.

### Schema

The application’s schema includes the following screens and functionalities:

- **Home Screen**
- **Departures**
- **Arrivals**
- **Favorites**
- **Search**
- **Search Results**
