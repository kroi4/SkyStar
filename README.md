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
    <td><img src="https://github.com/user-attachments/assets/a998eabe-b646-4ad1-8adf-325c1d3f710b" alt="Screenshot 1" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/f490ae3e-9424-4960-b653-f18dfd19b92e" alt="Screenshot 2" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/ede2d31a-6686-422a-924a-7abb4f2ada11" alt="Screenshot 3" width="200"></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/d93aee1a-d1cf-4bf1-a397-f93c2979029d" alt="Screenshot 4" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/c1218054-6399-4f5a-a076-9acac5aa586f" alt="Screenshot 5" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/ea3b7a30-5978-40e8-9f37-2f74b9e9ebcb" alt="Screenshot 6" width="200"></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/d47ee8be-e18c-4dce-be12-1eccbdc27847" alt="Screenshot 7" width="200"></td>
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
