# SkyStar

SkyStar is an Android application developed to provide users with real-time updates about flights at Ben Gurion Airport. The application allows users to track specific flights, perform advanced searches, and filter flights by various criteria such as airline, flight number, origin/destination, and date/time range.

## Features

- Real-time updates about inbound and outbound flights at Ben Gurion Airport.
- Track specific flights of interest.
- Advanced search and filtering options by airline, flight number, origin/destination, and date/time range.
- Regularly updated flight information including current status and actual departure/arrival times.

## Installation

To install the SkyStar application, download the APK file from the link below and install it on your Android device:

[Download SkyStar APK](https://www.dropbox.com/scl/fi/s1bmmvhcirpnj1bf71nip/SkyStar.apk?rlkey=jlodt3yn7z9e86a61p2a8ln7d&st=jx82ht2c&dl=0)

## Screenshots

![Home Screen](https://drive.google.com/uc?export=view&id=1qfIctEL-lpK-TJDm3ssRNc8WHUTTS6Qh)
![Departures]()
![Arrivals]()
![Favorites]()
![Search]()
![Search Results]()
![Flight Details]()

## Video Demonstration

Check out the video demonstration of our application:

[![SkyStar Video](https://img.youtube.com/vi/yENMu5VoE-Q/0.jpg)](https://youtu.be/yENMu5VoE-Q)

## Documentation

For detailed information about the project, refer to the following documents:

### Project Presentation

Click to view the full project presentation:
[![View Full Project Presentation](https://previews.dropbox.com/p/thumb/ACXyvlh-UoDkZVVatmdCLlazbLD3BENQ_RmtNmSgGuNwENHZ2ON5OFw2K62U1ndMsky7u0Qg8MBUm-Ru1x8cY6QeuBgWD0N-M-hf17M9NtN6OzidCRA6tEPqbTcYOdheqbYG98s5QDT7UXl9fcqD3nk7um-qVNiTw7jc6IfaINFOd9NP0_epH0-uy_e_esJLgAEgu0EHh9dRvUqf5gfKYLmlUoZQzyguRj-VIy4G1li2728kknDNQXRc-LSdEpTx8AnpfTf2hRijwaKW0YokNpQKhjvMB5V1VYkMChmHUo_Abl2ouHI3GUjnL-DsEtp5h-TuqSuzfgl-kZKzJ5ZLYAJE/p.jpeg)](https://www.dropbox.com/scl/fi/uvif1fuiq4udujfensxng/Project-Presentation.pdf?rlkey=a2y74myrhn7m1vz11ayu249n7&st=y4r6i4g9&dl=0)

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
