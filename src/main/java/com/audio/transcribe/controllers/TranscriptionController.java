/*

Create Object of OpenAiAudioTranscriptionModel
Initialize OpenAiAudioTranscriptionModel Object in the constructor using an object of openAiAudioApi, which has been initialized with the api key as it’s argument.
Create endpoint for users to Upload a file, get the file and use it to create a temporary file object of an audio with .wav extension.
Transfer content uploaded by user to the temp file.
Create a OpenAiAudioTranscriptionOptions object ​​cause that what OpenAI understands. Use this object to tell OpenAI what you want. Specify the response format, language, temperature, and build.
Create an object of FileSystemResource and wrap the temp file in it.
Create an audio transcription prompt (which combines the audio file and transcription options specified earlier) to send over.
Get response by doing a call to the model using the prompt.
Delete file and return response to user.

 */

package com.audio.transcribe.controllers;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/transcribe")
public class TranscriptionController {

    private final OpenAiAudioTranscriptionModel transcriptionModel;

    public TranscriptionController(@Value("${spring.ai.openai.api-key}") String apiKey) {
        //Create Object and initialize with API Key
        OpenAiAudioApi openAiAudioApi = OpenAiAudioApi.builder().apiKey(apiKey).build();
        this.transcriptionModel = new OpenAiAudioTranscriptionModel(openAiAudioApi);
    }

    @PostMapping
    public ResponseEntity<String> transcribeAudio (@RequestParam("file")MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("audio", ".wav");
        file.transferTo(tempFile);
        OpenAiAudioTranscriptionOptions transcriptionOptions = OpenAiAudioTranscriptionOptions.builder()
                .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
                .language("en")
                .temperature(0f)
                .build();

        FileSystemResource audioFile = new FileSystemResource(tempFile);
        // Combine both resource to be sent to AI model
        AudioTranscriptionPrompt transcriptionRequest = new AudioTranscriptionPrompt(audioFile, transcriptionOptions);
        AudioTranscriptionResponse transcriptionResponse = transcriptionModel.call(transcriptionRequest);
        tempFile.delete();
        return new ResponseEntity<>(transcriptionResponse.getResult().getOutput(), HttpStatus.OK);
    }

}
