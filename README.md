
# Audio to Text Transcriber

A full-stack application using **React**, **Spring Boot**, and **Spring AI** to transcribe uploaded audio files into text using OpenAI's Whisper model.

---
# Demo
![Demo](src/main/resources/static/demo.gif)
___

## Project Setup

### 1. Clone & Initialize

1. Download or clone the project.
2. Unzip and open the backend folder in **IntelliJ IDEA** or any Java IDE of your choice.

---

## Backend (Spring Boot + Spring AI)

### 2. Add OpenAI Dependency

> IMPORTANT: Adding the OpenAI dependency automatically creates a bean (`openAiChatModel`) that **requires** an API key. Without this, the application will crash at startup with:

```

Error creating bean with name ‘openAiChatModel’

````

### 3. Fix Bean Initialization Error

Set your OpenAI API key in `application.properties`:

```properties
spring.ai.openai.api-key=your_openai_api_key_here
````

---

## Get OpenAI API Key

1. Visit: [https://platform.openai.com](https://platform.openai.com)
2. Check pricing (optional).
3. Click **"Start Building"** to sign up or log in.
4. Go to your **Dashboard > API Keys** to create a new key.

---

## OpenAI Manual Transcription (Spring AI)

### Sample Setup:

1. Add the required Spring AI dependency.
2. Use `OpenAiAudioApi` to initialize `OpenAiAudioTranscriptionModel`.
3. Create a REST controller with an endpoint for audio file upload.

### Core Steps in Controller:

* Accept `MultipartFile` upload from user.
* Transfer content to a temporary `.wav` file.
* Create `OpenAiAudioTranscriptionOptions` to define:

    * Response format
    * Language
    * Temperature
* Wrap temp file using `FileSystemResource`.
* Create a `TranscriptionPrompt` with the resource and options.
* Call `transcriptionModel.call(prompt)`.
* Delete the temp file.
* Return the transcription result as JSON.

---

## Backend CORS Configuration

To allow the React frontend to access the Spring Boot backend, configure CORS.

### Create `WebConfig.java`:

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:5173")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}
```

This enables full CORS access from your frontend running on port `5173`.

---

## Frontend (React)

### Steps:

1. Create a `AudioUploader.jsx` component.
2. Use Tailwind CSS for styling.
3. Include:

    * An `<input type="file">` element for audio upload
    * A `<button>` to trigger transcription
    * Handlers for:

        * File selection
        * Submitting file via HTTP POST
        * Displaying the transcribed text

### Sample Error Fix

**CORS Error:**

```
Access to XMLHttpRequest at 'http://localhost:8080/api/transcribe' from origin 'http://localhost:5173' has been blocked by CORS policy
```

Fix this using the `WebConfig` class above to allow cross-origin requests.

---

## Test the API

* Use **Postman** to test `POST /api/transcribe`
* Send an audio file as `multipart/form-data`
* Confirm response contains the transcribed text

---

## Folder Structure

```
project-root/
├── backend/            # Spring Boot app
│   ├── controller/
│   ├── config/
│   ├── service/
│   └── application.properties
└── frontend/           # React app
    └── src/
        └── AudioUploader.jsx
```

---

## TODO / Improvements

* [ ] Add language selection support
* [ ] Show transcription confidence or timestamp
* [ ] Support for large audio files and progress bar
* [ ] Add user authentication

---

## Powered By

* [OpenAI Whisper Model](https://platform.openai.com/docs/guides/speech-to-text)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Spring AI](https://docs.spring.io/spring-ai/)
* [React.js](https://react.dev/)
* [Tailwind CSS](https://tailwindcss.com/)

---

## License

MIT – feel free to use and adapt this project.
